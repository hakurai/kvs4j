package kvs.core.util;
/**
 * VersionクラスはKVSのバージョン情報を保持するためのクラスです。
 * 
 */
public interface Version {
    
    /**
     * メジャーバージョン
     */
    public static final int KVS_VERSION_MAJOR = 1;
    
    /**
     * マイナーバージョン
     */
    public static final int KVS_VERSION_MINOR = 0;
    
    /**
     * パッチバージョン
     */
    public static final int KVS_VERSION_PATCH = 0;
    
    /**
     * バージョンステータス
     */
    public static final String KVS_VERSION_STATUS = "beta 2";
    
    /**
     * 完全なバージョン情報
     */
    public static final String KVS_NAME = KVS_VERSION_MAJOR + "." +
                                          KVS_VERSION_MINOR + "." +
                                          KVS_VERSION_PATCH +
                                          " (" + KVS_VERSION_STATUS + ")";
 
}
