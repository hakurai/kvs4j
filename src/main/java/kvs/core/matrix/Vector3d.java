package kvs.core.matrix;

/**
 * Vector3dクラスは成分表現にdouble型を利用する3次元ベクトルを表します。
 */

public final class Vector3d extends AbstractVectorDouble {

    private static final long serialVersionUID = 4534959609504654426L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector3d ZERO = new Vector3d(0, 0, 0);

    /**
     * 指定されたX,Y,Z成分を持つ3次元ベクトルを構築します。
     * 
     * @param x
     *            X,Y,Z成分
     */
    public Vector3d(final double x) {
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
    public Vector3d(final double x, final double y, final double z) {
        super(3);
        this.m_elements[0] = x;
        this.m_elements[1] = y;
        this.m_elements[2] = z;
    }

    /**
     * 指定されたVector2dとzの値から3次元ベクトルを構築します。
     * 
     * @param other
     *            2次元ベクトル
     * @param z
     *            Z成分
     */
    public Vector3d(final Vector2d other, final double z) {
        this(other.getX(), other.getY(), z);
    }

    /**
     * 指定されたdouble配列からベクトルを構築します。
     * 
     * @param elements
     *            3つ以上の値を持つdouble配列
     * @exception IndexOutOfBoundsException
     *                elements.length < 3
     */
    public Vector3d(final double[] elements) {
        this( elements[0], elements[1], elements[2]);
    }
    
    /**
     * 指定されたdouble配列のオフセット位置からの値でベクトルを構築します。
     * 
     * @param elements 3つ以上の値を持つdouble配列
     * @param offset オフセット
     * @exception IndexOutOfBoundsException elements.length < offset + 3
     */
    public Vector3d( final double[] elements, final int offset ) {
        this( elements[offset], elements[offset + 1], elements[offset + 2] );
    }

    /**
     * X成分の値を返します。
     * 
     * @return X成分の値
     */
    public double getX() {
        return this.m_elements[0];
    }

    /**
     * Y成分の値を返します。
     * 
     * @return Y成分の値
     */
    public double getY() {
        return this.m_elements[1];
    }

    /**
     * Z成分の値を返します。
     * 
     * @return Z成分の値
     */
    public double getZ() {
        return this.m_elements[2];
    }

    /**
     * 正規化されたベクトルを新しいVector3dで返します。
     * 
     * @return 正規化されたベクトル
     * @exception ArithmeticException
     *                ベクトルの大きさが0の場合
     */
    public Vector3d normalize() {

        double normalize_factor = 1.0 / this.length();
        return new Vector3d(this.m_elements[0] * normalize_factor,
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
    public double dot(final Vector3d rhs) {
        double result = 0d;

        result += this.m_elements[0] * rhs.get(0);
        result += this.m_elements[1] * rhs.get(1);
        result += this.m_elements[2] * rhs.get(2);

        return (result);

    }

    /**
     * 指定されたベクトルとの和を新しいVector3d返します。
     * 
     * @param rhs
     *            和を取る相手のベクトル
     * @return 指定されたベクトルとの和
     */
    public Vector3d add(final Vector3d rhs) {
        return new Vector3d(this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1], this.m_elements[2]
                                                                        + rhs.m_elements[2]);
    }

    /**
     * 指定されたベクトルとの差を新しいVector3dで返します。
     * 
     * @param rhs
     *            差を取る相手のベクトル
     * @return 指定されたベクトルとの差
     */
    public Vector3d sub(final Vector3d rhs) {
        return new Vector3d(this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1], this.m_elements[2]
                                                                        - rhs.m_elements[2]);
    }

    /**
     * 指定されたスケールとの積を新しいVector3dで返します。
     * 
     * @param s
     *            スケール
     * @return 指定されたスケールとの積
     */
    public Vector3d mul(final double s) {
        return new Vector3d(this.m_elements[0] * s, this.m_elements[1] * s,
                this.m_elements[2] * s);
    }

    /**
     * 指定されたスケールとの商を新しいVector3dで返します。
     * 
     * @param s
     *            スケール
     * @return 指定されたスケールとの商
     * @exception ArithmeticException
     *                s == 0
     */
    public Vector3d div(final double s) {
        return new Vector3d(this.m_elements[0] / s, this.m_elements[1] / s,
                this.m_elements[2] / s);
    }
    
    /**
     * 符号を反転したベクトルを返します。
     * 
     * @return -(this)
     */
    public Vector3d opposite(){
        return new Vector3d(
                -this.m_elements[0],
                -this.m_elements[1],
                -this.m_elements[2]);
    }

    /**
     * 指定されたベクトルとの外積を新しいVector3dで返します。
     * 
     * @param other
     *            外積を取る相手のベクトル
     * @return ベクトルの外積
     */
    public Vector3d cross(final Vector3d other) {
        double nx = this.m_elements[1] * other.m_elements[2] - this.m_elements[2]
                                                                              * other.m_elements[1];
        double ny = this.m_elements[2] * other.m_elements[0] - this.m_elements[0]
                                                                              * other.m_elements[2];
        double nz = this.m_elements[0] * other.m_elements[1] - this.m_elements[1]
                                                                              * other.m_elements[0];

        return new Vector3d(nx, ny, nz);
    }

}
