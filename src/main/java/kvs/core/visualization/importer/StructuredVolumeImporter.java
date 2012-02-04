package kvs.core.visualization.importer;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;
import kvs.core.fileformat.avsfield.AVSField;
import kvs.core.fileformat.avsfield.AVSField.FieldType;
import kvs.core.fileformat.kvsml.KVSMLObjectStructuredVolume;
import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector3i;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.StructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase.GridType;

public class StructuredVolumeImporter extends ImporterBase {
    
    public StructuredVolumeImporter()
    {}
    

    public static ObjectBase exec( FileFormatBase file_format ) throws KVSException {
        //final String class_name( file_format->className() );
                
        if ( file_format instanceof KVSMLObjectStructuredVolume )
        {
            return importFile( (KVSMLObjectStructuredVolume)( file_format ) );
        }
        else if ( file_format instanceof AVSField )
        {
            return importFile( (AVSField)( file_format ) );
        }
        else
        {
            //throw new KVSException( "Unsupported class '" +(file_fromat.Class.toString())+ "'." );
        }

        return null;
    }
    
    private static ObjectBase importFile( final KVSMLObjectStructuredVolume kvsml )
    {
        StructuredVolumeObject object = new StructuredVolumeObject();
        object.setGridType( StringToGridType( kvsml.gridType() ) );
        object.setVeclen( kvsml.veclen() );
        object.setResolution( kvsml.resolution() );
        object.setValues( kvsml.values() );
        object.updateMinMaxCoords();
                
        return object;
    }
    
    private static ObjectBase importFile( final AVSField field ) throws KVSException
    {
        if ( field.fieldType() != FieldType.Uniform )
        {
            throw new KVSException( "'Uniform' type in kvs::AVSField format is only supported." );

        }
        
        StructuredVolumeObject object = new StructuredVolumeObject();

        if ( field.hasMinMaxExt() )
        {
            final Vector3f min_coord = field.minExt();
            final Vector3f max_coord = field.maxExt();

            object.setMinMaxObjectCoords( min_coord, max_coord );
            object.setMinMaxExternalCoords( min_coord, max_coord );
        }
        else
        {
            final Vector3i dim = field.dim();
            final Vector3f  min_coord = Vector3f.ZERO;
            final Vector3f  max_coord = new Vector3f(dim.getX() - 1, dim.getY() - 1, dim.getZ() - 1 );

            object.setMinMaxObjectCoords( min_coord, max_coord );
            object.setMinMaxExternalCoords( min_coord, max_coord );
        }

        object.setGridType( GridType.Uniform );
        object.setVeclen( field.veclen() );
        object.setResolution( field.dim() );
        object.setValues( field.values() );
        
        return object;
    }
    
    public static final GridType StringToGridType( final String grid_type )
    {
        if (      grid_type.equals( "uniform" )     ) { return( GridType.Uniform );     }
        else if ( grid_type.equals( "rectilinear" ) ) { return( GridType.Rectilinear ); }
        else if ( grid_type.equals( "curvilinear" ) ) { return( GridType.Curvilinear ); }
        else
        {
            //kvsMessageError( "Unknown grid type '%s'.", grid_type.c_str() );
            return( GridType.UnknownGridType );
        }
    }
    
    

}
