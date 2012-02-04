package kvs.core.fileformat.kvsml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import kvs.core.KVSException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TransferFunctionParser {

    protected int   m_resolution;             ///< number of vertices
    protected Node  m_transfer_function_node; ///< pointer to the transfer function node

    public TransferFunctionParser(){}

    public TransferFunctionParser( Document document ) throws KVSException{
        this.parse( document );
    }

    public int resolution()
    {
        return( m_resolution );
    }

    public void parse( final Document document ) throws KVSException
    {
        // <KVSML>
        Element kvsml_node = document.getDocumentElement();
        if( kvsml_node == null )
        {
            throw new KVSException("Cannot find <KVSML>.");
        }

        // <TransferFunction>
        m_transfer_function_node = TagParser.findChildNode( kvsml_node, "TransferFunction" );
        if( m_transfer_function_node == null )
        {
            throw new KVSException("Cannot find <TransferFunction>.");
        }

        // <TransferFunction resolution="xxx">
        Element transfer_function_element = (Element) m_transfer_function_node;
        this.parse_resolution( transfer_function_element );

    }

    public void setOpacitiesAndColorsTo(
                                        float[] opacities,
                                        int[]  colors ) throws KVSException
    {
        Element transfer_function_element = (Element)m_transfer_function_node;
        String file = transfer_function_element.getAttribute( "file" );
        String transfer_function_text = null;
        if ( file.equals( "" ) )
        {
            /* <TransferFunction resolution="xxx">
             *     a r b g
             *     a r b g
             *     .......
             * </TransferFunction>
             */
            transfer_function_text = transfer_function_element.getNodeValue();
            if ( transfer_function_text == null )
            {
                throw new KVSException("No value in <TransferFunction>");
            }
        }
        else
        {
            /* <TransferFunction resolution="xxx" file="filename.dat"/>
             *
             * 'filename.dat' is described as follows:
             *     a r b g
             *     a r b g
             *     .......
             */
            FileReader f;
            BufferedReader b;
            StringBuilder text = new StringBuilder(m_resolution * 7);
            try {
                f = new FileReader( file );
                b = new BufferedReader(f);
                while(b.ready()){
                    text.append( b.readLine() );
                }
                transfer_function_text = text.toString();
                b.close();
                f.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final String delim = " \n";
        StringTokenizer t = new StringTokenizer( transfer_function_text, delim );

        opacities = new float[m_resolution];
        colors = new int[m_resolution * 3];

        final int nloops = m_resolution;
        for ( int i = 0; i < nloops; i++ )
        {
            opacities[i]        = Float.valueOf( t.nextToken() );
            colors[ 3 * i ]     = Byte.valueOf( t.nextToken());
            colors[ 3 * i + 1 ] = Byte.valueOf( t.nextToken());
            colors[ 3 * i + 2 ] = Byte.valueOf( t.nextToken());
        }
    }


    public void parse_resolution( final Element element ) throws KVSException
    {
        String resolution = element.getAttribute( "resolution" );
        if( resolution.equals( "" ) )
        {
            throw new KVSException("Cannot find 'resolution' in <TransferFunction>.");
        }

        m_resolution = Integer.valueOf( resolution );

    }


}
