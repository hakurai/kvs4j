package kvs.core.fileformat.bmp;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;

public class Bmp extends FileFormatBase{
    
    private static final long serialVersionUID = 6972821781485609999L;
    protected int                      m_width;  ///< width
    protected int                      m_height; ///< height
    protected int                      m_bpp;    ///< byte per pixel
    protected ByteBuffer m_data;   ///< pixel data 
    
    public int width(){
        return m_width;
    }
    
    public int height(){
        return m_height;
    }
    
    public ByteBuffer data(){
        return m_data;
    }
    
    public int bitsPerPixel(){
        return m_bpp * 8;
    }

    @Override
    public void read( File filename ) throws KVSException {
        try {
            BufferedImage image = ImageIO.read( filename );
            m_width = image.getWidth();
            m_height = image.getHeight();
            Raster raster = image.getData();
            
            m_bpp = raster.getNumDataElements();
            
            m_data = ByteBuffer.allocate( m_width * m_height * m_bpp );
            
            int[] tmp = new int[ m_bpp ];
            
            for( int y = 0; y < m_height ; y++ ){
                for( int x = 0; x < m_width ; x++ ){
                    tmp = raster.getPixel( x, y, tmp );
                    for(int i = 0; i < tmp.length ; i++ ){
                        m_data.put( (byte)tmp[i] );
                    }
                }
            }
            m_data.clear();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void write( File filename ) throws KVSException {
    }

}
