package kvs.core.visualization.viewer;

import java.nio.Buffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class FrameBuffer {

    protected int m_format; ///< pixel data format
    protected int m_type;   ///< pixel data type

    public FrameBuffer(){
        m_format = 0;
        m_type = 0;
    }

    public FrameBuffer( final int format, final int type ){
        m_format = format;
        m_type = type;
    }

    public void setFormat( final int format )
    {
        /* The following values are accepted:
         *     GL_COLOR_INDEX
         *     GL_STENCIL_INDEX
         *     GL_DEPTH_COMPONENT
         *     GL_RED
         *     GL_GREEN
         *     GL_BLUE
         *     GL_ALPHA
         *     GL_RGB
         *     GL_RGBA
         *     GL_LUMINANCE
         *     GL_LUMINANCE_ALPHA
         */
        m_format = format;
    }

    public void setType( final int type )
    {
        /* The following values are accepted:
         *     GL_UNSIGNED_BYTE
         *     GL_BYTE
         *     GL_BITMAP
         *     GL_UNSIGNED_SHORT
         *     GL_SHORT
         *     GL_UNSIGNED_INT
         *     GL_INT
         *     GL_FLOAT
         */
        m_type = type;
    }
    
    public void read(final int  width,
                     final int  height,
                     Buffer     pixels )
    {
        this.read( 0, 0, width, height, m_format, m_type, pixels, 0 );
    }

    public void read(final int  width,
                     final int  height,
                     Buffer     pixels,
                     final int  buffer)
    {
        this.read( 0, 0, width, height, m_format, m_type, pixels, buffer );
    }
    
    public void read(final int  x,
                     final int  y,
                     final int  width,
                     final int  height,
                     Buffer     pixels)
    {
        this.read( x, y, width, height, m_format, m_type, pixels, 0 );
    }

    public void read(final int  x,
                     final int  y,
                     final int  width,
                     final int  height,
                     Buffer     pixels,
                     final int  buffer )
    {
        this.read( x, y, width, height, m_format, m_type, pixels, buffer );
    }
    
    public void read(final int  x,
                     final int  y,
                     final int  width,
                     final int  height,
                     final int  format,
                     final int  type,
                     Buffer     pixels){
        this.read( x, y, width, height, format, type, pixels, 0 );
    }

    public void read(final int  x,
                     final int  y,
                     final int  width,
                     final int  height,
                     final int  format,
                     final int  type,
                     Buffer     pixels,
                     final int  buffer )
    {
        GL gl = GLU.getCurrentGL();
        gl.glPixelStorei( GL.GL_PACK_ALIGNMENT, 1 );

        if( buffer != 0 )
        {
            int[] current_buffer = new int[1];
            gl.glGetIntegerv( GL.GL_READ_BUFFER, current_buffer, 0 );
            gl.glReadBuffer( buffer );
            gl.glReadPixels( x, y, width, height, format, type, pixels );
            gl.glReadBuffer( current_buffer[0] );
        }
        else
        {
            gl.glReadPixels( x, y, width, height, format, type, pixels );
        }
    }
    
    public void draw(final int      width,
                     final int      height,
                     final Buffer   pixels)
    {
        this.draw( width, height, pixels, 0 );
    }
    
    public void draw(final int      width,
                     final int      height,
                     final Buffer   pixels,
                     final int      buffer )
    {
        GL gl = GLU.getCurrentGL();

        int[] viewport = new int[4];
        gl.glGetIntegerv( GL.GL_VIEWPORT, viewport, 0 );
        final int x = viewport[0];
        final int y = viewport[1];

        this.draw( x, y, width, height, m_format, m_type, viewport, pixels, buffer );
    }
    
    public void draw(final int      width,
                     final int      height,
                     final int[]    viewport,
                     final Buffer   pixels)
    {
        this.draw( width, height, viewport, pixels, 0 );
    }

    public void draw(final int      width,
                     final int      height,
                     final int[]    viewport,
                     final Buffer   pixels,
                     final int      buffer )
    {
        final int x = viewport[0];
        final int y = viewport[1];

        this.draw( x, y, width, height, m_format, m_type, viewport, pixels, buffer );
    }
    
    public void draw(final int      x,
                     final int      y,
                     final int      width,
                     final int      height,
                     final Buffer   pixels)
    {
        this.draw( x, y, width, height, pixels, 0 );
    }

    public void draw(final int      x,
                     final int      y,
                     final int      width,
                     final int      height,
                     final Buffer   pixels,
                     final int      buffer )
    {
        GL gl = GLU.getCurrentGL();

        int[] viewport = new int[4];
        gl.glGetIntegerv( GL.GL_VIEWPORT, viewport, 0 );

        this.draw( x, y, width, height, m_format, m_type, viewport, pixels, buffer );
    }
    
    public void draw(final int      x,
                     final int      y,
                     final int      width,
                     final int      height,
                     final int[]    viewport,
                     final Buffer   pixels)
    {
        this.draw( x, y, width, height, m_format, m_type, viewport, pixels, 0 );
    }

    public void draw(final int      x,
                     final int      y,
                     final int      width,
                     final int      height,
                     final int[]    viewport,
                     final Buffer   pixels,
                     final int      buffer )
    {
        this.draw( x, y, width, height, m_format, m_type, viewport, pixels, buffer );
    }
    
    public void draw(final int      x,
                     final int      y,
                     final int      width,
                     final int      height,
                     final int      format,
                     final int      type,
                     final int[]    viewport,
                     final Buffer   pixels)
    {
        this.draw( x, y, width, height, format, type, viewport, pixels, 0 );
    }

    public void draw(final int      x,
                     final int      y,
                     final int      width,
                     final int      height,
                     final int      format,
                     final int      type,
                     final int[]    viewport,
                     final Buffer   pixels,
                     final int      buffer )
    {
        GL gl = GLU.getCurrentGL();

        gl.glDisable( GL.GL_TEXTURE_1D );
        gl.glDisable( GL.GL_TEXTURE_2D );
        gl.glDisable( GL.GL_TEXTURE_3D );

        gl.glPixelStorei( GL.GL_UNPACK_ALIGNMENT, 1 );

        int[] current_buffer = new int[1];
        if( buffer != 0 )
        {
            gl.glGetIntegerv( GL.GL_DRAW_BUFFER, current_buffer, 0 );
            gl.glDrawBuffer( buffer );
        }

        final int left   = viewport[0];
        final int bottom = viewport[1];
        final int right  = viewport[2];
        final int top    = viewport[3];

        gl.glMatrixMode( GL.GL_PROJECTION );
        gl.glPushMatrix();
        {
            gl.glLoadIdentity();

            gl.glMatrixMode( GL.GL_MODELVIEW );
            gl.glPushMatrix();
            {
                gl.glLoadIdentity();
                gl.glOrtho( left, right, bottom, top, -1, 1 );

                gl.glRasterPos2i( x, y );
                gl.glDrawPixels( width, height, format, type, pixels );
            }
            gl.glMatrixMode( GL.GL_PROJECTION );
            gl.glPopMatrix();
            gl.glMatrixMode( GL.GL_MODELVIEW );
        }
        gl.glPopMatrix();

        if( buffer != 0 ) gl.glDrawBuffer( current_buffer[0] );
    }

}
