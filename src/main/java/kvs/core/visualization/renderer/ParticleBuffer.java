package kvs.core.visualization.renderer;

import kvs.core.visualization.object.PointObject;

public class ParticleBuffer {

    protected int   m_width;                      ///< width
    protected int   m_height;                     ///< height
    protected int   m_size;                       ///< pixel data size [byte]
    protected int   m_num_of_projected_particles; ///< total number of projected points
    protected int   m_num_of_stored_particles;    ///< total number of stored points
    protected int   m_subpixel_level;             ///< subpixel level
    boolean         m_enable_shading;             ///< shading flag
    protected int   m_extended_width;             ///< m_width * m_subpixel_level

    protected int[]     m_index_buffer; ///< index buffer
    protected float[]   m_depth_buffer; ///< depth buffer

    // Reference shader (NOTE: not allocated in thie class).
    protected ShadingType m_ref_shader;
    protected PointObject m_ref_point_object;


    public ParticleBuffer(){
        m_ref_shader = null;
        m_ref_point_object = null;
    }


    public ParticleBuffer(
                          final int width,
                          final int height,
                          final int subpixel_level )
    {
        this.create( width, height, subpixel_level );
    }

    public int width()
    {
        return( m_width );
    }

    public int height()
    {
        return( m_height );
    }

    public int[] indexBuffer()
    {
        return( m_index_buffer );
    }

    public int index( final int index )
    {
        return( m_index_buffer[index] );
    }

    public float[] depthBuffer()
    {
        return( m_depth_buffer );
    }

    public float depth( int index )
    {
        return( m_depth_buffer[index] );
    }

    public ShadingType shader()
    {
        return( m_ref_shader );
    }

    public PointObject pointObject()
    {
        return( m_ref_point_object );
    }

    public int numOfProjectedParticles()
    {
        return( m_num_of_projected_particles );
    }

    public int numOfStoredParticles()
    {
        return( m_num_of_stored_particles );
    }

    public void setSubpixelLevel( final int subpixel_level )
    {
        m_subpixel_level = subpixel_level;
    }

    public void attachShader( final ShadingType shader )
    {
        m_ref_shader = shader;
    }

    public void attachPointObject( final PointObject point_object )
    {
        m_ref_point_object = null;
        m_ref_point_object = point_object;
    }

    public void enableShading()
    {
        m_enable_shading = true;
    }

    public void disableShading()
    {
        m_enable_shading = false;
    }

    public void add(
            final float x,
            final float y,
            final float depth,
            final int voxel_index )
            {
        // Buffer coordinate value.
        final int bx = (int)( x * m_subpixel_level );
        final int by = (int)( y * m_subpixel_level );

        final int index = m_extended_width * by + bx;
        m_num_of_projected_particles++;

        if( m_depth_buffer[index] > 0.0f )
        {
            // Detect collision.
            if( m_depth_buffer[index] > depth )
            {
                m_depth_buffer[index] = depth;
                m_index_buffer[index] = voxel_index;
            }
        }
        else
        {
            m_depth_buffer[index] = depth;
            m_index_buffer[index] = voxel_index;
        }
            }

    public boolean create(
                          final int width,
                          final int height,
                          final int subpixel_level )
    {
        final int npixels = width * height;

        m_width                   = width;
        m_height                  = height;
        m_subpixel_level          = subpixel_level;
        m_size                    = npixels * 4;
        m_num_of_projected_particles = 0;
        m_num_of_stored_particles    = 0;
        m_enable_shading          = true;
        m_extended_width          = m_width * m_subpixel_level;

        if( m_width == 0 || m_height == 0 )
        {
            //kvsMessageError("Cannot create the pixel data for the particle buffer.");
            return( false );
        }

        final int subpixeled_npixels = npixels * subpixel_level * subpixel_level;
        m_index_buffer = new int[ subpixeled_npixels ];
        m_depth_buffer = new float[ subpixeled_npixels ];

        //m_index_buffer.fill( 0x00 );
        m_index_buffer = new int[m_index_buffer.length];
        //m_depth_buffer.fill( 0x00 );
        m_depth_buffer = new float[m_depth_buffer.length];

        return( true );
    }

    public void clean()
    {
      //m_index_buffer.fill( 0x00 );
        m_index_buffer = new int[m_index_buffer.length];
        //m_depth_buffer.fill( 0x00 );
        m_depth_buffer = new float[m_depth_buffer.length];

        m_num_of_projected_particles = 0;
        m_num_of_stored_particles    = 0;
    }

    public void createImage(
                            byte[]  color,
                            float[] depth )
    {
        if( m_enable_shading ){
            this.create_image_with_shading( color, depth );
        }
        else{
            this.create_image_without_shading( color, depth );
        }
    }

    protected void create_image_with_shading(
                                             byte[]  color,
                                             float[] depth )
    {
        final byte[]  point_color  = m_ref_point_object.colors().array();
        final float[] point_normal = m_ref_point_object.normals().array();

        final float inv_ssize = 1.0f / ( m_subpixel_level * m_subpixel_level );
        final float normalize_alpha = 255.0f * inv_ssize;

        int pindex   = 0;
        int pindex4  = 0;
        int by_start = 0;
        final int bw = m_extended_width;
        for( int py = 0; py < m_height; py++, by_start += m_subpixel_level )
        {
            int bx_start = 0;
            for( int px = 0; px < m_width; px++, pindex++, pindex4 += 4, bx_start += m_subpixel_level )
            {
                float R = 0.0f;
                float G = 0.0f;
                float B = 0.0f;
                float D = 0.0f;
                int npoints = 0;
                for( int by = by_start; by < by_start + m_subpixel_level; by++ )
                {
                    final int bindex_start = bw * by;
                    for( int bx = bx_start; bx < bx_start + m_subpixel_level; bx++ )
                    {
                        final int bindex = bindex_start + bx;
                        if( m_depth_buffer[bindex] > 0.0f )
                        {
                            final int point_index3 = 3 * m_index_buffer[ bindex ];

                            final float attenuate = m_ref_shader.attenuation( point_normal, point_index3 ); //FIXME lineobject
                            R += ( point_color[ check_value(point_index3, point_color.length) + 0 ] & 0xFF ) * attenuate;
                            G += ( point_color[ check_value(point_index3, point_color.length) + 1 ] & 0xFF ) * attenuate;
                            B += ( point_color[ check_value(point_index3, point_color.length) + 2 ] & 0xFF ) * attenuate;
                            D = kvs.core.util.Math.max( D, m_depth_buffer[ bindex ] );

                            npoints++;
                        }
                    }
                }

                R *= inv_ssize;
                G *= inv_ssize;
                B *= inv_ssize;

                color[ pindex4 + 0 ] = (byte)(R);
                color[ pindex4 + 1 ] = (byte)(G);
                color[ pindex4 + 2 ] = (byte)(B);
                color[ pindex4 + 3 ] = (byte)( npoints * normalize_alpha );
                depth[ pindex ]      = ( npoints == 0 ) ? 1.0f : D;
            }
        }
    }

    protected void create_image_without_shading(
                                                byte[]  color,
                                                float[] depth )
    {
        final byte[] point_color = m_ref_point_object.colors().array();

        final float inv_ssize = 1.0f / ( m_subpixel_level * m_subpixel_level );
        final float normalize_alpha = 255.0f * inv_ssize;

        int pindex   = 0;
        int pindex4  = 0;
        int by_start = 0;
        final int bw = m_extended_width;
        for( int py = 0; py < m_height; py++, by_start += m_subpixel_level )
        {
            int bx_start = 0;
            for( int px = 0; px < m_width; px++, pindex++, pindex4 += 4, bx_start += m_subpixel_level )
            {
                float R = 0.0f;
                float G = 0.0f;
                float B = 0.0f;
                float D = 0.0f;
                int npoints = 0;
                for( int by = by_start; by < by_start + m_subpixel_level; by++ )
                {
                    final int bindex_start = bw * by;
                    for( int bx = bx_start; bx < bx_start + m_subpixel_level; bx++ )
                    {
                        final int bindex = bindex_start + bx;
                        if( m_depth_buffer[bindex] > 0.0f )
                        {
                            final int point_index3 = 3 * m_index_buffer[ bindex ];

                            R += point_color[ check_value(point_index3, point_color.length) + 0 ] & 0xFF;
                            G += point_color[ check_value(point_index3, point_color.length) + 1 ] & 0xFF;
                            B += point_color[ check_value(point_index3, point_color.length) + 2 ] & 0xFF;
                            D = kvs.core.util.Math.max( D, m_depth_buffer[ bindex ] );
                            npoints++;
                        }
                    }
                }

                R *= inv_ssize;
                G *= inv_ssize;
                B *= inv_ssize;

                color[ pindex4 + 0 ] = (byte)(R);
                color[ pindex4 + 1 ] = (byte)(G);
                color[ pindex4 + 2 ] = (byte)(B);
                color[ pindex4 + 3 ] = (byte)( npoints * normalize_alpha );
                depth[ pindex ]      = ( npoints == 0 ) ? 1.0f : D;
            }
        }
    }
    
    private int check_value( int value, int limit ){   //FIXME 不要？
        return value < limit ? value : 0;
    }

}
