package kvs.core.matrix;

/**
 * Vector2dクラスは成分表現にdouble型を利用する2次元ベクトルを表します。
 */

public final class Vector2d extends AbstractVectorDouble {

    private static final long serialVersionUID = -4067681743125322596L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector2d ZERO = new Vector2d(0.0, 0.0);

    /**
     * 指定されたX,Y成分を持つベクトルを構築します。
     * 
     * @param x
     *            X,Y成分
     */
    public Vector2d(final double x) {
        this(x, x);

    }

    /**
     * 指定されたX,Y成分を持つベクトルを構築します。
     * 
     * @param x
     *            X成分
     * @param y
     *            Y成分
     */
    public Vector2d(final double x, final double y) {
        super( 2 );
        this.m_elements[0] = x;
        this.m_elements[1] = y;
    }

    /**
     * 指定されたdouble配列からベクトルを構築します。
     * 
     * @param elements
     *            2つ以上の値を持つdouble配列
     * @exception IndexOutOfBoundsException
     *                elements.length < 2
     */
    public Vector2d(final double[] elements) {
        this( elements[0], elements[1] );

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
     * 正規化されたベクトルを返します。
     * 
     * @return 正規化されたベクトル
     * 
     * @exception ArithmeticException
     *                ベクトルの大きさが0の場合
     */
    public Vector2d normalize() {

        double normalize_factor = 1.0 / this.length();

        return new Vector2d(this.m_elements[0] * normalize_factor,
                this.m_elements[1] * normalize_factor);
    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param rhs
     *            内積を取る相手のベクトル
     * @return ベクトルの内積
     */
    public double dot(final Vector2d rhs) {
        double result = 0d;

        result += this.m_elements[0] * rhs.get(0);
        result += this.m_elements[1] * rhs.get(1);

        return (result);

    }

    /**
     * 指定されたベクトルとの和を返します。
     * 
     * @param rhs
     *            和を取る相手のベクトル
     * @return 指定されたベクトルとの和
     */
    public Vector2d add(final Vector2d rhs) {
        return new Vector2d(this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1]);
    }

    /**
     * 指定されたベクトルとの差を返します。
     * 
     * @param rhs
     *            差を取る相手のベクトル
     * @return 指定されたベクトルとの差
     */
    public Vector2d sub(final Vector2d rhs) {
        return new Vector2d(this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1]);
    }

    /**
     * 指定されたスケールとの積を返します。
     * 
     * @param s
     *            スケール
     * @return 指定されたスケールとの積
     */
    public Vector2d mul(final double s) {
        return new Vector2d(this.m_elements[0] * s, this.m_elements[1] * s);
    }

    /**
     * 指定されたスケールとの商を返します。
     * 
     * @param s
     *            スケール
     * @return 指定されたスケールとの商
     * @exception ArithmeticException
     *                s == 0
     */
    public Vector2d div(final double s) {
        return new Vector2d(this.m_elements[0] / s, this.m_elements[1] / s);
    }
    
    /**
     * 符号を反転したベクトルを返します。
     * 
     * @return -(this)
     */
    public Vector2d opposite(){
        return new Vector2d(
                -this.m_elements[0],
                -this.m_elements[1]);
    }

}
