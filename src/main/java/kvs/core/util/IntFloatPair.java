package kvs.core.util;

import java.io.Serializable;

public class IntFloatPair implements Serializable{

    private static final long serialVersionUID = -2821044703790240767L;
    private int m_first;
    private float m_second;
    
    /**
     * 新しいペアを生成します。
     * 
     * @param first 一つ目の値
     * @param second 二つ目の値
     */
    public IntFloatPair( int first, int second){
        this.m_first = first;
        this.m_second = second;
    }
    
    public int getFirst(){
        return m_first;
    }
    
    public float getSecond(){
        return m_second;
    }
    
    public void setFirst( int first ){
        m_first = first;
    }
    
    public void setSecond( int second ){
        m_second = second;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + m_first;
        result = prime * result + (int)(m_second);
        return result;
    }
    @Override
    public boolean equals( Object obj ) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof IntFloatPair))
            return false;
        IntFloatPair other = (IntFloatPair) obj;
        if (m_first != other.m_first)
            return false;
        if (m_second != other.m_second)
            return false;
        return true;
    }

}
