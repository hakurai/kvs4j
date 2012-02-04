package kvs.core.visualization.viewer;

import java.nio.Buffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class Texture2D extends TextureBase {

    protected boolean       m_is_downloaded = false;        ///< if true, the texture is downloaded
    protected int           m_wrap_s        = GL.GL_CLAMP;  ///< wrap method for s-axis
    protected int           m_wrap_t        = GL.GL_CLAMP;  ///< wrap method for t-axis
    protected int           m_width         = 0;            ///< texture width
    protected int           m_height        = 0;            ///< texture height
    protected final Buffer  m_pixels        = null;         ///< pointer to the texture data (not allocated)

    public Texture2D()
    {
    }

    public Texture2D( final int wrap_s, final int wrap_t )
    {
        super();
        m_wrap_s = wrap_s;
        m_wrap_t = wrap_t;
        GL gl = GLU.getCurrentGL();

        int[] id_list = new int[1];
        if( !gl.glIsTexture( m_id ) ){
            gl.glGenTextures( 1, id_list, 0 );
            m_id = id_list[0];
            m_is_downloaded = false;
        }
    }

    public int getWrapS()
    {
        return( m_wrap_s );
    }

    public int getWrapT()
    {
        return( m_wrap_t );
    }

    public int getWidth()
    {
        return( m_width );
    }

    public int getHeight()
    {
        return( m_height );
    }

    public void setWrapS( final int wrap_s )
    {
        m_wrap_s = wrap_s;
    }

    public void setWrapT( final int wrap_t )
    {
        m_wrap_t = wrap_t;
    }

    public void create( final int width, final int height )
    {
        GL gl = GLU.getCurrentGL();
        m_width  = width;
        m_height = height;
        
        int[] id_list = new int[1];

        
        if( !gl.glIsTexture( m_id ) ){
            gl.glGenTextures( 1, id_list, 0 );
            m_id = id_list[0];
            //m_is_downloaded = false;
        }
        

        // Bind the texture.
        gl.glBindTexture( GL.GL_TEXTURE_2D, m_id );

        // Set the filter methods.
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, m_mag_filter );
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, m_min_filter );

        // Set the wrap methods.
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, m_wrap_s );
        gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, m_wrap_t );

        
        this.download( width, height, null );
        gl.glTexImage2D(
                GL.GL_TEXTURE_2D,
                0,
                this.getInternalFormat(),
                width,
                height,
                0,
                this.getExternalFormat(),
                this.getExternalType(),
                null );
    }

    public void download(
                         final int width,
                         final int height,
                         final Buffer  pixels )
    {
        GL gl = GLU.getCurrentGL();

        int[] swap = new int[1];    //GL_TRUE(1) or GL_FALSE(0)
        int[] alignment = new int[1];

        gl.glGetIntegerv( GL.GL_UNPACK_SWAP_BYTES, swap, 0 );
        gl.glGetIntegerv( GL.GL_UNPACK_ALIGNMENT, alignment, 0 );

        gl.glPixelStorei( GL.GL_UNPACK_SWAP_BYTES, swap[0] );
        gl.glPixelStorei( GL.GL_UNPACK_ALIGNMENT, 1 );
        
        if ( !m_is_downloaded )
        {
            //                             const size_t ext_width  = 1 << ( kvs::Math::Log2Smallest( width ) );
            //                             const size_t ext_height = 1 << ( kvs::Math::Log2Smallest( height ) );
            gl.glTexImage2D(
                    GL.GL_TEXTURE_2D,
                    0,
                    this.getInternalFormat(),
                    width,
                    height,
                    0,
                    this.getExternalFormat(),
                    this.getExternalType(),
                    pixels );

            m_is_downloaded = true;
        }
        else
        {
        
        gl.glTexSubImage2D(
                GL.GL_TEXTURE_2D,
                0,
                0,
                0,
                width,
                height,
                this.getExternalFormat(),
                this.getExternalType(),
                pixels );           
        }

        gl.glPixelStorei( GL.GL_UNPACK_ALIGNMENT, alignment[0] );
        gl.glPixelStorei( GL.GL_UNPACK_SWAP_BYTES, swap[0] );
        
        
    }
    
    public void bind()
    {
        GL gl = GLU.getCurrentGL();
        gl.glBindTexture( GL.GL_TEXTURE_2D, m_id );
    }
    
    public void unbind()
    {
        GL gl = GLU.getCurrentGL();
        gl.glBindTexture( GL.GL_TEXTURE_2D, 0 );
    }
    
    public void release()
    {
        GL gl = GLU.getCurrentGL();
        if ( gl.glIsTexture( m_id ) ){
            int[] id_list = { m_id };
            gl.glDeleteTextures( 1, id_list, 0 );
        }

        m_id = 0;
        //m_is_downloaded = false;
    }
    
    public int usedTextureMemorySize()
    {
        return( get_texture_memory_size_on_gpu( GL.GL_PROXY_TEXTURE_2D ) );
    }

    @Override
    public boolean equals( Object obj ){
        if( obj == this ){
            return true;
        }

        if( !super.equals( obj ) ){
            return false;
        }

        if( !(obj instanceof Texture2D) ){
            return false;
        }

        Texture2D other = (Texture2D)obj;

        if( this.m_is_downloaded != other.m_is_downloaded ){
            return false;
        }

        if( this.m_wrap_s != other.m_wrap_s||
            this.m_wrap_t != other.m_wrap_t||
            this.m_width  != other.m_width||
            this.m_height != other.m_height ){
            return false;
        }

        if( !this.m_pixels.equals( other.m_pixels ) ){
            return false;
        }

        return true;



    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.m_is_downloaded ? 1 : 0);
        hash = 31 * hash + this.m_wrap_s;
        hash = 31 * hash + this.m_wrap_t;
        hash = 31 * hash + this.m_width;
        hash = 31 * hash + this.m_height;
        hash = 31 * hash + (this.m_pixels != null ? this.m_pixels.hashCode() : 0);
        return hash;
    }
    
    private int get_texture_memory_size_on_gpu( final int proxy )
    {
        GL gl = GLU.getCurrentGL();
        // Get the texture size.
        int[] texture_size = { 0, 0 };
        gl.glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_WIDTH,          texture_size, 0 );
        gl.glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_HEIGHT,         texture_size, 1 );

        // Get the each channel size.
        int[] channel_size = { 0, 0, 0, 0, 0, 0, 0 };
        gl.glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_RED_SIZE,       channel_size, 0 );
        gl.glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_GREEN_SIZE,     channel_size, 1 );
        gl.glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_BLUE_SIZE,      channel_size, 2 );
        gl.glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_ALPHA_SIZE,     channel_size, 3 );
        gl.glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_LUMINANCE_SIZE, channel_size, 4 );
        gl.glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_INTENSITY_SIZE, channel_size, 5 );
        /*
    #if GL_EXT_paletted_texture
        //glGetTexLevelParameteriv( proxy, 0, GL_TEXTURE_INDEX_SIZE_EXT, &(channel_size[6]) );
    #endif

        /*
    #if GL_ARB_texture_compression
        // Get compressed texture size.
        GLint compressed[1] = { 0 };
        glGetTexLevelParameteriv( proxy, 0, GL.GL_TEXTURE_COMPRESSED_ARB, &(compressed[0]) );
        if( compressed[0] )
        {
            GLint  size[1] = { 0 };
            
    #if   GL_TEXTURE_COMPRESSED_IMAGE_SIZE_ARB
            GLenum pname = GL_TEXTURE_COMPRESSED_IMAGE_SIZE_ARB;
            glGetTexLevelParameteriv( proxy, 0, pname, &(size[0]) );
    #elif GL_TEXTURE_IMAGE_SIZE_ARB
            GLenum pname = GL_TEXTURE_IMAGE_SIZE_ARB;
            glGetTexLevelParameteriv( proxy, 0, pname, &(size[0]) );
    #endif
            return( size[0] );
        }
    #endif
    */

        // Compute the number of bytes per voxel.
        int total_bits =
            channel_size[0] +
            channel_size[1] +
            channel_size[2] +
            channel_size[3] +
            channel_size[4] +
            channel_size[5] +
            channel_size[6];

        // Convert unit from 'bit' to 'byte'.
        float bytes = (float)total_bits / 8.0f;

        // Round up to the next whole byte.
        if( !kvs.core.util.Math.equal( bytes, (float)( (int)bytes) ) )
        {
            bytes = (float)((int)bytes) + 1.0f;
        }

        // compute the amount of texture memory used.
        return( texture_size[0] * texture_size[1] * (int)bytes );
    }

}
