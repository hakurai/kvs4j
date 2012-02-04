package kvs.core.fileformat.kvsml;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.StringTokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import kvs.core.KVSException;
import kvs.core.matrix.Vector3i;

public class StructuredVolumeObjectParser {
    
    protected String    m_grid_type;          ///< grid type [uniform|rectilinear|curvilinear]
    protected Vector3i m_resolution;         ///< resolution
    protected Node     m_volume_object_node; ///< pointer to the structured volume node
    protected Node     m_node_node;          ///< pointer to the node node: <Node>
    
    public StructuredVolumeObjectParser()
    {
    }
    
    public StructuredVolumeObjectParser( Document document ) throws KVSException{
        this.parse( document );
    }
    

    public String gridType()
    {
        return m_grid_type;
    }

    public Vector3i resolution()
    {
        return m_resolution;
    }
    
    public int veclen()
    {
        // <Value>
        Node value_node = TagParser.findChildNode( m_node_node, "Value" );
        if( value_node == null )
        {
            System.err.println("Cannot find <Value>.");
            return 0;
        }

        // <Value veclen="xxx">
        Element value_element = (Element)value_node;
        String veclen = value_element.getAttribute( "veclen" );
        if( veclen.equals( "" ) )
        {
            System.err.println("Cannot find 'veclen' in <Value>.");
            return 0;
        }

        return Integer.valueOf( veclen );
    }
    
    public void parse( final Document document ) throws KVSException
    {
        // <KVSML>
        Node kvsml_node = TagParser.findNode( document, "KVSML" );
        if( kvsml_node == null )
        {
            throw new KVSException("Cannot find <KVSML>.");
        }

        // <Object>
        Node object_node = TagParser.findChildNode( kvsml_node, "Object" );
        if( object_node == null )
        {
            throw new KVSException("Cannot find <Object>.");
        }

        // <Object type="xxx">
        Element object_element = (Element)object_node;
        String object_type = object_element.getAttribute( "type" );
        if( !object_type.equals( "StructuredVolumeObject" ) )
        {
            throw new KVSException("This file type is not a uniform volume object.");
        }

        // <StructuredVolumeObject>
        m_volume_object_node = TagParser.findChildNode( object_node, object_type );
        if( m_volume_object_node == null )
        {
            throw new KVSException("Cannot find <" +object_type+ ">.");
        }

        // <StructuredVolumeObject resolution="xxx" grid_type="xxx">
        Element volume_element = (Element)m_volume_object_node;
        if( !this.parse_resolution( volume_element ) ){
            throw new KVSException("Cannot find 'resolution' in <StructuredVolume>.");
        }
        
        if( !this.parse_grid_type( volume_element ) ){
            throw new KVSException( "Unknown grid type '" +m_grid_type+ "'." );
        }

        // <Node>
        m_node_node = TagParser.findChildNode( m_volume_object_node, "Node" );
        if( m_node_node == null )
        {
            throw new KVSException("Cannot find <Node>.");
        }
    }
    
    public static boolean check( final Document document )
    {
        // <KVSML>
        Node kvsml_node = TagParser.findNode( document, "KVSML" );
        if( kvsml_node == null ){
            return false;
        }

        // <Object>
        Node object_node = TagParser.findChildNode( kvsml_node, "Object" );
        if( object_node == null ){
            return false;
        }

        // <Object type="xxx">
        Element object_element = (Element)object_node;
        final String object_type = object_element.getAttribute( "type" );
        if( !object_type.equals( "StructuredVolumeObject" ) ){
            return false;
        }

        // <StructuredVolumeObject>
        Node structured_volume_object_node = TagParser.findChildNode( object_node, object_type );
        if( structured_volume_object_node == null ){
            return false;
        }

        return true;
    }
    
    public Buffer setValuesTo() throws KVSException
    {
        final int nnodes = m_resolution.getX() * m_resolution.getY() * m_resolution.getZ();
        ValueTagParser parser = new ValueTagParser( nnodes );
        return parser.parse( m_node_node );
    }
    
    public FloatBuffer setCoordsTo() throws KVSException
    {
        final int nnodes = m_resolution.getX() * m_resolution.getY() * m_resolution.getZ();
        CoordTagParser parser = new CoordTagParser( nnodes );
        return parser.parse( m_node_node );
    }
    
    protected boolean parse_resolution( final Element element )
    {
        String resolution = element.getAttribute( "resolution" );
        if( resolution.equals( "" ) )
        {
            return false;
        }

        final String delim = " \n\t";
        StringTokenizer t = new StringTokenizer( resolution, delim );

        m_resolution = new Vector3i( Integer.valueOf( t.nextToken() ),
                                     Integer.valueOf( t.nextToken() ),
                                     Integer.valueOf( t.nextToken() ) );

        return true ;

    }
    
    protected boolean parse_grid_type( final Element element )
    {
        m_grid_type = element.getAttribute( "normal_type" );
        if( m_grid_type.equals( "" ) ) m_grid_type = "uniform";

        if( m_grid_type.equals( "uniform" )     ){
            return true;
        }
        if( m_grid_type.equals( "rectilinear" ) ){
            return true;
        }
        if( m_grid_type.equals( "curvilinear" ) ){
            return true;
        }

        return false;
    }
    
    

    

}
