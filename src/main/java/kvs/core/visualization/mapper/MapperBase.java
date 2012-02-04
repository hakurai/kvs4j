package kvs.core.visualization.mapper;

import java.io.Serializable;

import kvs.core.matrix.Vector3f;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.StructuredVolumeObject;
import kvs.core.visualization.object.UnstructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase;
import kvs.core.visualization.pipeline.PipelineModule;

public abstract class MapperBase implements PipelineModule, Serializable{
    
    private static final long serialVersionUID = -58967015773042007L;

    private TransferFunction    m_transfer_function = new TransferFunction();   ///< Transfer function.

    protected VolumeObjectBase  m_volume = null;      ///< Volume object.
    protected boolean           m_is_success = false;  ///< Check flag for mapping.
    
    public MapperBase(){}
    
    public MapperBase( TransferFunction transfer_function ){
        m_transfer_function = transfer_function;
    }
    
    //public abstract ObjectBase exec( ObjectBase object );
    
    public void setTransferFunction( final TransferFunction transfer_function )
    {
        m_transfer_function = transfer_function;
    }
    
    public void setColorMap( final ColorMap color_map )
    {
        m_transfer_function.setColorMap( color_map );
    }
    
    public void setOpacityMap( final OpacityMap opacity_map )
    {
        m_transfer_function.setOpacityMap( opacity_map );
    }
    
    public VolumeObjectBase volume()
    {
        return( m_volume );
    }
    
    public TransferFunction transferFunction()
    {
        return( m_transfer_function );
    }
    
    public ColorMap colorMap()
    {
        return( m_transfer_function.colorMap() );
    }
    
    public OpacityMap opacityMap()
    {
        return( m_transfer_function.opacityMap() );
    }
    
    public boolean isSuccess()
    {
        return( m_is_success );
    }
    
    public boolean isFailure()
    {
        return( !m_is_success );
    }
    
    public void attach_volume( final VolumeObjectBase volume )
    {
        m_volume = volume;
    }
    
    public void set_min_max_coords( final VolumeObjectBase volume, ObjectBase object )
    {
        if ( !volume.hasMinMaxObjectCoords() )
        {
            switch ( volume.volumeType() )
            {
            case Structured:
            {
                // WARNING: remove constness, but safe in this case.
                VolumeObjectBase b = volume;
                ((StructuredVolumeObject)b).updateMinMaxCoords();
                break;
            }
            case Unstructured:
            {
                // WARNING: remove constness, but safe in this case.
                VolumeObjectBase b =  volume;
                ((UnstructuredVolumeObject)b).updateMinMaxCoords();
                break;
            }
            default: break;
            }
        }

        final Vector3f min_obj_coord = m_volume.minObjectCoord();
        final Vector3f max_obj_coord = m_volume.maxObjectCoord();
        final Vector3f min_ext_coord = m_volume.minExternalCoord();
        final Vector3f max_ext_coord = m_volume.maxExternalCoord();
        object.setMinMaxObjectCoords( min_obj_coord, max_obj_coord );
        object.setMinMaxExternalCoords( min_ext_coord, max_ext_coord );
    }
}
