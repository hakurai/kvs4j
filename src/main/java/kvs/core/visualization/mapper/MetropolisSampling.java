package kvs.core.visualization.mapper;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import kvs.core.KVSException;
import kvs.core.matrix.Vector3f;
import kvs.core.matrix.Vector3i;
import kvs.core.visualization.filter.TrilinearInterpolator;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.PointObject;
import kvs.core.visualization.object.StructuredVolumeObject;
import kvs.core.visualization.object.UnstructuredVolumeObject;
import kvs.core.visualization.object.VolumeObjectBase;
import kvs.core.visualization.object.ObjectBase.ObjectType;
import kvs.core.visualization.object.VolumeObjectBase.VolumeType;

public class MetropolisSampling extends MapperBase{

    private static final long serialVersionUID = 7809365247131354699L;

    protected int m_nparticles; ///< number of generated particles

    PointObject m_object = new PointObject();

    public MetropolisSampling(){
        super();
    }

    public MetropolisSampling( final int nparticles ){
        super();
        m_nparticles = nparticles;
    }

    public MetropolisSampling( final int              nparticles,
                               final TransferFunction transfer_function ){
        super( transfer_function );
        m_nparticles = nparticles;
    }

    public int nparticles(){
        return m_nparticles;
    }

    public void setNParticles( final int nparticles ){
        m_nparticles = nparticles;
    }

    public ObjectBase exec( ObjectBase object ) throws KVSException {
        final ObjectType object_type = object.objectType();
        if ( object_type == ObjectType.Geometry )
        {
            throw new KVSException("Geometry object is not supported.");
        }

        final VolumeObjectBase volume = (VolumeObjectBase)object;
        final VolumeType volume_type = volume.volumeType();
        if ( volume_type == VolumeType.Structured )
        {
            this.mapping( (StructuredVolumeObject)object );
        }
        else // volume_type == kvs::VolumeObjectBase::Unstructured
        {
            this.mapping( (UnstructuredVolumeObject)object );
        }

        return m_object;
    }

    private void mapping( final StructuredVolumeObject volume ) throws KVSException
    {
        // Attach the pointer to the volume object.
        this.attach_volume( volume );

        // Set the min/max coordinates.
        this.set_min_max_coords( volume, m_object );

        // Generate the particles.
        final Buffer type = volume.values();
        if (      type instanceof IntBuffer ){
            this.generate_particles( volume );
        } else {
            throw new KVSException("Unsupported data type '" +type.getClass().getName()+ "' of the structured volume." );
        }
    }

    private void mapping( final UnstructuredVolumeObject volume ) throws KVSException
    {
        //kvs::IgnoreUnusedVariable( volume );
        throw new KVSException("Not yet supported the metropolis method for the unstructured volume");
    }

    private void generate_particles( final StructuredVolumeObject volume  )
    {
        // Set the trilinear interpolator.
        TrilinearInterpolator interpolator = new TrilinearInterpolator( volume );

        // Alias.
        final Vector3i r = volume.resolution().sub( new Vector3i(1) );

        // Allocate memory for generated particles.
        m_object.setCoords( FloatBuffer.allocate( m_nparticles * 3 ) );
        m_object.setColors( ByteBuffer.allocate( m_nparticles * 3 ) );
        m_object.setNormals( FloatBuffer.allocate( m_nparticles * 3 ) );

        // Random number generator.
        Random R = new Random();

        // Set a initial particle and a trial particle.
        Vector3f particle = new Vector3f( R.nextFloat() * r.getX(), R.nextFloat() * r.getY(), R.nextFloat() * r.getZ() );
        Vector3f trial_particle = Vector3f.ZERO;

        // Attach the initial particle to the interpolator and get a rho value.
        interpolator.attachPoint( particle );
        int scalar = (int)interpolator.scalar();
        float  rho    =  this.opacityMap().getAt( scalar );
        float  trial_rho =  0.0f;

        // Point sampling process.
        int counter = 0;
        while( counter < m_nparticles )
        {
            trial_particle = new Vector3f( R.nextFloat() * r.getX(), R.nextFloat() * r.getY(), R.nextFloat() * r.getZ() );
            interpolator.attachPoint( trial_particle );

            scalar    = (int)interpolator.scalar();
            trial_rho = this.opacityMap().getAt( scalar );

            final float ratio = trial_rho / rho;
            if( ratio >= 1.0f )
            {
                // Adopt the particle.
                final Vector3f gradient = interpolator.gradient();
                this.adopt_particle( counter, trial_particle, scalar, gradient );

                // Update the particle.
                particle = trial_particle;
                rho      = trial_rho;

                counter++;
            }
            else
            {
                if( ratio >= R.nextFloat() )
                {
                    // Adopt the particle.
                    final Vector3f gradient = interpolator.gradient();
                    this.adopt_particle( counter, trial_particle, scalar, gradient );

                    // Update the particle.
                    particle = trial_particle;
                    rho      = trial_rho;

                    counter++;
                }
                else
                {
                    interpolator.attachPoint( particle );
                    scalar = (int)interpolator.scalar();
                    final Vector3f gradient = interpolator.gradient();
                    this.adopt_particle( counter, particle, scalar, gradient );

                    counter++;
                }
            }
        }

        m_object.setSize( 1.0f );
    }

    private void adopt_particle( final int      index,
                                 final Vector3f coord,
                                 final int      scalar,
                                 final Vector3f gradient )
    {
        final int index3 = index * 3;
        float[] coords = m_object.coords().array();
        byte[] colors = m_object.colors().array();
        float[] normals = m_object.normals().array();
        coords[ index3 + 0 ]  = coord.getX();
        coords[ index3 + 1 ]  = coord.getY();
        coords[ index3 + 2 ]  = coord.getZ();
        colors[ index3 + 0 ]  = (byte)this.colorMap().getAt( scalar ).getRed();
        colors[ index3 + 1 ]  = (byte)this.colorMap().getAt( scalar ).getGreen();
        colors[ index3 + 2 ]  = (byte)this.colorMap().getAt( scalar ).getBlue();
        normals[ index3 + 0 ] = gradient.getX();
        normals[ index3 + 1 ] = gradient.getY();
        normals[ index3 + 2 ] = gradient.getZ();
    }

}
