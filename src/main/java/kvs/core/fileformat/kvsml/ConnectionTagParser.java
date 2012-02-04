package kvs.core.fileformat.kvsml;

import java.nio.IntBuffer;

import org.w3c.dom.Node;

import kvs.core.KVSException;

public class ConnectionTagParser {

    protected int m_nindices;

    public ConnectionTagParser( final int nindices ){
        m_nindices = nindices;
    }

    public IntBuffer parse( final Node parent_node ) throws KVSException
    {
        // <Connection>
        Node connection_node = TagParser.findChildNode( parent_node, "Connection" );
        if( connection_node == null )
        {
            throw new KVSException("Cannot find <Connection>.");
        }

        // <DataArray>
        Node data_array_node = TagParser.findChildNode( connection_node, "DataArray" );
        if( data_array_node == null ){
            throw new KVSException("Cannot find <DataArray>");
        }

        IntBuffer connections = IntBuffer.allocate( m_nindices );
        
        TagParser.parseArray( data_array_node, connections );

        return connections;
    }

}
