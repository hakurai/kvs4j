package kvs.core.visualization.mapper;

import java.io.File;
import java.io.Serializable;

import kvs.core.KVSException;
import kvs.core.fileformat.kvsml.KVSMLTransferFunction;

public class TransferFunction implements Serializable{

    private static final long serialVersionUID = -3646642997648838590L;

    private static final int DefaultResolution = 256;

    private ColorMap   m_color_map;   ///< Color map.
    private OpacityMap m_opacity_map; ///< Opacity map.

    public TransferFunction(){
        this( DefaultResolution );
    }


    public TransferFunction( final int resolution ){
        m_color_map = new ColorMap( resolution );
        m_opacity_map = new OpacityMap( resolution );
    }

    public TransferFunction( final File filename )
    {
        this.read( filename );
    }

    public TransferFunction( final ColorMap color_map )
    {
        m_color_map = color_map;
        m_opacity_map = new OpacityMap( color_map.resolution() );
    }

    public TransferFunction( final OpacityMap opacity_map )
    {
        m_color_map = new ColorMap( opacity_map.resolution() );
        m_opacity_map = opacity_map;
    }

    public TransferFunction( final ColorMap color_map, final OpacityMap opacity_map )
    {
        m_color_map = color_map;
        m_opacity_map = opacity_map;
    }

    public void setColorMap( final ColorMap color_map )
    {
        m_color_map = color_map;
    }

    public void setOpacityMap( final OpacityMap opacity_map )
    {
        m_opacity_map = opacity_map;
    }

    public ColorMap colorMap()
    {
        return( m_color_map );
    }

    public OpacityMap opacityMap()
    {
        return( m_opacity_map );
    }

    public int resolution()
    {
        //KVS_ASSERT( m_opacity_map.resolution() == m_color_map.resolution() );
        return( m_opacity_map.resolution() );
    }

    public void create( final int resolution )
    {
        m_opacity_map.create( resolution );
        m_color_map.create( resolution );
    }

    public boolean read( final File filename )
    {
        try{
            KVSMLTransferFunction transfer_function = new KVSMLTransferFunction( filename );
            m_opacity_map = new OpacityMap( transfer_function.opacities() );
            m_color_map   = new ColorMap( transfer_function.colors() );
        } catch (KVSException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
