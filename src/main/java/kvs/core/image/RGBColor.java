package kvs.core.image;

import java.awt.Color;

/**
 * オリジナルのRGBColorクラスの基本機能はjava.awt.Colorで代用されますが、
 * 一部必要な関数が不足しています。
 * そのため、このクラスで必要なメソッドのみを提供します。
 * 
 */

public final class RGBColor {

    private RGBColor(){}

    /**
     * 二つのColorのRGB値を単純に足し合わせます。
     * 
     * @param col1 一つ目のColor
     * @param col2 二つ目のColor
     * @return 新しいColor
     */
    public static Color add( Color col1, Color col2 ){
        return new Color( col1.getRed()      + col2.getRed(),
                          col1.getGreen()    + col2.getGreen(),
                          col1.getBlue()     + col2.getBlue());   
    }
    
    /**
     * ColorのRGB値に指定された値をかけ合わせます。
     * 
     * @param col Color
     * @param a R,G,Bのそれぞれに掛け合わす値
     * @return 新しいColor
     */
    public static Color mul( Color col, float a  ){
        return new Color( kvs.core.util.Math.round( col.getRed() * a ),
                          kvs.core.util.Math.round( col.getGreen() * a ),
                          kvs.core.util.Math.round( col.getBlue() * a ));
    }

}
