package kvs.core.visualization.pipeline;

import kvs.core.KVSException;
import kvs.core.visualization.object.ObjectBase;

public interface PipelineModule {
    
    public ObjectBase exec( final ObjectBase object ) throws KVSException;

}
