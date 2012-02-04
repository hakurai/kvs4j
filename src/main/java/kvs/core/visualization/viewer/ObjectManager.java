package kvs.core.visualization.viewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import kvs.core.matrix.Matrix33f;
import kvs.core.matrix.Vector2f;
import kvs.core.matrix.Vector3f;
import kvs.core.util.Tree.Iterator;
import kvs.core.util.Tree;
import kvs.core.util.Math;
import kvs.core.visualization.object.ObjectBase;

public class ObjectManager extends ObjectBase {

    // private static final Vector3f CENTER_OF_OBJECT_MASTER = Vector3f.ZERO;

    // object_manager_base => m_object_manager_base
    // object_ptr => Iterator
    // std::map => HashTable

    private static final long serialVersionUID = -2909200045971635377L;
    protected final Tree<ObjectBase> m_object_manager_base = new Tree<ObjectBase>(); // /<object_manager_base
    protected boolean m_has_active_object; // /< If active object exists true.
    protected boolean m_enable_all_move; // /< If All object move together true.
    protected Iterator<ObjectBase> m_root_ptr; // /< pointer to root of tree
    protected Iterator<ObjectBase> m_active_object_ptr; // /< pointer to active
    // object
    protected HashMap<Integer, Iterator<ObjectBase>> m_object_ptr_map; // /<
    // object
    // pointer
    // map
    protected int m_current_object_id; // /< current object ID

    public ObjectManager() {
        super( true );
        this.m_object_manager_base.clear();
        this.initialize();
        this.m_has_active_object = false;
        this.m_enable_all_move = false;
        this.m_object_ptr_map = new HashMap<Integer, Iterator<ObjectBase>>( 20 );
        this.m_object_ptr_map.clear();

        this.m_current_object_id = 0;
        this.insert_root();

        this.m_min_object_coord = new Vector3f( 1000000, 1000000, 1000000 );
        this.m_max_object_coord = new Vector3f( -1000000, -1000000, -1000000 );
        this.m_min_external_coord = new Vector3f( -3.0f, -3.0f, -3.0f );
        this.m_max_external_coord = new Vector3f( 3.0f, 3.0f, 3.0f );
    }

    private void insert_root() {
        this.m_root_ptr = this.m_object_manager_base.insert( this.m_object_manager_base.begin(), this );
    }

    @Override
    public ObjectBase.ObjectType objectType() {
        return (ObjectType.ObjectManager);
    }

    /**
     * このオブジェクトツリーのルート直下にオブジェクトを追加します。
     * @param obj　追加するオブジェクト
     * @return 追加したオブジェクトのID
     */
    public int insert( ObjectBase obj ) {
        obj.updateNormalizeParameters();
        this.update_normalize_parameters( obj.minExternalCoord(), obj.maxExternalCoord() );

        /*
         * Calculate a object ID by counting the number of this method called.
         * Therefore, we define the object ID as static parameter in this
         * method, and count it.
         */
        this.m_current_object_id++;

        //Iterator<ObjectBase> obj_ptr = (PreOrderIterator<ObjectBase>) m_object_manager_base.append_child( m_root_ptr, obj );
        Iterator<ObjectBase> obj_ptr = this.m_object_manager_base.append_child( this.m_root_ptr, obj );

        /*
         * A pair of the object ID and a pointer to the object is inserted to
         * the object map. The pointer to the object is got by inserting the
         * object to the object master base.
         */
        this.m_object_ptr_map.put( this.m_current_object_id, obj_ptr );

        return (this.m_current_object_id);
    }

    /**
     * このオブジェクトツリーの指定されたオブジェクトの下にオブジェクトを追加します。
     * 親として指定したオブジェクトが存在しない場合は-1を返します
     * @param parent_id 親となるオブジェクトのID
     * @param obj 追加するオブジェクト
     * @return 追加したオブジェクトのID
     */
    public int insert( int parent_id, ObjectBase obj ) {
        obj.updateNormalizeParameters();
        this.update_normalize_parameters( obj.minExternalCoord(), obj.maxExternalCoord() );

        Iterator<ObjectBase> parent_ptr = this.m_object_ptr_map.get( parent_id );
        if (parent_ptr == null) {
            return (-1);
        }

        this.m_current_object_id++;

        Iterator<ObjectBase> child_ptr = this.m_object_manager_base.append_child( parent_ptr, obj );

        this.m_object_ptr_map.put( this.m_current_object_id, child_ptr );

        return (this.m_current_object_id);
    }

    /**
     * すべてのオブジェクトを削除します。
     * @param delete_flg trueの場合オブジェクトのデータ自体をnullにします
     */
    public void erase( boolean delete_flg ) {
        Iterator<ObjectBase> first = this.m_object_manager_base.begin();
        Iterator<ObjectBase> last = this.m_object_manager_base.end();

        // Skip the root.
        first.next();

        if (delete_flg) {
            for (; !first.equals( last ); first.next()) {
                if (first.getData() != null) {
                    first.setData( null );
                }
            }
        }

        this.m_object_manager_base.clear();
        this.m_object_ptr_map.clear();

        this.insert_root();

        this.update_normalize_parameters();
    }

    /**
     * 指定したIDのオブジェクトを削除します。
     * @param obj_id 削除するオブジェクトのID
     * @param delete_flg trueの場合オブジェクトのデータ自体をnullにします
     */
    public void erase( int obj_id, boolean delete_flg ) {
        /*
         * Search the object which is specified by given object ID in the object
         * pointer map. If it isn't found, this method executes nothing.
         */
        Iterator<ObjectBase> ptr = this.m_object_ptr_map.get( obj_id );
        if (ptr == null) {
            return;
        }

        // Delete the object.
        ObjectBase obj = ptr.getData(); // object

        if (delete_flg) {
            if (obj != null) {
                ptr.setData( null );
            }
        }

        // Erase the object in the object master base.
        this.m_object_manager_base.erase( ptr );

        // Erase the map component, which is specified by map_id,
        // in m_obj_ptr_map.
        this.m_object_ptr_map.remove( obj_id );

        this.update_normalize_parameters();
    }

    /**
     * 指定したIDのオブジェクトを新しいオブジェクトに置き換えます。
     * @param obj_id 置き換えるオブジェクトのID
     * @param obj 新しく置き換えるオブジェクト
     * @param delete_flg trueの場合古いオブジェクトのデータをnullにします
     */
    public void change( int obj_id, ObjectBase obj, boolean delete_flg ) {
        /*
         * Search the object which is specified by given object ID in the object
         * pointer map. If it isn't found, this method executes nothing.
         */
        Iterator<ObjectBase> ptr = this.m_object_ptr_map.get( obj_id );
        if (ptr == null)
            return;

        // Change the object.
        ObjectBase old_obj = ptr.getData(); // object

        // Save the Xform.
        Xform xform = old_obj.getXform();

        // Erase the old object
        if (delete_flg) {
            if (old_obj != null) {
                old_obj = null;
            }
        }

        // Insert the new object
        obj.updateNormalizeParameters();
        obj.setXform( xform );

        ptr.setData( obj );

        this.update_normalize_parameters();
    }

    
    /**
     * 
     * @return　オブジェクトの個数
     */
    public final int nobjects() {
        return (this.m_object_manager_base.size());
    }

    /**
     * このオブジェクトツリーの一番最初のオブジェクトを返します。
     * @return オブジェクト
     */
    public ObjectBase object() {
        // pointer to the object
        Iterator<ObjectBase> obj_ptr = this.m_object_manager_base.begin();

        // skip the root
        obj_ptr.next();

        return (obj_ptr.getData());
    }

    /**
     *  指定されたIDのオブジェクトを返します
     * @param obj_id ID
     * @return オブジェクト
     */
    public ObjectBase object( int obj_id ) {
        /*
         * Search the object which is specified by given object ID in the object
         * pointer map. If it isn't found, this method executes nothing.
         */

        return this.m_object_ptr_map.get( obj_id ).getData();
    }

    /**
     * このオブジェクトツリーがオブジェクトを所持しているかを返します。
     * @return　オブジェクトを1つでも所持している場合はtrue
     */
    public boolean hasObject() {
        return (this.m_object_manager_base.size() > 1);
    }

    @Override
    public void resetXform() {
        Iterator<ObjectBase> first = this.m_object_manager_base.begin();
        Iterator<ObjectBase> last = this.m_object_manager_base.end();

        first.next();
        for (; !first.equals( last ); first.next()) {
            first.getData().resetXform();
        }

        super.resetXform();
    }

    public void resetXform( int obj_id ) {
        Iterator<ObjectBase> obj_ptr = this.m_object_ptr_map.get( obj_id );
        if (obj_ptr == null) {
            return;
        }

        Iterator<ObjectBase> first = this.m_object_manager_base.begin( obj_ptr );
        Iterator<ObjectBase> last = this.m_object_manager_base.end( obj_ptr );

        final Xform obj_form = obj_ptr.getData().getXform();
        final Xform trans = this.mul( obj_form.inverse() );

        obj_ptr.getData().setXform( this );

        for (; !first.equals( last ); first.next()) {
            first.getData().setXform( trans.mul( first.getData().getXform() ) );
        }
    }

    public Xform xform( int obj_id ) {
        /*
         * Search the object which is specified by given object ID in the object
         * pointer map. If it isn't found, this method retrun initial Xform.
         */
        ObjectBase obj_ptr = this.m_object_ptr_map.get( obj_id ).getData();
        if (obj_ptr == null) {
            return (new Xform());
        }

        // Delete the object.
        ObjectBase obj = obj_ptr; // object

        return (obj.getXform());
    }

    @SuppressWarnings("unchecked")
    public int activeObjectID() {
        if (this.m_has_active_object) {
            Set<Map.Entry<Integer, Iterator<ObjectBase>>> set = this.m_object_ptr_map.entrySet();
            if(!set.contains( this.m_active_object_ptr )){
                return (-1);
            }
            Map.Entry<Integer, Iterator<ObjectBase>>[] map = (Entry<Integer, Iterator<ObjectBase>>[]) set.toArray();
            for (Map.Entry<Integer, Iterator<ObjectBase>> entry : map) {
                if(entry.getValue().equals( this.m_active_object_ptr )){
                    return entry.getKey();
                }
            }
        }
        return (-1);
    }

    public boolean setActiveObjectID( int obj_id ) {
        Iterator<ObjectBase> map_id = this.m_object_ptr_map.get( obj_id );
        if (map_id == null) {
            return (false);
        }

        this.m_active_object_ptr = map_id;
        this.m_has_active_object = true;

        return (true);
    }
    
    /**
     * 現在アクティブなオブジェクトを返します。
     * 存在しない場合はnullを返します。
     * @return アクティブなオブジェクト
     */
    public ObjectBase activeObject() {
        return (this.m_has_active_object ? this.m_active_object_ptr.getData() : null);
    }

    public void resetActiveObjectXform() {
        if (this.m_has_active_object) {
            this.m_active_object_ptr.getData().resetXform();
            this.m_active_object_ptr.getData().multiplyXform( this );
        }
    }

    public void eraseActiveObject() {
        if (this.m_has_active_object) {
            if (this.m_active_object_ptr.getData() != null) {
                this.m_active_object_ptr = null;
            }
            this.m_object_manager_base.erase( this.m_active_object_ptr );
        }

        this.update_normalize_parameters();
    }

    public void enableAllMove() {
        this.m_enable_all_move = true;
    }

    public void disableAllMove() {
        this.m_enable_all_move = false;
    }

    public boolean isEnableAllMove() {
        return (this.m_enable_all_move);
    }

    public boolean hasActiveObject() {
        return (this.m_has_active_object);
    }

    public void releaseActiveObject() {
        this.m_has_active_object = false;
    }

    public boolean detectCollision( final Vector2f p_win, Camera camera ) {
        double min_distance = 100000;

        Iterator<ObjectBase> first = this.m_object_manager_base.begin();
        Iterator<ObjectBase> last = this.m_object_manager_base.end();

        // skip the root
        first.next();

        for (; !first.equals( last ); first.next()) {
            if (!first.getData().canCollision()) {
                continue;
            }

            final Vector2f diff = first.getData().positionInDevice( camera, this.m_object_center, this.m_normalize ).sub( p_win );

            final double distance = diff.length();

            if (distance < min_distance) {
                min_distance = distance;
                this.m_active_object_ptr = first.copy(); //FIXME バグ
            }
        }

        return (this.m_has_active_object = m_active_object_ptr.getData().collision( p_win, camera, this.m_object_center, this.m_normalize ));
    }

    public boolean detectCollision( final Vector3f p_world ) {
        double min_distance = 100000;

        Iterator<ObjectBase> first = this.m_object_manager_base.begin();
        Iterator<ObjectBase> last = this.m_object_manager_base.end();

        // skip the root
        first.next();

        for (; !first.equals( last ); first.next()) {
            if (!first.getData().canCollision()) {
                continue;
            }

            final Vector3f diff = first.getData().positionInWorld( this.m_object_center, this.m_normalize ).sub( p_world );

            final double distance = diff.length();

            if (distance < min_distance) {
                min_distance = distance;
                this.m_active_object_ptr = first.copy();
            }
        }

        return (this.m_has_active_object = this.m_active_object_ptr.getData().collision( p_world, this.m_object_center, this.m_normalize ));
    }

    public Vector2f positionInDevice( Camera camera ) {
        GL gl = GLU.getCurrentGL();
        Vector2f ret;
        gl.glPushMatrix();

        camera.update();

        ret = camera.projectObjectToWindow( this.getTranslation() );
        ret = new Vector2f( ret.getX(), camera.getWindowHeight() - ret.getY() );

        gl.glPopMatrix();

        return (ret);
    }

    public void rotate( final Matrix33f rotation ) {
        ObjectBase object = this.get_control_target();
        Vector3f center = this.get_rotation_center( object );

        object.rotate( rotation, center );

        Iterator<ObjectBase> first = this.get_control_first_pointer();
        Iterator<ObjectBase> last = this.get_control_last_pointer();

        for (; !first.equals( last ); first.next()) {
            first.getData().rotate( rotation, center );
        }
    }

    public void translate( final Vector3f translation ) {
        ObjectBase object = this.get_control_target();

        object.translateXform( translation );

        Iterator<ObjectBase> first = this.get_control_first_pointer();
        Iterator<ObjectBase> last = this.get_control_last_pointer();

        for (; !first.equals( last ); first.next()) {
            first.getData().translateXform( translation );
        }
    }

    public void scale( final Vector3f scaling )
    {
        ObjectBase object = this.get_control_target();

        Vector3f center = this.get_rotation_center( object );

        object.scale( scaling, center );

        Iterator<ObjectBase> first = this.get_control_first_pointer();
        Iterator<ObjectBase> last  = this.get_control_last_pointer();

        for( ; !first.equals( last ); first.next() )
        {
            first.getData().scale( scaling, center );
        }
    }

    public void updateExternalCoords()
    {
        this.update_normalize_parameters();
    }

    private void update_normalize_parameters( final Vector3f min_ext, final Vector3f max_ext ) {
        if (Math.equal( 0.0f, min_ext.getX() ) && Math.equal( 0.0f, min_ext.getY() ) && Math.equal( 0.0f, min_ext.getZ() ) && Math.equal( 0.0f, max_ext.getX() ) && Math.equal( 0.0f, max_ext.getY() ) && Math.equal( 0.0f, max_ext.getZ() )) {
            return;
        }

        float x = this.m_min_object_coord.getX() < min_ext.getX() ? this.m_min_object_coord.getX() : min_ext.getX();
        float y = this.m_min_object_coord.getY() < min_ext.getY() ? this.m_min_object_coord.getY() : min_ext.getY();
        float z = this.m_min_object_coord.getZ() < min_ext.getZ() ? this.m_min_object_coord.getZ() : min_ext.getZ();

        this.m_min_object_coord = new Vector3f( x, y, z );

        x = this.m_max_object_coord.getX() > max_ext.getX() ? this.m_max_object_coord.getX() : max_ext.getX();
        y = this.m_max_object_coord.getY() > max_ext.getY() ? this.m_max_object_coord.getY() : max_ext.getY();
        z = this.m_max_object_coord.getZ() > max_ext.getZ() ? this.m_max_object_coord.getZ() : max_ext.getZ();

        this.m_max_object_coord = new Vector3f( x, y, z );

        final Vector3f diff_obj = this.m_max_object_coord.sub( this.m_min_object_coord );
        final float max_diff = Math.max( diff_obj.getX(), diff_obj.getY(), diff_obj.getZ() );
        final float normalize = 6.0f / max_diff;

        this.m_normalize = new Vector3f( normalize );

        this.m_object_center = (this.m_max_object_coord.add( this.m_min_object_coord )).mul( 0.5f );
    }

    private void update_normalize_parameters() {
        this.m_min_object_coord = new Vector3f( 1000000, 1000000, 1000000 );
        this.m_max_object_coord = new Vector3f( -1000000, -1000000, -1000000 );
        this.m_min_external_coord = new Vector3f( -3.0f, -3.0f, -3.0f );
        this.m_max_external_coord = new Vector3f( 3.0f, 3.0f, 3.0f );

        int ctr = 0;
        if (this.m_object_manager_base.size() > 1) {
            Iterator<ObjectBase> first = this.m_object_manager_base.begin();
            Iterator<ObjectBase> last = this.m_object_manager_base.end();

            // skip the root
            first.next();

            for (; !first.equals( last ); first.next()) {
                if (Math.equal( 0.0f, first.getData().minExternalCoord().getX() ) && Math.equal( 0.0f, first.getData().minExternalCoord().getY() ) && Math.equal( 0.0f, first.getData().minExternalCoord().getZ() ) && Math.equal( 0.0f, first.getData().maxExternalCoord().getX() ) && Math.equal( 0.0f, first.getData().maxExternalCoord().getY() ) && Math.equal( 0.0f, first.getData().maxExternalCoord().getZ() )) {
                    continue;
                }

                float x = this.m_min_object_coord.getX() < first.getData().minExternalCoord().getX() ? this.m_min_object_coord.getX()
                        : first.getData().minExternalCoord().getX();
                float y = this.m_min_object_coord.getY() < first.getData().minExternalCoord().getY() ? this.m_min_object_coord.getY()
                        : first.getData().minExternalCoord().getY();
                float z = this.m_min_object_coord.getZ() < first.getData().minExternalCoord().getZ() ? this.m_min_object_coord.getZ()
                        : first.getData().minExternalCoord().getZ();

                this.m_min_object_coord = new Vector3f( x, y, z );

                x = this.m_max_object_coord.getX() > first.getData().maxExternalCoord().getX() ? this.m_max_object_coord.getX()
                        : first.getData().maxExternalCoord().getX();
                y = this.m_max_object_coord.getY() > first.getData().maxExternalCoord().getY() ? this.m_max_object_coord.getY()
                        : first.getData().maxExternalCoord().getY();
                z = this.m_max_object_coord.getZ() > first.getData().maxExternalCoord().getZ() ? this.m_max_object_coord.getZ()
                        : first.getData().maxExternalCoord().getZ();

                this.m_max_object_coord = new Vector3f( x, y, z );

                ctr++;
            }
        }

        if (ctr == 0) {
            this.m_normalize = new Vector3f( 1.0f );
            this.m_object_center = new Vector3f( 0.0f );
        } else {
            final Vector3f diff_obj = this.m_max_object_coord.sub( this.m_min_object_coord );

            final float max_diff = Math.max( diff_obj.getX(), diff_obj.getY(), diff_obj.getZ() );

            final float normalize = 6.0f / max_diff;

            this.m_normalize = new Vector3f( normalize );

            this.m_object_center = (this.m_max_object_coord.add( this.m_min_object_coord )).mul( 0.5f );
        }
    }

    private ObjectBase get_control_target() {
        if (this.isEnableAllMove()) {
            return (this);
        } else {
            return (this.m_active_object_ptr.getData());
        }
    }

    private Vector3f get_rotation_center( ObjectBase obj ) {
        if (this.isEnableAllMove()) {
            return (new Vector3f( this.get( 0, 3 ), this.get( 1, 3 ), this.get( 2, 3 ) ));
        } else {
            return (obj.positionInWorld( this.m_object_center, this.m_normalize ));
        }
    }

    private Iterator<ObjectBase> get_control_first_pointer() {
        Iterator<ObjectBase> first;

        if (this.isEnableAllMove()) {
            first = this.m_object_manager_base.begin();
            first.next();
        } else {
            first = this.m_object_manager_base.begin( this.m_active_object_ptr );
        }

        return (first);
    }

    private Iterator<ObjectBase> get_control_last_pointer() {
        Iterator<ObjectBase> last;

        if (this.isEnableAllMove()) {
            last = this.m_object_manager_base.end();
        } else {
            last = this.m_object_manager_base.end( this.m_active_object_ptr );
        }

        return (last);
    }

}
