package kvs.core.matrix;

/**
 * Vector4dクラスは成分表現にdouble型を利用する4次元ベクトルを表します。
 */

public final class Vector4d extends AbstractVectorDouble {

    private static final long serialVersionUID = 5755338485037886356L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector4d ZERO = new Vector4d(0, 0, 0, 0);

    /**
     * 指定されたX,Y,Z,W成分を持つ4次元ベクトルを構築します。
     * 
     * @param x
     *            X,Y,Z,W成分
     */
    public Vector4d(final double x) {
        this( x, x, x, x );
    }

    /**
     * 指定されたX,Y,Z,W成分を持つ4次元ベクトルを構築します。
     * 
     * @param x
     *            X成分
     * @param y
     *            Y成分
     * @param z
     *            Z成分
     * @param w
     *            W成分
     */
    public Vector4d(final double x, final double y, final double z, final double w) {
        super(4);
        this.m_elements[0] = x;
        this.m_elements[1] = y;
        this.m_elements[2] = z;
        this.m_elements[3] = w;
    }

    /**
     * 指定されたVector2dとz,wの値から4次元ベクトルを構築します。
     * 
     * @param other
     *            2次元ベクトル
     * @param z
     *            Z成分
     * @param w
     *            W成分
     */
    public Vector4d(final Vector2d other, final double z, final double w) {
        this( other.getX(), other.getY(), z, w );
    }

    /**
     * 指定されたVector3dとwの値から4次元ベクトルを構築します。
     * 
     * @param other
     *            3次元ベクトル
     * @param w
     *            W成分
     */
    public Vector4d(final Vector3d other, final double w) {
        this( other.getX(), other.getY(), other.getZ(), w );
    }

    /**
     * 指定されたdouble配列から4次元ベクトルを構築します。
     * 
     * @param elements
     *            4つ以上の値を持つdouble配列
     * @exception IndexOutOfBoundsException
     *                elements.length < 4
     */
    public Vector4d(final double[] elements) {
        this( elements[0], elements[1], elements[2], elements[3] );
    }

    /**
     * X成分の値を返します。
     * 
     * @return X成分の値
     */
    public final double getX() {
        return this.m_elements[0];
    }

    /**
     * Y成分の値を返します。
     * 
     * @return Y成分の値
     */
    public final double getY() {
        return this.m_elements[1];
    }

    /**
     * Z成分の値を返します。
     * 
     * @return Z成分の値
     */
    public final double getZ() {
        return this.m_elements[2];
    }

    /**
     * W成分の値を返します。
     * 
     * @return W成分の値
     */
    public final double w() {
        return this.m_elements[3];
    }

    /**
     * 正規化したベクトルを新しいVector4dで返します。
     * 
     * @return 正規化されたベクトル
     * @exception ArithmeticException
     *                ベクトルの大きさが0の場合
     */
    public final Vector4d normalize() {

        double normalize_factor = 1.0 / this.length();
        return new Vector4d(this.m_elements[0] * normalize_factor,
                this.m_elements[1] * normalize_factor,
                this.m_elements[2] * normalize_factor,
                this.m_elements[3] * normalize_factor);
    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param rhs
     *            内積を取る相手のベクトル
     * @return ベクトルの内積
     */
    public final double dot(final Vector4d rhs) {
        double result = 0d;

        result += this.m_elements[0] * rhs.get(0);
        result += this.m_elements[1] * rhs.get(1);
        result += this.m_elements[2] * rhs.get(2);
        result += this.m_elements[3] * rhs.get(3);

        return (result);

    }

    /**
     * 指定されたベクトルとの和を新しいVector4dで返します。
     * 
     * @param rhs
     *            和を取る相手のベクトル
     * @return ベクトルの和
     */
    public final Vector4d add(final Vector4d rhs) {
        return new Vector4d(this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1],
                this.m_elements[2] + rhs.m_elements[2],
                this.m_elements[3] + rhs.m_elements[3]);
    }

    /**
     * 指定されたベクトルとの差を新しいVector4dで返します。
     * 
     * @param rhs
     *            差を取る相手のベクトル
     * @return ベクトルの差
     */
    public final Vector4d sub(final Vector4d rhs) {
        return new Vector4d(this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1],
                this.m_elements[2] - rhs.m_elements[2],
                this.m_elements[3] - rhs.m_elements[3]);
    }

    /**
     * 指定されたスケールとの積を新しいVector4dで返します。
     * 
     * @param s
     *            スケール
     * @return (this * s)
     */
    public final Vector4d mul(final double s) {
        return new Vector4d(this.m_elements[0] * s,
                this.m_elements[1] * s,
                this.m_elements[2] * s,
                this.m_elements[3] * s);
    }

    /**
     * 指定されたスケールとの商を新しいVector4dで返します。
     * 
     * @param s
     *            スケール
     * @return (this / s)
     * @exception ArithmeticException
     */
    public final Vector4d div(final double s) {
        return new Vector4d(this.m_elements[0] / s,
                this.m_elements[1] / s,
                this.m_elements[2] / s,
                this.m_elements[3] / s);
    }
    
    /**
     * 符号を反転したベクトルを返します。
     * 
     * @return -(this)
     */
    public Vector4d opposite(){
        return new Vector4d(
                -this.m_elements[0],
                -this.m_elements[1],
                -this.m_elements[2],
                -this.m_elements[3]);
    }
}
