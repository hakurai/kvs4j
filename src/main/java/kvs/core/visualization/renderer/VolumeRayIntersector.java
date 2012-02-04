package kvs.core.visualization.renderer;

import kvs.core.matrix.Vector3f;
import kvs.core.visualization.object.VolumeObjectBase;

public class VolumeRayIntersector extends Ray {

    private Vector3f[]              m_vertex = new Vector3f[8];
    //private final VolumeObjectBase  m_reference_volume;

    public VolumeRayIntersector( final VolumeObjectBase volume )
    {
        super();
        //m_reference_volume = volume;
        final Vector3f min = new Vector3f(
                                volume.minExternalCoord().getX() + 1e-3f,
                                volume.minExternalCoord().getY() + 1e-3f,
                                volume.minExternalCoord().getZ() + 1e-3f );

        final Vector3f max = new Vector3f(
                                volume.maxExternalCoord().getX() - 1e-3f,
                                volume.maxExternalCoord().getY() - 1e-3f,
                                volume.maxExternalCoord().getZ() - 1e-3f );

        m_vertex[0]= new Vector3f( min.getX(), min.getY(), min.getZ() );
        m_vertex[1]= new Vector3f( max.getX(), min.getY(), min.getZ() );
        m_vertex[2]= new Vector3f( max.getX(), max.getY(), min.getZ() );
        m_vertex[3]= new Vector3f( min.getX(), max.getY(), min.getZ() );
        m_vertex[4]= new Vector3f( min.getX(), min.getY(), max.getZ() );
        m_vertex[5]= new Vector3f( max.getX(), min.getY(), max.getZ() );
        m_vertex[6]= new Vector3f( max.getX(), max.getY(), max.getZ() );
        m_vertex[7]= new Vector3f( min.getX(), max.getY(), max.getZ() );
    }
    
    public boolean isIntersected()
    {
        return(
            this.isIntersected( m_vertex[0], m_vertex[3], m_vertex[2], m_vertex[1] ) ||
            this.isIntersected( m_vertex[0], m_vertex[1], m_vertex[5], m_vertex[4] ) ||
            this.isIntersected( m_vertex[1], m_vertex[2], m_vertex[6], m_vertex[5] ) ||
            this.isIntersected( m_vertex[2], m_vertex[3], m_vertex[7], m_vertex[6] ) ||
            this.isIntersected( m_vertex[3], m_vertex[0], m_vertex[4], m_vertex[7] ) ||
            this.isIntersected( m_vertex[4], m_vertex[5], m_vertex[6], m_vertex[7] ) );
    }
    
    public boolean isInside()
    {
        final Vector3f point = this.point();

        if ( m_vertex[0].getZ() < point.getZ() && point.getZ() < m_vertex[6].getZ() )
        {
            if ( m_vertex[0].getY() < point.getY() && point.getY() < m_vertex[6].getY() )
            {
                if ( m_vertex[0].getX() < point.getX() && point.getX() < m_vertex[6].getX() )
                {
                    return( true );
                }
            }
        }

        return( false );
    }

    public void step( final float delta_t )
    {
        this.setT( this.t() + delta_t );
    }
}
