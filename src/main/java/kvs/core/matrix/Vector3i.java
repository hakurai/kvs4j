package kvs.core.matrix;

/**
 * Vector3iクラスは成分表現にint型を利用する3次元ベクトルを表します。
 */

public final class Vector3i extends AbstractVectorInt {

    private static final long serialVersionUID = -110574917472917245L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector3i ZERO = new Vector3i(0, 0, 0);

    /**
     * 指定されたX,Y,Z成分を持つ3次元ベクトルを構築します。
     * 
     * @param x
     *            X,Y,Z成分
     */
    public Vector3i(final int x) {
        this( x, x, x );
    }

    /**
     * 指定されたX,Y,Z成分を持つ3次元ベクトルを構築します。
     * 
     * @param x
     *            X成分
     * @param y
     *            Y成分
     * @param z
     *            Z成分
     */
    public Vector3i(final int x, final int y, final int z) {
        super( 3 );
        this.m_elements[0] = x;
        this.m_elements[1] = y;
        this.m_elements[2] = z;
    }

    /**
     * 指定されたVector2iとzの値から3次元ベクトルを構築します。
     * 
     * @param other
     *            2次元ベクトル
     * @param z
     *            Z成分
     */
    public Vector3i(final Vector2i other, final int z) {
        this( other.getX(), other.getY(), z );
    }

    /**
     * 指定されたint配列からベクトルを構築します。
     * 
     * @param elements
     *            3つ以上の値を持つint配列
     * @exception IndexOutOfBoundsException
     *                elements.length < 3
     */
    public Vector3i(final int[] elements) {
        this( elements[0], elements[1], elements[2] );
    }
    
    /**
     * 指定されたint配列のオフセット位置からの値でベクトルを構築します。
     * 
     * @param elements 3つ以上の値を持つint配列
     * @param offset オフセット
     * @exception IndexOutOfBoundsException elements.length < offset + 3
     */
    public Vector3i( final int[] elements, final int offset ) {
        this( elements[offset], elements[offset + 1], elements[offset + 2] );
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
     * Z成分の値を返します。
     * 
     * @return Z成分の値
     */
    public int getZ() {
        return this.m_elements[2];
    }

    /**
     * 正規化されたベクトルを返します。
     * 
     * @exception ArithmeticException
     *                ベクトルの大きさが0の場合
     */
    public Vector3i normalize() {

        int normalize_factor = (int) (1.0 / this.length());
        return new Vector3i(this.m_elements[0] * normalize_factor,
                this.m_elements[1] * normalize_factor, this.m_elements[2]
                                                                       * normalize_factor);

    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param rhs
     *            内積を取る相手のベクトル
     * @return ベクトルの内積
     */
    public int dot(final Vector3i rhs) {
        int result = 0;

        result += this.m_elements[0] * rhs.get(0);
        result += this.m_elements[1] * rhs.get(1);
        result += this.m_elements[2] * rhs.get(2);

        return (result);

    }

    /**
     * 指定されたベクトルとの和を返します。
     * 
     * @param rhs
     *            和を取る相手のベクトル
     * @return 指定されたベクトルとの和
     */
    public Vector3i add(final Vector3i rhs) {
        return new Vector3i(this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1], this.m_elements[2]
                                                                        + rhs.m_elements[2]);
    }

    /**
     * 指定されたベクトルとの差を取ります。
     * 
     * @param rhs
     *            差を取る相手のベクトル
     * @return 指定されたベクトルとの差
     */
    public Vector3i sub(final Vector3i rhs) {
        return new Vector3i(this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1], this.m_elements[2]
                                                                        - rhs.m_elements[2]);
    }

    /**
     * 指定されたスケールとの積を取ります。
     * 
     * @param s
     *            スケール
     * @return 指定されたスケールとの積
     */
    public Vector3i mul(final int s) {
        return new Vector3i(this.m_elements[0] * s, this.m_elements[1] * s,
                this.m_elements[2] * s);
    }

    /**
     * 指定されたスケールとの商を取ります。
     * 
     * @param s
     *            スケール
     * @return 指定されたスケールとの商
     * @exception ArithmeticException
     *                s == 0
     */
    public Vector3i div(final int s) {
        return new Vector3i(this.m_elements[0] / s, this.m_elements[1] / s,
                this.m_elements[2] / s);
    }

    /**
     * 指定されたベクトルとの外積を返します。
     * 
     * @param other
     *            外積を取る相手のベクトル
     * @return ベクトルの外積
     */
    public Vector3i cross(final Vector3i other) {
        int nx = this.m_elements[1] * other.m_elements[2] - this.m_elements[2]
                                                                              * other.m_elements[1];
        int ny = this.m_elements[2] * other.m_elements[0] - this.m_elements[0]
                                                                              * other.m_elements[2];
        int nz = this.m_elements[0] * other.m_elements[1] - this.m_elements[1]
                                                                              * other.m_elements[0];
        return new Vector3i(nx, ny, nz);
    }
}
