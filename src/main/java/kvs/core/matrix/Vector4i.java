package kvs.core.matrix;

/**
 * Vector4iクラスは成分表現にint型を利用する4次元ベクトルを表します。
 */

public final class Vector4i extends AbstractVectorInt {

    private static final long serialVersionUID = -772089528857109738L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector4i ZERO = new Vector4i( 0, 0, 0, 0 );

    /**
     * 指定されたX,Y,Z,W成分を持つ4次元ベクトルを構築します。
     * 
     * @param x X,Y,Z,W成分
     */
    public Vector4i( final int x ) {
        this( x, x, x, x );
    }

    /**
     * 指定されたX,Y,Z,W成分を持つ4次元ベクトルを構築します。
     * 
     * @param x X成分
     * @param y Y成分
     * @param z Z成分
     * @param w W成分
     */
    public Vector4i( final int x, final int y, final int z, final int w ) {
        super( 4 );
        this.m_elements[0] = x;
        this.m_elements[1] = y;
        this.m_elements[2] = z;
        this.m_elements[3] = w;
    }

    /**
     * 指定されたVector2iとz,wの値から4次元ベクトルを構築します。
     * 
     * @param other 2次元ベクトル
     * @param z Z成分
     * @param w W成分
     */
    public Vector4i( final Vector2i other, final int z, final int w ) {
        this( other.getX(), other.getY(), z, w );
    }

    /**
     * 指定されたVector3iとwの値から4次元ベクトルを構築します。
     * 
     * @param other 3次元ベクトル
     * @param w W成分
     */
    public Vector4i( final Vector3i other, final int w ) {
        this( other.getX(), other.getY(), other.getZ(), w );

    }

    /**
     * 指定されたint配列から4次元ベクトルを構築します。
     * 
     * @param elements 4つ以上の値を持つint配列
     * @exception IndexOutOfBoundsException elements.length < 4
     */
    public Vector4i( final int[] elements ) {
        this( elements[0], elements[1], elements[2], elements[3] );

    }

    /**
     * X成分の値を返します。
     * 
     * @return X成分の値
     */
    public int x() {
        return this.m_elements[0];
    }

    /**
     * Y成分の値を返します。
     * 
     * @return Y成分の値
     */
    public int y() {
        return this.m_elements[1];
    }

    /**
     * Z成分の値を返します。
     * 
     * @return Z成分の値
     */
    public int z() {
        return this.m_elements[2];
    }

    /**
     * W成分の値を返します。
     * 
     * @return W成分の値
     */
    public int w() {
        return this.m_elements[3];
    }
    
    /**
     * 正規化したベクトルを返します。
     * 
     * @exception ArithmeticException ベクトルの大きさが0の場合
     */
    public Vector4i normalize() {

        int normalize_factor = (int) (1.0 / this.length());
        return new Vector4i(
                this.m_elements[0] * normalize_factor,
                this.m_elements[1] * normalize_factor,
                this.m_elements[2] * normalize_factor,
                this.m_elements[3] * normalize_factor );
    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param rhs 内積を取る相手のベクトル
     * @return ベクトルの内積
     */
    public int dot( final Vector4i rhs ) {
        int result = 0;

        result += this.m_elements[0] * rhs.get( 0 );
        result += this.m_elements[1] * rhs.get( 1 );
        result += this.m_elements[2] * rhs.get( 2 );
        result += this.m_elements[3] * rhs.get( 3 );

        return (result);

    }

    /**
     * 指定されたベクトルとの和を返します。
     * 
     * @param rhs 和を取る相手のベクトル
     * @return 指定されたベクトルとの和
     */
    public Vector4i add( final Vector4i rhs ) {
        return new Vector4i(
                this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1],
                this.m_elements[2] + rhs.m_elements[2],
                this.m_elements[3] + rhs.m_elements[3] );
    }

    /**
     * 指定されたベクトルとの差を取ります。
     * 
     * @param rhs 差を取る相手のベクトル
     * @return ベクトルの差
     */
    public Vector4i sub( final Vector4i rhs ) {
        return new Vector4i(
                this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1],
                this.m_elements[2] - rhs.m_elements[2],
                this.m_elements[3] - rhs.m_elements[3] );
    }

    /**
     * 指定されたスケールとの積を取ります。
     * 
     * @param s スケール
     * @return (this *= s)
     */
    public Vector4i mul( final int s ) {
        return new Vector4i(
                this.m_elements[0] * s,
                this.m_elements[1] * s,
                this.m_elements[2] * s,
                this.m_elements[3] * s );
    }

    /**
     * 指定されたスケールとの商を取ります。
     * 
     * @param s スケール
     * @return (this /= s)
     * @exception ArithmeticException
     */
    public Vector4i div( final int s ) {
        return new Vector4i(
                this.m_elements[0] / s,
                this.m_elements[1] / s,
                this.m_elements[2] / s,
                this.m_elements[3] / s );
    }
}
