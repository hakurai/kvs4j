package kvs.core.fileformat.dicom;

public class Tag {

    protected short     m_group_id;   ///< group ID
    protected short     m_element_id; ///< element ID
    protected VRType    m_vr_type;    ///< VR type (initial value)
    protected String    m_name;       ///< element name

    public Tag(){
        m_vr_type = VRType.VR_NONE;
        m_name = "";
    }

    public Tag( final short     group_id,
                final short     element_id,
                final VRType    vr_type,
                final String    name ){
        m_group_id = group_id;
        m_element_id = element_id;
        m_vr_type = vr_type;
        m_name = name;
    }
    
    public Tag( final Tag tag ){
        m_group_id = tag.m_group_id;
        m_element_id = tag.m_element_id;
        m_vr_type = tag.m_vr_type;
        m_name = tag.m_name;
    }

}
