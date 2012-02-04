/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kvs.core.visualization.mapper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import kvs.core.KVSException;
import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector3i;
import kvs.core.util.Utility;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.ObjectBase.ObjectType;
import kvs.core.visualization.object.PointObject;
import kvs.core.visualization.object.StructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase;
import kvs.core.visualization.object.VolumeObjectBase.GridType;

/**
 *
 * @author eguchi
 */
public class ExtractVertices extends MapperBase{

    private static final long serialVersionUID = 1L;
    private PointObject m_object;

    public ExtractVertices(){
        super();
        m_object = new PointObject();
    }

    public ExtractVertices( final TransferFunction transfer_function ){
        super( transfer_function );
        m_object = new PointObject();
    }

    @Override
    public ObjectBase exec( ObjectBase object ) throws KVSException{
        final ObjectType type = object.objectType();
        if ( type == ObjectType.Volume ){
            this.mapping( (VolumeObjectBase) object );
        } else{
            throw new KVSException( "Geometry object is not supported." );
        }

        return m_object;
    }

    private void mapping( final VolumeObjectBase volume ){
//    m_volume = volume;
        this.attach_volume( volume );

//    this->pre_process();
        this.set_min_max_coords( volume, m_object );

        this.calculate_coords();

        this.calculate_colors();
    }

    private void calculate_coords(){
        GridType type = m_volume.gridType();

        if ( type == GridType.Uniform ){
            this.calculate_uniform_coords();
        } else if ( type == GridType.Rectilinear ){
            this.calculate_rectiliner_coords();
        } else{
            m_object.setCoords( m_volume.coords() );
        }
    }

    private void calculate_uniform_coords(){
        final StructuredVolumeObject volume = (StructuredVolumeObject) m_volume;

        FloatBuffer coords = FloatBuffer.allocate( 3 * volume.nnodes() );

        final Vector3i resolution = volume.resolution();

        final Vector3f volume_size = volume.maxExternalCoord().sub( volume.minExternalCoord() );
        final Vector3i ngrids = resolution.sub( new Vector3i( 1, 1, 1 ) );
        final Vector3f grid_size = new Vector3f(
                volume_size.getX() / (ngrids.getX()),
                volume_size.getY() / (ngrids.getY()),
                volume_size.getZ() / (ngrids.getZ()) );

        for ( int k = 0; k < resolution.getZ(); ++k ){
            final float z =
                    grid_size.getZ() * k;
            for ( int j = 0; j < resolution.getY(); ++j ){
                final float y =
                        grid_size.getY() * j;
                for ( int i = 0; i < resolution.getX(); ++i ){
                    final float x =
                            grid_size.getX() * i;

                    coords.put( x );
                    coords.put( y );
                    coords.put( z );
                }
            }
        }

        m_object.setCoords( coords );
    }

    private void calculate_rectiliner_coords(){
        System.err.println( "no support" );
    }

    private void calculate_colors(){
        final VolumeObjectBase volume = m_volume;

        Buffer buf = volume.values();
        double[] value = Utility.bufferToDoubleArray( buf );
        final int end = value.length;

        ByteBuffer colors = ByteBuffer.allocate( 3 * volume.nnodes() );

        ColorMap cmap = this.colorMap();

        if ( !volume.hasMinMaxValues() ){
            volume.updateMinMaxValues();
        }

        final double min_value = volume.minValue();
        final double max_value = volume.maxValue();

        final double normalize_factor =
                (cmap.resolution() - 1) / (max_value - min_value);

        final int veclen = m_volume.veclen();

        int index = 0;
        if ( veclen == 1 ){
            while ( index < end ){
                int color_level =
                        (int) (normalize_factor * (value[index++] - min_value));

                colors.put( (byte) cmap.getAt( color_level ).getRed() );
                colors.put( (byte) cmap.getAt( color_level ).getGreen() );
                colors.put( (byte) cmap.getAt( color_level ).getBlue() );
            }
        } else{
            while ( index < end ){
                double magnitude = 0.0;

                for ( int i = 0; i < veclen; ++i ){
                    magnitude = kvs.core.util.Math.square( value[index] );
                    ++index;
                }
                magnitude = kvs.core.util.Math.squareRoot( magnitude );

                final int color_level =
                        (int) (normalize_factor * (magnitude - min_value));

                colors.put( (byte) cmap.getAt( color_level ).getRed() );
                colors.put( (byte) cmap.getAt( color_level ).getGreen() );
                colors.put( (byte) cmap.getAt( color_level ).getBlue() );
            }
        }

        m_object.setColors( colors );
    }
}


