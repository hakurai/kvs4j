package kvs.core.fileformat.kvsml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class LineObjectParser {
    
    protected String    m_line_type = "";       ///< line type [strip|uniline|polyline|segment]
    protected String    m_color_type = "";      ///< color type [vertex|line]
    protected int       m_nvertices  = 0;       ///< number of vertices
    protected int       m_nlines     = 0;       ///< number of lines (connections)
    protected Node      m_line_object_node;     ///< pointer to the line object node
    protected Node      m_vertex_node;          ///< pointer to the vertex node
    protected Node      m_line_node;            ///< pointer to the line node
    
    public LineObjectParser(){}
    
    public LineObjectParser( Document document ) throws KVSException {
        this.parse( document );
    }

    public String lineType() {
        return m_line_type;
    }

    public String colorType() {
        return m_color_type;
    }

    public int nvertices() {
        return m_nvertices;
    }

    public int nlines() {
        return (m_nlines);
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
        Element object_node = (Element)TagParser.findChildNode( kvsml_node, "Object" );
        if( object_node == null )
        {
            throw new KVSException("Cannot find <Object>.");
        }

        // <Object type="xxx">
        String object_type = object_node.getAttribute( "type" );
        if( !object_type.equals( "LineObject" ) )
        {
            throw new KVSException("This file type is not a line object.");
        }

        // <LineObject>
        m_line_object_node = (Element)TagParser.findChildNode( object_node, object_type );
        if( m_line_object_node == null )
        {
            throw new KVSException("Cannot find <" +object_type+ ">.");
        }

        // <LineObject line_type="xxx" color_type="xxx">
        Element line_object_element = (Element)m_line_object_node;
        // Firstly, it is necessary to parse the types of line.
        if( !this.parse_line_type( line_object_element )){
            throw new KVSException( "Unknown line type '" +m_line_type+ "'."  );
        }
        if( !this.parse_color_type( line_object_element ) ){
            throw new KVSException( "Unknown color type '" +m_color_type+ "'." );
        }

        // <Vertex>
        m_vertex_node = (Element)TagParser.findChildNode( line_object_element, "Vertex" );
        if( m_vertex_node == null )
        {
            throw new KVSException("Cannot find <Vertex>.");
        }

        // <Vertex nvertices="xxx">
        Element vertex_element = (Element)m_vertex_node;
        if( !this.parse_nvertices( vertex_element ) ){
            throw new KVSException("Cannot find 'nvertices' in <Vertex>.");
        }

        // <Line>
        m_line_node = (Element)TagParser.findChildNode( line_object_element, "Line" );
        if( m_line_node == null )
        {
            throw new KVSException("Cannot find <Line>.");
        }

        // <Line nlines="x">
        Element line_element = (Element)m_line_node;

        this.parse_nlines( line_element );
    }
    
    public static boolean check( Document document )
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
        String object_type = object_element.getAttribute( "type" );
        if( !object_type.equals( "LineObject" ) ){
            return false;
        }

        // <LineObject>
        Node line_object_node = TagParser.findChildNode( object_element, object_type );
        if( line_object_node == null ){
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
        if(      m_line_type.equals( "uniline" ) ) nindices = m_nlines;
        else if( m_line_type.equals( "polyline" ) ) nindices = m_nlines * 2;
        else if( m_line_type.equals( "segment" ) ) nindices = m_nlines * 2;

        ConnectionTagParser parser = new ConnectionTagParser( nindices );
        return parser.parse( m_line_node );
    }
    
    public ByteBuffer setColorsTo() throws KVSException
    {
        int ncolors = m_nvertices;
        if( m_color_type.equals( "line_color" ) )
        {
            if(      m_line_type.equals( "strip"   ) ) ncolors = m_nvertices - 1;
            else if( m_line_type.equals( "uniline" ) ) ncolors = m_nlines - 1;
            else if( m_line_type.equals( "polyline") ) ncolors = m_nlines;
            else if( m_line_type.equals( "segment" ) ) ncolors = m_nlines;

            ColorTagParser parser = new ColorTagParser( ncolors );
            return parser.parse( m_line_node );
        }
        else
        {
            ColorTagParser parser = new ColorTagParser( ncolors );

           return parser.parse( m_vertex_node );
        }
    }
    
    public FloatBuffer setSizesTo() throws KVSException
    {
        SizeTagParser parser = new SizeTagParser( m_nvertices );
        return parser.parse( m_vertex_node );
    }
    
    protected boolean parse_line_type( final Element element )
    {
        m_line_type = element.getAttribute( "line_type" );
        if( m_line_type.equals( "" ) ) m_line_type = "strip";

        if( m_line_type.equals( "strip" )    ) return true;
        if( m_line_type.equals( "uniline"  ) ) return true;
        if( m_line_type.equals( "polyline" ) ) return true;
        if( m_line_type.equals( "segment"  ) ) return true;
        
        return false;

    }
    
    protected boolean parse_color_type( final Element element )
    {
        m_color_type = element.getAttribute( "color_type" );
        if( m_color_type.equals( "" ) ) m_color_type = "line";

        if( m_color_type.equals( "line"   ) ) return true;
        if( m_color_type.equals( "vertex" ) ) return true;

        return false;
    }
    
    protected boolean parse_nvertices( final Element element )
    {
        String nvertices = element.getAttribute( "nvertices" );
        if( nvertices.equals( "" ) )
        {
            return false;
        }

        m_nvertices = Integer.valueOf( nvertices );

        return true;
    }
    
    protected boolean parse_nlines( final Element element )
    {
        String nlines = element.getAttribute( "nlines" );
        if( nlines.equals( "" ) )
        {
            m_nlines = 0; // in the case of 'strip'

            if( !m_line_type.equals( "strip" ) ){
                return false;
            }
        }
        else
        {
            m_nlines = Integer.valueOf( nlines );
        }

        return( true );
    }

}
