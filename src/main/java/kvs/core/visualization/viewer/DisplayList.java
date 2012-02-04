package kvs.core.visualization.viewer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.KVSException;

public class DisplayList {
    
    protected int m_id;
    protected int m_range;
    
    public DisplayList(){
        m_id = 0;
        m_range = 0;  
    }
    
    public DisplayList( int range ){
        this.create( range );
    }
    
    public int id(){
        return m_id;
    }
    
    public int range(){
        return m_range;
    }
    
    public void begin( final int index, final int mode ) throws KVSException{
        GL gl = GLU.getCurrentGL();
        if ( index >= m_range )
        {
            throw new KVSException("Given display-list index is out of range.");
        }

        gl.glNewList( m_id + index, mode );
    }
    
    public void end(){
        GL gl = GLU.getCurrentGL();
        gl.glEndList();
    }
    
    public boolean create( final int range )
    {
        GL gl = GLU.getCurrentGL();
        m_id = gl.glGenLists( range );
        if ( m_id == 0 ){
            return( false );
        }

        m_range = range;

        return( true );
    }
    
    public void clear()
    {
        GL gl = GLU.getCurrentGL();
        if ( gl.glIsList( m_id ) == true )
        {
            gl.glDeleteLists( m_id, m_range );
        }
    }
    
    public void render( final int index ) throws KVSException
    {
        GL gl = GLU.getCurrentGL();
        if ( index >= m_range )
        {
            throw new KVSException("Given display-list index is out of range.");
        }

        gl.glCallList( m_id + index );
    }

}
