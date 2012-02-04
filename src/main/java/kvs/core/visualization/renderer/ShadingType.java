package kvs.core.visualization.renderer;

import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;

public interface ShadingType {

    public void set( final Camera camera, final Light light );

    public float attenuation( final float[] gradient, final int offset );

}
