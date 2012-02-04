package kvs.core.visualization.mapper;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kvs.core.image.HSVColor;
import kvs.core.util.IntPair;

public class ColorMap implements Serializable{

    private static final long serialVersionUID = 8760175827121611716L;

    /*
    typedef kvs::ValueArray<kvs::UInt8>   Table;
    typedef std::pair<size_t, kvs::UInt8> Point;
    typedef std::list<Point>              Points;
    
    */
    
    private static final int NumberOfChannels = 3;

    private List<IntPair>    m_points = new ArrayList<IntPair>();
    private int[]               m_table;

    public ColorMap(){

    }

    public ColorMap( final int resolution ){
        this.create( resolution );
    }
    
    public ColorMap( final int[] table ){
        m_table = table;
    }

    public ColorMap( ColorMap other ){
        m_points = other.m_points;
        m_table = other.m_table;
        }
    
    public int resolution()
    {
        return( m_table.length / NumberOfChannels );
    }
    
    public int[] table() 
    {
        return( m_table );
    }
    
    public void create( final int resolution )
    {
        m_points.clear();
        m_table = new int[NumberOfChannels * resolution];

        final float min_hue = 0.0f;   // blue
        final float max_hue = 240.0f; // red

        final float increment = ( max_hue - min_hue ) / (float)( resolution - 1 );

        for (int i = 0,index = 0; i < resolution; ++i )
        {
            // HSV to RGB
            final HSVColor hsv = new HSVColor(
                ( max_hue - increment * (float)( i ) ) / 360.0f,
                1.0f,
                1.0f );
            final Color rgb = hsv.toRGB();

            m_table[index++] = rgb.getRed();
            m_table[index++] = rgb.getGreen();
            m_table[index++] = rgb.getBlue();
        }
    }
    
    public Color getAt( int index )
    {
        //KVS_ASSERT( index < this->resolution() );

        //if( index < 0 ) index = 0;
        //if( index > resolution() ) index = resolution();

        final int offset = NumberOfChannels * index;
        return( new Color( m_table[offset], m_table[offset+1], m_table[offset+2] ) );
    }
}
