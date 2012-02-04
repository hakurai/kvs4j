package kvs.core.fileformat.dicom;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;
import kvs.core.matrix.Vector2f;

public class Dicom extends FileFormatBase {
    
    private static final long serialVersionUID = 1L;

    // DICOM attribute
    protected Attribute          m_attribute;            ///< attribute

    // DICOM header information
    protected ArrayList<Element> m_element_list;         ///< element list

    protected String             m_modality;             ///< (0008,0060) modality
    protected String             m_manufacturer;         ///< (0008,0070) manufacturer

    protected double             m_slice_thickness;      ///< (0018,0050) slice thickness
    protected double             m_slice_spacing;        ///< (0018,0088) spacing between slices

    protected int                m_series_number;        ///< (0020,0011) series number
    protected int                m_image_number;         ///< (0020,0013) image number
    protected double             m_slice_location;       ///< (0020,1041) slice location

    protected short             m_row;                  ///< (0028,0010) rows (height)
    protected short             m_column;               ///< (0028,0011) columns (width)
    protected Vector2f          m_pixel_spacing;        ///< (0028,0030) pixel spacing
    protected short             m_bits_allocated;       ///< (0028,0100) bits allocated
    protected short             m_bits_stored;          ///< (0028,0101) bits stored
    protected short             m_high_bit;             ///< (0028,0102) high bit
    protected boolean           m_pixel_representation; ///< (0028,0103) pixel representation
    protected int               m_window_level;         ///< (0028,1050) window center
    protected int               m_window_width;         ///< (0028,1051) window width
    protected double            m_rescale_intersept;    ///< (0028,1052) rescale intersept
    protected double            m_rescale_slope;        ///< (0028,1053) rescale slope

    // Parameters in this class
    protected Window            m_window;           ///< window
    protected int               m_min_raw_value;    ///< min. value of the raw data
    protected int               m_max_raw_value;    ///< max. value of the raw data
    //std::ios::pos_type        m_position;         ///< raw data position
    protected ByteBuffer        m_raw_data;         ///< raw data

    @Override
    public void read( File filename ) throws KVSException {
        // TODO 自動生成されたメソッド・スタブ
        
    }

    @Override
    public void write( File filename ) throws KVSException {
        // TODO 自動生成されたメソッド・スタブ
        
    }

}
