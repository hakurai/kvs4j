package kvs.core.visualization.mapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kvs.core.util.IntFloatPair;

public class OpacityMap implements Serializable{

    private static final long serialVersionUID = 6180980460718299773L;
    /*
     * typedef kvs::ValueArray<float>   Table;
     * typedef std::pair<size_t, float> Point;
     * typedef std::list<Point>         Points;
     */
    
    private List<IntFloatPair> m_points = new ArrayList<IntFloatPair>(); ///< Control point list.
    private float[]  m_table = new float[0];  ///< Value table.
    
    public OpacityMap(){

    }

    public OpacityMap( final int resolution ){
        this.create( resolution );
    }
    
    public OpacityMap( final float[] table ){
        m_table = table;
    }
    
    public OpacityMap( final OpacityMap other ){
        m_points = other.m_points;
        m_table = other.m_table;
    }
    
    public int resolution()
    {
        return( m_table.length );
    }
    
    public float[] table()
    {
        return( m_table );
    }
    
    public void create( final int resolution )
    {
        m_points.clear();
        m_table = new float[resolution];

        final float scale = 1.0f / (float)( resolution - 1 );
        for ( int i = 0; i < resolution; ++i )
        {
            m_table[i] = scale * (float)( i );
        }
    }
    
    public float getAt( final int index ){
        return( m_table[index] );
    }

}
