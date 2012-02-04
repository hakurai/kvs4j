package kvs.core.util;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class Utility {
    
    private Utility(){};

    public static String fgets( RandomAccessFile raf, int length ) throws IOException{
        char[] buf = new char[length];
        int i;
        for( i = 0; i < length; i++ ){
            buf[i] = (char)raf.read();
            if( buf[i] == 0x0a){
                i++;
                break;
            }

        }
            return String.valueOf( buf, 0, i );


    }
    
    public static final int[] ListToIntArray( List<? extends Number> list ){
        if( list.size() == 0 ){
            return new int[0];
        }
        Number[] values = new Number[1];
        values = list.toArray( values );
        int[] array = new int[values.length];
        for( int i =0; i < values.length; ++i ){
            array[i] = values[i].intValue();
        }
        return array;    
    }
    
    public static final float[] ListToFloatArray( List<? extends Number> list ){
        if( list.size() == 0 ){
            return new float[0];
        }
        Number[] values = new Number[1];
        values = list.toArray( values );
        float[] array = new float[values.length];
        for( int i =0; i < values.length; ++i ){
            array[i] = values[i].floatValue();
        }
        return array;    
    }
    
    public static final float[] bufferToFloatArray( Buffer buffer ){
        buffer.clear();
        if( buffer instanceof ByteBuffer){
            ByteBuffer tmp = (ByteBuffer)buffer;
            float[] array = new float[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (float)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof CharBuffer ) {
            CharBuffer tmp = (CharBuffer)buffer;
            float[] array = new float[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (float)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof ShortBuffer ) {
            ShortBuffer tmp = (ShortBuffer)buffer;
            float[] array = new float[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (float)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof IntBuffer ) {
            IntBuffer tmp = (IntBuffer)buffer;
            float[] array = new float[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (float)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof DoubleBuffer ) {
            DoubleBuffer tmp = (DoubleBuffer)buffer;
            double[] tmp_array = tmp.array();
            float[] array = new float[tmp_array.length];
            for( int i = 0; i < tmp_array.length; ++i ){
                array[i] = (float)tmp_array[i];
            }
            return array;
            
        } else if( buffer instanceof LongBuffer ) {
            LongBuffer tmp = (LongBuffer)buffer;
            float[] array = new float[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (float)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof FloatBuffer ) {
            FloatBuffer tmp = (FloatBuffer)buffer;
            float[] array = new float[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (float)tmp.get();
            }
            return array;
            
        }
        return null;
    }
    
    public static final double[] bufferToDoubleArray( Buffer buffer ){
        buffer.clear();
        if( buffer instanceof ByteBuffer){
            ByteBuffer tmp = (ByteBuffer)buffer;
            double[] array = new double[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (double)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof CharBuffer ) {
            CharBuffer tmp = (CharBuffer)buffer;
            double[] array = new double[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (double)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof ShortBuffer ) {
            ShortBuffer tmp = (ShortBuffer)buffer;
            double[] array = new double[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (double)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof IntBuffer ) {
            IntBuffer tmp = (IntBuffer)buffer;
            double[] array = new double[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (double)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof FloatBuffer ) {
            FloatBuffer tmp = (FloatBuffer)buffer;
            double[] array = new double[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (double)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof LongBuffer ) {
            LongBuffer tmp = (LongBuffer)buffer;
            double[] array = new double[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (double)tmp.get();
            }
            return array;
            
        } else if( buffer instanceof DoubleBuffer ) {
            DoubleBuffer tmp = (DoubleBuffer)buffer;
            double[] array = new double[tmp.limit()];
            for( int i = 0; i < array.length; ++i ){
                array[i] = (double)tmp.get();
            }
            return array;
            
        }
        return null;
    }

    public static final FloatBuffer bufferToDirectFloatBuffer( Buffer buffer ){
        buffer.clear();
        if( buffer instanceof ByteBuffer){
            ByteBuffer tmp = (ByteBuffer)buffer;
            FloatBuffer dBuffer = ByteBuffer.allocateDirect( tmp.limit() ).asFloatBuffer();
            for( int i = 0; i < tmp.limit(); ++i ){
                dBuffer.put( (float)tmp.get() );
            }
            return dBuffer;

        } else if( buffer instanceof CharBuffer ) {
            CharBuffer tmp = (CharBuffer)buffer;
            FloatBuffer dBuffer = ByteBuffer.allocateDirect( tmp.limit() * 2 ).asFloatBuffer();
            for( int i = 0; i < tmp.limit(); ++i ){
                dBuffer.put( (float)tmp.get() );
            }
            return dBuffer;

        } else if( buffer instanceof ShortBuffer ) {
            ShortBuffer tmp = (ShortBuffer)buffer;
            FloatBuffer dBuffer = ByteBuffer.allocateDirect( tmp.limit() * 2 ).asFloatBuffer();
            for( int i = 0; i < tmp.limit(); ++i ){
                dBuffer.put( (float)tmp.get() );
            }
            return dBuffer;

        } else if( buffer instanceof IntBuffer ) {
            IntBuffer tmp = (IntBuffer)buffer;
            FloatBuffer dBuffer = ByteBuffer.allocateDirect( tmp.limit() * 4 ).asFloatBuffer();
            for( int i = 0; i < tmp.limit(); ++i ){
                dBuffer.put( (float)tmp.get() );
            }
            return dBuffer;

        } else if( buffer instanceof DoubleBuffer ) {
            DoubleBuffer tmp = (DoubleBuffer)buffer;
            FloatBuffer dBuffer = ByteBuffer.allocateDirect( tmp.limit() * 8 ).asFloatBuffer();
            for( int i = 0; i < tmp.limit(); ++i ){
                dBuffer.put( (float)tmp.get() );
            }
            return dBuffer;

        } else if( buffer instanceof LongBuffer ) {
            LongBuffer tmp = (LongBuffer)buffer;
            FloatBuffer dBuffer = ByteBuffer.allocateDirect( tmp.limit() * 8 ).asFloatBuffer();
            for( int i = 0; i < tmp.limit(); ++i ){
                dBuffer.put( (float)tmp.get() );
            }
            return dBuffer;

        } else if( buffer instanceof FloatBuffer ) {
            FloatBuffer tmp = (FloatBuffer)buffer;
            FloatBuffer dBuffer = ByteBuffer.allocateDirect( tmp.limit() * 4 ).asFloatBuffer();
            for( int i = 0; i < tmp.limit(); ++i ){
                dBuffer.put( (float)tmp.get() );
            }
            return dBuffer;

        }
        return null;
    }

}
