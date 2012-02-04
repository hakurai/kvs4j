package kvs.core.visualization.viewer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.swing.event.MouseInputListener;

import kvs.core.KVSException;
import kvs.core.matrix.Vector2f;
import kvs.core.matrix.Vector3f;
import kvs.core.util.IntPair;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.renderer.RendererBase;

public abstract class ScreenCore {
    
    protected static String                 m_title;         ///< window title
    protected static int                    m_x;             ///< window position (y position)
    protected static int                    m_y;             ///< window position (x position)
    protected static int                    m_width;         ///< window size (width)
    protected static int                    m_height;        ///< window size (height)
    protected static int                    m_id;            ///< window ID
    protected static boolean                m_is_fullscreen; ///< flag for statement of fullscreen.
    protected static boolean                m_can_move_all;  ///< flag for object movement
    
    protected static InitializeFunc         m_add_initialize_func;
    protected static PaintEvent             m_add_paint_event;
    protected static ResizeEvent            m_add_resize_event;
    protected static MouseListener          m_add_mouse_listener;
    protected static MouseMotionListener    m_add_mouse_motion_listener;
    protected static MouseWheelListener     m_add_mouse_wheel_listener;
    protected static KeyListener            m_add_key_listener;
        
    public ScreenCore( )
    {
        m_title         = "<unknown>";
        m_x             = 0;
        m_y             = 0;
        m_width         = 512;
        m_height        = 512;
        m_id            = 0;
        m_is_fullscreen = false;
        m_can_move_all  = true;
        
        m_add_initialize_func       = new AddInitializeFunc();
        m_add_paint_event           = new AddPaintEvent();
        m_add_resize_event          = new AddResizeEvent();
        m_add_mouse_listener        = new AddMouseListener();
        m_add_mouse_motion_listener = new AddMouseListener();
        m_add_mouse_wheel_listener  = new AddMouseWheelListener();
        m_add_key_listener          = new AddKeyListener();
        
    }
    
    
    public void setPosition( int x, int y )
    {
        m_x = x;
        m_y = y;
    }
    
    public void setSize( int width, int height )
    {
        m_width  = width;
        m_height = height;

        if( GlobalCore.camera != null ){
            GlobalCore.camera.setWindowSize( width, height );
        }
        if( GlobalCore.mouse != null ){
            GlobalCore.mouse.setWindowSize( width, height );
        }
    }
    
    public void setGeometry( int x, int y, int width, int height )
    {
        setPosition( x, y );
        setSize( width, height );
    }
    
    public void setTitle( String title )
    {
        m_title = title;
    }
    
    public int x()
    {
        return( m_x );
    }
    
    public int y()
    {
        return( m_y );
    }
    
    public int width()
    {
        return( m_width );
    }
    
    public int height()
    {
        return( m_height );
    }
    
    public int id()
    {
        return( m_id );
    }
    
    
    public void addInitializeFunc( InitializeFunc pfunc )
    {
        m_add_initialize_func = pfunc;
    }
    
    
    public void addPaintEvent( PaintEvent event )
    {
        m_add_paint_event = event;
    }
    
    public void addResizeEvent( ResizeEvent event )
    {
        m_add_resize_event = event;
    }
    
    public void addMouseListener( MouseListener event ){
        m_add_mouse_listener = event;
    }
    
    public void addMouseMotionListener( MouseMotionListener event ){
        m_add_mouse_motion_listener = event;
    }
    
    public void addMouseInputListener( MouseInputListener event ){
        m_add_mouse_listener = event;
        m_add_mouse_motion_listener = event;
    }
    
    public void addMouseWheelEvent( MouseWheelListener event ){
        m_add_mouse_wheel_listener = event;
    }
    
    public void addKeyEvent( KeyListener event ){
        m_add_key_listener = event;
    }
    
    
    public abstract void setPaintEvent( PaintEventBase event );
    
    public abstract void setResizeEvent( ResizeEventBase event );

    public abstract void setMouseListener( MouseListener listener );
    
    public abstract void setMouseMotionListener( MouseMotionListener listener );

    public abstract void setMouseWheelListener( MouseWheelListener listener );

    public abstract void setKeyListener( KeyListener listener );
    
    
    public final void show(){
        show( true );
    }
    
    public abstract void show( boolean last );
    
    public static void showFullScreen(){};

    public static void showNormal(){};

    public static void popUp(){};

    public static void pushDown(){};

    public static void hide(){};

    public static void showWindow(){};

    public static void redraw(){};

    public void resize( int width, int height ){};
    

    public static boolean isFullScreen()
    {
        return( m_is_fullscreen );
    }
    
    public static void disableAllMove()
    {
        m_can_move_all = false;
    }
    
    protected final void initialize()
    {


        // Set the lighting parameters.
        GlobalCore.light.setID( GL.GL_LIGHT0 );
        GlobalCore.light.setPosition( 0.0f, 0.0f, 12.0f );
        GlobalCore.light.setColor( 1.0f, 1.0f, 1.0f );            

        // Attach the Camera to the Mouse
        GlobalCore.mouse.attachCamera( GlobalCore.camera );
        // Call the additional initializing function.
        m_add_initialize_func.initializeFunc();

        GlobalCore.light.on();

    }
    
    
    protected final void paint_event_core() throws KVSException
    {
        GL gl = GLU.getCurrentGL();
        gl.glMatrixMode( GL.GL_MODELVIEW );
        {
            gl.glLoadIdentity();

            // Update the camera and light.
            GlobalCore.camera.update();
            GlobalCore.light.update();

            // Set the background color or image.
            GlobalCore.background.apply();

            // Aliases for the object manager and the renderer manager.
            ObjectManager   om = GlobalCore.object_manager;
            RendererManager rm = GlobalCore.renderer_manager;
            IDManager       id = GlobalCore.id_manager;
            Camera          c  = GlobalCore.camera;
            Light           l  = GlobalCore.light;

            // Rendering the resistered object by using the corresponding renderer.
            gl.glPushMatrix();
            
            
            if( om.hasObject() )
            {
                final int size = id.size();
                for( int index = 0; index < size; index++ )
                {
                    IntPair id_pair = id.get( index );

                    ObjectBase   o = om.object( id_pair.getFirst() );
                    RendererBase r = rm.renderer( id_pair.getSecond() );

                    if( o.isShown() )
                    {
                        gl.glPushMatrix();
                        o.transform( om.objectCenter(), om.normalize() );
                        r.exec( o, c, l );
                        gl.glPopMatrix();
                    }
                }
            }
            else {
                om.applyXform();
            }
            

            // Call the additional paint function here.
            m_add_paint_event.paintEvent();
            gl.glPopMatrix();
        }
   }
    
    protected final void resize_event_core( int width, int height )
    {
        GL gl = GLU.getCurrentGL();
        if( !m_is_fullscreen )
        {
            m_width  = width;
            m_height = height;
        }

        // Update the viewport for OpenGL.
        gl.glViewport( 0 , 0 , width , height );

        // Update the window size for camera and mouse.
        GlobalCore.camera.setWindowSize( width, height );
        GlobalCore.mouse.setWindowSize( width, height );

        // Call the additional resize function.
        m_add_resize_event.resizeEvent( width, height );
    }
    
    protected static void mouse_button_release( int x, int y )
    {
        m_can_move_all = true;
        GlobalCore.mouse.release( x, y );

        if( !( GlobalCore.mouse.isUseAuto() &&
               GlobalCore.mouse.isAuto() ) )
        {
            GlobalCore.object_manager.releaseActiveObject();
        }
    }
    
    protected static void mouse_button_press( int x, int y, Mouse.TransMode mode )
    {
        set_object_manager_params();

        GlobalCore.mouse.setMode( mode );
        GlobalCore.mouse.press( x, y );
    }
    
    protected static boolean is_active_move( int x, int y )
    {
        if( !GlobalCore.object_manager.hasObject() ){
            return( true );
        }

        if( GlobalCore.target == GlobalCore.ControlTarget.TargetObject )
        {
            if( !m_can_move_all )
            {
                // Collision detection.
                ObjectManager om = GlobalCore.object_manager;
                Camera        c  = GlobalCore.camera;
                Vector2f p  = new Vector2f( x, y );
                return( om.detectCollision( p, c ) );
            }
        }

        return( true );
    }
    
    protected static void set_object_manager_params()
    {
        if( GlobalCore.target == GlobalCore.ControlTarget.TargetObject )
        {
            if( m_can_move_all )
            {
                GlobalCore.object_manager.enableAllMove();
                GlobalCore.object_manager.releaseActiveObject();
            }
            else
            {
                GlobalCore.object_manager.disableAllMove();
            }
        }
    }
    
    protected static void set_center_of_rotation()
    {
        // Center of rotation in the device coordinate system.
        Vector2f rot_center = Vector2f.ZERO;

        switch( GlobalCore.target )
        {
        case TargetCamera:
        case TargetLight:
            /* Get an at-point of the camera, which is the center of rotation,
             * in the device coord.
             */
            rot_center = GlobalCore.camera.lookAtInDevice();
            break;
        case TargetObject:
            if( m_can_move_all || !GlobalCore.object_manager.hasObject() )
            {
                ObjectManager om = GlobalCore.object_manager;
                rot_center = om.positionInDevice( GlobalCore.camera );
            }
            else
            {
                ObjectManager  om = GlobalCore.object_manager;
                ObjectBase     o  = om.activeObject();
                Camera         c  = GlobalCore.camera;
                final Vector3f t  = om.objectCenter();
                final Vector3f s  = om.normalize();
                rot_center = o.positionInDevice( c, t, s );
            }
            break;
        default:
            break;
        }

        GlobalCore.mouse.setRotationCenter( rot_center );
    }
    
    protected static void update_xform()
    {
        switch( GlobalCore.target )
        {
        case TargetCamera:
            update_camera_xform( GlobalCore.camera );
            break;
        case TargetLight:
            update_light_xform( GlobalCore.light );
            break;
        case TargetObject:
            update_object_manager_xform( GlobalCore.object_manager );
            break;
        default:
            break;
        }
    }
    
    protected static void update_object_manager_xform( ObjectManager manager )
    {
        switch( GlobalCore.mouse.getMode() )
        {
        case Rotation:
            manager.rotate( GlobalCore.mouse.rotation() );
            break;
        case Translation:
            manager.translate( GlobalCore.mouse.getTranslation() );
            break;
        case Scaling:
            manager.scale( GlobalCore.mouse.getScaling() );
            break;
        default:
            break;
        }
    }
    
    protected static void update_camera_xform( Camera camera )
    {
        switch( GlobalCore.mouse.getMode() )
        {
        case Rotation:
            camera.rotate( GlobalCore.mouse.rotation() );
            break;
        case Translation:
            camera.translate( GlobalCore.mouse.getTranslation() );
            break;
        case Scaling:
            camera.scale( GlobalCore.mouse.getScaling() );
            break;
        default:
            break;
        }
    }
    
    protected static void update_light_xform( Light light )
    {
        switch( GlobalCore.mouse.getMode() )
        {
        case Rotation:
            light.rotate( GlobalCore.mouse.rotation() );
            break;
        case Translation:
            light.translate( GlobalCore.mouse.getTranslation() );
            break;
        case Scaling:
            light.scale( GlobalCore.mouse.getScaling() );
            break;
        default:
            break;
        }
    }
    
    private class AddInitializeFunc implements InitializeFunc{
        public void initializeFunc() {}
    }
    
    private class AddPaintEvent implements PaintEvent{
        public void paintEvent() {} 
    }
    
    private class AddResizeEvent implements ResizeEvent{
        public void resizeEvent( int wigth, int height ) {} 
    }
    
    private class AddMouseListener implements MouseInputListener{
        public void mouseClicked( MouseEvent e ) {}
        public void mouseEntered( MouseEvent e ) {}
        public void mouseExited( MouseEvent e ) {}
        public void mousePressed( MouseEvent e ) {}
        public void mouseReleased( MouseEvent e ) {}
        public void mouseDragged( MouseEvent e ) {}
        public void mouseMoved( MouseEvent e ) {}    
    }
    
    private class AddMouseWheelListener implements MouseWheelListener{
        public void mouseWheelMoved( MouseWheelEvent e ) {}    
    }
    
    private class AddKeyListener implements KeyListener{
        public void keyPressed( KeyEvent e ) {}
        public void keyReleased( KeyEvent e ) {}
        public void keyTyped( KeyEvent e ) {}  
    }
    
}
