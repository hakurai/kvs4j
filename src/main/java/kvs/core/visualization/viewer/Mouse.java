package kvs.core.visualization.viewer;

import kvs.core.matrix.Vector2i;
import kvs.core.matrix.Vector3f;
import kvs.core.numeric.Quaternionf;
import kvs.core.util.Timer;

public class Mouse extends Trackball{
    
    public enum TransMode
    {
        Rotation,       ///< rotation mode
        Translation,    ///< translation mode
        Scaling         ///< scaling mode
        
    };

    public enum AutoMode
    {
        AutoOff,    ///< auto rotation mode off
        AutoOn      ///< auto rotation mode on
    };
    
    public static long  SpinTime = 20;          ///< spin time  mac=25
    public static float WheelUpValue = 1.05f;   ///< wheel up value
    public static float WheelDownValue = 0.95f; ///< wheel down value
    
    protected TransMode     m_mode;         ///< transform mode
    protected ScalingType   m_scaling_type; ///< scaling type
    protected Vector2i      m_old;          ///< old mouse position on the window coordinate (org: upper-left)
    protected Vector2i      m_new;          ///< new mouse position on the window coordinate (org: upper-left)
    protected Vector2i      m_start;        ///< position at start of rotation
    protected Vector2i      m_stop;         ///< position at stop of rotation
    protected Timer         m_timer;        ///< auto check timer
    protected boolean       m_is_auto;      ///< auto flag (true: if auto mode)
    protected boolean       m_is_slow;      ///< slow flag (true: if slow mode)
    protected boolean       m_is_use_auto;  ///< auto use flag (true: if user use auto mode )
    
    public Mouse(){
        this.reset();
        m_timer = new Timer();
        m_is_use_auto = true;
    }
    
    public Mouse( AutoMode auto_flag ){
        this();
        m_is_use_auto = (auto_flag == AutoMode.AutoOn);
    }
    
    @Override
    public void reset()
    {
        super.reset();
        m_mode = TransMode.Rotation;
        m_scaling_type = ScalingType.ScalingXYZ;
        m_old = Vector2i.ZERO;
        m_new = Vector2i.ZERO;
        m_start = Vector2i.ZERO;
        m_stop = Vector2i.ZERO;
        m_is_auto = false;
        m_is_slow = false;
    }
    
    public void press( final int x, final int y )
    {
        m_old   = new Vector2i( x, y );
        m_new   = new Vector2i( x, y );
        m_start = new Vector2i( x, y );

        m_timer.start();
        m_is_auto = false;
        m_is_slow = false;
    }
    
    public void move( final int x, final int y )
    {
        m_new = new Vector2i( x, y );

        switch( m_mode )
        {
        case Scaling:
            scale( m_old, m_new, m_scaling_type );
            break;
        case Rotation:
            rotate( m_old, m_new );
            break;
        case Translation:
            translate( m_old, m_new );
            break;
        default:
            break;
        }

        // Update the mouse cursor position.
        m_old = m_new;
    }
    
    public void wheel( final float value )
    {
        Vector3f scale = new Vector3f( 1.0f );

        switch( getScalingType() )
        {
        case ScalingXYZ: scale = scale.mul( value ); break;
        case ScalingX:   scale = new Vector3f(scale.getX() * value, scale.getY(), scale.getZ()); break;
        case ScalingY:   scale = new Vector3f(scale.getX(), scale.getY() * value, scale.getZ()); break;
        case ScalingZ:   scale = new Vector3f(scale.getX(), scale.getY(), scale.getZ() * value); break;
        case ScalingXY:  scale = new Vector3f(scale.getX() * value, scale.getY() * value, scale.getZ()); break;
        case ScalingYZ:  scale = new Vector3f(scale.getX(), scale.getY() * value, scale.getZ() * value); break;
        case ScalingZX:  scale = new Vector3f(scale.getX() * value, scale.getY(), scale.getZ() * value); break;
        default: break;
        }

        m_scale = scale;
        
        scale( m_old, m_new, getScalingType() );

        m_old = m_new;
    }
    
    public void release( final int x, final int y )
    {
        m_timer.stop();
        m_stop = new Vector2i( x, y );

        final double threshould_time    = 500.0;
        final double tolerance_auto_len = 2.0;
        final double tolerance_slow_len = 80.0;

        final double length = ( m_start.sub( m_stop ) ).length();

        if( m_timer.msec() < threshould_time )
        {
            if( length > tolerance_auto_len )
            {
                m_is_auto = true;
                m_is_slow = length < tolerance_slow_len;
            }
        }
    }
    
    public final boolean idle()
    {
        if( !( m_is_use_auto && m_is_auto ) ) return( false );

        if( m_is_slow )
        {
            switch( m_mode )
            {
            case Scaling:
            {
                final float big_scale   = 1.0003f;
                final float small_scale = 0.9997f;
                switch( getScalingType() )
                {

                case ScalingXYZ:
                {
                    if( m_scale.getX() < 1.0 )
                    {
                        m_scale = m_scale.mul( big_scale );
                        if( m_scale.getX() > 1.0 )
                        {
                            m_scale = new Vector3f( 1.0f );
                            m_is_slow = false;
                        }
                    }
                    else if( m_scale.getX() > 1.0 )
                    {
                        m_scale = m_scale.mul( small_scale );
                        if( m_scale.getX() < 1.0 )
                        {
                            m_scale = new Vector3f( 1.0f );
                            m_is_slow = false;
                        }
                    }
                    break;
                }

                case ScalingX:
                {
                    if( m_scale.getX() < 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX() * big_scale, m_scale.getY(), m_scale.getZ());
                        if( m_scale.getX() > 1.0 )
                        {
                            m_scale = new Vector3f( 1.0f, m_scale.getY(), m_scale.getZ());
                            m_is_slow = false;
                        }
                    }
                    else if( m_scale.getX() > 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX() * small_scale, m_scale.getY(), m_scale.getZ());
                        if( m_scale.getX() < 1.0 )
                        {
                            m_scale = new Vector3f( 1.0f, m_scale.getY(), m_scale.getZ());
                            m_is_slow = false;
                        }
                    }
                    break;
                }

                case ScalingY:
                {
                    if( m_scale.getY() < 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX(), m_scale.getY() * big_scale, m_scale.getZ());
                        if( m_scale.getY() > 1.0 )
                        {
                            m_scale = new Vector3f(  m_scale.getX(), 1.0f, m_scale.getZ());
                            m_is_slow = false;
                        }
                    }
                    else if( m_scale.getY() > 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX(), m_scale.getY() * small_scale, m_scale.getZ());
                        if( m_scale.getY() < 1.0 )
                        {
                            m_scale = new Vector3f( m_scale.getX(), 1.0f, m_scale.getZ());
                            m_is_slow = false;
                        }
                    }
                    break;
                }

                case ScalingZ:
                {
                    if( m_scale.getZ() < 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX(), m_scale.getY(), m_scale.getZ() * big_scale);
                        if( m_scale.getZ() > 1.0 )
                        {
                            m_scale = new Vector3f( m_scale.getX(), m_scale.getY(), 1.0f);
                            m_is_slow = false;
                        }
                    }
                    else if( m_scale.getZ() > 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX(), m_scale.getY(), m_scale.getZ() * small_scale);
                        if( m_scale.getZ() < 1.0 )
                        {
                            m_scale = new Vector3f( m_scale.getX(), m_scale.getY(), 1.0f);
                            m_is_slow = false;
                        }
                    }
                    break;
                }

                case ScalingXY:
                {
                    if( m_scale.getX() < 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX() * big_scale, m_scale.getY() * big_scale, m_scale.getZ());
                        if( m_scale.getX() > 1.0 )
                        {
                            m_scale = new Vector3f( 1.0f, 1.0f, m_scale.getZ());
                            m_is_slow = false;
                        }
                    }
                    else if( m_scale.getX() > 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX() * small_scale, m_scale.getY() * small_scale, m_scale.getZ());
                        if( m_scale.getX() < 1.0 )
                        {
                            m_scale = new Vector3f( 1.0f, 1.0f, m_scale.getZ());
                            m_is_slow = false;
                        }
                    }
                    break;
                }

                case ScalingYZ:
                {
                    if( m_scale.getY() < 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX(), m_scale.getY() * big_scale, m_scale.getZ() * big_scale );
                        if( m_scale.getY() > 1.0 )
                        {
                            m_scale = new Vector3f( m_scale.getX(), 1.0f, 1.0f );
                            m_is_slow = false;
                        }
                    }
                    else if( m_scale.getY() > 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX(), m_scale.getY() * small_scale, m_scale.getZ() * small_scale );
                        if( m_scale.getY() < 1.0 )
                        {
                            m_scale = new Vector3f( m_scale.getX(), 1.0f, 1.0f );
                            m_is_slow = false;
                        }
                    }
                    break;
                }

                case ScalingZX:
                {
                    if( m_scale.getZ() < 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX() * big_scale, m_scale.getY(), m_scale.getZ() * big_scale);
                        if( m_scale.getZ() > 1.0 )
                        {
                            m_scale = new Vector3f( 1.0f, m_scale.getY(), 1.0f );
                            m_is_slow = false;
                        }
                    }
                    else if( m_scale.getZ() > 1.0 )
                    {
                        m_scale = new Vector3f( m_scale.getX() * small_scale, m_scale.getY(), m_scale.getZ() * small_scale);
                        if( m_scale.getZ() < 1.0 )
                        {
                            m_scale = new Vector3f( 1.0f, m_scale.getY(), 1.0f );
                            m_is_slow = false;
                        }
                    }
                    break;
                }

                default: break;
                }
                break;
            } // Mouse::Scaling

            case Rotation:
            {   
                m_rot = new Quaternionf( m_rot.getX() * 0.9f, m_rot.getY() * 0.9f, m_rot.getZ() * 0.9f, m_rot.getW());
                break;
            }

            case Translation:
            {
                m_trans = m_trans.mul( 0.9f );
                if( m_trans.length() < 0.001 )
                {
                    m_trans   = new Vector3f( 0.0f );
                    m_is_slow = false;
                };
                break;
            }
            default:
                break;
            }
        }

        return( true );
    }
    
    public void setMode( final TransMode mode )
    {
        m_mode = mode;
    }
    
    public void setScalingType( final ScalingType type )
    {
        m_scaling_type = type;
    }
    
    public void setScalingType( final boolean x, final boolean y, final boolean z )
    {
        m_scaling_type = x ?
            ( y ? ( z ? ScalingType.ScalingXYZ : ScalingType.ScalingXY ) : ( z ? ScalingType.ScalingZX : ScalingType.ScalingX   ) ) :
            ( y ? ( z ? ScalingType.ScalingYZ  : ScalingType.ScalingY  ) : ( z ? ScalingType.ScalingZ  : ScalingType.ScalingNot ) );
    }
    
    public TransMode getMode()
    {
        return( m_mode );
    }
    
    public ScalingType getScalingType()
    {
        return( m_scaling_type );
    }
    
    public void setUseAuto( final boolean flg )
    {
        m_is_use_auto = flg;
    }
    
    public boolean isUseAuto()
    {
        return( m_is_use_auto );
    }
    
    public boolean isAuto()
    {
        return( m_is_auto );
    }
    
    public boolean isSlow()
    {
        return( m_is_slow );
    }

}
