package kvs.core.matrix;

/**
 * Vectorfクラスは成分表現にfloat型を利用する多次元ベクトルを表します。
 */
public final class Vectorf extends AbstractVectorFloat {

    private static final long serialVersionUID = -5660930697092662009L;
    private final int m_size;
    
    /**
     * 指定されたサイズのゼロベクトルを構築します。
     * 
     * @param size 次元数
     */
    public Vectorf( final int size ) {
        super( size );
        this.m_size = size;
        for (int i = 0; i < this.m_size; ++i) {
            this.m_elements[i] = 0.0f;
        }
    }

    /**
     * 指定された成分を持つベクトルを構築します。
     * 
     * @param elements　配列
     */
    public Vectorf( final float[] elements ) {
        super( elements.length );
        this.m_size = elements.length;
        for(int i = 0;i < this.m_size;++i){
             this.m_elements[i] = elements[i];
        }
    }

    /**
     * 次元数を返します。
     * 
     * @return サイズ
     */
    public int getSize() {
        return this.m_size;
    }
    
    /**
     * 指定された位置の成分を指定した値に変更した新しいベクトルを返します。
     * 
     * @param i 値を設定する要素のインデックス
     * @param x 設定する値
     * @exception IndexOutOfBoundsException size() < i
     */
    public Vectorf modify( final int i, final float x ) {
        float[] elements = new float[m_size];
        System.arraycopy(this.m_elements, 0, elements, 0, m_elements.length);
        elements[i] = x;
        
        return new Vectorf(elements);
    }

    /**
     * 正規化したベクトルを返します。
     * 
     * @exception ArithmeticException
     */
    public Vectorf normalize() {

        float normalize_factor = (float) (1.0 / this.length());
        float[] elements = new float[this.m_size];
        
        for(int i = 0;i < this.m_size;++i){
            elements[i] = this.m_elements[i] * normalize_factor;
        }
        return new Vectorf(elements);

    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param other 内積を取る相手のベクトル
     * @return 指定されたベクトルとの内積
     */
    public float dot( final Vectorf other ) {
        float result = 0f;
        for (int i = 0; i < this.getSize(); ++i) {
            result += this.m_elements[i] * other.m_elements[i];
        }

        return (result);

    }

    /**
     * 指定されたベクトルとの和を返します。
     * 二つのベクトルは同じ次元数である必要があります。
     * 
     * @param rhs 和を取る相手のベクトル
     * @return 指定したベクトルとの和 this + rhs
     * @exception ArithmeticException this.size() != rhs.size()
     */
    public Vectorf add( final Vectorf rhs ) {
        if(this.getSize() != rhs.getSize()){
            throw new ArithmeticException("Invalid Vector");
        }
        float[] elements = new float[this.m_size];
        for (int i = 0; i < this.getSize(); ++i) {
            elements[i] = this.m_elements[i] + rhs.m_elements[i];
        }
        return new Vectorf(elements);
    }
    

    /**
     * 指定されたベクトルとの差を返します。
     * 二つのベクトルは同じ次元数である必要があります。
     * 
     * @param rhs 差を取る相手のベクトル
     * @return 指定されたベクトルとの差 this - rhs
     * @exception ArithmeticException this.size() != rhs.size()
     */
    public Vectorf sub( final Vectorf rhs ) {
        if(this.getSize() != rhs.getSize()){
            throw new ArithmeticException("Invalid Vector");
        }
        float[] elements = new float[this.m_size];
        for (int i = 0; i < this.getSize(); ++i) {
            elements[i] = this.m_elements[i] - rhs.m_elements[i];
        }
        return new Vectorf(elements);
    }
    
    /**
     * 指定されたスケールとの積を返します。
     * 
     * @param s スケール
     * @return this * s
     */
    public Vectorf mul( final float s ) {
        float[] elements = new float[this.m_size];
        for (int i = 0; i < this.getSize(); ++i) {
            elements[i] = this.m_elements[i] * s;
        }
        return new Vectorf(elements);
    }

    /**
     * 指定されたスケールとの商を返します。
     * 
     * @param s スケール
     * @return this / s
     * @exception ArithmeticException s = 0
     */
    public Vectorf div( final float s ) {
        float[] elements = new float[this.m_size];
        for (int i = 0; i < this.getSize(); ++i) {
            elements[i] = this.m_elements[i] / s;
        }
        return new Vectorf(elements);
    }
}
