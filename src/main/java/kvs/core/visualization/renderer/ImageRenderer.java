package kvs.core.visualization.renderer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.visualization.object.ImageObject;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.ImageObject.PixelType;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;
import kvs.core.visualization.viewer.Texture2D;

public class ImageRenderer extends RendererBase {
    
    private static final long serialVersionUID = 8783035035519592328L;

    public enum Type
    {
        Stretching,
        Centering
    };

    protected double        m_initial_aspect_ratio; ///< initial aspect ratio
    protected double        m_left;                 ///< screen left position
    protected double        m_right;                ///< screen right position
    protected double        m_bottom;               ///< screen bottom position
    protected double        m_top;                  ///< screen top position
    protected Type          m_type = Type.Centering;                 ///< rendering type
    protected Texture2D     m_texture = new Texture2D();              ///< texture image
    
    public ImageRenderer()
    {
    
    }
    
    public ImageRenderer( final Type type )
    {
        m_type = type;
    }

    
    @Override
    public void exec( ObjectBase object, Camera camera, Light light ) {
        GL gl = GLU.getCurrentGL();
        //kvs::IgnoreUnusedVariable( light );

        ImageObject image = ImageObject.valueOf( object );
        
        gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
        
        gl.glPushAttrib( GL.GL_CURRENT_BIT | GL.GL_ENABLE_BIT );
        
        if ( !gl.glIsTexture( m_texture.getId() ) ){
            this.create_texture( image );
        }

        gl.glDisable( GL.GL_DEPTH_TEST );
        gl.glEnable( GL.GL_TEXTURE_2D );


        switch( m_type )
        {
        case Centering:
            this.centering( camera.getWindowWidth(), camera.getWindowHeight() );
            break;
        default: break;
        }
            m_texture.download( image.width(), image.height(), image.data() );
            m_texture.bind();


        gl.glMatrixMode( GL.GL_MODELVIEW );
        gl.glPushMatrix();
        {
            gl.glLoadIdentity();


            gl.glMatrixMode( GL.GL_PROJECTION );
            gl.glPushMatrix();
            {
                gl.glLoadIdentity();
                gl.glOrtho( m_left, m_right, m_bottom, m_top, -1, 1 );

                gl.glBegin( GL.GL_QUADS );
                gl.glTexCoord2f( 0.0f, 0.0f );gl.glVertex2f(  0.0f,  1.0f );
                gl.glTexCoord2f( 0.0f, 1.0f );gl.glVertex2f(  0.0f,  0.0f );
                gl.glTexCoord2f( 1.0f, 1.0f );gl.glVertex2f(  1.0f,  0.0f );
                gl.glTexCoord2f( 1.0f, 0.0f );gl.glVertex2f(  1.0f,  1.0f );                
                gl.glEnd();                

            }
            gl.glPopMatrix();
            gl.glMatrixMode( GL.GL_MODELVIEW );

        }
        gl.glPopMatrix();

        gl.glClearDepth( 1000 );

        gl.glEnable( GL.GL_DEPTH_TEST );
        gl.glDisable( GL.GL_TEXTURE_2D );

        gl.glPopAttrib();

    }

    @Override
    public boolean equals( Object obj ){
        if( this == obj ){
            return true;
        }

        if( !(obj instanceof ImageRenderer) ){
            return false;
        }

        ImageRenderer other = (ImageRenderer)obj;

        if( this.m_initial_aspect_ratio != other.m_initial_aspect_ratio||
                this.m_left != other.m_left||
                this.m_right != other.m_right||
                this.m_bottom != other.m_bottom||
                this.m_top != other.m_top){
            return false;
        }

        if( !this.m_type.equals( other.m_type ) ){
            return false;
        }

        if( !this.m_texture.equals( other.m_texture )){
            return false;
        }


        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (Double.doubleToLongBits( this.m_initial_aspect_ratio ) ^ (Double.doubleToLongBits( this.m_initial_aspect_ratio ) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits( this.m_left ) ^ (Double.doubleToLongBits( this.m_left ) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits( this.m_right ) ^ (Double.doubleToLongBits( this.m_right ) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits( this.m_bottom ) ^ (Double.doubleToLongBits( this.m_bottom ) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits( this.m_top ) ^ (Double.doubleToLongBits( this.m_top ) >>> 32));
        hash = 67 * hash + this.m_type.hashCode();
        hash = 67 * hash + (this.m_texture != null ? this.m_texture.hashCode() : 0);
        return hash;
    }
    
    protected void create_texture( final ImageObject image )
    {
        GL gl = GLU.getCurrentGL();
        final double width  = image.width();
        final double height = image.height();
        m_initial_aspect_ratio = width / height;
        m_left   = 0.0;
        m_right  = 1.0;
        m_bottom = 0.0;
        m_top    = 1.0;

        if ( image.type() == PixelType.Gray8 )
        {
            final int nchannels         = 1;
            final int bytes_per_channel = 1;
            m_texture.setPixelFormat( nchannels, bytes_per_channel );
        }
        else if ( image.type() == PixelType.Gray16 )
        {
            final int nchannels         = 1;
            final int bytes_per_channel = 2;
            m_texture.setPixelFormat( nchannels, bytes_per_channel );
        }
        else if ( image.type() == PixelType.Color24 )
        {
            final int nchannels         = 3;
            final int bytes_per_channel = 1;
            m_texture.setPixelFormat( nchannels, bytes_per_channel );
        }
        else if ( image.type() == PixelType.Color32 )
        {
            final int nchannels         = 4;
            final int bytes_per_channel = 1;
            m_texture.setPixelFormat( nchannels, bytes_per_channel );
        }
        else
        {
            //kvsMessageError("Unknown pixel color type.");
        }

        m_texture.create( image.width(), image.height() );
        gl.glTexEnvf( GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE );
        m_texture.download( image.width(), image.height(), image.data() );
    }
    
    protected void centering( double width, double height )
    {
        double current_aspect_ratio = width / height;
        double aspect_ratio = current_aspect_ratio / m_initial_aspect_ratio;
        if( aspect_ratio >= 1.0 )
        {
            m_left   = ( 1.0 - aspect_ratio ) * 0.5;
            m_right  = ( 1.0 + aspect_ratio ) * 0.5;
            m_bottom = 0.0;
            m_top    = 1.0;
        }
        else
        {
            m_left   = 0.0;
            m_right  = 1.0;
            m_bottom = ( 1.0 - 1.0 / aspect_ratio ) * 0.5;
            m_top    = ( 1.0 + 1.0 / aspect_ratio ) * 0.5;
        }
    }

    @Override
    protected void initialize_modelview() {}

    @Override
    protected void initialize_projection() {}

}
