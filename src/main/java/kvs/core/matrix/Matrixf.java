package kvs.core.matrix;

import java.io.Serializable;

import kvs.core.util.Math;

/**
 * <Core/Matrix/Matrix> 成分表現にfloat型を使い任意の行数と列数を持つ行列
 */
public class Matrixf implements Serializable{

    private static final long serialVersionUID = 5168203473806444479L;
    int m_nrows;
    int m_ncolumns;
    private Vectorf[] m_rows;

    /**
     * 行列を構築します。
     * 
     * @param nrows 列数
     * @param ncolumns 行数
     */
    public Matrixf( final int nrows, final int ncolumns ) {
        this.setSize( nrows, ncolumns );
        this.zero();
    }

    /**
     * 指定されたfloat配列で行列を構築します。 配列の長さは構築する行列の成分の総数以上である必要があります。
     * 
     * @param nrows 列数
     * @param ncolumns 行数
     * @param elements float配列
     * @exception IndexOutOfBoundsException
     */
    public Matrixf( final int nrows, final int ncolumns, final float[] elements ) {
        this.setSize( nrows, ncolumns );

        Vectorf[] m = m_rows;

        int index = 0;
        for (int r = 0; r < nrows; ++r) {
            float[] e = new float[ncolumns];
            for (int c = 0; c < ncolumns; ++c) {
                e[c] = elements[index];
                ++index;
            }
            m[r] = new Vectorf(e);
        }
    }
    
    /**
     * 指定された行列と同じ値の成分を持つ行列を構築します。
     * 
     * @param other 成分のコピー元
     */
    private Matrixf( final Matrixf other ) {

        this.setSize( other.m_nrows, other.m_ncolumns );

        this.m_rows = other.m_rows;
    }
    
    /**
     * この行列と同じ値の成分を持つ行列を返します。
     * 
     * @return 同じ値の成分を持つ行列
     */
    public final Matrixf copy() {

        return new Matrixf(this);
    }
    
    /**
     * 行列のサイズを設定し、初期化します。
     * 
     * @param nrows 列数
     * @param ncolumns 行数
     */
    public final void setSize( final int nrows, final int ncolumns ) {
        if (this.nrows() != nrows || this.ncolumns() != ncolumns) {
            m_nrows = nrows;
            m_ncolumns = ncolumns;

            this.m_rows = new Vectorf[m_nrows];
        }

        this.zero();
    }

    /**
     * すべての成分をVectorf配列で返します。
     */
    public final Vectorf[] get() {
        return this.m_rows;
    }

    /**
     * 指定された列を返します。
     * 
     * @param a 取得する列の値
     * @return 指定された列
     * @exception IndexOutOfBoundsException
     */
    public final Vectorf get( final int a ) {
        return this.m_rows[a];
    }


    /**
     * 指定された位置の値を返します。
     * 
     * @param row 列
     * @param col 行
     * @exception IndexOutOfBoundsException
     */
    public final float get( final int row, final int col ) {
        return this.m_rows[row].get( col );
    }
    
    /**
     * 指定された値を指定した位置の成分に設定します。
     * 
     * @param row 列
     * @param col 行
     * @param a 成分
     * @exception IndexOutOfBoundsException
     */
    public final void setAt( final int row, final int col, final float a ) {
        this.m_rows[row] = this.m_rows[row].modify( col, a );
    }
    
    /**
     * すべての成分をゼロに設定します。
     */
    public final void zero() {
        Vectorf zero = new Vectorf( this.m_ncolumns );
        for (int i = 0; i < this.m_nrows; ++i) {
            m_rows[i] = zero;
        }
    }
    
    
    /**
     * 恒等行列を返します。
     */
    public final Matrixf identity() {
        
        float[] elements = new float[m_nrows * m_ncolumns];
        for (int r = 0; r < m_nrows; ++r) {
            for (int c = 0; c < m_ncolumns; ++c) {
                elements[m_nrows * r + c] = 0.0f;
                if(r == c){
                    elements[m_nrows * r + c] = 1.0f;
                }
            }
        }
        
        return new Matrixf(m_nrows, m_ncolumns, elements);

    }

    /**
     * この行列の列数を返します。
     * 
     * @return 列数
     */
    public final int nrows() {
        return m_nrows;
    }

    /**
     * この行列の行数を返します。
     * 
     * @return 行数
     */
    public final int ncolumns() {
        return m_ncolumns;
    }

    /**
     * 転置行列を求めます。
     */
    public final Matrixf transpose() {
        int nrows = this.nrows();
        int ncolumns = this.ncolumns();
        
        float[] elements = new float[m_nrows * m_ncolumns];

        if (nrows == ncolumns) {
            for (int r = 0; r < nrows; ++r) {
                for (int c = r + 1; c < ncolumns; ++c) {
                    elements[m_nrows * r + c] = get(c, r);
                }
            }
        } else {
            for (int r = 0; r < nrows; ++r) {
                for (int c = 0; c < ncolumns; ++c) {
                    elements[m_nrows * r + c] = m_rows[r].get( c );
                }
            }
        }

        return new Matrixf(m_nrows, m_ncolumns, elements);
    }


    /**
     * 逆行列を求めます。
     * 
     * @exception ArithmeticException
     */
    public final Matrixf inverse() {
        
        Matrixf original = this.copy();
        Matrixf result = this.identity();
        
        // Alias.
        final int size = original.nrows();
        final int nrows = original.nrows();
        final int ncolumns = original.ncolumns();
        Vectorf[] m = original.m_rows;

        for (int k = 0; k < size; k++) {
            // Search a pivot row.
            final int pivot_row = this.pivot( k );

            // Swap the k-row and the pivot_row.
            if (k != pivot_row) {
                Vectorf tmp = m[k];
                m[k] = m[pivot_row];
                m[pivot_row] = tmp;
                
                tmp = result.m_rows[k];
                result.m_rows[k] = result.m_rows[pivot_row];
                result.m_rows[pivot_row] = tmp;
            }

            // Forward elimination
            final float diagonal_element = m[k].get( k );

            for (int c = 0; c < ncolumns; ++c) {
                final float x = m[k].get( c ) / diagonal_element;
                m[k] = m[k].modify( c, x );
                final float a = result.get( k, c ) / diagonal_element;
                result.setAt( k, c, a );
            }

            for (int r = 0; r < nrows; ++r) {
                // Skip the pivot_row.
                if (r != k) {
                    final float value = m[r].get( k );
                    for (int c = 0; c < ncolumns; ++c) {
                        final float x = m[r].get( c ) - value * m[k].get( c );
                        m[r] = m[r].modify( c, x );
                        final float a = result.get( r, c ) - value * result.get( k, c );
                        result.setAt( r, c, a );
                    }
                }
            }
        }

        return result;
    }

    /**
     * 標準出力にこのオブジェクトの文字列表現を出力します。
     */
    public final void print() {
        System.out.println( this.toString() );
    }

    /**
     * 対角成分の和を求めます。
     * 
     * @return 対角成分の和
     */
    public final float trace() {
        int nrows = this.nrows();
        Vectorf[] m = m_rows;

        float result = 0.0f;

        for (int r = 0; r < nrows; ++r) {
            result += m[r].get( r );
        }

        return result;
    }

    /**
     * 行列式を求めます。
     * 
     * @return 行列式の値
     */
    public final float determinant() {

        final int size = this.nrows();
        final int nrows = this.nrows();
        final int ncolumns = this.ncolumns();

        float det = 1.0f;
        
        float[] elements = new float[m_nrows * m_ncolumns];

        for (int k = 0; k < size; ++k) {
            final int pivot_row = this.pivot( k );

            if (k != pivot_row) {
                det *= -1.0;
            }

            det *= this.get( k, k );

            for (int r = k + 1; r < nrows; ++r) {
                final float value = this.get( r, k ) / this.get( k, k );

                for (int c = k + 1; c < ncolumns; ++c) {
                    // result[r][c] -= value * result[k][c];
                    elements[m_nrows * r + c] = this.get( r, c ) - value * this.get( k, c );
                }
            }
        }

        return (det);
    }

    /**
     * ピボット。
     * 
     * @param column
     * @return ピボット
     */
    public final int pivot( final int column ) {
        final int nrows = this.nrows();
        Vectorf[] m = m_rows;

        // Search a max absolute value in the vector of a given row index.
        float max = 0.0f;
        int k = column;

        for (int r = column; r < nrows; r++) {
            final float abs = Math.abs( m[r].get( column ) );
            if (abs > max) {
                max = abs;
                k = r;
            }
        }

        return (k);
    }

    /**
     * 指定された行列との和をを返します。
     * 
     * @param rhs 和を取る相手の行列
     * @return 行列の和
     */
    public final Matrixf add( final Matrixf rhs ) {
        final int nrows = this.nrows();
        Matrixf result = this.copy();

        for (int r = 0; r < nrows; ++r) {
            result.m_rows[r] = m_rows[r].add( rhs.get( r ) );
        }
        return result;
    }
    

    /**
     * 指定された行列との差を返します。
     * 
     * @param rhs 差を取る相手の行列
     * @return 行列の差
     */
    public final Matrixf sub( final Matrixf rhs ) {
        final int nrows = this.nrows();
        Matrixf result = this.copy();

        for (int r = 0; r < nrows; ++r) {
            result.m_rows[r] = m_rows[r].sub( rhs.get( r ) );
        }
        return result;
    }

    /**
     * 指定された値でスカラー倍した行列を返します。
     * 
     * @param rhs スカラー
     * @return (this * rhs)
     */
    public final Matrixf mul( final float rhs ) {
        final int nrows = this.nrows();
        Matrixf result = this.copy();

        for (int r = 0; r < nrows; ++r) {
            result.m_rows[r] = m_rows[r].mul( rhs );
        }
        return result;
    }

    /**
     * 指定された値でスカラー倍した行列を返します。
     * 
     * @param rhs スカラー
     * @return (this / rhs)
     * @exception ArithmeticException
     */
    public final Matrixf div( final float rhs ) {
        final int nrows = this.nrows();
        Matrixf result = this.copy();

        for (int r = 0; r < nrows; ++r) {
            result.m_rows[r] = m_rows[r].div( rhs );
        }
        return result;
    }

    /**
     * このベクトルと指定されたオブジェクトを比較します。
     * このオブジェクトと同じベクトルを表すVectorfオブジェクトである場合にだけtrueを返します。
     * 
     * @param obj このベクトルと比較されるオブジェクト
     * @return ベクトルが等しい場合はtrue、そうでない場合はfalse
     */
    @Override
    public final boolean equals( final Object obj ) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Matrixf)) {
            return false;
        }
        Matrixf rhs = (Matrixf) obj;

        final int nrows = this.nrows();

        boolean result = (this.nrows() == rhs.nrows());

        for (int r = 0; r < nrows; ++r) {
            result = result && (this.get( r ) == rhs.get( r ));
        }

        return result;
    }

    /**
     * オブジェクトのハッシュコード値を返します。
     * 
     * @return このベクトルのハッシュコード値
     */
    @Override
    public final int hashCode() {
        int result = 17;
        for (int i = 0; i < this.nrows(); ++i) {
            result = 31 * result + this.get( i ).hashCode();
        }
        return result;
    }

    /**
     * このオブジェクトの文字列表現を返します。
     * 
     * @return このオブジェクトの文字列表現
     */
    @Override
    public final String toString() {
        String crlf = System.getProperty( "line.separator" );
        StringBuffer os = new StringBuffer();

        for (int r = 0; r < this.nrows() - 1; ++r) {
            os.append( this.get( r ).toString() );
            os.append( crlf );
        }
        os.append( this.get( this.nrows() - 1 ).toString() );

        return os.toString();
    }
}
