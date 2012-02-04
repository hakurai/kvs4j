package kvs.core.visualization.viewer;

import java.util.ArrayList;
import java.util.LinkedList;

import kvs.core.util.IntPair;

public class IDManager {

    protected ArrayList<Integer> m_flip_table = new ArrayList<Integer>();
    protected LinkedList<IntPair> m_manager_base = new LinkedList<IntPair>();

    public void insert( int object_id, int renderer_id ) {
        m_manager_base.add( new IntPair( object_id, renderer_id ) );
        update_flip_table();
    }

    public void insertObjectID( int object_id ) {
        insert( object_id, m_manager_base.getLast().getSecond() );
        update_flip_table();
    }

    public void insertRendererID( int renderer_id ) {
        insert( m_manager_base.getLast().getFirst(), renderer_id );
        update_flip_table();
    }

    public final LinkedList<Integer> objectID( int renderer_id ) {
        LinkedList<Integer> object_ids = new LinkedList<Integer>();

        for (IntPair p : m_manager_base) {
            if (renderer_id == p.getSecond()) {
                object_ids.add( p.getFirst() );
            }
        }

        return (object_ids);
    }

    public int objectID() {
        return (m_manager_base.getLast().getFirst());
    }

    public final LinkedList<Integer> rendererID( int object_id ) {
        LinkedList<Integer> renderer_ids = new LinkedList<Integer>();

        for (IntPair p : m_manager_base) {
            if (object_id == p.getFirst()) {
                renderer_ids.add( p.getSecond() );
            }
        }

        return (renderer_ids);
    }

    public final int rendererID() {
        return (m_manager_base.getLast().getSecond());
    }

    public void erase() {
        m_manager_base.clear();
        m_flip_table.clear();
    }

    public void erase( int object_id, int renderer_id ) {
        LinkedList<IntPair> erase_ptr = new LinkedList<IntPair>();

        for (IntPair p : m_manager_base) {
            if (object_id == p.getFirst() && renderer_id == p.getSecond()) {
                erase_ptr.add( p );
            }
        }

        for (IntPair p : erase_ptr) {
            m_manager_base.remove( p );
        }

        update_flip_table();
    }

    public void eraseByObjectID( int object_id ) {
        LinkedList<IntPair> erase_ptr = new LinkedList<IntPair>();

        for (IntPair p : m_manager_base) {
            if (object_id == p.getFirst()) {
                erase_ptr.add( p );
            }
        }

        for (IntPair p : erase_ptr) {
            m_manager_base.remove( p );
        }

        update_flip_table();
    }

    public void eraseByRendererID( int renderer_id ) {
        LinkedList<IntPair> erase_ptr = new LinkedList<IntPair>();

        for (IntPair p : m_manager_base) {
            if (renderer_id == p.getSecond()) {
                erase_ptr.add( p );
            }
        }

        for (IntPair p : erase_ptr) {
            m_manager_base.remove( p );
        }

        update_flip_table();
    }

    public void changeObject( int object_id ) {
        m_manager_base.getLast().setFirst( object_id );
    }

    public void changeObject( int renderer_id, int object_id ) {
        for (IntPair p : m_manager_base) {
            if (renderer_id == p.getSecond()) {
                p.setFirst( object_id );
                break;
            }
        }
    }

    public void changeObject( final IntPair id_pair, int object_id ) {
        for (IntPair p : m_manager_base) {
            if (p.equals( id_pair )) {
                p.setFirst( object_id );
                break;
            }
        }
    }

    public void changeRenderer( int renderer_id ) {
        m_manager_base.getLast().setSecond( renderer_id );
    }

    public void changeRenderer( int object_id, int renderer_id ) {
        for (IntPair p : m_manager_base) {
            if (object_id == p.getFirst()) {
                p.setSecond( renderer_id );
                break;
            }
        }
    }

    public void changeRenderer( final IntPair id_pair, int renderer_id ) {
        for (IntPair p : m_manager_base) {
            if (p.equals( id_pair )) {
                p.setSecond( renderer_id );
                break;
            }
        }
    }
    
    public int size() {
        return m_manager_base.size();
    }

    public IntPair get( int index ) {
        return m_manager_base.get( index );
    }

    private void update_flip_table() {
        m_flip_table.clear();

        final int n = m_manager_base.size();

        for (int i = 0; i < n; i++) {
            m_flip_table.add( i );
        }
    }

}
