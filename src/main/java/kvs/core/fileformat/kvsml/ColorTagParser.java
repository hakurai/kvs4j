package kvs.core.fileformat.kvsml;

import java.nio.ByteBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Node;

public class ColorTagParser {

    protected int m_nvertices;

    public ColorTagParser( final int nvertices ){
        m_nvertices = nvertices;
    }

    public ByteBuffer parse( final Node parent_node ) throws KVSException
    {
        // <Color>
        Node color_node = TagParser.findChildNode( parent_node, "Color" );
        if( color_node == null )
        {
            // Set default value (black).
            ByteBuffer colors = ByteBuffer.allocate( 3 );
            colors.put( (byte)0 );
            colors.put( (byte)0 );
            colors.put( (byte)0 );

            System.err.println("Cannot find <Color>.");

            return colors;
        }

        // <DataValue>
        Node data_value_node = TagParser.findChildNode( color_node, "DataValue" );
        if( data_value_node != null )
        {
            ByteBuffer colors = ByteBuffer.allocate( 3 );
            TagParser.parseValue( data_value_node, colors );
            return colors;
        }

        // <DataArray>
        Node data_array_node = TagParser.findChildNode( color_node, "DataArray" );
        if( data_array_node == null )
        {
            throw new KVSException("Cannot find <DataArray>");
        }

        ByteBuffer colors = ByteBuffer.allocate( m_nvertices * 3 );
        TagParser.parseArray( data_array_node, colors );
        return colors;
    }
}
