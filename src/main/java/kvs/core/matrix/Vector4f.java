package kvs.core.matrix;

/**
 * Vector4fクラスは成分表現にfloat型を利用する4次元ベクトルを表します。
 */

public final class Vector4f extends AbstractVectorFloat {

    private static final long serialVersionUID = 8927328012906747847L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector4f ZERO = new Vector4f( 0.0f, 0.0f, 0.0f, 0.0f );

    /**
     * 指定されたX,Y,Z,W成分を持つ4次元ベクトルを構築します。
     * 
     * @param x X,Y,Z,W成分
     */
    public Vector4f( final float x ) {
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
    public Vector4f( final float x, final float y, final float z, final float w ) {
        super( 4 );
        this.m_elements[0] = x;
        this.m_elements[1] = y;
        this.m_elements[2] = z;
        this.m_elements[3] = w;
    }

    /**
     * 指定されたVector2fとz,wの値から4次元ベクトルを構築します。
     * 
     * @param other 2次元ベクトル
     * @param z Z成分
     * @param w W成分
     */
    public Vector4f( final Vector2f other, final float z, final float w ) {
        this( other.getX(), other.getY(), z, w );
    }

    /**
     * 指定されたVector3fとwの値から4次元ベクトルを構築します。
     * 
     * @param other 3次元ベクトル
     * @param w W成分
     */
    public Vector4f( final Vector3f other, final float w ) {
        this( other.getX(), other.getY(), other.getZ(), w );
    }

    /**
     * 指定されたfloat配列から4次元ベクトルを構築します。
     * 
     * @param elements 4つ以上の値を持つfloat配列
     * @exception IndexOutOfBoundsException elements.length < 4
     */
    public Vector4f( final float[] elements ) {
        this( elements[0], elements[1], elements[2], elements[3] );
    }

    /**
     * X成分の値を返します。
     * 
     * @return X成分の値
     */
    public float x() {
        return this.m_elements[0];
    }

    /**
     * Y成分の値を返します。
     * 
     * @return Y成分の値
     */
    public float y() {
        return this.m_elements[1];
    }

    /**
     * Z成分の値を返します。
     * 
     * @return Z成分の値
     */
    public float z() {
        return this.m_elements[2];
    }

    /**
     * W成分の値を返します。
     * 
     * @return W成分の値
     */
    public float w() {
        return this.m_elements[3];
    }
    
    /**
     * 正規化したベクトルを新しいVector4fで返します。
     * 
     * @exception ArithmeticException ベクトルの大きさが0の場合
     */
    public Vector4f normalize() {

        float normalize_factor = (float) (1.0 / this.length());
        return new Vector4f(
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
    public float dot( final Vector4f rhs ) {
        float result = 0.0f;

        result += this.m_elements[0] * rhs.get( 0 );
        result += this.m_elements[1] * rhs.get( 1 );
        result += this.m_elements[2] * rhs.get( 2 );
        result += this.m_elements[3] * rhs.get( 3 );

        return (result);

    }

    /**
     * 指定されたベクトルとの和を新しいVector4fで返します。
     * 
     * @param rhs 和を取る相手のベクトル
     * @return 指定されたベクトルとの和
     */
    public Vector4f add( final Vector4f rhs ) {
        return new Vector4f(
                this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1],
                this.m_elements[2] + rhs.m_elements[2],
                this.m_elements[3] + rhs.m_elements[3] );
    }

    /**
     * 指定されたベクトルとの差を新しいVector4fで返します。
     * 
     * @param rhs 差を取る相手のベクトル
     * @return ベクトルの差
     */
    public Vector4f sub( final Vector4f rhs ) {
        return new Vector4f(
                this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1],
                this.m_elements[2] - rhs.m_elements[2],
                this.m_elements[3] - rhs.m_elements[3] );
    }
    
    /**
     * 指定された行列との積を新しいVector3fで返します。
     * 
     * @param rhs 行列
     * @return (this * rhs)
     */
    public Vector4f mul( final Matrix44f rhs ){
        return new Vector4f(
                this.m_elements[0] * rhs.get(0, 0) + this.m_elements[1] * rhs.get(1, 0) + this.m_elements[2] * rhs.get(2, 0) + this.m_elements[3] * rhs.get(3, 0),
                this.m_elements[0] * rhs.get(0, 1) + this.m_elements[1] * rhs.get(1, 1) + this.m_elements[2] * rhs.get(2, 1) + this.m_elements[3] * rhs.get(3, 1),
                this.m_elements[0] * rhs.get(0, 2) + this.m_elements[1] * rhs.get(1, 2) + this.m_elements[2] * rhs.get(2, 2) + this.m_elements[3] * rhs.get(3, 2),
                this.m_elements[0] * rhs.get(0, 3) + this.m_elements[1] * rhs.get(1, 3) + this.m_elements[2] * rhs.get(2, 3) + this.m_elements[3] * rhs.get(3, 3) );
    }

    /**
     * 指定されたスケールとの積を新しいVector4fで返します。
     * 
     * @param s スケール
     * @return (this * s)
     */
    public Vector4f mul( final float s ) {
        return new Vector4f(
                this.m_elements[0] * s,
                this.m_elements[1] * s,
                this.m_elements[2] * s,
                this.m_elements[3] * s );
    }

    /**
     * 指定されたスケールとの商を新しいVector4fで返します。
     * 
     * @param s スケール
     * @return (this / s)
     * @exception ArithmeticException
     */
    public Vector4f div( final float s ) {
        return new Vector4f(
                this.m_elements[0] / s,
                this.m_elements[1] / s,
                this.m_elements[2] / s,
                this.m_elements[3] / s );
    }
    
    /**
     * 符号を反転した新しいVector4fで返します。
     * 
     * @return -(this)
     */
    public Vector4f opposite(){
        return new Vector4f(
                -this.m_elements[0],
                -this.m_elements[1],
                -this.m_elements[2],
                -this.m_elements[3]);
    }

}
