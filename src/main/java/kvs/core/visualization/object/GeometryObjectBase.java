package kvs.core.visualization.object;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import kvs.core.matrix.Vector3f;

public abstract class GeometryObjectBase extends ObjectBase{

    private static final long serialVersionUID = -2735000428160206040L;

    public enum GeometryType
    {
        Point,   ///< point object
        Line,    ///< line object
        Polygon  ///< polygon object
    };

    protected FloatBuffer m_coords  = FloatBuffer.allocate( 0 );             ///< vertex array
    protected ByteBuffer m_colors   = ByteBuffer.allocate( 3 );  ///< color(r,g,b) array
    protected FloatBuffer m_normals = FloatBuffer.allocate( 0 );            ///< normal array

    public GeometryObjectBase(){
    }

    public GeometryObjectBase( FloatBuffer coords, ByteBuffer colors, FloatBuffer normals ){
        this.setCoords( coords );
        this.setColors( colors );
        this.setNormals( normals );
    }
    
    public GeometryObjectBase( float[] coords, byte[] colors, float[] normals ){
        this.setCoords( coords );
        this.setColors( colors );
        this.setNormals( normals );
    }
    
    public GeometryObjectBase( float[] coords, int[] colors, float[] normals ){
        this.setCoords( coords );
        this.setColors( colors );
        this.setNormals( normals );
    }

    public GeometryObjectBase( FloatBuffer coords, Color colors, FloatBuffer normals ){
        this.setCoords( coords );
        this.setColor( colors );
        this.setNormals( normals );
    }
    
    public GeometryObjectBase( float[] coords, Color colors, float[] normals ){
        this.setCoords( coords );
        this.setColor( colors );
        this.setNormals( normals );
    }

    public GeometryObjectBase( FloatBuffer coords, FloatBuffer normals ){
        this.setCoords( coords );
        this.setNormals( normals );
    }
    
    public GeometryObjectBase( float[] coords, float[] normals ){
        this.setCoords( coords );
        this.setNormals( normals );
    }

    public GeometryObjectBase( FloatBuffer coords, ByteBuffer colors){
        this.setCoords( coords );
        this.setColors( colors );
    }
    
    public GeometryObjectBase( float[] coords, byte[] colors){
        this.setCoords( coords );
        this.setColors( colors );
    }
    
    public GeometryObjectBase( float[] coords, int[] colors){
        this.setCoords( coords );
        this.setColors( colors );
    }

    public GeometryObjectBase( FloatBuffer coords, Color colors){
        this.setCoords( coords );
        this.setColor( colors );

    }
    
    public GeometryObjectBase( float[] coords, Color colors){
        this.setCoords( coords );
        this.setColor( colors );

    }

    public GeometryObjectBase( FloatBuffer coords ){
        this.setCoords( coords );
        this.setColor( Color.WHITE );
    }
    
    public GeometryObjectBase( float[] coords ){
        this.setCoords( coords );
        this.setColor( Color.WHITE );
    }

    public void setCoords( FloatBuffer coords )
    {
        m_coords = coords;
    }
    
    public void setCoords( float[] coords )
    {
        setCoords( FloatBuffer.wrap( coords ) );
    }

    public void setColors( ByteBuffer colors )
    {
        m_colors = colors;
    }
    
    public void setColors( byte[] colors )
    {
        setColors( ByteBuffer.wrap( colors ) );
    }
    
    public void setColors( int[] colors )
    {
        setColors( IntBuffer.wrap( colors ) );
    }
    
    public void setColors( IntBuffer colors )
    {
        int limit = colors.limit();
        m_colors = ByteBuffer.allocate( limit );
        
        for(int i = 0; i < limit; i++ ){
            m_colors.put( i, (byte)colors.get( i ) );
        }
    }

    public void setColor( Color color )
    {
        m_colors.put( 0, (byte)color.getRed() );
        m_colors.put( 1, (byte)color.getGreen() );
        m_colors.put( 2, (byte)color.getBlue() );
    }

    public void setNormals( FloatBuffer normals )
    {
        m_normals = normals;
    }
    
    public void setNormals( float[] normals )
    {
        setNormals( FloatBuffer.wrap( normals ) );
    }

    @Override
    public ObjectType objectType(){
        return ObjectType.Geometry;
    }
    
    public abstract GeometryType geometryType();

    public int nvertices()
    {
        final int dimension = 3;
        return( m_coords.limit() / dimension );
    }

    public int ncolors()
    {
        final int nchannels = 3;
        return( m_colors.limit() / nchannels );
    }

    public int nnormals()
    {
        final int dimension = 3;
        return( m_normals.limit() / dimension );
    }

    public Vector3f coord( int index )
    {
        final int dimension = 3;
        return( new Vector3f( m_coords.array(), dimension * index ) );
    }

    public Vector3f coord()
    {
        return this.coord( 0 );
    }

    public Color color( int index )
    {
        final int nchannels = 3;
        index *= nchannels;
        return( new Color( m_colors.get( index )     & 0xFF, 
                           m_colors.get( index + 1 ) & 0xFF, 
                           m_colors.get( index + 2 ) & 0xFF ) );
    }

    public Color color()
    {
        return this.color( 0 );
    }

    public Vector3f normal( int index )
    {
        final int dimension = 3;
        return( new Vector3f( m_normals.array(), dimension * index ) );
    }

    public Vector3f normal()
    {
        return this.normal( 0 );
    }

    public FloatBuffer coords()
    {
        return m_coords;
    }

    public ByteBuffer colors()
    {
        return m_colors;
    }

    public FloatBuffer normals()
    {
        return m_normals ;
    }

}
