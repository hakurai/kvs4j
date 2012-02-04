package kvs.core.visualization.viewer;

import java.io.Serializable;
import kvs.core.matrix.Matrix33f;
import kvs.core.matrix.Matrix44f;
import kvs.core.matrix.Vector3f;

public class Xform  implements Serializable{

    protected Matrix44f m_base;

    private Matrix33f m_rotation; // /< rotation matrix

    private Vector3f m_scaling;

    public Xform() {
        this.initialize();
    }

    public Xform( Vector3f translation, Vector3f scaling, Matrix33f rotation ) {
        this.set( translation, scaling, rotation );

    }

    public Xform( Matrix44f m ) {
        this.m_base = m;
        
    }

    public void initialize() {
        this.m_base = Matrix44f.IDENTITY;
        m_rotation = Matrix33f.IDENTITY;
        m_scaling = new Vector3f( 1.0f, 1.0f, 1.0f );
    }

    public void set( Vector3f translation, Vector3f scaling, Matrix33f rotation ) {
        this.initialize();
        this.updateRotation( rotation );
        this.updateScaling( scaling );
        this.updateTranslation( translation );
    }

    public void set( Xform xform ) {
        this.m_base = xform.m_base;
        this.m_rotation = xform.m_rotation;
        this.m_scaling = xform.m_scaling;
    }

    public final float get( final int row, final int col ) {
        return this.m_base.get( row, col );
    }

    /**
     * このXformを配列にして返します。
     * 
     */
    public float[] toArray() {
        float[] elements = new float[16];

        elements[0] = m_base.get( 0, 0 );
        elements[1] = m_base.get( 1, 0 );
        elements[2] = m_base.get( 2, 0 );
        elements[3] = m_base.get( 3, 0 );

        elements[4] = m_base.get( 0, 1 );
        elements[5] = m_base.get( 1, 1 );
        elements[6] = m_base.get( 2, 1 );
        elements[7] = m_base.get( 3, 1 );

        elements[8] = m_base.get( 0, 2 );
        elements[9] = m_base.get( 1, 2 );
        elements[10] = m_base.get( 2, 2 );
        elements[11] = m_base.get( 3, 2 );

        elements[12] = m_base.get( 0, 3 );
        elements[13] = m_base.get( 1, 3 );
        elements[14] = m_base.get( 2, 3 );
        elements[15] = m_base.get( 3, 3 );

        return elements;

    }

    public void updateRotation( final Matrix33f rotation ) {
        Matrix44f rotation44 = new Matrix44f(
                rotation.get( 0, 0 ),
                rotation.get( 0, 1 ),
                rotation.get( 0, 2 ),
                0,
                rotation.get( 1, 0 ),
                rotation.get( 1, 1 ),
                rotation.get( 1, 2 ),
                0,
                rotation.get( 2, 0 ),
                rotation.get( 2, 1 ),
                rotation.get( 2, 2 ),
                0,
                0,
                0,
                0,
                1 );

        this.m_base = rotation44.mul( this.m_base );

        m_rotation = rotation.mul( m_rotation );
    }

    public void updateTranslation( Vector3f translation ) {
        float[] elements = m_base.get();
        elements[3] += translation.getX();
        elements[7] += translation.getY();
        elements[11] += translation.getZ();

        this.m_base = new Matrix44f( elements );
    }

    public void updateScaling( final Vector3f scaling ) {
        float[] elements = m_base.get();
        for (int c = 0; c < 4; ++c) {
            elements[c] *= scaling.getX();
        }
        for (int c = 0; c < 4; ++c) {
            elements[4 + c] *= scaling.getY();
        }
        for (int c = 0; c < 4; ++c) {
            elements[8 + c] *= scaling.getZ();
        }

        this.m_base = new Matrix44f( elements );

        m_scaling = new Vector3f(
                m_scaling.getX() * scaling.getX(),
                m_scaling.getY() * scaling.getY(),
                m_scaling.getZ() * scaling.getZ() );
    }

    public void updateScaling( final float scaling ) {
        float[] elements = m_base.get();
        for (int i = 0; i < 4; ++i) {
            elements[i] *= scaling;
        }

        this.m_base = new Matrix44f( elements );

        m_scaling = m_scaling.mul( scaling );
    }

    public Vector3f getTranslation() {
        return new Vector3f(
                this.m_base.get( 0, 3 ),
                this.m_base.get( 1, 3 ),
                this.m_base.get( 2, 3 ) );
    }

    public Matrix33f getRotation() {
        return m_rotation;
    }

    public Matrix33f scaledRotation() {
        return new Matrix33f(
                this.m_base.get( 0, 0 ),
                this.m_base.get( 1, 0 ),
                this.m_base.get( 2, 0 ),
                this.m_base.get( 0, 1 ),
                this.m_base.get( 1, 1 ),
                this.m_base.get( 2, 1 ),
                this.m_base.get( 0, 2 ),
                this.m_base.get( 1, 2 ),
                this.m_base.get( 2, 2 ) );
    }

    public Vector3f getScaling() {
        return m_scaling;
    }

    public Xform mul( final Xform xform ) {

        this.m_base = this.m_base.mul( xform.m_base );

        return this;

    }

    public Xform inverse() {
        m_base = m_base.inverse();
        return this;
    }

}
