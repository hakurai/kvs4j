package kvs.core.matrix;

/**
 * Vector2fクラスは成分表現にfloat型を利用する2次元ベクトルを表します。
 */

public final class Vector2f extends AbstractVectorFloat {

    private static final long serialVersionUID = -3975175260317132591L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector2f ZERO = new Vector2f(0, 0);

    /**
     * 指定されたX,Y成分を持つベクトルを構築します。
     * 
     * @param x
     *            X,Y成分
     */
    public Vector2f(final float x) {
        this( x, x );
    }

    /**
     * 指定されたX,Y成分を持つベクトルを構築します。
     * 
     * @param x
     *            X成分
     * @param y
     *            Y成分
     */
    public Vector2f(final float x, final float y) {
        super( 2 );
        this.m_elements[0] = x;
        this.m_elements[1] = y;
    }

    /**
     * 指定されたfloat配列からベクトルを構築します。
     * 
     * @param elements
     *            2つ以上の値を持つfloat配列
     * @exception IndexOutOfBoundsException
     *                elements.length < 2
     */
    public Vector2f(final float[] elements) {
        this( elements[0], elements[1] );

    }

    /**
     * X成分の値を返します。
     * 
     * @return X成分の値
     */
    public float getX() {
        return this.m_elements[0];
    }

    /**
     * Y成分の値を返します。
     * 
     * @return Y成分の値
     */
    public float getY() {
        return this.m_elements[1];
    }

    /**
     * 正規化されたベクトルを返します。
     * 
     * @exception ArithmeticException
     *                ベクトルの大きさが0の場合
     */
    public Vector2f normalize() {

        float normalize_factor = (float) (1.0 / this.length());

        return new Vector2f(this.m_elements[0] * normalize_factor,
                this.m_elements[1] * normalize_factor);

    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param rhs
     *            内積を取る相手のベクトル
     * @return ベクトルの内積
     */
    public float dot(final Vector2f rhs) {
        float result = 0f;

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
    public Vector2f add(final Vector2f rhs) {
        return new Vector2f(this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1]);
    }

    /**
     * 指定されたベクトルとの差を返します。
     * 
     * @param rhs
     *            差を取る相手のベクトル
     * @return 指定されたベクトルとの差
     */
    public Vector2f sub(final Vector2f rhs) {
        return new Vector2f(this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1]);
    }

    /**
     * 指定されたスケールとの積を返します。
     * 
     * @param s
     *            スケール
     * @return 指定されたスケールとの積
     */
    public Vector2f mul(final float s) {
        return new Vector2f(this.m_elements[0] * s, this.m_elements[1] * s);
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
    public Vector2f div(final float s) {
        return new Vector2f(this.m_elements[0] / s, this.m_elements[1] / s);
    }
    
    /**
     * 符号を反転したベクトルを返します。
     * 
     * @return -(this)
     */
    public Vector2f opposite(){
        return new Vector2f(
                -this.m_elements[0],
                -this.m_elements[1]);
    }
}
