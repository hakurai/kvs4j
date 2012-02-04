package kvs.core.visualization.importer;

import kvs.core.fileformat.FileFormatBase;
import kvs.core.fileformat.kvsml.KVSMLObjectPoint;
import kvs.core.matrix.Vector3f;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PointObject;

public class PointImporter extends ImporterBase {

    public PointImporter(){
        
    }

    @Override
    public ObjectBase exec( FileFormatBase file_format ) {
      //const std::string class_name = file_format->className();
        if ( file_format instanceof KVSMLObjectPoint )
        {
            return this.importFile( (KVSMLObjectPoint)file_format );
        }
        else
        {
            //kvsMessageError( "Unsupported class '%s'.", class_name.c_str() );
        }
        return null;
    }
    
    private ObjectBase importFile( final KVSMLObjectPoint kvsml )
    {

        PointObject object = new PointObject();
        object.setCoords( kvsml.coords() );
        object.setColors( kvsml.colors() );
        object.setNormals( kvsml.normals() );
        object.setSizes( kvsml.sizes() );

        this.set_min_max_coord( object );
        
        return object;
    }
    

    private void set_min_max_coord( PointObject object )
    {
        float[] coords = object.coords().array();
        Vector3f min_coord = new Vector3f( coords[0], coords[1], coords[2] );
        Vector3f max_coord = min_coord;
        final int  dimension = 3;
        final int  nvertices = coords.length / dimension;
        int        index3    = 3;
        for ( int i = 1; i < nvertices; i++, index3 += 3 )
        {
            
            min_coord = new Vector3f( kvs.core.util.Math.min( min_coord.getX(), coords[index3] ),
                                      kvs.core.util.Math.min( min_coord.getY(), coords[index3 + 1] ),
                                      kvs.core.util.Math.min( min_coord.getZ(), coords[index3 + 2] ) );

            max_coord = new Vector3f( kvs.core.util.Math.max( max_coord.getX(), coords[index3] ),
                                      kvs.core.util.Math.max( max_coord.getY(), coords[index3 + 1] ),
                                      kvs.core.util.Math.max( max_coord.getZ(), coords[index3 + 2] ) );
        }

        object.setMinMaxObjectCoords( min_coord, max_coord );
        object.setMinMaxExternalCoords( min_coord, max_coord );
    }

}
