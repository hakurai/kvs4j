package kvs.core.visualization.importer;


import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;
import kvs.core.visualization.object.ObjectBase;

public abstract interface  ImporterBase{
    
    public ObjectBase exec( final FileFormatBase file_format ) throws KVSException;
    
    
}
