package kvs.core.image;

import java.awt.Color;

public class HSVColor {
    
    protected float m_hue;        ///< hue angle         [0-1]
    protected float m_saturation; ///< saturation        [0-1]
    protected float m_value;      ///< value (intensity) [0-1]
    
    public HSVColor( float hue, float saturation, float value ){
        m_hue = hue;
        m_saturation = saturation;
        m_value = value;
    }
    
    public HSVColor( final HSVColor hsv ){
        m_hue = hsv.m_hue;
        m_saturation = hsv.m_saturation;
        m_value = hsv.m_value;
    }
    
    
    
    public HSVColor( final Color rgb ){
        float R = rgb.getRed() / 255.0f;
        float G = rgb.getGreen() / 255.0f;
        float B = rgb.getBlue() / 255.0f;

        float min_rgb = kvs.core.util.Math.min( R, G, B );
        float max_rgb = kvs.core.util.Math.max( R, G, B );

        float delta = max_rgb - min_rgb;

        m_value = max_rgb;

        if( kvs.core.util.Math.isZero( delta ) )
        {
            m_hue = 0.0f;
            m_saturation = 0.0f;
        }
        else
        {
            m_saturation = delta / max_rgb;

            float delta_r = ( ( ( max_rgb - R ) / 6.0f ) + ( delta / 2.0f ) ) / delta;
            float delta_g = ( ( ( max_rgb - G ) / 6.0f ) + ( delta / 2.0f ) ) / delta;
            float delta_b = ( ( ( max_rgb - B ) / 6.0f ) + ( delta / 2.0f ) ) / delta;

            if(      kvs.core.util.Math.equal( R, max_rgb ) ) m_hue = delta_b - delta_g;
            else if( kvs.core.util.Math.equal( G, max_rgb ) ) m_hue = ( 1.0f / 3.0f ) + delta_r - delta_b;
            else if( kvs.core.util.Math.equal( B, max_rgb ) ) m_hue = ( 2.0f / 3.0f ) + delta_g - delta_r;

            if( m_hue < 0 ) m_hue += 1;
            if( m_hue > 1 ) m_hue -= 1;
        }
    }
    
    public float h()
    {
        return( m_hue );
    }
    
    public float getHue()
    {
        return( m_hue );
    }
    
    public float s()
    {
        return( m_saturation );
    }
    
    public float getSaturation()
    {
        return( m_saturation );
    }
    
    public float v()
    {
        return( m_value );
    }
    
    public float getValue()
    {
        return( m_value );
    }
    
    public float getIntensity()
    {
        return( m_value );
    }
    
    public Color toRGB(){
        int red;
        int green;
        int blue;
        if( kvs.core.util.Math.isZero( this.s() ) )
        {
            red   = (int)( this.v() * 255 );
            green = (int)( this.v() * 255 );
            blue  = (int)( this.v() * 255 );
        }
        else
        {
            float h = this.h() * 6.0f;
            int   i = (int)( h );

            float tmp1 = this.v() * ( 1 - this.s() );
            float tmp2 = this.v() * ( 1 - this.s() * ( h - i ) );
            float tmp3 = this.v() * ( 1 - this.s() * ( 1 - h + i ) );

            float tmp_r, tmp_g, tmp_b;
            switch( i )
            {
            case 0:
            {
                tmp_r = this.v();
                tmp_g = tmp3;
                tmp_b = tmp1;
                break;
            }
            case 1:
            {
                tmp_r = tmp2;
                tmp_g = this.v();
                tmp_b = tmp1;
                break;
            }
            case 2:
            {
                tmp_r = tmp1;
                tmp_g = this.v();
                tmp_b = tmp3;
                break;
            }
            case 3:
            {
                tmp_r = tmp1;
                tmp_g = tmp2;
                tmp_b = this.v();
                break;
            }
            case 4:
            {
                tmp_r = tmp3;
                tmp_g = tmp1;
                tmp_b = this.v();
                break;
            }
            default:
            {
                tmp_r = this.v();
                tmp_g = tmp1;
                tmp_b = tmp2;
                break;
            }
            }

            red   = (int)( tmp_r * 255 );
            green = (int)( tmp_g * 255 );
            blue  = (int)( tmp_b * 255 );
        }
        return new Color( red, green, blue);
    }

}
