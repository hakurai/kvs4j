package kvs.core.matrix;

import java.io.Serializable;

/**
 * Matrix33fクラスは成分表現にfloat型を利用する3*3行列ベクトルを表します。
 */

public final class Matrix33f implements Serializable{
    
    private static final long serialVersionUID = -2014913149170991067L;

    public static final Matrix33f ZERO = new Matrix33f(0.0f);
    
    public static final Matrix33f IDENTITY = new Matrix33f(1.0f, 0.0f, 0.0f,
                                                           0.0f, 1.0f, 0.0f,
                                                           0.0f, 0.0f, 1.0f);

    private Vector3f[] m_rows = new Vector3f[3];

    /**
     * すべての成分が指定された値の行列を構築します。
     */
    public Matrix33f( final float a ) {
        this(a, a, a, a, a, a, a, a, a);
    }

    /**
     * 指定された値の行列を構築します。
     */
    public Matrix33f( float a00, float a01, float a02, float a10, float a11, float a12, float a20, float a21, float a22 ) {
        this.m_rows[0] = new Vector3f( a00, a01, a02 );
        this.m_rows[1] = new Vector3f( a10, a11, a12 );
        this.m_rows[2] = new Vector3f( a20, a21, a22 );
    }

    /**
     * 指定されたベクトルを各列として行列を構築します。
     */
    public Matrix33f( final Vector3f v0, final Vector3f v1, final Vector3f v2 ) {
        this.m_rows[0] = v0;
        this.m_rows[1] = v1;
        this.m_rows[2] = v2;
    }

    /**
     * 指定されたfloat配列から行列を構築します。
     * 
     * @param elements 9つ以上の値を持つfloat配列
     *@exception IndexOutOfBoundsException elements.length < 9
     */
    public Matrix33f( final float[] elements ) {
        this.m_rows[0] = new Vector3f( elements[0], elements[1], elements[2] );
        this.m_rows[1] = new Vector3f( elements[3], elements[4], elements[5] );
        this.m_rows[2] = new Vector3f( elements[6], elements[7], elements[8] );
    }

    /**
     * 指定された列を返します。
     * 
     * @param a 取得する列の値
     * @return 指定された列
     */
    public Vector3f getRow( final int a ) {
        return this.m_rows[a];
    }

    /**
     * すべての成分を配列で返します。
     * 
     * @return すべての成分
     */
    public float[] get() {
        float[] elements = new float[9];
        for(int r = 0;r < 3;++r){
            for(int c = 0;c < 3;++c){
                elements[3*r + c] = this.get(r, c);
            }
        }
        return elements;
    }

    /**
     * 指定された位置の値を返します。
     * 
     * @param row 列
     * @param col 行
     */
    public float get( final int row, final int col ) {
        return this.m_rows[row].get( col );
    }

    /**
     * 
     * この行列のすべての成分をゼロに設定します。
     *
     */
    public void zero(){
        this.m_rows[0]
                    = this.m_rows[1]
                                  = this.m_rows[2]
                                                = Vector3f.ZERO;
    }

    /**
     * 転置行列を返します。
     */
    public Matrix33f transpose() {

        return new Matrix33f(
                get( 0, 0 ),
                get( 1, 0 ),
                get( 2, 0 ),
                get( 0, 1 ),
                get( 1, 1 ),
                get( 2, 1 ),
                get( 0, 2 ),
                get( 1, 2 ),
                get( 2, 2 ) );
    }

    /**
     * 逆行列を求めます。
     */
    public Matrix33f inverse() {
        final float[] det22 = { this.get( 1, 1 ) * this.get( 2, 2 ) - this.get( 1, 2 ) * this.get( 2, 1 ), this.get( 1, 0 ) * this.get( 2, 2 ) - this.get( 1, 2 ) * this.get( 2, 0 ), this.get( 1, 0 ) * this.get( 2, 1 ) - this.get( 1, 1 ) * this.get( 2, 0 ), this.get( 0, 1 ) * this.get( 2, 2 ) - this.get( 0, 2 ) * this.get( 2, 1 ), this.get( 0, 0 ) * this.get( 2, 2 ) - this.get( 0, 2 ) * this.get( 2, 0 ), this.get( 0, 0 ) * this.get( 2, 1 ) - this.get( 0, 1 ) * this.get( 2, 0 ), this.get( 0, 1 ) * this.get( 1, 2 ) - this.get( 0, 2 ) * this.get( 1, 1 ), this.get( 0, 0 ) * this.get( 1, 2 ) - this.get( 0, 2 ) * this.get( 1, 0 ), this.get( 0, 0 ) * this.get( 1, 1 ) - this.get( 0, 1 ) * this.get( 1, 0 ) };

        final float det33 = this.get( 0, 0 ) * det22[0] - this.get( 0, 1 ) * det22[1] + this.get( 0, 2 ) * det22[2];

        final float det_inverse = (1.0f / det33);

        for (int i = 0; i < det22.length; ++i) {
            det22[i] *= det_inverse;
        }

        return new Matrix33f(
                +det22[0],
                -det22[3],
                +det22[6],
                -det22[1],
                +det22[4],
                -det22[7],
                +det22[2],
                -det22[5],
                +det22[8] );
    }

    /**
     * 標準出力にこのオブジェクトの文字列表現を出力します。
     */
    public void print() {
        System.out.println( this.toString() );
    }

    /**
     * 対角成分の和を求めます。
     * 
     * @return 対角成分の和
     */
    public float trace() {
        return (this.m_rows[0].get( 0 ) + this.m_rows[1].get( 1 ) + this.m_rows[2].get( 2 ));
    }

    /**
     * 行列式を求めます。
     * 
     * @return 行列式の値
     */
    public float determinant() {
        final float det22[] = { this.get( 1, 1 ) * this.get( 2, 2 ) - this.get( 1, 2 ) * this.get( 2, 1 ), this.get( 1, 0 ) * this.get( 2, 2 ) - this.get( 1, 2 ) * this.get( 2, 0 ), this.get( 1, 0 ) * this.get( 2, 1 ) - this.get( 1, 1 ) * this.get( 2, 0 ) };

        final float det33 = this.get( 0, 0 ) * det22[0] - this.get( 0, 1 ) * det22[1] + this.get( 0, 2 ) * det22[2];

        return (det33);
    }

    /**
     * 指定された行列との和を返します。
     * 
     * @param rhs 和を取る相手の行列
     * @return (this + rhs)
     */
    public Matrix33f add( final Matrix33f rhs ) {
        return new Matrix33f(
                this.m_rows[0].add( rhs.m_rows[0] ),
                this.m_rows[1].add( rhs.m_rows[1] ),
                this.m_rows[2].add( rhs.m_rows[2] ) );
    }

    /**
     * 指定された行列との差を返します。
     * 
     * @param rhs 差を取る相手の行列
     * @return (this - rhs)
     */
    public Matrix33f sub( final Matrix33f rhs ) {
        return new Matrix33f(
                this.m_rows[0].sub( rhs.m_rows[0] ),
                this.m_rows[1].sub( rhs.m_rows[1] ),
                this.m_rows[2].sub( rhs.m_rows[2] ) );
    }

    /**
     * 指定された行列との積を返します。
     * 
     * @param rhs 積を取る相手の行列
     * @return (this * s)
     */
    public Matrix33f mul( final Matrix33f rhs ) {
        return new Matrix33f(
                this.get( 0, 0 ) * rhs.get( 0, 0 ) + this.get( 0, 1 ) * rhs.get( 1, 0 ) + this.get( 0, 2 ) * rhs.get( 2, 0 ),
                this.get( 0, 0 ) * rhs.get( 0, 1 ) + this.get( 0, 1 ) * rhs.get( 1, 1 ) + this.get( 0, 2 ) * rhs.get( 2, 1 ),
                this.get( 0, 0 ) * rhs.get( 0, 2 ) + this.get( 0, 1 ) * rhs.get( 1, 2 ) + this.get( 0, 2 ) * rhs.get( 2, 2 ),
                this.get( 1, 0 ) * rhs.get( 0, 0 ) + this.get( 1, 1 ) * rhs.get( 1, 0 ) + this.get( 1, 2 ) * rhs.get( 2, 0 ),
                this.get( 1, 0 ) * rhs.get( 0, 1 ) + this.get( 1, 1 ) * rhs.get( 1, 1 ) + this.get( 1, 2 ) * rhs.get( 2, 1 ),
                this.get( 1, 0 ) * rhs.get( 0, 2 ) + this.get( 1, 1 ) * rhs.get( 1, 2 ) + this.get( 1, 2 ) * rhs.get( 2, 2 ),
                this.get( 2, 0 ) * rhs.get( 0, 0 ) + this.get( 2, 1 ) * rhs.get( 1, 0 ) + this.get( 2, 2 ) * rhs.get( 2, 0 ),
                this.get( 2, 0 ) * rhs.get( 0, 1 ) + this.get( 2, 1 ) * rhs.get( 1, 1 ) + this.get( 2, 2 ) * rhs.get( 2, 1 ),
                this.get( 2, 0 ) * rhs.get( 0, 2 ) + this.get( 2, 1 ) * rhs.get( 1, 2 ) + this.get( 2, 2 ) * rhs.get( 2, 2 ) );
    }
    
    /**
     * 指定されたベクトルとの積を新しいVector4fで返します。
     * 
     * @param rhs 積を取る相手のベクトル
     * @return (this * rhs)
     */
    public Vector3f mul( final Vector3f rhs ) {
        return new Vector3f(m_rows[0].dot( rhs ),
                m_rows[1].dot( rhs ),
                m_rows[2].dot( rhs ) );
    }

    /**
     * 指定された値でスカラー倍した行列を返します。
     * 
     * @param rhs スカラー
     * @return (this * rhs)
     */
    public Matrix33f mul( final float rhs ) {
        return new Matrix33f(
                this.m_rows[0].mul( rhs ),
                this.m_rows[1].mul( rhs ),
                this.m_rows[2].mul( rhs ) );
    }

    /**
     * 指定された値でスカラー倍します。
     * 
     * @param rhs スカラー
     * @return (this / rhs)
     * @exception ArithmeticException rhs == 0
     */
    public Matrix33f div( final float rhs ) {
        return new Matrix33f(
                this.m_rows[0].div( rhs ),
                this.m_rows[1].div( rhs ),
                this.m_rows[2].div( rhs ) );
    }

    /**
     * このベクトルと指定されたオブジェクトを比較します。
     * このオブジェクトと同じベクトルを表すVector4dオブジェクトである場合にだけtrueを返します。
     * 
     * @param obj このベクトルと比較されるオブジェクト
     * @return ベクトルが等しい場合はtrue、そうでない場合はfalse
     */
    @Override
    public boolean equals( final Object obj ) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Matrix33f)) {
            return false;
        }
        Matrix33f other = (Matrix33f) obj;
        return (this.getRow( 0 ).equals( other.getRow( 0 ) ) && this.getRow( 1 ).equals( other.getRow( 1 ) ) && this.getRow( 2 ).equals( other.getRow( 2 ) ));
    }

    /**
     * オブジェクトのハッシュコード値を返します。
     * 
     * @return このベクトルのハッシュコード値
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.getRow( 0 ).hashCode();
        result = 31 * result + this.getRow( 1 ).hashCode();
        result = 31 * result + this.getRow( 2 ).hashCode();
        return result;
    }

    /**
     * このオブジェクトの文字列表現を返します。 this.get( 0 ).toString() + "\n" + this.get( 1
     * ).toString() + "\n" + this.get( 2 ).toString();
     * 
     * @return このオブジェクトの文字列表現
     */
    @Override
    public String toString() {
        String crlf = System.getProperty( "line.separator" );
        return this.getRow( 0 ).toString() + crlf + this.getRow( 1 ).toString() + crlf + this.getRow( 2 ).toString();
    }

}
