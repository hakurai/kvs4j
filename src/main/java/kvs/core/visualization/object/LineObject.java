package kvs.core.visualization.object;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import kvs.core.matrix.Vector2i;

public class LineObject extends GeometryObjectBase {

    private static final long serialVersionUID = 1718452582586965279L;

    public enum LineType {
        Strip, Uniline, Polyline, Segment, UnknownLineType;
    };

    public enum ColorType {
        VertexColor, LineColor, UnknownColorType;
    };

    protected LineType m_line_type; // /< line type
    protected ColorType m_color_type; // /< line color type
    protected IntBuffer m_connections; // /< connection array
    protected FloatBuffer m_sizes; // /< size array

    public LineObject() {
    }

    public LineObject(
            FloatBuffer coords,
            IntBuffer connections,
            ByteBuffer colors,
            FloatBuffer sizes,
            LineType line_type,
            ColorType color_type ) {
        super( coords, colors );
        this.setLineType( line_type );
        this.setColorType( color_type );
        this.setConnections( connections );
        this.setSizes( sizes );
    }

    public LineObject(
            FloatBuffer coords,
            IntBuffer connections,
            ByteBuffer colors,
            float size,
            LineType line_type,
            ColorType color_type ) {
        super( coords, colors );
        this.setLineType( line_type );
        this.setColorType( color_type );
        this.setConnections( connections );
        this.setSize( size );
    }

    public LineObject(
            FloatBuffer coords,
            IntBuffer connections,
            Color color,
            FloatBuffer sizes,
            LineType line_type ) {
        super( coords, color );
        this.setLineType( line_type );
        this.setColorType( ColorType.LineColor );
        this.setConnections( connections );
        this.setSizes( sizes );
    }

    public LineObject(
            FloatBuffer coords,
            IntBuffer connections,
            Color color,
            float size,
            LineType line_type ) {
        super( coords, color );

        this.setLineType( line_type );
        this.setColorType( ColorType.LineColor );
        this.setConnections( connections );
        this.setSize( size );
    }

    public LineObject( FloatBuffer coords, ByteBuffer colors, FloatBuffer sizes, ColorType color_type ) {
        super( coords, colors );

        this.setLineType( LineType.Strip );
        this.setColorType( color_type );
        this.setSizes( sizes );
    }

    public LineObject( FloatBuffer coords, ByteBuffer colors, float size, ColorType color_type ) {
        super( coords, colors );

        this.setLineType( LineType.Strip );
        this.setColorType( color_type );
        this.setSize( size );
    }

    public LineObject( FloatBuffer coords, Color color, FloatBuffer sizes ) {
        super( coords, color );

        this.setLineType( LineType.Strip );
        this.setColorType( ColorType.LineColor );
        this.setSizes( sizes );
    }

    public LineObject( FloatBuffer coords, Color color, float size ) {
        super( coords, color );

        this.setLineType( LineType.Strip );
        this.setColorType( ColorType.LineColor );
        this.setSize( size );
    }

    public LineObject( FloatBuffer coords ) {
        super( coords );

        this.setLineType( LineType.Strip );
        this.setColorType( ColorType.LineColor );
        this.setSize( 1.0f );
    }


    public void setLineType( LineType line_type ) {
        m_line_type = line_type;
    }

    public void setColorType( ColorType color_type ) {
        m_color_type = color_type;
    }

    public void setConnections( IntBuffer connections ) {
        m_connections = connections;
    }

    @Override
    public void setColor( Color color ) {
        super.setColor( color );

        m_color_type = ColorType.LineColor;
    }

    public void setSizes( FloatBuffer sizes ) {
        m_sizes = sizes;
    }

    public void setSize( float size ) {
        m_sizes = FloatBuffer.allocate( 1 );
        m_sizes.put( 0, size );
    }

    public GeometryType geometryType() {
        return (GeometryType.Line);
    }

    public LineType lineType() {
        return (m_line_type);
    }

    public ColorType colorType() {
        return (m_color_type);
    }

    public int nconnections() {
        return (m_line_type == LineType.Uniline ? m_connections.limit() : m_connections.limit() / 2);
    }

    public int nsizes() {
        return m_sizes.limit();
    }

    public Vector2i connection( int index ) {
        return (new Vector2i( m_connections.array(), 2 * index ));
    }

    public Vector2i connection() {
        return this.connection( 0 );
    }

    public float size( int index ) {
        return m_sizes.get( index );
    }

    public float size() {
        return this.size( 0 );
    }

    public IntBuffer connections() {
        return m_connections;
    }

    public FloatBuffer sizes() {
        return m_sizes;
    }
    
    public static LineObject valueOf( ObjectBase object ){
        if( object instanceof LineObject ){
            return (LineObject)object;
        } else if( object instanceof PolygonObject ){
            return valueOf( (PolygonObject)object );
        }
        
        return null;
        
    }
    
    
    public static LineObject valueOf( PolygonObject polygon ){
        LineObject line = new LineObject();
        line.setCoords( polygon.coords() );

        if (polygon.colorType() == PolygonObject.ColorType.VertexColor) {
            line.setColorType( ColorType.VertexColor );
            line.setColors( polygon.colors() );
        } else {
            line.setColorType( ColorType.LineColor );
            line.setColor( polygon.color() );
        }

        line.setSize( 1.0f );

        line.setLineType( LineType.Segment );

        final int nconnections = polygon.nconnections();
        final int ncorners = polygon.polygonType().intValue();
        final int npolygons = (nconnections == 0) ? polygon.nvertices() / ncorners : nconnections;

        IntBuffer connections = IntBuffer.allocate( npolygons * ncorners * 2 );
        int p_index = 0;
        int l_index = 0;
        for (int i = 0; i < npolygons; i++) {
            for (int j = 0; j < ncorners; j++) {
                connections.put( l_index++, p_index++ );
            }
            connections.put( l_index++, p_index - ncorners );
        }
        connections.clear();

        line.setConnections( connections );

        line.setMinMaxObjectCoords( polygon.minObjectCoord(), polygon.maxObjectCoord() );

        line.setMinMaxExternalCoords( polygon.minExternalCoord(), polygon.maxExternalCoord() );
        
        return line;
    }

}
