/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kvs.core.visualization.mapper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import kvs.core.KVSException;
import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector3i;
import kvs.core.matrix.Vector4f;
import kvs.core.util.Utility;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.ObjectBase.ObjectType;
import kvs.core.visualization.object.PolygonObject;
import kvs.core.visualization.object.StructuredVolumeObject;
import kvs.core.visualization.object.UnstructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase;

/**
 *
 * @author eguchi
 */
public class SlicePlane extends MapperBase{

    private static final long serialVersionUID = 1L;
    private Vector4f m_coefficients; ///< coeficients of a slice plane
    PolygonObject m_object = new PolygonObject();

    public SlicePlane(){
        super();
    }

    public SlicePlane( Vector4f coefficients ){
        super();
        m_coefficients = coefficients;
    }

    public SlicePlane( Vector4f coefficients,
            TransferFunction transfer_function ){
        super( transfer_function );
        m_coefficients = coefficients;
    }

    public SlicePlane( Vector3f point,
            Vector3f norm ){
        super();
        m_coefficients = new Vector4f( norm, -point.dot( norm ) );
    }

    public SlicePlane( Vector3f point,
            Vector3f norm,
            TransferFunction transfer_function ){
        super( transfer_function );
        m_coefficients = new Vector4f( norm, -point.dot( norm ) );
    }

    @Override
    public ObjectBase exec( ObjectBase object ) throws KVSException{
        ObjectType type = object.objectType();
        if ( type == ObjectType.Volume ){
            this.mapping( (VolumeObjectBase) object );
        } else{
            throw new KVSException( "Geometry object is not supported." );
        }

        return m_object;
    }

    private void mapping( VolumeObjectBase volume ) throws KVSException{
        // Check whether the volume can be processed or not.
        if ( volume.veclen() != 1 ){
            throw new KVSException( "The input volume is not a sclar field data." );
        }

        // Attach the pointer to the volume object.
        //    BaseClass.m_volume = volume;
        attach_volume( volume );

        // Set the min/max coordinates.
        //    this.set_min_max_coords();
        set_min_max_coords( volume, m_object );

        if ( volume.volumeType() == VolumeObjectBase.VolumeType.Structured ){
            final StructuredVolumeObject structured_volume = (StructuredVolumeObject) volume;

            extract_plane( structured_volume );
        } else{
            final UnstructuredVolumeObject unstructured_volume = (UnstructuredVolumeObject) volume;
            extract_plane( unstructured_volume );

        }
    }

    private void extract_plane( final StructuredVolumeObject volume ) throws KVSException{
        // Calculated the coordinate data array and the normal vector array.
        ArrayList<Float> coords = new ArrayList<Float>();
        ArrayList<Float> normals = new ArrayList<Float>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Calculate min/max values of the node data.
        if ( !volume.hasMinMaxValues() ){
            volume.updateMinMaxValues();
        }

        // Calculate a normalize_factor.
        double min_value = volume.minValue();
        double max_value = volume.maxValue();
        double normalize_factor = 255.0 / (max_value - min_value);

        Vector3i ncells = volume.resolution().sub( new Vector3i( 1 ) );
        int line_size = volume.nnodesPerLine();
        ColorMap color_map = transferFunction().colorMap();

        // Extract surfaces.
        int index = 0;
        for ( int z = 0; z < ncells.getZ(); ++z ){
            for ( int y = 0; y < ncells.getY(); ++y ){
                for ( int x = 0; x < ncells.getX(); ++x ){
                    // Calculate the index of the reference table.
                    int table_index = this.calculate_table_index( x, y, z );
                    if ( table_index == 0 ){
                        continue;
                    }
                    if ( table_index == 255 ){
                        continue;
                    }

                    // Calculate the triangle polygons.
                    for ( int i = 0; MarchingCubesTable.TriangleID[table_index][i] != -1; i += 3 ){
                        // Refer the edge IDs from the TriangleTable by using the table_index.
                        int e0 = MarchingCubesTable.TriangleID[table_index][i];
                        int e1 = MarchingCubesTable.TriangleID[table_index][i + 2];
                        int e2 = MarchingCubesTable.TriangleID[table_index][i + 1];

                        // Determine vertices for each edge.
                        Vector3f v0 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e0][0][0],
                                y + MarchingCubesTable.VertexID[e0][0][1],
                                z + MarchingCubesTable.VertexID[e0][0][2] );

                        Vector3f v1 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e0][1][0],
                                y + MarchingCubesTable.VertexID[e0][1][1],
                                z + MarchingCubesTable.VertexID[e0][1][2] );

                        Vector3f v2 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e1][0][0],
                                y + MarchingCubesTable.VertexID[e1][0][1],
                                z + MarchingCubesTable.VertexID[e1][0][2] );

                        Vector3f v3 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e1][1][0],
                                y + MarchingCubesTable.VertexID[e1][1][1],
                                z + MarchingCubesTable.VertexID[e1][1][2] );

                        Vector3f v4 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e2][0][0],
                                y + MarchingCubesTable.VertexID[e2][0][1],
                                z + MarchingCubesTable.VertexID[e2][0][2] );

                        Vector3f v5 = new Vector3f(
                                x + MarchingCubesTable.VertexID[e2][1][0],
                                y + MarchingCubesTable.VertexID[e2][1][1],
                                z + MarchingCubesTable.VertexID[e2][1][2] );

                        // Calculate coordinates of the vertices which are composed
                        // of the triangle polygon.
                        Vector3f vertex0 = this.interpolate_vertex( v0, v1 );
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

                        final double value0 = this.interpolate_value( volume, v0, v1 );
                        final double value1 = this.interpolate_value( volume, v2, v3 );
                        final double value2 = this.interpolate_value( volume, v4, v5 );

                        final int color0 =
                                (int) (normalize_factor * (value0 - min_value));
                        colors.add( color_map.getAt( color0 ).getRed() );
                        colors.add( color_map.getAt( color0 ).getGreen() );
                        colors.add( color_map.getAt( color0 ).getBlue() );

                        final int color1 =
                                (int) (normalize_factor * (value1 - min_value));
                        colors.add( color_map.getAt( color1 ).getRed() );
                        colors.add( color_map.getAt( color1 ).getGreen() );
                        colors.add( color_map.getAt( color1 ).getBlue() );

                        final int color2 =
                                (int) (normalize_factor * (value2 - min_value));
                        colors.add( color_map.getAt( color2 ).getRed() );
                        colors.add( color_map.getAt( color2 ).getGreen() );
                        colors.add( color_map.getAt( color2 ).getBlue() );

                        // Calculate a normal vector for the triangle polygon.
                        final Vector3f normal = (vertex2.sub( vertex0 )).cross( vertex1.sub( vertex0 ) );
                        normals.add( normal.getX() );
                        normals.add( normal.getY() );
                        normals.add( normal.getZ() );
                    } // end of loop-triangle
                } // end of loop-x
                ++index;
            } // end of loop-y
            index += line_size;
        } // end of loop-z

        m_object.setCoords( Utility.ListToFloatArray( coords ) );
        m_object.setColors( Utility.ListToIntArray( colors ) );
        m_object.setNormals( Utility.ListToFloatArray( normals ) );
        m_object.setOpacity( (byte) 255 );
        m_object.setPolygonType( PolygonObject.PolygonType.Triangle );
        m_object.setColorType( PolygonObject.ColorType.VertexColor );
        m_object.setNormalType( PolygonObject.NormalType.PolygonNormal );
    }

    private void extract_plane(
            final UnstructuredVolumeObject volume ) throws KVSException{
        // Calculated the coordinate data array and the normal vector array.
        ArrayList<Float> coords = new ArrayList<Float>();
        ArrayList<Float> normals = new ArrayList<Float>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Calculate min/max values of the node data.
        if ( !volume.hasMinMaxValues() ){
            volume.updateMinMaxValues();
        }

        // Calculate a normalize factor.
        double min_value = volume.minValue();
        double max_value = volume.maxValue();
        double normalize_factor = 255.0 / (max_value - min_value);

        // Refer the parameters of the unstructured volume object.
        final float[] volume_coords = volume.coords();
        final int[] volume_connections = volume.connections();
        final int ncells = volume.ncells();

        final ColorMap color_map = transferFunction().colorMap();

        // Extract surfaces.
        int index = 0;
        int[] local_index = new int[4];
        for ( int cell = 0; cell < ncells; ++cell, index += 4 ){
            // Calculate the indices of the target cell.
            local_index[0] = volume_connections[index];
            local_index[1] = volume_connections[index + 1];
            local_index[2] = volume_connections[index + 2];
            local_index[3] = volume_connections[index + 3];

            // Calculate the index of the reference table.
            final int table_index = this.calculate_table_index( local_index );
            if ( table_index == 0 ){
                continue;
            }
            if ( table_index == 15 ){
                continue;
            }

            // Calculate the triangle polygons.
            for ( int i = 0; MarchingTetrahedraTable.TriangleID[table_index][i] != -1; i += 3 ){
                // Refer the edge IDs from the TriangleTable using the table_index.
                final int e0 = MarchingTetrahedraTable.TriangleID[table_index][i];
                final int e1 = MarchingTetrahedraTable.TriangleID[table_index][i + 1];
                final int e2 = MarchingTetrahedraTable.TriangleID[table_index][i + 2];

                // Refer indices of the coordinate array from the VertexTable using the edgeIDs.
                final int c0 = local_index[MarchingTetrahedraTable.VertexID[e0][0]];
                final int c1 = local_index[MarchingTetrahedraTable.VertexID[e0][1]];
                final int c2 = local_index[MarchingTetrahedraTable.VertexID[e1][0]];
                final int c3 = local_index[MarchingTetrahedraTable.VertexID[e1][1]];
                final int c4 = local_index[MarchingTetrahedraTable.VertexID[e2][0]];
                final int c5 = local_index[MarchingTetrahedraTable.VertexID[e2][1]];

                // Determine vertices for each edge.
                final Vector3f v0 = new Vector3f( volume_coords, 3 * c0 );
                final Vector3f v1 = new Vector3f( volume_coords, 3 * c1 );

                final Vector3f v2 = new Vector3f( volume_coords, 3 * c2 );
                final Vector3f v3 = new Vector3f( volume_coords, 3 * c3 );

                final Vector3f v4 = new Vector3f( volume_coords, 3 * c4 );
                final Vector3f v5 = new Vector3f( volume_coords, 3 * c5 );

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

                final double value0 = this.interpolate_value( volume, c0, c1 );
                final double value1 = this.interpolate_value( volume, c2, c3 );
                final double value2 = this.interpolate_value( volume, c4, c5 );

                final int color0 =
                        (int) (normalize_factor * (value0 - min_value));
                colors.add( color_map.getAt( color0 ).getRed() );
                colors.add( color_map.getAt( color0 ).getGreen() ); //blue?
                colors.add( color_map.getAt( color0 ).getBlue() );

                final int color1 =
                        (int) (normalize_factor * (value1 - min_value));
                colors.add( color_map.getAt( color1 ).getRed() );
                colors.add( color_map.getAt( color1 ).getGreen() );
                colors.add( color_map.getAt( color1 ).getBlue() );

                final int color2 =
                        (int) (normalize_factor * (value2 - min_value));
                colors.add( color_map.getAt( color2 ).getRed() );
                colors.add( color_map.getAt( color2 ).getGreen() );
                colors.add( color_map.getAt( color2 ).getBlue() );

                // Calculate a normal vector for the triangle polygon.
                final Vector3f normal = (vertex2.sub( vertex0 )).cross( vertex1.sub( vertex0 ) );
                normals.add( normal.getX() );
                normals.add( normal.getY() );
                normals.add( normal.getZ() );
            } // end of loop-triangle
        } // end of loop-cell

        m_object.setCoords( Utility.ListToFloatArray( coords ) );
        m_object.setColors( Utility.ListToIntArray( colors ) );
        m_object.setNormals( Utility.ListToFloatArray( normals ) );
        m_object.setOpacity( (byte) 255 );
        m_object.setPolygonType( PolygonObject.PolygonType.Triangle );
        m_object.setColorType( PolygonObject.ColorType.PolygonColor );
        m_object.setNormalType( PolygonObject.NormalType.PolygonNormal );
    }

    private int calculate_table_index(
            int x,
            int y,
            int z ){
        int table_index = 0;
        if ( this.substitute_plane_equation( x, y, z ) > 0.0f ){
            table_index |= 1;
        }
        if ( this.substitute_plane_equation( x + 1, y, z ) > 0.0f ){
            table_index |= 2;
        }
        if ( this.substitute_plane_equation( x + 1, y + 1, z ) > 0.0f ){
            table_index |= 4;
        }
        if ( this.substitute_plane_equation( x, y + 1, z ) > 0.0f ){
            table_index |= 8;
        }
        if ( this.substitute_plane_equation( x, y, z + 1 ) > 0.0f ){
            table_index |= 16;
        }
        if ( this.substitute_plane_equation( x + 1, y, z + 1 ) > 0.0f ){
            table_index |= 32;
        }
        if ( this.substitute_plane_equation( x + 1, y + 1, z + 1 ) > 0.0f ){
            table_index |= 64;
        }
        if ( this.substitute_plane_equation( x, y + 1, z + 1 ) > 0.0f ){
            table_index |= 128;
        }

        return (table_index);
    }

    private int calculate_table_index(
            int[] local_index ){
        float[] coords = m_volume.coords();

        Vector3f vertex0 = new Vector3f( coords, 3 * local_index[0] );
        Vector3f vertex1 = new Vector3f( coords, 3 * local_index[1] );
        Vector3f vertex2 = new Vector3f( coords, 3 * local_index[2] );
        Vector3f vertex3 = new Vector3f( coords, 3 * local_index[3] );

        int table_index = 0;
        if ( this.substitute_plane_equation( vertex0 ) > 0.0 ){
            table_index |= 1;
        }
        if ( this.substitute_plane_equation( vertex1 ) > 0.0 ){
            table_index |= 2;
        }
        if ( this.substitute_plane_equation( vertex2 ) > 0.0 ){
            table_index |= 4;
        }
        if ( this.substitute_plane_equation( vertex3 ) > 0.0 ){
            table_index |= 8;
        }

        return (table_index);
    }

    private float substitute_plane_equation(
            int x,
            int y,
            int z ){
        return (m_coefficients.x() * x +
                m_coefficients.y() * y +
                m_coefficients.z() * z +
                m_coefficients.w());
    }

    private float substitute_plane_equation( final Vector3f vertex ){
        return (m_coefficients.x() * vertex.getX() +
                m_coefficients.y() * vertex.getY() +
                m_coefficients.z() * vertex.getZ() +
                m_coefficients.w());
    }

    private Vector3f interpolate_vertex(
            final Vector3f vertex0,
            final Vector3f vertex1 ){
        float value0 = this.substitute_plane_equation( vertex0 );
        float value1 = this.substitute_plane_equation( vertex1 );
        float ratio = kvs.core.util.Math.abs( value0 / (value1 - value0) );

        return (vertex0.mul( 1.0f - ratio ).add( vertex1.mul( ratio ) ));
    }

    private double interpolate_value(
            final StructuredVolumeObject volume,
            final Vector3f vertex0,
            final Vector3f vertex1 ) throws KVSException{
        Buffer buf = volume.values();

        final int line_size = volume.nnodesPerLine();
        final int slice_size = volume.nnodesPerSlice();

        final float value0 = this.substitute_plane_equation( vertex0 );
        final float value1 = this.substitute_plane_equation( vertex1 );
        final float ratio = kvs.core.util.Math.abs( value0 / (value1 - value0) );

        final int index0 = (int) (vertex0.getX() + vertex0.getY() * line_size + vertex0.getZ() * slice_size);
        final int index1 = (int) (vertex1.getX() + vertex1.getY() * line_size + vertex1.getZ() * slice_size);

        if ( buf instanceof ByteBuffer ){
            ByteBuffer values = (ByteBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof ShortBuffer ){
            ShortBuffer values = (ShortBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof IntBuffer ){
            IntBuffer values = (IntBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof LongBuffer ){
            LongBuffer values = (LongBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof FloatBuffer ){
            FloatBuffer values = (FloatBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof DoubleBuffer ){
            DoubleBuffer values = (DoubleBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof CharBuffer ){
            CharBuffer values = (CharBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else{
            throw new KVSException( "Unsupported data type" );
        }


    }

    private double interpolate_value(
            final UnstructuredVolumeObject volume,
            final int index0,
            final int index1 ) throws KVSException{
        Buffer buf = volume.values();
        float[] coords = volume.coords();

        final float value0 = this.substitute_plane_equation( new Vector3f( coords, 3 * index0 ) );
        final float value1 = this.substitute_plane_equation( new Vector3f( coords, 3 * index1 ) );
        final float ratio = kvs.core.util.Math.abs( value0 / (value1 - value0) );

        if ( buf instanceof ByteBuffer ){
            ByteBuffer values = (ByteBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof ShortBuffer ){
            ShortBuffer values = (ShortBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof IntBuffer ){
            IntBuffer values = (IntBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof LongBuffer ){
            LongBuffer values = (LongBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof FloatBuffer ){
            FloatBuffer values = (FloatBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof DoubleBuffer ){
            DoubleBuffer values = (DoubleBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else if ( buf instanceof CharBuffer ){
            CharBuffer values = (CharBuffer) buf;
            return (values.get( index0 ) + ratio * (values.get( index1 ) - values.get( index0 )));
        } else{
            throw new KVSException( "Unsupported data type" );
        }
    }
}
