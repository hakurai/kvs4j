package kvs.core.fileformat.avsucd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.StringTokenizer;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;
import kvs.core.util.Utility;

public class AVSUcd extends FileFormatBase {

    private static final long serialVersionUID = 7354823378212582913L;
    private final static String Delimiter = " \n\r";

    private final static int MAX_LENGTH = 512;

    public enum FormatType
    {
        FormatTypeUnknown, ///< Unknown format type.
        SingleStep,            ///< Single step format type ( for example tet.inp ).
        MultiStep;             ///< Multi step format type ( for example ALL.inp ).

    }

    public enum CycleType
    {
        CycleTypeUnknown( "unknown" ), ///< Unknown cycle type.
        Data( "data" ),                 ///< Only data changes at each step.
        Geom( "geometry" ),                 ///< Only geometry changes at each step.
        DataGeom( "data_geometry" );             ///< Both data and geometry changes at each step.

        private String m_value;

        private CycleType( String value ){
            m_value = value;
        }

        @Override
        public String toString(){
            return m_value;
        }
    }

    public enum ElementType
    {
        ElementTypeUnknown( "unknown" ), ///< Unknown element type.
        Point( "point" ),                  ///< Point.
        Line( "line" ),                   ///< Line.
        Triangle( "triangle" ),               ///< Triangle.
        Quadrangle( "quadrangle" ),             ///< Quadrangle.
        Tetrahedra( "tetrahedra" ),             ///< Tetrahedra.
        Tetrahedra2( "tetrahedra2" ),            ///< Quadratic tetrahedra.
        Pyramid( "pyramid" ),                ///< Pyramid.
        Prism( "prism" ),                  ///< Prism.
        Hexahedra( "hexahedra" ),              ///< Hexahedra.
        Hexahedra2( "hexahedra2" );             ///< Quadratic hexahedra.

        private String m_value;

        private ElementType( String value ){
            m_value = value;
        }

        @Override
        public String toString(){
            return m_value;
        }
    }

    private int                 m_nsteps = 0;       ///< Number of steps.
    private CycleType           m_cycle_type = CycleType.CycleTypeUnknown ;   ///< Cycle type.
    private ElementType         m_element_type = ElementType.ElementTypeUnknown; ///< Element type.

    private int                 m_step_id = 0;      ///< Step ID.
    private String              m_step_comment = ""; ///< Comment of step.

    private int                 m_nnodes = 0;               ///< Number of nodes.
    private int                 m_nelements = 0;            ///< Number of elements.
    private int                 m_nvalues_per_node = 0;     ///< Number of values per node.
    private int                 m_ncomponents_per_node = 0; ///< Number of components per node.
    private LinkedList<Integer> m_veclens = new LinkedList<Integer>();              ///< Veclens of each component.
    private LinkedList<String>  m_component_names = new LinkedList<String>();       ///< Names of each component.
    private LinkedList<String>  m_component_units = new LinkedList<String>();       ///< Units of each component.

    private int                 m_component_id = 0; ///< Component ID.

    private float[]             m_coords;
    private int[]               m_connections;
    private float[]             m_values;

    public AVSUcd(){
        super();
    }

    public AVSUcd(final File  filename,
                  final int     step_id,
                  final int     component_id ) throws IOException, KVSException
                  {
        super();
        m_step_id = step_id;
        m_component_id = component_id;
        this.read( filename );
                  }

    public int nsteps()
    {
        return( m_nsteps );
    }

    public CycleType cycleType()
    {
        return( m_cycle_type );
    }

    public ElementType elementType()
    {
        return( m_element_type );
    }

    public int stepID()
    {
        return( m_step_id );
    }

    public String stepComment()
    {
        return( m_step_comment );
    }

    public int nnodes()
    {
        return( m_nnodes );
    }

    public int nelements()
    {
        return( m_nelements );
    }

    public int nvaluesPerNode()
    {
        return( m_nvalues_per_node );
    }

    public int ncomponentsPerNode()
    {
        return( m_ncomponents_per_node );
    }

    public LinkedList<Integer> veclens()
    {
        return( m_veclens );
    }

    public LinkedList<String> componentNames()
    {
        return( m_component_names );
    }

    public LinkedList<String> componentUnits()
    {
        return( m_component_units );
    }

    public int componentID()
    {
        return( m_component_id );
    }

    public String componentName()
    {
        return( m_component_names.get( m_component_id ) );
    }

    public String componentUnit()
    {
        return( m_component_units.get( m_component_id ) );
    }

    public float[] coords()
    {
        return( m_coords );
    }

    public int[] connections()
    {
        return( m_connections );
    }

    public float[] values()
    {
        return( m_values );
    }

    public void setNSteps( final int nsteps )
    {
        m_nsteps = nsteps;
    }

    public void setCycleType( final CycleType cycle_type )
    {
        m_cycle_type = cycle_type;
    }

    public void setElementType( final ElementType element_type )
    {
        m_element_type = element_type;
    }

    public void setStepID( final int step_id )
    {
        m_step_id = step_id;
    }

    public void setStepComment( final String step_comment )
    {
        m_step_comment = step_comment;
    }

    public void setNNodes( final int nnodes )
    {
        m_nnodes = nnodes;
    }

    public void setNElements( final int nelements )
    {
        m_nelements = nelements;
    }

    public void setNValuesPerNode( final int nvalues_per_node )
    {
        m_nvalues_per_node = nvalues_per_node;
    }

    public void setNComponentsPerNode( final int ncomponents_per_node )
    {
        m_ncomponents_per_node = ncomponents_per_node;
    }

    public void setVeclens( final LinkedList<Integer> veclens )
    {
        m_veclens = veclens;
    }

    public void setComponentNames( final LinkedList<String> component_names )
    {
        m_component_names.clear();

        final int size = component_names.size();
        for ( int i = 0; i < size; ++i )
        {
            m_component_names.add( component_names.get( i ) );
        }
    }

    public void setComponentUnits( final LinkedList<String> component_units )
    {
        m_component_units.clear();

        final int size = component_units.size();
        for ( int i = 0; i < size; ++i )
        {
            m_component_units.add( component_units.get( i ) );
        }
    }

    public void setComponentID( final int component_id )
    {
        m_component_id = component_id;
    }

    public void setCoords( final float[] coords )
    {
        m_coords = coords;
    }

    public void setConnections( final int[] connections )
    {
        m_connections = connections;
    }

    public void setValues( final float[] values )
    {
        m_values = values;
    }

    @Override
    public void read( File filename ) throws KVSException {
        m_filename = filename;
        try {
            RandomAccessFile raf = new RandomAccessFile( filename, "r");


            final FormatType format_type = check_format_type( raf );
            if ( format_type == FormatType.SingleStep )
            {
                this.read_single_step_format( raf );
            }
            else if ( format_type == FormatType.MultiStep )
            {
                this.read_multi_step_format( raf );
            }

            raf.close();
            //m_is_success = true;

        } catch (IOException e) {
            e.printStackTrace();
            throw new KVSException("Read file failured");
        }

    }

    @Override
    public void write( File filename ) throws KVSException {
        m_filename = filename;
        try {
            BufferedWriter bw = new BufferedWriter( new FileWriter( filename ) );
            this.write_single_step_format( bw );

            bw.close();
            //m_is_success = true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new KVSException("Write file failured");
        }

    }

    private FormatType check_format_type( RandomAccessFile raf )
    {
        String buffer;

        FormatType format_type = FormatType.SingleStep;

        try {
            while ( (buffer = Utility.fgets( raf, MAX_LENGTH ) ) != null )
            {
                if ( buffer.startsWith( "#" ) )
                {
                    // Skip comment line.
                    continue;
                }
                else
                {
                    // Skip first token.
                    StringTokenizer token = new StringTokenizer( buffer, Delimiter );

                    token.nextToken();


                    if ( token.hasMoreTokens() )
                    {
                        format_type = FormatType.SingleStep;
                    }
                    else
                    {
                        format_type = FormatType.MultiStep;
                    }

                    break;
                }
            }

            // Go back file-pointer to head.
            raf.seek( 0 );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return( format_type );
    }

    private void read_single_step_format( RandomAccessFile raf ) throws KVSException
    {
        try {
            String buffer;
            m_nsteps = 1;
            m_cycle_type = CycleType.DataGeom;
            m_step_id = 0;
            m_step_comment = "";
            while( (buffer = Utility.fgets( raf, MAX_LENGTH )) != null ) {
                if( buffer.startsWith( "#" ) ){
                    // Skip header comment line.
                    continue;
                } else {
                    StringTokenizer token = new StringTokenizer( buffer, Delimiter );
                    m_nnodes = Integer.valueOf( token.nextToken() );
                    m_nelements = Integer.valueOf( token.nextToken() );
                    m_nvalues_per_node = Integer.valueOf( token.nextToken() );
                    this.read_coords( raf );
                    this.read_connections( raf );
                    this.read_components( raf );
                    this.read_values( raf );
                    return;
                }
            }
            throw new KVSException( "Unexpected EOF in skipping header comments." );
        } catch( IOException ex ) {
            throw new KVSException( ex );
        }
    }

    private void read_multi_step_format( RandomAccessFile raf ) throws NumberFormatException, KVSException
    {
        try {
            String buffer;
            while( (buffer = Utility.fgets( raf, MAX_LENGTH )) != null ) {
                if( buffer.startsWith( "#" ) ){
                    // Skip comment line.
                    continue;
                } else {
                    StringTokenizer token = new StringTokenizer( buffer, Delimiter );
                    m_nsteps = Integer.valueOf( token.nextToken() );
                    if( (buffer = Utility.fgets( raf, MAX_LENGTH )) != null ){
                        token = new StringTokenizer( buffer, Delimiter );
                        final String cycle_type = token.nextToken();
                        m_cycle_type = cycle_type.equals( "data" ) ? CycleType.Data : cycle_type.equals( "geom" ) ? CycleType.Geom : cycle_type.equals( "data_geom" ) ? CycleType.DataGeom : CycleType.CycleTypeUnknown;
                        if( m_cycle_type == CycleType.Data ){
                            throw new KVSException( "Cycle type \"data\" is not supported." );
                            //this.read_multi_step_format_data( raf );
                        } else if( m_cycle_type == CycleType.Geom ){
                            throw new KVSException( "Cycle type \"geom\" is not supported." );
                            //this.read_multi_step_format_geom( raf );
                        } else if( m_cycle_type == CycleType.DataGeom ){
                            this.read_multi_step_format_data_geom( raf );
                        } else {
                            throw new KVSException( "Unknown cycle type." );
                        }
                    }
                    return;
                }
            }
            throw new KVSException( "Unexpected EOF in skipping header comments." );
        } catch( IOException ex ) {
            throw new KVSException( ex );
        }
    }
    /*
    private void read_multi_step_format_data( RandomAccessFile raf )
    {  
    }

    private void read_multi_step_format_geom( RandomAccessFile raf )
    {
    }
     */
    private void read_multi_step_format_data_geom( RandomAccessFile raf ) throws NumberFormatException, IOException, KVSException
    {
        String buffer;

        this.seek_target_step( raf );

        if ( ( buffer = Utility.fgets( raf, MAX_LENGTH ) ) != null )
        {
            StringTokenizer token = new StringTokenizer( buffer, Delimiter );
            m_nnodes    = Integer.valueOf( token.nextToken() );
            m_nelements = Integer.valueOf( token.nextToken() );
        }
        else
        {
            throw new KVSException("Unexpected EOF in reading nnodes and nelements.");
        }

        this.read_coords( raf );
        this.read_connections( raf );

        if ( ( buffer = Utility.fgets( raf, MAX_LENGTH ) ) != null )
        {
            StringTokenizer token = new StringTokenizer( buffer, Delimiter );

            m_nvalues_per_node = Integer.valueOf( token.nextToken() );
        }
        else
        {
            throw new KVSException("Unexpected EOF in reading nvalues per node.");
        }

        this.read_components( raf );
        this.read_values( raf );
    }

    private void seek_target_step( RandomAccessFile raf ) throws IOException, KVSException
    {
        String buffer;

        String target_step = "step" + (m_step_id + 1);

        while ( ( buffer = Utility.fgets( raf, MAX_LENGTH ) ) != null )
        {
            StringTokenizer token = new StringTokenizer( buffer, Delimiter );
            final String first_token = token.nextToken();
            if ( first_token.equals( target_step ) )
            {
                if ( token.hasMoreTokens() )
                {
                    m_step_comment = token.nextToken();
                }
                else
                {
                    m_step_comment = "";
                }

                return;
            }
        }

        throw new KVSException("Unexpected EOF in seeking target step.");
    }

    private void read_coords( RandomAccessFile raf ) throws IOException
    {
        String buffer;

        m_coords = new float[ 3 * m_nnodes ];

        System.out.println( "start");
        long s = System.currentTimeMillis();
        for ( int i = 0; i < m_coords.length; )
        {
            buffer = Utility.fgets( raf, MAX_LENGTH );
            //buffer = raf.readLine();

            StringTokenizer token = new StringTokenizer( buffer, Delimiter );
            token.nextToken();// Skip node index.

            m_coords[i++] = Float.valueOf( token.nextToken() );
            m_coords[i++] = Float.valueOf( token.nextToken() );
            m_coords[i++] = Float.valueOf( token.nextToken() );
        }
        long e = System.currentTimeMillis();
        System.out.println( (e - s)+"ms");
    }

    private void read_connections( RandomAccessFile raf ) throws IOException, KVSException
    {
        String buffer = null;
        StringTokenizer token = null;

        // Read first line.
        if ( ( buffer = Utility.fgets( raf, MAX_LENGTH ) ) != null )
        {
            token = new StringTokenizer( buffer, Delimiter );
            token.nextToken();// Skip element index.
            token.nextToken();// Skip material index.

            final String element_type = token.nextToken();

            m_element_type =
                element_type.equals( "tet" ) ? ElementType.Tetrahedra  :
                    element_type.equals( "tet2" ) ? ElementType.Tetrahedra2 :
                        element_type.equals( "hex2" ) ? ElementType.Hexahedra2   :
                            element_type.equals( "hex" ) ? ElementType.Hexahedra  : ElementType.ElementTypeUnknown;

            switch( m_element_type ){
            case Tetrahedra:
                m_connections = new int[ 4 * m_nelements ];
                break;
            case Tetrahedra2:
                m_connections = new int[ 10 * m_nelements ];
                break;
            case Hexahedra:
                m_connections = new int[ 8 * m_nelements ];
                break;
            case Hexahedra2:
                m_connections = new int[ 20 * m_nelements ];
                break;
            default:
                throw new KVSException("Unknown element type.");
            }
        }
        else
        {
            throw new KVSException("Unexpected EOF in reading first line of connections.");
        }

        int i = 0;
        int end = m_connections.length;

        switch( m_element_type ){
        case Tetrahedra:
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;

            while ( i < end )
            {
                buffer = Utility.fgets( raf, MAX_LENGTH );
                //fgets( buffer, ::MaxLineLength, ifs );

                // skip element index.
                token = new StringTokenizer( buffer, Delimiter );
                token.nextToken();

                // skip material index.
                token.nextToken();

                final String element_type = token.nextToken();
                if ( !element_type.equals( "tet" ) )
                {
                    //throw "Multi-element type is not supported.";
                }

                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            }
            break;

        case Tetrahedra2:
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;

            while ( i < end )
            {
                buffer = Utility.fgets( raf, MAX_LENGTH );

                // skip element index.
                token = new StringTokenizer( buffer, Delimiter );
                token.nextToken();

                // skip material index.
                token.nextToken();

                final String element_type = token.nextToken();
                if ( !element_type.equals( "tet" ) )
                {
                    throw new KVSException("Multi-element type is not supported.");
                }

                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            }
            break;

        case Hexahedra:
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;

            while ( i < end )
            {
                buffer = Utility.fgets( raf, MAX_LENGTH );

                // Skip element index.
                token = new StringTokenizer( buffer, Delimiter );
                token.nextToken();

                // Skip material index.
                token.nextToken();

                final String element_type = token.nextToken();
                if ( !element_type.equals( "hex" ) )
                {
                    throw new KVSException("Multi-element type is not supported.");
                }

                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            }
            break;

        case Hexahedra2:
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;

            while ( i < end )
            {
                buffer = Utility.fgets( raf, MAX_LENGTH );

                // Skip element index.
                token = new StringTokenizer( buffer, Delimiter );
                token.nextToken();

                // Skip material index.
                token.nextToken();

                final String element_type = token.nextToken();
                if ( !element_type.equals( "hex2" ) )
                {
                    throw new KVSException("Multi-element type is not supported.");
                }

                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
                m_connections[i++] = Integer.valueOf( token.nextToken() ) - 1;
            }
            break;

        default:
            throw new KVSException("Unknown element type.");
        }
    }

    private void read_components( RandomAccessFile raf ) throws IOException, KVSException
    {
        String buffer = null;

        if ( ( buffer = Utility.fgets( raf, MAX_LENGTH ) ) != null )
        {
            StringTokenizer token = new StringTokenizer( buffer, Delimiter );
            m_ncomponents_per_node = Integer.valueOf( token.nextToken() );

            for ( int i = 0; i < m_ncomponents_per_node; ++i )
            {
                m_veclens.add( Integer.valueOf( token.nextToken() ) );
            }
        }
        else
        {
            throw new KVSException("Unexpected EOF in reading ncomponents per node");
        }

        for ( int i = 0; i < m_ncomponents_per_node; ++i )
        {
            if ( ( buffer = Utility.fgets( raf, MAX_LENGTH ) ) != null )
            {
                StringTokenizer token = new StringTokenizer( buffer, ",\n\r" );

                if ( token.hasMoreTokens() )
                {
                    m_component_names.add( token.nextToken() );
                }
                else
                {
                    m_component_names.add( "" );
                }

                if ( token.hasMoreTokens() )
                {
                    m_component_units.add( token.nextToken() );
                }
                else
                {
                    m_component_units.add( "" );
                }
            }
            else
            {
                throw new KVSException("Unexpected EOF in reading component name and unit");
            }
        }
    }

    private void read_values( RandomAccessFile raf ) throws IOException
    {
        String buffer;

        final int veclen = m_veclens.get( m_component_id );
        m_values = new float[ veclen * m_nnodes ];

        //kvs::Real32*       value = m_values.pointer();
        //kvs::Real32* const end   = value + m_values.size();

        int index = 0;
        int end = m_values.length;

        int nskips = 0;
        for ( int i = 0; i < m_component_id; ++i )
        {
            nskips += m_veclens.get( i );
        }

        while ( index < end )
        {
            //fgets( buffer, ::MaxLineLength, ifs );
            buffer = Utility.fgets( raf, MAX_LENGTH );

            // Skip node index
            StringTokenizer token = new StringTokenizer( buffer, Delimiter );
            token.nextToken();

            // Skip other components
            for ( int i = 0; i < nskips; ++i )
            {
                token.nextToken();
            }

            for ( int i = 0; i < veclen; ++i )
            {
                m_values[index++] = ( Float.valueOf( token.nextToken() ) );
            }
        }
    }

    private void write_single_step_format( BufferedWriter bw ) throws IOException, KVSException
    {
        bw.write( MessageFormat.format( "{0} {1} {2} 0 0\n", m_nnodes, m_nelements, m_nvalues_per_node ) );

        this.write_coords( bw );
        this.write_connections( bw );
        this.write_components( bw );
        this.write_values( bw );
    }

    private void write_coords( BufferedWriter bw ) throws IOException
    {
        int node_id = 1;

        int index = 0;
        final int end = m_coords.length;


        while ( index < end )
        {
            final float x = m_coords[index++];
            final float y = m_coords[index++];
            final float z = m_coords[index++];

            bw.write( MessageFormat.format( "{0} {1} {2} {3}\n", node_id++, x, y, z ) );
        }
    }

    private void write_connections( BufferedWriter bw ) throws IOException, KVSException
    {
        int element_id = 1;

        //const kvs::UInt32*       connection = m_connections.pointer();
        //const kvs::UInt32* const end        = connection + m_connections.size();
        int index = 0;
        final int end = m_connections.length;

        switch( m_element_type ){
        case Tetrahedra:
            while ( index < end )
            {
                final int node_id0 = m_connections[index++];
                final int node_id1 = m_connections[index++];
                final int node_id2 = m_connections[index++];
                final int node_id3 = m_connections[index++];

                bw.write( MessageFormat.format( "{0} 0 tet {1} {2} {3} {4}\n", 
                        element_id++,
                        node_id0,
                        node_id1,
                        node_id2,
                        node_id3 ) );
            }
            break;

        case Tetrahedra2:
            while ( index < end )
            {
                final int node_id0 = m_connections[index++];
                final int node_id1 = m_connections[index++];
                final int node_id2 = m_connections[index++];
                final int node_id3 = m_connections[index++];
                final int node_id4 = m_connections[index++];
                final int node_id5 = m_connections[index++];
                final int node_id6 = m_connections[index++];
                final int node_id7 = m_connections[index++];
                final int node_id8 = m_connections[index++];
                final int node_id9 = m_connections[index++];

                bw.write( MessageFormat.format( "{0} 0 tet {1} {2} {3} {4} {5} {6} {7} {8} {9} {10}\n", 
                        element_id++,
                        node_id0,
                        node_id1,
                        node_id2,
                        node_id3,
                        node_id4,
                        node_id5,
                        node_id6,
                        node_id7,
                        node_id8,
                        node_id9 ) );
            }

            break;

        case Hexahedra:
            while ( index < end )
            {
                final int node_id0 = m_connections[index++];
                final int node_id1 = m_connections[index++];
                final int node_id2 = m_connections[index++];
                final int node_id3 = m_connections[index++];
                final int node_id4 = m_connections[index++];
                final int node_id5 = m_connections[index++];
                final int node_id6 = m_connections[index++];
                final int node_id7 = m_connections[index++];

                bw.write( MessageFormat.format( "{0} 0 hex {1} {2} {3} {4} {5} {6} {7} {8}\n", 
                        element_id++,
                        node_id0,
                        node_id1,
                        node_id2,
                        node_id3,
                        node_id4,
                        node_id5,
                        node_id6,
                        node_id7 ) );
            }

            break;

        case Hexahedra2:
            while ( index < end )
            {
                bw.write( MessageFormat.format( "{0} 0 hex2", element_id++) );

                //fprintf( ofs, "%u 0 hex2", static_cast<unsigned int>( element_id++ ) );

                final int nnodes = 20;
                for ( int i = 0; i < nnodes; i++ )
                {
                    final int node_id = m_connections[index++];
                    bw.write( " " + node_id );
                }
                bw.write( "\n" );
            }

            break;

        default:
            throw new KVSException("Unknown element type.");
        }   
    }

    private void write_components( BufferedWriter bw ) throws IOException
    {
        bw.write( String.valueOf( m_ncomponents_per_node ) );
        for ( int i = 0; i < m_ncomponents_per_node; ++i )
        {
            bw.write( " " + m_veclens.get( i ) );
        }
        bw.write( "\n" );

        for ( int i = 0; i < m_ncomponents_per_node; ++i )
        {
            bw.write( MessageFormat.format( "{0},{1}\n", m_component_names.get( i ), m_component_units.get( i ) ) );
        }
    }

    private void write_values( BufferedWriter bw ) throws IOException
    {
        int node_id = 1;

        //const kvs::Real32*       value = m_values.pointer();
        //const kvs::Real32* const end   = value + m_values.size();
        int index = 0;
        final int end = m_values.length; 

        while ( index < end )
        {
            bw.write( MessageFormat.format( "{0} {1}\n", node_id++,m_values[index++] ) );
        }
    }


}
