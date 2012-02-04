package kvs.core.visualization.pipeline;

import java.io.File;
import java.util.ArrayList;

import kvs.core.KVSException;
import kvs.core.visualization.filter.FilterBase;
import kvs.core.visualization.mapper.MapperBase;
import kvs.core.visualization.object.GeometryObjectBase;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.VolumeObjectBase;
import kvs.core.visualization.renderer.ImageRenderer;
import kvs.core.visualization.renderer.LineRenderer;
import kvs.core.visualization.renderer.PointRenderer;
import kvs.core.visualization.renderer.PolygonRenderer;
import kvs.core.visualization.renderer.RayCastingRenderer;
import kvs.core.visualization.renderer.RendererBase;

public class VisualizationPipeline {

    public enum Category
    {
        Empty,     ///< empty module
        Filter,    ///< filter module
        Mapper,    ///< mapper module
        Object,    ///< object module
        Renderer;  ///< renderer module
    };

    private File                        m_filename;    ///< filename
    private boolean                     m_cache;       ///< cache mode (DISABLE NOW)
    private ArrayList<PipelineModule>   m_module_list = new ArrayList<PipelineModule>(); ///< pipeline module list
    private ObjectBase                  m_object;   ///< pointer to the object inserted to the manager
    private RendererBase                m_renderer; ///< pointer to the renderer inserted to the manager

    public VisualizationPipeline(){
        m_cache = true;
    }

    public VisualizationPipeline( final File filename ){
        m_filename = filename;
        m_cache = true;
    }

    public VisualizationPipeline connect( PipelineModule module )
    {
        m_module_list.add( module );

        return this;
    }

    public boolean exec() throws KVSException
    {
        // Setup object.
        if ( !this.hasObject() )
        {
            // Check filename.
            if ( m_filename == null )
            {
                System.err.println( "Input data is not specified." );
                return( false );
            }

            // Create object module.
            if ( !this.create_object_module( m_filename ) )
            {
                System.err.println( "Cannot create a object from '" +m_filename.getName()+ "'." );
                return( false );
            }
        }

        int last = m_module_list.size();

        if ( this.hasRenderer() ){
            --last;
        }
        
        ObjectBase object = null;
        
        for( int i = 0; i < last; i++ ){
            PipelineModule module = m_module_list.get( i );
            object = module.exec( object );
            if ( object == null )
            {
                System.err.println("Cannot execute '" + module.getClass().getName() + "'." );
                return( false );
            }
        }

        m_object = object;

        // Setup renderer.
        if ( !this.hasRenderer() )
        {
            // create renderer module.
            if ( !this.create_renderer_module( object ) )
            {
                System.err.println( "Cannot create a renderer for '" +m_filename.getName()+ "'." );
                return( false );
            }
        }

        PipelineModule renderer_module = this.find_module( Category.Renderer );
        if ( renderer_module != null )
        {
            m_renderer = (RendererBase)renderer_module;
        }

        return( true );
    }

    public boolean cache()
    {
        return m_cache;
    }

    public void enableCache()
    {
        m_cache = true;
    }

    public void disableCache()
    {
        m_cache = false;
    }

    public boolean hasObject()
    {
        return( this.count_module( Category.Object ) > 0 );
    }
    
    public boolean hasRenderer()
    {
        return( this.count_module( Category.Renderer ) > 0 );
    }

    public ObjectBase object()
    {
        return m_object;
    }

    public RendererBase renderer(){
        return m_renderer;
    }
    
    public ArrayList<PipelineModule> moduleList(){
        return m_module_list;
    }

    public void print()
    {
        System.out.println( m_filename.getPath() );

        for( PipelineModule module : m_module_list ){
            System.out.print( " >> " );
            System.out.print( module.getClass().getName() );            
        }

    }

    private boolean create_object_module( final File filename )
    {
        // Read a file and import a object.
        ObjectImporter importer = new ObjectImporter( filename );
        ObjectBase object;
        try {
            object = importer.importFile();
        } catch (KVSException e) {
            System.err.println( "Cannot import a object." );

            e.printStackTrace();
            return false;
        }   
        m_module_list.add( object );

        return true;
    }

    private boolean create_renderer_module( final ObjectBase object )
    {
        switch ( object.objectType() )
        {
        case Geometry:
        {
            final GeometryObjectBase geometry = (GeometryObjectBase)object;

            return this.create_renderer_module( geometry );
        }
        case Volume:
        {
            final VolumeObjectBase volume = (VolumeObjectBase)object;

            return this.create_renderer_module( volume );
        }
        case Image:
        {
            PipelineModule module = new ImageRenderer();
            m_module_list.add( module );
            return true;
        }
        default: break;
        }

        return false;
    }

    private boolean create_renderer_module( final GeometryObjectBase geometry )
    {
        boolean ret = true;

        switch ( geometry.geometryType() )
        {
        case Point:
        {
            PipelineModule module = new PointRenderer();
            m_module_list.add( module );
            break;
        }
        case Line:
        {
            PipelineModule module = new LineRenderer();
            m_module_list.add( module );
            break;
        }
        case Polygon:
        {
            PipelineModule module = new PolygonRenderer();
            m_module_list.add( module );
            break;
        }
        default: ret = false; break;
        }

        return ret;
    }

    private boolean create_renderer_module( final VolumeObjectBase volume )
    {
        boolean ret = true;

        switch ( volume.volumeType() )
        {
        case Structured:
        {
            PipelineModule module = new RayCastingRenderer();
            m_module_list.add( module );
            break;
        }
        case Unstructured:
        {
            /*
            kvs::PipelineModule module( new kvs::RayCastingRenderer );
            m_module_list.push_back( module );
            */
            ret = false;
            break;
        }
        default: ret = false; break;
        }

        return ret;
    }

    private PipelineModule find_module( Category category )
    {
        for( PipelineModule module : m_module_list ){
            if( module_type( module ) == category ){
                return module;
            }
        }

        return null;
    }

    private Category module_type( PipelineModule module ){
        if( module instanceof FilterBase ){
            return Category.Filter;
        } else if( module instanceof MapperBase ){
            return Category.Mapper;
        } else if( module instanceof ObjectBase ){
            return Category.Object;
        } else if( module instanceof RendererBase ){
            return Category.Renderer;
        } else {
            return Category.Empty;
        }
    }

    private int count_module( Category category ){
        int counter = 0;

        for( PipelineModule module : m_module_list ){
            if( module_type( module ) == category ){
                ++counter;
            }
        }

        return counter;
    }

}
