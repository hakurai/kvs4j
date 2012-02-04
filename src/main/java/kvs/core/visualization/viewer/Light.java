package kvs.core.visualization.viewer;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.matrix.Matrix33f;
import kvs.core.matrix.Vector3f;

public class Light extends XformControl {

    protected int m_id; // /< light ID
    protected Vector3f m_init_position; // /< initial light position
    protected Vector3f m_position; // /< light position
    protected Vector3f m_diffuse; // /< diffuse color
    protected Vector3f m_ambient; // /< ambient color
    protected Vector3f m_specular; // /< specular color

    public Light() {
        this( true );
    }

    public Light( boolean collision ) {
        super( collision );
        this.initialize();
    }

    @Override
    public void initialize() {
        m_id = 0;
        m_init_position = new Vector3f( 0.0f, 0.0f, 10.0f );
        m_position = new Vector3f( 0.0f, 0.0f, 10.0f );
        m_diffuse = new Vector3f( 1.0f, 1.0f, 1.0f );
        m_ambient = Vector3f.ZERO;
        m_specular = new Vector3f( 1.0f, 1.0f, 1.0f );
    }

    public void setID( final int id ) {
        m_id = id;
    }

    public void setPosition( final float x, final float y, final float z ) {
        m_init_position = new Vector3f( x, y, z );
        m_position = new Vector3f( x, y, z );
    }

    public void setPosition( final Vector3f position ) {
        m_init_position = position;
        m_position = position;
    }

    public void setColor( final float r, final float g, final float b ) {
        m_diffuse = new Vector3f( r, g, b );
    }

    public void setColor( final Color color ) {
        float[] rgb = color.getRGBColorComponents( null );
        m_diffuse = new Vector3f( rgb[0], rgb[1], rgb[2] );
    }

    public void setDiffuse( final float r, final float g, final float b ) {
        m_diffuse = new Vector3f( r, g, b );
    }

    public void setDiffuse( final Color color ) {
        float[] rgb = color.getRGBColorComponents( null );
        m_diffuse = new Vector3f( rgb[0], rgb[1], rgb[2] );
    }

    public void setAmbient( final float r, final float g, final float b ) {
        m_ambient = new Vector3f( r, g, b );
    }

    public void setAmbient( final Color color ) {
        float[] rgb = color.getRGBColorComponents( null );
        m_ambient = new Vector3f( rgb[0], rgb[1], rgb[2] );
    }

    public void setSpecular( final float r, final float g, final float b ) {
        m_specular = new Vector3f( r, g, b );
    }

    public void setSpecular( final Color color ) {
        float[] rgb = color.getRGBColorComponents( null );
        m_specular = new Vector3f( rgb[0], rgb[1], rgb[2] );
    }

    public final void setEnabled( boolean b ){
        GL gl = GLU.getCurrentGL();
        if( b ){
            gl.glEnable( m_id );
        } else {
            gl.glDisable( m_id );
        }
    }


    public final Vector3f getPosition() {
        return m_position;
    }

    public final Vector3f getDiffuse() {
        return m_diffuse;
    }

    public final Vector3f getAmbient() {
        return m_ambient;
    }

    
    public final Vector3f getSpecular() {
        return m_specular;
    }

    /**
     * このライトを更新します。
     * 
     */
    public void update() {

        GL gl = GLU.getCurrentGL();

        float[] position = { m_position.getX(), m_position.getY(), m_position.getZ(), 1.0f };
        float[] diffuse = { m_diffuse.getX(), m_diffuse.getY(), m_diffuse.getZ(), 1.0f };
        float[] ambient = { m_ambient.getX(), m_ambient.getY(), m_ambient.getZ(), 1.0f };
        float[] specular = { m_specular.getX(), m_specular.getY(), m_specular.getZ(), 1.0f };

        gl.glLightfv( m_id, GL.GL_POSITION, position, 0 );
        gl.glLightfv( m_id, GL.GL_DIFFUSE, diffuse, 0 );
        gl.glLightfv( m_id, GL.GL_AMBIENT, ambient, 0 );
        gl.glLightfv( m_id, GL.GL_SPECULAR, specular, 0 );
    }

    /**
     * このライトを点灯します。
     * 
     */
    public final void on() {
        GL gl = GLU.getCurrentGL();
        gl.glEnable( m_id );
    }

    /**
     * このライトを消灯します。
     * 
     */
    public final void off() {
        GL gl = GLU.getCurrentGL();
        gl.glDisable( m_id );
    }

    /**
     * このライトの状態を返します。
     * 
     * @return 点灯状態であればtrue
     * 
     */
    public final boolean isEnabled() {
        GL gl = GLU.getCurrentGL();
        return gl.glIsEnabled( m_id );
    }

    public void rotate( final Matrix33f rotation ) {
        rotateXform( rotation );
        this.update_position();
    }

    public void translate( final Vector3f translation ) {
        translateXform( translation );
        this.update_position();
    }

    public void scale( final Vector3f scaling ) {
        scaleXform( scaling );
        this.update_position();
    }

    public void update_position() {
        this.m_position = this.scaledRotation().mul( m_init_position ).add( getTranslation() );
    }

    public void setModelLocalViewer( boolean flag ) {
        GL gl = GLU.getCurrentGL();
        int f;
        if (flag) {
            f = 1;
        } else {
            f = 0;
        }
        gl.glLightModeli( GL.GL_LIGHT_MODEL_LOCAL_VIEWER, f );
    }

    public void setModelTwoSide( boolean flag ) {
        GL gl = GLU.getCurrentGL();
        int f;
        if (flag) {
            f = 1;
        } else {
            f = 0;
        }
        gl.glLightModeli( GL.GL_LIGHT_MODEL_TWO_SIDE, f );
    }

    public void setModelAmbient( float[] ambient ) {
        GL gl = GLU.getCurrentGL();
        gl.glLightModelfv( GL.GL_LIGHT_MODEL_AMBIENT, ambient, 0 );
    }

}
