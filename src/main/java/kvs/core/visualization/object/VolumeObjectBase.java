package kvs.core.visualization.object;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public abstract class VolumeObjectBase extends ObjectBase {
    
    private static final long serialVersionUID = -1468667482296749680L;

    public enum VolumeType
    {
        Structured, ///< Structured volume.
        Unstructured   ///< Unstructured volume.
    };

    public enum GridType
    {
        UnknownGridType,
        Uniform,             ///< Uniform grid.
        Rectilinear,         ///< Rectilinear grid.
        Curvilinear,         ///< Curvilinear grid.
        Irregular           ///< Irregular grid.
    };

    public enum CellType
    {
        UnknownCellType(0),
        Tetrahedra(4),  ///< Tetrahedral cell.
        Hexahedra(8),  ///< Hexahedral cell.
        QuadraticTetrahedra(10), ///< Quadratic tetrahedral cell.
        QuadraticHexahedra(20); ///< Quadratic Hexahedral cell.
        
        private int m_value;
        
        private CellType( int value ){
            m_value = value;
        }
        
        public int intValue(){
            return m_value;
        }
    };
    
    private int         m_veclen = 0; ///< Vector length.

    private float[]     m_coords; ///< Coordinate array.
    private Buffer      m_values; ///< Value array.
    
    private boolean     m_has_min_max_values = false; ///< Whether includes min/max values or not.
    private double      m_min_value = 0.0;          ///< Minimum field value.
    private double      m_max_value = 0.0;          ///< Maximum field value.
    
    
    public VolumeObjectBase()
    {
    }

    public VolumeObjectBase(int    veclen,
                            float[]    coords,
                            Buffer    values ){
        super();
        m_veclen = veclen;
        m_coords = coords;
        m_values = values;
    }
    
    /*
    public VolumeObjectBase( VolumeObjectBase other )

    {
        super(other);
        m_veclen = other.veclen();
        m_coords = other.coords();
        m_values = other.values();
        m_has_min_max_values = other.hasMinMaxValues();
        m_min_value = other.minValue();
        m_max_value = other.maxValue();
    }
    */
    
    public void setVeclen( int veclen )
    {
        m_veclen = veclen;
    }
    
    public void setCoords( float[] coords )
    {
        m_coords = coords;
    }
    
    public void setValues( Buffer values )
    {
        m_values = values;
    }
    
    public void setMinMaxValues(
            double min_value,
            double max_value )
        {
            m_min_value          = min_value;
            m_max_value          = max_value;
            m_has_min_max_values = true;
        }
    
    @Override
    public ObjectType objectType()
    {
        return( ObjectBase.ObjectType.Volume );
    }
    
    public abstract VolumeType volumeType();

    public abstract GridType gridType();

    public abstract CellType cellType();

    public abstract int nnodes();

    public int veclen()
    {
        return( m_veclen );
    }

    public float[] coords()
    {
        float[] coords = new float[m_coords.length];
        System.arraycopy( m_coords, 0, coords, 0, m_coords.length );
        return( coords );
    }
    
    public Buffer values()
    {
        return( m_values );
    }


    public boolean hasMinMaxValues()
    {
        return( m_has_min_max_values );
    }

    public double minValue()
    {
        return( m_min_value );
    }

    public double maxValue()
    {
        return( m_max_value );
    }
    
    public void updateMinMaxValues() 
    {
        m_values.clear();
        if( m_values instanceof ByteBuffer){
            calculate_min_max_values( (ByteBuffer)m_values );
        } else if( m_values instanceof ShortBuffer ) {
            calculate_min_max_values( (ShortBuffer)m_values );
        } else if( m_values instanceof IntBuffer ) {
            calculate_min_max_values( (IntBuffer)m_values );
        } else if( m_values instanceof FloatBuffer ) {
            calculate_min_max_values( (FloatBuffer)m_values );
        } else if( m_values instanceof DoubleBuffer ) {
            calculate_min_max_values( (DoubleBuffer)m_values );
        }
        
    }
    
    private void calculate_min_max_values( ByteBuffer valuesBuffer )
    {        
        byte[] values = valuesBuffer.array();
        
        int index = 0;
        final int end = values.length;

        if ( m_veclen == 1 )
        {
            double min_value = values[index];
            double max_value = values[index];

            while ( index < end )
            {
                min_value = kvs.core.util.Math.min( values[index], min_value );
                max_value = kvs.core.util.Math.max( values[index], max_value );
                ++index;
            }

            this.setMinMaxValues(
                    min_value,
                    max_value );
        }
        else
        {
            double min_value = 0.0;
            double max_value = 0.0;

            final int veclen = m_veclen;

            while ( index < end )
            {
                double magnitude = 0.0;
                for ( int i = 0; i < veclen; ++i )
                {
                    magnitude = values[index] * values[index];
                    ++index;
                }
                magnitude = Math.sqrt( magnitude );

                min_value = kvs.core.util.Math.min( magnitude, min_value );
                max_value = kvs.core.util.Math.max( magnitude, max_value );
            }

            this.setMinMaxValues( min_value, max_value );
        }
    }
    
    private void calculate_min_max_values( ShortBuffer valuesBuffer )
    {        
        short[] values = valuesBuffer.array();
        
        int index = 0;
        final int end = values.length;

        if ( m_veclen == 1 )
        {
            double min_value = values[index];
            double max_value = values[index];

            while ( index < end )
            {
                min_value = kvs.core.util.Math.min( values[index], min_value );
                max_value = kvs.core.util.Math.max( values[index], max_value );
                ++index;
            }

            this.setMinMaxValues(
                    min_value,
                    max_value );
        }
        else
        {
            double min_value = 0.0;
            double max_value = 0.0;

            final int veclen = m_veclen;

            while ( index < end )
            {
                double magnitude = 0.0;
                for ( int i = 0; i < veclen; ++i )
                {
                    magnitude = values[index] * values[index];
                    ++index;
                }
                magnitude = Math.sqrt( magnitude );

                min_value = kvs.core.util.Math.min( magnitude, min_value );
                max_value = kvs.core.util.Math.max( magnitude, max_value );
            }

            this.setMinMaxValues( min_value, max_value );
        }
    }
    
    private void calculate_min_max_values( IntBuffer valuesBuffer )
    {        
        int[] values = valuesBuffer.array();
        
        int index = 0;
        final int end = values.length;

        if ( m_veclen == 1 )
        {
            double min_value = values[index];
            double max_value = values[index];

            while ( index < end )
            {
                min_value = kvs.core.util.Math.min( values[index], min_value );
                max_value = kvs.core.util.Math.max( values[index], max_value );
                ++index;
            }

            this.setMinMaxValues(
                    min_value,
                    max_value );
        }
        else
        {
            double min_value = 0.0;
            double max_value = 0.0;

            final int veclen = m_veclen;

            while ( index < end )
            {
                double magnitude = 0.0;
                for ( int i = 0; i < veclen; ++i )
                {
                    magnitude = values[index] * values[index];
                    ++index;
                }
                magnitude = Math.sqrt( magnitude );

                min_value = kvs.core.util.Math.min( magnitude, min_value );
                max_value = kvs.core.util.Math.max( magnitude, max_value );
            }

            this.setMinMaxValues( min_value, max_value );
        }
    }
    
    
    private void calculate_min_max_values( FloatBuffer values )
    {        
        //float[] values = valuesBuffer.array(); //FIXME UnsupportedOperationException
        
        int index = 0;
        final int end = values.limit();

        if ( m_veclen == 1 )
        {
            double min_value = values.get( index );
            double max_value = values.get( index );

            while ( index < end )
            {
                min_value = kvs.core.util.Math.min( values.get( index ), min_value );
                max_value = kvs.core.util.Math.max( values.get( index ), max_value );
                ++index;
            }

            this.setMinMaxValues(
                    min_value,
                    max_value );
        }
        else
        {
            double min_value = 0.0;
            double max_value = 0.0;

            final int veclen = m_veclen;

            while ( index < end )
            {
                double magnitude = 0.0;
                for ( int i = 0; i < veclen; ++i )
                {
                    magnitude = values.get( index ) * values.get( index );
                    ++index;
                }
                magnitude = Math.sqrt( magnitude );

                min_value = kvs.core.util.Math.min( magnitude, min_value );
                max_value = kvs.core.util.Math.max( magnitude, max_value );
            }

            this.setMinMaxValues( min_value, max_value );
        }
    }
    
    private void calculate_min_max_values( DoubleBuffer values )
    {        
        //double[] values = valuesBuffer.array();
        
        int index = 0;
        final int end = values.limit();

        if ( m_veclen == 1 )
        {
            double min_value = values.get( index );
            double max_value = values.get( index );

            while ( index < end )
            {
                min_value = kvs.core.util.Math.min( values.get( index ), min_value );
                max_value = kvs.core.util.Math.max( values.get( index ), max_value );
                ++index;
            }

            this.setMinMaxValues(
                    min_value,
                    max_value );
        }
        else
        {
            double min_value = 0.0;
            double max_value = 0.0;

            final int veclen = m_veclen;

            while ( index < end )
            {
                double magnitude = 0.0;
                for ( int i = 0; i < veclen; ++i )
                {
                    magnitude = values.get( index ) * values.get( index );
                    ++index;
                }
                magnitude = Math.sqrt( magnitude );

                min_value = kvs.core.util.Math.min( magnitude, min_value );
                max_value = kvs.core.util.Math.max( magnitude, max_value );
            }

            this.setMinMaxValues( min_value, max_value );
        }
    }
    
}
