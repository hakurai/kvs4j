package kvs.core.visualization.object;

import java.io.Serializable;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.util.Math;
import kvs.core.matrix.Matrix33f;
import kvs.core.matrix.Vector2f;
import kvs.core.matrix.Vector3f;
import kvs.core.visualization.pipeline.PipelineModule;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Material;
import kvs.core.visualization.viewer.XformControl;

public abstract class ObjectBase extends XformControl implements PipelineModule, Serializable{
    /*
    public enum Face
    {
        Front(Material.MaterialFace.Front.intValue()),
        Back(Material.MaterialFace.Back.intValue()) ,
        FrontAndBack(Material.MaterialFace.FrontAndBack.intValue());

        private int m_value;

        private Face( int value ) {
            this.m_value = value;
        }

        public final int intValue(){
            return this.m_value;
        }
    };
     */

    private static final long serialVersionUID = -3421612758614231859L;

    public enum ObjectType
    {
        Geometry, ///< Geometric object.
        Volume,       ///< Volumetric object.
        Image,        ///< Image object
        ObjectManager ///< Object manager
    };

    protected Vector3f m_min_object_coord  = new Vector3f( -3.0f, -3.0f, -3.0f );   ///< min coord in the object coordinate system
    protected Vector3f m_max_object_coord = new Vector3f(  3.0f,  3.0f,  3.0f );    ///< max coord in the object coordinate system
    protected Vector3f m_min_external_coord  = new Vector3f( -3.0f, -3.0f, -3.0f ); ///< min coord in the external coordinate system
    protected Vector3f m_max_external_coord  = new Vector3f(  3.0f,  3.0f,  3.0f ); ///< max coord in the external coordinate system
    protected boolean  m_has_min_max_object_coords = false;                         ///< has min-max coorinate values ?
    protected boolean  m_has_min_max_external_coords = false;                       ///< has min-max coorinate values ?
    protected Vector3f m_object_center;                                             ///< center of gravity in object coordinate system
    protected Vector3f m_external_position;                                         ///< position in external coordinate system
    protected Vector3f m_normalize;                                                 ///< normalize parameter
    protected Material m_material = new Material();                                 ///< material
    protected boolean  m_show_flg = true;
    
    public ObjectBase() {
        this( true );
    }

    public ObjectBase(boolean collision) {
        super(collision);
        this.m_min_object_coord = new Vector3f( -3.0f, -3.0f, -3.0f );
        this.m_max_object_coord = new Vector3f(  3.0f,  3.0f,  3.0f );
        this.m_min_external_coord = new Vector3f( -3.0f, -3.0f, -3.0f );
        this.m_max_external_coord = new Vector3f(  3.0f,  3.0f,  3.0f );
        this.m_has_min_max_object_coords = false;
        this.m_has_min_max_external_coords = false;
        this.m_show_flg = true;
    }

    public ObjectBase(
            final Vector3f  t,
            final Vector3f  s,
            final Matrix33f r,
            boolean                  collision ){
        super( t, s, r, collision );
        this.m_min_object_coord = new Vector3f( -3.0f, -3.0f, -3.0f );
        this.m_max_object_coord = new Vector3f(  3.0f,  3.0f,  3.0f );
        this.m_min_external_coord = new Vector3f( -3.0f, -3.0f, -3.0f );
        this.m_max_external_coord = new Vector3f(  3.0f,  3.0f,  3.0f );
        this.m_has_min_max_object_coords = false;
        this.m_has_min_max_external_coords = false;
        this.m_show_flg = true;
    }

    public void setMinMaxObjectCoords(
            final Vector3f min_coord,
            final Vector3f max_coord ){
        m_min_object_coord = min_coord;
        m_max_object_coord = max_coord;

        m_has_min_max_object_coords = true;

        this.updateNormalizeParameters();
    }

    public void setMinMaxExternalCoords(
            final Vector3f min_coord,
            final Vector3f max_coord ){

        m_min_external_coord = min_coord;
        m_max_external_coord = max_coord;

        m_has_min_max_external_coords = true;

        this.updateNormalizeParameters();
    }

    public void setMaterial( final Material material )
    {
        m_material = material;
    }

    public void setFace( final Material.MaterialFace face )
    {
        m_material.setFace( face );
    }

    public void show()
    {
        m_show_flg = true;
    }

    public void hide()
    {
        m_show_flg = false;
    }
    
    public abstract ObjectType objectType();

    public final Vector3f minObjectCoord()
    {
        return m_min_object_coord;
    }

    public final Vector3f maxObjectCoord()
    {
        return m_max_object_coord;
    }

    public final Vector3f minExternalCoord()
    {
        return m_min_external_coord;
    }

    public final Vector3f maxExternalCoord()
    {
        return m_max_external_coord;
    }

    public final boolean hasMinMaxObjectCoords()
    {
        return m_has_min_max_object_coords;
    }

    public final boolean hasMinMaxExternalCoords()
    {
        return m_has_min_max_external_coords;
    }

    public final Vector3f objectCenter()
    {
        return m_object_center;
    }

    public final Vector3f externalPosition()
    {
        return m_external_position;
    }

    public final Vector3f normalize()
    {
        return m_normalize;
    }

    public final boolean isShown()
    {
        return m_show_flg;
    }

    public final Material material()
    {
        return m_material;
    }
    
    public final ObjectBase exec( ObjectBase object ){
        return this;
    }

    public final Vector2f positionInDevice(
            Camera         camera,
            final Vector3f global_trans,
            final Vector3f global_scale )
    {
        GL gl = GLU.getCurrentGL();
        Vector2f ret;
        gl.glPushMatrix();
        {
            camera.update();

            transform( global_trans, global_scale );

            ret     = camera.projectObjectToWindow( m_object_center );
            ret =  new Vector2f(ret.getX(), camera.getWindowHeight() - ret.getY());
        }
        gl.glPopMatrix();

        return( ret );
    }

    public final Vector3f positionInWorld(
            final Vector3f global_trans,
            final Vector3f global_scale )
    {
        Vector3f init_pos = m_external_position.sub(global_trans);

        float x = init_pos.getX() * global_scale.getX();
        float y = init_pos.getY() * global_scale.getY();
        float z = init_pos.getZ() * global_scale.getZ();

        init_pos = new Vector3f(x, y, z);

        return( this.getTranslation().add( init_pos.mul(this.scaledRotation()) ) );
    }

    public final Vector3f positionInExternal()
    {
        return( m_external_position );
    }

    public void updateNormalizeParameters()
    {
        Vector3f diff_obj = m_max_object_coord.sub( m_min_object_coord );
        Vector3f diff_ext = m_max_external_coord.sub( m_min_external_coord );

        m_object_center = ( m_max_object_coord.add( m_min_object_coord ) ).mul( 0.5f );
        m_external_position = ( m_max_external_coord.add( m_min_external_coord ) ).mul( 0.5f );

        float x = ( Math.equal( diff_obj.getX(), 0.0f ) ) ?
                Math.max( diff_ext.getY() / diff_obj.getY(), diff_ext.getZ() / diff_obj.getZ() ):
                    diff_ext.getX() / diff_obj.getX();

                float y = ( Math.equal( diff_obj.getY(), 0.0f ) ) ?
                        Math.max( diff_ext.getX() / diff_obj.getX(), diff_ext.getZ() / diff_obj.getZ() ):
                            diff_ext.getY() / diff_obj.getY();

                        float z = ( Math.equal( diff_obj.getZ(), 0.0f ) ) ?
                                Math.max( diff_ext.getX() / diff_obj.getX(), diff_ext.getY() / diff_obj.getY() ):
                                    diff_ext.getZ() / diff_obj.getZ();

                                this.m_normalize = new Vector3f( x, y, z);
    }

    public final void transform(
            final Vector3f global_trans,
            final Vector3f global_scale )
    {
        GL gl = GLU.getCurrentGL();
        /* Apply the transformation from the world coordinate system by using
         * the object's xform. You see also kvs::XformControl class and kvs::Xform
         * class in detail.
         */
        this.applyXform();

        gl.glScalef( global_scale.getX(), global_scale.getY(), global_scale.getZ() );

        gl.glTranslatef( -global_trans.getX(),
                -global_trans.getY(),
                -global_trans.getZ() );

        gl.glTranslatef( m_external_position.getX(),
                m_external_position.getY(),
                m_external_position.getZ() );

        gl.glScalef( m_normalize.getX(), m_normalize.getY(), m_normalize.getZ() );

        gl.glTranslatef( -m_object_center.getX(),
                -m_object_center.getY(),
                -m_object_center.getZ() );
    }
    
    public void applyMaterial()
    {
        m_material.apply();
    }
    
    public boolean collision(
            final Vector2f p_win,
            final Camera         camera,
            final Vector3f global_trans,
            final Vector3f global_scale )
        {
        GL gl = GLU.getCurrentGL();

        float max_distance = -1.0f;

        // Center of this object in the window coordinate system.
        Vector2f center;

        gl.glPushMatrix();
        {
            camera.update();

            this.transform( global_trans, global_scale );

            center = camera.projectObjectToWindow( m_object_center );

                // Object's corner points in the object coordinate system.
                final Vector3f[] corners = {
                    new Vector3f( m_min_object_coord.getX(),
                                  m_min_object_coord.getY(),
                                  m_min_object_coord.getZ() ),
                    new Vector3f( m_max_object_coord.getX(),
                                  m_min_object_coord.getY(),
                                  m_min_object_coord.getZ() ),
                    new Vector3f( m_min_object_coord.getX(),
                                  m_min_object_coord.getY(),
                                  m_max_object_coord.getZ() ),
                    new Vector3f( m_max_object_coord.getX(),
                                  m_min_object_coord.getY(),
                                  m_max_object_coord.getZ() ),
                    new Vector3f( m_min_object_coord.getX(),
                                  m_max_object_coord.getY(),
                                  m_min_object_coord.getZ() ),
                    new Vector3f( m_max_object_coord.getX(),
                                  m_max_object_coord.getY(),
                                  m_min_object_coord.getZ() ),
                    new Vector3f( m_min_object_coord.getX(),
                                  m_max_object_coord.getY(),
                                  m_max_object_coord.getZ() ),
                    new Vector3f( m_max_object_coord.getX(),
                                  m_max_object_coord.getY(),
                                  m_max_object_coord.getZ() ) };

                // Calculate max distance between the center and the corner in
                // the window coordinate system.
                for( int i = 0; i < 8; i++ )
                {
                    Vector2f corner = camera.projectObjectToWindow( corners[i] );
                    float distance = ( float )( corner.sub( center ) ).length();
                    max_distance = Math.max( max_distance, distance );
                }
        }
        gl.glPopMatrix();

        Vector2f pos_window = new Vector2f( p_win.getX(), camera.getWindowHeight() - p_win.getY() );

        return( ( float )( pos_window.sub( center ) ).length() < max_distance );
        }
    
    public boolean collision(
            final Vector3f p_world,
            final Vector3f global_trans,
            final Vector3f global_scale )
    {
        GL gl = GLU.getCurrentGL();

        float max_distance = -1.0f;
        Vector3f center;

        gl.glPushMatrix();
        {
            this.transform( global_trans, global_scale );

            center = object_to_world_coordinate( m_object_center,
                    global_trans,
                    global_scale );

                // Object's corner points in the object coordinate system.
                final Vector3f[] corners = {
                    new Vector3f( m_min_object_coord.getX(),
                                   m_min_object_coord.getY(),
                                   m_min_object_coord.getZ() ),
                    new Vector3f( m_max_object_coord.getX(),
                                   m_min_object_coord.getY(),
                                   m_min_object_coord.getZ() ),
                    new Vector3f( m_min_object_coord.getX(),
                                   m_min_object_coord.getY(),
                                   m_max_object_coord.getZ() ),
                    new Vector3f( m_max_object_coord.getX(),
                                   m_min_object_coord.getY(),
                                   m_max_object_coord.getZ() ),
                    new Vector3f( m_min_object_coord.getX(),
                                   m_max_object_coord.getY(),
                                   m_min_object_coord.getZ() ),
                    new Vector3f( m_max_object_coord.getX(),
                                   m_max_object_coord.getY(),
                                   m_min_object_coord.getZ() ),
                    new Vector3f( m_min_object_coord.getX(),
                                   m_max_object_coord.getY(),
                                   m_max_object_coord.getZ() ),
                    new Vector3f( m_max_object_coord.getX(),
                                   m_max_object_coord.getY(),
                                   m_max_object_coord.getZ() ) };

                // Calculate max distance between the center and the corner in
                // the world coordinate system.
                for( int i = 0; i < 8; i++ )
                {
                    Vector3f corner = this.object_to_world_coordinate( corners[i], global_trans, global_scale );
                    float distance = ( float )( corner.sub( center ) ).length();
                    max_distance = Math.max( max_distance, distance );
                }
        }
        gl.glPopMatrix();

        return( ( float )( p_world.sub( center ) ).length() < max_distance );
    }
    
    public final void rotate(
            final Matrix33f rot,
            final Vector3f  center )
        {
            this.translateXform( center.opposite() );
            this.updateRotation( rot );
            this.translateXform( center );
        }
    
    public final void scale(
            final Vector3f scale,
            final Vector3f center )
        {
            this.translateXform( center.opposite() );
            this.updateScaling( scale );
            this.translateXform( center );
        }
    
    private Vector3f object_to_world_coordinate(
            final Vector3f p_obj,
            final Vector3f global_trans,
            final Vector3f global_scale )
    {
        Vector3f p_external = p_obj.sub( m_object_center );

        float x =p_external.getX() * m_normalize.getX();
        float y =p_external.getY() * m_normalize.getY();
        float z =p_external.getZ() * m_normalize.getZ();
        
        p_external = new Vector3f( x, y, z );

        p_external = p_external.add( m_external_position );

        Vector3f p_world = p_external.sub( global_trans );

        x = p_world.getX() * global_scale.getX();
        y = p_world.getY() * global_scale.getY();
        z = p_world.getZ() * global_scale.getZ();
        
        p_world = new Vector3f( x, y, z );

        p_world = p_world.mul( this.scaledRotation() );

        p_world = p_world.add( this.getTranslation() );

        return( p_world );
    }
}
