package kvs.core.visualization.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PointObject;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;

public class ParticleVolumeRenderer extends VolumeRendererBase {

    private static final long serialVersionUID = 1393956892831069740L;
    protected boolean           m_enable_rendering; ///< rendering flag
    protected int               m_subpixel_level;   ///< number of divisions in a pixel
    protected ParticleBuffer    m_buffer; ///< particle buffer

    // Reference data (NOTE: not allocated in thie class).
    protected PointObject m_ref_point; ///< pointer to the point data

    public ParticleVolumeRenderer(){
        setShader( new Shader.Lambert() );
    }

    public ParticleVolumeRenderer(
                                  final PointObject point,
                                  final int         subpixel_level )
    {
        setShader( new Shader.Lambert() );

        this.initialize();
        this.setSubpixelLevel( subpixel_level );
        this.attachPointObject( point );
    }

    @Override
    public void exec( ObjectBase object, Camera camera, Light light ) {
        if( !m_enable_rendering ){
            return;
        }

        PointObject point = PointObject.valueOf( object );

        //KVS_ASSERT( m_ref_point == point );
        //KVS_ASSERT( m_ref_point );

        m_timer.start();

        this.create_image( point, camera, light );
        draw_image();
        this.clean_particle_buffer();

        m_timer.stop();
    }

    public void attachPointObject( final PointObject point )
    {
        m_ref_point = point;
    }

    public void setSubpixelLevel( final int subpixel_level )
    {
        m_subpixel_level = subpixel_level;
    }

    @Override
    public void initialize()
    {
        super.initialize();
        m_enable_rendering = true;
        m_subpixel_level   = 1;
        m_buffer           = null;
    }

    public ParticleBuffer particleBuffer()
    {
        return( m_buffer );
    }

    public int subpixelLevel()
    {
        return( m_subpixel_level );
    }

    public void enableRendering()
    {
        m_enable_rendering = true;
    }

    public void disableRendering()
    {
        m_enable_rendering = false;
    }

    protected boolean create_particle_buffer(
                                             final int width,
                                             final int height,
                                             final int subpixel_level )
    {
        m_buffer = new ParticleBuffer( width, height, subpixel_level );
        if( m_buffer == null ){
            return( false );
        }

        return( true );
    }

    protected void clean_particle_buffer()
    {
        m_buffer.clean();
    }

    protected void delete_particle_buffer()
    {
        if( m_buffer != null ){
            //delete m_buffer;
            m_buffer = null; }
    }

    protected void create_image(
                                final PointObject point,
                                final Camera      camera,
                                final Light       light )
    {
        // Create memory region for the buffers, if the screen size is changed.
        if( ( m_width  != camera.getWindowWidth() ) ||
                ( m_height != camera.getWindowHeight() ) )
        {
            m_width  = camera.getWindowWidth();
            m_height = camera.getWindowHeight();

            m_color_data = ByteBuffer.allocate( m_width * m_height * 4 );
            m_depth_data = FloatBuffer.allocate( m_width * m_height );

            this.delete_particle_buffer();
            this.create_particle_buffer(
                    m_width,
                    m_height,
                    m_subpixel_level );
        }

        // Initialize the frame buffers.
        //BaseClass::m_color_data.fill( 0x00 );
        //BaseClass::m_depth_data.fill( 0x00 );

        this.project_particle( point, camera, light );
    }

    protected void project_particle(
                                    final PointObject point,
                                    final Camera      camera,
                                    final Light       light )
    {
        float[] t = new float[16];
        camera.getCombinedMatrix( t );
        final int w = camera.getWindowWidth() / 2;
        final int  h = camera.getWindowHeight() / 2;

        // Set shader initial parameters.
        m_shader.set( camera, light );

        // Attach the shader and the point object to the point buffer.
        m_buffer.attachShader( m_shader );
        m_buffer.attachPointObject( point );

        // Aliases.
        final int       nv = point.nvertices();
        final float[] v  = point.coords().array();

        int index3 = 0;
        final int bounds_width  = m_width  - 1;
        final int bounds_height = m_height - 1;
        for( int index = 0; index < nv; index++, index3 += 3 )
        {
            /* Calculate the projected point position in the window coordinate system.
             * Ex.) Camera::projectObjectToWindow().
             */
             float p_tmp[] = {
                     v[index3]*t[0] + v[index3+1]*t[4] + v[index3+2]*t[ 8] + t[12],
                     v[index3]*t[1] + v[index3+1]*t[5] + v[index3+2]*t[ 9] + t[13],
                     v[index3]*t[2] + v[index3+1]*t[6] + v[index3+2]*t[10] + t[14],
                     v[index3]*t[3] + v[index3+1]*t[7] + v[index3+2]*t[11] + t[15] };
             p_tmp[3] = 1.0f / p_tmp[3];
             p_tmp[0] *= p_tmp[3];
             p_tmp[1] *= p_tmp[3];
             p_tmp[2] *= p_tmp[3];

             final float p_win_x = ( 1.0f + p_tmp[0] ) * w;
             final float p_win_y = ( 1.0f + p_tmp[1] ) * h;
             final float depth   = ( 1.0f + p_tmp[2] ) * 0.5f;

             // Store the projected point in the point buffer.
             if( ( 0 < p_win_x ) & ( 0 < p_win_y ) )
             {
                 if( ( p_win_x < bounds_width ) & ( p_win_y < bounds_height ) )
                 {
                     m_buffer.add( p_win_x, p_win_y, depth, index );
                 }
             }
        }

        // Shading calculation.
        if( m_enable_shading ){
            m_buffer.enableShading();
        }
        else{
            m_buffer.disableShading();
        }

        m_buffer.createImage( m_color_data.array(), m_depth_data.array() );
    }

    @Override
    protected void initialize_modelview() {}

    @Override
    protected void initialize_projection() {}

}
