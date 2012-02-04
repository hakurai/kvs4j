package kvs.core.fileformat.kvsml;

import java.nio.FloatBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Node;

public class NormalTagParser {
    
    protected int m_nvertices;
    
    public NormalTagParser( final int nvertices ){
        m_nvertices = nvertices;
    }
    
    public FloatBuffer parse( final Node parent_node) throws KVSException
        {
            // <Normal>
            // Set the nothing, if the normal node is not existed.
            Node normal_node = TagParser.findChildNode( parent_node, "Normal" );
            if( normal_node == null ){
                FloatBuffer normals = FloatBuffer.allocate( 0 );
                return normals;
            }

            // <DataValue>
            Node data_value_node = TagParser.findChildNode( normal_node, "DataValue" );
            if( data_value_node != null )
            {
                if( m_nvertices == 1 )
                {
                    final int dimension = 3;
                    FloatBuffer normals = FloatBuffer.allocate( dimension );
                    TagParser.parseValue( data_value_node, normals );
                    return normals;
                }

                throw new KVSException("Incorrect the number of vertices.");
            }

            // <DataArray>
            Node data_array_node = TagParser.findChildNode( normal_node, "DataArray" );
            if( data_array_node == null )
            {
                throw new KVSException("Cannot find <DataArray>");
            }

            final int dimension = 3;
            FloatBuffer normals = FloatBuffer.allocate( m_nvertices * dimension );
            
            TagParser.parseArray( data_array_node, normals );
            return normals;
        }

}
