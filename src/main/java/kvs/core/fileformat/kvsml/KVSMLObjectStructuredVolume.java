package kvs.core.fileformat.kvsml;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;
import kvs.core.matrix.Vector3i;

public class KVSMLObjectStructuredVolume extends FileFormatBase {

    private static final long serialVersionUID = 1879468689677256649L;
    protected String    m_grid_type;  ///< grid type
    protected int       m_veclen;     ///< vector length
    protected Vector3i  m_resolution; ///< grid resolution
    protected Buffer    m_values;     ///< field value array

    public KVSMLObjectStructuredVolume(){

    }

    public KVSMLObjectStructuredVolume( final File filename ) throws KVSException{
        this.read( filename );
    }

    public String gridType()
    {
        return m_grid_type;
    }

    public int veclen()
    {
        return m_veclen;
    }

    public Vector3i resolution()
    {
        return m_resolution;
    }

    public Buffer values()
    {
        return m_values;
    }

    public void setVeclen( final int veclen )
    {
        m_veclen = veclen;
    }

    public void setResolution( final Vector3i resolution )
    {
        m_resolution = resolution;
    }

    public void setValues( final Buffer values )
    {
        m_values = values;
    }

    public static boolean check( final File filename )
    {
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse( filename );
            return StructuredVolumeObjectParser.check( document );
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void read( final File filename ) throws KVSException {
        m_filename = filename;
        try{
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse( filename );

            StructuredVolumeObjectParser parser =new StructuredVolumeObjectParser();
            parser.parse( document );

            m_grid_type  = parser.gridType();
            m_veclen     = parser.veclen();
            m_resolution = parser.resolution();

            m_values = parser.setValuesTo();
        } catch (SAXException e) {
            e.printStackTrace();
            throw new KVSException("Xml parse failured");
        } catch (IOException e) {
            e.printStackTrace();
            throw new KVSException("Read failured");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new KVSException("Create DocumentBuilder failured");
        }
    }

    @Override
    public void write( final File filename ) throws KVSException {
        throw new KVSException("Not yet implemented.");
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        String crlf = System.getProperty( "line.separator" );
        
        sb.append( "Grid type:  " );
        sb.append( m_grid_type );
        sb.append( crlf );
        sb.append( "Veclen:        " );
        sb.append( m_veclen );
        sb.append( crlf );
        sb.append( "Resolution: " );
        sb.append( m_resolution );
        sb.append( crlf );
        sb.append( "Value type:    " );
        sb.append( m_values );
        sb.append( crlf );
        
        return sb.toString();
    }

}
