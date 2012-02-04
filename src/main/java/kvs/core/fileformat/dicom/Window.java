package kvs.core.fileformat.dicom;

public class Window {

    protected short     m_bits;      ///< number of bits
    protected boolean   m_sign;      ///< true: signed, false: unsigned
    protected int       m_min_value; ///< maximum value of the window's range
    protected int       m_max_value; ///< minimum value of the window's range
    protected int       m_range;     ///< range of the window
    protected int       m_level;     ///< current window level
    protected int       m_width;     ///< current window width

    public Window(){

    }

    public Window( final short bits, final boolean sign ){
        this.set( bits, sign );
    }
    
    public void set( final short bits, final boolean sign )
    {
        switch( bits )
        {
        case 8:  this.set_range_8bit( sign );  break;
        case 12: this.set_range_12bit( sign ); break;
        case 16: this.set_range_16bit( sign ); break;
        default: break;
        }

        m_range = m_max_value - m_min_value + 1;
    }
    
    public void rescale( final double slope, final double intersept )
    {
        final double min_value = m_min_value * slope + intersept;
        final double max_value = m_max_value * slope + intersept;

        m_min_value = (int)min_value;
        m_max_value = (int)max_value;
    }
    
    public void setLevel( final int level )
    {
        m_level = level;
    }

    public void setWidth( final int width )
    {
        m_width = width;
    }
    
    public int minValue()
    {
        return m_min_value;
    }
    
    public int maxValue()
    {
        return m_max_value;
    }

    public int level()
    {
        return m_level;
    }

    public int width()
    {
        return m_width;
    }
    
    public int clampLevel( final int level )
    {
        return kvs.core.util.Math.clamp( level, m_min_value, m_max_value );
    }
    
    public int clampWidth( final int width )
    {
        return kvs.core.util.Math.clamp( width, 0, m_range );
    }
    
    protected void set_range_8bit( final boolean sign )
    {

            m_min_value = Byte.MIN_VALUE;
            m_max_value = Byte.MAX_VALUE;
    }
    
    protected void set_range_12bit( final boolean sign )
    {

            m_min_value = Short.MIN_VALUE;
            m_max_value = Short.MAX_VALUE;
    }
    
    protected void set_range_16bit( final boolean sign )
    {

            m_min_value = Short.MIN_VALUE;
            m_max_value = Short.MAX_VALUE;
    }

}
