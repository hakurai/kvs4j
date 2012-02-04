package kvs.core.fileformat.kvsml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class PolygonObjectParser {
    
    protected String    m_polygon_type;        ///< polygon type [triangle|quadrangle]
    protected String    m_color_type;          ///< color type [vertex|polygon]
    protected String    m_normal_type;         ///< normal type [vertex|polygon]
    protected int       m_nvertices;           ///< number of vertices
    protected int       m_npolygons;           ///< number of polygons (connections)
    protected Node      m_polygon_object_node; ///< pointer to the polygon object node
    protected Node      m_vertex_node;         ///< pointer to the vertex node
    protected Node      m_polygon_node;        ///< pointer to the polygon node
    
    public PolygonObjectParser(){
        m_polygon_type = "";
        m_color_type = "";
        m_normal_type = "";
        m_nvertices = 0;
        m_npolygons = 0;
    }
    
    public PolygonObjectParser( Document document ) throws KVSException{
        this.parse( document );
    }
    
    public String polygonType(){
        return m_polygon_type;
    }
    
    public String colorType(){
        return m_color_type;
    }
    
    public String normalType(){
        return m_normal_type;
    }
    
    public int nvertices(){
        return m_nvertices;
    }
    
    public int npolygons(){
        return m_npolygons;
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
        if( !object_type.equals( "PolygonObject" ) )
        {
            throw new KVSException("This file type is not a polygon object.");
        }

        // <PolygonObject>
        m_polygon_object_node = TagParser.findChildNode( object_node, object_type );
        if( m_polygon_object_node == null )
        {
            throw new KVSException("Cannot find <" +object_type+ ">.");
        }

        // <PolygonObject polygon_type="xxx" color_type="xxx" normal_type="xxx">
        Element polygon_object_element = (Element)m_polygon_object_node;
        // Firstly, it is necessary to parse the types of polygon.
        if( !this.parse_polygon_type( polygon_object_element ) ){
            throw new KVSException( "Unknown polygon type '" +m_polygon_type+ "'." );
        }
        
        if( !this.parse_color_type( polygon_object_element ) ){
            throw new KVSException( "Unknown color type '" +m_color_type+ "'." );
        }
        
        if( !this.parse_normal_type( polygon_object_element ) ){
            throw new KVSException( "Unknown normal type '" +m_normal_type+ "'." );
        }

        // <Vertex>
        m_vertex_node = TagParser.findChildNode( m_polygon_object_node, "Vertex" );
        if( m_vertex_node == null )
        {
            throw new KVSException("Cannot find <Vertex>.");
        }

        // <Vertex nvertices="xxx">
        Element vertex_element = (Element)m_vertex_node;
        if( !this.parse_nvertices( vertex_element ) ){
            throw new KVSException("Cannot find 'nvertices' in <Point>.");
        }

        // <Polygon>
        m_polygon_node = TagParser.findChildNode( m_polygon_object_node, "Polygon" );
        if( m_polygon_node == null )
        {
            throw new KVSException("Cannot find <Polygon>.");
        }

        // <Polygon npolygons="x">
        Element polygon_element = (Element)m_polygon_node;
        if( polygon_element == null )
        {
            throw new KVSException("Cannot read <Polygon>.");
        }
        
        if( !this.parse_npolygons( polygon_element ) ){
            throw new KVSException("Cannot find 'npolygons' in <Polygon>.");
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
        if( !object_type.equals( "PolygonObject" ) ){
            return false;
        }

        // <PolygonObject>
        Node polygon_object_node = TagParser.findChildNode( object_node, object_type );
        if( polygon_object_node == null ){
            return false;
        }

        return true;
    }
    
    public FloatBuffer setCoordsTo() throws KVSException
    {
        CoordTagParser parser = new CoordTagParser( m_nvertices );
        return parser.parse( m_vertex_node );
    }
    
    public IntBuffer setConnectionsTo() throws KVSException
    {
        int nindices = 0;
        if(      m_polygon_type.equals( "triangle"   ) ){
            nindices = m_npolygons * 3;
        }
        else if( m_polygon_type.equals( "quadrangle" ) ){
            nindices = m_npolygons * 4;
        }

        ConnectionTagParser parser = new ConnectionTagParser( nindices );
        return parser.parse( m_polygon_node );
    }
    
    public FloatBuffer setNormalsTo() throws KVSException
    {
        int nnormals = m_nvertices; // vertex
        if( m_normal_type.equals( "polygon" ) )
        {
            nnormals = m_npolygons;
            NormalTagParser parser = new NormalTagParser( nnormals );
            return parser.parse( m_polygon_node );
        }
        else
        {
            NormalTagParser parser = new NormalTagParser( nnormals );

            return parser.parse( m_vertex_node );
        }
    }
    
    public ByteBuffer setColorsTo() throws KVSException
    {
        int ncolors = m_nvertices; // vertex_color
        if( m_color_type.equals( "polygon" ) )
        {
            ncolors = m_npolygons;

            ColorTagParser parser = new ColorTagParser( ncolors );
            return parser.parse( m_vertex_node );
        }
        else
        {
            ColorTagParser parser = new ColorTagParser( ncolors );

            return parser.parse( m_vertex_node );
        }
    }
    
    public ByteBuffer setOpacitiesTo() throws KVSException
    {
        OpacityTagParser parser = new OpacityTagParser( m_nvertices );
        return parser.parse( m_vertex_node );
    }
    
    public boolean parse_polygon_type( final Element element )
    {
        m_polygon_type = element.getAttribute( "polygon_type" );
        if( m_polygon_type.equals( "" ) ){
            m_polygon_type = "triangle";
        }

        if( m_polygon_type.equals( "triangle" )   ){
            return true;
        }
        if( m_polygon_type.equals( "quadrangle" ) ){
            return true;
        }
        return false;
    }
    
    public boolean parse_color_type( final Element element )
    {
        m_color_type = element.getAttribute( "color_type" );
        if( m_color_type.equals( "" ) ) m_color_type = "polygon";

        if( m_color_type.equals( "polygon" ) ){
            return true;
        }
        if( m_color_type.equals( "vertex" )  ){
            return true;
        }
        return false;
    }
    
    public boolean parse_normal_type( final Element element )
    {
        m_normal_type = element.getAttribute( "normal_type" );
        if( m_normal_type.equals( "" ) ) m_normal_type = "polygon";

        if( m_normal_type.equals( "polygon" ) ){
            return true ;
        }
        if( m_normal_type.equals( "vertex" )  ){
            return true ;
        }
        return false;
    }
    
    public boolean parse_nvertices( final Element element )
    {
        String nvertices = element.getAttribute( "nvertices" );
        if( nvertices.equals( "" ) )
        {
            return false;
        }

        m_nvertices = Integer.valueOf( nvertices );

        return true;
    }
    
    public boolean parse_npolygons( final Element element )
    {
        String npolygons = element.getAttribute( "npolygons" );
        if( npolygons.equals( "" ) )
        {
            return false;
        }

        m_npolygons = Integer.valueOf( npolygons );

        return true;
    }
    

}
