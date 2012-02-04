package kvs.core.fileformat.kvsml;

import java.nio.FloatBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CoordTagParser {
    
    protected int m_nvertices; ///< number of vertices

    public CoordTagParser( final int nvertices ){
        m_nvertices = nvertices;
    }

    public FloatBuffer parse(final Node parent_node) throws KVSException
    {
        // <Coord>
        Node coord_node = TagParser.findChildNode( (Element) parent_node, "Coord" );
        if( coord_node == null )
        {
            throw new KVSException("Cannot find <Coord>.");
        }
        
        FloatBuffer coords;

        // <DataValue>
        Node data_value_node = TagParser.findChildNode( (Element)coord_node, "DataValue" );
        if( data_value_node != null )
        {
            if( m_nvertices == 1 )
            {
                final int dimension = 3;
                coords = FloatBuffer.allocate( dimension );
                TagParser.parseValue( data_value_node, coords );
                return coords;
            }

            throw new KVSException("Incorrect the number of vertices.");
            
        } else {

            // <DataArray>
            Node data_array_node = TagParser.findChildNode( (Element)coord_node, "DataArray" );
            if( data_array_node == null )
            {
                throw new KVSException("Cannot find <DataArray>");
            }

            final int dimension = 3;
            coords = FloatBuffer.allocate( m_nvertices * dimension );

            TagParser.parseArray( data_array_node, coords );
        }
        
        return coords;
    }
}
