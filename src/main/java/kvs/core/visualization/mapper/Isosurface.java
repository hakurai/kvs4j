package kvs.core.visualization.mapper;

import kvs.core.KVSException;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PolygonObject;
import kvs.core.visualization.object.StructuredVolumeObject;
import kvs.core.visualization.object.UnstructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase;
import kvs.core.visualization.object.ObjectBase.ObjectType;
import kvs.core.visualization.object.PolygonObject.NormalType;
import kvs.core.visualization.object.VolumeObjectBase.VolumeType;

public class Isosurface extends MapperBase {

    private static final long serialVersionUID = -5762794166363059513L;
    private double  m_isolevel      = 0;    ///< isosurface level
    private boolean m_duplication   = true; ///< duplication flag

    PolygonObject m_object = new PolygonObject();

    public Isosurface(){
        super();
    }

    public Isosurface( final double                 isolevel,
                       final NormalType             normal_type )
    {
        super();
        m_isolevel = isolevel;
        m_object.setNormalType( normal_type );

        // In the case of VertexNormal-type, the duplicated vertices are forcibly deleted.
        if ( normal_type == NormalType.VertexNormal )
        {
            m_duplication = false;
        }

        // Extract the surfaces.
        //this.exec( volume );
    }

    public Isosurface( final double                 isolevel,
                       final NormalType             normal_type,
                       final boolean                duplication,
                       final TransferFunction transfer_function )
    {
        super( transfer_function );
        m_isolevel = isolevel;

        m_duplication = duplication;
        m_object.setNormalType( normal_type );

        // In the case of VertexNormal-type, the duplicated vertices are forcibly deleted.
        if ( normal_type == NormalType.VertexNormal )
        {
            m_duplication = false;
        }

        // Extract the surfaces.
        //this.exec( volume );
    }

    @Override
    public ObjectBase exec( ObjectBase object ) throws KVSException {
                    System.out.println( "isosurface exec..." );
        long s = System.currentTimeMillis();
        if ( object.objectType() == ObjectType.Volume )
        {
            this.mapping( (VolumeObjectBase)( object ) );
        }
        else
        {
            throw new KVSException("Geometry object is inputed.");
        }

        long e = System.currentTimeMillis();
            System.out.println( (e-s) +"ms" );

        return( m_object );
    }
    
    private void mapping( final VolumeObjectBase volume ) throws KVSException
    {
        // Check whether the volume can be processed or not.
        if ( volume.veclen() != 1 )
        {
            throw new KVSException("The input volume is not a sclar field data.");
        }

        if ( volume.volumeType() == VolumeType.Structured )
        {
            final StructuredVolumeObject structured_volume =
                (StructuredVolumeObject)volume;
            
            MarchingCubes factory = new MarchingCubes(
                    m_isolevel,
                    m_object.normalType(),
                    m_duplication,
                    this.transferFunction() );

            PolygonObject polygon = (PolygonObject)factory.exec(structured_volume);
            if ( polygon == null )
            {
                //kvsMessageError("Cannot create isosurfaces.");
                throw new KVSException("Cannot create isosurfaces.");
            }

            // Shallow copy.
            m_object = polygon;
            
        }
        else // volume->volumeType() == kvs::VolumeObjectBase::Unstructured
        {
            final UnstructuredVolumeObject unstructured_volume =
                (UnstructuredVolumeObject)volume;
            
            MarchingTetrahedra factory = new MarchingTetrahedra(
                    m_isolevel,
                    m_object.normalType(),
                    m_duplication,
                    this.transferFunction() );

            PolygonObject polygon = (PolygonObject)factory.exec( unstructured_volume );
            if ( polygon == null )
            {
                //kvsMessageError("Cannot create isosurfaces.");
                throw new KVSException("Cannot create isosurfaces.");
            }

            // Shallow copy.
            m_object = polygon;
        }
    }

}
