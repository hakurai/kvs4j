package kvs.core.fileformat.dicom;

public enum VRType {
    VR_NONE, ///< None
    VR_AE, ///< Application Entity    (16 bytes max.)
    VR_AS, ///< Age String            (4 bytes fixed)
    VR_AT, ///< Attribute Tag         (4 bytes fixed)
    VR_CS, ///< Code String           (16 bytes max.)
    VR_DA, ///< Data                  (8 bytes fixed)
    VR_DS, ///< Decimal String        (16 bytes max.)
    VR_DT, ///< Date Time             (26 bytes max.)
    VR_FL, ///< Floating Point Single (4 bytes fixed)
    VR_FD, ///< Floating Point Double (8 bytes fixed)
    VR_IS, ///< Integer String        (12 bytes max.)
    VR_LO, ///< Long String           (62 chars max.)
    VR_LT, ///< Long Text             (10240 chars max.)
    VR_OB, ///< Other Byte String     (*)
    VR_OW, ///< Other Word String     (*)
    VR_PN, ///< Person Name           (64 chars max. per component group)
    VR_SH, ///< Short String          (16 chars max.)
    VR_SL, ///< Singled Long          (4 bytes fixed)
    VR_SQ, ///< Sequence of Items     (*)
    VR_SS, ///< Signed Short          (2 bytes fixed)
    VR_ST, ///< Short Text            (1024 chars max.)
    VR_TM, ///< Time                  (16 bytes max.)
    VR_UI, ///< Unique Identifier     (64 bytes max.)
    VR_UL, ///< Unsigned Long         (4 bytes fixed)
    VR_US, ///< Unsigned short        (2 bytes fixed)
    VR_UN, ///< Unknown               (*)
    VR_UT; ///< Unlimited Text        (*)

}
