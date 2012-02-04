package kvs.core.matrix;

/**
 * Vectordクラスは成分表現にdouble型を利用する多次元ベクトルを表します。
 */
public final class Vectord extends AbstractVectorDouble {

    private static final long serialVersionUID = -6727512732658649145L;
    private final int m_size;
    
    /**
     * 指定されたサイズのゼロベクトルを構築します。
     * 
     * @param size 次元数
     */
    public Vectord( final int size ) {
        super( size );
        this.m_size = size;
        for (int i = 0; i < this.m_size; ++i) {
            this.m_elements[i] = 0.0;
        }
    }

    /**
     * 指定された成分を持つベクトルを構築します。
     * 
     * @param elements　配列
     */
    public Vectord( final double[] elements ) {
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
    public Vectord modify( final int i, final double x ) {
        double[] elements = new double[m_size];
        System.arraycopy(this.m_elements, 0, elements, 0, m_elements.length);
        elements[i] = x;
        
        return new Vectord(elements);
    }

    /**
     * 正規化したベクトルを返します。
     * 
     * @exception ArithmeticException
     */
    public Vectord normalize() {

        double normalize_factor = 1.0 / this.length();
        double[] elements = new double[this.m_size];
        
        for(int i = 0;i < this.m_size;++i){
            elements[i] = this.m_elements[i] * normalize_factor;
        }
        return new Vectord(elements);

    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param other 内積を取る相手のベクトル
     * @return 指定されたベクトルとの内積
     */
    public double dot( final Vectord other ) {
        double result = 0d;
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
    public Vectord add( final Vectord rhs ) {
        if(this.getSize() != rhs.getSize()){
            throw new ArithmeticException("Invalid Vector");
        }
        double[] elements = new double[this.m_size];
        for (int i = 0; i < this.getSize(); ++i) {
            elements[i] = this.m_elements[i] + rhs.m_elements[i];
        }
        return new Vectord(elements);
    }
    

    /**
     * 指定されたベクトルとの差を返します。
     * 二つのベクトルは同じ次元数である必要があります。
     * 
     * @param rhs 差を取る相手のベクトル
     * @return 指定されたベクトルとの差 this - rhs
     * @exception ArithmeticException this.size() != rhs.size()
     */
    public Vectord sub( final Vectord rhs ) {
        if(this.getSize() != rhs.getSize()){
            throw new ArithmeticException("Invalid Vector");
        }
        double[] elements = new double[this.m_size];
        for (int i = 0; i < this.getSize(); ++i) {
            elements[i] = this.m_elements[i] - rhs.m_elements[i];
        }
        return new Vectord(elements);
    }
    
    /**
     * 指定されたスケールとの積を返します。
     * 
     * @param s スケール
     * @return this * s
     */
    public Vectord mul( final double s ) {
        double[] elements = new double[this.m_size];
        for (int i = 0; i < this.getSize(); ++i) {
            elements[i] = this.m_elements[i] * s;
        }
        return new Vectord(elements);
    }

    /**
     * 指定されたスケールとの商を返します。
     * 
     * @param s スケール
     * @return this / s
     * @exception ArithmeticException s = 0
     */
    public Vectord div( final double s ) {
        double[] elements = new double[this.m_size];
        for (int i = 0; i < this.getSize(); ++i) {
            elements[i] = this.m_elements[i] / s;
        }
        return new Vectord(elements);
    }
}
