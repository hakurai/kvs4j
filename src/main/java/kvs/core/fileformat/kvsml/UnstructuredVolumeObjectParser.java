package kvs.core.fileformat.kvsml;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class UnstructuredVolumeObjectParser {
    
    protected String  m_cell_type;          ///< cell type [tetrahedra|hexahedra]
    protected int     m_nnodes;             ///< number of nodes
    protected int     m_ncells;             ///< number of cells
    protected Node    m_volume_object_node; ///< pointer to the unstructured volume node
    protected Node    m_node_node;          ///< pointer to the node node
    protected Node    m_cell_node;          ///< pointer to the cell node
    
    public UnstructuredVolumeObjectParser(){
        m_nnodes = 0;
        m_ncells = 0;
    }
    
    public UnstructuredVolumeObjectParser( final Document document ) throws KVSException{
        this.parse( document );
    }
    
    public String cellType()
    {
        return m_cell_type;
    }
    
    public int nnodes()
    {
        return m_nnodes;
    }
    
    public int ncells()
    {
        return m_ncells;
    }
    
    public int veclen()
    {
        // <Value>
        Node value_node = TagParser.findChildNode( m_node_node, "Value" );
        if( value_node == null )
        {
            //kvsMessageError("Cannot find <Value>.");
            return 0;
        }

        // <Value veclen="xxx">
        Element value_element = (Element)value_node;
        String veclen = value_element.getAttribute( "veclen" );
        if( veclen.equals( "" ) )
        {
            //kvsMessageError("Cannot find 'veclen' in <Value>.");
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
        if( !object_type.equals( "UnstructuredVolumeObject" ) )
        {
            throw new KVSException("This file type is not a uniform volume object.");
        }

        // <UnstructuredVolumeObject>
        m_volume_object_node = TagParser.findChildNode( object_node, object_type );
        if( m_volume_object_node == null )
        {
            throw new KVSException("Cannot find <" +object_type+ ">.");
        }

        // <UnstructuredVolumeObject cell_type="xxx">
        Element volume_element = (Element)m_volume_object_node;
        if( !this.parse_cell_type( volume_element ) ){
            throw new KVSException( "Unknown grid type '" +m_cell_type+ "'." );
        }

        // <Node>
        m_node_node = TagParser.findChildNode( m_volume_object_node, "Node" );
        if( m_node_node == null )
        {
            throw new KVSException("Cannot find <Node>.");
        }

        // <Node nnodes="xxx">
        Element node_element = (Element)m_node_node;
        if( !this.parse_nnodes( node_element ) ){
            throw new KVSException("Cannot find 'nnodes' in <Node>.");
        }

        // <Cell>
        m_cell_node = TagParser.findChildNode( m_volume_object_node, "Cell" );
        if( m_cell_node == null )
        {
            throw new KVSException("Cannot find <Cell>.");
        }

        // <Cell ncells="xxx">
        Element cell_element = (Element)m_cell_node;
        if( !this.parse_ncells( cell_element ) ){
            throw new KVSException("Cannot find 'ncells' in <Node>.");
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
        if( !object_type.equals( "UnstructuredVolumeObject" ) ){
            return false;
        }

        // <UnstructuredVolumeObject>
        Node unstructured_volume_object_node = TagParser.findChildNode( object_node, object_type );
        if( unstructured_volume_object_node == null ){
            return false;
        }

        return true;
    }
    
    public Buffer setValuesTo() throws KVSException
    {
        ValueTagParser parser = new ValueTagParser( m_nnodes );
        return parser.parse( m_node_node );
    }
    
    public FloatBuffer setCoordsTo() throws KVSException
    {
        CoordTagParser parser = new CoordTagParser( m_nnodes );
        return parser.parse( m_node_node );
    }
    
    public IntBuffer setConnectionsTo() throws KVSException
    {
        int nindices = 0;
        if(      m_cell_type.equals( "tetrahedra" ) ){
            nindices = m_ncells * 4;
        }
        else if( m_cell_type.equals( "hexahedra" )  ){
            nindices = m_ncells * 8;
        }

        ConnectionTagParser parser = new ConnectionTagParser( nindices );
        return parser.parse( m_cell_node );
    }
    
    protected boolean parse_nnodes( final Element element )
    {
        String nnodes = element.getAttribute( "nnodes" );
        if( nnodes.equals( "" ) )
        {
            
            return false;
        }

        m_nnodes = Integer.valueOf( nnodes );

        return true;
    }
    
    protected boolean parse_ncells( final Element element )
    {
        String ncells = element.getAttribute( "ncells" );
        if( ncells.equals( "" ) )
        {
            return false;
        }

        m_ncells = Integer.valueOf( ncells );

        return true;
    }
    
    protected boolean parse_cell_type( final Element element )
    {
        m_cell_type = element.getAttribute( "cell_type" );

        if( m_cell_type.equals( "tetrahedra" ) ){
            return true;
        }
        if( m_cell_type.equals( "hexahedra" )  ){
            return true;
        }

        return false;
    }

}
