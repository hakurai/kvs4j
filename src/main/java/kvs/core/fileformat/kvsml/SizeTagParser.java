package kvs.core.fileformat.kvsml;

import java.nio.FloatBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Node;

public class SizeTagParser {

    protected int m_nvertices;

    public SizeTagParser( final int nvertices ){
        m_nvertices = nvertices;
    }

    public FloatBuffer parse( final Node parent_node ) throws KVSException
    {
        // <Size>
        Node size_node = TagParser.findChildNode( parent_node, "Size" );
        if( size_node == null ){
            FloatBuffer sizes = FloatBuffer.allocate( 0 );
            return sizes;
        }

        // <DataValue>
        Node data_value_node = TagParser.findChildNode( size_node, "DataValue" );
        if( data_value_node != null )
        {
            FloatBuffer sizes = FloatBuffer.allocate( 1 );
            TagParser.parseValue( data_value_node, sizes );
            return sizes;
        }

        // <DataArray>
        Node data_array_node = TagParser.findChildNode( size_node, "DataArray" );
        if( data_array_node == null )
        {
            throw new KVSException("Cannot find <DataArray>");
        }

        FloatBuffer sizes = FloatBuffer.allocate( m_nvertices );
        TagParser.parseValue( data_array_node, sizes );
        return sizes;
    }

}
