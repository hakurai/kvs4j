package kvs.core.visualization.mapper;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import kvs.core.KVSException;
import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector3i;
import kvs.core.util.Utility;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PolygonObject;
import kvs.core.visualization.object.StructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase;
import kvs.core.visualization.object.ObjectBase.ObjectType;
import kvs.core.visualization.object.PolygonObject.ColorType;
import kvs.core.visualization.object.PolygonObject.NormalType;
import kvs.core.visualization.object.PolygonObject.PolygonType;
import kvs.core.visualization.object.VolumeObjectBase.VolumeType;

public class MarchingCubes extends MapperBase {

    private static final long serialVersionUID = 8672567272642924408L;
    private double  m_isolevel      = 0;    ///< isosurface level
    private boolean m_duplication   = true; ///< duplication flag

    private PolygonObject m_object = new PolygonObject();
    private double[]  baseValues; 


    public MarchingCubes(){
        super();
    }

    public MarchingCubes(
                         //final StructuredVolumeObject volume,
                         final double                       isolevel,
                         final NormalType                   normal_type,
                         final boolean                      duplication,
                         final TransferFunction       transfer_function )
    {
        super(transfer_function);
        m_duplication = duplication;
        m_object.setNormalType( normal_type );

        this.setIsolevel( isolevel );

        // Extract the surfaces.
        //this.exec( volume );
    }

    public void setIsolevel( final double isolevel )
    {
        m_isolevel = isolevel;
    }

    @Override
    public ObjectBase exec( ObjectBase object ) throws KVSException {
        final ObjectType object_type = object.objectType();
        if ( object_type == ObjectType.Geometry )
        {
            throw new KVSException("Geometry object is not supported.");
        }

        // In the case of VertexNormal-type, the duplicated vertices are forcibly deleted.
        if ( m_object.normalType() == NormalType.VertexNormal )
        {
            m_duplication = false;
        }

        final VolumeObjectBase volume = (VolumeObjectBase)object;
        final VolumeType volume_type = volume.volumeType();
        if ( volume_type == VolumeType.Structured )
        {
            this.mapping( (StructuredVolumeObject)object );
        }
        else // volume_type == kvs::VolumeObjectBase::Unstructured
        {
            throw new KVSException("Unstructured volume object is not supported.");
        }

        return( m_object );
    }

    private void mapping( final StructuredVolumeObject volume ) throws KVSException
    {
        // Check whether the volume can be processed or not.
        if ( volume.veclen() != 1 )
        {
            throw new KVSException("The input volume is not a sclar field data.");
        }

        // Attach the pointer to the volume object.
        attach_volume( volume );

        // Set the min/max coordinates.
        set_min_max_coords( volume, m_object );

        // Extract surfaces.
        extract_surfaces( volume );
    }

    private void extract_surfaces( final StructuredVolumeObject volume )
    {
        baseValues = Utility.bufferToDoubleArray( m_volume.values() );
        if ( m_duplication ) this.extract_surfaces_with_duplication( volume );
        else                 this.extract_surfaces_without_duplication( volume );
    }

    private void extract_surfaces_with_duplication( final StructuredVolumeObject volume )
    {
        // Calculated the coordinate data array and the normal vector array.
        List<Float> coords  = new ArrayList<Float>();
        List<Float> normals = new ArrayList<Float>();

        final Vector3i  ncells = volume.resolution().sub( new Vector3i(1) );
        final int       line_size = volume.nnodesPerLine();
        final int       slice_size = volume.nnodesPerSlice();

        final double[]  values = Utility.bufferToDoubleArray(volume.values());
        
        // Extract surfaces.
        int index = 0;
        int[] local_index = new int[8];
        for ( int z = 0; z < ncells.getZ(); ++z )
        {

            for ( int y = 0; y < ncells.getY(); ++y )
            {
                for ( int x = 0; x < ncells.getX(); ++x )
                {
                    // Calculate the indices of the target cell.
                    local_index[0] = index;
                    local_index[1] = local_index[0] + 1;
                    local_index[2] = local_index[1] + line_size;
                    local_index[3] = local_index[0] + line_size;
                    local_index[4] = local_index[0] + slice_size;
                    local_index[5] = local_index[1] + slice_size;
                    local_index[6] = local_index[2] + slice_size;
                    local_index[7] = local_index[3] + slice_size;
                    index++;

                    // Calculate the index of the reference table.
                    final int table_index = this.calculate_table_index( values, local_index );
                    if ( table_index == 0 ) continue;
                    if ( table_index == 255 ) continue;

                    // Calculate the triangle polygons.
                    for ( int i = 0; MarchingCubesTable.TriangleID[ table_index ][i] != -1; i += 3 )
                    {
                        // Refer the edge IDs from the TriangleTable by using the table_index.
                        final int e0 = MarchingCubesTable.TriangleID[table_index][i];
                        final int e1 = MarchingCubesTable.TriangleID[table_index][i+2];
                        final int e2 = MarchingCubesTable.TriangleID[table_index][i+1];

                        // Determine vertices for each edge.
                        final Vector3f v0 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e0][0][0],
                                y + MarchingCubesTable.VertexID[e0][0][1],
                                z + MarchingCubesTable.VertexID[e0][0][2] );

                        final Vector3f v1 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e0][1][0],
                                y + MarchingCubesTable.VertexID[e0][1][1],
                                z + MarchingCubesTable.VertexID[e0][1][2] );

                        final Vector3f v2 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e1][0][0],
                                y + MarchingCubesTable.VertexID[e1][0][1],
                                z + MarchingCubesTable.VertexID[e1][0][2] );

                        final Vector3f v3 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e1][1][0],
                                y + MarchingCubesTable.VertexID[e1][1][1],
                                z + MarchingCubesTable.VertexID[e1][1][2] );

                        final Vector3f v4 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e2][0][0],
                                y + MarchingCubesTable.VertexID[e2][0][1],
                                z + MarchingCubesTable.VertexID[e2][0][2] );

                        final Vector3f v5 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e2][1][0],
                                y + MarchingCubesTable.VertexID[e2][1][1],
                                z + MarchingCubesTable.VertexID[e2][1][2] );

                        // Calculate coordinates of the vertices which are composed
                        // of the triangle polygon.
                        final Vector3f vertex0 = this.interpolate_vertex( v0, v1 );
                        coords.add( vertex0.getX() );
                        coords.add( vertex0.getY() );
                        coords.add( vertex0.getZ() );

                        final Vector3f vertex1 = this.interpolate_vertex( v2, v3 );
                        coords.add( vertex1.getX() );
                        coords.add( vertex1.getY() );
                        coords.add( vertex1.getZ() );

                        final Vector3f vertex2 = this.interpolate_vertex( v4, v5 );
                        coords.add( vertex2.getX() );
                        coords.add( vertex2.getY() );
                        coords.add( vertex2.getZ() );

                        // Calculate a normal vector for the triangle polygon.
                        final Vector3f normal =  ( vertex1.sub( vertex0) ).cross( vertex2.sub( vertex0 ) );
                        normals.add( normal.getX() );
                        normals.add( normal.getY() );
                        normals.add( normal.getZ() );
                    } // end of loop-triangle
                } // end of loop-x
                ++index;
            } // end of loop-y
            index += line_size;
        } // end of loop-z

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

    private void extract_surfaces_without_duplication(
                                                      final StructuredVolumeObject volume )
    {
        final int volume_size = volume.nnodes();
        final int byte_size   = 4 * 3 * volume_size;
        int[] vertex_map = new int[byte_size];

        List<Float> coords  = new ArrayList<Float>();
        this.calculate_isopoints( vertex_map, coords );


        int[] connections = this.connect_isopoints( vertex_map );

        vertex_map = null;

        float[] normals = null;
        float[] coords_array = Utility.ListToFloatArray( coords );
        if ( m_object.normalType() == NormalType.PolygonNormal )
        {
            normals = this.calculate_normals_on_polygon( coords_array, connections );
        }
        else
        {
            normals = this.calculate_normals_on_vertex( coords_array, connections );
        }

        // Calculate the polygon color for the isolevel.
        final Color color = this.calculate_color();

        m_object.setCoords( Utility.ListToFloatArray( coords ) );
        m_object.setConnections( connections );
        m_object.setColor( color );
        m_object.setNormals( normals );
        m_object.setOpacity( (byte)255 );
        m_object.setPolygonType( PolygonType.Triangle );
        m_object.setColorType( ColorType.PolygonColor );
    }

    private int calculate_table_index( final double[] values, final int[] local_index )
    {
        //final T* const values = static_cast<const T*>( m_volume.values().pointer() );
        final double isolevel = m_isolevel;

        int table_index = 0;
        if ( ( values[ local_index[0] ] ) > isolevel ) { table_index |=   1; }
        if ( ( values[ local_index[1] ] ) > isolevel ) { table_index |=   2; }
        if ( ( values[ local_index[2] ] ) > isolevel ) { table_index |=   4; }
        if ( ( values[ local_index[3] ] ) > isolevel ) { table_index |=   8; }
        if ( ( values[ local_index[4] ] ) > isolevel ) { table_index |=  16; }
        if ( ( values[ local_index[5] ] ) > isolevel ) { table_index |=  32; }
        if ( ( values[ local_index[6] ] ) > isolevel ) { table_index |=  64; }
        if ( ( values[ local_index[7] ] ) > isolevel ) { table_index |= 128; }

        return( table_index );
    }

    private Vector3f interpolate_vertex(
                                        final Vector3f vertex0,
                                        final Vector3f vertex1 )
    {
        //const T* const values = static_cast<const T*>( BaseClass::m_volume->values().pointer() );
        //final double[] values = BufferUtility.bufferToDoubleArray( m_volume.values() );
        StructuredVolumeObject volume = (StructuredVolumeObject) m_volume;


        final int line_size  = volume.nnodesPerLine();
        final int slice_size = volume.nnodesPerSlice();

        final int v0_index = (int)( vertex0.getX() + vertex0.getY() * line_size + vertex0.getZ() * slice_size );
        final int v1_index = (int)( vertex1.getX() + vertex1.getY() * line_size + vertex1.getZ() * slice_size );

        final double v0 =  baseValues[ v0_index ];
        final double v1 =  baseValues[ v1_index ];
        final float ratio = (float)( kvs.core.util.Math.abs( ( m_isolevel - v0 ) / ( v1 - v0 ) ) );

        final float x = ( 1.0f - ratio ) * vertex0.getX() + ratio * vertex1.getX();
        final float y = ( 1.0f - ratio ) * vertex0.getY() + ratio * vertex1.getY();
        final float z = ( 1.0f - ratio ) * vertex0.getZ() + ratio * vertex1.getZ();

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
        if( index > 255 ){
            index = 255;
        }
        if( index < 0 ){
            index = 0;
        }

        return( transferFunction().colorMap().getAt( index ) );
    }

    private void calculate_isopoints(
                                     int[]             vertex_map,
                                     List<Float> coords )
    {
        final double[] values = Utility.bufferToDoubleArray( m_volume.values() );
        
        final StructuredVolumeObject volume = (StructuredVolumeObject)m_volume;

        final Vector3i resolution   = volume.resolution();
        final Vector3i ncells       = resolution.sub( new Vector3i(1) );
        final int      line_size    = volume.nnodesPerLine();
        final int      slice_size   = volume.nnodesPerSlice();
        final double   isolevel     = m_isolevel;

        int nisopoints = 0;
        int index = 0;
        for ( int z = 0; z < resolution.getZ(); ++z )
        {
            for ( int y = 0; y < resolution.getY(); ++y )
            {
                for ( int x = 0; x < resolution.getX(); ++x )
                {
                    final int id0 = index;
                    final int id1 = id0 + 1;
                    final int id2 = id0 + line_size;
                    final int id3 = id0 + slice_size;

                    if ( x != ncells.getX() )
                    {
                        if ( ( values[id0] > isolevel ) !=
                            ( values[id1] > isolevel ) )
                        {
                            final Vector3f v1 = new Vector3f( x,   y, z );
                            final Vector3f v2 = new Vector3f( x+1, y, z );
                            final Vector3f isopoint = this.interpolate_vertex( v1, v2 );

                            coords.add( isopoint.getX() );
                            coords.add( isopoint.getY() );
                            coords.add( isopoint.getZ() );

                            vertex_map[ 3 * index ] = nisopoints++;
                        }
                    }

                    if ( y != ncells.getY() )
                    {
                        if ( ( ( values[id0] ) > isolevel ) !=
                            ( ( values[id2] ) > isolevel ) )
                        {
                            final Vector3f v1 = new Vector3f( x,   y, z );
                            final Vector3f v2 = new Vector3f( x, y+1, z );
                            final Vector3f isopoint = this.interpolate_vertex( v1, v2 );

                            coords.add( isopoint.getX() );
                            coords.add( isopoint.getY() );
                            coords.add( isopoint.getZ() );

                            vertex_map[ 3 * index + 1 ] = nisopoints++;
                        }
                    }

                    if ( z != ncells.getZ() )
                    {
                        if ( ( ( values[id0] ) > isolevel ) !=
                            ( ( values[id3] ) > isolevel ) )
                        {
                            final Vector3f v1 = new Vector3f( x,   y, z );
                            final Vector3f v2 = new Vector3f( x, y, z+1 );
                            final Vector3f isopoint = this.interpolate_vertex( v1, v2 );

                            coords.add( isopoint.getX() );
                            coords.add( isopoint.getY() );
                            coords.add( isopoint.getZ() );

                            vertex_map[ 3 * index + 2 ] = nisopoints++;
                        }
                    }
                    ++index;
                } // x
            } // y
        } // z
    }

    
    private int[] connect_isopoints( int[] vertex_map )
    {
        final StructuredVolumeObject volume = (StructuredVolumeObject)m_volume;

        final Vector3i resolution   = volume.resolution();
        final Vector3i ncells       = resolution.sub( new Vector3i(1) );
        final int      line_size    = volume.nnodesPerLine();
        final int      slice_size   = volume.nnodesPerSlice();
        //final double[]  values = Utility.bufferToDoubleArray(volume.values());

        
        List<Integer> connections = new ArrayList<Integer>();

        int   index       = 0;
        int[] local_index = new int[8];
        int[] local_edge  = new int[12];
        for ( int z = 0; z < ncells.getZ(); ++z )
        {
            for ( int y = 0; y < ncells.getY(); ++y )
            {
                for ( int x = 0; x < ncells.getX(); ++x )
                {
                    // Calculate the indices of the target cell.
                    local_index[0] = index;
                    local_index[1] = local_index[0] + 1;
                    local_index[2] = local_index[1] + line_size;
                    local_index[3] = local_index[0] + line_size;
                    local_index[4] = local_index[0] + slice_size;
                    local_index[5] = local_index[1] + slice_size;
                    local_index[6] = local_index[2] + slice_size;
                    local_index[7] = local_index[3] + slice_size;
                    index++;

                    // Calculate the index of the reference table.
                    final int table_index = this.calculate_table_index( baseValues,local_index );
                    if ( table_index == 0 ) continue;
                    if ( table_index == 255 ) continue;

                    local_edge[ 0] = 3 * local_index[0];
                    local_edge[ 1] = local_edge[0] + 3 + 1;
                    local_edge[ 2] = local_edge[0] + 3 * line_size;
                    local_edge[ 3] = local_edge[0] + 1;
                    local_edge[ 4] = local_edge[0] + 3 * slice_size;
                    local_edge[ 5] = local_edge[1] + 3 * slice_size;
                    local_edge[ 6] = local_edge[2] + 3 * slice_size;
                    local_edge[ 7] = local_edge[3] + 3 * slice_size;
                    local_edge[ 8] = local_edge[0] + 2;
                    local_edge[ 9] = local_edge[8] + 3;
                    local_edge[10] = local_edge[8] + 3 + 3 * line_size;
                    local_edge[11] = local_edge[8] + 3 * line_size;

                    for ( int i = 0; MarchingCubesTable.TriangleID[table_index][i] != -1; i += 3 )
                    {
                        final int e0 = local_edge[ MarchingCubesTable.TriangleID[table_index][i]   ];
                        final int e1 = local_edge[ MarchingCubesTable.TriangleID[table_index][i+2] ];
                        final int e2 = local_edge[ MarchingCubesTable.TriangleID[table_index][i+1] ];

                        connections.add( vertex_map[e0] );
                        connections.add( vertex_map[e1] );
                        connections.add( vertex_map[e2] );
                    }
                } // x
                ++index;
            } // y
            index += line_size;
        } // z
        
        return Utility.ListToIntArray( connections );
    }


    private float[] calculate_normals_on_polygon(
                                              final float[] coords,
                                              final int[] connections )
    {
        if ( coords.length == 0 ) return new float[0];

        float[] normals = new float[connections.length];

        //final float coords_ptr = coords.get( 0 );

        final int size = connections.length;
        for ( int index = 0; index < size; index += 3 )
        {
            final int coord0_index = 3 * connections[ index     ];
            final int coord1_index = 3 * connections[ index + 1 ];
            final int coord2_index = 3 * connections[ index + 2 ];

            final Vector3f v0 = new Vector3f( coords, coord0_index  );
            final Vector3f v1 = new Vector3f( coords, coord1_index  );
            final Vector3f v2 = new Vector3f( coords, coord2_index  );

            final Vector3f normal = ( v1.sub( v0 ) ).cross( v2.sub( v0 ) );

            normals[ index     ] = normal.getX();
            normals[ index + 1 ] = normal.getY();
            normals[ index + 2 ] = normal.getZ();
        }
        return normals;
    }
    
    private float[] calculate_normals_on_vertex(
                                             final float[] coords,
                                             final int[] connections)
    {
        if ( coords.length == 0 ) return new float[0];

        float[] normals = new float[coords.length];
        //std::fill( normals.begin(), normals.end(), 0.0f );

        //final float coords_ptr = coords.get( 0 );

        final int size = connections.length;
        for ( int index = 0; index < size; index += 3 )
        {
            
            final int coord0_index = 3 * connections[ index     ];
            final int coord1_index = 3 * connections[ index + 1 ];
            final int coord2_index = 3 * connections[ index + 2 ];

            final Vector3f v0 = new Vector3f( coords, coord0_index  );
            final Vector3f v1 = new Vector3f( coords, coord1_index  );
            final Vector3f v2 = new Vector3f( coords, coord2_index  );

            final Vector3f normal = ( v1.sub( v0 ) ).cross( v2.sub( v0 ) );

            normals[ coord0_index     ] += normal.getX();
            normals[ coord0_index + 1 ] += normal.getY();
            normals[ coord0_index + 2 ] += normal.getZ();

            normals[ coord1_index     ] += normal.getX();
            normals[ coord1_index + 1 ] += normal.getY();
            normals[ coord1_index + 2 ] += normal.getZ();

            normals[ coord2_index     ] += normal.getX();
            normals[ coord2_index + 1 ] += normal.getY();
            normals[ coord2_index + 2 ] += normal.getZ();
        }
        return normals;
    }

}
