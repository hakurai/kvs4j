package kvs.core.visualization.object;

import java.nio.Buffer;

import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector3i;

public class StructuredVolumeObject extends VolumeObjectBase {

    private static final long serialVersionUID = 4977151939612674873L;
    private GridType m_grid_type = GridType.UnknownGridType; // /< Grid type.
    private Vector3i m_resolution = Vector3i.ZERO; // /< Node resolution.

    public StructuredVolumeObject() {
        super();
    }

    public StructuredVolumeObject( Vector3i resolution, int veclen, Buffer values ) {
        super( veclen, null, values );
        m_grid_type = GridType.Uniform;
        m_resolution = resolution;
    }

    public StructuredVolumeObject(
            GridType grid_type,
            Vector3i resolution,
            int veclen,
            float[] coords,
            Buffer values ) {
        super( veclen, coords, values );
        m_grid_type = grid_type;
        m_resolution = resolution;
    }

    public void setGridType( GridType grid_type ) {
        m_grid_type = grid_type;
    }

    public void setResolution( Vector3i resolution ) {
        m_resolution = resolution;
    }

    @Override
    public VolumeType volumeType() {
        return (VolumeType.Structured);
    }

    @Override
    public GridType gridType() {
        return (m_grid_type);
    }

    @Override
    public CellType cellType() {
        return (CellType.Hexahedra);
    }

    public Vector3i resolution() {
        return (m_resolution);
    }

    public int nnodesPerLine() {
        return (m_resolution.getX());
    }

    public int nnodesPerSlice() {
        return (this.nnodesPerLine() * m_resolution.getY());
    }

    @Override
    public int nnodes() {
        return (this.nnodesPerSlice() * m_resolution.getZ());
    }

    public void updateMinMaxCoords() {
        this.calculate_min_max_coords();
    }

    private void calculate_min_max_coords() {
        Vector3f min_coord = Vector3f.ZERO;
        Vector3f max_coord = Vector3f.ZERO;

        switch (m_grid_type) {
        case Uniform: {
            min_coord = Vector3f.ZERO;
            max_coord = new Vector3f(
                    (m_resolution.getX()) - 1.0f,
                    (m_resolution.getY()) - 1.0f,
                    (m_resolution.getZ()) - 1.0f );

            break;
        }
        case Rectilinear: {
            final float[] coord = this.coords();

            min_coord = new Vector3f(
                    coord[0],
                    coord[ m_resolution.getX() ],
                    coord[ m_resolution.getX() + m_resolution.getY() ]);

            max_coord = new Vector3f(
                    coord[ m_resolution.getX() - 1],
                    coord[ m_resolution.getX() + m_resolution.getY() - 1],
                    coord[ m_resolution.getX() + m_resolution.getY() + m_resolution.getY() - 1 ]);

            break;
        }
        case Curvilinear: {
            final float[] coord = this.coords();
            final int end = this.coords().length;

            int index = 0;

            float x = coord[index++];
            float y = coord[index++];
            float z = coord[index++];

            min_coord = new Vector3f( x, y, z );
            max_coord = new Vector3f( x, y, z );

            while (index < end) {
                x = coord[index++];
                y = coord[index++];
                z = coord[index++];

                min_coord = new Vector3f(
                        kvs.core.util.Math.min( min_coord.getX(), x ),
                        kvs.core.util.Math.min( min_coord.getY(), y ),
                        kvs.core.util.Math.min( min_coord.getZ(), z ) );

                max_coord = new Vector3f(
                        kvs.core.util.Math.max( max_coord.getX(), x ),
                        kvs.core.util.Math.max( max_coord.getY(), y ),
                        kvs.core.util.Math.max( max_coord.getZ(), z ) );
            }

            break;
        }
        default: {
            break;
        }
        }

        this.setMinMaxObjectCoords( min_coord, max_coord );

        if (!(this.hasMinMaxExternalCoords())) {
            this.setMinMaxExternalCoords( this.minObjectCoord(), this.maxObjectCoord() );
        }
    }
}
