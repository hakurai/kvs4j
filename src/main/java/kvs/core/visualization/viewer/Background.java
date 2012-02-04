package kvs.core.visualization.viewer;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class Background {

    public enum Type
    {
        MonoColor,    ///< mono color background
        TwoSideColor,     ///< gradation color background using two colors
        FourCornersColor, ///< gradation color background using four colors
        //Image             ///< image background
    };

    protected Type           m_type;     ///< background type
    protected Color[] m_color = {Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK,};

    /**
     * 黒単色の新しい背景を生成します。
     **/
    public Background(){
        m_type = Type.MonoColor;
    }

    /**
     * 指定された単色の背景を生成します。
     * 
     * @param color 背景色
     **/
    public Background( final Color color ){
        this.setColor( color );
    }

    /**
     * 指定された2色によるグラデーションの背景を生成します。
     * 
     * @param color0 上の色
     * @param color1 下の色
     */
    public Background( final Color color0, final Color color1 ){
        this.setColor( color0, color1 );
    }

    /**
     * 指定された4色によるグラデーションの背景を生成します。
     * 
     * @param color0 左下の色
     * @param color1 右下の色
     * @param color2 右上の色
     * @param color3 左上の色
     */
    public Background( final Color color0, final Color color1, final Color color2, final Color color3 ){
        this.setColor( color0, color1, color2, color3 );
    }

    /**
     * この背景を指定された単色に設定します。
     * 
     * @param color 背景色
     */
    public void setColor( final Color color ){
        m_type     = Type.MonoColor;
        m_color[0] = color;
    }

    /**
     * この背景を指定された2色によるグラデーションに設定します。
     * 
     * @param color0 上の色
     * @param color1 下の色
     */
    public void setColor( final Color color0, final Color color1 ){
        m_type     = Type.TwoSideColor;
        m_color[0] = color0;
        m_color[1] = color0;
        m_color[2] = color1;
        m_color[3] = color1;
    }

    /**
     * この背景を指定された4色によるグラデーションに設定します。
     * 
     * @param color0 左下の色
     * @param color1 右下の色
     * @param color2 右上の色
     * @param color3 左上の色
     */
    public void setColor(
            final Color color0,
            final Color color1,
            final Color color2,
            final Color color3 ){
        m_type     = Type.FourCornersColor;
        m_color[0] = color0;
        m_color[1] = color1;
        m_color[2] = color2;
        m_color[3] = color3;
    }
    
    /**
     * この背景色のタイプを返します。
     * 
     * @return MonoColor 単色
     *         TwoSideColor 2色のグラデーション
     *         FourCornersColor 4色のグラデーション
     */
    public Type type(){
        return m_type;
    }
    
    /**
     * 指定されたインデックスの位置の色を返します。
     * 
     * @param index　取得する位置のインデックス
     * @return 色
     */
    public Color getColor( int index ){
        return( m_color[index] );
    }
    
    /**
     * 現在のこの背景の設定を反映させます。
     */
    public void apply(){
        GL gl = GLU.getCurrentGL();
        if(m_type == Type.MonoColor){
            this.apply_mono_color();
        } else if(m_type == Type.TwoSideColor){
            this.apply_gradation_color();
        } else if(m_type == Type.FourCornersColor){
            this.apply_gradation_color();
        }
        gl.glFlush();
    }
    
    private void apply_mono_color(){
        GL gl = GLU.getCurrentGL();
        float[] rgb = m_color[0].getRGBColorComponents(null);
        gl.glClearColor( rgb[0], rgb[1], rgb[2], 1.0f );
        gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
    }
    
    private void apply_gradation_color(){
        GL gl = GLU.getCurrentGL();
        // Clear bits.
        gl.glClear( GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );

        gl.glPushAttrib( GL.GL_CURRENT_BIT | GL.GL_ENABLE_BIT );

        // Disable OpenGL parameters.
        gl.glDisable( GL.GL_DEPTH_TEST );

        // Draw a gradation plane on the background.
        gl.glMatrixMode( GL.GL_MODELVIEW );
        gl.glPushMatrix();
        {
            gl.glLoadIdentity();

            gl.glMatrixMode( GL.GL_PROJECTION );
            gl.glPushMatrix();
            {
                gl.glLoadIdentity();
                gl.glOrtho( 0, 1, 0, 1, -1, 1 );

                // Gradation plane.
                gl.glBegin( GL.GL_QUADS );
                gl.glColor3ub( (byte)m_color[0].getRed(), (byte)m_color[0].getGreen(), (byte)m_color[0].getBlue() );
                gl.glVertex2d( 0.0, 0.0 );
                gl.glColor3ub( (byte)m_color[1].getRed(), (byte)m_color[1].getGreen(), (byte)m_color[1].getBlue() );
                gl.glVertex2d( 1.0, 0.0 );
                gl.glColor3ub( (byte)m_color[2].getRed(), (byte)m_color[2].getGreen(), (byte)m_color[2].getBlue() );
                gl.glVertex2d( 1.0, 1.0 );
                gl.glColor3ub( (byte)m_color[3].getRed(), (byte)m_color[3].getGreen(), (byte)m_color[3].getBlue() );
                gl.glVertex2d( 0.0, 1.0 );
                gl.glEnd(); 
            }
            gl.glPopMatrix();
            gl.glMatrixMode( GL.GL_MODELVIEW );
        }
        gl.glPopMatrix();

        gl.glClearDepth(1000);
        gl.glEnable( GL.GL_DEPTH_TEST );

        gl.glPopAttrib();
    }

}
