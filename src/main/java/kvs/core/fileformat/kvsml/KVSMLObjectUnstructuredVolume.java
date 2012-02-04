package kvs.core.fileformat.kvsml;

import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;

public class KVSMLObjectUnstructuredVolume extends FileFormatBase {

    private static final long serialVersionUID = 5552414766527074859L;
    protected String        m_cell_type;   ///< cell type
    protected int           m_veclen;      ///< vector length
    protected int           m_nnodes;      ///< number of nodes
    protected int           m_ncells;      ///< number of cells
    protected Buffer        m_values;      ///< field value array
    protected FloatBuffer   m_coords;      ///< coordinate value array
    protected IntBuffer     m_connections; ///< connection id array

    public KVSMLObjectUnstructuredVolume(){

    }

    public KVSMLObjectUnstructuredVolume( final File filename ) throws KVSException{
        this.read( filename );
    }

    public String cellType()
    {
        return m_cell_type;
    }

    public int veclen()
    {
        return m_veclen;
    }

    public int nnodes()
    {
        return m_nnodes;
    }

    public int ncells()
    {
        return m_ncells;
    }

    public Buffer values()
    {
        return m_values;
    }

    public FloatBuffer coords()
    {
        return m_coords;
    }

    public IntBuffer connections()
    {
        return m_connections;
    }

    public void setVeclen( final int veclen )
    {
        m_veclen = veclen;
    }

    public void setNNodes( final int nnodes )
    {
        m_nnodes = nnodes;
    }

    public void setNCells( final int ncells )
    {
        m_ncells = ncells;
    }

    public void setValues( final Buffer values )
    {
        m_values = values;
    }

    public void setCoords( final FloatBuffer coords )
    {
        m_coords = coords;
    }

    public static boolean check( final File filename )
    {
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse( filename );
            return UnstructuredVolumeObjectParser.check( document );
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

            UnstructuredVolumeObjectParser parser =new UnstructuredVolumeObjectParser();
            parser.parse( document );

            TagParser.setCurrentFile( filename );


            m_cell_type  = parser.cellType();
            m_veclen     = parser.veclen();
            m_nnodes     = parser.nnodes();
            m_ncells     = parser.ncells();

            m_values = parser.setValuesTo();
            m_coords = parser.setCoordsTo();
            m_connections = parser.setConnectionsTo();

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
        
        sb.append( "Cell type:     " );
        sb.append( m_cell_type );
        sb.append( crlf );
        sb.append( "Veclen:        " );
        sb.append( m_veclen );
        sb.append( crlf );
        sb.append( "Num. of nodes: " );
        sb.append( m_nnodes );
        sb.append( crlf );
        sb.append( "Num. of cells: " );
        sb.append( m_ncells );
        sb.append( crlf );
        sb.append( "Value type:    " );
        sb.append( m_values );
        sb.append( crlf );
        
        return sb.toString();
    }

}
