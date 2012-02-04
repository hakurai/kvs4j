package kvs.core.visualization.importer;

import kvs.core.fileformat.FileFormatBase;
import kvs.core.fileformat.kvsml.KVSMLObjectLine;
import kvs.core.matrix.Vector3f;
import kvs.core.visualization.object.LineObject;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.LineObject.ColorType;
import kvs.core.visualization.object.LineObject.LineType;

public class LineImporter extends ImporterBase {

    public LineImporter(){
    }

    @Override
    public ObjectBase exec( FileFormatBase file_format ) {
        //const std::string class_name = file_format->className();
        if ( file_format instanceof KVSMLObjectLine )
        {
            return this.importFile( (KVSMLObjectLine)file_format );
        }
        else
        {
            //kvsMessageError( "Unsupported class '%s'.", class_name.c_str() );
        }

        return null;
    }

    private ObjectBase importFile( final KVSMLObjectLine kvsml )
    {

        LineObject object = new LineObject();
        object.setLineType( stringToLineType( kvsml.lineType() ) );
        object.setColorType( stringToColorType( kvsml.colorType() ) );
        object.setCoords( kvsml.coords() );
        object.setColors( kvsml.colors() );
        object.setConnections( kvsml.connections() );
        object.setSizes( kvsml.sizes() );

        this.set_min_max_coord( object );
        
        return object;
    }
    
    private void set_min_max_coord( LineObject object )
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

    public LineType stringToLineType( final String line_type )
    {
        if (      line_type.equals( "strip"    ) ) { return( LineObject.LineType.Strip ); }
        else if ( line_type.equals( "uniline"  ) ) { return( LineObject.LineType.Uniline ); }
        else if ( line_type.equals( "polyline" ) ) { return( LineObject.LineType.Polyline ); }
        else if ( line_type.equals( "segment"  ) ) { return( LineObject.LineType.Segment ); }
        else
        {
            //kvsMessageError( "Unknown line type '%s'.", line_type.c_str() );
            return( LineObject.LineType.UnknownLineType );
        }
    }

    public ColorType stringToColorType( final String color_type )
    {
        if (      color_type.equals( "vertex" ) ) { return( LineObject.ColorType.VertexColor ); }
        else if ( color_type.equals( "line"   ) ) { return( LineObject.ColorType.LineColor ); }
        else
        {
            //kvsMessageError( "Unknown line color type '%s'.", color_type.c_str() );
            return( LineObject.ColorType.UnknownColorType );
        }
    }

}
