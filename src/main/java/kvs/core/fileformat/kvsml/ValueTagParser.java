package kvs.core.fileformat.kvsml;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import kvs.core.KVSException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ValueTagParser {

    protected int m_nnodes; ///< number of nodes

    public ValueTagParser( final int nnodes ){
        m_nnodes = nnodes;
    }

    public Buffer parse( final Node parent_node ) throws KVSException
    {
        Buffer values = null;
        // <Value>
        Node value_node = TagParser.findChildNode( parent_node, "Value" );
        if( value_node == null )
        {
            throw new KVSException("Cannot find <Value>.");
        }

        // <Value veclen="xxx">
        Element value_element = (Element)value_node;
        String veclen = value_element.getAttribute( "veclen" );
        if( veclen.equals( "" ) )
        {
            throw new KVSException("Cannot find 'veclen' in <Value>.");
        }

        // <DataArray>
        Node data_array_node = TagParser.findChildNode( value_node, "DataArray" );
        if( data_array_node == null )
        {
            throw new KVSException("Cannot find <DataArray>");
        }

        // <DataArray type="xxx">
        Element data_array_element = (Element)data_array_node;
        String type = data_array_element.getAttribute( "type" );
        if( type.equals( "" ) )
        {
            throw new KVSException("Cannot find 'type' in <DataArray>.");
        }

        final int nvalues = m_nnodes * Integer.valueOf( veclen );
        if( type.equals( "char" ) )
        {
            values = ByteBuffer.allocate( nvalues );
            TagParser.parseArray( data_array_node, values );
        }
        else if( type.equals( "unsigned char" ) || type.equals( "uchar" ) )
        {
            values = ByteBuffer.allocate( nvalues );
            TagParser.parseArray( data_array_node, values );
        }
        else if( type.equals( "short" ) )
        {
            values = ShortBuffer.allocate( nvalues );
            TagParser.parseArray( data_array_node, values );
        }
        else if( type.equals( "unsigned short" ) || type.equals( "ushort" ) )
        {
            values = ShortBuffer.allocate( nvalues );
            TagParser.parseArray( data_array_node, values );
        }
        else if( type.equals( "int" ) )
        {
            values = IntBuffer.allocate( nvalues );
            TagParser.parseArray( data_array_node, values );
        }
        else if( type.equals( "unsigned int" ) || type.equals( "uint" ) )
        {
            values = IntBuffer.allocate( nvalues );
            TagParser.parseArray( data_array_node, values );
        }
        else if( type.equals( "float" ) )
        {
            values = FloatBuffer.allocate( nvalues );
            TagParser.parseArray( data_array_node, values );
        }
        else if( type.equals( "double" ) )
        {
            values = DoubleBuffer.allocate( nvalues );
            TagParser.parseArray( data_array_node, values );
        }
        else
        {
            new KVSException("Unknown 'type' in <DataArray>.");
        }
        
        return values;

    }

}
