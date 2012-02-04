package kvs.core.fileformat.kvsml;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.StringTokenizer;

import kvs.core.KVSException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public final class TagParser {

    public enum WritingDataType {
        Ascii, ExternalAscii, ExternalBinary
    };
    
    private static File currentFile;

    public enum DataType{
        Byte(1),
        Short(2),
        Int(4),
        Long(8),
        Float(4),
        Double(8);

        private DataType( int value ){
            m_value = value;
        }

        private final int m_value;

        public int size(){
            return m_value;
        }

        public static DataType getType( String type ){
            if( type.equals( "char" )
                    || type.equals( "unsigned char" )
                    || type.equals( "uchar" ) )
            {
                return DataType.Byte;
            }
            else if( type.equals( "short" )
                    || type.equals( "unsigned short" )
                    || type.equals( "ushort" ) )
            {
                return DataType.Short;
            }
            else if( type.equals( "int" )
                    || type.equals( "unsigned int" )
                    || type.equals( "uint" ) )
            {
                return DataType.Int;
            }
            else if( type.equals( "float" ) )
            {
                return DataType.Float;
            }
            else if( type.equals( "double" ) )
            {
                return DataType.Double;
            }

            return null;
        }
    }

    private TagParser(){}

    public static Node findNode( final Document document, final String name )
    {
        Node node = document.getFirstChild();
        while( node != null )
        {
            if( node.getNodeName().equals( name ) ){
                return( node );
            }

            node = node.getNextSibling();
        }

        return null ;
    }

    public static Node findChildNode( Node node, final String name )
    {
        Node child_node = node.getFirstChild();
        while( child_node != null )
        {
            if( child_node.getNodeName().equals( name ) ){
                return( child_node );
            }

            child_node = child_node.getNextSibling();
        }

        return null;
    }

    public static Node nodeToText( final Node node )
    {
        Element e = (Element)node;
        Node    n = e.getFirstChild();

        return n;
    }

    public static void parseValue( final Node value_node, FloatBuffer data_array ) throws KVSException
    {
        Node value_text = value_node.getFirstChild();
        if( value_text == null )
        {
            throw new KVSException("No value in <Value>");
        }

        final String delim = " \t\n";
        StringTokenizer t = new StringTokenizer( value_text.getNodeValue(), delim );

        final int nloops = data_array.limit();
        for( int i = 0; i < nloops; i++ )
        {
            data_array.put( i, Double.valueOf( t.nextToken() ).floatValue() );
        }
    }

    public static void parseValue( final Node value_node, ByteBuffer data_array ) throws KVSException
    {
        Node value_text = value_node.getFirstChild();
        if( value_text == null )
        {
            throw new KVSException("No value in <Value>");
        }

        final String delim = " \n";
        StringTokenizer t = new StringTokenizer( value_text.getNodeValue(), delim );

        final int nloops = data_array.limit();
        for( int i = 0; i < nloops; i++ )
        {
            data_array.put( i, Double.valueOf( t.nextToken() ).byteValue() );
        }
    }

    public static void parseArray( final Node array_node, Buffer data_array ) throws KVSException
    {
        Element array_element = (Element)array_node;
        String file = array_element.getAttribute( "file" );
        if( file.equals( "" ) )
        {
            // <DataArray>xxx</DataArray>
            Node array_text = TagParser.nodeToText( array_node );
            if( array_text == null )
            {
                throw new KVSException("No value in <DataArray>");
            }

            final String delim = " \t\n";
            StringTokenizer t = new StringTokenizer( array_text.getNodeValue(), delim );

            final int nloops = data_array.limit();
            if( data_array instanceof ByteBuffer ){
                ByteBuffer byte_array = (ByteBuffer)data_array;
                for( int i = 0; i < nloops; i++ )
                {
                    byte_array.put( i, Double.valueOf( t.nextToken() ).byteValue() );
                }
            } else if( data_array instanceof FloatBuffer ){
                FloatBuffer float_array = (FloatBuffer)data_array;
                for( int i = 0; i < nloops; i++ )
                {
                    float_array.put( i, Double.valueOf( t.nextToken() ).floatValue() );
                }
            } else if ( data_array instanceof IntBuffer ){
                IntBuffer int_array = (IntBuffer)data_array;
                for( int i = 0; i < nloops; i++ )
                {
                    int_array.put( i, Double.valueOf( t.nextToken() ).intValue() );
                }
            } else {
                throw new KVSException("'unsupport buffer type'");
            }
        }
        else
        {
            // <DataArray file="xxx" type="xxx" format="xxx"/>
            String format = array_element.getAttribute( "format" );
            if( format.equals( "" ) )
            {
                throw new KVSException("'format' is not specified in <DataArray/>.");
            }

            String type = array_element.getAttribute( "type" );
            if( type.equals( "" ) )
            {
                throw new KVSException("'type' is not specified in <DataArray/>.");
            }

            DataType datatype = DataType.getType( type );

            if( datatype == null ){
                throw new KVSException("Unknown 'type' in <DataArray>.");
            }

            try{
            if( data_array instanceof ByteBuffer ){
                ByteBuffer byte_array = (ByteBuffer)data_array;
                TagParser.readArray( file, format, byte_array, datatype );
            } else if( data_array instanceof IntBuffer ){
                IntBuffer int_array = (IntBuffer)data_array;
                TagParser.readArray( file, format, int_array, datatype );
            } else if ( data_array instanceof FloatBuffer ){
                FloatBuffer float_array = (FloatBuffer)data_array;
                TagParser.readArray( file, format, float_array, datatype );

            }
            } catch( IOException ex ){
                throw new KVSException( ex );
            }
        }
    }

    public static void readArray( final String   file,
                                  final String   format,
                                  ByteBuffer data_array,
                                  DataType type ) throws IOException, KVSException
                                  {
        File filename = new File( currentFile.getParent(), file ); 
        if( format.equals( "binary" ) )
        {
            InputStream in = new BufferedInputStream( new FileInputStream( filename ) );

            byte[] byteData = new byte[ data_array.limit() * type.size() ];
            in.read( byteData );
            ByteBuffer buf = ByteBuffer.wrap( byteData );
            final int nloops = data_array.limit();

            switch( type ){
            case Byte:

                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, buf.get( i ) );
                }
                break;
            case Short:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (byte)buf.getShort( i ) );
                }
                break;
            case Int:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (byte)buf.getInt( i ) );
                }
                break;
            case Long:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (byte)buf.getLong( i ) );
                }
                break;
            case Float:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (byte)buf.getFloat( i ) );
                }
                break;
            case Double:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (byte)buf.getDouble( i ) );
                }
                break;
            }

            in.close();
        }
        else if( format.equals( "ascii" ) )
        {
            BufferedReader br = new BufferedReader( new FileReader( filename ) );

            final String delim = " \t\n";

            StringBuilder buffer = new StringBuilder();

            while( br.ready() ){
                buffer.append( br.readLine() );
            }

            StringTokenizer t = new StringTokenizer( buffer.toString() , delim );

            data_array.clear();
            while( t.hasMoreTokens() ){
                data_array.put( Double.valueOf( t.nextToken() ).byteValue() );
            }

            br.close();
        }
        else
        {
            throw new KVSException("Unknown format '" +format+ "'.");
        }

                                  }

    public static void readArray( final String   file,
                                  final String   format,
                                  IntBuffer data_array,
                                  DataType type ) throws IOException, KVSException
                                  {
        File filename = new File( currentFile.getParent(), file ); 

        if( format.equals( "binary" ) )
        {
            InputStream in = new BufferedInputStream( new FileInputStream( filename ) );

            byte[] byteData = new byte[ data_array.limit() * type.size() ];
            in.read( byteData );
            ByteBuffer buf = ByteBuffer.wrap( byteData );
            final int nloops = data_array.limit();

            switch( type ){
            case Byte:

                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (int)buf.get( i ) );
                }
                break;
            case Short:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (int)buf.getShort( i ) );
                }
                break;
            case Int:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, buf.getInt( i ) );
                }
                break;
            case Long:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (int)buf.getLong( i ) );
                }
                break;
            case Float:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (int)buf.getFloat( i ) );
                }
                break;
            case Double:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (int)buf.getDouble( i ) );
                }
                break;
            }

            in.close();
        }
        else if( format.equals( "ascii" ) )
        {
            BufferedReader br = new BufferedReader( new FileReader( filename ) );

            final String delim = " \t\n";

            StringBuilder buffer = new StringBuilder();

            while( br.ready() ){
                buffer.append( br.readLine() );
            }

            StringTokenizer t = new StringTokenizer( buffer.toString() , delim );

            data_array.clear();
            while( t.hasMoreTokens() ){
                data_array.put( Double.valueOf( t.nextToken() ).intValue() );
            }

            br.close();
        }
        else
        {
            throw new KVSException("Unknown format '" +format+ "'.");
        }

                                  }

    public static void readArray( final String   file,
                                  final String   format,
                                  FloatBuffer data_array,
                                  DataType type ) throws IOException, KVSException
                                  {
        File filename = new File( currentFile.getParent(), file ); 
        if( format.equals( "binary" ) )
        {
            InputStream in = new BufferedInputStream( new FileInputStream( filename ) );

            byte[] byteData = new byte[ data_array.limit() * type.size() ];
            in.read( byteData );
            ByteBuffer buf = ByteBuffer.wrap( byteData );
            final int nloops = data_array.limit();

            switch( type ){
            case Byte:

                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (float)buf.get( i ) );
                }
                break;
            case Short:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (float)buf.getShort( i ) );
                }
                break;
            case Int:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (float)buf.getInt( i ) );
                }
                break;
            case Long:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (float)buf.getLong( i ) );
                }
                break;
            case Float:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, buf.getFloat( i ) );
                }
                break;
            case Double:
                for( int i = 0; i < nloops; i++ )
                {
                    data_array.put( i, (float)buf.getDouble( i ) );
                }
                break;
            }

            in.close();
        }
        else if( format.equals( "ascii" ) )
        {
            BufferedReader br = new BufferedReader( new FileReader( filename ) );

            final String delim = " \t\n";

            StringBuilder buffer = new StringBuilder();

            while( br.ready() ){
                buffer.append( br.readLine() );
            }

            StringTokenizer t = new StringTokenizer( buffer.toString() , delim );

            data_array.clear();
            while( t.hasMoreTokens() ){
                data_array.put( Double.valueOf( t.nextToken() ).floatValue() );
            }

            br.close();
        }
        else
        {
            throw new KVSException("Unknown format '" +format+ "'.");
        }

                                  }

    public static String getDataFormat( final WritingDataType writing_type )
    {
        switch ( writing_type )
        {
        case Ascii:
        case ExternalAscii:
            return("ascii");
        case ExternalBinary:
            return("binary");
        default:
            return("unknown");
        }
    }

    public static File getDataFilename( final File filename, final String type )
    {
        String  name = filename.getName();
        String  basename = name.substring( 0, name.indexOf( '.' ) );
        File    pathname = filename.getParentFile();
        String  ext = "dat";

        return( new File ( pathname, basename + "_" + type + "." + ext ) );
    }

    public static String getDataType( final Buffer data_array )
    {
        if (      data_array instanceof ByteBuffer   ) return("char");
        //else if ( typeid(T) == typeid(kvs::UInt8)  ) return("uchar");
        else if ( data_array instanceof ShortBuffer  ) return("short");
        //else if ( typeid(T) == typeid(kvs::UInt16) ) return("ushort");
        else if ( data_array instanceof IntBuffer  ) return("int");
        //else if ( typeid(T) == typeid(kvs::UInt32) ) return("uint");
        else if ( data_array instanceof LongBuffer  ) return("long");
        //else if ( typeid(T) == typeid(kvs::UInt64) ) return("ulong");
        else if ( data_array instanceof FloatBuffer ) return("float");
        else if ( data_array instanceof DoubleBuffer ) return("double");
        else return("unknown");
    }

    public static boolean composeArray(
                                       Node                  parent_node,
                                       final WritingDataType writing_type,
                                       final Buffer          data_array,
                                       final int             veclen,
                                       final File            file )
    {
        if ( data_array.limit() == 0 ) return( true );

        Document doc = parent_node.getOwnerDocument();

        // <DataValue> : In the case of one component.
        if ( data_array.limit() == veclen )
        {
            // <DataValue>xxx</DataValue>
            Element data_elem = doc.createElement("DataValue");
            Node data_node = parent_node.appendChild( data_elem );

            // Write the data array to string-stream.
            String data = dataArrayToString( data_array, veclen );

            // Insert the data array as string-stream to the parent node.
            Text data_text = doc.createTextNode( data );

            return( data_node.appendChild( data_text ) != null );
        }

        // <DataArray>
        else
        {
            // <DataArray>xxx</DataArray>
            Element data_elem = doc.createElement("DataArray");
            if ( writing_type == WritingDataType.Ascii )
            {
                // Write the data array to string-stream.
                final int data_size = data_array.limit();
                String data = dataArrayToString( data_array, data_size );


                // Insert the data array as string-stream to the parent node.
                Text data_text = doc.createTextNode( data );

                Node data_node = parent_node.appendChild( data_elem );
                return( data_node.appendChild( data_text ) != null );
            }
            // <DataArray type="float" format="xxx" file="xxx"/>
            else
            {
                // Type name of the data array.
                final String type = getDataType( data_array );
                final String format = getDataFormat( writing_type );

                // Set attributes.
                data_elem.setAttribute( "type", type );
                data_elem.setAttribute( "format", format );
                data_elem.setAttribute( "file", file.getPath() );   //TODO 確認
                parent_node.appendChild( data_elem );

                // Set text.
                try {
                    writeArray( writing_type, data_array, type, file );
                } catch (KVSException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }

        return true;
    }

    public static void writeArray( final WritingDataType   writing_type,
                                   final Buffer            data_array,
                                   final String            data_type,
                                   final File              filename ) throws KVSException
    {
        try {
            if ( writing_type == WritingDataType.ExternalAscii )
            {
                FileWriter fw = new FileWriter( filename );
                BufferedWriter bw = new BufferedWriter( fw );

                final int data_size = data_array.limit();
                String data = dataArrayToString( data_array, data_size );

                bw.write( data );

                bw.close();
                fw.close();
            }
            else if ( writing_type == WritingDataType.ExternalBinary )
            {
                FileOutputStream fos = new FileOutputStream( filename );

                final int data_size = data_array.limit();
                byte[] data = dataArrayToString( data_array, data_size ).getBytes();
                fos.write( data );

                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new KVSException("Cannot write file '" +filename.getName()+ "'." );
        }
    }


    private static String dataArrayToString( final Buffer data_array, final int size ){
        StringBuilder builder = new StringBuilder();
        if( data_array instanceof ByteBuffer ){
            ByteBuffer array = (ByteBuffer)data_array;
            for ( int i = 0; i < size; i++ ){
                builder.append( array.get( i ) & 0xFF );    //unsigned
                builder.append( " " );
            }
        } else if( data_array instanceof ShortBuffer ){
            ShortBuffer array = (ShortBuffer)data_array;
            for ( int i = 0; i < size; i++ ){
                builder.append( (int)array.get( i ) );
                builder.append( " " );
            }
        } else if( data_array instanceof IntBuffer ){
            IntBuffer array = (IntBuffer)data_array;
            for ( int i = 0; i < size; i++ ){
                builder.append( array.get( i ) );
                builder.append( " " );
            }
        } else if( data_array instanceof LongBuffer ){
            LongBuffer array = (LongBuffer)data_array;
            for ( int i = 0; i < size; i++ ){
                builder.append( array.get( i ) );
                builder.append( " " );
            }
        } else if( data_array instanceof FloatBuffer ){
            FloatBuffer array = (FloatBuffer)data_array;
            for ( int i = 0; i < size; i++ ){
                builder.append( array.get( i ) );
                builder.append( " " );
            }
        } else if( data_array instanceof DoubleBuffer ){
            DoubleBuffer array = (DoubleBuffer)data_array;
            for ( int i = 0; i < size; i++ ){
                builder.append( array.get( i ) );
                builder.append( " " );
            }
        }
        return builder.toString();
    }
    
    public static void setCurrentFile( File file ){
        currentFile = file;
    }
    
    public static File currentFile(){
        return currentFile;
    }
}
