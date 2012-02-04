package kvs.core.visualization.renderer;

import kvs.core.matrix.Vector3f;
import kvs.core.visualization.viewer.Camera;
import kvs.core.visualization.viewer.Light;

public abstract class Shader implements ShadingType {
    protected Vector3f C; ///< direction of the camera
    protected Vector3f L; ///< direction of the light

    @Override
    public abstract void set( final Camera camera, final Light light );

    @Override
    public abstract float attenuation( final float[] gradient, final int offset );
    
    public static class Lambert extends Shader {

        private float Ka; ///< ambient coefficient
        private float Kd; ///< diffuse coefficient

        public Lambert(){
            Ka = 0.5f;
            Kd = 0.5f;
        }

        public Lambert( final Lambert shader ){
            C  = shader.C;
            L  = shader.L;
            Ka = shader.Ka;
            Kd = shader.Kd;
        }

        public Lambert( final float ka, final float kd ){
            Ka = ka;
            Kd = kd;
        }

        @Override
        public void set( Camera camera, Light light ) {
            //kvs::IgnoreUnusedVariable( camera );

            L = light.getPosition().normalize().opposite();
        }
        
        @Override
        public float attenuation( final float[] gradient, final int offset ) {
         // Normal vector.
            final Vector3f N = new Vector3f( gradient, offset ).normalize();

            //const float dd = kvs::Math::Max( N.dot( L ), 0.0 );
            final float dd = kvs.core.util.Math.abs( N.dot( L ) );

            /* I = Ia + Id
             *
             * Ia = Ka (constant term)
             * Id = Ip *  Kd * cos(A) = Ip * Kd * ( L dot N )
             *
             * Ip : the intensity emitted from the light source.
             */
            final float Ia = Ka;
            final float Id = Kd * dd;

            return( kvs.core.util.Math.min( Ia + Id, 1.0f ) );
        }

    }
    
    public static class Phong extends Shader {

        private float Ka; ///< ambient coefficient
        private float Kd; ///< diffuse coefficient
        private float Ks; ///< specular coefficient
        private float S;  ///< shininess

        public Phong(){
            Ka = 0.2f;
            Kd = 0.5f;
            Ks = 0.3f;
            S = 20.0f;
        }

        public Phong( final Phong shader ){
            C  = shader.C;
            L  = shader.L;
            Ka = shader.Ka;
            Kd = shader.Kd;
            Ks = shader.Ks;
            S  = shader.S;
        }

        public Phong( final float ka, final float kd, final float ks, final float s ){
            Ka = ka;
            Kd = kd;
            Ks = ks;
            S  = s;
        }
        
        @Override
        public void set( Camera camera, Light light ) {
            //kvs::IgnoreUnusedVariable( camera );

            L = light.getPosition().normalize().opposite();
        }

        @Override
        public float attenuation( final float[] gradient, final int offset ) {
         // Normal vector N and reflection vector R.
            final Vector3f N = new Vector3f( gradient, offset ).normalize();
            final Vector3f R = N.mul(2.0f * N.dot( L )).sub( L );

            //const float dd = Math::Max( N.dot( L ), 0.0f );
            //const float ds = Math::Max( N.dot( R ), 0.0f );
            final float dd = kvs.core.util.Math.abs( N.dot( L ) );
            final float ds = kvs.core.util.Math.abs( N.dot( R ) );

            /* I = Ia + Id + Is
             *
             * Is = Ip * Ks * cos^s(B) = Ip * Ks * ( R dot N )^s
             */
            final float Ia = Ka;
            final float Id = Kd * dd;
            final float Is = (float) (Ks * java.lang.Math.pow( ds, S ));

            return( kvs.core.util.Math.min( Ia + Id + Is, 1.0f ) );
        }

    }
    
    public static class BlinnPhong extends Shader {

        private Vector3f H;  ///< halfway vector
        private float         Ka; ///< ambient coefficient
        private float         Kd; ///< diffuse coefficient
        private float         Ks; ///< specular coefficient
        private float         S;  ///< shininess

        public BlinnPhong(){
            Ka = 0.2f;
            Kd = 0.5f;
            Ks = 0.3f;
            S = 20.0f;
        }

        public BlinnPhong( final BlinnPhong shader ){
            C  = shader.C;
            L  = shader.L;
            H  = shader.H;
            Ka = shader.Ka;
            Kd = shader.Kd;
            Ks = shader.Ks;
            S  = shader.S;
        }
        
        public BlinnPhong( final Camera camera, final Light light ){
            this.set( camera, light );  
        }

        @Override
        public void set( Camera camera, Light light ) {
            C = camera.getPosition().normalize().opposite();
            L = light.getPosition().normalize().opposite();
            H = ( C.add( L ) ).normalize();
        }

        @Override
        public float attenuation( float[] gradient , int offset ) {
         // Normal vector.
            final Vector3f N = new Vector3f( gradient, offset ).normalize();

            //const float dd = kvs::Math::Max( N.dot( L ), 0.0 );
            //const float ds = kvs::Math::Max( N.dot( H ), 0.0 );
            final float dd = kvs.core.util.Math.abs( N.dot( L ) );
            final float ds = kvs.core.util.Math.abs( N.dot( H ) );

            /* I = Ia + Id + Is
             *
             * Is = Ip * Ks * cos^s(B) = Ip * Ks * ( H dot N )^s
             */
            final float Ia = Ka;
            final float Id = Kd * dd;
            final float Is = (float) (Ks * java.lang.Math.pow( ds, S ));

            return( kvs.core.util.Math.min( Ia + Id + Is, 1.0f ) );
        }

    }
}
