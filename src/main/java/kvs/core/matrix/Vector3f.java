package kvs.core.matrix;

/**
 * Vector3fクラスは成分表現にfloat型を利用する3次元ベクトルを表します。
 */

public final class Vector3f extends AbstractVectorFloat {

    private static final long serialVersionUID = -4918321887340232182L;
    /**
     * ゼロベクトルを表します。
     */
    public static final Vector3f ZERO = new Vector3f( 0, 0, 0 );

    /**
     * 指定されたX,Y,Z成分を持つ3次元ベクトルを構築します。
     * 
     * @param x X,Y,Z成分
     */
    public Vector3f( final float x ) {
        this( x, x, x );
    }

    /**
     * 指定されたX,Y,Z成分を持つ3次元ベクトルを構築します。
     * 
     * @param x X成分
     * @param y Y成分
     * @param z Z成分
     */
    public Vector3f( final float x, final float y, final float z ) {
        super( 3 );
        this.m_elements[0] = x;
        this.m_elements[1] = y;
        this.m_elements[2] = z;
    }

    /**
     * 指定されたVector2fとzの値から3次元ベクトルを構築します。
     * 
     * @param other 2次元ベクトル
     * @param z Z成分
     */
    public Vector3f( final Vector2f other, final float z ) {
        this( other.getX(), other.getY(), z );
    }

    /**
     * 指定されたfloat配列からベクトルを構築します。
     * 
     * @param elements 3つ以上の値を持つfloat配列
     * @exception IndexOutOfBoundsException elements.length < 3
     */
    public Vector3f( final float[] elements ) {
        this( elements[0], elements[1], elements[2] );
    }

    /**
     * 指定されたfloat配列のオフセット位置からの値でベクトルを構築します。
     * 
     * @param elements 3つ以上の値を持つfloat配列
     * @param offset オフセット
     * @exception IndexOutOfBoundsException elements.length < offset + 3
     */
    public Vector3f( final float[] elements, final int offset ) {
        this( elements[offset], elements[offset + 1], elements[offset + 2] );
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
     * Z成分の値を返します。
     * 
     * @return Z成分の値
     */
    public float getZ() {
        return this.m_elements[2];
    }

    /**
     * 正規化された新しいVector3fで返します。
     * 
     * @exception ArithmeticException ベクトルの大きさが0の場合
     */
    public Vector3f normalize() {

        float normalize_factor = (float) (1.0 / this.length());
        return new Vector3f(
                this.m_elements[0] * normalize_factor,
                this.m_elements[1] * normalize_factor,
                this.m_elements[2] * normalize_factor );

    }

    /**
     * 指定されたベクトルとの内積を返します。
     * 
     * @param rhs 内積を取る相手のベクトル
     * @return ベクトルの内積
     */
    public float dot( final Vector3f rhs ) {
        float result = 0f;

        result += this.m_elements[0] * rhs.get( 0 );
        result += this.m_elements[1] * rhs.get( 1 );
        result += this.m_elements[2] * rhs.get( 2 );

        return (result);

    }

    /**
     * 指定されたベクトルとの和を新しいVector3fで返します。
     * 
     * @param rhs 和を取る相手のベクトル
     * @return 指定されたベクトルとの和
     */
    public Vector3f add( final Vector3f rhs ) {
        return new Vector3f(
                this.m_elements[0] + rhs.m_elements[0],
                this.m_elements[1] + rhs.m_elements[1],
                this.m_elements[2] + rhs.m_elements[2] );
    }

    /**
     * 指定されたベクトルとの差を新しいVector3fで返します。
     * 
     * @param rhs 差を取る相手のベクトル
     * @return 指定されたベクトルとの差
     */
    public Vector3f sub( final Vector3f rhs ) {
        return new Vector3f(
                this.m_elements[0] - rhs.m_elements[0],
                this.m_elements[1] - rhs.m_elements[1],
                this.m_elements[2] - rhs.m_elements[2] );
    }

    /**
     * 指定された行列との積を新しいVector3fで返します。
     * 
     * @param rhs 行列
     * @return (this * rhs)
     */
    public Vector3f mul( final Matrix33f rhs ) {
        return new Vector3f(
                this.m_elements[0] * rhs.get( 0, 0 ) + this.m_elements[1] * rhs.get( 1, 0 ) + this.m_elements[2] * rhs.get( 2, 0 ),
                this.m_elements[0] * rhs.get( 0, 1 ) + this.m_elements[1] * rhs.get( 1, 1 ) + this.m_elements[2] * rhs.get( 2, 1 ),
                this.m_elements[0] * rhs.get( 0, 2 ) + this.m_elements[1] * rhs.get( 1, 2 ) + this.m_elements[2] * rhs.get( 2, 2 ) );
    }

    /**
     * 指定されたスケールとの積を新しいVector3fで返します。
     * 
     * @param s スケール
     * @return 指定されたスケールとの積
     */
    public Vector3f mul( final float s ) {
        return new Vector3f( this.m_elements[0] * s, this.m_elements[1] * s, this.m_elements[2] * s );
    }

    /**
     * 指定されたスケールとの商を新しいVector3fで返します。
     * 
     * @param s スケール
     * @return 指定されたスケールとの商
     * @exception ArithmeticException s == 0
     */
    public Vector3f div( final float s ) {
        return new Vector3f( this.m_elements[0] / s, this.m_elements[1] / s, this.m_elements[2] / s );
    }

    /**
     * 符号を反転したベクトルを新しいVector3fで返します。
     * 
     * @return -(this)
     */
    public Vector3f opposite() {
        return new Vector3f( -this.m_elements[0], -this.m_elements[1], -this.m_elements[2] );
    }

    /**
     * 指定されたベクトルとの外積を新しいVector3fで返します。
     * 
     * @param other 外積を取る相手のベクトル
     * @return ベクトルの外積
     */
    public Vector3f cross( final Vector3f other ) {
        float nx = this.m_elements[1] * other.m_elements[2] - this.m_elements[2] * other.m_elements[1];
        float ny = this.m_elements[2] * other.m_elements[0] - this.m_elements[0] * other.m_elements[2];
        float nz = this.m_elements[0] * other.m_elements[1] - this.m_elements[1] * other.m_elements[0];
        return new Vector3f( nx, ny, nz );
    }
}
