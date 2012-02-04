package kvs.core.matrix;

import java.io.Serializable;

import kvs.core.util.Math;

/**
 * AbstractVectorDoubleクラスは成分表現にdouble型を利用する多次元ベクトルで共通する
 * メソッドとフィールドをまとめたクラスです。
 */

abstract class AbstractVectorDouble implements Serializable{

    private static final long serialVersionUID = -5597179754604698944L;
    protected final double[] m_elements;  //成分を格納するための配列

    /**
     * メンバー変数を初期化します。
     * このクラスを継承するすべてのクラスで明示的に呼び出す必要があります。
     * 
     * 
     * @param size このベクトルの次元数
     */
    public AbstractVectorDouble( int size ) {
        m_elements = new double[size];
    }

    /**
     * すべての成分を配列で返します。
     * 
     * @return すべての成分
     */
    public double[] get(){
        double[] elements = new double[m_elements.length];
        System.arraycopy(m_elements, 0, elements, 0, m_elements.length);
        return elements;
    }

    /**
     * 指定された位置の成分を返します。
     * 
     * @param i
     *            返される成分のインデックス
     * @return 指定された位置の成分
     * @exception IndexOutOfBoundsException
     */
    public double get(final int i) {
        return this.m_elements[i];
    }

    /**
     * 標準出力にこのオブジェクトの文字列表現を出力します。
     */
    public void print() {
        System.out.println(this.toString());
    }

    /**
     * ベクトルの大きさを返します。
     * 
     * @return ベクトルの大きさ
     */
    public double length() {
        return (Math.squareRoot(this.length2()));
    }

    /**
     * ベクトルの大きさの二乗の値を返します。
     * 
     * @return ベクトルの大きさの二乗の値
     */
    public double length2() {
        double result = 0.0d;

        for(double element : m_elements){
            result += element * element;
        }
        return (result);

    }

    /**
     * このベクトルと指定されたオブジェクトを比較します。
     * このオブジェクトと同じ成分のベクトルを表すオブジェクトである場合にだけtrueを返します。
     * 
     * @param obj
     *            このベクトルと比較されるオブジェクト
     * @return ベクトルが等しい場合はtrue、そうでない場合はfalse
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AbstractVectorDouble)) {
            return false;
        }
        AbstractVectorDouble rhs = (AbstractVectorDouble) obj;

        boolean result = (this.m_elements.length == rhs.m_elements.length);
        for( int i = 0;i<m_elements.length;++i){
            result &= Math.equal(this.m_elements[i], rhs.m_elements[i]);
        }

        return result;
    }

    /**
     * オブジェクトのハッシュコード値を返します。
     * 
     * @return このベクトルのハッシュコード値。
     */
    @Override
    public int hashCode() {
        int result = 17;
        for( double element : m_elements){
            result = 31 * result + (int)element;
        }
        return result;
    }

    /**
     * このオブジェクトの文字列表現を返します。 
     * 
     * @return このオブジェクトの文字列表現
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(m_elements[0]);
        for(int i = 1; i < m_elements.length;++i){
            sb.append(" ");
            sb.append(this.m_elements[i]);
        }

        return sb.toString();
    }
}
