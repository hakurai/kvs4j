package kvs.core.matrix;

/**
 * Vector2iクラスは成分表現にint型を利用する2次元ベクトルを表します。
 */

public final class Vector2i extends AbstractVectorInt {

    private static final long serialVersionUID = 9139028518025580265L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector2i ZERO = new Vector2i( 0, 0 );

    /**
     * 指定されたX,Y成分を持つベクトルを構築します。
     * 
     * @param x X,Y成分
     */
    public Vector2i( final int x ) {
        this( x, x );
    }

    /**
     * 指定されたX,Y成分を持つベクトルを構築します。
     * 
     * @param x X成分
     * @param y Y成分
     */
    public Vector2i( final int x, final int y ) {
        super( 2 );
        this.m_elements[0] = x;
        this.m_elements[1] = y;
    }

    /**
     * 指定されたint配列からベクトルを構築します。
     * 
     * @param elements 2つ以上の値を持つint配列
     * @exception IndexOutOfBoundsException elements.length < 2
     */
    public Vector2i( final int[] elements ) {
        this( elements[0], elements[1] );
    }

    /**
     * 指定されたint配列のオフセット位置からの値でベクトルを構築します。
     * 
     * @param elements 2つ以上の値を持つfloat配列
     * @param offset オフセット
     * @exception IndexOutOfBoundsException elements.length < offset + 2
     */
    public Vector2i( final int[] elements, int offset ) {
        this( elements[offset], elements[offset + 1] );
    }

    /**
     * X成分の値を返します。
     * 
     * @return X成分の値
     */
    public int getX() {
        return this.m_elements[0];
    }

    /**
     * Y成分の値を返します。
     * 
     * @return Y成分の値
     */
    public int getY() {
        return this.m_elements[1];
    }

    /**
     * 正規化されたベクトルを返します。
     * 
     * @exception ArithmeticException ベクトルの大きさが0の場合
     */
    public Vector2i normalize() {

        int normalize_factor = (int) (1.0 / this.length());

        return new Vector2i(
                this.m_elements[0] * normalize_factor,
                this.m_elements[1] * normalize_factor );

    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param rhs 内積を取る相手のベクトル
     * @return ベクトルの内積
     */
    public int dot( final Vector2i rhs ) {
        int result = 0;

        result += this.m_elements[0] * rhs.get( 0 );
        result += this.m_elements[1] * rhs.get( 1 );

        return (result);

    }

    /**
     * 指定されたベクトルとの和を返します。
     * 
     * @param rhs 和を取る相手のベクトル
     * @return 指定されたベクトルとの和
     */
    public Vector2i add( final Vector2i rhs ) {
        return new Vector2i(
                this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1] );
    }

    /**
     * 指定されたベクトルとの差を返します。
     * 
     * @param rhs 差を取る相手のベクトル
     * @return 指定されたベクトルとの差
     */
    public Vector2i sub( final Vector2i rhs ) {
        return new Vector2i(
                this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1] );
    }

    /**
     * 指定されたスケールとの積を返します。
     * 
     * @param s スケール
     * @return 指定されたスケールとの積
     */
    public Vector2i mul( final int s ) {
        return new Vector2i( this.m_elements[0] * s, this.m_elements[1] * s );
    }

    /**
     * 指定されたスケールとの商を返します。
     * 
     * @param s スケール
     * @return 指定されたスケールとの商
     * @exception ArithmeticException s == 0
     */
    public Vector2i div( final int s ) {
        return new Vector2i( this.m_elements[0] / s, this.m_elements[1] / s );
    }
}
