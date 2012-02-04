package kvs.core.visualization.object;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class PolygonObject extends GeometryObjectBase {

    private static final long serialVersionUID = 9104053639754315428L;

    public enum PolygonType {
        Triangle(3), Quadrangle(4), Tri(3), Quad(4), UnknownPolygonType(-1);

        private int m_value;

        private PolygonType( int value ) {
            this.m_value = value;
        }

        public final int intValue() {
            return this.m_value;
        }

    };

    public enum ColorType {
        VertexColor, PolygonColor, UnknownColorType
    };

    public enum NormalType {
        VertexNormal, PolygonNormal, UnknownNormalType
    };

    protected PolygonType m_polygon_type; // /< polygon type
    protected ColorType m_color_type; // /< polygon color type
    protected NormalType m_normal_type; // /< polygon normal type
    protected IntBuffer m_connections = IntBuffer.allocate( 0 ); // /< connection array
    protected ByteBuffer m_opacities = ByteBuffer.allocate( 0 ); // /< opacity array

    public PolygonObject() {

    }

    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param connections 頂点の接続情報
     * @param colors 頂点色
     * @param opacities 頂点の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param color_type 頂点色の割り当て方法
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            FloatBuffer coords,
            IntBuffer connections,
            ByteBuffer colors,
            ByteBuffer opacities,
            FloatBuffer normals,
            PolygonType polygon_type,
            ColorType color_type,
            NormalType normal_type ) {
        super( coords, colors, normals );
        this.setPolygonType( polygon_type );
        this.setColorType( color_type );
        this.setNormalType( normal_type );
        this.setConnections( connections );
        this.setOpacities( opacities );
    }
    
    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param connections 頂点の接続情報
     * @param colors 頂点色
     * @param opacities 頂点の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param color_type 頂点色の割り当て方法
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            float[] coords,
            int[] connections,
            byte[] colors,
            byte[] opacities,
            float[] normals,
            PolygonType polygon_type,
            ColorType color_type,
            NormalType normal_type ) {
        super( coords, colors, normals );
        this.setPolygonType( polygon_type );
        this.setColorType( color_type );
        this.setNormalType( normal_type );
        this.setConnections( connections );
        this.setOpacities( opacities );
    }

    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param connections 頂点の接続情報
     * @param colors 頂点色
     * @param opacity 全体の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param color_type 頂点色の割り当て方法
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            FloatBuffer coords,
            IntBuffer connections,
            ByteBuffer colors,
            byte opacity,
            FloatBuffer normals,
            PolygonType polygon_type,
            ColorType color_type,
            NormalType normal_type ) {
        super( coords, colors, normals );
        this.setPolygonType( polygon_type );
        this.setColorType( color_type );
        this.setNormalType( normal_type );
        this.setConnections( connections );
        this.setOpacity( opacity );
    }
    
    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param connections 頂点の接続情報
     * @param colors 頂点色
     * @param opacity 全体の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param color_type 頂点色の割り当て方法
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            float[] coords,
            int[] connections,
            byte[] colors,
            byte opacity,
            float[] normals,
            PolygonType polygon_type,
            ColorType color_type,
            NormalType normal_type ) {
        super( coords, colors, normals );
        this.setPolygonType( polygon_type );
        this.setColorType( color_type );
        this.setNormalType( normal_type );
        this.setConnections( connections );
        this.setOpacity( opacity );
    }

    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param connections 頂点の接続情報
     * @param color 全体の色
     * @param opacities 頂点の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            FloatBuffer coords,
            IntBuffer connections,
            Color color,
            ByteBuffer opacities,
            FloatBuffer normals,
            PolygonType polygon_type,
            NormalType normal_type ) {
        super( coords, color, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( ColorType.PolygonColor );
        this.setNormalType( normal_type );
        this.setConnections( connections );
        this.setOpacities( opacities );
    }
    
    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param connections 頂点の接続情報
     * @param color 全体の色
     * @param opacities 頂点の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            float[] coords,
            int[] connections,
            Color color,
            byte[] opacities,
            float[] normals,
            PolygonType polygon_type,
            NormalType normal_type ) {
        super( coords, color, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( ColorType.PolygonColor );
        this.setNormalType( normal_type );
        this.setConnections( connections );
        this.setOpacities( opacities );
    }

    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param connections 頂点の接続情報
     * @param color 全体の色
     * @param opacity 全体の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            FloatBuffer coords,
            IntBuffer connections,
            Color color,
            byte opacity,
            FloatBuffer normals,
            PolygonType polygon_type,
            NormalType normal_type ) {
        super( coords, color, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( ColorType.PolygonColor );
        this.setNormalType( normal_type );
        this.setConnections( connections );
        this.setOpacity( opacity );
    }
    
    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param connections 頂点の接続情報
     * @param color 全体の色
     * @param opacity 全体の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            float[] coords,
            int[] connections,
            Color color,
            byte opacity,
            float[] normals,
            PolygonType polygon_type,
            NormalType normal_type ) {
        super( coords, color, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( ColorType.PolygonColor );
        this.setNormalType( normal_type );
        this.setConnections( connections );
        this.setOpacity( opacity );
    }

    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param colors 頂点色
     * @param opacities 頂点の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param color_type 頂点色の割り当て方法
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            FloatBuffer coords,
            ByteBuffer colors,
            ByteBuffer opacities,
            FloatBuffer normals,
            PolygonType polygon_type,
            ColorType color_type,
            NormalType normal_type ) {
        super( coords, colors, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( color_type );
        this.setNormalType( normal_type );
        this.setOpacities( opacities );
    }
    
    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param colors 頂点色
     * @param opacities 頂点の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param color_type 頂点色の割り当て方法
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            float[] coords,
            byte[] colors,
            byte[] opacities,
            float[] normals,
            PolygonType polygon_type,
            ColorType color_type,
            NormalType normal_type ) {
        super( coords, colors, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( color_type );
        this.setNormalType( normal_type );
        this.setOpacities( opacities );
    }

    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param colors 頂点色
     * @param opacity 全体の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param color_type 頂点色の割り当て方法
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            FloatBuffer coords,
            ByteBuffer colors,
            byte opacity,
            FloatBuffer normals,
            PolygonType polygon_type,
            ColorType color_type,
            NormalType normal_type ) {
        super( coords, colors, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( color_type );
        this.setNormalType( normal_type );
        this.setOpacity( opacity );
    }
    
    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param colors 頂点色
     * @param opacity 全体の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param color_type 頂点色の割り当て方法
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            float[] coords,
            byte[] colors,
            byte opacity,
            float[] normals,
            PolygonType polygon_type,
            ColorType color_type,
            NormalType normal_type ) {
        super( coords, colors, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( color_type );
        this.setNormalType( normal_type );
        this.setOpacity( opacity );
    }

    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param color 全体の色
     * @param opacities 頂点の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            FloatBuffer coords,
            Color color,
            ByteBuffer opacities,
            FloatBuffer normals,
            PolygonType polygon_type,
            NormalType normal_type ) {
        super( coords, color, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( ColorType.PolygonColor );
        this.setNormalType( normal_type );
        this.setOpacities( opacities );
    }
    
    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param color 全体の色
     * @param opacities 頂点の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            float[] coords,
            Color color,
            byte[] opacities,
            float[] normals,
            PolygonType polygon_type,
            NormalType normal_type ) {
        super( coords, color, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( ColorType.PolygonColor );
        this.setNormalType( normal_type );
        this.setOpacities( opacities );
    }

    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param color 全体の色
     * @param opacity 全体の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            FloatBuffer coords,
            Color color,
            byte opacity,
            FloatBuffer normals,
            PolygonType polygon_type,
            NormalType normal_type ) {
        super( coords, color, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( ColorType.PolygonColor );
        this.setNormalType( normal_type );
        this.setOpacity( opacity );
    }
    
    /**
     * 新しいポリゴンを構築します。
     * 
     * @param coords 頂点位置
     * @param color 全体の色
     * @param opacity 全体の不透明度
     * @param normals 法線ベクトル
     * @param polygon_type ポリゴン形状
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public PolygonObject(
            float[] coords,
            Color color,
            byte opacity,
            float[] normals,
            PolygonType polygon_type,
            NormalType normal_type ) {
        super( coords, color, normals );

        this.setPolygonType( polygon_type );
        this.setColorType( ColorType.PolygonColor );
        this.setNormalType( normal_type );
        this.setOpacity( opacity );
    }

    /**
     * ポリゴン形状を設定します。
     * 
     * @param polygon_type ポリゴン形状
     */
    public void setPolygonType( PolygonType polygon_type ) {
        m_polygon_type = polygon_type;
    }

    /**
     * 頂点色の割り当て方法を設定します。
     * 
     * @param color_type 頂点色の割り当て方法
     */
    public void setColorType( ColorType color_type ) {
        m_color_type = color_type;
    }

    /**
     * 法線ベクトルの割り当て方法を設定します。
     * 
     * @param normal_type 法線ベクトルの割り当て方法
     */
    public void setNormalType( NormalType normal_type ) {
        m_normal_type = normal_type;
    }

    public void setConnections( IntBuffer connections ) {
        m_connections = connections;
    }
    
    public void setConnections( int[] connections ) {
        setConnections( IntBuffer.wrap( connections ) );
    }

    @Override
    public void setColor( Color color ) {
        super.setColor( color );

        m_color_type = ColorType.PolygonColor;
    }

    public void setOpacities( ByteBuffer opacities ) {
        m_opacities = opacities;
    }
    
    public void setOpacities( byte[] opacities ) {
        setOpacities( ByteBuffer.wrap( opacities ) );
    }

    public void setOpacity( byte opacity ) {
        m_opacities = ByteBuffer.allocate( 1 );
        m_opacities.put( 0, opacity );
    }

    public GeometryType geometryType() {
        return (GeometryType.Polygon);
    }

    public PolygonType polygonType() {
        return (m_polygon_type);
    }

    public ColorType colorType() {
        return (m_color_type);
    }

    public NormalType normalType() {
        return (m_normal_type);
    }

    public int nconnections() {
        int nvertices_per_face = m_polygon_type.intValue();
        return (m_connections.limit() / nvertices_per_face);
    }

    public int nopacities() {
        return (m_opacities.limit());
    }

    public byte opacity( int index ) {
        return m_opacities.get( index );
    }
    
    public byte opacity() {
        return this.opacity( 0 );
    }

    public IntBuffer connections() {
        return m_connections;
    }

    public ByteBuffer opacities() {
        return m_opacities;
    }

}
