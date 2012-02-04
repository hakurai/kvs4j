package kvs.core.fileformat;

import java.io.File;
import java.io.Serializable;

import kvs.core.KVSException;

public abstract class FileFormatBase implements Serializable{
    
    private static final long serialVersionUID = -5995663842733656446L;
    protected File    m_filename;   ///< Filename.
    //protected boolean   m_is_success; ///< Whether the reading is success or not.
    
    public FileFormatBase(){
        m_filename = new File("");
        //m_is_success = false;
    }
    
    public File filename(){
        return m_filename;
    }
    /*
    public boolean isSuccess()
    {
        return( m_is_success );
    }
    
    public boolean isFailure()
    {
        return( !m_is_success );
    }
    */
    public abstract void read( File filename ) throws KVSException;
    
    public abstract void write( File filename ) throws KVSException;

}
