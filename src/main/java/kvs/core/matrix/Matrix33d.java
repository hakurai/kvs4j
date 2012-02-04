package kvs.core.matrix;

import java.io.Serializable;

/**
 * Matrix33dクラスは成分表現にdouble型を利用する3*3行列ベクトルを表します。
 */

public final class Matrix33d implements Serializable{
    
    private static final long serialVersionUID = 8751871465498543479L;

    public static final Matrix33d ZERO = new Matrix33d(0.0d);
    
    public static final Matrix33d IDENTITY = new Matrix33d(1.0d, 0.0d, 0.0d,
                                                           0.0d, 1.0d, 0.0d,
                                                           0.0d, 0.0d, 1.0d);

    private Vector3d[] m_rows = new Vector3d[3];

    /**
     * すべての成分が指定された値の行列を構築します。
     */
    public Matrix33d( final double a ) {
        this(a, a, a, a, a, a, a, a, a);
    }

    /**
     * 指定された値の行列を構築します。
     */
    public Matrix33d( double a00, double a01, double a02, double a10, double a11, double a12, double a20, double a21, double a22 ) {
        this.m_rows[0] = new Vector3d( a00, a01, a02 );
        this.m_rows[1] = new Vector3d( a10, a11, a12 );
        this.m_rows[2] = new Vector3d( a20, a21, a22 );
    }

    /**
     * 指定されたベクトルを各列として行列を構築します。
     */
    public Matrix33d( final Vector3d v0, final Vector3d v1, final Vector3d v2 ) {
        this.m_rows[0] = v0;
        this.m_rows[1] = v1;
        this.m_rows[2] = v2;
    }

    /**
     * 指定されたdouble配列から行列を構築します。
     * 
     * @param elements 9つ以上の値を持つdouble配列
     *@exception IndexOutOfBoundsException elements.length < 9
     */
    public Matrix33d( final double[] elements ) {
        this.m_rows[0] = new Vector3d( elements[0], elements[1], elements[2] );
        this.m_rows[1] = new Vector3d( elements[3], elements[4], elements[5] );
        this.m_rows[2] = new Vector3d( elements[6], elements[7], elements[8] );
    }

    /**
     * 指定された列を返します。
     * 
     * @param a 取得する列の値
     * @return 指定された列
     */
    public Vector3d getRow( final int a ) {
        return this.m_rows[a];
    }

    /**
     * すべての成分を配列で返します。
     * 
     * @return すべての成分
     */
    public double[] get() {
        double[] elements = new double[9];
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
    public double get( final int row, final int col ) {
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
                                                = Vector3d.ZERO;
    }

    /**
     * 転置行列を返します。
     */
    public Matrix33d transpose() {

        return new Matrix33d(
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
    public Matrix33d inverse() {
        final double[] det22 = { this.get( 1, 1 ) * this.get( 2, 2 ) - this.get( 1, 2 ) * this.get( 2, 1 ), this.get( 1, 0 ) * this.get( 2, 2 ) - this.get( 1, 2 ) * this.get( 2, 0 ), this.get( 1, 0 ) * this.get( 2, 1 ) - this.get( 1, 1 ) * this.get( 2, 0 ), this.get( 0, 1 ) * this.get( 2, 2 ) - this.get( 0, 2 ) * this.get( 2, 1 ), this.get( 0, 0 ) * this.get( 2, 2 ) - this.get( 0, 2 ) * this.get( 2, 0 ), this.get( 0, 0 ) * this.get( 2, 1 ) - this.get( 0, 1 ) * this.get( 2, 0 ), this.get( 0, 1 ) * this.get( 1, 2 ) - this.get( 0, 2 ) * this.get( 1, 1 ), this.get( 0, 0 ) * this.get( 1, 2 ) - this.get( 0, 2 ) * this.get( 1, 0 ), this.get( 0, 0 ) * this.get( 1, 1 ) - this.get( 0, 1 ) * this.get( 1, 0 ) };

        final double det33 = this.get( 0, 0 ) * det22[0] - this.get( 0, 1 ) * det22[1] + this.get( 0, 2 ) * det22[2];

        final double det_inverse = (1.0d / det33);

        for (int i = 0; i < det22.length; ++i) {
            det22[i] *= det_inverse;
        }

        return new Matrix33d(
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
    public double trace() {
        return (this.m_rows[0].get( 0 ) + this.m_rows[1].get( 1 ) + this.m_rows[2].get( 2 ));
    }

    /**
     * 行列式を求めます。
     * 
     * @return 行列式の値
     */
    public double determinant() {
        final double det22[] = { this.get( 1, 1 ) * this.get( 2, 2 ) - this.get( 1, 2 ) * this.get( 2, 1 ), this.get( 1, 0 ) * this.get( 2, 2 ) - this.get( 1, 2 ) * this.get( 2, 0 ), this.get( 1, 0 ) * this.get( 2, 1 ) - this.get( 1, 1 ) * this.get( 2, 0 ) };

        final double det33 = this.get( 0, 0 ) * det22[0] - this.get( 0, 1 ) * det22[1] + this.get( 0, 2 ) * det22[2];

        return (det33);
    }

    /**
     * 指定された行列との和を返します。
     * 
     * @param rhs 和を取る相手の行列
     * @return (this + rhs)
     */
    public Matrix33d add( final Matrix33d rhs ) {
        return new Matrix33d(
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
    public Matrix33d sub( final Matrix33d rhs ) {
        return new Matrix33d(
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
    public Matrix33d mul( final Matrix33d rhs ) {
        return new Matrix33d(
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
    public Vector3d mul( final Vector3d rhs ) {
        return new Vector3d(m_rows[0].dot( rhs ),
                m_rows[1].dot( rhs ),
                m_rows[2].dot( rhs ) );
    }

    /**
     * 指定された値でスカラー倍した行列を返します。
     * 
     * @param rhs スカラー
     * @return (this * rhs)
     */
    public Matrix33d mul( final double rhs ) {
        return new Matrix33d(
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
    public Matrix33d div( final double rhs ) {
        return new Matrix33d(
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
        if (!(obj instanceof Matrix33d)) {
            return false;
        }
        Matrix33d other = (Matrix33d) obj;
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
