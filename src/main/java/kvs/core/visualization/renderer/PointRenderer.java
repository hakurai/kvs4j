package kvs.core.visualization.renderer;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PointObject;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;

public class PointRenderer extends RendererBase {

    private static final long serialVersionUID = 8280320797289408176L;

    /*
     * S: size, Ss: size array 
     * C: color, Cs: color array 
     * N: normal, Ns: normal array
     */
    public enum PointRenderingType {
        Type_S_C_Ns {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                gl.glEnable( GL.GL_NORMALIZE );

                final float size = point.size();
                final Color color = point.color();

                gl.glPointSize( size );
                gl.glColor3ub( (byte) color.getRed(), (byte) color.getGreen(),
                        (byte) color.getBlue() );

                final float[] vertex = point.coords().array();
                final float[] normal = point.normals().array();

                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        final int i3 = i * 3;
                        gl.glNormal3f( normal[i3], normal[i3 + 1], normal[i3 + 2] );
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_S_C {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                final float size = point.size();
                final Color color = point.color();

                gl.glPointSize( size );
                gl.glColor3ub( (byte) color.getRed(), (byte) color.getGreen(),
                        (byte) color.getBlue() );

                final float[] vertex = point.coords().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        int i3 = i * 3;
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_S_Cs_Ns {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                gl.glEnable( GL.GL_NORMALIZE );

                final float size = point.size();
                gl.glPointSize( size );

                final float[] vertex = point.coords().array();
                final float[] normal = point.normals().array();
                final byte[] color = point.colors().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        int i3 = i * 3;
                        gl.glNormal3f( normal[i3], normal[i3 + 1], normal[i3 + 2] );
                        gl.glColor3ub( color[i3], color[i3 + 1], color[i3 + 2] );
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_S_Cs {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                final float size = point.size();
                gl.glPointSize( size );

                final float[] vertex = point.coords().array();
                final byte[] color = point.colors().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        final int i3 = i * 3;
                        gl.glColor3ub( color[i3], color[i3 + 1], color[i3 + 2] );
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_S {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                final float size = point.size();
                gl.glPointSize( size );

                final float[] vertex = point.coords().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        final int i3 = i * 3;
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Ss_C_Ns {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                gl.glEnable( GL.GL_NORMALIZE );

                final Color color = point.color();
                gl.glColor3ub( (byte) color.getRed(), (byte) color.getGreen(),
                        (byte) color.getBlue() );

                final float[] size = point.sizes().array();
                final float[] vertex = point.coords().array();
                final float[] normal = point.normals().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        final int i3 = i * 3;
                        gl.glPointSize( size[i3] );
                        gl.glNormal3f( normal[i3], normal[i3 + 1], normal[i3 + 2] );
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Ss_C {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                final Color color = point.color();
                gl.glColor3ub( (byte) color.getRed(), (byte) color.getGreen(),
                        (byte) color.getBlue() );

                final float[] size = point.sizes().array();
                final float[] vertex = point.coords().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        final int i3 = i * 3;
                        gl.glPointSize( size[i3] );
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Ss_Cs_Ns {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                gl.glEnable( GL.GL_NORMALIZE );

                final float[] size = point.sizes().array();
                final float[] vertex = point.coords().array();
                final float[] normal = point.normals().array();
                final byte[] color = point.colors().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        final int i3 = i * 3;
                        gl.glPointSize( size[i3] );
                        gl.glNormal3f( normal[i3], normal[i3 + 1], normal[i3 + 2] );
                        gl.glColor3ub( color[i3], color[i3 + 1], color[i3 + 2] );
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Ss_Cs {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                final float[] size = point.sizes().array();
                final float[] vertex = point.coords().array();
                final byte[] color = point.colors().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        final int i3 = i * 3;
                        gl.glPointSize( size[i3] );
                        gl.glColor3ub( color[i3], color[i3 + 1], color[i3 + 2] );
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Ss {
            @Override
            void rendering( PointObject point ) {
                GL gl = GLU.getCurrentGL();
                final float[] size = point.sizes().array();
                final float[] vertex = point.coords().array();
                gl.glBegin( GL.GL_POINTS );
                {
                    final int nvertices = point.nvertices();
                    for (int i = 0; i < nvertices; i++) {
                        int i3 = i * 3;
                        gl.glPointSize( size[i3] );
                        gl.glVertex3f( vertex[i3], vertex[i3 + 1], vertex[i3 + 2] );
                    }
                }
                gl.glEnd();
            }
        };

        abstract void rendering( PointObject point );
    };
    
    @Override
    public void exec( ObjectBase object, Camera camera, Light light ) {
        GL gl = GLU.getCurrentGL();

        // kvs::IgnoreUnusedVariable( light );
        // kvs::IgnoreUnusedVariable( camera );

        PointObject point = PointObject.valueOf( object );

        gl.glPushAttrib( GL.GL_CURRENT_BIT | GL.GL_ENABLE_BIT );

        if (point.normals().limit() == 0) {
            this.disableShading();
        }

        this.initialize();

        gl.glEnable( GL.GL_DEPTH_TEST );
        {
            this.timer().start();
            this.PointRenderingFunction( point );
            this.timer().stop();
        }
        gl.glDisable( GL.GL_DEPTH_TEST );

        gl.glPopAttrib();
    }

    private void PointRenderingFunction( PointObject point ) {
        if (point.nvertices() > 0) {
            PointRenderingType type = this.GetPointRenderingType( point );
            type.rendering( point );
        }
    };

    private PointRenderingType GetPointRenderingType( PointObject point ) {
        final int nsizes = point.nsizes();
        final int ncolors = point.ncolors();
        final int nnormals = point.nnormals();

        if (nsizes == 1) {
            if (ncolors == 1)
                return ((nnormals > 0) ? PointRenderingType.Type_S_C_Ns
                        : PointRenderingType.Type_S_C);
            else if (ncolors > 1)
                return ((nnormals > 0) ? PointRenderingType.Type_S_Cs_Ns
                        : PointRenderingType.Type_S_Cs);
            else
                return (PointRenderingType.Type_S);
        } else {
            if (ncolors == 1)
                return ((nnormals > 0) ? PointRenderingType.Type_Ss_C_Ns
                        : PointRenderingType.Type_Ss_C);
            else if (ncolors > 1)
                return ((nnormals > 0) ? PointRenderingType.Type_Ss_Cs_Ns
                        : PointRenderingType.Type_Ss_Cs);
            else
                return (PointRenderingType.Type_Ss);
        }
    };

    @Override
    protected void initialize_modelview() {
        GL gl = GLU.getCurrentGL();
        gl.glColorMaterial( GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE );
        gl.glEnable( GL.GL_COLOR_MATERIAL );

        gl.glDisable( GL.GL_NORMALIZE );

        if (!this.isShading())
            gl.glDisable( GL.GL_LIGHTING );
        else
            gl.glEnable( GL.GL_LIGHTING );
    }

    @Override
    protected void initialize_projection() {
        GL gl = GLU.getCurrentGL();
        gl.glMatrixMode( GL.GL_PROJECTION );

        gl.glMatrixMode( GL.GL_MODELVIEW );
    }

}
