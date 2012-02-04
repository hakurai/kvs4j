package kvs.core.visualization.mapper;

import java.awt.Color;
import java.util.LinkedList;

import kvs.core.matrix.Vector3f;
import kvs.core.util.Utility;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PolygonObject;
import kvs.core.visualization.object.UnstructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase;
import kvs.core.visualization.object.ObjectBase.ObjectType;
import kvs.core.visualization.object.PolygonObject.ColorType;
import kvs.core.visualization.object.PolygonObject.NormalType;
import kvs.core.visualization.object.PolygonObject.PolygonType;
import kvs.core.visualization.object.VolumeObjectBase.CellType;
import kvs.core.visualization.object.VolumeObjectBase.VolumeType;

public class MarchingTetrahedra extends MapperBase {
    
    private static final long serialVersionUID = 3397780576456948308L;
    private double  m_isolevel      = 0;    ///< isosurface level
    private boolean m_duplication   = true; ///< duplication flag
    
    private PolygonObject m_object  = new PolygonObject();
    
    public MarchingTetrahedra(){
        super();
    }
    
    public MarchingTetrahedra(
            final double                         isolevel,
            final NormalType                     normal_type,
            final boolean                         duplication,
            final TransferFunction         transfer_function )
        {
                super(transfer_function);
                m_isolevel = isolevel;
                m_duplication = duplication;
            m_object.setNormalType( normal_type );

            // In the case of VertexNormal-type, the duplicated vertices are forcibly deleted.
            if ( normal_type == NormalType.VertexNormal )
            {
                m_duplication = false;
            }

            // Extract the surfaces.
            //this->exec( volume );
        }

    @Override
    public ObjectBase exec( ObjectBase object ) {
        final ObjectType object_type = object.objectType();
        if ( object_type == ObjectType.Geometry )
        {
            //kvsMessageError("Geometry object is not supported.");
            return( null );
        }

        final VolumeObjectBase volume = (VolumeObjectBase)object;
        final VolumeType volume_type = volume.volumeType();
        if ( volume_type == VolumeType.Unstructured )
        {
            this.mapping( (UnstructuredVolumeObject)object );
        }
        else // volume_type == kvs::VolumeObjectBase::Structured
        {
            //kvsMessageError("Unstructured volume object is not supported.");
            return( null );
        }

        return( m_object);
    }
    
    private void mapping( final UnstructuredVolumeObject volume )
    {
        // Check whether the volume can be processed or not.
        if ( volume.veclen() != 1 )
        {
            //kvsMessageError("The input volume is not a sclar field data.");
            return;
        }

        if ( volume.cellType() != CellType.Tetrahedra )
        {
            //kvsMessageError("The input volume is not tetrahedra cell data.");
            return;
        }

        // Attach the pointer to the volume object.
//        BaseClass::m_volume = volume;
        attach_volume( volume );

        // Set the min/max coordinates.
//        this->set_min_max_coords();
        set_min_max_coords( volume, m_object );

        // Extract surfaces.
        extract_surfaces( volume );
    }
    
    private void extract_surfaces( final UnstructuredVolumeObject volume )
    {
        if ( m_duplication ) this.extract_surfaces_with_duplication( volume );
        //else                 this.extract_surfaces_without_duplication( volume );
    }
    
    private void extract_surfaces_with_duplication(
                                                   final UnstructuredVolumeObject volume )
    {
        // Calculated the coordinate data array and the normal vector array.
        LinkedList<Float> coords = new LinkedList<Float>();
        LinkedList<Float> normals = new LinkedList<Float>();

        // Refer the unstructured volume object.
        final int[] connections = volume.connections();

        final int ncells = volume.ncells();
        
        double[] values = Utility.bufferToDoubleArray( m_volume.values() );

        // Extract surfaces.
        int index = 0;
        int[] local_index = new int[4];
        for ( int cell = 0; cell < ncells; ++cell, index += 4 )
        {
            // Calculate the indices of the target cell.
            local_index[0] = connections[ index ];
            local_index[1] = connections[ index + 1 ];
            local_index[2] = connections[ index + 2 ];
            local_index[3] = connections[ index + 3 ];

            // Calculate the index of the reference table.
            final int table_index = this.calculate_table_index( values, local_index );
            if ( table_index == 0 ) continue;
            if ( table_index == 15 ) continue;

            // Calculate the triangle polygons.
            for ( int i = 0; MarchingTetrahedraTable.TriangleID[ table_index ][i] != -1; i += 3 )
            {
                // Refer the edge IDs from the TriangleTable by using the table_index.
                final int e0 = MarchingTetrahedraTable.TriangleID[table_index][i];
                final int e1 = MarchingTetrahedraTable.TriangleID[table_index][i+1];
                final int e2 = MarchingTetrahedraTable.TriangleID[table_index][i+2];

                // Determine vertices for each edge.
                final int v0 = local_index[ MarchingTetrahedraTable.VertexID[e0][0] ];
                final int v1 = local_index[ MarchingTetrahedraTable.VertexID[e0][1] ];

                final int v2 = local_index[ MarchingTetrahedraTable.VertexID[e1][0] ];
                final int v3 = local_index[ MarchingTetrahedraTable.VertexID[e1][1] ];

                final int v4 = local_index[ MarchingTetrahedraTable.VertexID[e2][0] ];
                final int v5 = local_index[ MarchingTetrahedraTable.VertexID[e2][1] ];

                // Calculate coordinates of the vertices which are composed
                // of the triangle polygon.
                final Vector3f vertex0 = this.interpolate_vertex( values, v0, v1 );
                coords.add( vertex0.getX() );
                coords.add( vertex0.getY() );
                coords.add( vertex0.getZ() );

                final Vector3f vertex1 = this.interpolate_vertex( values, v2, v3 );
                coords.add( vertex1.getX() );
                coords.add( vertex1.getY() );
                coords.add( vertex1.getZ() );

                final Vector3f vertex2 = this.interpolate_vertex( values, v4, v5 );
                coords.add( vertex2.getX() );
                coords.add( vertex2.getY() );
                coords.add( vertex2.getZ() );

                // Calculate a normal vector for the triangle polygon.
                final Vector3f normal = ( vertex1.sub( vertex0 ) ).cross( vertex2.sub( vertex0 ) );
                normals.add( normal.getX() );
                normals.add( normal.getY() );
                normals.add( normal.getZ() );
            } // end of loop-triangle
        } // end of loop-cell

        // Calculate the polygon color for the isolevel.
        final Color color = this.calculate_color();

        m_object.setCoords( Utility.ListToFloatArray( coords ) );
        m_object.setColor( color );
        m_object.setNormals( Utility.ListToFloatArray( normals ) );
        m_object.setOpacity( (byte)255 );
        m_object.setPolygonType( PolygonType.Triangle );
        m_object.setColorType( ColorType.PolygonColor );
        m_object.setNormalType( NormalType.PolygonNormal );
    }
    
    private int calculate_table_index( double[] values, final int[] local_index )
    {
        final double isolevel = m_isolevel;

        int table_index = 0;
        if ( ( values[ local_index[0] ] ) > isolevel ) { table_index |=   1; }
        if ( ( values[ local_index[1] ] ) > isolevel ) { table_index |=   2; }
        if ( ( values[ local_index[2] ] ) > isolevel ) { table_index |=   4; }
        if ( ( values[ local_index[3] ] ) > isolevel ) { table_index |=   8; }

        return( table_index );
    }
    
    private Vector3f interpolate_vertex(
                                        double[] values,
                                        final int vertex0,
                                        final int vertex1 )
    {
        final float[] coords = m_volume.coords();

        final int coord0_index = 3 * vertex0;
        final int coord1_index = 3 * vertex1;

        final double v0 = values[ vertex0 ];
        final double v1 = values[ vertex1 ];
        final float ratio = (float)( kvs.core.util.Math.abs( ( m_isolevel - v0 ) / ( v1 - v0 ) ) );

        final float x = coords[coord0_index]   + ratio * ( coords[coord1_index]   - coords[coord0_index] );
        final float y = coords[coord0_index+1] + ratio * ( coords[coord1_index+1] - coords[coord0_index+1] );
        final float z = coords[coord0_index+2] + ratio * ( coords[coord1_index+2] - coords[coord0_index+2] );

        return( new Vector3f( x, y, z ) );
    }
    
    private Color calculate_color()
    {
        // Calculate the min/max values of the node data.
        if ( !m_volume.hasMinMaxValues() )
        {
            m_volume.updateMinMaxValues();
        }

        final double min_value = m_volume.minValue();
        final double max_value = m_volume.maxValue();
        final double normalize_factor = 255.0 / ( max_value - min_value );
        int  index = (int)( normalize_factor * ( m_isolevel - min_value ) );
        if( index < 0 ) index = 0;
        if( index > 255 ) index = 255;

        return( transferFunction().colorMap().getAt( index ) );
    }


}
