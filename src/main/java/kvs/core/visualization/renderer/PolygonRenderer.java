package kvs.core.visualization.renderer;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PolygonObject;
import kvs.core.visualization.object.PolygonObject.ColorType;
import kvs.core.visualization.object.PolygonObject.NormalType;
import kvs.core.visualization.object.PolygonObject.PolygonType;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;

public class PolygonRenderer extends RendererBase {


    private static final long serialVersionUID = -1032662834315725496L;

    enum PolygonRenderingType {

        Type_Tri_VC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL();
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final int   nvertices = polygon.nvertices();
                    final byte[] colors    = polygon.colors().array();
                    final float[] coords  = polygon.coords().array();
                    final byte  opacity   = polygon.opacity();

                    int index = 0;
                    for( int i = 0; i < nvertices; i++, index += 3 )
                    {
                        gl.glColor4ub( colors[index],
                                colors[index + 1],
                                colors[index + 2],
                                opacity );
                        gl.glVertex3fv( coords, index );
                    }
                }
                gl.glEnd();

            }
        },
        Type_Tri_VC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL();
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();

            }
        },
        Type_Tri_VC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();

            }
        },
        Type_Tri_VC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index)   * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity(con0/3) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity(con1/3) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity(con2/3) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();

            }
        },
        Type_Tri_PC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }

                }
                gl.glEnd();
            }
        },
        Type_Tri_PC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nopacities(); i++ )
                    {
                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        int index = 9 * i;

                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();

            }
        },
        Type_Tri_PC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PCs_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );

                        index = 9 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PCs_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PCs_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );

                        index = 9 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PCs_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },

        Type_Quad_VC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glColor4ub( polygon.colors().get(con3),
                                polygon.colors().get(con3 + 1),
                                polygon.colors().get(con3 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity(con0 / 3) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity(con1 / 3) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity(con2 / 3) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glColor4ub( polygon.colors().get(con3),
                                polygon.colors().get(con3 + 1),
                                polygon.colors().get(con3 + 2),
                                polygon.opacity(con3 / 3) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nopacities(); i++ )
                    {
                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        int index = 12 * i;

                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PCs_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );

                        index = 12 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PCs_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PCs_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );

                        index = 12 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PCs_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();

            }
        },

        Type_Tri_VN_VC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();

            }
        },
        Type_Tri_VN_VC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_VC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_VC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity(con0 / 3) );
                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity(con1 / 3) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity(con2 / 3) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_PC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );

                    }
                }
                gl.glEnd();
            }
        },
        
        Type_Tri_VN_PC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_PC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nopacities(); i++ )
                    {
                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        int index = 9 * i;

                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glNormal3f( polygon.normals().get(index + 3),
                                polygon.normals().get(index + 4),
                                polygon.normals().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glNormal3f( polygon.normals().get(index + 6),
                                polygon.normals().get(index + 7),
                                polygon.normals().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_PC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_PCs_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );

                        index = 9 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glNormal3f( polygon.normals().get(index + 3),
                                polygon.normals().get(index + 4),
                                polygon.normals().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glNormal3f( polygon.normals().get(index + 6),
                                polygon.normals().get(index + 7),
                                polygon.normals().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_PCs_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_PCs_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );

                        index = 9 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glNormal3f( polygon.normals().get(index + 3),
                                polygon.normals().get(index + 4),
                                polygon.normals().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glNormal3f( polygon.normals().get(index + 6),
                                polygon.normals().get(index + 7),
                                polygon.normals().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_VN_PCs_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },

        Type_Quad_VN_VC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_VC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glColor4ub( polygon.colors().get(con3),
                                polygon.colors().get(con3 + 1),
                                polygon.colors().get(con3 + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(con3),
                                polygon.normals().get(con3 + 1),
                                polygon.normals().get(con3 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                        
                        
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_VC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_VC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity(con0 / 3) );
                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity(con1 / 3) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity(con2 / 3) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glColor4ub( polygon.colors().get(con3),
                                polygon.colors().get(con3 + 1),
                                polygon.colors().get(con3 + 2),
                                polygon.opacity(con3 / 3) );
                        gl.glNormal3f( polygon.normals().get(con3),
                                polygon.normals().get(con3 + 1),
                                polygon.normals().get(con3 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_PC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nvertices(); i++ )
                    {
                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_PC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( 0 ) );

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glNormal3f( polygon.normals().get(con3),
                                polygon.normals().get(con3 + 1),
                                polygon.normals().get(con3 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_PC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nopacities(); i++ )
                    {
                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        int index = 12 * i;

                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glNormal3f( polygon.normals().get(index + 3),
                                polygon.normals().get(index + 4),
                                polygon.normals().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glNormal3f( polygon.normals().get(index + 6),
                                polygon.normals().get(index + 7),
                                polygon.normals().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glNormal3f( polygon.normals().get(index + 9),
                                polygon.normals().get(index + 10),
                                polygon.normals().get(index + 11) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_PC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glNormal3f( polygon.normals().get(con3),
                                polygon.normals().get(con3 + 1),
                                polygon.normals().get(con3 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_PCs_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );

                        index = 12 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glNormal3f( polygon.normals().get(index + 3),
                                polygon.normals().get(index + 4),
                                polygon.normals().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glNormal3f( polygon.normals().get(index + 6),
                                polygon.normals().get(index + 7),
                                polygon.normals().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glNormal3f( polygon.normals().get(index + 9),
                                polygon.normals().get(index + 10),
                                polygon.normals().get(index + 11) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_PCs_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glNormal3f( polygon.normals().get(con3),
                                polygon.normals().get(con3 + 1),
                                polygon.normals().get(con3 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_PCs_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );

                        index = 12 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glNormal3f( polygon.normals().get(index + 3),
                                polygon.normals().get(index + 4),
                                polygon.normals().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glNormal3f( polygon.normals().get(index + 6),
                                polygon.normals().get(index + 7),
                                polygon.normals().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glNormal3f( polygon.normals().get(index + 9),
                                polygon.normals().get(index + 10),
                                polygon.normals().get(index + 11) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_VN_PCs_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glNormal3f( polygon.normals().get(con0),
                                polygon.normals().get(con0 + 1),
                                polygon.normals().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glNormal3f( polygon.normals().get(con1),
                                polygon.normals().get(con1 + 1),
                                polygon.normals().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glNormal3f( polygon.normals().get(con2),
                                polygon.normals().get(con2 + 1),
                                polygon.normals().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glNormal3f( polygon.normals().get(con3),
                                polygon.normals().get(con3 + 1),
                                polygon.normals().get(con3 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },

        Type_Tri_PN_VC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nnormals(); i++ )
                    {
                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );

                        index = 9 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glColor4ub( polygon.colors().get(index + 3),
                                polygon.colors().get(index + 4),
                                polygon.colors().get(index + 5),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glColor4ub( polygon.colors().get(index + 6),
                                polygon.colors().get(index + 7),
                                polygon.colors().get(index + 8),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_VC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        
                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_VC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nnormals(); i++ )
                    {
                        int nindex = 3 * i;
                        gl.glNormal3f( polygon.normals().get(nindex),
                                polygon.normals().get(nindex+1),
                                polygon.normals().get(nindex+2) );

                        int vindex = 9 * i;
                        gl.glColor4ub( polygon.colors().get(vindex),
                                polygon.colors().get(vindex+1),
                                polygon.colors().get(vindex+2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(vindex),
                                polygon.coords().get(vindex+1),
                                polygon.coords().get(vindex+2) );
                        gl.glColor4ub( polygon.colors().get(vindex+3),
                                polygon.colors().get(vindex+4),
                                polygon.colors().get(vindex+5),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(vindex+3),
                                polygon.coords().get(vindex+4),
                                polygon.coords().get(vindex+5) );
                        gl.glColor4ub( polygon.colors().get(vindex+6),
                                polygon.colors().get(vindex+7),
                                polygon.colors().get(vindex+8),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(vindex+6),
                                polygon.coords().get(vindex+7),
                                polygon.coords().get(vindex+8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_VC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_PC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nnormals(); i++ )
                    {
                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );

                        index = 9 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_PC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_PC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nopacities(); i++ )
                    {
                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );

                        int index = 3 * i;

                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );

                        index = 9 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_PC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_PCs_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );

                        index = 9 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_PCs_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_PCs_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );

                        index = 9 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Tri_PN_PCs_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_TRIANGLES );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;

                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index + 1),
                                polygon.normals().get(index + 2) );

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                    }
                }
                gl.glEnd();
            }
        },

        Type_Quad_PN_VC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nnormals(); i++ )
                    {
                        int index = 12 * i;
                        gl.glNormal3f( polygon.normals().get(i),
                                    polygon.normals().get(i+1),
                                    polygon.normals().get(i+2) );
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glColor4ub( polygon.colors().get(index + 3),
                                polygon.colors().get(index + 4),
                                polygon.colors().get(index + 5),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glColor4ub( polygon.colors().get(index + 6),
                                polygon.colors().get(index + 7),
                                polygon.colors().get(index + 8),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glColor4ub( polygon.colors().get(index + 9),
                                polygon.colors().get(index + 10),
                                polygon.colors().get(index + 11),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_VC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glColor4ub( polygon.colors().get(con3),
                                polygon.colors().get(con3 + 1),
                                polygon.colors().get(con3 + 2),
                                polygon.opacity() );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_VC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nnormals(); i++ )
                    {
                        int nindex = 3 * i;
                        gl.glNormal3f( polygon.normals().get(nindex),
                                polygon.normals().get(nindex+1),
                                polygon.normals().get(nindex+2) );

                        int vindex = 12 * i;
                        gl.glColor4ub( polygon.colors().get(vindex),
                                polygon.colors().get(vindex+1),
                                polygon.colors().get(vindex+2),
                                polygon.opacity(nindex) );
                        gl.glVertex3f( polygon.coords().get(vindex),
                                polygon.coords().get(vindex+1),
                                polygon.coords().get(vindex+2) );
                        gl.glColor4ub( polygon.colors().get(vindex+3),
                                polygon.colors().get(vindex+4),
                                polygon.colors().get(vindex+5),
                                polygon.opacity(nindex + 1) );
                        gl.glVertex3f( polygon.coords().get(vindex+3),
                                polygon.coords().get(vindex+4),
                                polygon.coords().get(vindex+5) );
                        gl.glColor4ub( polygon.colors().get(vindex+6),
                                polygon.colors().get(vindex+7),
                                polygon.colors().get(vindex+8),
                                polygon.opacity(nindex + 2) );
                        gl.glVertex3f( polygon.coords().get(vindex+6),
                                polygon.coords().get(vindex+7),
                                polygon.coords().get(vindex+8) );
                        gl.glColor4ub( polygon.colors().get(vindex+9),
                                polygon.colors().get(vindex+10),
                                polygon.colors().get(vindex+11),
                                polygon.opacity(nindex + 3) );
                        gl.glVertex3f( polygon.coords().get(vindex+9),
                                polygon.coords().get(vindex+10),
                                polygon.coords().get(vindex+11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_VC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glColor4ub( polygon.colors().get(con0),
                                polygon.colors().get(con0 + 1),
                                polygon.colors().get(con0 + 2),
                                polygon.opacity(con0 / 3) );
                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glColor4ub( polygon.colors().get(con1),
                                polygon.colors().get(con1 + 1),
                                polygon.colors().get(con1 + 2),
                                polygon.opacity(con1 / 3) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glColor4ub( polygon.colors().get(con2),
                                polygon.colors().get(con2 + 1),
                                polygon.colors().get(con2 + 2),
                                polygon.opacity(con2 / 3) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glColor4ub( polygon.colors().get(con3),
                                polygon.colors().get(con3 + 1),
                                polygon.colors().get(con3 + 2),
                                polygon.opacity(con3 / 3) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_PC_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nnormals(); i++ )
                    {
                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 12 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_PC_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity() );

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_PC_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nopacities(); i++ )
                    {
                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );

                        int index = 3 * i;

                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 12 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_PC_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    final Color col = polygon.color();

                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        gl.glColor4ub( (byte)col.getRed(), (byte)col.getGreen(), (byte)col.getBlue(), polygon.opacity( i ) );

                        int index = 3 * i;
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_PCs_O {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 12 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_PCs_O_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity() );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_PCs_Os {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.ncolors(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 12 * i;
                        gl.glVertex3f( polygon.coords().get(index),
                                polygon.coords().get(index + 1),
                                polygon.coords().get(index + 2) );
                        gl.glVertex3f( polygon.coords().get(index + 3),
                                polygon.coords().get(index + 4),
                                polygon.coords().get(index + 5) );
                        gl.glVertex3f( polygon.coords().get(index + 6),
                                polygon.coords().get(index + 7),
                                polygon.coords().get(index + 8) );
                        gl.glVertex3f( polygon.coords().get(index + 9),
                                polygon.coords().get(index + 10),
                                polygon.coords().get(index + 11) );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Quad_PN_PCs_Os_Cs {
            @Override
            void rendering( PolygonObject polygon ) {
                GL gl = GLU.getCurrentGL(); 
                gl.glBegin( GL.GL_QUADS );
                {
                    for( int i = 0; i < polygon.nconnections(); i++ )
                    {
                        int index = 3 * i;
                        gl.glColor4ub( polygon.colors().get(index),
                                polygon.colors().get(index + 1),
                                polygon.colors().get(index + 2),
                                polygon.opacity( i ) );
                        gl.glNormal3f( polygon.normals().get(index),
                                polygon.normals().get(index+1),
                                polygon.normals().get(index+2) );

                        index = 4 * i;
                        int con0 = polygon.connections().get(index) * 3;
                        int con1 = polygon.connections().get(index + 1) * 3;
                        int con2 = polygon.connections().get(index + 2) * 3;
                        int con3 = polygon.connections().get(index + 3) * 3;

                        gl.glVertex3f( polygon.coords().get(con0),
                                polygon.coords().get(con0 + 1),
                                polygon.coords().get(con0 + 2) );
                        gl.glVertex3f( polygon.coords().get(con1),
                                polygon.coords().get(con1 + 1),
                                polygon.coords().get(con1 + 2) );
                        gl.glVertex3f( polygon.coords().get(con2),
                                polygon.coords().get(con2 + 1),
                                polygon.coords().get(con2 + 2) );
                        gl.glVertex3f( polygon.coords().get(con3),
                                polygon.coords().get(con3 + 1),
                                polygon.coords().get(con3 + 2) );
                    }
                }
                gl.glEnd();
            }
        };

        abstract void rendering( PolygonObject polygon );
    };

    public PolygonRenderer() {

    }

    @Override
    public void exec( ObjectBase object, Camera camera, Light light ) {
        GL gl = GLU.getCurrentGL(); 
        // kvs::IgnoreUnusedVariable( light );
        // kvs::IgnoreUnusedVariable( camera );

        PolygonObject polygon = (PolygonObject) object;

        gl.glPushAttrib( GL.GL_CURRENT_BIT | GL.GL_ENABLE_BIT );

        this.initialize();

        polygon.applyMaterial();

        gl.glEnable( GL.GL_DEPTH_TEST );
        {
            this.timer().start();
            this.PolygonRenderingFunction( polygon );
            this.timer().stop();
        }
        gl.glDisable( GL.GL_DEPTH_TEST );

        gl.glPopAttrib();
    }

    private void PolygonRenderingFunction( final PolygonObject polygon ) {
        if (polygon.nvertices() > 0) {
            PolygonRenderingType type = this.GetPolygonRenderingType( polygon );
            type.rendering( polygon );
        }
    };

    private PolygonRenderingType GetPolygonRenderingType( final PolygonObject polygon ) {
        final int nopacities = polygon.nopacities();
        final int ncolors = polygon.ncolors();
        final int nconnects = polygon.nconnections();
        final PolygonType polygon_type = polygon.polygonType();
        final NormalType normal_type = polygon.normalType();
        final ColorType color_type = polygon.colorType();

        if (polygon.normals().limit() == 0) {
            if (polygon_type == PolygonType.Triangle || polygon_type == PolygonType.Tri) {
                if (color_type == ColorType.VertexColor) {
                    if (nopacities == 1) {
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_VC_O
                                : PolygonRenderingType.Type_Tri_VC_O_Cs);
                    } else {
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_VC_Os
                                : PolygonRenderingType.Type_Tri_VC_Os_Cs);
                    }
                } else if (color_type == ColorType.PolygonColor) {
                    if (ncolors == 1) {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PC_O
                                    : PolygonRenderingType.Type_Tri_PC_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PC_Os
                                    : PolygonRenderingType.Type_Tri_PC_Os_Cs);
                    } else {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PCs_O
                                    : PolygonRenderingType.Type_Tri_PCs_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PCs_Os
                                    : PolygonRenderingType.Type_Tri_PCs_Os_Cs);
                    }
                }
            } else if (polygon_type == PolygonType.Quadrangle || polygon_type == PolygonType.Quad) {
                if (color_type == ColorType.VertexColor) {
                    if (nopacities == 1)
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_VC_O
                                : PolygonRenderingType.Type_Quad_VC_O_Cs);
                    else
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_VC_Os
                                : PolygonRenderingType.Type_Quad_VC_Os_Cs);
                } else if (color_type == ColorType.PolygonColor) {
                    if (ncolors == 1) {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PC_O
                                    : PolygonRenderingType.Type_Quad_PC_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PC_Os
                                    : PolygonRenderingType.Type_Quad_PC_Os_Cs);
                    } else {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PCs_O
                                    : PolygonRenderingType.Type_Quad_PCs_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PCs_Os
                                    : PolygonRenderingType.Type_Quad_PCs_Os_Cs);
                    }
                }
            }
        }

        else if (normal_type == NormalType.VertexNormal) {
            if (polygon_type == PolygonType.Triangle || polygon_type == PolygonType.Tri) {
                if (color_type == ColorType.VertexColor) {
                    if (nopacities == 1)
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_VN_VC_O
                                : PolygonRenderingType.Type_Tri_VN_VC_O_Cs);
                    else
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_VN_VC_Os
                                : PolygonRenderingType.Type_Tri_VN_VC_Os_Cs);
                } else if (color_type == ColorType.PolygonColor) {
                    if (ncolors == 1) {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_VN_PC_O
                                    : PolygonRenderingType.Type_Tri_VN_PC_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_VN_PC_Os
                                    : PolygonRenderingType.Type_Tri_VN_PC_Os_Cs);
                    } else {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_VN_PCs_O
                                    : PolygonRenderingType.Type_Tri_VN_PCs_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_VN_PCs_Os
                                    : PolygonRenderingType.Type_Tri_VN_PCs_Os_Cs);
                    }
                }
            } else if (polygon_type == PolygonType.Quadrangle || polygon_type == PolygonType.Quad) {
                if (color_type == ColorType.VertexColor) {
                    if (nopacities == 1)
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_VN_VC_O
                                : PolygonRenderingType.Type_Quad_VN_VC_O_Cs);
                    else
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_VN_VC_Os
                                : PolygonRenderingType.Type_Quad_VN_VC_Os_Cs);
                } else if (color_type == ColorType.PolygonColor) {
                    if (ncolors == 1) {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_VN_PC_O
                                    : PolygonRenderingType.Type_Quad_VN_PC_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_VN_PC_Os
                                    : PolygonRenderingType.Type_Quad_VN_PC_Os_Cs);
                    } else {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_VN_PCs_O
                                    : PolygonRenderingType.Type_Quad_VN_PCs_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_VN_PCs_Os
                                    : PolygonRenderingType.Type_Quad_VN_PCs_Os_Cs);
                    }
                }
            }
        }

        else if (normal_type == NormalType.PolygonNormal) {
            if (polygon_type == PolygonType.Triangle || polygon_type == PolygonType.Tri) {
                if (color_type == ColorType.VertexColor) {
                    if (nopacities == 1)
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PN_VC_O
                                : PolygonRenderingType.Type_Tri_PN_VC_O_Cs);
                    else
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PN_VC_Os
                                : PolygonRenderingType.Type_Tri_PN_VC_Os_Cs);
                } else if (color_type == ColorType.PolygonColor) {
                    if (ncolors == 1) {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PN_PC_O
                                    : PolygonRenderingType.Type_Tri_PN_PC_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PN_PC_Os
                                    : PolygonRenderingType.Type_Tri_PN_PC_Os_Cs);
                    } else {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PN_PCs_O
                                    : PolygonRenderingType.Type_Tri_PN_PCs_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Tri_PN_PCs_Os
                                    : PolygonRenderingType.Type_Tri_PN_PCs_Os_Cs);
                    }
                }
            }

            else if (polygon_type == PolygonType.Quadrangle || polygon_type == PolygonType.Quad) {
                if (color_type == ColorType.VertexColor) {
                    if (nopacities == 1)
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PN_VC_O
                                : PolygonRenderingType.Type_Quad_PN_VC_O_Cs);
                    else
                        return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PN_VC_Os
                                : PolygonRenderingType.Type_Quad_PN_VC_Os_Cs);
                } else if (color_type == ColorType.PolygonColor) {
                    if (ncolors == 1) {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PN_PC_O
                                    : PolygonRenderingType.Type_Quad_PN_PC_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PN_PC_Os
                                    : PolygonRenderingType.Type_Quad_PN_PC_Os_Cs);
                    } else {
                        if (nopacities == 1)
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PN_PCs_O
                                    : PolygonRenderingType.Type_Quad_PN_PCs_O_Cs);
                        else
                            return ((nconnects == 0) ? PolygonRenderingType.Type_Quad_PN_PCs_Os
                                    : PolygonRenderingType.Type_Quad_PN_PCs_Os_Cs);
                    }
                }
            }
        }

        return (PolygonRenderingType.Type_Tri_VC_O);
    }

    @Override
    protected void initialize_modelview() {
        GL gl = GLU.getCurrentGL(); 
        gl.glDisable( GL.GL_LINE_SMOOTH );

        gl.glEnable( GL.GL_BLEND );
        gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );

        gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_FILL );

        gl.glShadeModel( GL.GL_SMOOTH );

        gl.glColorMaterial( GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE );
        gl.glEnable( GL.GL_COLOR_MATERIAL );

        if( !this.isShading() )
        {
            gl.glDisable( GL.GL_NORMALIZE );
            gl.glDisable( GL.GL_LIGHTING );
        }
        else
        {
            gl.glEnable( GL.GL_NORMALIZE );
            gl.glEnable( GL.GL_LIGHTING );
        }
    }

    @Override
    protected void initialize_projection() {
        GL gl = GLU.getCurrentGL(); 
        gl.glMatrixMode( GL.GL_PROJECTION );

        gl.glMatrixMode( GL.GL_MODELVIEW );
    };

}
