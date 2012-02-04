package kvs.core.fileformat.avsfield;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;
import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector3i;
import kvs.core.util.Utility;
import kvs.core.util.Version;

/**
 * AVSFieldクラスAVSFieldファイルフォーマットのデータを保持するクラスです。
 * AVSFieldファイルの読み込み、書き出しをサポートします。
 * 
 */

public class AVSField extends FileFormatBase{

    //private static final int MaxLineLength = 256;

    private static final long serialVersionUID = -4990211954656898214L;

    public enum FieldType
    {
        UnknownFieldType("unknown"),   // unknown field type
        Uniform("uniform"),            // uniform grid type
        Rectilinear("rectilinear"),        // rectilinear grid type
        Irregular("irregular");          // irregular grid type

        private FieldType( String value ){
            m_value = value;
        }

        private final String m_value;

        @Override
        public String toString(){
            return m_value;
        }
    };

    public enum DataType
    {
        UnknownDataType("unknown"),    // unknown data type
        Byte("byte"),               // byte (unsigned char) data type
        Short("short"),              // short data type
        Integer("integer"),            // ineger data type
        Float("float"),              // float data type
        Double("double");             // double data type

        private DataType( String value ){
            m_value = value;
        }

        private final String m_value;

        public String toString(){
            return m_value;
        }
    }

    protected int                   m_bits;            ///< bits per voxel
    protected boolean               m_is_signed;       ///< signed voxel [true/false]
    protected int                   m_veclen;          ///< vector length
    protected int                   m_nspace;          ///< number of spaces
    protected int                   m_ndim;            ///< number of dimensions
    protected Vector3i              m_dim;             ///< dimention
    protected Vector3f              m_min_ext;         ///< minimum external coordinate value
    protected Vector3f              m_max_ext;         ///< maximum external coordinate value
    protected boolean               m_has_min_max_ext; ///< has min max external coord
    protected FieldType             m_field;           ///< field data type
    protected DataType              m_type;            ///< data type
    protected List<String>          m_labels;          ///< label
    protected ByteBuffer            m_values;          ///< field value array (shared array)
    protected ByteBuffer            m_coords;          ///< coordinate value array (shared array)

    public AVSField()
    {
        this.initialize();
    }

    public AVSField( final File filename ) throws KVSException
    {
        this.initialize();
        this.read( filename );
    }

    public void initialize()
    {
        m_bits              = 0;
        m_is_signed         = true;
        m_veclen            = 1;
        m_nspace            = 0;
        m_ndim              = 0;
        m_dim               = new Vector3i( 1, 1, 1 );
        m_min_ext           = Vector3f.ZERO;
        m_max_ext           = new Vector3f( 1, 1, 1 );
        m_has_min_max_ext   = false;
        m_field             = FieldType.Uniform;
        m_type              = DataType.UnknownDataType;
        m_labels            = new ArrayList<String>();
    }

    public void clear()
    {
        m_values = null;
        m_coords = null;
    }

    public int bits()
    {
        return( m_bits );
    }

    public boolean isSigned()
    {
        return( m_is_signed );
    }

    public int veclen()
    {
        return( m_veclen );
    }

    public int nspace()
    {
        return( m_nspace );
    }

    public int ndim()
    {
        return( m_ndim );
    }

    public Vector3i dim()
    {
        return( m_dim );
    }

    public Vector3f minExt()
    {
        return( m_min_ext );
    }

    public Vector3f maxExt()
    {
        return( m_max_ext );
    }

    public boolean hasMinMaxExt()
    {
        return( m_has_min_max_ext );
    }

    public String field()
    {
        return( m_field.toString() );
    }

    public String type()
    {
        return( m_type.toString() );
    }

    public FieldType fieldType()
    {
        return( m_field );
    }

    public DataType dataType()
    {
        return( m_type );
    }

    public List<String> labels()
    {
        return( m_labels );
    }

    public Buffer values()
    {
        switch( m_type )
        {
        //case Byte:      return m_values;
        case Byte:
            //符号なしに変換する
            int limit = m_values.limit();
            IntBuffer buf = IntBuffer.allocate( limit );
            m_values.clear();
            while(m_values.position() < limit){
                byte b = m_values.get();
                buf.put( b & 0xFF );
            }
            buf.clear();
            return buf;
        case Short:     return m_values.asShortBuffer();
        case Integer:   return m_values.asIntBuffer();
        case Float:     return m_values.asFloatBuffer();
        case Double:    return m_values.asDoubleBuffer();
        default:        return null;
        }
    }

    public Buffer coords()
    {
        return( m_coords );
    }

    public void setBits( final int bits )
    {
        m_bits = bits;
    }

    public void setSigned( final boolean sign )
    {
        m_is_signed = sign;
    }

    public void setVeclen( final int veclen )
    {
        m_veclen = veclen;
    }

    public void setNSpace( final int nspace )
    {
        m_nspace = nspace;
    }

    public void setNDim( final int ndim )
    {
        m_ndim = ndim;
    }

    public void setDim( final Vector3i dim )
    {
        m_dim = dim;
    }

    public void setMinExt( final Vector3f m )
    {
        m_has_min_max_ext = true;
        m_min_ext = m;
    }

    public void setMaxExt( final Vector3f m )
    {
        m_has_min_max_ext = true;
        m_max_ext = m;
    }

    public void setFieldType( final FieldType field )
    {
        m_field = field;
    }

    public void setDataType( final DataType type )
    {
        m_type = type;
    }

    public void setLabels( final LinkedList<String> labels )
    {
        m_labels.clear();
        for( int i = 0; i < labels.size(); i++ )
        {
            m_labels.add( labels.get( i ) );
        }
    }

    public void setValues( final ByteBuffer values )
    {
        m_values = values;
    }

    public void setCoords( final ByteBuffer coords )
    {
        m_coords = coords;
    }


    @Override
    public void read( File filename ) throws KVSException {
        m_filename = filename;

        try{
            System.out.println( "AVSField load... " );
            long s = System.currentTimeMillis();

            RandomAccessFile raf = new RandomAccessFile( filename, "r");
            //FileChannel channel = raf.getChannel();
            if( !read_header( raf ) ){
                raf.close();
            }
            if( !read_node( raf ) ){
                raf.close();
            }

            raf.close();

            long e = System.currentTimeMillis();
            System.out.println( (e-s)+"ms" );

        } catch (IOException e) {
            e.printStackTrace();
            throw new KVSException("file read failured");
        }

    }

    private boolean read_header( RandomAccessFile raf )
    {


        if( !check_header( raf ) ){
            return( false );
        }

 


        String buf;

        try {
            while( ( buf = Utility.fgets( raf, 256 ) ) != null )
            {

                if( buf.startsWith( "#" ) )   continue; // skip comment line
                if( buf.startsWith( "\f" ) ) break;    // detected data separator

                StringTokenizer token = new StringTokenizer( buf, " ,\t\n" );

                String tag   = token.nextToken();
                String equal = token.nextToken();
                String value = token.nextToken();

                if( !equal.equals( "=" ) ) continue;

                if( tag.equals( "veclen" ) ) m_veclen  = Integer.valueOf( value );
                if( tag.equals( "nspace" ) ) m_nspace  = Integer.valueOf( value );
                if( tag.equals( "ndim"   ) ) m_ndim    = Integer.valueOf( value );
                int x = m_dim.getX(), y = m_dim.getY() ,z = m_dim.getZ();
                if( tag.equals( "dim1"   ) ) x = Integer.valueOf( value );
                if( tag.equals( "dim2"   ) ) y = Integer.valueOf( value );
                if( tag.equals( "dim3"   ) ) z = Integer.valueOf( value );
                m_dim = new Vector3i( x, y, z);

                if( tag.equals( "field"  ) )
                {
                    m_field =
                        value.equals( "uniform"    ) ? FieldType.Uniform :
                            value.equals( "rectilinear") ? FieldType.Rectilinear :
                                value.equals( "irregular"  ) ? FieldType.Irregular :
                                    FieldType.UnknownFieldType;
                }

                if( tag.equals( "data"  ) )
                {
                    m_type =
                        value.equals( "byte"     ) ? DataType.Byte :
                            value.equals( "short"    ) ? DataType.Short :
                                value.equals( "integer"  ) ? DataType.Integer :
                                    value.equals( "float"    ) ? DataType.Float :
                                        value.equals( "double"   ) ? DataType.Double :
                                            DataType.UnknownDataType;
                }

                if( tag.equals( "label" ) )
                {
                    m_labels.clear();
                    while( token.hasMoreTokens() )
                    {
                        m_labels.add( value );
                        value = token.nextToken();
                    }
                }

                if( tag.equals( "min_ext" ) )
                {

                    float m_min_ext_x = Float.valueOf( value ); value = token.nextToken();
                    float m_min_ext_y = Float.valueOf( value ); value = token.nextToken();
                    float m_min_ext_z = Float.valueOf( value );
                    m_min_ext = new Vector3f( m_min_ext_x, m_min_ext_y, m_min_ext_z );

                    m_has_min_max_ext = true;
                }

                if( tag.equals( "max_ext" ) )
                {
                    float m_max_ext_x = Float.valueOf( value ); value = token.nextToken();
                    float m_max_ext_y = Float.valueOf( value ); value = token.nextToken();
                    float m_max_ext_z = Float.valueOf( value );
                    m_max_ext = new Vector3f( m_max_ext_x, m_max_ext_y, m_max_ext_z );

                    m_has_min_max_ext = true;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        read_comment( raf );

        return( true );
    }

    private boolean read_comment( RandomAccessFile raf )
    {
        // Go back file-pointer to head.
        try {
            raf.seek( 0 );


            // Set initial value.
            switch( m_type )
            {
            case Byte:
            {
                m_bits = 1 << 3;
                m_is_signed = false;
                //m_is_signed = true;
                break;
            }
            case Short:
            {
                m_bits = 2 << 3;
                m_is_signed = true;
                break;
            }
            case Integer:
            {
                m_bits = 4 << 3;
                m_is_signed = true;
                break;
            }
            case Float:
            {
                m_bits = 4 << 3;
                m_is_signed = true;
                break;
            }
            case Double:
            {
                m_bits = 8 << 3;
                m_is_signed = true;
                break;
            }
            default: break;
            }

            // Read comment.
            String buf;
            while( (buf = Utility.fgets( raf, 256 )) != null )
            {
                if( buf.startsWith( "\f" ) ) break; // detected data separator
                if( buf.startsWith( "#"  )){
                    if( buf.length() < 2   ) continue;
                    if( buf.indexOf( '=' ) < 0 ) continue;

                    buf = buf.substring( 1 );
                    StringTokenizer token = new StringTokenizer( buf, " ,\t\n" );

                    String tag   = token.nextToken();
                    String equal = token.nextToken();
                    String value = token.nextToken();
                    if( !equal.equals( "=" ) ) continue;

                    if( tag.equals( "bits"  ) ){
                        m_bits      = Integer.valueOf( value );
                    }
                    if( tag.equals( "signed" ) ){
                        m_is_signed = value.equals( "signed" );
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return( true );
    }


    private boolean read_node( RandomAccessFile raf )
    {
        try {
            skip_header( raf );

            int nelems = m_dim.getX() * m_dim.getY() * m_dim.getZ() * m_veclen;
            switch( m_type )
            {
            case Byte:    m_values = ByteBuffer.allocateDirect( nelems );      break;
            case Short:   m_values = ByteBuffer.allocateDirect( 2 * nelems );  break;
            case Integer: m_values = ByteBuffer.allocateDirect( 4 * nelems );  break;
            case Float:   m_values = ByteBuffer.allocateDirect( 4 * nelems );  break;
            case Double:  m_values = ByteBuffer.allocateDirect( 8 * nelems );  break;
            default: break;
            }
            //fread( m_values.pointer(), 1, m_values.byteSize(), ifs );

            long s = System.currentTimeMillis();
            //raf.read( m_values.array() );
            raf.getChannel().read( m_values );


            long e = System.currentTimeMillis();
            System.out.println( "read node " + (e-s)+"ms" );
            
            //#if defined ( KVS_PLATFORM_BIG_ENDIAN )
            //    m_values.swapByte();
            //#endif
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return( true );
    }

    private boolean check_header( RandomAccessFile raf )
    {
        try {
            String buf = raf.readLine();
            return buf.startsWith( "# AVS" );
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void skip_header( RandomAccessFile raf )
    {
        // Go back file-pointer to head.
        try {
            raf.seek( 0 );


            // Search the data separator '\f\f'.
            byte c;
            while( true )
            {
                try{
                    c = raf.readByte();
                    if( c == '\f' ){
                        if( raf.readByte() == '\f' ){
                            break;
                        }
                    }
                } catch ( EOFException e ){
                    break;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void write( File filename ) throws KVSException {
        m_filename = filename;
        try{
            FileWriter fw = new FileWriter( filename );
            BufferedWriter bw = new BufferedWriter( fw );

            if( !write_header( bw ) ){
                bw.close();
                fw.close();
            }
            bw.close();
            fw.close();

            FileOutputStream fos = new FileOutputStream( filename, true ); 
            BufferedOutputStream bos = new BufferedOutputStream( fos );

            write_node( bos );

            bos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new KVSException("file write failured");
        }



    };

    private boolean write_header( BufferedWriter bw ) throws IOException
    {
        // Output a necessary line.
        String version = Version.KVS_NAME;

        bw.write( "# AVS field generated by KVS (" + version + ")" );
        bw.newLine();
        bw.write( "#" );
        bw.newLine();

        String author = System.getProperty( "user.name" );

        Date time = new Date();

        bw.write( "# Author: " + author );
        bw.newLine();
        bw.write( "# Date:   " + time.toString() );
        bw.newLine();
        bw.write( "#" );
        bw.newLine();

        // Output extended header information.
        bw.write( "# bits = " + m_bits );
        bw.newLine();
        bw.write( "# signed = " + ( m_is_signed ? "signed" : "unsigned" ) );
        bw.newLine();
        bw.write( "#" );
        bw.newLine();

        // Output header information.
        bw.write( "nspace = " + m_nspace );
        bw.newLine();
        bw.write( "veclen = " + m_veclen );
        bw.newLine();
        bw.write( "ndim   = " + m_ndim );
        bw.newLine();
        bw.write( "dim1   = " + m_dim.getX() );
        bw.newLine();
        if( m_ndim >= 2 ){
            bw.write( "dim2   = " + m_dim.getY() );
            bw.newLine();
        }
        if( m_ndim >= 3 ){
            bw.write( "dim3   = " + m_dim.getZ() );
            bw.newLine();
        }

        bw.write( "data   = " + m_type.toString() );
        bw.newLine();
        bw.write( "field  = " + m_field.toString() );
        bw.newLine();

        if( m_has_min_max_ext )
        {
            bw.write( "min_ext = " + 
                    m_min_ext.getX() + " " +
                    m_min_ext.getY() + " " +
                    m_min_ext.getZ());
            bw.newLine();
            bw.write( "max_ext = " + 
                    m_max_ext.getX() + " " +
                    m_max_ext.getY() + " " +
                    m_max_ext.getZ());
            bw.newLine();
        }
        if( m_labels.size() != 0 )
        {
            bw.write( "label  = " );
            for( int i = 0; i < m_labels.size(); i++ )
            {
                bw.write( m_labels.get( i ) + " " );
            }
            bw.newLine();
        }

        return( true );
    }

    private void write_node( BufferedOutputStream bos ) throws IOException
    {
        bos.write( '\f' );
        bos.write( '\f' );

        bos.write(m_values.array());

    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String crlf = System.getProperty( "line.separator" );
        sb.append( "bits    : " );
        sb.append( m_bits );
        sb.append( crlf );
        sb.append( "signed  : " );
        sb.append( m_is_signed ? "signed" : "unsigned" );
        sb.append( crlf );
        sb.append( "veclen  : " );
        sb.append( m_veclen );
        sb.append( crlf );
        sb.append( "nspace  : " );
        sb.append( m_nspace );
        sb.append( crlf );
        sb.append( "ndim    : " );
        sb.append( m_ndim );
        sb.append( crlf );
        sb.append( "dim     : " );
        sb.append( m_dim );
        sb.append( crlf );
        sb.append( "min ext : " );
        sb.append( m_min_ext );
        sb.append( crlf );
        sb.append( "max ext : " );
        sb.append( m_max_ext );
        sb.append( crlf );
        sb.append( "data    : " );
        sb.append( m_type.toString() );
        sb.append( crlf );
        sb.append( "field   : " );
        sb.append( m_field.toString() );
        sb.append( crlf );
        sb.append( "label   : " );
        for( int i = 0; i < m_labels.size(); i++ )
        {
            sb.append( m_labels.get( i ) );
            sb.append( " " );
        }
        sb.append( crlf );

        return( sb.toString() );
    }

}
