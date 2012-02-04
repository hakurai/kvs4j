package kvs.core.visualization.viewer;

import java.util.ArrayList;
import java.util.HashMap;

import kvs.core.visualization.renderer.RendererBase;

public class RendererManager {

    protected ArrayList<RendererBase> m_renderer_manager_base = new ArrayList<RendererBase>();

    protected HashMap<Integer, RendererBase> m_renderer_ptr_map = new HashMap<Integer, RendererBase>();

    static int ren_id = 0;

    public RendererManager() {
        m_renderer_manager_base.clear();
        m_renderer_ptr_map.clear();
    }

    public int insert( RendererBase renderer ) {
        ren_id++;

        m_renderer_ptr_map.put( ren_id, renderer );
        m_renderer_manager_base.add( renderer );

        return (ren_id);
    }

    public void erase( boolean delete_flg ) {
        if (delete_flg) {
            for (RendererBase p : m_renderer_manager_base) {
                if (p != null) {
                    p = null;
                }
            }
        }

        m_renderer_manager_base.clear();
        m_renderer_ptr_map.clear();
    }

    public void erase( int renderer_id, boolean delete_flg ) {
        RendererBase ren = m_renderer_ptr_map.get( renderer_id );
        if (ren == null) {
            return;
        }

        if (delete_flg) {
            if (ren != null) {
                ren = null;
            }
        }

        // Erase the renderer in the renderer master.
        m_renderer_manager_base.remove( ren );

        // Erase the map component, which is specified by map_id,
        // in m_renderer_ptr_map.
        m_renderer_ptr_map.remove( renderer_id );
    }

    public int nrenderers() {
        return (m_renderer_manager_base.size());
    }

    public RendererBase renderer() {
        return m_renderer_manager_base.get(0);
    }

    public RendererBase renderer( int renderer_id ) {
        RendererBase ren = m_renderer_ptr_map.get( renderer_id );

        return (ren);
    }

    public final boolean hasRenderer() {
        return (m_renderer_manager_base.size() != 0);
    }
}
