package kvs.core.util;

import java.io.Serializable;

/**
 * Pairクラスはプリミティブなint値のペアを持つクラスです。
 */

public class IntPair implements Serializable{
    
    private static final long serialVersionUID = 1687355955338218090L;
    private int m_first;
    private int m_second;
    
    /**
     * 新しいペアを生成します。
     * 
     * @param first 一つ目の値
     * @param second 二つ目の値
     */
    public IntPair( int first, int second){
        this.m_first = first;
        this.m_second = second;
    }
    
    public int getFirst(){
        return m_first;
    }
    
    public int getSecond(){
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
        result = prime * result + m_second;
        return result;
    }
    @Override
    public boolean equals( Object obj ) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof IntPair))
            return false;
        IntPair other = (IntPair) obj;
        if (m_first != other.m_first)
            return false;
        if (m_second != other.m_second)
            return false;
        return true;
    }

}
