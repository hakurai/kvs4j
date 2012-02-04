package kvs.awt.viewer;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.AWTGraphicsConfiguration;
import javax.media.opengl.AWTGraphicsDevice;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLDrawableFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import kvs.core.KVSException;
import kvs.core.visualization.viewer.GlobalCore;
import kvs.core.visualization.viewer.Mouse;
import kvs.core.visualization.viewer.PaintEventBase;
import kvs.core.visualization.viewer.ResizeEventBase;
import kvs.core.visualization.viewer.ScreenCore;
import kvs.core.visualization.viewer.Trackball.ScalingType;

public class ScreenBase extends ScreenCore {

    protected static PaintEventBase m_paint_event;
    protected static ResizeEventBase m_resize_event;
    protected static TimerTask m_timer_task;
    protected static MouseListener m_mouse_listener;
    protected static MouseMotionListener m_mouse_motion_listener;
    protected static MouseWheelListener m_mouse_wheel_listener;
    protected static KeyListener m_key_listener;
    protected static ArrayList<TimerTask> m_timer_task_list = new ArrayList<TimerTask>();
    protected static ArrayList<Long> m_timer_task_time_list = new ArrayList<Long>();
    protected static Timer m_timer = new Timer();
    protected static GLDrawable m_drawable;
    protected static GLContext m_context;
    private Thread m_animator;
    private static final Vector<Runnable> m_queue = new Vector<Runnable>();

    public ScreenBase() {
        super();
        initialize_event();

    }

    public static void makeContentCurrent() // make the rendering context current for this thread
    {
        try {
            while( m_context.makeCurrent() == GLContext.CONTEXT_NOT_CURRENT ) {
                System.out.println( "Context not yet current..." );
                Thread.sleep( 10 );
            }
        } catch( InterruptedException e ) {
            e.printStackTrace();
        }
    }  // end of makeContentCurrent()

    public static void release() {
        m_context.release();
    }

    private void initialize_event() {
        m_paint_event = new DefaultPaintEvent();
        m_resize_event = new DefaultResizeEvent();
        m_timer_task = new DefaultTimerTask();
        m_mouse_listener = new DefaultMouseListener();
        m_mouse_motion_listener = new DefaultMouseMotionListener();
        m_mouse_wheel_listener = new DefaultMouseWheelListener();
        m_key_listener = new DefaultKeyListener();
    }

    public void display() throws KVSException {
        m_paint_event.paintEventBase();
    }

    /**
     * ウィンドウサイズが変更されたときの処理です

     */
    @Override
    public void resize( int width, int height ) {

        m_width = width;
        m_height = height;
        m_resize_event.resizeEventBase( width, height );
    }

    public void addTimerEvent( TimerTask event, long time ) {
        m_timer_task_list.add( event );
        m_timer_task_time_list.add( time );
    }

    @Override
    public void setKeyListener( KeyListener listener ) {
        m_key_listener = listener;

    }

    @Override
    public void setMouseListener( MouseListener listener ) {
        m_mouse_listener = listener;
    }

    @Override
    public void setMouseMotionListener( MouseMotionListener listener ) {
        m_mouse_motion_listener = listener;
    }

    @Override
    public void setMouseWheelListener( MouseWheelListener listener ) {
        m_mouse_wheel_listener = listener;
    }

    @Override
    public void setPaintEvent( PaintEventBase event ) {
        m_paint_event = event;
    }

    @Override
    public void setResizeEvent( ResizeEventBase event ) {
        m_resize_event = event;

    }

    public void setTimerMouseEvent( TimerTask event ) {
        m_timer_task = event;
    }

    public JPanel createCanvas( boolean last ) {

        GLCapabilities caps = new GLCapabilities();
        caps.setSampleBuffers( true );
        caps.setDoubleBuffered( true );
        caps.setHardwareAccelerated( true );
        AWTGraphicsDevice dev = new AWTGraphicsDevice( null );
        AWTGraphicsConfiguration awtConfig = (AWTGraphicsConfiguration) GLDrawableFactory.getFactory().chooseGraphicsConfiguration( caps, null, dev );

        GraphicsConfiguration config = null;
        if( awtConfig != null ) {
            config = awtConfig.getGraphicsConfiguration();
        }

        Canvas canvas = new KVSCanvas( config, caps );

        canvas.setPreferredSize( new Dimension( m_width, m_height ) );

        canvas.setFocusable( true );
        canvas.setFocusTraversalKeysEnabled( false );
        if( last ){
            canvas.addMouseListener( m_mouse_listener );
            canvas.addMouseMotionListener( m_mouse_motion_listener );
            canvas.addMouseWheelListener( m_mouse_wheel_listener );
        }
        canvas.addKeyListener( m_key_listener );

        JPanel renderPanel = new JPanel();
        renderPanel.setLayout( new BorderLayout() );
        renderPanel.setOpaque( false );
        renderPanel.add( canvas );

        return renderPanel;
    }

    /**
     * ウィンドウを生成、表示します。
     * 
     * @param last アニメーションを行う場合はtrue
     */
    @Override
    public void show( boolean last ) {
        JFrame frame = new JFrame( m_title );
        frame.setLocation( m_x, m_y );
        frame.add( createCanvas( last ) );
        frame.addWindowListener( new WindowAdapter() {

            @Override
            public void windowClosing( WindowEvent e ) {
                System.exit( 0 );
            }
        } );
        frame.pack();
        frame.setVisible( true );

    }

    public static void invokeLater( Runnable run ) {
        m_queue.add( run );
    }

    private class DefaultMouseListener implements MouseListener {

        @Override
        public void mouseClicked( MouseEvent e ) {
            m_add_mouse_listener.mouseClicked( e );
        }

        @Override
        public void mouseEntered( MouseEvent e ) {
            m_add_mouse_listener.mouseEntered( e );
        }

        @Override
        public void mouseExited( MouseEvent e ) {
            m_add_mouse_listener.mouseExited( e );
        }

        @Override
        public void mousePressed( final MouseEvent e ) {
            //makeContentCurrent();

            invokeLater( new Runnable() {

                @Override
                public void run() {
                    m_add_mouse_listener.mousePressed( e );
                    // Left button action.
                    if( javax.swing.SwingUtilities.isLeftMouseButton( e ) ){
                        if( !is_active_move( e.getX(), e.getY() ) ){
                            return;
                        }

                        // Key modification.
                        Mouse.TransMode mode;

                        if( e.isShiftDown() ){
                            mode = Mouse.TransMode.Scaling;
                        } else if( e.isControlDown() ){
                            System.out.println( "ctrl" );
                            mode = Mouse.TransMode.Translation;
                        } else {
                            mode = Mouse.TransMode.Rotation;
                            set_center_of_rotation();
                        }

                        mouse_button_press( e.getX(), e.getY(), mode );
                    } else if( javax.swing.SwingUtilities.isMiddleMouseButton( e ) ){

                        if( !is_active_move( e.getX(), e.getY() ) ){
                            return;
                        }
                        mouse_button_press( e.getX(), e.getY(), Mouse.TransMode.Scaling );

                    } // Right button action.
                    else if( javax.swing.SwingUtilities.isRightMouseButton( e ) ){
                        if( !is_active_move( e.getX(), e.getY() ) ){
                            return;
                        }
                        mouse_button_press( e.getX(), e.getY(), Mouse.TransMode.Translation );

                    }


                }
            } );

            //release();
        }

        @Override
        public void mouseReleased( MouseEvent e ) {
            m_add_mouse_listener.mouseReleased( e );
            mouse_button_release( e.getX(), e.getY() );

        }
    }

    private class DefaultMouseMotionListener implements MouseMotionListener {

        @Override
        public void mouseDragged( MouseEvent e ) {
            m_add_mouse_motion_listener.mouseDragged( e );

            if( GlobalCore.target() == GlobalCore.ControlTarget.TargetObject ){
                if( !GlobalCore.objectManager().isEnableAllMove() ){
                    if( !GlobalCore.objectManager().hasActiveObject() ){
                        return;
                    }
                }
            }

            GlobalCore.mouse().move( e.getX(), e.getY() );

            update_xform();
        }

        @Override
        public void mouseMoved( MouseEvent e ) {
            m_add_mouse_motion_listener.mouseMoved( e );
        }
    }

    private class DefaultMouseWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved( MouseWheelEvent e ) {
            set_object_manager_params();
            GlobalCore.mouse().setScalingType( ScalingType.ScalingXYZ );

            if( e.getWheelRotation() > 0 ){
                GlobalCore.mouse().wheel( Mouse.WheelUpValue );
            }

            if( e.getWheelRotation() < 0 ){
                GlobalCore.mouse().wheel( Mouse.WheelDownValue );
            }

            update_xform();

        }
    }

    private class DefaultKeyListener implements KeyListener {

        @Override
        public void keyPressed( KeyEvent e ) {
            switch( e.getKeyCode() ) {
                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_Q:
                    System.exit( 0 );
                    break;
                case KeyEvent.VK_TAB:
                    m_can_move_all = false;
                    break;
                case KeyEvent.VK_HOME:
                    GlobalCore.reset_core();
                default:
                    break;
            }
            m_add_key_listener.keyPressed( e );
        }

        @Override
        public void keyReleased( KeyEvent e ) {
            m_add_key_listener.keyReleased( e );
        }

        @Override
        public void keyTyped( KeyEvent e ) {
            m_add_key_listener.keyTyped( e );
        }
    }

    private class DefaultTimerTask extends TimerTask {

        @Override
        public void run() {
            if( GlobalCore.mouse().idle() ){
                if( !(GlobalCore.target() == GlobalBase.ControlTarget.TargetObject && !GlobalCore.objectManager().isEnableAllMove() && !GlobalCore.objectManager().hasActiveObject()) ){
                    update_xform();
                }
            }
        }
    }

    private class DefaultPaintEvent implements PaintEventBase {

        @Override
        public void paintEventBase() throws KVSException {
            paint_event_core();
        }
    }

    private class DefaultResizeEvent implements ResizeEventBase {

        @Override
        public void resizeEventBase( int width, int height ) {
            resize_event_core( width, height );
        }
    }

    private class KVSCanvas extends Canvas {

        private static final long serialVersionUID = 1L;

        public KVSCanvas( GraphicsConfiguration config, GLCapabilities caps ) {
            super( config );
            setBackground( Color.white );

            m_drawable = GLDrawableFactory.getFactory().getGLDrawable( this, caps, null );
            m_context = m_drawable.createContext( null );
            //setPreferredSize( new Dimension( m_width, m_height ) );
        }

        @Override
        public void addNotify() // wait for the canvas to be added to the JPanel before starting
        {
            super.addNotify();      // creates the peer
            m_drawable.setRealized( true );  // the canvas can now be rendering into

            if( m_animator == null ){
                m_animator = new JOGLEventDispatchThread();
                m_animator.start();
            }

            m_timer.schedule( m_timer_task, 0L, Mouse.SpinTime );
            int num = m_timer_task_list.size();
            for( int i = 0; i < num; i++ ) {
                m_timer.schedule( m_timer_task_list.get( i ), i + 1, m_timer_task_time_list.get( i ) );
            }

        }

        @Override
        public void update( Graphics g ) {
        }

        @Override
        public void paint( Graphics g ) {
        }

        protected class JOGLEventDispatchThread extends Thread {

            @Override
            public void run() {
                makeContentCurrent();
                initialize();
                release();
                while( true ) {
                    makeContentCurrent();

                    resizeView();
                    try {
                        m_paint_event.paintEventBase();
                    } catch( KVSException ex ) {
                        Logger.getLogger( ScreenBase.class.getName() ).log( Level.SEVERE, null, ex );
                    }

                    try {
                        synchronized( m_queue ) {
                            for( int i = m_queue.size() - 1; i >= 0; i-- ) {
                                Runnable task = m_queue.remove( i );
                                task.run();
                                //System.gc();
                            }
                        }
                    } catch( Exception e ) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep( 5 );
                    } catch( InterruptedException e ) {
                        e.printStackTrace();
                    }

                    m_drawable.swapBuffers();

                    release();
                }

            }

            private void resizeView() {
                int width = getWidth();
                int height = getHeight();
                if( width != m_width || height != m_height ){
                    ScreenBase.this.resize( width, height );
                }
            }
        }
    }
}
