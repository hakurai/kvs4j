package kvs.core.visualization.renderer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.matrix.Matrix44f;
import kvs.core.matrix.Vector2f;
import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector4f;

public class Ray {

    private float       m_t = 0.0f;         ///< Parameter.
    private Vector3f    m_from = Vector3f.ZERO;      ///< From point.
    private Vector3f    m_direction = Vector3f.ZERO; ///< Directional vector.
    private Matrix44f   m_combined;  ///< combined matrix
    private Matrix44f   m_inverse;   ///< inverse matrix
    private Vector2f    m_delta;     ///<
    private Vector2f    m_constant;  ///<


    public Ray()
    {
        GL gl = GLU.getCurrentGL();
        double[] modelview = new double[16];
        double[] projection = new double[16];
        int[]    viewport = new int[4];

        gl.glGetDoublev( GL.GL_MODELVIEW_MATRIX, modelview, 0 );
        gl.glGetDoublev( GL.GL_PROJECTION_MATRIX, projection, 0 );
        gl.glGetIntegerv( GL.GL_VIEWPORT, viewport, 0 );

        m_delta = new Vector2f( 2.0f / viewport[2],
                                2.0f / viewport[3] );

        m_constant = new Vector2f( - viewport[0] * 2.0f / viewport[2] - 1.0f,
                                   - viewport[1] * 2.0f / viewport[3] - 1.0f );

        this.combine_projection_and_modelview( projection, modelview );
    }

    public void setOrigin( final int win_x, final int win_y )
    {
        final float[] in =
        {
                win_x * m_delta.get( 0 ) + m_constant.get( 0 ),
                win_y * m_delta.get( 1 ) + m_constant.get( 1 )
        };

        final float[] center =
        {
                m_inverse.get( 0, 0 ) * in[0] + m_inverse.get( 0, 1 ) * in[1] + m_inverse.get( 0, 3 ),
                m_inverse.get( 1, 0 ) * in[0] + m_inverse.get( 1, 1 ) * in[1] + m_inverse.get( 1, 3 ),
                m_inverse.get( 2, 0 ) * in[0] + m_inverse.get( 2, 1 ) * in[1] + m_inverse.get( 2, 3 ),
                m_inverse.get( 3, 0 ) * in[0] + m_inverse.get( 3, 1 ) * in[1] + m_inverse.get( 3, 3 )
        };

        float[] front =
        {
                center[0] - m_inverse.get( 0, 2 ),
                center[1] - m_inverse.get( 1, 2 ),
                center[2] - m_inverse.get( 2, 2 ),
                center[3] - m_inverse.get( 3, 2 )
        };

        final float front_inv = 1.0f / front[3];
        front[0] *= front_inv;
        front[1] *= front_inv;
        front[2] *= front_inv;

        float[] back =
        {
                center[0] + m_inverse.get( 0, 2 ),
                center[1] + m_inverse.get( 1, 2 ),
                center[2] + m_inverse.get( 2, 2 ),
                center[3] + m_inverse.get( 3, 2 )
        };

        final float back_inv = 1.0f / back[3];
        back[0] *= back_inv;
        back[1] *= back_inv;
        back[2] *= back_inv;

        m_t = 0.0f;

        m_from = new Vector3f( front[0], front[1], front[2] );

        Vector3f direction = new Vector3f( back[0] - front[0], back[1] - front[1], back[2] - front[2] );
        m_direction = direction.normalize();
    }

    public void setT( final float t )
    {
        m_t = t;
    }

    public final boolean isIntersected(
                                 final Vector3f v0,
                                 final Vector3f v1,
                                 final Vector3f v2 )
    {
        final Vector3f v01 = v1.sub( v0 );
        final Vector3f v02 = v2.sub( v0 );

        final Vector3f pvec = this.direction().cross( v02 );

        final float det = v01.dot( pvec );

        if ( det > 1e-6f )
        {
            final Vector3f tvec = this.from().sub( v0 );
            final float u = tvec.dot( pvec );
            if ( u < 0.0f || u > det ) { 
                return( false ); 
            }

            final Vector3f qvec = tvec.cross( v01 );
            final float v = this.direction().dot( qvec );
            if ( v < 0.0f || u + v > det ) {
                return( false );
                }

            final float t = v02.dot( qvec ) / det;
            this.setT( t );

            return( true );    //hit!!
        }

        return( false );
    }

    public final boolean isIntersected(
                                 final Vector3f v0,
                                 final Vector3f v1,
                                 final Vector3f v2,
                                 final Vector3f v3 )
    {
        final Vector3f v01 = v1.sub( v0 );
        final Vector3f v03 = v3.sub( v0 );

        final Vector3f pvec = this.direction().cross( v03 );

        final float det = v01.dot( pvec );

        if ( det > 1e-6f )
        {
            final Vector3f tvec = this.from().sub( v0 );
            final float u = tvec.dot( pvec );
            if ( u < 0.0f || u > det ) { 
                return false;
            }

            final Vector3f qvec = tvec.cross( v01 );
            final float v = this.direction().dot( qvec );
            if ( v < 0.0f || v > det ) {
                return false;
            }

            final float t = v03.dot( qvec ) / det;
            this.setT( t );

            return true;    //hit!!
        }

        return( false );
    }

    public float t()
    {
        return( m_t );
    }

    public Vector3f from()
    {
        return( m_from );
    }

    public Vector3f direction()
    {
        return( m_direction );
    }

    public Vector3f point()
    {
        return( m_from.add(m_direction.mul( m_t ) ) );
    }

    public float depth()
    {
        Vector3f point = this.point();

        final float view2 =
            point.getX() * m_combined.get( 2, 0 ) +
            point.getY() * m_combined.get( 2, 1 ) +
            point.getZ() * m_combined.get( 2, 2 ) +
            m_combined.get( 2, 3 );

        final float view3 =
            point.getX() * m_combined.get( 3, 0 ) +
            point.getY() * m_combined.get( 3, 1 ) +
            point.getZ() * m_combined.get( 3, 2 ) +
            m_combined.get( 3, 3 );

        return( ( 1.0f + view2 / view3 ) * 0.5f );
    }

    public final void combine_projection_and_modelview( double[] projection, double[] modelview )
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

        // Row 1.
        Vector4f row1 = new Vector4f(
                (float)(projection[ 0] * modelview[ 0]),
                (float)(projection[ 0] * modelview[ 4]),
                (float)(projection[ 0] * modelview[ 8]),
                (float)(projection[ 0] * modelview[12]) );

        // Row 2.
        Vector4f row2 = new Vector4f(
                (float)(projection[ 5] * modelview[ 1]),
                (float)(projection[ 5] * modelview[ 5]),
                (float)(projection[ 5] * modelview[ 9]),
                (float)(projection[ 5] * modelview[13]) );

        // Row 3.
        Vector4f row3 = new Vector4f(
                (float)(projection[10] * modelview[ 2]),
                (float)(projection[10] * modelview[ 6]),
                (float)(projection[10] * modelview[10]),
                (float)(projection[10] * modelview[14] + projection[14]) );

        // Row 4.
        Vector4f row4;
        // Perspective.
        if ( kvs.core.util.Math.isZero( projection[15] ) )
        {
            row4 = new Vector4f(
                    (float)(- modelview[ 2]),
                    (float)(- modelview[ 6]),
                    (float)(- modelview[10]),
                    (float)(- modelview[14]) );
        }
        // Orthogonal.
        else
        {
            row4 = new Vector4f(
                    0.0f,
                    0.0f,
                    0.0f,
                    1.0f );
        }
        
        m_combined = new Matrix44f( row1, row2, row3, row4 );

        // Calculate the inverse of the PM matrix.
        m_inverse = m_combined.inverse();
    }
    
    /**
     * このオブジェクトの文字列表現を返します。 
     * 
     * @return このオブジェクトの文字列表現
     */
    @Override
    public String toString()
    {
        String crlf = System.getProperty( "line.separator" );
        StringBuilder sb = new StringBuilder();
        sb.append( "from      : " );
        sb.append( m_from.toString() );
        sb.append( crlf );
        sb.append( "direction : " );
        sb.append( m_direction.toString() );
        sb.append( crlf );
        sb.append( "t         : " );
        sb.append( m_t );
        
        return sb.toString();
    
    }

}
