package kvs.core.visualization.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.visualization.mapper.TransferFunction;
import kvs.core.visualization.viewer.FrameBuffer;

public abstract class VolumeRendererBase extends RendererBase {
    
    private static final long serialVersionUID = -7475342475051025737L;
    protected int                   m_width;          ///< width of rendering image
    protected int                   m_height;         ///< height of rendering image
    protected FrameBuffer           m_depth_buffer;   ///< depth buffer
    protected FloatBuffer           m_depth_data;     ///< depth data as float type
    protected FrameBuffer           m_color_buffer;   ///< color (RGBA) buffer
    protected ByteBuffer             m_color_data;     ///< color (RGBA) data as uchar type
    protected boolean               m_enable_shading; ///< shading flag
    protected TransferFunction      m_tfunc = new TransferFunction();          ///< transfer function
    protected ShadingType           m_shader;         ///< shading method
    
    public VolumeRendererBase()
    {
        this.initialize();
    }
    
    public void setTransferFunction( final TransferFunction tfunc )
    {
        m_tfunc = tfunc;
    }
    
    public void enableShading()
    {
        m_enable_shading = true;
    }
    
    public void disableShading()
    {
        m_enable_shading = false;
    }
    
    public boolean isEnabledShading()
    {
        return( m_enable_shading );
    }
    
    public final TransferFunction transferFunction()
    {
        return( m_tfunc );
    }
    
    public void initialize()
    {
        m_enable_shading = true;
        m_shader = null;

        m_depth_buffer = new FrameBuffer();
        m_depth_buffer.setFormat( GL.GL_DEPTH_COMPONENT );
        m_depth_buffer.setType( GL.GL_FLOAT );
        
        m_color_buffer = new FrameBuffer();
        m_color_buffer.setFormat( GL.GL_RGBA );
        m_color_buffer.setType( GL.GL_UNSIGNED_BYTE );
    }
    
    public final void clear()
    {
        if ( m_shader != null )
        {
            m_shader = null;
        }
    }
    
    protected final void draw_image()
    {
        GL gl = GLU.getCurrentGL();
        // Get viewport information.
        int[] viewport = new int[4];
        gl.glGetIntegerv( GL.GL_VIEWPORT, viewport, 0 );

        gl.glDepthFunc( GL.GL_LEQUAL );
        gl.glDepthMask( true );
        gl.glColorMask( false, false, false, false );
        this.draw_depth_buffer( viewport );

        gl.glBlendFunc( GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA );
        gl.glColorMask( true, true, true, true );
        this.draw_color_buffer( viewport );
    }
    
    protected final void draw_depth_buffer( final int[] viewport )
    {
        GL gl = GLU.getCurrentGL();
        // Enable/Disable OpenGL parameters.
        gl.glEnable( GL.GL_DEPTH_TEST );

        // Send depth data to the frame buffer (depth buffer).

        m_depth_buffer.draw( m_width, m_height, viewport, m_depth_data );

        // Recover OpenGL parameters.
        gl.glDisable( GL.GL_DEPTH_TEST );
    }
    
    protected final void draw_color_buffer( final int[] viewport )
    {
        GL gl = GLU.getCurrentGL();
        // Enable/Disable OpenGL parameters.
        gl.glEnable( GL.GL_BLEND );
        gl.glDisable( GL.GL_DEPTH_TEST );

        // Send color data to the frame buffer (color buffer).
        m_color_buffer.draw( m_width, m_height, viewport, m_color_data );

        // Recover OpenGL parameters.
        gl.glDisable( GL.GL_BLEND );
        gl.glEnable( GL.GL_DEPTH_TEST );
    }
    
    protected final void setShader( final ShadingType shader )
    {
        m_shader = shader;

    };
    


}
