package kvs.core.visualization.viewer;

import java.io.Serializable;
import javax.media.opengl.GL;

public class TextureBase implements Serializable{

    protected int m_id = 0; // /< texture ID
    protected int m_internal_format = 0; // /< internal pixel data format
    protected int m_external_format = 0; // /< external pixel data format
    protected int m_external_type = 0; // /< external pixel data type
    protected int m_mag_filter = GL.GL_LINEAR; // /< filtering method for
    // magnification
    protected int m_min_filter = GL.GL_LINEAR; // /< filtering method for

    // minification

    public TextureBase() {
    }

    public TextureBase( final int mag_filter, final int min_filter ) {
        m_mag_filter = mag_filter;
        m_min_filter = min_filter;
    }

    public int getId() {
        return m_id;
    }

    public int getMagFilter() {
        return m_mag_filter;
    }

    public int getMinFilter() {
        return m_min_filter;
    }

    public int getInternalFormat() {
        return m_internal_format;
    }

    public int getExternalFormat() {
        return m_external_format;
    }

    public int getExternalType() {
        return m_external_type;
    }

    public void setMagFilter( final int mag_filter ) {
        m_mag_filter = mag_filter;
    }

    public void setMinFilter( final int min_filter )
    {
        m_min_filter = min_filter;
    }

    public void setPixelFormat(
                               final int  internal_format,
                               final int external_format,
                               final int external_type )
    {
        m_internal_format = internal_format;
        m_external_format = external_format;
        m_external_type   = external_type;
    }
    
    public void setPixelFormat( final int nchannels, final int bytes_per_channel )
    {
        this.estimate_pixel_format( nchannels, bytes_per_channel );
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + this.m_id;
        hash = 41 * hash + this.m_internal_format;
        hash = 41 * hash + this.m_external_format;
        hash = 41 * hash + this.m_external_type;
        hash = 41 * hash + this.m_mag_filter;
        hash = 41 * hash + this.m_min_filter;
        return hash;
    }

    public boolean equals( Object obj ){
        if( this == obj ){
            return true;
        }

        if( !(obj instanceof TextureBase) ){
            return false;
        }

        TextureBase other = (TextureBase)obj;

        if( this.m_id != other.m_id ){
            return false;
        }

        if( this.m_internal_format != other.m_internal_format||
                this.m_external_format != other.m_external_format||
                this.m_external_type != other.m_external_type||
                this.m_mag_filter != other.m_mag_filter||
                this.m_min_filter != other.m_min_filter ){
            return false;
        }
        return true;
    }
    
    protected int get_nchannels( final int external_format )
    {
        int nchannels = 0;

        switch( external_format )
        {
        case GL.GL_COLOR_INDEX:
        case GL.GL_STENCIL_INDEX:
        case GL.GL_DEPTH_COMPONENT:
        case GL.GL_RED:
        case GL.GL_GREEN:
        case GL.GL_BLUE:
        case GL.GL_ALPHA:
        case GL.GL_LUMINANCE:
            nchannels = 1;
            break;
        case GL.GL_LUMINANCE_ALPHA:
            nchannels = 2;
            break;
        case GL.GL_RGB:
    //#ifdef GL_BGR_EXT
    //  case GL.GL_BGR_EXT:
    //#endif
            nchannels = 3;
            break;
        case GL.GL_RGBA:
    //#ifdef GL_BGRA_EXT
    //  case GL.GL_BGRA_EXT:
    //#endif
    //#ifdef GL_ABGR_EXT
        case GL.GL_ABGR_EXT:
    //#endif
            nchannels = 4;
            break;
        default:
            //kvsMessageError("Unsupported OpenGL external pixel format.");
            break;
        }

        return( nchannels );
    }
    
    protected int get_channel_size( final int external_type )
    {
        int channel_size = 0;

        switch( external_type )
        {
        case GL.GL_UNSIGNED_BYTE:
        case GL.GL_BYTE:
            channel_size = 2;
            break;
        case GL.GL_UNSIGNED_SHORT:
        case GL.GL_SHORT:
            channel_size = 2;
            break;
        case GL.GL_UNSIGNED_INT:
        case GL.GL_INT:
            channel_size = 4;
            break;
        case GL.GL_FLOAT:
            channel_size = 4;
            break;
        default:
            //kvsMessageError("Unsupported OpenGL external pixel data type.");
            break;
        }

        return( channel_size );
    }
    
    protected void estimate_pixel_format( final int nchannels, final int bytes_per_channel )
    {
        // Initialize.
        this.setPixelFormat( 0, 0, 0 );

        switch( nchannels )
        {
        case 1: this.determine_pixel_format_for_1_channel( bytes_per_channel ); break;
        case 2: this.determine_pixel_format_for_2_channel( bytes_per_channel ); break;
        case 3: this.determine_pixel_format_for_3_channel( bytes_per_channel ); break;
        case 4: this.determine_pixel_format_for_4_channel( bytes_per_channel ); break;
        default:
            //kvsMessageError("Invalid the number of channels is specified.");
            break;
        }
    }
    
    protected void determine_pixel_format_for_1_channel( final int bytes_per_channel )
    {
        switch( bytes_per_channel )
        {
        case 1: this.setPixelFormat( GL.GL_INTENSITY8,  GL.GL_LUMINANCE, GL.GL_UNSIGNED_BYTE  ); break;
        case 2: this.setPixelFormat( GL.GL_INTENSITY16, GL.GL_LUMINANCE, GL.GL_UNSIGNED_SHORT ); break;
        case 4: this.setPixelFormat( GL.GL_INTENSITY,   GL.GL_LUMINANCE, GL.GL_FLOAT          ); break;
        default:
            //kvsMessageError("Bytes per channel must be 1, 2 or 4.");
            break;
        }
    }
    
    protected void determine_pixel_format_for_2_channel( final int bytes_per_channel )
    {
        switch( bytes_per_channel )
        {
        case 1: this.setPixelFormat( GL.GL_LUMINANCE8_ALPHA8,   GL.GL_LUMINANCE_ALPHA, GL.GL_UNSIGNED_BYTE  ); break;
        case 2: this.setPixelFormat( GL.GL_LUMINANCE16_ALPHA16, GL.GL_LUMINANCE_ALPHA, GL.GL_UNSIGNED_SHORT ); break;
        case 4: this.setPixelFormat( GL.GL_LUMINANCE_ALPHA,     GL.GL_LUMINANCE_ALPHA, GL.GL_FLOAT          ); break;
        default:
            //kvsMessageError("Bytes per channel must be 1, 2 or 4.");
            break;
        }
    }
    
    protected void determine_pixel_format_for_3_channel( final int bytes_per_channel )
    {
        switch( bytes_per_channel )
        {
        case 1: this.setPixelFormat( GL.GL_RGB8,  GL.GL_RGB, GL.GL_UNSIGNED_BYTE  ); break;
        case 2: this.setPixelFormat( GL.GL_RGB16, GL.GL_RGB, GL.GL_UNSIGNED_SHORT ); break;
        case 4: this.setPixelFormat( GL.GL_RGB,   GL.GL_RGB, GL.GL_FLOAT          ); break;
        default:
            //kvsMessageError("Bytes per channel must be 1, 2 or 4.");
            break;
        }
    }
    
    protected void determine_pixel_format_for_4_channel( final int bytes_per_channel )
    {
        switch( bytes_per_channel )
        {
        case 1: this.setPixelFormat( GL.GL_RGBA8,  GL.GL_RGBA, GL.GL_UNSIGNED_BYTE  ); break;
        case 2: this.setPixelFormat( GL.GL_RGBA16, GL.GL_RGBA, GL.GL_UNSIGNED_SHORT ); break;
        case 4: this.setPixelFormat( GL.GL_RGBA,   GL.GL_RGBA, GL.GL_FLOAT          ); break;
        default:
            //kvsMessageError("Bytes per channel must be 1, 2 or 4.");
            break;
        }
    }

}
