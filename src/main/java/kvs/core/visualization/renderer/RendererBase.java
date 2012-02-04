package kvs.core.visualization.renderer;


import java.io.Serializable;

import kvs.core.KVSException;
import kvs.core.util.Timer;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.pipeline.PipelineModule;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;

public abstract class RendererBase implements PipelineModule, Serializable {
    
    private static final long serialVersionUID = -412236055270959054L;
    protected Timer m_timer = new Timer();
    protected boolean m_shading_flag;
    
    public RendererBase(){
        m_shading_flag = true;
    }
    
    public final Timer timer(){
        return m_timer;
    }
    
    public boolean isShading(){
        return m_shading_flag;
    }
    
    public void enableShading(){
        m_shading_flag = true;
    }
    
    public void disableShading(){
        m_shading_flag = false;
    }
    
    public void initialize()
    {
        this.initialize_projection();
        this.initialize_modelview();
    }
    
    
    @Override
    public final ObjectBase exec( ObjectBase Object ) throws KVSException{
        return null;
    }
    
    public abstract void exec( ObjectBase object, Camera camera, Light light ) throws KVSException;

    @Override
    public boolean equals( Object obj ){
        if( this == obj ){
            return true;
        }

        if( !(obj instanceof RendererBase) ){
            return false;
        }

        RendererBase other = (RendererBase)obj;

        if( this.m_shading_flag != other.m_shading_flag ){
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.m_shading_flag ? 1 : 0);
        return hash;
    }
    
    protected abstract void initialize_projection();
    
    protected abstract void initialize_modelview();

    
}
