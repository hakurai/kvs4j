package kvs.core.visualization.viewer;

import java.io.Serializable;
import kvs.core.matrix.Matrix33f;
import kvs.core.matrix.Vector2f;
import kvs.core.matrix.Vector2i;
import kvs.core.matrix.Vector3f;
import kvs.core.numeric.Quaternionf;

public class Trackball implements Serializable {

    private static final float ScalingFactor = 100.0f;
    private static final float Sqrt2 = 1.4142135623730950488f;
    private static final float HalfOfSqrt2 = 0.7071067811865475244f;

    public enum ScalingType {

        ScalingXYZ {

            @Override
            void Scaling( Vector2f start,
                    Vector2f end,
                    Trackball this_class ) {
                x_scaling( start, end, this_class );
                y_scaling( start, end, this_class );
                z_scaling( start, end, this_class );
            }
        },
        ScalingX {

            @Override
            void Scaling( Vector2f start,
                    Vector2f end,
                    Trackball this_class ) {
                x_scaling( start, end, this_class );
            }
        },
        ScalingY {

            @Override
            void Scaling( Vector2f start,
                    Vector2f end,
                    Trackball this_class ) {
                y_scaling( start, end, this_class );
            }
        },
        ScalingZ {

            @Override
            void Scaling( Vector2f start,
                    Vector2f end,
                    Trackball this_class ) {
                z_scaling( start, end, this_class );
            }
        },
        ScalingXY {

            @Override
            void Scaling( Vector2f start,
                    Vector2f end,
                    Trackball this_class ) {
                x_scaling( start, end, this_class );
                y_scaling( start, end, this_class );
            }
        },
        ScalingYZ {

            @Override
            void Scaling( Vector2f start,
                    Vector2f end,
                    Trackball this_class ) {
                y_scaling( start, end, this_class );
                z_scaling( start, end, this_class );
            }
        },
        ScalingZX {

            @Override
            void Scaling( Vector2f start,
                    Vector2f end,
                    Trackball this_class ) {
                x_scaling( start, end, this_class );
                z_scaling( start, end, this_class );
            }
        },
        ScalingNot {

            @Override
            void Scaling( Vector2f start,
                    Vector2f end,
                    Trackball this_class ) {
            }
        };

        abstract void Scaling( Vector2f start,
                Vector2f end,
                Trackball this_class );
    };
    protected float m_size; // /< trackball size
    protected float m_depth; // /< how near from center
    protected Vector2f m_rot_center; // /< center of rotation in the devise
    // coordinate system
    protected Vector3f m_scale; // /< current scaling value
    protected Vector3f m_trans; // /< current translation vector
    protected Quaternionf m_rot; // /< current rotation quaternion
    protected int m_window_width; // /< window width
    protected int m_window_height; // /< window height
    protected Camera m_ref_camera; // /< pointer to camera (reference only)

    public Trackball() {
        this.reset();
    }

    public void attachCamera( Camera camera ) {
        m_ref_camera = camera;
    }

    public void resetRotationCenter() {
        m_rot_center = Vector2f.ZERO;
    }

    /**
     * このトラックボールのサイズを設定します。
     * サイズが小さいほどドラッグあたりの変化量が大きくなります。
     * 
     * @param size
     */
    public void setTrackballSize( float size ) {
        m_size = size;
    }

    public void setDepthValue( float depth ) {
        m_depth = depth;
    }

    public void setRotationCenter( Vector2f center ) {
        m_rot_center = center;
    }

    public void setWindowSize( int width, int height ) {
        m_window_width = width;
        m_window_height = height;
    }

    public float getTrackballSize() {
        return m_size;
    }

    public float getDepthValue() {
        return m_depth;
    }

    public Vector2f getRotationCenter() {
        return m_rot_center;
    }

    public Vector3f getScaling() {
        return m_scale;
    }

    public Vector3f getTranslation() {
        return m_trans;
    }

    public Matrix33f rotation() {
        return (m_rot.toMatrix33f());
    }

    public int getWindowWidth() {
        return (m_window_width);
    }

    public int getWindowHeight() {
        return (m_window_height);
    }

    public void scale(
            Vector2i start,
            Vector2i end,
            ScalingType type ) {
        m_scale = new Vector3f( 1.0f, 1.0f, 1.0f );

        Vector2f n_old = this.get_norm_position( start );
        Vector2f n_new = this.get_norm_position( end );

        type.Scaling( n_old, n_new, this );
    }

    public void translate( Vector2i start, Vector2i end ) {
        // Calculate the bias point which is the center in the window coordinate
        // system.
        final Vector2f bias = new Vector2f( (float) m_window_width / 2.0f, (float) m_window_height / 2.0f );

        // Calculate the drag start point and the end point in the world
        // coordinate system.

        float x = ((float) start.getX() - bias.getX()) * 10.0f / m_window_width;
        float y = -((float) start.getY() - bias.getY()) * 10.0f / m_window_height;
        Vector2f world_start = new Vector2f( x, y );

        x = ((float) end.getX() - bias.getX()) * 10.0f / m_window_width;
        y = -((float) end.getY() - bias.getY()) * 10.0f / m_window_height;
        Vector2f world_end = new Vector2f( x, y );

        Vector2f trans = world_end.sub( world_start );
        Vector3f vec1 = m_ref_camera.getUpVector();
        Vector3f vec2 = vec1.cross( m_ref_camera.getPosition().sub( m_ref_camera.getLookAt() ) );

        vec1 = vec1.normalize();
        vec2 = vec2.normalize();

        m_trans = vec1.mul( trans.getY() ).add( (vec2.mul( trans.getX() )) );
    }

    public void rotate( Vector2i start, Vector2i end ) {
        if( start.equals( end ) ){
            m_rot = Quaternionf.IDENTITY;
            return;
        }

        Vector2f n_old = this.get_norm_position( start );
        Vector2f n_new = this.get_norm_position( end );

        Vector3f p1 = new Vector3f( n_old.getX(), n_old.getY(), this.depth_on_sphere( n_old ) );
        Vector3f p2 = new Vector3f( n_new.getX(), n_new.getY(), this.depth_on_sphere( n_new ) );

        if( GlobalCore.target == GlobalCore.ControlTarget.TargetCamera ){
            m_rot = Quaternionf.rotationQuaternion( p1, p2 );
            return;
        }

        // Calculate view.
        final Vector3f init_vec = new Vector3f( 0.0f, 0.0f, 1.0f );
        final Vector3f from_vec = m_ref_camera.getPosition().sub( m_ref_camera.getLookAt() );
        final Quaternionf rot = Quaternionf.rotationQuaternion( init_vec, from_vec );

        p1 = Quaternionf.rotate( p1, rot );
        p2 = Quaternionf.rotate( p2, rot );

        m_rot = Quaternionf.rotationQuaternion( p1, p2 );
    }

    protected void reset() {
        m_size = 0.6f;
        m_scale = new Vector3f( 1.0f, 1.0f, 1.0f );
        m_depth = 1.0f;
        m_rot_center = Vector2f.ZERO;
        m_trans = Vector3f.ZERO;
        m_rot = Quaternionf.IDENTITY;
    }

    protected float depth_on_sphere( Vector2f dir ) {
        final double r = m_size;
        final double d = dir.length();
        double z;

        // inside sphere
        if( d < r * HalfOfSqrt2 ){
            z = java.lang.Math.sqrt( r * r - d * d );
        } // on hyperbola
        else {
            final double t = r / Sqrt2;
            z = t * t / d;
        }

        return (float) (z);
    }

    protected Vector2f get_norm_position( Vector2i pos ) {
        float x = 2.0f * (pos.getX() - m_rot_center.getX()) / m_window_width;
        float y = -2.0f * (pos.getY() - m_rot_center.getY()) / m_window_height;

        return (new Vector2f( x, y ));
    }

    public static void x_scaling( Vector2f start, Vector2f end, Trackball trackball ) {
        final int h = trackball.m_window_height;
        final float x = 1.0f + ScalingFactor * (start.getY() - end.getY()) / h;
        trackball.m_scale = new Vector3f( x, trackball.m_scale.getY(), trackball.m_scale.getZ() );
    }

    public static void y_scaling( Vector2f start, Vector2f end, Trackball trackball ) {
        final int h = trackball.m_window_height;
        final float y = 1.0f + ScalingFactor * (start.getY() - end.getY()) / h;
        trackball.m_scale = new Vector3f( trackball.m_scale.getX(), y, trackball.m_scale.getZ() );
    }

    public static void z_scaling( Vector2f start, Vector2f end, Trackball trackball ) {
        int h = trackball.m_window_height;
        float z = 1.0f + ScalingFactor * (start.getY() - end.getY()) / h;
        trackball.m_scale = new Vector3f( trackball.m_scale.getX(), trackball.m_scale.getY(), z );
    }
}
