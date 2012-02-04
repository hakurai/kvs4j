package kvs.core.numeric;

import java.io.Serializable;

import kvs.core.matrix.Matrix33d;
import kvs.core.matrix.Matrix44d;
import kvs.core.matrix.Vector3d;

public final class Quaterniond implements Serializable{
    
    private static final long serialVersionUID = -5884606316368845210L;

    private final double[] m_elements = new double[4];
    
    public static final Quaterniond IDENTITY = new Quaterniond(0d, 0d, 0d, 1d);

    public Quaterniond()
    {
        m_elements[0] = 0d;
        m_elements[1] = 0d;
        m_elements[2] = 0d;
        m_elements[3] = 0d;
    }

    public Quaterniond( double x, double y, double z, double w )
    {
        m_elements[0] = x;
        m_elements[1] = y;
        m_elements[2] = z;
        m_elements[3] = w;
    }

    

    public Quaterniond( final Vector3d axis, double angle )
    {
        double s = ( java.lang.Math.sin( angle * 0.5 ) );
        double w = ( java.lang.Math.cos( angle * 0.5 ) );

        Vector3d n = axis;
        n = n.normalize();

        Vector3d v = n.mul( s );

        m_elements[0] = v.getX();
        m_elements[1] = v.getY();
        m_elements[2] = v.getZ();
        m_elements[3] = w;
    }

    public Quaterniond( final Matrix33d m )
    {
        double trace =  m.trace() + 1.0 ;

        double x, y, z, w;
        if( trace >= 1.0 )
        {
            double sqrt_trace = java.lang.Math.sqrt( trace );

            x = ( m.get( 1, 2 ) - m.get( 2, 1 ) ) * 0.5 / sqrt_trace;
            y = ( m.get( 2, 0 ) - m.get( 0, 2 ) ) * 0.5 / sqrt_trace;
            z = ( m.get( 0, 1 ) - m.get( 1, 0 ) ) * 0.5 / sqrt_trace;
            w = sqrt_trace * 0.5;
        }
        else
        {
            if( m.get( 0, 0)> m.get( 1, 1 ) && m.get( 0, 0 ) > m.get( 2, 2 ) )
            {
                x = java.lang.Math.sqrt( ( m.get( 0, 0 ) - m.get( 1, 1 ) - m.get( 2, 2 ) ) + 1.0 ) * 0.5 ;
                y = ( m.get( 0, 1 ) + m.get( 1, 0 ) ) * 0.25 / x;
                z = ( m.get( 0, 2 ) + m.get( 2, 0 ) ) * 0.25 / x;
                w = ( m.get( 1, 2 ) + m.get( 2, 1 ) ) * 0.25 / x;
            }
            else if( m.get( 1, 1 ) > m.get( 2, 2 ) )
            {
                y = java.lang.Math.sqrt( ( m.get( 1, 1 ) - m.get( 2, 2 ) - m.get( 0, 0 ) ) + 1.0 ) * 0.5 ;
                z = ( m.get( 1, 2 ) + m.get( 2, 1 ) ) * 0.25 / y;
                x = ( m.get( 1, 0 ) + m.get( 0, 1 ) ) * 0.25 / y;
                w = ( m.get( 2, 0 ) + m.get( 0, 2 ) ) * 0.25 / y;
            }
            else
            {
                z = java.lang.Math.sqrt( ( m.get( 2, 2 ) - m.get( 0, 0 ) - m.get( 1, 1 ) ) + 1.0 ) * 0.5 ;
                x = ( m.get( 2, 0 ) + m.get( 0, 2 ) ) * 0.25 / z;
                y = ( m.get( 2, 1 ) + m.get( 1, 2 ) ) * 0.25 / z;
                w = ( m.get( 0, 1 ) + m.get( 1, 0 ) ) * 0.25 / z;
            }
        }

        m_elements[0] = x;
        m_elements[1] = y;
        m_elements[2] = z;
        m_elements[3] = w;
    }


    public Quaterniond( final Matrix44d m )
    {
        double trace =  m.trace();

        double x, y, z, w;
        if( trace >= 1.0 )
        {
            double sqrt_trace = java.lang.Math.sqrt( trace );

            x = ( m.get( 1, 2 ) - m.get( 2, 1 ) ) * 0.5 / sqrt_trace;
            y = ( m.get( 2, 0 ) - m.get( 0, 2 ) ) * 0.5 / sqrt_trace;
            z = ( m.get( 0, 1 ) - m.get( 1, 0 ) ) * 0.5 / sqrt_trace;
            w = sqrt_trace * 0.5;
        }
        else
        {
            if( m.get( 0, 0)> m.get( 1, 1 ) && m.get( 0, 0 ) > m.get( 2, 2 ) )
            {
                x = java.lang.Math.sqrt( ( m.get( 0, 0 ) - m.get( 1, 1 ) - m.get( 2, 2 ) ) + 1.0 ) * 0.5 ;
                y = ( m.get( 0, 1 ) + m.get( 1, 0 ) ) * 0.25 / x;
                z = ( m.get( 0, 2 ) + m.get( 2, 0 ) ) * 0.25 / x;
                w = ( m.get( 1, 2 ) + m.get( 2, 1 ) ) * 0.25 / x;
            }
            else if( m.get( 1, 1 ) > m.get( 2, 2 ) )
            {
                y = java.lang.Math.sqrt( ( m.get( 1, 1 ) - m.get( 2, 2 ) - m.get( 0, 0 ) ) + 1.0 ) * 0.5 ;
                z = ( m.get( 1, 2 ) + m.get( 2, 1 ) ) * 0.25 / y;
                x = ( m.get( 1, 0 ) + m.get( 0, 1 ) ) * 0.25 / y;
                w = ( m.get( 2, 0 ) + m.get( 0, 2 ) ) * 0.25 / y;
            }
            else
            {
                z = java.lang.Math.sqrt( ( m.get( 2, 2 ) - m.get( 0, 0 ) - m.get( 1, 1 ) ) + 1.0 ) * 0.5 ;
                x = ( m.get( 2, 0 ) + m.get( 0, 2 ) ) * 0.25 / z;
                y = ( m.get( 2, 1 ) + m.get( 1, 2 ) ) * 0.25 / z;
                w = ( m.get( 0, 1 ) + m.get( 1, 0 ) ) * 0.25 / z;
            }
        }
        
        m_elements[0] = x;
        m_elements[1] = y;
        m_elements[2] = z;
        m_elements[3] = w;
    }

    public double getX()
    {
        return( m_elements[0] );
    }


    public double getY()
    {
        return( m_elements[1] );
    }

    public double getZ()
    {
        return( m_elements[2] );
    }

    public double getW()
    {
        return( m_elements[3] );
    }

    public Quaterniond conjunction()
    {
        return new Quaterniond(m_elements[0] * -1d,
                m_elements[1] * -1d,
                m_elements[2] * -1d,
                m_elements[3]);
    }

    public Quaterniond normalize()
    {
        double n = this.length();
        n = n > 0d ? 1d / n : 0d;

        return new Quaterniond(m_elements[0] * n,
                m_elements[1] * n,
                m_elements[2] * n,
                m_elements[3] * n);
    }
    

    public Quaterniond inverse()
    {
        double n = this.length2();

        if( n > 0d ){
            return new Quaterniond(
                    m_elements[0] / -n,
                    m_elements[1] / -n,
                    m_elements[2] / -n,
                    m_elements[3] /  n);
        } else {
            return( this );
        }

    }
    
    public Quaterniond log()
    {
        double x;
        double y;
        double z;
        double w;
        
        double theta     = java.lang.Math.acos( m_elements[3] );
        double sin_theta = java.lang.Math.sin( theta );

        w = 0d;

        if( sin_theta > 0 )
        {
            x = theta * m_elements[0] / sin_theta ;
            y = theta * m_elements[1] / sin_theta ;
            z = theta * m_elements[2] / sin_theta ;
        }
        else
        {
            x = 0d;
            y = 0d;
            z = 0d;
        }

        return new Quaterniond(x, y, z, w);
    }
    
    public Quaterniond exp()
    {
        double x;
        double y;
        double z;
        double w;
        
        double theta2 = 0.0;
        theta2 += m_elements[0] * m_elements[0];
        theta2 += m_elements[1] * m_elements[1];
        theta2 += m_elements[2] * m_elements[2];

        double theta     = java.lang.Math.sqrt( theta2 );
        double cos_theta = java.lang.Math.cos( theta );

        w = ( cos_theta );

        if( theta > 0 )
        {
            double sin_theta = java.lang.Math.sin( theta );
            x = ( sin_theta * m_elements[0] / theta );
            y = ( sin_theta * m_elements[1] / theta );
            z = ( sin_theta * m_elements[2] / theta );
        }
        else
        {
            x = ( 0 );
            y = ( 0 );
            z = ( 0 );
        }

        return new Quaterniond(x, y, z, w);
    }
    
    public double dot( final Quaterniond q )
    {
        double result = 0.0;
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
    public Matrix33d toMatrix33d()
    {
        double length_2 = ( this.length2() );
        double s = ( length_2 > (0d) ) ? 2d / length_2 : 0d;

        double xx = this.getX() * this.getX() * s;
        double yy = this.getY() * this.getY() * s;
        double zz = this.getZ() * this.getZ() * s;
        double xy = this.getX() * this.getY() * s;
        double xz = this.getX() * this.getZ() * s;
        double yz = this.getY() * this.getZ() * s;
        double wx = this.getW() * this.getX() * s;
        double wy = this.getW() * this.getY() * s;
        double wz = this.getW() * this.getZ() * s;
        
        Matrix33d ret = new Matrix33d(1d - ( yy + zz ), xy - wz, xz + wy,
        								 xy + wz, 1d - ( xx + zz ), yz - wx,
        								 xz - wy, yz + wx, 1d - ( xx + yy ));

        return ret;
        
    }
    
    /**
     * 
     * この四元数を回転行列に変換し、指定された行列に格納します。
     * 
     * @param m 回転行列の格納先
     *
     */
    public Matrix44d toMatrix44d( Matrix44d m )
    {
        double length_2 = ( this.length2() );
        double s = ( length_2 > (0d) ) ? 2d / length_2 : 0d;

        double xx = this.getX() * this.getX() * s;
        double yy = this.getY() * this.getY() * s;
        double zz = this.getZ() * this.getZ() * s;
        double xy = this.getX() * this.getY() * s;
        double xz = this.getX() * this.getZ() * s;
        double yz = this.getY() * this.getZ() * s;
        double wx = this.getW() * this.getX() * s;
        double wy = this.getW() * this.getY() * s;
        double wz = this.getW() * this.getZ() * s;
        
        Matrix44d ret = new Matrix44d(1d - ( yy + zz ), xy - wz, xz + wy, 0d,
                                      xy + wz, 1d - ( xx + zz ), yz - wx, 0d,
                                      xz - wy, yz + wx, 1d - ( xx + yy ), 0d,
                                      0d, 0d, 0d, 1d);
        
        return ret;
    }
    
    /**
     * 
     * この四元数を回転行列に変換し、配列で返します。
     *
     */
    public double[] toMatrix()
    {
        double length_2 = ( this.length2() );
        double s = ( length_2 > (0d) ) ? 2d / length_2 : 0d;
        
        double[] m = new double[16];

        double xx = this.getX() * this.getX() * s;
        double yy = this.getY() * this.getY() * s;
        double zz = this.getZ() * this.getZ() * s;
        double xy = this.getX() * this.getY() * s;
        double xz = this.getX() * this.getZ() * s;
        double yz = this.getY() * this.getZ() * s;
        double wx = this.getW() * this.getX() * s;
        double wy = this.getW() * this.getY() * s;
        double wz = this.getW() * this.getZ() * s;

        m[0]  = 1d - ( yy + zz );
        m[1]  = xy + wz;
        m[2]  = xz - wy;
        m[3]  = 0d;

        m[4]  = xy - wz;
        m[5]  = 1d - ( xx + zz );
        m[6]  = yz + wx;
        m[7]  = 0d;

        m[8]  = xz + wy;
        m[9]  = yz - wx;
        m[10] = 1d - ( xx + yy );
        m[11] = 0d;

        m[12] = 0d;
        m[13] = 0d;
        m[14] = 0d;
        m[15] = 1d;
        
        return m;
    }
    
    /**
     * 
     * この四元数の軸を返します。
     * 
     * @return 四元数の軸
     *
     */
    public Vector3d axis()
    {
        double s = ( java.lang.Math.sin( ( this.angle() ) * 0.5 ) );

        Vector3d ret = new Vector3d( this.getX(), this.getY(), this.getZ() );
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
    public double angle()
    {
        return( java.lang.Math.acos( this.getW() ) * 2.0 );
    }
    
    /**
     * 
     * 
     * @return 回転後の位置
     *
     */
    public static Vector3d rotate( final Vector3d pos, final Vector3d axis, double rad )
    {
        final Quaterniond p = new Quaterniond( pos.getX(), pos.getY(), pos.getZ(), 0d );
        final Quaterniond rotate_quat = new Quaterniond( axis, rad );
        final Quaterniond rotate_conj = rotate_quat.conjunction();
        final Quaterniond rotate_pos = rotate_conj.mul( p ).mul(rotate_quat);

        return( new Vector3d( rotate_pos.getX(), rotate_pos.getY(), rotate_pos.getZ() ) );
    }
    
    /**
     * 
     * 
     * @return 回転後の位置
     *
     */
    public static Vector3d rotate( final Vector3d pos, final Quaterniond q )
    {
        final Quaterniond p = new Quaterniond( pos.getX(), pos.getY(), pos.getZ(), 0d );
        final Quaterniond rotate_conj = q.conjunction();
        final Quaterniond rotate_pos = rotate_conj.mul( p).mul( q );

        return( new Vector3d( rotate_pos.getX(), rotate_pos.getY(), rotate_pos.getZ() ) );
    }
    
    public static Quaterniond rotationQuaternion( Vector3d v0, Vector3d v1 )
    {
        v0 = v0.normalize();
        v1 = v1.normalize();

        Vector3d c = v0.cross( v1 );
        double   d = v0.getX() * v1.getX() + v0.getY() * v1.getY() + v0.getZ() * v1.getZ();
        double   s = java.lang.Math.sqrt( ( 1 + d ) * 2.0 );

        double x = ( c.getX() / s );
        double y = ( c.getY() / s );
        double z = ( c.getZ() / s );
        double w = ( s / 2.0 );
        
        return new Quaterniond( x, y, z, w );

    }
    
    public static Quaterniond linearInterpolation(
            final Quaterniond q1,
            final Quaterniond q2,
            double            t,
            boolean           for_rotation )
        {
            Quaterniond ret = ( q2.sub( q1 ) ).mul( t ).add( q1 );

            if ( for_rotation ) {
                ret = ret.normalize();
                }

            return( ret );
        }

    public static Quaterniond sphericalLinearInterpolation(
            final Quaterniond q1,
            final Quaterniond q2,
            double            t,
            boolean           invert,
            boolean           for_rotation )
    {
        Quaterniond tmp1 = q1.normalize();
        Quaterniond tmp2 = q2.normalize();

        double dot = tmp1.dot( tmp2 );

        Quaterniond q3;
        // dot = cos( theta )
        // if (dot < 0), q1 and q2 are more than 90 degrees apart,
        // so we can invert one to reduce spining

        if( invert && dot < 0 ){
            dot = -dot;
            q3 = q2.mul( -1d );
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
            Quaterniond ans1 = q1.mul( sinaomt );
            Quaterniond ans2 = q3.mul( sinat );
            Quaterniond ans3 = ans1.add( ans2 );
            return ans3.div( sina );
        }
        // if the angle is small, use linear interpolation
        else
        {
            return( linearInterpolation( q1, q3, t, for_rotation ) );
        }
    }

    public static Quaterniond sphericalCubicInterpolation(
            final Quaterniond q1,
            final Quaterniond q2,
            final Quaterniond a,
            final Quaterniond b,
            double            t,
            boolean           for_rotation )
        {
            Quaterniond c = sphericalLinearInterpolation( q1, q2, t, false, for_rotation );
            Quaterniond d = sphericalLinearInterpolation(  a,  b, t, false, for_rotation );

            return( sphericalLinearInterpolation( c, d, 2.0 * t * (1-t), false, for_rotation ) );
        }
    
    public static Quaterniond splineInterpolation(
            final Quaterniond q1,
            final Quaterniond q2,
            final Quaterniond q3,
            final Quaterniond q4,
            double            t,
            boolean           for_rotation )
        {
            Quaterniond a = spline( q1, q2, q3 );
            Quaterniond b = spline( q2, q3, q4 );

            return( sphericalCubicInterpolation( q2, q3, a, b, t, for_rotation ) );
        }
    
    public static Quaterniond spline(
            final Quaterniond qnm1,
            final Quaterniond qn,
            final Quaterniond qnp1 )
        {
        
            Quaterniond tmpm1 = qnm1.normalize();
            Quaterniond tmpp1 = qnp1.normalize();

            Quaterniond qni = qn.conjunction();
            qni = qni.normalize();
            
            // ( qn * ( ( ( qni * tmpm1 ).log() + ( qni * tmpp1 ).log() ) / -4 ).exp() )
            
            Quaterniond ans1 = qni.mul( tmpm1 ).log();
            Quaterniond ans2 = qni.mul( tmpp1 ).log();
            
            Quaterniond ans3 = ans1.add( ans2 );
            ans3 = ans3.div( -4d );
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
    public Quaterniond add( final Quaterniond q ){
        return new Quaterniond(this.m_elements[0] + q.m_elements[0],
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
    public Quaterniond sub( final Quaterniond q ){
        return new Quaterniond(this.m_elements[0] - q.m_elements[0],
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
    public Quaterniond mul( final double a ){
        return new Quaterniond(this.m_elements[0] * a,
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
    public Quaterniond mul( final Quaterniond q )
    {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        double w = this.getW();

        return new Quaterniond( w * q.getX() + x * q.getW() + y * q.getZ() - z * q.getY(),
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
    public Quaterniond div( final double a ) {
        return new Quaterniond(this.m_elements[0] / a,
                this.m_elements[1] / a,
                this.m_elements[2] / a,
                this.m_elements[3] / a);
    }
    
    
    /**
     * 
     * このベクトルと指定されたオブジェクトを比較します。
     * このオブジェクトと同じ四元数を表すQuaterniondオブジェクトである場合にだけtrueを返します。
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
        if (!(obj instanceof Quaterniond)) {
            return false;
        }
        Quaterniond q = (Quaterniond) obj;
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
        double result = 17;
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
