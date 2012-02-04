package kvs.core.matrix;

import java.io.Serializable;

/**
 * Matrix44dクラスは成分表現にdouble型を利用する4*4行列ベクトルを表します。
 */

public final class Matrix44d implements Serializable{

    private static final long serialVersionUID = 904702376411219717L;

    public static final Matrix44d ZERO = new Matrix44d(0.0);

    public static final Matrix44d IDENTITY = new Matrix44d(1.0d, 0.0, 0.0, 0.0,
            0.0, 1.0d, 0.0, 0.0,
            0.0, 0.0, 1.0d, 0.0,
            0.0, 0.0, 0.0, 1.0d);

    private Vector4d[] m_rows = new Vector4d[4];

    /**
     * すべての成分が指定された値の行列を構築します。
     */
     public Matrix44d( final double a ) {
         this.m_rows[0] =
         this.m_rows[1] = 
         this.m_rows[2] = 
         this.m_rows[3] = new Vector4d( a );
     }

     /**
      * 指定された値の行列を構築します。
      */
     public Matrix44d( final double a00, final double a01, final double a02, final double a03, final double a10, final double a11, final double a12, final double a13, final double a20, final double a21, final double a22, final double a23, final double a30, final double a31, final double a32, final double a33 ) {
         this.m_rows[0] = new Vector4d(a00, a01, a02, a03);
         this.m_rows[1] = new Vector4d(a10, a11, a12, a13);
         this.m_rows[2] = new Vector4d(a20, a21, a22, a23);
         this.m_rows[3] = new Vector4d(a30, a31, a32, a33);
     }

     /**
      * 指定されたベクトルを各列として行列を構築します。
      */
     public Matrix44d( final Vector4d v0, final Vector4d v1, final Vector4d v2, final Vector4d v3 ) {
         this.m_rows[0] = v0;
         this.m_rows[1] = v1;
         this.m_rows[2] = v2;
         this.m_rows[3] = v3;    }

     /**
      * 指定されたdouble配列で4*4行列を構築します。
      * 
      * @param elements 16つ以上の値を持つdouble配列
      * @exception IndexOutOfBoundsException elements.length < 16
      */
     public Matrix44d( final double[] elements ) {
         this.m_rows[0] = new Vector4d(elements[0],elements[1],elements[2],elements[3]);
         this.m_rows[1] = new Vector4d(elements[4],elements[5],elements[6],elements[7]);
         this.m_rows[2] = new Vector4d(elements[8],elements[9],elements[10],elements[11]);
         this.m_rows[3] = new Vector4d(elements[12],elements[13],elements[14],elements[15]);
     }

     /**
      * 指定された列を返します。
      * 
      * @param a 取得する列の値
      * @return 指定された列
      */
     public Vector4d getRow( final int a ) {
         return this.m_rows[a];
     }

     /**
      * すべての成分を配列で返します。
      * 
      * @return すべての成分
      */
     public double[] get() {
         double[] elements = new double[16];
         for(int r = 0;r < 4;++r){
             for(int c = 0;c < 4;++c){
                 elements[4*r + c] = this.get(r, c);
             }
         }
         return elements;
     }

     /**
      * 指定された位置の値を返します。
      * 
      * @param row 列
      * @param col 行
      * @exception IndexOutOfBoundsException row > 3 || col > 3
      */
     public double get( final int row, final int col ) {
         return this.m_rows[row].get( col );
     }

     /**
      * 転置行列を新しいMatrix44dで返します。
      */
     public Matrix44d transpose() {
         return new Matrix44d(
                 get( 0, 0 ),
                 get( 1, 0 ),
                 get( 2, 0 ),
                 get( 3, 0 ),
                 get( 0, 1 ),
                 get( 1, 1 ),
                 get( 2, 1 ),
                 get( 3, 1 ),
                 get( 0, 2 ),
                 get( 1, 2 ),
                 get( 2, 2 ),
                 get( 3, 2 ),
                 get( 0, 3 ),
                 get( 1, 3 ),
                 get( 2, 3 ),
                 get( 3, 3 ) );
     }

     /**
      * 逆行列を新しいMatrix44dで返します。
      * 
      * @exception ArithmeticException
      */
     public Matrix44d inverse() {
         final double[] det22upper = { m_rows[0].get( 2 ) * m_rows[1].get( 3 ) - m_rows[0].get( 3 ) * m_rows[1].get( 2 ), m_rows[0].get( 1 ) * m_rows[1].get( 3 ) - m_rows[0].get( 3 ) * m_rows[1].get( 1 ), m_rows[0].get( 0 ) * m_rows[1].get( 3 ) - m_rows[0].get( 3 ) * m_rows[1].get( 0 ), m_rows[0].get( 1 ) * m_rows[1].get( 2 ) - m_rows[0].get( 2 ) * m_rows[1].get( 1 ), m_rows[0].get( 0 ) * m_rows[1].get( 2 ) - m_rows[0].get( 2 ) * m_rows[1].get( 0 ), m_rows[0].get( 0 ) * m_rows[1].get( 1 ) - m_rows[0].get( 1 ) * m_rows[1].get( 0 ) };

         final double[] det22lower = { m_rows[2].get( 2 ) * m_rows[3].get( 3 ) - m_rows[2].get( 3 ) * m_rows[3].get( 2 ), m_rows[2].get( 1 ) * m_rows[3].get( 3 ) - m_rows[2].get( 3 ) * m_rows[3].get( 1 ), m_rows[2].get( 0 ) * m_rows[3].get( 3 ) - m_rows[2].get( 3 ) * m_rows[3].get( 0 ), m_rows[2].get( 1 ) * m_rows[3].get( 2 ) - m_rows[2].get( 2 ) * m_rows[3].get( 1 ), m_rows[2].get( 0 ) * m_rows[3].get( 2 ) - m_rows[2].get( 2 ) * m_rows[3].get( 0 ), m_rows[2].get( 0 ) * m_rows[3].get( 1 ) - m_rows[2].get( 1 ) * m_rows[3].get( 0 ) };

         final double[] det33 = { m_rows[1].get( 1 ) * det22lower[0] - m_rows[1].get( 2 ) * det22lower[1] + m_rows[1].get( 3 ) * det22lower[3], m_rows[1].get( 0 ) * det22lower[0] - m_rows[1].get( 2 ) * det22lower[2] + m_rows[1].get( 3 ) * det22lower[4], m_rows[1].get( 0 ) * det22lower[1] - m_rows[1].get( 1 ) * det22lower[2] + m_rows[1].get( 3 ) * det22lower[5], m_rows[1].get( 0 ) * det22lower[3] - m_rows[1].get( 1 ) * det22lower[4] + m_rows[1].get( 2 ) * det22lower[5], m_rows[0].get( 1 ) * det22lower[0] - m_rows[0].get( 2 ) * det22lower[1] + m_rows[0].get( 3 ) * det22lower[3], m_rows[0].get( 0 ) * det22lower[0] - m_rows[0].get( 2 ) * det22lower[2] + m_rows[0].get( 3 ) * det22lower[4], m_rows[0].get( 0 ) * det22lower[1] - m_rows[0].get( 1 ) * det22lower[2] + m_rows[0].get( 3 ) * det22lower[5], m_rows[0].get( 0 ) * det22lower[3] - m_rows[0].get( 1 ) * det22lower[4] + m_rows[0].get( 2 ) * det22lower[5], m_rows[3].get( 1 ) * det22upper[0] - m_rows[3].get( 2 ) * det22upper[1] + m_rows[3].get( 3 ) * det22upper[3], m_rows[3].get( 0 ) * det22upper[0] - m_rows[3].get( 2 ) * det22upper[2] + m_rows[3].get( 3 ) * det22upper[4], m_rows[3].get( 0 ) * det22upper[1] - m_rows[3].get( 1 ) * det22upper[2] + m_rows[3].get( 3 ) * det22upper[5], m_rows[3].get( 0 ) * det22upper[3] - m_rows[3].get( 1 ) * det22upper[4] + m_rows[3].get( 2 ) * det22upper[5], m_rows[2].get( 1 ) * det22upper[0] - m_rows[2].get( 2 ) * det22upper[1] + m_rows[2].get( 3 ) * det22upper[3], m_rows[2].get( 0 ) * det22upper[0] - m_rows[2].get( 2 ) * det22upper[2] + m_rows[2].get( 3 ) * det22upper[4], m_rows[2].get( 0 ) * det22upper[1] - m_rows[2].get( 1 ) * det22upper[2] + m_rows[2].get( 3 ) * det22upper[5], m_rows[2].get( 0 ) * det22upper[3] - m_rows[2].get( 1 ) * det22upper[4] + m_rows[2].get( 2 ) * det22upper[5] };

         final double det44 = m_rows[0].get( 0 ) * det33[0] - m_rows[0].get( 1 ) * det33[1] + m_rows[0].get( 2 ) * det33[2] - m_rows[0].get( 3 ) * det33[3];

         final double det_inverse = (1.0d / det44);
         for (int i = 0; i < det33.length; ++i) {
             det33[i] *= det_inverse;
         }

         return new Matrix44d( +det33[0], -det33[4], +det33[8], -det33[12],
                 -det33[1], +det33[5], -det33[9], +det33[13],
                 +det33[2], -det33[6], +det33[10], -det33[14],
                 -det33[3], +det33[7], -det33[11], +det33[15] );
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
         return (this.get( 0, 0 ) + this.get( 1, 1 ) + this.get( 2, 2 ) + this.get( 3, 3 ));
     }

     /**
      * 行列式を求めます。
      * 
      * @return 行列式の値
      */
     public double determinant() {
         final double[] det22lower = { this.get( 2, 2 ) * this.get( 3, 3 ) - this.get( 2, 3 ) * this.get( 3, 2 ), this.get( 2, 1 ) * this.get( 3, 3 ) - this.get( 2, 3 ) * this.get( 3, 1 ), this.get( 2, 0 ) * this.get( 3, 3 ) - this.get( 2, 3 ) * this.get( 3, 0 ), this.get( 2, 1 ) * this.get( 3, 2 ) - this.get( 2, 2 ) * this.get( 3, 1 ), this.get( 2, 0 ) * this.get( 3, 2 ) - this.get( 2, 2 ) * this.get( 3, 0 ), this.get( 2, 0 ) * this.get( 3, 1 ) - this.get( 2, 1 ) * this.get( 3, 0 ) };

         final double[] det33 = { this.get( 1, 1 ) * det22lower[0] - this.get( 1, 2 ) * det22lower[1] + this.get( 1, 3 ) * det22lower[3], this.get( 1, 0 ) * det22lower[0] - this.get( 1, 2 ) * det22lower[2] + this.get( 1, 3 ) * det22lower[4], this.get( 1, 0 ) * det22lower[1] - this.get( 1, 1 ) * det22lower[2] + this.get( 1, 3 ) * det22lower[5], this.get( 1, 0 ) * det22lower[3] - this.get( 1, 1 ) * det22lower[4] + this.get( 1, 2 ) * det22lower[5] };

         final double det44 = this.get( 0, 0 ) * det33[0] - this.get( 0, 1 ) * det33[1] + this.get( 0, 2 ) * det33[2] - this.get( 0, 3 ) * det33[3];

         return (det44);
     }

     /**
      * 指定された行列との和を新しいMatrix44dで返します。
      * 
      * @param rhs 和を取る相手の行列
      * @return 行列の和
      */
     public Matrix44d add( final Matrix44d rhs ) {
         return new Matrix44d(
                 this.m_rows[0].add( rhs.m_rows[0] ),
                 this.m_rows[1].add( rhs.m_rows[1] ),
                 this.m_rows[2].add( rhs.m_rows[2] ),
                 this.m_rows[3].add( rhs.m_rows[3] ));
     }

     /**
      * 指定された行列との差を新しいMatrix44dで返します。
      * 
      * @param rhs 差を取る相手の行列
      * @return 行列の差
      */
     public Matrix44d sub( final Matrix44d rhs ) {
         return new Matrix44d(
                 this.m_rows[0].sub( rhs.m_rows[0] ),
                 this.m_rows[1].sub( rhs.m_rows[1] ),
                 this.m_rows[2].sub( rhs.m_rows[2] ),
                 this.m_rows[3].sub( rhs.m_rows[3] ));
     }

     /**
      * 指定されたベクトルとの積を新しいVector4dで返します。
      * 
      * @param rhs 積を取る相手のベクトル
      * @return (this * rhs)
      */
     public Vector4d mul( final Vector4d rhs ) {
         return new Vector4d(
                 m_rows[0].dot( rhs ),
                 m_rows[1].dot( rhs ),
                 m_rows[2].dot( rhs ),
                 m_rows[3].dot( rhs ));
     }

     /**
      * 指定された行列との積を新しいMatrix44dで返します。
      * 
      * @param rhs 積を取る相手の行列
      * @return (this * rhs)
      */
     public Matrix44d mul( final Matrix44d rhs ) {
         return new Matrix44d( m_rows[0].get( 0 ) * rhs.get( 0, 0 ) + m_rows[0].get( 1 ) * rhs.get( 1, 0 ) + m_rows[0].get( 2 ) * rhs.get( 2, 0 ) + m_rows[0].get( 3 ) * rhs.get( 3, 0 ),
                 m_rows[0].get( 0 ) * rhs.get( 0, 1 ) + m_rows[0].get( 1 ) * rhs.get( 1, 1 ) + m_rows[0].get( 2 ) * rhs.get( 2, 1 ) + m_rows[0].get( 3 ) * rhs.get( 3, 1 ),
                 m_rows[0].get( 0 ) * rhs.get( 0, 2 ) + m_rows[0].get( 1 ) * rhs.get( 1, 2 ) + m_rows[0].get( 2 ) * rhs.get( 2, 2 ) + m_rows[0].get( 3 ) * rhs.get( 3, 2 ),
                 m_rows[0].get( 0 ) * rhs.get( 0, 3 ) + m_rows[0].get( 1 ) * rhs.get( 1, 3 ) + m_rows[0].get( 2 ) * rhs.get( 2, 3 ) + m_rows[0].get( 3 ) * rhs.get( 3, 3 ),
                 m_rows[1].get( 0 ) * rhs.get( 0, 0 ) + m_rows[1].get( 1 ) * rhs.get( 1, 0 ) + m_rows[1].get( 2 ) * rhs.get( 2, 0 ) + m_rows[1].get( 3 ) * rhs.get( 3, 0 ),
                 m_rows[1].get( 0 ) * rhs.get( 0, 1 ) + m_rows[1].get( 1 ) * rhs.get( 1, 1 ) + m_rows[1].get( 2 ) * rhs.get( 2, 1 ) + m_rows[1].get( 3 ) * rhs.get( 3, 1 ),
                 m_rows[1].get( 0 ) * rhs.get( 0, 2 ) + m_rows[1].get( 1 ) * rhs.get( 1, 2 ) + m_rows[1].get( 2 ) * rhs.get( 2, 2 ) + m_rows[1].get( 3 ) * rhs.get( 3, 2 ),
                 m_rows[1].get( 0 ) * rhs.get( 0, 3 ) + m_rows[1].get( 1 ) * rhs.get( 1, 3 ) + m_rows[1].get( 2 ) * rhs.get( 2, 3 ) + m_rows[1].get( 3 ) * rhs.get( 3, 3 ),
                 m_rows[2].get( 0 ) * rhs.get( 0, 0 ) + m_rows[2].get( 1 ) * rhs.get( 1, 0 ) + m_rows[2].get( 2 ) * rhs.get( 2, 0 ) + m_rows[2].get( 3 ) * rhs.get( 3, 0 ),
                 m_rows[2].get( 0 ) * rhs.get( 0, 1 ) + m_rows[2].get( 1 ) * rhs.get( 1, 1 ) + m_rows[2].get( 2 ) * rhs.get( 2, 1 ) + m_rows[2].get( 3 ) * rhs.get( 3, 1 ),
                 m_rows[2].get( 0 ) * rhs.get( 0, 2 ) + m_rows[2].get( 1 ) * rhs.get( 1, 2 ) + m_rows[2].get( 2 ) * rhs.get( 2, 2 ) + m_rows[2].get( 3 ) * rhs.get( 3, 2 ),
                 m_rows[2].get( 0 ) * rhs.get( 0, 3 ) + m_rows[2].get( 1 ) * rhs.get( 1, 3 ) + m_rows[2].get( 2 ) * rhs.get( 2, 3 ) + m_rows[2].get( 3 ) * rhs.get( 3, 3 ),
                 m_rows[3].get( 0 ) * rhs.get( 0, 0 ) + m_rows[3].get( 1 ) * rhs.get( 1, 0 ) + m_rows[3].get( 2 ) * rhs.get( 2, 0 ) + m_rows[3].get( 3 ) * rhs.get( 3, 0 ),
                 m_rows[3].get( 0 ) * rhs.get( 0, 1 ) + m_rows[3].get( 1 ) * rhs.get( 1, 1 ) + m_rows[3].get( 2 ) * rhs.get( 2, 1 ) + m_rows[3].get( 3 ) * rhs.get( 3, 1 ),
                 m_rows[3].get( 0 ) * rhs.get( 0, 2 ) + m_rows[3].get( 1 ) * rhs.get( 1, 2 ) + m_rows[3].get( 2 ) * rhs.get( 2, 2 ) + m_rows[3].get( 3 ) * rhs.get( 3, 2 ),
                 m_rows[3].get( 0 ) * rhs.get( 0, 3 ) + m_rows[3].get( 1 ) * rhs.get( 1, 3 ) + m_rows[3].get( 2 ) * rhs.get( 2, 3 ) + m_rows[3].get( 3 ) * rhs.get( 3, 3 ));

     }


     /**
      * 指定された値でスカラー倍したものを新しいMatrix44dで返します。
      * 
      * @param rhs スカラー
      * @return (this * rhs)
      */
     public Matrix44d mul( final double rhs ) {
         return new Matrix44d(
                 this.m_rows[0].mul( rhs ),
                 this.m_rows[1].mul( rhs ),
                 this.m_rows[2].mul( rhs ),
                 this.m_rows[3].mul( rhs ));
     }

     /**
      * 指定された値でスカラー倍したものを新しいMatrix44dで返します。
      * 
      * @param rhs スカラー
      * @return (this / rhs)
      * @exception ArithmeticException
      */
     public Matrix44d div( final double rhs ) {
         return new Matrix44d(
                 this.m_rows[0].div( rhs ),
                 this.m_rows[1].div( rhs ),
                 this.m_rows[2].div( rhs ),
                 this.m_rows[3].div( rhs ));
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
         if (!(obj instanceof Matrix44d)) {
             return false;
         }
         Matrix44d other = (Matrix44d) obj;
         return (this.getRow( 0 ).equals( other.getRow( 0 ) ) &&
                 this.getRow( 1 ).equals( other.getRow( 1 ) ) &&
                 this.getRow( 2 ).equals( other.getRow( 2 ) ) &&
                 this.getRow( 3 ).equals( other.getRow( 3 ) ));
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
         result = 31 * result + this.getRow( 3 ).hashCode();
         return result;
     }

     /**
      * このオブジェクトの文字列表現を返します。 this.get( 0 ).toString() this.get( 1 ).toString()
      * this.get( 2 ).toString() this.get( 3 ).toString();
      * 
      * @return このオブジェクトの文字列表現
      */
     @Override
     public String toString() {
         String crlf = System.getProperty( "line.separator" );
         return this.getRow( 0 ).toString() + crlf + this.getRow( 1 ).toString() + crlf + this.getRow( 2 ).toString() + crlf + this.getRow( 3 ).toString();
     }

}
