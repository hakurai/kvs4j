package kvs.core.visualization.renderer;
/**
 * 読み込むStructuredVolumeObjectのvalueはIntBufferのみ。
 */
import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import kvs.core.KVSException;
import kvs.core.image.RGBColor;
import kvs.core.visualization.filter.TrilinearInterpolator;
import kvs.core.visualization.mapper.ColorMap;
import kvs.core.visualization.mapper.OpacityMap;
import kvs.core.visualization.mapper.TransferFunction;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.StructuredVolumeObject;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;

public class RayCastingRenderer extends VolumeRendererBase {

    private static final long serialVersionUID = 3882676668949905970L;
    private float m_step;   ///< sampling step
    private float m_opaque; ///< opaque value for early ray termination

    public RayCastingRenderer()
    {
        this.initialize();
        setShader( new Shader.Lambert() );
    }

    public RayCastingRenderer( final TransferFunction tfunc )
    {
        this.initialize();
        setTransferFunction( tfunc );
        setShader( new Shader.Lambert() );

    }

    public RayCastingRenderer( final ShadingType shader )
    {
        this.initialize();
        setShader( shader );
    }

    @Override
    public void initialize()
    {
        super.initialize();
        m_step   = 0.5f;
        m_opaque = 0.97f;
        m_width  = 0;
        m_height = 0;
    }

    @Override
    public void exec( ObjectBase object, Camera camera, Light light ) throws KVSException {
        final StructuredVolumeObject volume = (StructuredVolumeObject)(object);

        m_timer.start();

        this.create_image( volume, camera, light );
        this.draw_image();

        m_timer.stop();        
    }

    private void create_image(
                              final StructuredVolumeObject volume,
                              final Camera                 camera,
                              final Light                  light ) throws KVSException
    {
        if( m_width  != camera.getWindowWidth() || m_height != camera.getWindowHeight() )
        {
            m_width  = camera.getWindowWidth();
            m_height = camera.getWindowHeight();

            final int npixels = m_width * m_height;
            m_color_data = ByteBuffer.allocate( npixels * 4 );
            m_depth_data = FloatBuffer.allocate( npixels );
        }

        if( volume.values() instanceof IntBuffer ){
            this.rasterize( volume, camera, light );
        } else {
            throw new KVSException( "Not supported data type" );
        }
    }

    //template <typename T> < unsigned int
    private void rasterize(
                           final StructuredVolumeObject volume,
                           final Camera                 camera,
                           final Light                  light )
    {
        // Set shader initial parameters.
        m_shader.set( camera, light );

        // Aliases.
        final byte[]    pixel      = m_color_data.array();
        for( int i = 0; i < pixel.length; i++){
            pixel[i] = 0;
        }
        final float[]   depth_data = m_depth_data.array();
        for( int i = 0; i < depth_data.length; i++){
            depth_data[i] = 0;
        }

        // Set the trilinear interpolator.
        TrilinearInterpolator interpolator = new TrilinearInterpolator( volume );

        // Calculate the ray in the object coordinate system.
        VolumeRayIntersector ray = new VolumeRayIntersector( volume );

        // Execute ray casting.
        final int height = m_height;
        final int width  = m_width;

        final float step   = m_step;
        final float opaque = m_opaque;

        final ShadingType shader = m_shader;
        final ColorMap   cmap = transferFunction().colorMap();
        final OpacityMap omap = transferFunction().opacityMap();

        int depth_index = 0;
        int pixel_index = 0;
        for ( int y = 0; y < height; ++y )
        {
            for ( int x = 0; x < width; ++x )
            {

                ray.setOrigin( x, y );

                // Intersection the ray with the bounding box.
                if ( ray.isIntersected() )
                {
                    Color color = Color.BLACK;
                    float alpha = 0.0f;

                    depth_data[ depth_index ] = ray.depth();

                    do //FIXME 動作が遅い
                    {
                        // Interpolation.
                        interpolator.attachPoint( ray.point() );

                        int s = (int)( interpolator.scalar() ); /// < interpolator.scalar<T>()                        
                        s = ( s < 0 ) ? 0 : s;                  /// unsigned
                        final float density = omap.getAt( s );
                        if ( !kvs.core.util.Math.isZero( density ) )
                        {
                            // Front-to-back accumulation.
                            final float attenuate = shader.attenuation( ( interpolator.gradient().get() ), 0 ); /// < interpolator.gradient<T>()[0]
                            final float current_alpha = ( 1.0f - alpha ) * density;
                            color = RGBColor.add( color, RGBColor.mul( cmap.getAt( s ), current_alpha * attenuate ) );
                            alpha += current_alpha;
                            if ( alpha > opaque )
                            {
                                alpha = opaque;
                                break;
                            }
                        }

                        ray.step( step );


                    } while ( ray.isInside() );

                    // Set pixel value.
                    pixel[ pixel_index     ] = (byte)color.getRed();
                    pixel[ pixel_index + 1 ] = (byte)color.getGreen();
                    pixel[ pixel_index + 2 ] = (byte)color.getBlue();
                    pixel[ pixel_index + 3 ] = (byte)kvs.core.util.Math.round( alpha * 255.0 );
                }
                else
                {
                    depth_data[ depth_index ] = 1.0f;
                }

                ++depth_index;
                pixel_index += 4;
            }
        }
    }

    @Override
    protected void initialize_modelview() {}

    @Override
    protected void initialize_projection() {}


}
