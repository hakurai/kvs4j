package kvs.core.fileformat.dicom;

public class Attribute {
    
    public final static String  Part10Code     = "DICM";
    public final static  int    Part10CodeSize = 4;
    public final static  int    Part10CodeSeek = 128;
    
    protected boolean m_part10; ///< part10 flag
    protected boolean m_swap;   ///< swap flag
    
    public boolean part10(){
        return m_part10;
    }

    public boolean swap(){
        return m_swap;
    }
    /*
    public boolean check( RandomAccessFile raf )
    {
        ifs.seekg( ::Part10CodeSeek, std::ios::beg );

        char part10[ ::Part10CodeSize ];
        ifs.read( part10, ::Part10CodeSize );
        part10[ ::Part10CodeSize ] = '\0';

        if( std::string(part10) == ::Part10Code )
        {
            m_part10 = true;
        }
        else
        {
            m_part10 = false;
            ifs.seekg( 0, std::ios::beg );
        }

        short temp;
        ifs.read( reinterpret_cast<char*>(&temp), sizeof(short) );
        m_swap = ( temp > 0xff );

        int size = sizeof(short);
        ifs.seekg( -size, std::ios::cur );

        return( true );
    }
    */
}
