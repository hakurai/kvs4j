package kvs.core.fileformat.kvsml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import kvs.core.KVSException;
import kvs.core.fileformat.FileFormatBase;

public class KVSMLTransferFunction extends FileFormatBase {

    private static final long serialVersionUID = 4952241606743621828L;
    protected float[] m_opacities; // /< opacity array
    protected int[] m_colors; // /< color(r,g,b) array

    public KVSMLTransferFunction() {
    }

    public KVSMLTransferFunction( final File filename ) throws KVSException {

        this.read( filename );

    }

    public float[] opacities() {
        return (m_opacities);
    }

    public int[] colors() {
        return (m_colors);
    }

    public void setOpacities( final float[] opacities ) {
        m_opacities = opacities;
    }

    public void setColors( final int[] colors ) {
        m_colors = colors;
    }

    public boolean check( final String filename ) {
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse( new File( filename ) );

            TransferFunctionParser parser = new TransferFunctionParser();
            parser.parse( document );
            

        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void read( final File filename ) throws KVSException{
        m_filename = filename;
        
        try{

        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse( filename );

            TransferFunctionParser parser = new TransferFunctionParser();
            parser.parse( document );

            parser.setOpacitiesAndColorsTo( m_opacities, m_colors );
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
        // kvs::IgnoreUnusedVariable( filename );
        throw new KVSException("Not yet implemented.");
        
    }
}
