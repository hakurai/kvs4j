package kvs.core.visualization.importer;

import kvs.core.fileformat.FileFormatBase;
import kvs.core.fileformat.kvsml.KVSMLObjectPolygon;
import kvs.core.matrix.Vector3f;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PolygonObject;
import kvs.core.visualization.object.PolygonObject.ColorType;
import kvs.core.visualization.object.PolygonObject.NormalType;
import kvs.core.visualization.object.PolygonObject.PolygonType;

public class PolygonImporter extends ImporterBase {
    
    private static final long serialVersionUID = -3589518272232027248L;

    public PolygonImporter(){
        
    }

    @Override
    public ObjectBase exec( FileFormatBase file_format ) {
      //const std::string class_name = file_format->className();
        if ( file_format instanceof KVSMLObjectPolygon )
        {
            return this.importFile( (KVSMLObjectPolygon)file_format );
        }
        else
        {
            //kvsMessageError( "Unsupported class '%s'.", class_name.c_str() );
        }

        return null;
    }
    
    private ObjectBase importFile( final KVSMLObjectPolygon kvsml )
    {
        PolygonObject object = new PolygonObject();
        object.setPolygonType( stringToPolygonType( kvsml.polygonType() ) );
        object.setColorType( stringToColorType( kvsml.colorType() ) );
        object.setNormalType( stringToNormalType( kvsml.normalType() ) );
        object.setCoords( kvsml.coords() );
        object.setColors( kvsml.colors() );
        object.setConnections( kvsml.connections() );
        object.setNormals( kvsml.normals() );
        object.setOpacities( kvsml.opacities() );

        this.set_min_max_coord( object );
        
        return object;
    }
    
    private void set_min_max_coord( PolygonObject object )
    {
        float[] coords = object.coords().array();
        float min_coord_x = coords[0];
        float min_coord_y = coords[1];
        float min_coord_z = coords[2];
        
        float max_coord_x = coords[0];
        float max_coord_y = coords[1];
        float max_coord_z = coords[2];
        
        final int  dimension = 3;
        final int  nvertices = coords.length / dimension;
        int        index3    = 3;
        for ( int i = 1; i < nvertices; i++, index3 += 3 )
        {
            min_coord_x = kvs.core.util.Math.min( min_coord_x, coords[index3] );
            min_coord_y = kvs.core.util.Math.min( min_coord_y, coords[index3 + 1] );
            min_coord_z = kvs.core.util.Math.min( min_coord_z, coords[index3 + 2] );

            max_coord_x = kvs.core.util.Math.max( max_coord_x, coords[index3] );
            max_coord_y = kvs.core.util.Math.max( max_coord_y, coords[index3 + 1] );
            max_coord_z = kvs.core.util.Math.max( max_coord_z, coords[index3 + 2] );
        }
        
        Vector3f min_coord = new Vector3f( min_coord_x, min_coord_y, min_coord_z );
        Vector3f max_coord = new Vector3f( max_coord_x, max_coord_y, max_coord_z );
        
        object.setMinMaxObjectCoords( min_coord, max_coord );
        object.setMinMaxExternalCoords( min_coord, max_coord );
    }
    
    public PolygonType stringToPolygonType( final String polygon_type )
    {
        if (      polygon_type.equals( "triangle"   ) ){
            return PolygonObject.PolygonType.Triangle;
            }
        else if ( polygon_type.equals( "quadrangle" ) ){
            return PolygonObject.PolygonType.Quadrangle;
            }
        else
        {
            //kvsMessageError( "Unknown polygon type '%s'.", polygon_type.c_str() );
            return PolygonObject.PolygonType.UnknownPolygonType;
        }
    }
    
    public ColorType stringToColorType( final String color_type )
    {
        if (      color_type.equals( "vertex"  ) ){
            return PolygonObject.ColorType.VertexColor;
            }
        else if ( color_type.equals( "polygon" ) ){
            return PolygonObject.ColorType.PolygonColor;
            }
        else
        {
            //kvsMessageError( "Unknown polygon color type '%s'.", color_type.c_str() );
            return PolygonObject.ColorType.UnknownColorType;
        }
    }
    
    public NormalType stringToNormalType( final String normal_type )
    {
        if (      normal_type.equals( "vertex"  ) ){
            return PolygonObject.NormalType.VertexNormal;
            }
        else if ( normal_type.equals( "polygon" ) ){
            return PolygonObject.NormalType.PolygonNormal;
            }
        else
        {
            //kvsMessageError( "Unknown polygon normal type '%s'.", normal_type.c_str() );
            return PolygonObject.NormalType.UnknownNormalType;
        }
    }

}
