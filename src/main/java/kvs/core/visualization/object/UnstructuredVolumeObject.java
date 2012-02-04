package kvs.core.visualization.object;

import java.nio.Buffer;

import kvs.core.matrix.Vector3f;

public class UnstructuredVolumeObject extends VolumeObjectBase {

    private static final long serialVersionUID = 8269085918374697004L;
    private CellType m_cell_type = CellType.UnknownCellType; ///< Cell type.
    private int   m_nnodes = 0;    ///< Number of nodes.
    private int   m_ncells = 0;    ///< Number of cells.

    private int[] m_connections; ///< Connection ( Node ID ) array.

    public UnstructuredVolumeObject()
    {
    }

    public UnstructuredVolumeObject(
                                    CellType     cell_type,
                                    int       nnodes,
                                    int       ncells,
                                    int       veclen,
                                    float[]      coords,
                                    int[] connections,
                                    Buffer      values )
    {
        super( veclen, coords, values );
        m_cell_type = cell_type;
        m_nnodes = nnodes;
        m_ncells = ncells;
        m_connections = connections;
    }
    /*
    public UnstructuredVolumeObject( UnstructuredVolumeObject other )

    {
        super( other );
        m_cell_type = other.m_cell_type;
        m_nnodes = other.m_nnodes;
        m_ncells = other.m_ncells;
        m_connections = other.m_connections;
    }
     */
    
    public void setCellType( CellType cell_type )
    {
        m_cell_type = cell_type;
    }
    
    public void setNNodes( int nnodes )
    {
        m_nnodes = nnodes;
    }
    
    public void setNCells( int ncells )
    {
        m_ncells = ncells;
    }
    
    public void setConnections( int[] connections )
    {
        m_connections = connections;
    }
    
    public VolumeType volumeType()
    {
        return( VolumeType.Unstructured );
    }
    
    public GridType gridType()
    {
        return( GridType.Irregular );
    }
    
    public CellType cellType()
    {
        return( m_cell_type );
    }
    
    public int nnodes()
    {
        return( m_nnodes );
    }
    
    public int ncells()
    {
        return( m_ncells );
    }
    
    public int[] connections()
    {
        return( m_connections );
    }

    public void updateMinMaxCoords()
    {
        this.calculate_min_max_coords();
    }
    
    public void calculate_min_max_coords()
    {
        Vector3f min_coord;
        Vector3f max_coord;

        final float[]       coord = this.coords();
        final int end = this.coords().length;
        
        int index = 0;

        float x = coord[index++];
        float y = coord[index++];
        float z = coord[index++];

        float min_coord_x = x;
        float min_coord_y = y;
        float min_coord_z = z;
        
        float max_coord_x = x;
        float max_coord_y = y;
        float max_coord_z = z;
        

        while ( index < end )
        {
            x = coord[index++];
            y = coord[index++];
            z = coord[index++];

            min_coord_x = kvs.core.util.Math.min( min_coord_x, x );
            min_coord_y = kvs.core.util.Math.min( min_coord_y, y );
            min_coord_z = kvs.core.util.Math.min( min_coord_z, z );

            max_coord_x = kvs.core.util.Math.max( max_coord_x, x );
            max_coord_y = kvs.core.util.Math.max( max_coord_y, y );
            max_coord_z = kvs.core.util.Math.max( max_coord_z, z );
        }
        
        min_coord = new Vector3f( min_coord_x, min_coord_y, min_coord_z );
        max_coord = new Vector3f( max_coord_x, max_coord_y, max_coord_z );

        this.setMinMaxObjectCoords( min_coord, max_coord );

        if ( !( this.hasMinMaxExternalCoords() ) )
        {
            this.setMinMaxExternalCoords(
                this.minObjectCoord(),
                this.maxObjectCoord() );
        }
    }
}
