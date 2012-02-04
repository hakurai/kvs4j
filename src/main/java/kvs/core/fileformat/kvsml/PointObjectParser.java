package kvs.core.fileformat.kvsml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PointObjectParser {
    
    protected int   m_nvertices;         ///< number of vertices
    protected Node  m_point_object_node; ///< pointer to the point object node
    protected Node  m_vertex_node;       ///< pointer to the vertex node
    
    public PointObjectParser(){
        
    }
    
    public PointObjectParser( final Document document ) throws KVSException{
        this.parse( document );
    }
    
    public int nvertices(){
        return m_nvertices;
    }
    
    public void parse( final Document document ) throws KVSException{
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
        if( !object_type.equals( "PointObject" ) )
        {
            throw new KVSException("This file type is not a point object.");
        }

        // <PointObject>
        m_point_object_node = TagParser.findChildNode( object_node, object_type );
        if( m_point_object_node == null )
        {
            throw new KVSException("Cannot find <" +object_type+ ">.");
        }

        // <Vertex>
        m_vertex_node = TagParser.findChildNode( m_point_object_node, "Vertex" );
        if( m_vertex_node == null )
        {
            throw new KVSException("Cannot find <Vertex>.");
        }

        // <Vertex nvertices="xxx">
        Element vertex_element = (Element)m_vertex_node;
        this.parse_nvertices( vertex_element );

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
        if( !object_type.equals( "PointObject" ) ){
            return false;
        }

        // <PointObject>
        Node point_object_node = TagParser.findChildNode( object_node, object_type );
        if( point_object_node == null ){
            return false;
        }

        return true;
    }
    
    public FloatBuffer setCoordsTo() throws KVSException
    {
        CoordTagParser parser = new CoordTagParser( m_nvertices );
        return parser.parse( m_vertex_node );
    }
    
    public ByteBuffer setColorsTo() throws KVSException
    {
        ColorTagParser parser = new ColorTagParser( m_nvertices );
        return parser.parse( m_vertex_node );
    }
    
    public FloatBuffer setNormalsTo() throws KVSException
    {
        NormalTagParser parser = new NormalTagParser( m_nvertices );
        return parser.parse( m_vertex_node );
    }
    
    public FloatBuffer setSizesTo() throws KVSException
    {
        SizeTagParser parser = new SizeTagParser( m_nvertices );
        return parser.parse( m_vertex_node );
    }
    
    protected boolean parse_nvertices( final Element element )
    {
        String nvertices = element.getAttribute( "nvertices" );
        if( nvertices.equals( "" ) )
        {
            System.err.println("Cannot find 'nvertices' in <Vertex>.");
            return false;
        }

        m_nvertices = Integer.valueOf( nvertices );

        return( true );
    }

}
