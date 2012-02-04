package kvs.core.fileformat.kvsml;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;
import kvs.core.fileformat.kvsml.TagParser.WritingDataType;

public class KVSMLObjectPolygon extends FileFormatBase {
    
    private static final long serialVersionUID = 7775861188348840188L;
    protected WritingDataType   m_writing_type; ///< writing data type
    protected String            m_polygon_type; ///< polygon type
    protected String            m_color_type;   ///< polygon color type
    protected String            m_normal_type;  ///< polygon normal type
    protected FloatBuffer       m_coords;       ///< coordinate array
    protected IntBuffer         m_connections;  ///< connection array
    protected ByteBuffer        m_colors;       ///< color (r,g,b) array
    protected ByteBuffer        m_opacities;    ///< opacity array
    protected FloatBuffer       m_normals;      ///< normal array
    
    public KVSMLObjectPolygon(){
        m_writing_type = WritingDataType.Ascii;
    }
    
    public KVSMLObjectPolygon( File filename ) throws KVSException{
        m_writing_type = WritingDataType.Ascii;
        this.read( filename );
    }
    
    public String polygonType() 
    {
        return( m_polygon_type );
    }

    public String colorType() 
    {
        return( m_color_type );
    }

    public String normalType() 
    {
        return( m_normal_type );
    }

    public FloatBuffer coords() 
    {
        return( m_coords );
    }

    public IntBuffer connections() 
    {
        return( m_connections );
    }

    public ByteBuffer colors() 
    {
        return( m_colors );
    }

    public ByteBuffer opacities() 
    {
        return( m_opacities );
    }

    public FloatBuffer normals() 
    {
        return( m_normals );
    }

    public void setWritingDataType(  WritingDataType writing_type )
    {
        m_writing_type = writing_type;
    }

    public void setPolygonType( String polygon_type )
    {
        m_polygon_type = polygon_type;
    }

    public void setColorType( String color_type )
    {
        m_color_type = color_type;
    }

    public void setNormalType( String normal_type )
    {
        m_normal_type = normal_type;
    }

    public void setCoords( FloatBuffer coords )
    {
        m_coords = coords;
    }

    public void setConnections( IntBuffer connections )
    {
        m_connections = connections;
    }

    public void setColors( ByteBuffer colors )
    {
        m_colors = colors;
    }

    public void setOpacities( ByteBuffer opacities )
    {
        m_opacities = opacities;
    }

    public void setNormals( FloatBuffer normals )
    {
        m_normals = normals;
    }
    
    public static boolean check( final File filename )
    {
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse( filename );
            return PolygonObjectParser.check( document );
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
    public void read( File filename ) throws KVSException {
        m_filename = filename;
        try{

            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse( filename );

            PolygonObjectParser parser =new PolygonObjectParser();
            parser.parse( document );

            m_polygon_type = parser.polygonType();
            m_color_type   = parser.colorType();
            m_normal_type  = parser.normalType();
            
            m_coords = parser.setCoordsTo();
            m_connections = parser.setConnectionsTo();
            m_colors = parser.setColorsTo();
            m_opacities = parser.setOpacitiesTo();
            m_normals = parser.setNormalsTo();
            

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
    public void write( File filename ) throws KVSException {
        m_filename = filename;
        try{

            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbfactory.newDocumentBuilder();

            Document doc = builder.newDocument();

            doc.setXmlVersion( "1.0" );
            doc.createComment( " Generated by kvs.KVSMLObjectLine.write() " );

            // <KVSML>
            Element kvs_node = doc.createElement( "KVSML" );
            doc.appendChild( kvs_node );

            // <Object type="PolygonObject">
            Element obj_elem = doc.createElement("Object");
            obj_elem.setAttribute("type","PolygonObject");
            Node obj_node = kvs_node.appendChild( obj_elem );

            // <PolygonObject>
            Element polygon_elem = doc.createElement("PolygonObject");
            polygon_elem.setAttribute("polygon_type",m_polygon_type);
            polygon_elem.setAttribute("color_type",m_color_type);
            polygon_elem.setAttribute("normal_type",m_normal_type);
            Node polygon_node = obj_node.appendChild( polygon_elem );

            // <Vertex nvertices="xxx">
            Element vertex_elem = doc.createElement("Vertex");
            vertex_elem.setAttribute("nvertices", Integer.toString( m_coords.limit() / 3 ) );
            Node vertex_node = polygon_node.appendChild( vertex_elem );
            
            

            // Writing data type.
            final WritingDataType writing_type = m_writing_type;

            // <Coord>
            if ( m_coords.limit() > 0 )
            {
                Element coord_elem = doc.createElement("Coord");
                Node coord_node = vertex_node.appendChild( coord_elem );
                final File coord_file = TagParser.getDataFilename( m_filename, "coord" );
                if ( !TagParser.composeArray( coord_node, writing_type, m_coords, 3, coord_file ) )
                {
                    throw new KVSException("Cannot compose the data array for <Coord>.");
                }
            }

            // <Color>
            if ( m_colors.limit() > 0 )
            {
                Element color_elem = doc.createElement("Color");
                Node color_node = vertex_node.appendChild( color_elem );
                final File color_file = TagParser.getDataFilename( m_filename, "color" );
                if ( !TagParser.composeArray( color_node, writing_type, m_colors, 3, color_file ) )
                {
                    throw new KVSException("Cannot compose the data array for <Color>.");
                }
            }
            
         // <Normal>
            if ( m_normals.limit() > 0 )
            {
                Element normal_elem = doc.createElement("Normal");
                Node normal_node = vertex_node.appendChild( normal_elem );
                final File normal_file = TagParser.getDataFilename( m_filename, "normal" );
                if ( !TagParser.composeArray( normal_node, writing_type, m_normals, 3, normal_file ) )
                {
                    throw new KVSException("Cannot compose the data array for <Normal>.");
                }
            }
            
         // <Polygon>
            Element polygon_connect_elem = doc.createElement("Polygon");
            final int npolygons = m_polygon_type.equals( "triangle" ) ? m_connections.limit() / 3 : m_connections.limit() / 4;
            polygon_connect_elem.setAttribute("npolygons", Integer.toString( npolygons ) );
            Node polygon_connect_node = polygon_node.appendChild( polygon_connect_elem );

            // <Connection>
            if ( m_connections.limit() > 0 )
            {
                Element connect_elem = doc.createElement("Connection");
                Node connect_node = polygon_connect_node.appendChild( connect_elem );
                final File connect_file = TagParser.getDataFilename( m_filename, "connect" );
                if ( !TagParser.composeArray( connect_node, writing_type, m_connections, 1, connect_file ) )
                {
                    throw new KVSException("Cannot compose the data array for <Connection>.");
                }
            }

            TransformerFactory tfactory = TransformerFactory.newInstance(); 
            Transformer transformer = tfactory.newTransformer(); 
            transformer.transform( new DOMSource( doc ), new StreamResult (m_filename ) ); 
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new KVSException("create document failured");
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new KVSException("file write failured");
        }
    }

}
