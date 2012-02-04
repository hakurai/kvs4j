package kvs.core.visualization.object;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class PointObject extends GeometryObjectBase {

    private static final long serialVersionUID = 9063552201244571336L;
    protected FloatBuffer m_sizes;

    public PointObject() {
        this.setSize( 1.0f );
    }

    public PointObject( FloatBuffer coords, ByteBuffer colors, FloatBuffer normals, FloatBuffer sizes ) {
        super( coords, colors, normals );

        this.setSizes( sizes );
    }
    
    public PointObject( float[] coords, byte[] colors, float[] normals, float[] sizes ) {
        super( coords, colors, normals );

        this.setSizes( sizes );
    }

    public PointObject( FloatBuffer coords, ByteBuffer colors, FloatBuffer normals, float size ) {
        super( coords, colors, normals );

        this.setSize( size );
    }
    
    public PointObject( float[] coords, byte[] colors, float[] normals, float size ) {
        super( coords, colors, normals );

        this.setSize( size );
    }

    public PointObject( FloatBuffer coords, Color color, FloatBuffer normals, FloatBuffer sizes ) {
        super( coords, color, normals );

        this.setSizes( sizes );
    }
    
    public PointObject( float[] coords, Color color, float[] normals, float[] sizes ) {
        super( coords, color, normals );

        this.setSizes( sizes );
    }

    public PointObject( FloatBuffer coords, FloatBuffer normals, FloatBuffer sizes ) {
        super( coords, normals );

        this.setSizes( sizes );
    }
    
    public PointObject( float[] coords, float[] normals, float[] sizes ) {
        super( coords, normals );

        this.setSizes( sizes );
    }

    public PointObject( FloatBuffer coords, Color color, FloatBuffer normals, float size ) {
        super( coords, color, normals );

        this.setSize( size );
    }
    
    public PointObject( float[] coords, Color color, float[] normals, float size ) {
        super( coords, color, normals );

        this.setSize( size );
    }

    public PointObject( FloatBuffer coords, ByteBuffer colors, FloatBuffer sizes ) {
        super( coords, colors );

        this.setSizes( sizes );
    }
    
    public PointObject( float[] coords, byte[] colors, float[] sizes ) {
        super( coords, colors );

        this.setSizes( sizes );
    }

    public PointObject( FloatBuffer coords, Color color, FloatBuffer sizes ) {
        // KVS_ASSERT( coords.size() == sizes.size() * 3 );

        super( coords, color );

        this.setSizes( sizes );
    }
    
    public PointObject( float[] coords, Color color, float[] sizes ) {
        // KVS_ASSERT( coords.size() == sizes.size() * 3 );

        super( coords, color );

        this.setSizes( sizes );
    }

    public PointObject( FloatBuffer coords, ByteBuffer colors, float size ) {
        super( coords, colors );

        this.setSize( size );
    }
    
    public PointObject( float[] coords, byte[] colors, float size ) {
        super( coords, colors );

        this.setSize( size );
    }

    public PointObject( FloatBuffer coords, Color color, float size ) {
        super( coords, color );

        this.setSize( size );
    }
    
    public PointObject( float[] coords, Color color, float size ) {
        super( coords, color );

        this.setSize( size );
    }

    public PointObject( FloatBuffer coords ) {
        super( coords );

        this.setSize( 1.0f );
    }
    
    public PointObject( float[] coords ) {
        super( coords );

        this.setSize( 1.0f );
    }

    public PointObject( PolygonObject polygon ) {
        this.setCoords( polygon.coords() );

        if (polygon.colorType() == PolygonObject.ColorType.VertexColor) {
            this.setColors( polygon.colors() );
        } else {
            this.setColor( polygon.color() );
        }

        if (polygon.normalType() == PolygonObject.NormalType.VertexNormal) {
            this.setNormals( polygon.normals() );
        }

        this.setSize( 1.0f );

        this.setMinMaxObjectCoords( polygon.minObjectCoord(), polygon.maxObjectCoord() );

        this.setMinMaxExternalCoords( polygon.minExternalCoord(), polygon.maxExternalCoord() );
    }

    public void setSizes( FloatBuffer sizes ) {
        m_sizes = sizes;
    }
    
    public void setSizes( float[] sizes ) {
        setSizes( FloatBuffer.wrap( sizes ) );
    }

    public void setSize( float size ) {
        m_sizes = FloatBuffer.allocate( 1 );
        m_sizes.put( 0, size );
    }

    @Override
    public GeometryType geometryType() {
        return (GeometryType.Point);
    }

    public int nsizes() {
        return m_sizes.limit();
    }

    public float size( int index ) {
        return m_sizes.get( index );
    }
    
    public float size() {
        return size( 0 );
    }

    public FloatBuffer sizes() {
        return m_sizes;
    }
    
    public static PointObject valueOf( ObjectBase object ){
        if( object instanceof PointObject ){
            return (PointObject)object;
        } else if( object instanceof LineObject ){
            return valueOf( (LineObject)object );
        } else if( object instanceof PolygonObject ){
            return valueOf( (PolygonObject)object );
        }
        return null;
    }
    
    public static PointObject valueOf( LineObject line ){
        PointObject point = new PointObject();
        point.setCoords( line.coords() );

        if (line.colorType() == LineObject.ColorType.VertexColor) {
            point.setColors( line.colors() );
        } else {
            point.setColor( line.color() );
        }

        point.setSize( line.size() );

        point.setMinMaxObjectCoords( line.minObjectCoord(), line.maxObjectCoord() );

        point.setMinMaxExternalCoords( line.minExternalCoord(), line.maxExternalCoord() );
        
        return point;
    }
    
    public static PointObject valueOf( PolygonObject polygon ){
        PointObject point = new PointObject( polygon );

        return point;
    }

}
