package kvs.core.util;
/**
 * <Core/Utility/Math.h>
 * 
 */

public final class Math {
    
    public static final double KVS__MATH_TINY_VALUE = 1.e-6;
    
    public static final double sqrt2 = 1.4142135623730950488;

    public static final double sqrt2_div_2 = 0.7071067811865475244;

    public static final double pi = 3.1415926535897932384;

    public static final double pi_mul_2 = 6.2831853071795864769;

    public static final double pi_div_2 = 1.5707963267948966192;

    public static final double pi_div_16 = 0.1963495408493620774;

    public static final double golden_ratio = 1.6180339887498948482;
    
    private Math(){
        
    }
    
    /**
     * 2 つの int 値のうち大きい方を返します。
     *
     * @param a
     * @param b
     * @return a と b のどちらか大きい方
     */
    public static final int max(int a, int b) {
        return (a >= b) ? a : b;
    }
    
    /**
     * 2 つの long 値のうち大きい方を返します。
     *
     * @param a
     * @param b
     * @return a と b のどちらか大きい方
     */
    public static final long max(long a, long b) {
        return (a >= b) ? a : b;
    }
    
    /**
     * 2 つの float 値のうち大きい方を返します。
     *
     * @param a
     * @param b
     * @return a と b のどちらか大きい方
     */
    public static final float max(float a, float b) {
        return java.lang.Math.max(a, b);
    }
    
    /**
     * 2 つの double 値のうち大きい方を返します。
     *
     * @param a
     * @param b
     * @return a と b のどちらか大きい方
     */
    public static final double max(double a, double b) {
        return java.lang.Math.max(a, b);
    }
    
    /**
     * 3 つの int 値のうち大きい方を返します。
     *
     * @param a
     * @param b
     * @param c
     * @return a と b と c のうち最も大きい方方
     */
    public static final int max(int a, int b, int c) {
        return ( max( max( a, b ), c ) );
    }
    
    /**
     * 3 つの long 値のうち大きい方を返します。
     *
     * @param a
     * @param b
     * @param c
     * @return a と b と c のうち最も大きい方
     */
    public static final long max(long a, long b, long c) {
        return ( max( max( a, b ), c ) );
    }
    
    /**
     * 3 つの float 値のうち大きい方を返します。
     *
     * @param a
     * @param b
     * @param c
     * @return a と b と c のうち最も大きい方
     */
    public static final float max(float a, float b, float c) {
        return ( max( max( a, b ), c ) );
    }
    
    /**
     * 3 つの double 値のうち大きい方を返します。
     *
     * @param a
     * @param b
     * @param c
     * @return a と b と c のうち最も大きい方
     */
    public static final double max(double a, double b, double c) {
        return ( max( max( a, b ), c ) );
    }
    
    /**
     * 2 つの int 値のうち小さい方を返します。
     *
     * @param a
     * @param b
     * @return a と b のどちらか小さい方
     */
    public static final int min(int a, int b) {
        return (a <= b) ? a : b;
    }
    
    /**
     * 2 つの long 値のうち小さい方を返します。
     *
     * @param a
     * @param b
     * @return a と b のどちらか小さい方
     */
    public static final long min(long a, long b) {
        return (a <= b) ? a : b;
    }
    
    /**
     * 2 つの float 値のうち小さい方を返します。
     *
     * @param a
     * @param b
     * @return a と b のどちらか小さい方
     */
    public static final float min(float a, float b) {
        return java.lang.Math.min(a, b);
    }
    
    /**
     * 2 つの double 値のうち小さい方を返します。
     *
     * @param a
     * @param b
     * @return a と b のどちらか小さい方
     */
    public static final double min(double a, double b) {
        return java.lang.Math.min(a, b);
    }
    
    /**
     * 3 つの int 値のうち小さい方を返します。
     *
     * @param a
     * @param b
     * @param c
     * @return a と b と c のうち最も小さい方
     */
    public static final int min(int a, int b, int c) {
        return ( min( min( a, b ), c ) );
    }
    
    /**
     * 3 つの long 値のうち小さい方を返します。
     *
     * @param a
     * @param b
     * @param c
     * @return a と b と c のうち最も小さい方
     */
    public static final long min(long a, long b, long c) {
        return ( min( min( a, b ), c ) );
    }
    
    /**
     * 3 つの float 値のうち小さい方を返します。
     *
     * @param a
     * @param b
     * @param c
     * @return a と b と c のうち最も小さい方
     */
    public static final float min(float a, float b, float c) {
        return ( min( min( a, b ), c ) );
    }
    
    /**
     * 3 つの double 値のうち小さい方を返します。
     *
     * @param a
     * @param b
     * @param c
     * @return a と b と c のうち最も小さい方
     */
    public static final double min(double a, double b, double c) {
        return ( min( min( a, b ), c ) );
    }
    
    /**
     * int 値の絶対値を返します。
     *
     * @param a
     * @return 引数の絶対値
     */
    public static final int abs(int a) {
        return (a < 0) ? -a : a;
    }
    
    /**
     * long 値の絶対値を返します。
     *
     * @param a
     * @return 引数の絶対値
     */
    public static final long abs(long a) {
        return (a < 0) ? -a : a;
    }
    
    /**
     * float 値の絶対値を返します。
     *
     * @param a
     * @return 引数の絶対値
     */
    public static final float abs(float a) {
        return (a <= 0.0F) ? 0.0F - a : a;
    }
    
    /**
     * double 値の絶対値を返します。
     *
     * @param a
     * @return 引数の絶対値
     */
    public static final double abs(double a) {
        return (a <= 0.0D) ? 0.0D - a : a;
    }
    
    public static final int sgn(int a){
        return( ( a < 0 ) ? -1 : 1 );
    }
    
    public static final long sgn(long a){
        return( ( a < 0 ) ? -1 : 1 );
    }
    
    public static final float sgn(float a){
        return( ( a < 0F ) ? -1 : 1 );
    }
    
    public static final double sgn(double a){
        return( ( a < 0D ) ? -1 : 1 );
    }
    
    public static final int sgn(int a, int b){
        return( ( b <  0  ) ? -abs( a ) : abs( a ) );
    }
    
    public static final long sgn(long a, long b){
        return( ( b <  0  ) ? -abs( a ) : abs( a ) );
    }
    
    public static final float sgn(float a, float b){
        return( ( b <  0F  ) ? -abs( a ) : abs( a ) );
    }
    
    public static final double sgn(double a, double b){
        return( ( b <  0D  ) ? -abs( a ) : abs( a ) );
    }
    
    public static final int[] shift(int[] a){
        for(int i = 1;i<a.length;i++){
            a[i - 1] = a[i];
        }
        return a;
    }
    
    public static final long[] shift(long[] a){
        for(int i = 1;i<a.length;i++){
            a[i - 1] = a[i];
        }
        return a;
    }
    
    public static final float[] shift(float[] a){
        for(int i = 1;i<a.length;i++){
            a[i - 1] = a[i];
        }
        return a;
    }
    
    public static final double[] shift(double[] a){
        for(int i = 1;i<a.length;i++){
            a[i - 1] = a[i];
        }
        return a;
    }
    
    /**
     * 引数に最も近いint 値を返します。
     *
     * @param a
     * @return 引数をもっとも近い int 値に丸めた値
     */
    public static final int round(float a){
        return( a >= 0 ? (int)( a + 0.5 ) : -(int)( 0.5 - a ) );
    }
    
    /**
     * 引数に最も近いint 値を返します。
     *
     * @param a
     * @return 引数をもっとも近い int 値に丸めた値
     */
    public static final int round(double a){
        return( a >= 0 ? (int)( a + 0.5 ) : -(int)( 0.5 - a ) );
    }
    
    public static final int floor(float a){
        return( a >= 0F ? (int)( a ) : (int)( a ) - 1 );
    }
    
    public static final int floor(double a){
        return( a >= 0D ? (int)( a ) : (int)( a ) - 1 );
    }
    
    public static final int ceil(double a) {
        return (int) java.lang.Math.ceil(a);
    }
    
    public static final int clamp(int a, int low, int high){
        return( a <= low ? low : a <= high ? a : high );
    }
    
    public static final long clamp(long a, long low, long high){
        return( a <= low ? low : a <= high ? a : high );
    }
    
    public static final float clamp(float a, float low, float high){
        return( a <= low ? low : a <= high ? a : high );
    }
    
    public static final double clamp(double a, double low, double high){
        return( a <= low ? low : a <= high ? a : high );
    }
    
    public static final boolean equal( int a, int b ){
        return( abs( a - b ) < KVS__MATH_TINY_VALUE );
    }
    
    public static final boolean equal( long a, long b ){
        return( abs( a - b ) < KVS__MATH_TINY_VALUE );
    }
    
    public static final boolean equal( float a, float b ){
        return( abs( a - b ) < KVS__MATH_TINY_VALUE );
    }
    
    public static final boolean equal(double a, double b ){
        return( abs( a - b ) < KVS__MATH_TINY_VALUE );
    }
    
    public static final boolean isZero( int a ){
        return( equal( a, 0 ) );
    }
    
    public static final boolean isZero( long a ){
        return( equal( a, 0 ) );
    }
    
    public static final boolean isZero( float a ){
        return( equal( a, 0 ) );
    }
    
    public static final boolean isZero( double a ){
        return( equal( a, 0 ) );
    }
    
    public static final double power( double a, double b ){
        return java.lang.Math.pow(a, b);
    }
    
    public static final int square(int a){
        return (a * a);
    }
    
    public static final long square(long a){
        return (a * a);
    }
    
    public static final float square(float a){
        return (a * a);
    }
    
    public static final double square(double a){
        return (a * a);
    }
    
    public static final int squareRoot( int a )
    {
        return (int) java.lang.Math.sqrt(a);
    }
    
    public static final long squareRoot( long a )
    {
        return (long) java.lang.Math.sqrt(a);
    }
    
    public static final float squareRoot( float a )
    {
        return (float) java.lang.Math.sqrt(a);
    }
    
    public static final double squareRoot( double a )
    {
        return java.lang.Math.sqrt(a);
    }
    
    public static final int Pythag( int a, int b )
    {
        int abs_a = abs( a );
        int abs_b = abs( b );

        if ( abs_a > abs_b )
        {
            return( abs_a * (int)( java.lang.Math.sqrt( 1.0 + sqr( abs_b / abs_a ) ) ) );
        }
        else
        {
            return( isZero( abs_b ) ? (int)( 0 ) :
                    abs_b * (int)( java.lang.Math.sqrt( 1.0 + sqr( abs_a / abs_b ) ) ) );
        }
    }
    
    public static final int sqr(int a){
        return (a * a);
    }
    
    public static final long sqr(long a){
        return (a * a);
    }
    
    public static final float sqr(float a){
        return (a * a);
    }
    
    public static final double sqr(double a){
        return (a * a);
    }
    
    public static final int Log2Largest( int n )
    {
        /*
           MessageAssert( sizeof( unsigned int ) == 4,
           "UTIL_LOG2_LARGEST allows to use on 32 bit CPU only.");
         */
        int t;
        int log2;

        if ( n >= 0x10000 ) { log2 = 16; t = 0x1000000; }
        else                { log2 = 0;  t = 0x100;     }

        if ( n >= t ) { log2 += 8; t <<= 4; }
        else          {            t >>= 4; }

        if ( n >= t ) { log2 += 4; t <<= 2; }
        else          {            t >>= 2; }

        if ( n >= t ) { log2 += 2; t <<= 1; }
        else          {            t >>= 1; }

        if ( n >= t ) { log2 += 1; }

        return( t );
    }
    
    public static final int Log2Smallest(int n )
    {
        /*
           MessageAssert( sizeof( unsigned int ) == 4,
           "UTIL_LOG2_SMALLEST allows to use on 32 bit CPU only.");
         */
        if ( n > 0x80000000 ) { return( 32 ); }

        int t;
        int log2;

        if ( n > 0x8000 ) { log2 = 16; t = 0x800000; }
        else              { log2 = 0;  t = 0x80;     }

        if ( n > t ) { log2 += 8; t <<= 4; }
        else         {            t >>= 4; }

        if ( n > t ) { log2 += 4; t <<= 2; }
        else         {            t >>= 2; }

        if ( n > t ) { log2 += 2; t <<= 1; }
        else         {            t >>= 1; }

        if ( n > t ) { log2 += 1; }

        return( log2 );
    }
    
    public static final int Power2Largest( int n )
    {
        return( 1 << Log2Largest( n ) );
    }

    public static final int Power2Smallest( int n )
    {
        return( 1 << Log2Smallest( n ) );
    }
    
    
    /*
    public static boolean IsPower2( int n )
    {
        return( !( n & ( n - 1 ) ) );
    }
    */
    
    public static final float  Deg2Rad( float a )
    {
        return( a * 0.01745329252F );
    }

    public static final double Deg2Rad( double a )
    {
        return( a * 0.01745329252 );
    }

    public static final float Rad2Deg( float a )
    {
        return( a * 57.29577951F );
    }

    public static final double Rad2Deg( double a )
    {
        return( a * 57.29577951 );
    }
    
    
}
