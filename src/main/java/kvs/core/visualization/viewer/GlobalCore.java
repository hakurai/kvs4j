package kvs.core.visualization.viewer;

import java.awt.Color;

public class GlobalCore {
    
    public enum ControlTarget
    {
        TargetObject,
        TargetCamera,
        TargetLight,
        NumberOfTargets
    };

    // Basic components in the viewer.
    protected static Camera             camera;           ///< camera
    protected static Light              light;            ///< light
    protected static Mouse              mouse;            ///< mouse
    protected static Background         background;       ///< background

    // Parameters for controlling the viewer.
    protected static ControlTarget      target;           ///< control target
    protected static ObjectManager      object_manager;   ///< object manager
    protected static RendererManager    renderer_manager; ///< renderer manager
    protected static IDManager          id_manager;       ///< ID manager ( object_id, renderer_id )
    

    
    public GlobalCore()
    {
        create_core();
    }
    
    
    public static void create_core()
    {

        target = ControlTarget.TargetObject;

        camera = new Camera();
        light = new Light();
        mouse = new Mouse();
        background = new Background( new Color( 212, 221, 229 ) );
        object_manager = new ObjectManager();
        renderer_manager = new RendererManager();
        id_manager = new IDManager();


    }
    
    public static void clear_core()
    {
        camera = null;
        light = null;
        mouse = null;
        background = null;
        object_manager = null;
        renderer_manager = null;
        id_manager = null;
    }
    
    public static void reset_core()
    {
        // Reset viewpoint to the initial position.
        mouse.reset();

        // Reset the xform of the object.
        if( object_manager.hasActiveObject() )
        {
            object_manager.resetActiveObjectXform();
        }
        else
        {
            object_manager.resetXform();
        }

        // Reset the xform of the camera.
        camera.resetXform();
        camera.initialize();

        // Reset the xform of the light.
        light.resetXform();
        light.initialize();
    }
    
    public static Mouse mouse(){
        return mouse;
    }
    
    public static ControlTarget target(){
        return target;
    }
    
    public static Camera camera(){
        return camera;
    }
    
    public static Light light(){
        return light;
    }
    
    public static Background background(){
        return background;
    }
    public static ObjectManager objectManager(){
        return object_manager;
    }
    
    public static RendererManager rendererManager(){
        return renderer_manager;
    }
    
    public static IDManager idManager(){
        return id_manager;
    }
    

}
