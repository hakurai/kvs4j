package kvs.core.visualization.viewer;

import java.awt.image.BufferedImage;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import kvs.core.matrix.Matrix33f;
import kvs.core.matrix.Matrix44f;
import kvs.core.matrix.Vector2f;
import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector4f;

public class Camera extends XformControl{

    private boolean  m_pers;           ///< true: perspective, false: orthogonal

    private Vector3f m_init_position;  ///< initial position in the world coordinate system
    private Vector3f m_init_up_vector; ///< initial up vector in the world coordinate system
    private Vector3f m_init_look_at;   ///< initial look-at point in the world coordinate system

    private Vector3f m_position;       ///< position in the world coordinate system
    private Vector3f m_up_vector;      ///< up vector in the world coordinate system
    private Vector3f m_look_at;        ///< look-at point in the world coordinate system

    private float    m_field_of_view;  ///< field of view [deg]
    private float    m_front;          ///< front plane position
    private float    m_back;           ///< back plane position

    private float    m_left;           ///< left plane position
    private float    m_right;          ///< right plane position
    private float    m_bottom;         ///< bottom plane position
    private float    m_top;            ///< top plane position

    private int      m_window_width;   ///< window width
    private int      m_window_height;  ///< window height

    /**
     * カメラを標準設定で構築します。
     * 
     */
    public Camera(){
        this( true );
    }

    public Camera( boolean collision ){
        super( collision );
        this.initialize();
    }

    /**
     * このカメラの初期化を行います。
     * 
     */
    @Override
    public void initialize(){
        m_pers = true;
        m_init_position = new Vector3f( 0.0f, 0.0f, 12.0f );
        m_init_up_vector = new Vector3f( 0.0f, 1.0f, 0.0f );
        m_init_look_at = new Vector3f( 0.0f, 0.0f, 0.0f );
        m_position = new Vector3f( 0.0f, 0.0f, 12.0f );
        m_up_vector = new Vector3f( 0.0f, 1.0f, 0.0f );
        m_look_at = new Vector3f( 0.0f, 0.0f, 0.0f );
        m_field_of_view =   45.0f;
        m_front         =    1.0f;
        m_back          = 2000.0f;
        m_left          =   -5.0f;
        m_right         =    5.0f;
        m_bottom        =   -5.0f;
        m_top           =    5.0f;
    }

    private Matrix44f LookAtMatrix44f(
            final Vector3f eye,
            final Vector3f up,
            final Vector3f target )
    {
        Vector3f f = target.sub(eye);
        Vector3f s = f.cross( up.normalize() );
        Vector3f u = s.cross( f );

        f.normalize();
        s.normalize();
        u.normalize();

        final float[] elements =
        {
                s.getX(),  s.getY(),  s.getZ(), 0,
                u.getX(),  u.getY(),  u.getZ(), 0,
                -f.getX(), -f.getY(), -f.getZ(), 0,
                0,      0,      0, 1
        };

        return new Matrix44f( elements );
    }

    /**
     * このカメラの表示方法を透視法射影にします。
     * 
     */
    public void setPerspectiveCamera()
    {
        m_pers = true;
    }

    /**
     * このカメラの表示方法を正射影にします。
     * 
     */
    public void setOrthogonalCamera()
    {
        m_pers = false;
    }

    /**
     * このカメラの座標を指定します。
     * 
     * @param position カメラの座標
     * 
     */
    public void setPosition( final Vector3f position )
    {
        m_position = position;
    }

    public void setUpVector( final Vector3f up_vector )
    {
        m_up_vector = up_vector;
    }

    /**
     * このカメラの描写中心の座標を指定します。
     * 
     * @param look_at 描写中心の座標
     * 
     */
    public void setLookAt( final Vector3f look_at )
    {
        m_look_at = look_at;
    }

    public void setFieldOfView( final float fov )
    {
        m_field_of_view = fov;
    }

    public void setBack( final float back )
    {
        m_back = back;
    }

    public void setFront( final float front )
    {
        m_front = front;
    }

    public void setLeft( final float left )
    {
        m_left = left;
    }

    public void setRight( final float right )
    {
        m_right = right;
    }

    public void setBottom( final float bottom )
    {
        m_bottom = bottom;
    }

    public void setTop( final float top )
    {
        m_top = top;
    }

    /**
     * ウィンドウサイズを設定します。
     * この値はアスペクト比計算に利用されます。
     * 
     * @param width ウィンドウの幅
     * @param height ウィンドウの高さ
     */
    public void setWindowSize( final int width, final int height )
    {
        m_window_width  = width;
        m_window_height = height;
    }

    /**
     * このカメラが透視法射影を使用する場合はtrueを、正射影を使用する場合はfalseを返します。
     * 
     * @return 透視法射影であればtrue
     */
    public final boolean isPerspective()
    {
        return m_pers;
    }

    /**
     * このカメラの座標を表すVector3fを返します。
     * 
     * @return このカメラの座標
     */
    public final Vector3f getPosition()
    {
        return m_position;
    }

    public final Vector3f getUpVector()
    {
        return m_up_vector;
    }

    /**
     * このカメラの描写中心の座標の座標を表すVector3fを返します。
     * 
     * @return このカメラの視界の中心位置
     */
    public final Vector3f getLookAt()
    {
        return m_look_at;
    }

    public final Vector2f lookAtInDevice()
    {
        return new Vector2f( (float)m_window_width / 2.0f, (float)m_window_height / 2.0f );
    }
    
    /**
     * このカメラの透視法射影で描写する視野角を返します。
     * 
     * @return 視野角
     */
    public final float getFieldOfView()
    {
        return m_field_of_view;
    }

    /**
     * このカメラが透視法射影で描写する範囲の奥の座標を返します。
     * 
     * @return 描写範囲の奥の座標値
     */
    public final float getBack()
    {
        return m_back;
    }

    /**
     * このカメラが透視法射影で描写する範囲の手前の座標を返します。
     * 
     * @return 描写範囲の手前の座標値
     */
    public final float getFront()
    {
        return m_front;
    }

    /**
     * このカメラが正射影で描写する範囲の左端の座標を返します。
     * 
     * @return 描写範囲の左端の座標値
     */
    public final float getLeft()
    {
        return m_left;
    }

    /**
     * このカメラが正射影で描写する範囲の右端の座標を返します。
     * 
     * @return 描写範囲の右端の座標値
     */
    public final float getRight()
    {
        return m_right;
    }

    /**
     * このカメラが正射影で描写する範囲の下端の座標を返します。
     * 
     * @return 描写範囲の下端の座標値
     */
    public final float getBottom()
    {
        return m_bottom;
    }

    /**
     * このカメラが正射影で描写する範囲の上端の座標を返します。
     * 
     * @return 描写範囲の上端の座標値
     */
    public final float getTop()
    {
        return m_top;
    }

    /**
     * ウィンドウの幅を返します。
     * 
     * @return ウィンドウの幅
     */
    public final int getWindowWidth()
    {
        return m_window_width;
    }

    /**
     * ウィンドウの高さを返します。
     * 
     * @return ウィンドウの高さ
     */
    public final int getWindowHeight()
    {
        return m_window_height;
    }

    /**
     * このカメラの状態を更新します。
     * 
     */
    public void update()
    {
        GL gl = GLU.getCurrentGL();
        GLU glu = new GLU();

        float aspect = (float)m_window_width / (float)m_window_height;

        gl.glMatrixMode( GL.GL_PROJECTION );

        gl.glLoadIdentity();
        if( m_pers )
        {
            // Perspective camera mode
            glu.gluPerspective( m_field_of_view, aspect, m_front, m_back );
        }
        else
        {
            // Orthogonal camera mode
            if( aspect >= 1.0f )
            {
                gl.glOrtho( m_left * aspect, m_right * aspect,
                        m_bottom, m_top,
                        m_front, m_back );
            }
            else
            {
                gl.glOrtho( m_left, m_right,
                        m_bottom / aspect, m_top / aspect,
                        m_front, m_back );
            }
        }

        gl.glMatrixMode( GL.GL_MODELVIEW );

        gl.glLoadIdentity();
        glu.gluLookAt( m_position.getX(),  m_position.getY(),  m_position.getZ(),
                m_look_at.getX(),   m_look_at.getY(),   m_look_at.getZ(),
                m_up_vector.getX(), m_up_vector.getY(), m_up_vector.getZ() );
    }
    
    /**
     * 画面をキャプチャーします。
     * 未実装です。
     * 
     */
    public final BufferedImage snapshot()
    {
        return null;
    }
    

    public final void getProjectionMatrix( float[] projection )
    {
        GL gl = GLU.getCurrentGL();
        gl.glGetFloatv( GL.GL_PROJECTION_MATRIX, projection, 0 );
    }

    public final void getModelviewMatrix( float[] modelview )
    {
        GL gl = GLU.getCurrentGL();
        gl.glGetFloatv( GL.GL_MODELVIEW_MATRIX, modelview, 0 );
    }

    public final void getCombinedMatrix( float[] combined )
    {
        float[] projection = new float[16];
        this.getProjectionMatrix( projection );
        float[] modelview = new float[16];
        this.getModelviewMatrix( modelview );

        this.getCombinedMatrix( projection, modelview, combined );
    }

    public final void getCombinedMatrix(
            float[] projection,
            float[] modelview,
            float[] combined )
    {
        /* Calculate a combined matrix PM in order to project the point in the
         * object coordinate system onto the image plane in the window coordinate
         * system. The matrix PM is composed of a modelview marix M and a projection
         * matrix P. It is possible to calculate the efficiently by taking advantage
         * of zero-elements in the M and P.
         *
         * Modelview matrix M:   [ m0, m4, m8,  m12 ]   [ m0, m4, m8,  m12 ]
         *                       [ m1, m5, m9,  m13 ] = [ m1, m5, m9,  m13 ]
         *                       [ m2, m6, m10, m14 ]   [ m2, m6, m10, m14 ]
         *                       [ m3, m7, m11, m15 ]   [  0,  0,   0,   1 ]
         *
         * Projection matrix P:  [ p0, p4, p8,  p12 ]   [ p0,  0, p8,    0 ] (Pers.)
         *                       [ p1, p5, p9,  p13 ] = [  0, p5, p9,    0 ]
         *                       [ p2, p6, p10, p14 ]   [  0,  0, p10, p14 ]
         *                       [ p3, p7, p11, p15 ]   [  0,  0,  -1,   0 ]
         *
         *                                              [ p0,  0,   0, p12 ] (Orth.)
         *                                            = [  0, p5,   0, p13 ]
         *                                              [  0,  0, p10, p14 ]
         *                                              [  0,  0,   0,   1 ]
         *
         * if 'r == -l' in the view volume, P is denoted as follows:
         *
         *       [ p0,  0,   0,   0 ] (Pers.)     [ p0,  0,   0,   0 ] (Orth.)
         *       [  0, p5,   0,   0 ]             [  0, p5,   0,   0 ]
         *       [  0,  0, p10, p14 ]             [  0,  0, p10, p14 ]
         *       [  0,  0,  -1,   0 ]             [  0,  0,   0,   1 ]
         *
         * Combined matrix PM:
         *
         *         [ p0  m0,   p0  m4,   p0  m8,    p0  m12       ] (Pers.)
         *         [ p5  m1,   p5  m5,   p5  m9,    p5  m13       ]
         *         [ p10 m2,   p10 m6,   p10 m10,   p10 m14 + p14 ]
         *         [    -m2,      -m6,      -m10,            -m14 ]
         *
         *         [ p0  m0,   p0  m4,   p0  m8,    p0  m12       ] (Orth.)
         *         [ p5  m1,   p5  m5,   p5  m9,    p5  m13       ]
         *         [ p10 m2,   p10 m6,   p10 m10,   p10 m14 + p14 ]
         *         [      0,        0,         0,               1 ]
         */

        // Row 1
        combined[ 0] = projection[0] * modelview[ 0];
        combined[ 4] = projection[0] * modelview[ 4];
        combined[ 8] = projection[0] * modelview[ 8];
        combined[12] = projection[0] * modelview[12];

        // Row 2
        combined[ 1] = projection[5] * modelview[ 1];
        combined[ 5] = projection[5] * modelview[ 5];
        combined[ 9] = projection[5] * modelview[ 9];
        combined[13] = projection[5] * modelview[13];

        // Row 3
        combined[ 2] = projection[10] * modelview[ 2];
        combined[ 6] = projection[10] * modelview[ 6];
        combined[10] = projection[10] * modelview[10];
        combined[14] = projection[10] * modelview[14] + projection[14];

        // Row 4
        if( this.isPerspective() )
        {
            combined[ 3] = -modelview[ 2];
            combined[ 7] = -modelview[ 6];
            combined[11] = -modelview[10];
            combined[15] = -modelview[14];
        }
        else
        {
            combined[ 3] = 0.0f;
            combined[ 7] = 0.0f;
            combined[11] = 0.0f;
            combined[15] = 1.0f;
        }
    }

    public final Vector2f projectObjectToWindow(
            float  p_obj_x,
            float  p_obj_y,
            float  p_obj_z,
            float depth )
    {
        float[] p = new float[16];
        getProjectionMatrix( p );
        float[] m = new float[16];
        getModelviewMatrix( m );
        float[] pm = new float[16];
        getCombinedMatrix( p, m, pm );

        float[] p_tmp = {
                p_obj_x * pm[0] + p_obj_y * pm[4] + p_obj_z * pm[ 8] + pm[12],
                p_obj_x * pm[1] + p_obj_y * pm[5] + p_obj_z * pm[ 9] + pm[13],
                p_obj_x * pm[2] + p_obj_y * pm[6] + p_obj_z * pm[10] + pm[14],
                p_obj_x * pm[3] + p_obj_y * pm[7] + p_obj_z * pm[11] + pm[15]
        };

        p_tmp[3] = 1.0f / p_tmp[3];
        p_tmp[0] *= p_tmp[3];
        p_tmp[1] *= p_tmp[3];

        if( depth == 0.0f ){
            depth = ( 1.0f + p_tmp[2] * p_tmp[3] ) * 0.5f;
        }

        return new Vector2f( ( 1.0f + p_tmp[0] ) * m_window_width  * 0.5f,
                ( 1.0f + p_tmp[1] ) * m_window_height * 0.5f );
    }
    
    public final Vector2f projectObjectToWindow(
            final Vector3f p_obj)
    {
        return( this.projectObjectToWindow( p_obj.getX(), p_obj.getY(), p_obj.getZ(), 0.0f ) );
    }

    public final Vector2f projectObjectToWindow(
            final Vector3f p_obj,
            float          depth )
    {
        return( this.projectObjectToWindow( p_obj.getX(), p_obj.getY(), p_obj.getZ(), depth ) );
    }

    public final Vector3f projectWindowToObject(
            final Vector2f p_win,
            float          depth )
    {
        GL gl = GLU.getCurrentGL();
        GLU glu = new GLU();

        double[] m = new double[16];
        gl.glGetDoublev( GL.GL_MODELVIEW_MATRIX, m, 0 );
        double[] p = new double[16];
        gl.glGetDoublev( GL.GL_PROJECTION_MATRIX, p, 0 );
        int[]    v = new int[4];
        gl.glGetIntegerv( GL.GL_VIEWPORT, v, 0 );

        double[] pos = new double[3];
        glu.gluUnProject( p_win.getX(), p_win.getY(), depth,
                m, 0,
                p, 0,
                v, 0,
                pos, 0);


        return new Vector3f( (float)pos[0], (float)pos[1], (float)pos[2] );
    }

    public final Vector3f projectWindowToCamera(
            final Vector2f p_win,
            float          depth )
    {
        GL gl = GLU.getCurrentGL();
        GLU glu = new GLU();
        double[] m = { 1.0, 0.0, 0.0, 0.0,
                0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0 };

        double[] p = new double[16];
        gl.glGetDoublev(  GL.GL_PROJECTION_MATRIX, p, 0 );
        int[] v = new int[4];
        gl.glGetIntegerv( GL.GL_VIEWPORT, v, 0 );

        double[] pos = new double[3];
        glu.gluUnProject( p_win.getX(), p_win.getY(), depth,
                m, 0,
                p, 0,
                v, 0,
                pos, 0 );

        return new Vector3f( (float)pos[0], (float)pos[1], (float)pos[2] );
    }

    public final Vector3f projectWindowToWorld(
            final GLAutoDrawable drawable,
            final Vector2f p_win,
            float          depth )
    {
        Vector3f p_cam = this.projectWindowToCamera( p_win, depth );

        return( scaledRotation().mul(( p_cam.add(m_init_position) ).add(getTranslation())) );
    }

    public final Vector3f projectObjectToCamera(
            final Vector3f p_obj )
    {
        GL gl = GLU.getCurrentGL();
        float[] m = new float[16];
        gl.glGetFloatv( GL.GL_MODELVIEW_MATRIX, m, 0 );
        final Matrix44f modelview = new Matrix44f( m );
        final Vector4f p_cam = ( (new Vector4f( p_obj, 1.0f )).mul(modelview) );

        return new Vector3f( p_cam.x(), p_cam.y(), p_cam.z() );
    }

    public final Vector3f projectCameraToObject(
            final Vector3f p_cam )
    {
        GL gl = GLU.getCurrentGL();
        float[] m = new float[16];
        gl.glGetFloatv( GL.GL_MODELVIEW_MATRIX, m, 0 );
        final Matrix44f modelview = new Matrix44f( m );
        final Vector4f p_obj = (new Vector4f( p_cam, 1.0f )).mul(modelview.inverse());

        return new Vector3f( p_obj.x(), p_obj.y(), p_obj.z() );
    }

    public final Vector3f projectWorldToCamera( final Vector3f p_wld )
    {
        final Matrix44f M = LookAtMatrix44f( m_position, m_up_vector, m_look_at );
        final Vector4f p_cam = (new Vector4f( p_wld, 1.0f )).mul(M);

        return new Vector3f( p_cam.x(), p_cam.y(), p_cam.z() );
    }

    public final Vector3f projectCameraToWorld( final Vector3f p_cam )
    {
        Matrix44f M = LookAtMatrix44f( m_position, m_up_vector, m_look_at );
        M = M.inverse();

        final Vector4f p_wld = (new Vector4f( p_cam, 1.0f )).mul(M);

        return new Vector3f( p_wld.x(), p_wld.y(), p_wld.z() );
    }

    public final Vector3f projectWorldToObject( final Vector3f p_wld )
    {
        final Vector3f p_cam = this.projectWorldToCamera( p_wld );

        return this.projectCameraToObject( p_cam );
    }
    
    public final Vector3f projectObjectToWorld( final Vector3f p_obj )
    {
        final Vector3f p_cam = this.projectObjectToCamera( p_obj );

        return this.projectCameraToWorld( p_cam );
    }
    
    public final void rotate( final Matrix33f rotation )
    {
        rotateXform( rotation );
        this.update_up_at_from();
    }
    
    public void translate( final Vector3f translation )
    {
        translateXform( translation );
        this.update_up_at_from();
    }
    
    public void scale( final Vector3f scaling )
    {
        scaleXform( scaling );
        this.update_up_at_from();
    }
    
    private void update_up_at_from()
    {
        Vector3f vec = m_init_position.sub( m_init_look_at );

        this.m_look_at   = this.getTranslation().add(m_init_look_at);
        m_position  = this.scaledRotation().mul( vec ).add( m_look_at );
        m_up_vector = this.scaledRotation().mul(m_init_up_vector);
    }

}
