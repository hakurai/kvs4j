package kvs.core.visualization.pipeline;

import java.io.File;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;
import kvs.core.fileformat.avsfield.AVSField;
import kvs.core.fileformat.avsucd.AVSUcd;
import kvs.core.fileformat.bmp.Bmp;
import kvs.core.fileformat.kvsml.KVSMLObjectLine;
import kvs.core.fileformat.kvsml.KVSMLObjectPoint;
import kvs.core.fileformat.kvsml.KVSMLObjectPolygon;
import kvs.core.fileformat.kvsml.KVSMLObjectStructuredVolume;
import kvs.core.fileformat.kvsml.KVSMLObjectUnstructuredVolume;
import kvs.core.visualization.importer.ImageImporter;
import kvs.core.visualization.importer.ImporterBase;
import kvs.core.visualization.importer.LineImporter;
import kvs.core.visualization.importer.PointImporter;
import kvs.core.visualization.importer.PolygonImporter;
import kvs.core.visualization.importer.StructuredVolumeImporter;
import kvs.core.visualization.importer.UnstructuredVolumeImporter;
import kvs.core.visualization.object.ObjectBase;

public class ObjectImporter {
    
    public enum ImporterType
    {
        Image,               ///< image object importer
        Point,               ///< point object importer
        Line,                ///< line object importer
        Polygon,             ///< polygon object importer
        StructuredVolume,    ///< structured volume object importer
        UnstructuredVolume,  ///< unstructured volume object importer
        Unknown;             ///< unknown importer
    };
    
    private File            m_filename;      ///< input filename
    private ImporterType    m_importer_type; ///< importer type
    private FileFormatBase  m_file_format;   ///< pointer to the estimated file format class
    private ImporterBase    m_importer;
    
    public ObjectImporter( final File filename ){
        m_filename = filename;
        m_importer_type = ImporterType.Unknown;
    }
    
    public ObjectBase importFile() throws KVSException{
        if ( !this.estimate_file_format() )
        {
            throw new KVSException( "Cannot create a file format class for '" +m_filename.getName()+ "'." );
        }

        if ( !this.estimate_importer() )
        {
            throw new KVSException( "Cannot create a importer class for '" +m_filename.getName()+ "'." );
        }

        m_file_format.read( m_filename );


        ObjectBase object = m_importer.exec( m_file_format );
        if ( object == null )
        {
            System.err.println( "Cannot import a object." );

            // NOTE: Delete m_importer only when the memory allocation
            //       of m_importer is failed.
            return null;
        }

        return object;
    }
    
    private boolean estimate_file_format()
    {
        File file = m_filename;
        
        if( !file.isFile() ){
            return false;
        }
        
        if ( file.getPath().endsWith( "fld" ) )
        {
            m_importer_type = ImporterType.StructuredVolume;
            m_file_format = new AVSField();
        }

        else if ( file.getPath().endsWith( "ucd" ) || file.getPath().endsWith( "inp" ) )
        {
            m_importer_type = ImporterType.UnstructuredVolume;
            m_file_format = new AVSUcd();
        }

        else if ( file.getPath().endsWith( "bmp" ) )
        {
            m_importer_type = ImporterType.Image;
            m_file_format = new Bmp();
        }

        else if ( file.getPath().endsWith( "ppm" ) )
        {
            m_importer_type = ImporterType.Image;
            //m_file_format = new Ppm();
        }

        else if ( file.getPath().endsWith( "pgm" ) )
        {
            m_importer_type = ImporterType.Image;
            //m_file_format = new Pgm();
        }

        else if ( file.getPath().endsWith( "pbm" ) )
        {
            m_importer_type = ImporterType.Image;
            //m_file_format = new Pbm();
        }

        else if ( file.getPath().endsWith( "tif" ) ||
                  file.getPath().endsWith( "tiff" )||
                  file.getPath().endsWith( "TIF" ) ||
                  file.getPath().endsWith( "TIFF" ) )
        {
            m_importer_type = ImporterType.Image;
            //m_file_format = new Tiff();
        }

        else if ( file.getPath().endsWith( "kvsml" ) )
        {
            if ( KVSMLObjectPoint.check( file ) )
            {
                m_importer_type = ImporterType.Point;
                m_file_format = new KVSMLObjectPoint();
            }

            else if ( KVSMLObjectLine.check( file ) )
            {
                m_importer_type = ImporterType.Line;
                m_file_format = new KVSMLObjectLine();
            }

            else if ( KVSMLObjectPolygon.check( file ) )
            {
                m_importer_type = ImporterType.Polygon;
                m_file_format = new KVSMLObjectPolygon();
            }

            else if ( KVSMLObjectStructuredVolume.check( file ) )
            {
                m_importer_type = ImporterType.StructuredVolume;
                m_file_format = new KVSMLObjectStructuredVolume();
            }

            else if ( KVSMLObjectUnstructuredVolume.check( file ) )
            {
                m_importer_type = ImporterType.UnstructuredVolume;
                m_file_format = new KVSMLObjectUnstructuredVolume();
            }
        }

        return( m_file_format != null );
    }
    
    private boolean estimate_importer()
    {
        switch( m_importer_type )
        {
        case Point:
        {
            m_importer = new PointImporter();
            break;
        }
        case Line:
        {
            m_importer = new LineImporter();
            break;
        }
        case Polygon:
        {
            m_importer = new PolygonImporter();
            break;
        }
        case StructuredVolume:
        {
            m_importer = new StructuredVolumeImporter();
            break;
        }
        case UnstructuredVolume:
        {
            m_importer = new UnstructuredVolumeImporter();
            break;
        }
        case Image:
        {
            m_importer = new ImageImporter();
            break;
        }
        default: break;
        }

        return( m_importer != null );
    }
}
