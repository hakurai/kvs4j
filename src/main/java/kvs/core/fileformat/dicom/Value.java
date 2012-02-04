/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kvs.core.fileformat.dicom;

/**
 *
 * @author eguchi
 */
public class Value {

    public enum DataType
    {
        DATA_CHAR( "char", 2 ), ///< char type data
        DATA_UCHAR( "unsigned char", 2 ), ///< unsigned char type data
        DATA_INT( "int", 4 ), ///< int type data
        DATA_UINT( "unsigned int", 4 ), ///< unsigned int type data
        DATA_SHORT( "short", 2 ), ///< short type data
        DATA_USHORT( "unsigned short", 2 ), ///< unsigned short type data
        DATA_FLOAT( "float", 4 ), ///< float type data
        DATA_DOUBLE( "double", 8 ), ///< double type data
        DATA_STRING( "string", 0 ), ///< string type data
        DATA_OTHER( "other", 0 ); ///< other

        private String data_type_string;
        private int data_type_size;

        private DataType( String string, int size ){
            data_type_string = string;
            data_type_size = size;
        }

        public String getString(){
            return data_type_string;
        }

        public int getSize(){
            return data_type_size;
        }
    };
/*
    private String m_value_string; ///< string type value

    private DataType      m_type;         ///< data type
    private int       m_length;       ///< data

    public Value( DataType type, int length )
    {
        // Check the data type.
        DataType data_type = type;
        if( type != DataType.DATA_STRING || type != DataType.DATA_OTHER )
        {
            if( type.getSize() != length )
            {
                data_type = DataType.DATA_STRING;
            }
        }

        m_type   = data_type;
        m_length = length;
    }
*/
}
