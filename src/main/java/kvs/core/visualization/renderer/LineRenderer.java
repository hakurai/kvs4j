package kvs.core.visualization.renderer;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.matrix.Vector3f;
import kvs.core.visualization.object.LineObject;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;

public class LineRenderer extends RendererBase {

    private static final long serialVersionUID = 7308763550782763351L;

    enum LineRenderingType
    {
        Type_Strip_VCs_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );
                gl.glBegin( GL.GL_LINE_STRIP );
                {
                    final int nvertices = line.nvertices();

                    for( int i = 0; i < nvertices; i++ )
                    {
                        final Color color    = line.color(i);
                        final Vector3f position = line.coord(i);

                        gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                        gl.glVertex3f( position.getX(), position.getY(), position.getZ() );
                    }
                }
                gl.glEnd();                
            }
        },
        Type_Strip_VCs_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final int nlines = line.nvertices() - 1;
                for( int i = 0; i < nlines; i++ )
                {
                    gl.glLineWidth( line.size(i) );

                    final Color color1    = line.color(i);
                    final Vector3f position1 = line.coord(i);
                    final Color color2    = line.color(i+1);
                    final Vector3f position2 = line.coord(i+1);

                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glColor3ub( (byte)color1.getRed(), (byte)color1.getGreen(), (byte)color1.getBlue() );
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glColor3ub( (byte)color2.getRed(), (byte)color2.getGreen(), (byte)color2.getBlue() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }                
            }
        },
        Type_Strip_LC_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                final int nvertices = line.nvertices();
                final Color color = line.color(0);

                gl.glBegin( GL.GL_LINE_STRIP );
                {
                    gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                    for( int i = 0; i < nvertices; i++ )
                    {
                        final Vector3f position = line.coord(i);
                        gl.glVertex3f( position.getX(), position.getY(), position.getZ() );
                    }
                }
                gl.glEnd();                
            }
        },
        Type_Strip_LCs_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                final int num = line.nvertices() - 1;

                gl.glBegin( GL.GL_LINES );
                {
                    for( int i = 0; i < num; i++ )
                    {
                        final Color color     = line.color(i);
                        final Vector3f position1 = line.coord(i);
                        final Vector3f position2 = line.coord(i+1);

                        gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                }
                gl.glEnd();                
            }
        },
        Type_Strip_LC_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final int         num   = line.nvertices() - 1;
                final Color color = line.color(0);
                gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );

                for( int i = 0; i < num; i++ )
                {
                    gl.glLineWidth( line.size( i ) );

                    final Vector3f position1 = line.coord(i);
                    final Vector3f position2 = line.coord(i+1);

                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }
            }
        },
        Type_Strip_LCs_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final int num = line.nvertices() - 1;

                for( int i = 0; i < num; i++ )
                {
                    final Color color     = line.color(i);
                    final Vector3f position1 = line.coord(i);
                    final Vector3f position2 = line.coord(i+1);

                    gl.glLineWidth( line.size( i ) );
                    gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );

                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }                
            }
        },

        Type_Uniline_VCs_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                gl.glBegin( GL.GL_LINE_STRIP );
                {
                    final int nconnections = line.nconnections();
                    for( int i = 0; i < nconnections; i++ )
                    {
                        int id = line.connections().get( i );

                        final Color color    = line.color(id);
                        final Vector3f position = line.coord(id);

                        gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                        gl.glVertex3f( position.getX(), position.getY(), position.getZ() );
                    }
                }
                gl.glEnd();                
            }
        },
        Type_Uniline_VCs_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final int num = line.nconnections() - 1;
                for( int i = 0; i < num; i++ )
                {
                    int id1 = line.connections().get( i );
                    int id2 = line.connections().get( i+1 );

                    final Color color1    = line.color(id1);
                    final Color color2    = line.color(id2);
                    final Vector3f position1 = line.coord(id1);
                    final Vector3f position2 = line.coord(id2);

                    gl.glLineWidth( line.size( i ) );
                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glColor3ub( (byte)color1.getRed(), (byte)color1.getGreen(), (byte)color1.getBlue() );
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glColor3ub( (byte)color2.getRed(), (byte)color2.getGreen(), (byte)color2.getBlue() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }                
            }
        },
        Type_Uniline_LC_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                gl.glBegin( GL.GL_LINE_STRIP );
                {
                    final Color color = line.color(0);
                    gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );

                    final int nconnections = line.nconnections();

                    for( int i = 0; i < nconnections; i++ )
                    {
                        final Vector3f position = line.coord(i);
                        gl.glVertex3d( position.getX(), position.getY(), position.getZ() );
                    }
                }
                gl.glEnd();                
            }
        },
        Type_Uniline_LCs_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                gl.glBegin( GL.GL_LINES );
                {
                    final int num = line.nconnections() - 1;
                    for( int i = 0; i < num; i++ )
                    {
                        final Color color     = line.color(i);
                        final Vector3f position1 = line.coord(i);
                        final Vector3f position2 = line.coord(i+1);

                        gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                }
                gl.glEnd();                
            }
        },
        Type_Uniline_LC_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final Color color = line.color(0);
                gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );

                final int num = line.nconnections() - 1;

                for( int i = 0; i < num; i++ )
                {
                    final Vector3f position1 = line.coord(i);
                    final Vector3f position2 = line.coord(i+1);

                    gl.glLineWidth( line.size( i ) );
                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glVertex3d( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3d( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }                
            }
        },
        Type_Uniline_LCs_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final int num = line.nconnections() - 1;
                for( int i = 0; i < num; i++ )
                {
                    final Color color     = line.color(i);
                    final Vector3f position1 = line.coord(i);
                    final Vector3f position2 = line.coord(i+1);

                    gl.glLineWidth( line.size( i ) );
                    gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }                
            }
        },

        Type_Polyline_VCs_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final int index = 2 * i;
                    final int id1   = line.connections().get( index );
                    final int id2   = line.connections().get( index + 1 );

                    gl.glBegin( GL.GL_LINE_STRIP );
                    {
                        for( int j = id1; j <= id2; j++ )
                        {
                            final Color color    = line.color(j);
                            final Vector3f position = line.coord(j);

                            gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                            gl.glVertex3f( position.getX(), position.getY(), position.getZ() );
                        }
                    }
                    gl.glEnd();
                }                
            }
        },
        Type_Polyline_VCs_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                int ctr = 0;
                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final int index = 2*i;
                    final int id1   = line.connections().get( index );
                    final int id2   = line.connections().get( index + 1 );

                    for( int j = id1; j < id2; j++ )
                    {
                        final Color color1    = line.color(j);
                        final Color color2    = line.color(j+1);
                        final Vector3f position1 = line.coord(j);
                        final Vector3f position2 = line.coord(j+1);

                        gl.glLineWidth( line.size( ctr ) );
                        gl.glBegin( GL.GL_LINES );
                        {
                            gl.glColor3ub( (byte)color1.getRed(), (byte)color1.getGreen(), (byte)color1.getBlue() );
                            gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                            gl.glColor3ub( (byte)color2.getRed(), (byte)color2.getGreen(), (byte)color2.getBlue() );
                            gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                        }
                        gl.glEnd();

                        ctr++;
                    }
                }                
            }
        },
        Type_Polyline_LC_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final int index = 2 * i;
                    final int id1   = line.connections().get( index );
                    final int id2   = line.connections().get( index + 1 );

                    gl.glBegin( GL.GL_LINE_STRIP );
                    {
                        final Color color = line.color(0);
                        gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );

                        for( int j = id1; j < id2; j++ )
                        {
                            final Vector3f position1 = line.coord(j);
                            final Vector3f position2 = line.coord(j+1);

                            gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                            gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                        }
                    }
                    gl.glEnd();
                }                
            }
        },
        Type_Polyline_LCs_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                int ctr = 0;
                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final int index = 2 * i;
                    final int id1   = line.connections().get( index );
                    final int id2   = line.connections().get( index + 1 );

                    gl.glBegin( GL.GL_LINE_STRIP );
                    {
                        for( int j = id1; j < id2; j++ )
                        {
                            final Vector3f position1 = line.coord(j);
                            final Vector3f position2 = line.coord(j+1);
                            final Color color     = line.color(ctr);

                            gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                            gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                            gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                            ctr++;
                        }
                    }
                    gl.glEnd();
                }                
            }
        },
        Type_Polyline_LC_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                int ctr = 0;
                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final int index = 2*i;
                    final int id1   = line.connections().get( index );
                    final int id2   = line.connections().get( index + 1 );

                    final Color color = line.color(0);
                    gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );

                    for( int j = id1; j < id2; j++ )
                    {
                        final Vector3f position1 = line.coord(j);
                        final Vector3f position2 = line.coord(j+1);

                        gl.glLineWidth( line.size( ctr ) );
                        gl.glBegin( GL.GL_LINES );
                        {
                            gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                            gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                        }
                        gl.glEnd();

                        ctr++;
                    }
                }                
            }
        },
        Type_Polyline_LCs_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                int ctr = 0;
                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final int index = 2*i;
                    final int id1   = line.connections().get( index );
                    final int id2   = line.connections().get( index + 1 );

                    for( int j = id1; j < id2; j++ )
                    {
                        final Color color     = line.color(ctr);
                        final Vector3f position1 = line.coord(j);
                        final Vector3f position2 = line.coord(j+1);

                        gl.glLineWidth( line.size( ctr ) );
                        gl.glBegin( GL.GL_LINES );
                        {
                            gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                            gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                            gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                        }
                        gl.glEnd();

                        ctr++;
                    }
                }
            }
        },

        Type_Segment_VCs_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                gl.glBegin( GL.GL_LINES );
                {
                    final int num = line.nconnections() * 2;
                    for( int i = 0; i < num; i++ )
                    {
                        final int id = line.connections().get( i );

                        final Color color    = line.color(id);
                        final Vector3f position = line.coord(id);

                        gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                        gl.glVertex3f( position.getX(), position.getY(), position.getZ() );
                    }
                }
                gl.glEnd();
            }
        },
        Type_Segment_VCs_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final int index = 2*i;
                    final int id1   = line.connections().get( index );
                    final int id2   = line.connections().get( index + 1 );

                    final Color color1    = line.color(id1);
                    final Color color2    = line.color(id2);
                    final Vector3f position1 = line.coord(id1);
                    final Vector3f position2 = line.coord(id2);

                    gl.glLineWidth( line.size( i ) );
                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glColor3ub( (byte)color1.getRed(), (byte)color1.getGreen(), (byte)color1.getBlue() );
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glColor3ub( (byte)color2.getRed(), (byte)color2.getGreen(), (byte)color2.getBlue() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }                
            }
        },
        Type_Segment_LC_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                final Color color = line.color(0);
                gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );

                gl.glBegin( GL.GL_LINES );
                {
                    final int num = line.nconnections() * 2;
                    for( int i = 0; i < num; i++ )
                    {
                        final int id = line.connections().get( i );
                        final Vector3f position = line.coord(id);

                        gl.glVertex3f( position.getX(), position.getY(), position.getZ() );
                    }
                }
                gl.glEnd();                
            }
        },
        Type_Segment_LCs_S {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                gl.glLineWidth( line.size( 0 ) );

                gl.glBegin( GL.GL_LINES );
                {
                    final int nconnections = line.nconnections();
                    for( int i = 0; i < nconnections; i++ )
                    {
                        final Color color = line.color(i);

                        final int index = 2 * i;
                        final int id1 = line.connections().get( index );
                        final int id2 = line.connections().get( index + 1 );

                        final Vector3f position1 = line.coord(id1);
                        final Vector3f position2 = line.coord(id2);

                        gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                }
                gl.glEnd();                
            }
        },
        Type_Segment_LC_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final Color color = line.color(0);
                gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );

                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final int index = 2*i;
                    final int id1   = line.connections().get( index );
                    final int id2   = line.connections().get( index + 1 );

                    final Vector3f position1 = line.coord(id1);
                    final Vector3f position2 = line.coord(id2);

                    gl.glLineWidth( line.size( i ) );
                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }                
            }
        },
        Type_Segment_LCs_Ss {
            @Override
            void rendering( LineObject line ) {
                GL gl = GLU.getCurrentGL();
                final int nconnections = line.nconnections();
                for( int i = 0; i < nconnections; i++ )
                {
                    final Color color = line.color(i);

                    final int index = 2 * i;
                    final int id1 = line.connections().get( index );
                    final int id2 = line.connections().get( index + 1 );
                    final Vector3f position1 = line.coord(id1);
                    final Vector3f position2 = line.coord(id2);

                    gl.glColor3ub( (byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue() );
                    gl.glLineWidth( line.size( i ) );

                    gl.glBegin( GL.GL_LINES );
                    {
                        gl.glVertex3f( position1.getX(), position1.getY(), position1.getZ() );
                        gl.glVertex3f( position2.getX(), position2.getY(), position2.getZ() );
                    }
                    gl.glEnd();
                }
            }
        };
        
        abstract void rendering( LineObject line );
    };

    private LineRenderingType getLineRenderingType( LineObject line )
    {
        final int nsizes    = line.nsizes();
        final int ncolors   = line.ncolors();

        switch( line.lineType() )
        {
        case Strip:
            switch( line.colorType() )
            {
            case VertexColor:
            {
                return( ( nsizes == 1 ) ?
                        LineRenderingType.Type_Strip_VCs_S :
                            LineRenderingType.Type_Strip_VCs_Ss );
            }
            default:
            {
                if( nsizes == 1 )
                    return( ( ncolors == 1 ) ?
                            LineRenderingType.Type_Strip_LC_S :
                                LineRenderingType.Type_Strip_LCs_S );
                else
                    return( ( ncolors == 1 ) ?
                            LineRenderingType.Type_Strip_LC_Ss : 
                                LineRenderingType.Type_Strip_LCs_Ss ); 
            } 
            }
        case Uniline:
            switch( line.colorType() )
            {
            case VertexColor:
            {
                return( ( nsizes == 1 ) ?
                        LineRenderingType.Type_Uniline_VCs_S :
                            LineRenderingType.Type_Uniline_VCs_Ss );
            }
            default:
            {
                if( nsizes == 1 )
                    return( ( ncolors == 1 ) ?
                            LineRenderingType.Type_Uniline_LC_S :
                                LineRenderingType.Type_Uniline_LCs_S );
                else
                    return( ( ncolors == 1 ) ?
                            LineRenderingType.Type_Uniline_LC_Ss : 
                                LineRenderingType.Type_Uniline_LCs_Ss ); 
            } 
            }
        case Polyline:
            switch( line.colorType() )
            {
            case VertexColor:
            {
                return( ( nsizes == 1 ) ?
                        LineRenderingType.Type_Polyline_VCs_S :
                            LineRenderingType.Type_Polyline_VCs_Ss );
            }
            default:
            {
                if( nsizes == 1 )
                    return( ( ncolors == 1 ) ?
                            LineRenderingType.Type_Polyline_LC_S :
                                LineRenderingType.Type_Polyline_LCs_S );
                else
                    return( ( ncolors == 1 ) ?
                            LineRenderingType.Type_Polyline_LC_Ss : 
                                LineRenderingType.Type_Polyline_LCs_Ss ); 
            } 
            }
        case Segment:
            switch( line.colorType() )
            {
            case VertexColor:
            {
                return( ( nsizes == 1 ) ?
                        LineRenderingType.Type_Segment_VCs_S :
                            LineRenderingType.Type_Segment_VCs_Ss );
            }
            default:
            {
                if( nsizes == 1 )
                    return( ( ncolors == 1 ) ?
                            LineRenderingType.Type_Segment_LC_S :
                                LineRenderingType.Type_Segment_LCs_S );
                else
                    return( ( ncolors == 1 ) ?
                            LineRenderingType.Type_Segment_LC_Ss : 
                                LineRenderingType.Type_Segment_LCs_Ss ); 
            } 
            }
        default: break;
        }

        return( LineRenderingType.Type_Strip_VCs_S );
    };


    void LineRenderingFunction( final LineObject line )
    {
        if( line.nvertices() > 0 )
        {
            LineRenderingType type = getLineRenderingType( line );
            type.rendering( line );
        }
    };

    @Override
    public void exec( ObjectBase object, Camera camera, Light light ) {

        GL gl = GLU.getCurrentGL();
        //kvs::IgnoreUnusedVariable( light );
        //kvs::IgnoreUnusedVariable( camera );

        LineObject line = LineObject.valueOf( object );

        gl.glPushAttrib( GL.GL_CURRENT_BIT | GL.GL_ENABLE_BIT );

        this.initialize();

        gl.glEnable( GL.GL_DEPTH_TEST );
        {
            this.timer().start();
            LineRenderingFunction( line );
            this.timer().stop();
        }
        gl.glDisable( GL.GL_DEPTH_TEST );

        gl.glPopAttrib();
    }

    @Override
    protected void initialize_modelview() {
    }

    @Override
    protected void initialize_projection() {
    }

}
