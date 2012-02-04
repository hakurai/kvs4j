package kvs.core.visualization.viewer;
import java.awt.Color;
import java.io.Serializable;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.matrix.Vector4f;

public class Material implements Serializable{
    
    
    private static final long serialVersionUID = 1018555129849425604L;

    public enum MaterialFace
    {
        Front(GL.GL_FRONT), Back( GL.GL_BACK ) , FrontAndBack( GL.GL_FRONT_AND_BACK );
        
        private int m_value;
        
        private MaterialFace( int value) {
            this.m_value = value;
        }
        
        public final int intValue(){
            return this.m_value;
        }
    };
    
    
    private float[] m_ambient  = new float[4];  ///< ambient color
    private float[] m_diffuse  = new float[4];  ///< diffuse color
    private float[] m_specular = new float[4]; ///< specular color
    private float   m_shininess;   ///< shininess [0,128]
    private MaterialFace m_face;        ///< material face
    
    public Material(){
        this.initialize();
    }
    
    public Material( final Color ambient, final Color diffuse, final Color specular, float shininess ) {
        this( ambient, diffuse, specular, shininess, MaterialFace.Front);
    }
    
    public Material( final Color ambient, final Color diffuse, final Color specular, float shininess, MaterialFace face ) {
        setAmbient( ambient );
        setDiffuse( diffuse );
        setSpecular( specular );
        setShininess( shininess );
        m_face = face;
    }

    public Material( float ambi_r, float ambi_g, float ambi_b, float ambi_a,
        float diff_r, float diff_g, float diff_b, float diff_a,
        float spec_r, float spec_g, float spec_b, float spec_a,
        float shininess ){
        setAmbient( ambi_r, ambi_g, ambi_b, ambi_a);
        setDiffuse( diff_r, diff_g, diff_b, diff_a);
        setSpecular( spec_r, spec_g, spec_b, spec_a );
        setShininess( shininess );
        m_face = MaterialFace.Front;

    }
    
    public void initialize(){
        m_ambient[0]  = m_ambient[1]  = m_ambient[2]  = m_ambient[3]  = 1.0f;
        m_diffuse[0]  = m_diffuse[1]  = m_diffuse[2]  = m_diffuse[3]  = 1.0f;
        m_specular[0] = m_specular[1] = m_specular[2] = m_specular[3] = 1.0f;
        m_shininess   = 100.0f;
        m_face = MaterialFace.Front;
    }
    
    public final Vector4f getAmbient()
    {
        return new Vector4f( m_ambient );
    }

    public final Vector4f getDiffuse()
    {
        return new Vector4f( m_diffuse );
    }

    public final Vector4f getSpecular()
    {
        return new Vector4f( m_specular );
    }

    public final float getShininess()
    {
        return m_shininess;
    }

    public void setFace( MaterialFace face )
    {
        m_face = face;
    }
    
    public void setAmbient( float r, float g, float b, float a ){
        m_ambient[0] = r;
        m_ambient[1] = g;
        m_ambient[2] = b;
        m_ambient[3] = a;
    }
    
    public void setAmbient( final float[] ambient ){
        this.m_ambient = ambient;
    }
    
    public void setAmbient( final Color ambient ){
        this.m_ambient = ambient.getComponents( null );

    }
    
    public void setDiffuse( float r, float g, float b, float a ){
        m_diffuse[0] = r;
        m_diffuse[1] = g;
        m_diffuse[2] = b;
        m_diffuse[3] = a;
    }

    public void setDiffuse( final float[] diffuse ){
        this.m_diffuse = diffuse;
    }

    public void setDiffuse( final Color diffuse ){
        this.m_diffuse = diffuse.getComponents( null );
    }
    
    public void setSpecular( float r, float g, float b, float a ){
        m_specular[0] = r;
        m_specular[1] = g;
        m_specular[2] = b;
        m_specular[3] = a;
    }

    public void setSpecular( final float[] specular ){
        this.m_specular = specular;
    }

    public void setSpecular( Color specular ){
        this.m_specular = specular.getComponents( null );
    }

    public void setShininess( float shininess ){
        m_shininess = shininess;
    }

    public void apply(){
        GL gl = GLU.getCurrentGL();
        gl.glMaterialfv( m_face.intValue(), GL.GL_AMBIENT, m_ambient, 0 );
        gl.glMaterialfv( m_face.intValue(), GL.GL_DIFFUSE,   m_diffuse, 0 );
        gl.glMaterialfv( m_face.intValue(), GL.GL_SPECULAR,  m_specular, 0 );
        gl.glMaterialf( m_face.intValue(), GL.GL_SHININESS, m_shininess );
    }
    

}
