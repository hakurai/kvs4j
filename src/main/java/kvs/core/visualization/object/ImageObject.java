package kvs.core.visualization.object;

import java.nio.ByteBuffer;

public class ImageObject extends ObjectBase {

    private static final long serialVersionUID = -6573687788900709916L;

    public enum PixelType
    {
        Gray8(8), ///< 8 bit gray pixel
        Gray16(16), ///< 16 bit gray pixel
        Color24(24), ///< 24 bit RGB color pixel (8x8x8 bits)
        Color32(32);  ///< 32 bit RGBA color pixel (8x8x8x8 bits)

        PixelType( int value ){
            this.m_value = value;
        }

        public int intValue(){
            return this.m_value;
        }

        private int m_value;

    };

    protected PixelType      m_type;   ///< pixel type
    protected int                      m_width;  ///< image width
    protected int                      m_height; ///< image height
    protected ByteBuffer m_data;   ///< pixel data

    public ImageObject(){}
    
    public ImageObject(
                       final int                       width,
                       final int                       height,
                       final ByteBuffer data)
    {
        this( width, height, data, PixelType.Color24);
    }

    public ImageObject(
                       final int                       width,
                       final int                       height,
                       final ByteBuffer data,
                       final PixelType       type )
    {
        this.m_type = type;
        this.m_width = width;
        this.m_height = height;
        this.m_data = data; // shallow copy
    }
    
    public void setWidth( int width )
    {
        m_width = width;
    }
    
    public void setHeight( int height )
    {
        m_height = height;
    }
    
    public void setData( ByteBuffer data ){
        m_data = data;
    }
    
    public void setType( PixelType type ){
        m_type = type;
    }
    
    public ObjectType objectType()
    {
        return( ObjectType.Image );
    }
    
    public PixelType type()
    {
        return( m_type );
    }
    
    public int width()
    {
        return( m_width );
    }
    
    public int height()
    {
        return( m_height );
    }
    
    public ByteBuffer data()
    {
        return( m_data );
    }
    
    public int bitsPerPixel()
    {
        return( m_type.intValue() );
    }
    
    public int bytesPerPixel()
    {
        return( m_type.intValue() >> 3 );
    }
    
    public int nchannels()
    {
        int ret = 0;
        switch ( m_type )
        {
        case Gray8:   ret = 1; break;
        case Gray16:  ret = 1; break;
        case Color24: ret = 3; break;
        case Color32: ret = 4; break;
        default: break;
        }

        return( ret );
    }
    
    public int get_npixels()
    {
        return( ( m_type.intValue() >> 3 ) * m_width * m_height );
    }
    
    public static ImageObject valueOf( ObjectBase object ){
        if( object instanceof ImageObject ){
            return (ImageObject)object;
        }
        return null;
    }
    

}
