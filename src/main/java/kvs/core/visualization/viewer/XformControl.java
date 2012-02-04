package kvs.core.visualization.viewer;

import kvs.core.matrix.Matrix33f;
import kvs.core.matrix.Vector3f;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class XformControl extends Xform {

    //private Xform parent_class;

    private Xform m_initial_xform;

    private boolean m_can_collision;

    public XformControl() {
        this( true );

    }

    public XformControl( boolean collision ) {
        super();
        this.m_can_collision = collision;
        this.m_initial_xform = new Xform();
        this.m_initial_xform.initialize();
    }

    public XformControl( final Vector3f translation, final Vector3f scale, final Matrix33f rotation, boolean collision ) {
        this.m_initial_xform = new Xform( translation, scale, rotation );
        this.m_can_collision = collision;
        this.saveXform();
    }

    public void enableCollision() {
        this.m_can_collision = true;
    }

    public void disableCollision() {
        this.m_can_collision = false;
    }

    public boolean canCollision() {
        return (this.m_can_collision);
    }

    public void setInitialXform( final Vector3f translation, final Vector3f scale, final Matrix33f rotation ) {
        this.set( translation, scale, rotation );
        this.saveXform();
    }

    public void saveXform() {
        this.m_initial_xform.set( this );
    }

    public void resetXform() {
        this.set( this.m_initial_xform );
    }

    public void multiplyXform( final Xform xform ) {
        this.m_base = xform.m_base.mul( this.m_base );
    }

    public void setXform( final Xform xform ) {
        this.set( xform );
    }

    public final void applyXform() {
        GL gl = GLU.getCurrentGL();
        float[] xform;
        xform = this.toArray();
        gl.glMultMatrixf( xform, 0 );
    }

    public final Xform getXform() {
        return ( this );
    }

    public final void rotateXform( final Matrix33f rotation ) {
        Vector3f position = new Vector3f( this.get( 0, 3 ), this.get( 1, 3 ), this.get( 2, 3 ) );

        this.translateXform( position.opposite() );
        this.updateRotation( rotation );
        this.translateXform( position );
    }

    public final void translateXform( final Vector3f translation ) {
        this.updateTranslation( translation );
    }

    public final void scaleXform( final Vector3f scaling ) {
        Vector3f position = new Vector3f( this.get( 0, 3 ), this.get( 1, 3 ), this.get( 2, 3 ) );

        this.translateXform( position.opposite() );
        this.updateScaling( scaling );
        this.translateXform( position );
    }

}
