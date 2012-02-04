package kvs.core.numeric;

import java.io.Serializable;

import kvs.core.matrix.Matrix33f;
import kvs.core.matrix.Matrix44f;
import kvs.core.matrix.Vector3f;

public final class Quaternionf implements Serializable{
    
    private static final long serialVersionUID = -6625532193705697959L;

    private final float[] m_elements = new float[4];
    
    public static final Quaternionf IDENTITY = new Quaternionf(0f, 0f, 0f, 1f);

    public Quaternionf()
    {
        m_elements[0] = 0f;
        m_elements[1] = 0f;
        m_elements[2] = 0f;
        m_elements[3] = 0f;
    }

    public Quaternionf( float x, float y, float z, float w )
    {
        m_elements[0] = x;
        m_elements[1] = y;
        m_elements[2] = z;
        m_elements[3] = w;
    }

    

    public Quaternionf( final Vector3f axis, float angle )
    {
        float s = (float)( java.lang.Math.sin( angle * 0.5 ) );
        float w = (float)( java.lang.Math.cos( angle * 0.5) );

        Vector3f n = axis;
        n = n.normalize();

        Vector3f v = n.mul( s );

        m_elements[0] = v.getX();
        m_elements[1] = v.getY();
        m_elements[2] = v.getZ();
        m_elements[3] = w;
    }

    public Quaternionf( final Matrix33f m )
    {
        double trace =  m.trace() + 1.0 ;

        float x, y, z, w;
        if( trace >= 1.0 )
        {
            double sqrt_trace = java.lang.Math.sqrt( trace );

            x = (float)(( m.get( 1, 2 ) - m.get( 2, 1 ) ) * 0.5 / sqrt_trace);
            y = (float)(( m.get( 2, 0 ) - m.get( 0, 2 ) ) * 0.5 / sqrt_trace);
            z = (float)(( m.get( 0, 1 ) - m.get( 1, 0 ) ) * 0.5 / sqrt_trace);
            w = (float)(sqrt_trace * 0.5);
        }
        else
        {
            if( m.get( 0, 0)> m.get( 1, 1 ) && m.get( 0, 0 ) > m.get( 2, 2 ) )
            {
                x = (float)(java.lang.Math.sqrt( ( m.get( 0, 0 ) - m.get( 1, 1 ) - m.get( 2, 2 ) ) + 1.0 ) * 0.5) ;
                y = (float)(( m.get( 0, 1 ) + m.get( 1, 0 ) ) * 0.25 / x);
                z = (float)(( m.get( 0, 2 ) + m.get( 2, 0 ) ) * 0.25 / x);
                w = (float)(( m.get( 1, 2 ) + m.get( 2, 1 ) ) * 0.25 / x);
            }
            else if( m.get( 1, 1 ) > m.get( 2, 2 ) )
            {
                y = (float)(java.lang.Math.sqrt( ( m.get( 1, 1 ) - m.get( 2, 2 ) - m.get( 0, 0 ) ) + 1.0 ) * 0.5) ;
                z = (float)(( m.get( 1, 2 ) + m.get( 2, 1 ) ) * 0.25 / y);
                x = (float)(( m.get( 1, 0 ) + m.get( 0, 1 ) ) * 0.25 / y);
                w = (float)(( m.get( 2, 0 ) + m.get( 0, 2 ) ) * 0.25 / y);
            }
            else
            {
                z = (float)(java.lang.Math.sqrt( ( m.get( 2, 2 ) - m.get( 0, 0 ) - m.get( 1, 1 ) ) + 1.0 ) * 0.5) ;
                x = (float)(( m.get( 2, 0 ) + m.get( 0, 2 ) ) * 0.25 / z);
                y = (float)(( m.get( 2, 1 ) + m.get( 1, 2 ) ) * 0.25 / z);
                w = (float)(( m.get( 0, 1 ) + m.get( 1, 0 ) ) * 0.25 / z);
            }
        }

        m_elements[0] = x;
        m_elements[1] = y;
        m_elements[2] = z;
        m_elements[3] = w;
    }


    public Quaternionf( final Matrix44f m )
    {
        double trace =  m.trace();

        float x, y, z, w;
        if( trace >= 1.0 )
        {
            double sqrt_trace = java.lang.Math.sqrt( trace );

            x = (float)(( m.get( 1, 2 ) - m.get( 2, 1 ) ) * 0.5 / sqrt_trace);
            y = (float)(( m.get( 2, 0 ) - m.get( 0, 2 ) ) * 0.5 / sqrt_trace);
            z = (float)(( m.get( 0, 1 ) - m.get( 1, 0 ) ) * 0.5 / sqrt_trace);
            w = (float)(sqrt_trace * 0.5);
        }
        else
        {
            if( m.get( 0, 0)> m.get( 1, 1 ) && m.get( 0, 0 ) > m.get( 2, 2 ) )
            {
                x = (float)(java.lang.Math.sqrt( ( m.get( 0, 0 ) - m.get( 1, 1 ) - m.get( 2, 2 ) ) + 1.0 ) * 0.5) ;
                y = (float)(( m.get( 0, 1 ) + m.get( 1, 0 ) ) * 0.25 / x);
                z = (float)(( m.get( 0, 2 ) + m.get( 2, 0 ) ) * 0.25 / x);
                w = (float)(( m.get( 1, 2 ) + m.get( 2, 1 ) ) * 0.25 / x);
            }
            else if( m.get( 1, 1 ) > m.get( 2, 2 ) )
            {
                y = (float)(java.lang.Math.sqrt( ( m.get( 1, 1 ) - m.get( 2, 2 ) - m.get( 0, 0 ) ) + 1.0 ) * 0.5) ;
                z = (float)(( m.get( 1, 2 ) + m.get( 2, 1 ) ) * 0.25 / y);
                x = (float)(( m.get( 1, 0 ) + m.get( 0, 1 ) ) * 0.25 / y);
                w = (float)(( m.get( 2, 0 ) + m.get( 0, 2 ) ) * 0.25 / y);
            }
            else
            {
                z = (float)(java.lang.Math.sqrt( ( m.get( 2, 2 ) - m.get( 0, 0 ) - m.get( 1, 1 ) ) + 1.0 ) * 0.5) ;
                x = (float)(( m.get( 2, 0 ) + m.get( 0, 2 ) ) * 0.25 / z);
                y = (float)(( m.get( 2, 1 ) + m.get( 1, 2 ) ) * 0.25 / z);
                w = (float)(( m.get( 0, 1 ) + m.get( 1, 0 ) ) * 0.25 / z);
            }
        }
        
        m_elements[0] = x;
        m_elements[1] = y;
        m_elements[2] = z;
        m_elements[3] = w;
    }

    public float getX()
    {
        return( m_elements[0] );
    }


    public float getY()
    {
        return( m_elements[1] );
    }

    public float getZ()
    {
        return( m_elements[2] );
    }

    public float getW()
    {
        return( m_elements[3] );
    }

    public Quaternionf conjunction()
    {
        return new Quaternionf(m_elements[0] * -1f,
                m_elements[1] * -1f,
                m_elements[2] * -1f,
                m_elements[3]);
    }

    public Quaternionf normalize()
    {
        float n = (float)this.length();
        n = n > 0f ? 1f / n : 0f;

        return new Quaternionf(m_elements[0] * n,
                m_elements[1] * n,
                m_elements[2] * n,
                m_elements[3] * n);
    }
    

    public Quaternionf inverse()
    {
        float n = (float)this.length2();

        if( n > 0d ){
            return new Quaternionf(
                    m_elements[0] / -n,
                    m_elements[1] / -n,
                    m_elements[2] / -n,
                    m_elements[3] /  n);
        } else {
            return( this );
        }

    }
    
    public Quaternionf log()
    {
        float x;
        float y;
        float z;
        float w;
        
        double theta     = java.lang.Math.acos( m_elements[3] );
        double sin_theta = java.lang.Math.sin( theta );

        w = 0f;

        if( sin_theta > 0 )
        {
            x = (float)(theta * m_elements[0] / sin_theta) ;
            y = (float)(theta * m_elements[1] / sin_theta) ;
            z = (float)(theta * m_elements[2] / sin_theta) ;
        }
        else
        {
            x = 0f;
            y = 0f;
            z = 0f;
        }

        return new Quaternionf(x, y, z, w);
    }
    
    public Quaternionf exp()
    {
        float x;
        float y;
        float z;
        float w;
        
        double theta2 = 0.0d;
        theta2 += m_elements[0] * m_elements[0];
        theta2 += m_elements[1] * m_elements[1];
        theta2 += m_elements[2] * m_elements[2];

        double theta     = java.lang.Math.sqrt( theta2 );
        double cos_theta = java.lang.Math.cos( theta );

        w = (float)( cos_theta );

        if( theta > 0 )
        {
            double sin_theta = java.lang.Math.sin( theta );
            x = (float)( sin_theta * m_elements[0] / theta );
            y = (float)( sin_theta * m_elements[1] / theta );
            z = (float)( sin_theta * m_elements[2] / theta );
        }
        else
        {
            x = ( 0.0f );
            y = ( 0.0f );
            z = ( 0.0f );
        }

        return new Quaternionf(x, y, z, w);
    }
    
    public float dot( final Quaternionf q )
    {
        float result = 0.0f;
        result += this.getX() * q.getX();
        result += this.getY() * q.getY();
        result += this.getZ() * q.getZ();
        result += this.getW() * q.getW();

        return( result );
    }
    
    public double length()
    {
        return( java.lang.Math.sqrt( this.length2() ) );
    }
    
    public double length2()
    {
        double result = 0.0;
        result += m_elements[0] * m_elements[0];
        result += m_elements[1] * m_elements[1];
        result += m_elements[2] * m_elements[2];
        result += m_elements[3] * m_elements[3];

        return( result );
    }
    
    
    /**
     * 
     * この四元数を回転行列に変換し、指定された行列に格納します。
     *
     */
    public Matrix33f toMatrix33f()
    {
        float length_2 = (float)( this.length2() );
        float s = ( length_2 > (0f) ) ? 2f / length_2 : 0f;

        float xx = this.getX() * this.getX() * s;
        float yy = this.getY() * this.getY() * s;
        float zz = this.getZ() * this.getZ() * s;
        float xy = this.getX() * this.getY() * s;
        float xz = this.getX() * this.getZ() * s;
        float yz = this.getY() * this.getZ() * s;
        float wx = this.getW() * this.getX() * s;
        float wy = this.getW() * this.getY() * s;
        float wz = this.getW() * this.getZ() * s;
        
        Matrix33f ret = new Matrix33f(1f - ( yy + zz ), xy - wz, xz + wy,
                                         xy + wz, 1f - ( xx + zz ), yz - wx,
                                         xz - wy, yz + wx, 1f - ( xx + yy ));

        return ret;
        
    }
    
    /**
     * 
     * この四元数を回転行列に変換し、指定された行列に格納します。
     * 
     * @param m 回転行列の格納先
     *
     */
    public Matrix44f toMatrix44f( Matrix44f m )
    {
        float length_2 = (float)( this.length2() );
        float s = ( length_2 > (0f) ) ? 2f / length_2 : 0f;

        float xx = this.getX() * this.getX() * s;
        float yy = this.getY() * this.getY() * s;
        float zz = this.getZ() * this.getZ() * s;
        float xy = this.getX() * this.getY() * s;
        float xz = this.getX() * this.getZ() * s;
        float yz = this.getY() * this.getZ() * s;
        float wx = this.getW() * this.getX() * s;
        float wy = this.getW() * this.getY() * s;
        float wz = this.getW() * this.getZ() * s;
        
        Matrix44f ret = new Matrix44f(1f - ( yy + zz ), xy - wz, xz + wy, 0f,
                                      xy + wz, 1f - ( xx + zz ), yz - wx, 0f,
                                      xz - wy, yz + wx, 1f - ( xx + yy ), 0f,
                                      0f, 0f, 0f, 1f);
        
        return ret;
    }
    
    /**
     * 
     * この四元数を回転行列に変換し、配列で返します。
     *
     */
    public float[] toMatrix()
    {
        float length_2 = (float)( this.length2() );
        float s = ( length_2 > (0.0f) ) ? 2.0f / length_2 : 0.0f;
        
        float[] m = new float[16];

        float xx = this.getX() * this.getX() * s;
        float yy = this.getY() * this.getY() * s;
        float zz = this.getZ() * this.getZ() * s;
        float xy = this.getX() * this.getY() * s;
        float xz = this.getX() * this.getZ() * s;
        float yz = this.getY() * this.getZ() * s;
        float wx = this.getW() * this.getX() * s;
        float wy = this.getW() * this.getY() * s;
        float wz = this.getW() * this.getZ() * s;

        m[0]  = 1f - ( yy + zz );
        m[1]  = xy + wz;
        m[2]  = xz - wy;
        m[3]  = 0f;

        m[4]  = xy - wz;
        m[5]  = 1f - ( xx + zz );
        m[6]  = yz + wx;
        m[7]  = 0f;

        m[8]  = xz + wy;
        m[9]  = yz - wx;
        m[10] = 1f - ( xx + yy );
        m[11] = 0f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = 0f;
        m[15] = 1f;
        
        return m;
    }
    
    /**
     * 
     * この四元数の軸を返します。
     * 
     * @return 四元数の軸
     *
     */
    public Vector3f axis()
    {
        float s = (float)( java.lang.Math.sin( ( this.angle() ) * 0.5 ) );

        Vector3f ret = new Vector3f( this.getX(), this.getY(), this.getZ() );
        ret.div( s );

        return( ret );
    }
    
    /**
     * 
     * この四元数の角度を返します。
     * 
     * @return 四元数の角度
     *
     */
    public float angle()
    {
        return(float)( java.lang.Math.acos( this.getW() ) * 2.0 );
    }
    
    /**
     * 
     * 
     * @return 回転後の位置
     *
     */
    public static Vector3f rotate( final Vector3f pos, final Vector3f axis, float rad )
    {
        final Quaternionf p = new Quaternionf( pos.getX(), pos.getY(), pos.getZ(), 0f );
        final Quaternionf rotate_quat = new Quaternionf( axis, rad );
        final Quaternionf rotate_conj = rotate_quat.conjunction();
        final Quaternionf rotate_pos = rotate_conj.mul( p ).mul(rotate_quat);

        return( new Vector3f( rotate_pos.getX(), rotate_pos.getY(), rotate_pos.getZ() ) );
    }
    
    /**
     * 
     * 
     * @return 回転後の位置
     *
     */
    public static Vector3f rotate( final Vector3f pos, final Quaternionf q )
    {
        final Quaternionf p = new Quaternionf( pos.getX(), pos.getY(), pos.getZ(), 0.0f );
        final Quaternionf rotate_conj = q.conjunction();
        final Quaternionf rotate_pos = rotate_conj.mul( p).mul( q );

        return( new Vector3f( rotate_pos.getX(), rotate_pos.getY(), rotate_pos.getZ() ) );
    }
    
    public static Quaternionf rotationQuaternion( Vector3f v0, Vector3f v1 )
    {
        v0 = v0.normalize();
        v1 = v1.normalize();

        Vector3f c = v0.cross( v1 );
        float   d = v0.getX() * v1.getX() + v0.getY() * v1.getY() + v0.getZ() * v1.getZ();
        float   s = (float) java.lang.Math.sqrt( ( 1.0f + d ) * 2.0f );

        float x = ( c.getX() / s );
        float y = ( c.getY() / s );
        float z = ( c.getZ() / s );
        float w = ( s / 2.0f );
        
        return new Quaternionf( x, y, z, w );

    }
    
    public static Quaternionf linearInterpolation(
            final Quaternionf q1,
            final Quaternionf q2,
            double            t,
            boolean           for_rotation )
        {
            Quaternionf ret = ( q2.sub( q1 ) ).mul( (float)t ).add( q1 );

            if ( for_rotation ) {
                ret = ret.normalize();
                }

            return( ret );
        }

    public static Quaternionf sphericalLinearInterpolation(
            final Quaternionf q1,
            final Quaternionf q2,
            double            t,
            boolean           invert,
            boolean           for_rotation )
    {
        Quaternionf tmp1 = q1.normalize();
        Quaternionf tmp2 = q2.normalize();

        double dot = tmp1.dot( tmp2 );

        Quaternionf q3;
        // dot = cos( theta )
        // if (dot < 0), q1 and q2 are more than 90 degrees apart,
        // so we can invert one to reduce spining

        if( invert && dot < 0 ){
            dot = -dot;
            q3 = q2.mul( -1f );
        } else {
            q3 = ( q2 );
        }

        if( (  invert && dot < 0.95 ) ||
                ( !invert && dot > -0.95 && dot < 0.95 ) )
        {
            double angle   = java.lang.Math.acos( dot );
            double sina    = java.lang.Math.sin( angle );
            double sinat   = java.lang.Math.sin( angle * t );
            double sinaomt = java.lang.Math.sin( angle * (1-t) );

            // ( q1 * T( sinaomt ) + q3 * T( sinat ) ) / T( sina )
            Quaternionf ans1 = q1.mul( (float)sinaomt );
            Quaternionf ans2 = q3.mul( (float)sinat );
            Quaternionf ans3 = ans1.add( ans2 );
            return ans3.div( (float)sina );
        }
        // if the angle is small, use linear interpolation
        else
        {
            return( linearInterpolation( q1, q3, t, for_rotation ) );
        }
    }

    public static Quaternionf sphericalCubicInterpolation(
            final Quaternionf q1,
            final Quaternionf q2,
            final Quaternionf a,
            final Quaternionf b,
            double            t,
            boolean           for_rotation )
        {
            Quaternionf c = sphericalLinearInterpolation( q1, q2, t, false, for_rotation );
            Quaternionf d = sphericalLinearInterpolation(  a,  b, t, false, for_rotation );

            return( sphericalLinearInterpolation( c, d, 2.0 * t * (1-t), false, for_rotation ) );
        }
    
    public static Quaternionf splineInterpolation(
            final Quaternionf q1,
            final Quaternionf q2,
            final Quaternionf q3,
            final Quaternionf q4,
            double            t,
            boolean           for_rotation )
        {
            Quaternionf a = spline( q1, q2, q3 );
            Quaternionf b = spline( q2, q3, q4 );

            return( sphericalCubicInterpolation( q2, q3, a, b, t, for_rotation ) );
        }
    
    public static Quaternionf spline(
            final Quaternionf qnm1,
            final Quaternionf qn,
            final Quaternionf qnp1 )
        {
        
            Quaternionf tmpm1 = qnm1.normalize();
            Quaternionf tmpp1 = qnp1.normalize();

            Quaternionf qni = qn.conjunction();
            qni = qni.normalize();
            
            // ( qn * ( ( ( qni * tmpm1 ).log() + ( qni * tmpp1 ).log() ) / -4 ).exp() )
            
            Quaternionf ans1 = qni.mul( tmpm1 ).log();
            Quaternionf ans2 = qni.mul( tmpp1 ).log();
            
            Quaternionf ans3 = ans1.add( ans2 );
            ans3 = ans3.div( -4f );
            ans3 = ans3.mul( qni );
            
            return ans3.exp();
        }
    
    
    /**
     * 
     * 指定された四元数との和を返します。
     * 
     * @param q 和を取る相手の四元数
     * @return (this + q)
     *
     */
    public Quaternionf add( final Quaternionf q ){
        return new Quaternionf(this.m_elements[0] + q.m_elements[0],
                this.m_elements[1] + q.m_elements[1],
                this.m_elements[2] + q.m_elements[2],
                this.m_elements[3] + q.m_elements[3]);
    }

    /**
     * 
     * 指定された四元数との差を取ります。
     * 
     * @param q 差を取る相手の四元数
     * @return (this - q)
     *
     */
    public Quaternionf sub( final Quaternionf q ){
        return new Quaternionf(this.m_elements[0] - q.m_elements[0],
                this.m_elements[1] - q.m_elements[1],
                this.m_elements[2] - q.m_elements[2],
                this.m_elements[3] - q.m_elements[3]);
    }

    /**
     * 
     * 指定された値でスカラー倍します。
     * 
     * @param a スカラー
     * @return (this * a)
     * @exception ArithmeticException
     *
     */
    public Quaternionf mul( final float a ){
        return new Quaternionf(this.m_elements[0] * a,
                this.m_elements[1] * a,
                this.m_elements[2] * a,
                this.m_elements[3] * a);
    }
    
    
    /**
     * 
     * 指定された四元数との積を取ります返します。
     * 
     * @param q 積を取る相手の四元数
     * @return (this * q)
     *
     */
    public Quaternionf mul( final Quaternionf q )
    {
        float x = this.getX();
        float y = this.getY();
        float z = this.getZ();
        float w = this.getW();

        return new Quaternionf( w * q.getX() + x * q.getW() + y * q.getZ() - z * q.getY(),
                   w * q.getY() - x * q.getZ() + y * q.getW() + z * q.getX(),
                   w * q.getZ() + x * q.getY() - y * q.getX() + z * q.getW(),
                   w * q.getW() - x * q.getX() - y * q.getY() - z * q.getZ() );

    }

    /**
     * 
     * 指定された値でスカラー倍します。
     * 
     * @param a スカラー
     * @return (this / a)
     * @exception ArithmeticException
     */
    public Quaternionf div( final float a ) {
        return new Quaternionf(this.m_elements[0] / a,
                this.m_elements[1] / a,
                this.m_elements[2] / a,
                this.m_elements[3] / a);
    }
    
    
    /**
     * 
     * このベクトルと指定されたオブジェクトを比較します。
     * このオブジェクトと同じ四元数を表すQuaternionfオブジェクトである場合にだけtrueを返します。
     * 
     * @param obj この四元数と比較されるオブジェクト
     * @return 四元数が等しい場合はtrue、そうでない場合はfalse
     * 
     */
    @Override
    public boolean equals( final Object obj ) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Quaternionf)) {
            return false;
        }
        Quaternionf q = (Quaternionf) obj;
        return ( kvs.core.util.Math.equal( this.getX(), q.getX()  ) &&
                 kvs.core.util.Math.equal( this.getY(), q.getY()  ) &&
                 kvs.core.util.Math.equal( this.getZ(), q.getZ()  ) &&
                 kvs.core.util.Math.equal( this.getW(), q.getW()  ));
    }
    
    /**
     * 
     * オブジェクトのハッシュコード値を返します。
     * 
     * @return このベクトルのハッシュコード値
     * 
     */
    @Override
    public int hashCode() {
        float result = 17;
        result = 31 * result + this.getX();
        result = 31 * result + this.getY();
        result = 31 * result + this.getZ();
        result = 31 * result + this.getW();
        return (int)result;
    }
    
    /**
     * 
     * このオブジェクトの文字列表現を返します。 
     * this.x
     * this.y
     * this.z
     * this.w
     * 
     * @return このオブジェクトの文字列表現
     * 
     */
    @Override
    public String toString() {
        String crlf = System.getProperty("line.separator");
        return
        this.getX() + crlf +
        this.getY() + crlf +
        this.getZ() + crlf +
        this.getW();
    }

}
