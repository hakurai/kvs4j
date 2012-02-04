package kvs.core.visualization.filter;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector3i;
import kvs.core.util.Utility;
import kvs.core.visualization.object.StructuredVolumeObject;

public class TrilinearInterpolator {
    

    private Vector3i    m_grid_index = Vector3i.ZERO; ///< grid index
    private int[]       m_index = new int[8];   ///< neighbouring grid index < unsigned
    private float[]     m_weight = new float[8];  ///< weight for the neighbouring grid index

    private final StructuredVolumeObject m_reference_volume; ///< reference irregular volume m_data
    
    private FloatBuffer m_data;                                 ///m_reference_volumeのvaluesを配列に変換したもの
    
    private int m_data_limit;

    public TrilinearInterpolator( final StructuredVolumeObject volume ){
        m_reference_volume = volume;
        final Buffer buffer = volume.values();
        m_data = Utility.bufferToDirectFloatBuffer( buffer );
        m_data_limit = m_data.limit();
            
    }

    public final void attachPoint( final Vector3f point )
    {
        final int i = point.getX() < 0 ? 0 : (int)( point.getX() ); //unsigned
        final int j = point.getY() < 0 ? 0 : (int)( point.getY() ); //unsigned
        final int k = point.getZ() < 0 ? 0 : (int)( point.getZ() ); //unsigned

        final int line_size  = m_reference_volume.nnodesPerLine();
        final int slice_size = m_reference_volume.nnodesPerSlice();

        // Calculate index.
        m_grid_index = new Vector3i( i, j, k );
        
        m_index[0] = (i + j * line_size + k * slice_size);
        m_index[1] = (m_index[0] + 1);
        m_index[2] = (m_index[1] + line_size);
        m_index[3] = (m_index[0] + line_size);
        m_index[4] = (m_index[0] + slice_size);
        m_index[5] = (m_index[1] + slice_size);
        m_index[6] = (m_index[2] + slice_size);
        m_index[7] = (m_index[3] + slice_size);

        // Calculate local coordinate.
        final float x = point.getX() - i;
        final float y = point.getY() - j;
        final float z = point.getZ() - k;

        final float xy = x * y;
        final float yz = y * z;
        final float zx = z * x;

        final float xyz = xy * z;

        m_weight[0] = 1.0f - x - y - z + xy + yz + zx - xyz;
        m_weight[1] = x - xy - zx + xyz;
        m_weight[2] = xy - xyz;
        m_weight[3] = y - xy - yz + xyz;
        m_weight[4] = z - zx - yz + xyz;
        m_weight[5] = zx - xyz;
        m_weight[6] = xyz;
        m_weight[7] = yz - xyz;
    }

    public final float scalar()
    {
        //const T* const data = reinterpret_cast<const T*>( m_reference_volume->values().pointer() );
        final Buffer buffer = m_reference_volume.values();

        if( buffer instanceof IntBuffer ){
            final IntBuffer data = (IntBuffer)buffer;
            final int limit = data.limit(); 
            return (data.get( m_index[0] < limit ? m_index[0] : 0 ) * m_weight[0] +
                    data.get( m_index[1] < limit ? m_index[1] : 0 ) * m_weight[1] +
                    data.get( m_index[2] < limit ? m_index[2] : 0 ) * m_weight[2] +
                    data.get( m_index[3] < limit ? m_index[3] : 0 ) * m_weight[3] +
                    data.get( m_index[4] < limit ? m_index[4] : 0 ) * m_weight[4] +
                    data.get( m_index[5] < limit ? m_index[5] : 0 ) * m_weight[5] +
                    data.get( m_index[6] < limit ? m_index[6] : 0 ) * m_weight[6] +
                    data.get( m_index[7] < limit ? m_index[7] : 0 ) * m_weight[7] );

        } else {
            return -1;
        }
    }
    
    public final Vector3f gradient()
    {
        // Calculate the point's gradient.
        float[] dx = new float[8];
        float[] dy = new float[8];
        float[] dz = new float[8];
        
        final Vector3i resolution = m_reference_volume.resolution();
        final int line_size  = m_reference_volume.nnodesPerLine();
        final int slice_size = m_reference_volume.nnodesPerSlice();

        final int i = m_grid_index.getX();
        final int j = m_grid_index.getY();
        final int k = m_grid_index.getZ();
        
        //float[] data = m_data;

        if ( i == 0 )
        {
            dx[0] = ( m_data.get( check_value( m_index[1]     ) )  );
            dx[1] = ( m_data.get( check_value( m_index[1] + 1 ) ) ) - ( m_data.get( check_value( m_index[0]     ) ) );
            dx[2] = ( m_data.get( check_value( m_index[2] + 1 ) ) ) - ( m_data.get( check_value( m_index[3]     ) ) );
            dx[3] = ( m_data.get( check_value( m_index[2]     ) ) );
            dx[4] = ( m_data.get( check_value( m_index[5]     ) ) );
            dx[5] = ( m_data.get( check_value( m_index[5] + 1 ) ) ) - ( m_data.get( check_value( m_index[4]     ) ) );
            dx[6] = ( m_data.get( check_value( m_index[6] + 1 ) ) ) - ( m_data.get( check_value( m_index[7]     ) ) );
            dx[7] = ( m_data.get( check_value( m_index[6]     ) ) );
        }
        else if ( i == resolution.getX() - 2 )
        {
            dx[0] = ( m_data.get( check_value( m_index[1]     ) ) ) - ( m_data.get( check_value( m_index[0] - 1 ) ) );
            dx[1] =                                             - ( m_data.get( check_value( m_index[0]     ) ) );
            dx[2] =                                             - ( m_data.get( check_value( m_index[3]     ) ) );
            dx[3] = ( m_data.get( check_value( m_index[2]     ) ) ) - ( m_data.get( check_value( m_index[3] - 1 ) ) );
            dx[4] = ( m_data.get( check_value( m_index[5]     ) ) ) - ( m_data.get( check_value( m_index[4] - 1 ) ) );
            dx[5] =                                             - ( m_data.get( check_value( m_index[4]     ) ) );
            dx[6] =                                             - ( m_data.get( check_value( m_index[7]     ) ) );
            dx[7] = ( m_data.get( check_value( m_index[6]     ) ) ) - ( m_data.get( check_value( m_index[7] - 1 ) ) );
        }
        else
        {
            dx[0] = ( m_data.get( check_value( m_index[1]     ) ) ) - ( m_data.get( check_value( m_index[0] - 1 ) ) );
            dx[1] = ( m_data.get( check_value( m_index[1] + 1 ) ) ) - ( m_data.get( check_value( m_index[0]     ) ) );
            dx[2] = ( m_data.get( check_value( m_index[2] + 1 ) ) ) - ( m_data.get( check_value( m_index[3]     ) ) );
            dx[3] = ( m_data.get( check_value( m_index[2]     ) ) ) - ( m_data.get( check_value( m_index[3] - 1 ) ) );
            dx[4] = ( m_data.get( check_value( m_index[5]     ) ) ) - ( m_data.get( check_value( m_index[4] - 1 ) ) );
            dx[5] = ( m_data.get( check_value( m_index[5] + 1 ) ) ) - ( m_data.get( check_value( m_index[4]     ) ) );
            dx[6] = ( m_data.get( check_value( m_index[6] + 1 ) ) ) - ( m_data.get( check_value( m_index[7]     ) ) );
            dx[7] = ( m_data.get( check_value( m_index[6]     ) ) ) - ( m_data.get( check_value( m_index[7] - 1 ) ) );
        }

        if ( j == 0 )
        {
            dy[0] = ( m_data.get( check_value( m_index[3]             ) ) );
            dy[1] = ( m_data.get( check_value( m_index[2]             ) ) );
            dy[2] = ( m_data.get( check_value( m_index[2] + line_size ) ) ) - ( m_data.get( check_value( m_index[1]             ) ) );
            dy[3] = ( m_data.get( check_value( m_index[3] + line_size ) ) ) - ( m_data.get( check_value( m_index[0]             ) ) );
            dy[4] = ( m_data.get( check_value( m_index[7]             ) ) );
            dy[5] = ( m_data.get( check_value( m_index[6]             ) ) );
            dy[6] = ( m_data.get( check_value( m_index[6] + line_size ) ) ) - ( m_data.get( check_value( m_index[5]             ) ) );
            dy[7] = ( m_data.get( check_value( m_index[7] + line_size ) ) ) - ( m_data.get( check_value( m_index[4]             ) ) );
        }
        else if ( j == resolution.getY() - 2 )
        {
            dy[0] = ( m_data.get( check_value( m_index[3]             ) ) ) - ( m_data.get( check_value( m_index[0] - line_size ) ) );
            dy[1] = ( m_data.get( check_value( m_index[2]             ) ) ) - ( m_data.get( check_value( m_index[1] - line_size ) ) );
            dy[2] =                                                     - ( m_data.get( check_value( m_index[1]             ) ) );
            dy[3] =                                                     - ( m_data.get( check_value( m_index[0]             ) ) );
            dy[4] = ( m_data.get( check_value( m_index[7]             ) ) ) - ( m_data.get( check_value( m_index[4] - line_size ) ) );
            dy[5] = ( m_data.get( check_value( m_index[6]             ) ) ) - ( m_data.get( check_value( m_index[5] - line_size ) ) );
            dy[6] =                                                     - ( m_data.get( check_value( m_index[5]             ) ) );
            dy[7] =                                                     - ( m_data.get( check_value( m_index[4]             ) ) );
        }
        else
        {
            dy[0] = ( m_data.get( check_value( m_index[3]             ) ) ) - ( m_data.get( check_value( m_index[0] - line_size ) ) );
            dy[1] = ( m_data.get( check_value( m_index[2]             ) ) ) - ( m_data.get( check_value( m_index[1] - line_size ) ) );
            dy[2] = ( m_data.get( check_value( m_index[2] + line_size ) ) ) - ( m_data.get( check_value( m_index[1]             ) ) );
            dy[3] = ( m_data.get( check_value( m_index[3] + line_size ) ) ) - ( m_data.get( check_value( m_index[0]             ) ) );
            dy[4] = ( m_data.get( check_value( m_index[7]             ) ) ) - ( m_data.get( check_value( m_index[4] - line_size ) ) );
            dy[5] = ( m_data.get( check_value( m_index[6]             ) ) ) - ( m_data.get( check_value( m_index[5] - line_size ) ) );
            dy[6] = ( m_data.get( check_value( m_index[6] + line_size ) ) ) - ( m_data.get( check_value( m_index[5]             ) ) );
            dy[7] = ( m_data.get( check_value( m_index[7] + line_size ) ) ) - ( m_data.get( check_value( m_index[4]             ) ) );
        }

        if ( k == 0 )
        {
            dz[0] = ( m_data.get( check_value( m_index[4]              ) ) );
            dz[1] = ( m_data.get( check_value( m_index[5]              ) ) );
            dz[2] = ( m_data.get( check_value( m_index[6]              ) ) );
            dz[3] = ( m_data.get( check_value( m_index[7]              ) ) );
            dz[4] = ( m_data.get( check_value( m_index[4] + slice_size ) ) ) - ( m_data.get( check_value( m_index[0]              ) ) );
            dz[5] = ( m_data.get( check_value( m_index[5] + slice_size ) ) ) - ( m_data.get( check_value( m_index[1]              ) ) );
            dz[6] = ( m_data.get( check_value( m_index[6] + slice_size ) ) ) - ( m_data.get( check_value( m_index[2]              ) ) );
            dz[7] = ( m_data.get( check_value( m_index[7] + slice_size ) ) ) - ( m_data.get( check_value( m_index[3]              ) ) );
        }
        else if ( k == resolution.getZ() - 2 )
        {
            dz[0] = ( m_data.get( check_value( m_index[4]              ) ) ) - ( m_data.get( check_value( m_index[0] - slice_size ) ) );
            dz[1] = ( m_data.get( check_value( m_index[5]              ) ) ) - ( m_data.get( check_value( m_index[1] - slice_size ) ) );
            dz[2] = ( m_data.get( check_value( m_index[6]              ) ) ) - ( m_data.get( check_value( m_index[2] - slice_size ) ) );
            dz[3] = ( m_data.get( check_value( m_index[7]              ) ) ) - ( m_data.get( check_value( m_index[3] - slice_size ) ) );
            dz[4] =                                                      - ( m_data.get( check_value( m_index[0]              ) ) );
            dz[5] =                                                      - ( m_data.get( check_value( m_index[1]              ) ) );
            dz[6] =                                                      - ( m_data.get( check_value( m_index[2]              ) ) );
            dz[7] =                                                      - ( m_data.get( check_value( m_index[3]              ) ) );
        }
        else
        {
            dz[0] = ( m_data.get( check_value( m_index[4]              ) ) ) - ( m_data.get( check_value( m_index[0] - slice_size ) ) );
            dz[1] = ( m_data.get( check_value( m_index[5]              ) ) ) - ( m_data.get( check_value( m_index[1] - slice_size ) ) );
            dz[2] = ( m_data.get( check_value( m_index[6]              ) ) ) - ( m_data.get( check_value( m_index[2] - slice_size ) ) );
            dz[3] = ( m_data.get( check_value( m_index[7]              ) ) ) - ( m_data.get( check_value( m_index[3] - slice_size ) ) );
            dz[4] = ( m_data.get( check_value( m_index[4] + slice_size ) ) ) - ( m_data.get( check_value( m_index[0]              ) ) );
            dz[5] = ( m_data.get( check_value( m_index[5] + slice_size ) ) ) - ( m_data.get( check_value( m_index[1]              ) ) );
            dz[6] = ( m_data.get( check_value( m_index[6] + slice_size ) ) ) - ( m_data.get( check_value( m_index[2]              ) ) );
            dz[7] = ( m_data.get( check_value( m_index[7] + slice_size ) ) ) - ( m_data.get( check_value( m_index[3]              ) ) );
        }

        final float x =
            dx[0] * m_weight[0] +
            dx[1] * m_weight[1] +
            dx[2] * m_weight[2] +
            dx[3] * m_weight[3] +
            dx[4] * m_weight[4] +
            dx[5] * m_weight[5] +
            dx[6] * m_weight[6] +
            dx[7] * m_weight[7];

        final float y =
            dy[0] * m_weight[0] +
            dy[1] * m_weight[1] +
            dy[2] * m_weight[2] +
            dy[3] * m_weight[3] +
            dy[4] * m_weight[4] +
            dy[5] * m_weight[5] +
            dy[6] * m_weight[6] +
            dy[7] * m_weight[7];

        final float z =
            dz[0] * m_weight[0] +
            dz[1] * m_weight[1] +
            dz[2] * m_weight[2] +
            dz[3] * m_weight[3] +
            dz[4] * m_weight[4] +
            dz[5] * m_weight[5] +
            dz[6] * m_weight[6] +
            dz[7] * m_weight[7];

        return( new Vector3f( x, y, z ) );
    }
    
    private int check_value( int value ){   //TODO 改良の余地あり
        return value < m_data_limit ? value : 0;
    }


}
