package kvs.core.fileformat.kvsml;

import java.nio.ByteBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Node;

public class OpacityTagParser {

    protected int m_nvertices;

    public OpacityTagParser( final int nvertices ){
        m_nvertices = nvertices;
    }

    public ByteBuffer parse( final Node parent_node) throws KVSException
    {
        // <Opacity>
        Node opacity_node = TagParser.findChildNode( parent_node, "Opacity" );
        if( opacity_node == null )
        {
            // Set default value.
            ByteBuffer opacities = ByteBuffer.allocate(1);
            opacities.put( 0, (byte)255 );

            return opacities;
        }

        // <DataValue>
        Node data_value_node = TagParser.findChildNode( opacity_node, "DataValue" );
        if( data_value_node != null )
        {
            ByteBuffer opacities = ByteBuffer.allocate(1);

            TagParser.parseValue( data_value_node, opacities );
            return opacities;
        }

        // <DataArray>
        Node data_array_node = TagParser.findChildNode( opacity_node, "DataArray" );
        if( data_array_node == null )
        {
            throw new KVSException("Cannot find <DataArray>");
        }
        ByteBuffer opacities = ByteBuffer.allocate( m_nvertices );

        TagParser.parseArray( data_array_node, opacities );
        return opacities;
    }

}
