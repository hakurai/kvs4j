package kvs.core.visualization.importer;

import java.nio.FloatBuffer;

import kvs.core.fileformat.FileFormatBase;
import kvs.core.fileformat.avsucd.AVSUcd;
import kvs.core.fileformat.avsucd.AVSUcd.ElementType;
import kvs.core.fileformat.kvsml.KVSMLObjectUnstructuredVolume;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.UnstructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase.CellType;

public class UnstructuredVolumeImporter extends ImporterBase {

    private UnstructuredVolumeImporter() {
    }

    public static CellType ElementTypeToCellType( final ElementType element_type ) {
        if( element_type == ElementType.Tetrahedra ){
            return (CellType.Tetrahedra);
        } else if( element_type == ElementType.Tetrahedra2 ){
            return (CellType.QuadraticTetrahedra);
        } else if( element_type == ElementType.Hexahedra ){
            return (CellType.Hexahedra);
        } else if( element_type == ElementType.Hexahedra2 ){
            return (CellType.QuadraticHexahedra);
        } else {
            //kvsMessageError( "Unknown element type." );
            return (CellType.UnknownCellType);
        }
    }

    public static ObjectBase exec( FileFormatBase file_format ) {
        //const std::string class_name = file_format->className();
        if( file_format instanceof KVSMLObjectUnstructuredVolume ){
            importFile( (KVSMLObjectUnstructuredVolume) (file_format) );
        } else if( file_format instanceof AVSUcd ){
            return importFile( (AVSUcd) (file_format) );
        } else {
            //kvsMessageError( "Unsupported class '%s'.", class_name.c_str() );
        }

        return null;
    }

    private static UnstructuredVolumeObject importFile( final KVSMLObjectUnstructuredVolume kvsml ) {
        UnstructuredVolumeObject object = new UnstructuredVolumeObject();

        object.setVeclen( kvsml.veclen() );
        object.setNNodes( kvsml.nnodes() );
        object.setNCells( kvsml.ncells() );
        object.setCellType( StringToCellType( kvsml.cellType() ) );
        object.setCoords( kvsml.coords().array() );
        object.setConnections( kvsml.connections().array() );
        object.setValues( kvsml.values() );
        object.updateMinMaxCoords();
        object.updateMinMaxValues();

        return object;
    }

    private static ObjectBase importFile( final AVSUcd ucd ) {
        UnstructuredVolumeObject object = new UnstructuredVolumeObject();

        object.setVeclen( ucd.veclens().get( ucd.componentID() ) );
        object.setNNodes( ucd.nnodes() );
        object.setNCells( ucd.nelements() );
        object.setCellType( ElementTypeToCellType( ucd.elementType() ) );
        object.setCoords( ucd.coords() );
        object.setConnections( ucd.connections() );
        object.setValues( FloatBuffer.wrap( ucd.values() ) );
        object.updateMinMaxCoords();
        object.updateMinMaxValues();

        return object;
    }

    public static CellType StringToCellType( final String cell_type ) {
        if( cell_type.equals( "tetrahedra" ) ){
            return (CellType.Tetrahedra);
        } else if( cell_type.equals( "hexahedra" ) ){
            return (CellType.Hexahedra);
        } else {
            //kvsMessageError( "Unknown cell type '%s'.", cell_type.c_str() );
            return (CellType.UnknownCellType);
        }
    }
}
