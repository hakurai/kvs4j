/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kvs.core.visualization.filter;

import java.awt.Color;
import java.nio.IntBuffer;
import java.util.ArrayList;
import kvs.core.matrix.Vector3f;
import kvs.core.util.Utility;
import kvs.core.visualization.object.LineObject;
import kvs.core.visualization.object.ObjectBase;

/**
 *
 * @author electra
 */
public class Bounds extends FilterBase{

    public enum Type{

        Box, ///< box type bounds
        Corner, ///< corner type bounds
        Circle; ///< circle type bounds
    };
    private LineObject m_object;
    protected Type m_type;         ///< bounds type
    protected float m_corner_scale; ///< length of corner line
    protected float m_division;     ///< division of circle

    public Bounds(){
        m_type = Type.Box;
        m_corner_scale = 0.2f;
        m_division = 50.0f;
    }

    public Bounds( Type type ){
        m_type = type;
        m_corner_scale = 0.2f;
        m_division = 50.0f;
    }

    @Override
    public ObjectBase exec( ObjectBase object ){
        m_object = new LineObject();
        if ( object != null ){
            m_object.setMinMaxObjectCoords(
                    object.minObjectCoord(),
                    object.maxObjectCoord() );

            m_object.setMinMaxExternalCoords(
                    object.minExternalCoord(),
                    object.maxExternalCoord() );
        }

        switch ( m_type ){
            case Box:
                this.create_box_bounds();
                break;
            case Corner:
                this.create_corner_bounds();
                break;
            case Circle:
                this.create_circle_bounds();
                break;
            default:
                break;
        }

        m_object.disableCollision();

        return m_object;
    }

    public void setType( final Type type ){
        m_type = type;
    }

    public void setCornerScale( final float corner_scale ){
        m_corner_scale = corner_scale;
    }

    public void setCircleDivision( final float division ){
        m_division = division;
    }

    public float getCornerScale(){
        return m_corner_scale;
    }

    public float getCircleDivision(){
        return m_division;
    }

    public void initialize(){
        m_type = Type.Box;
        m_corner_scale = 0.2f;
        m_division = 20.0f;
    }

    private void create_box_bounds(){
        ArrayList<Float> coords = new ArrayList<Float>();
        ArrayList<Integer> connects = new ArrayList<Integer>();

        final float min_x = m_object.minObjectCoord().getX();
        final float min_y = m_object.minObjectCoord().getY();
        final float min_z = m_object.minObjectCoord().getZ();
        final float max_x = m_object.maxObjectCoord().getX();
        final float max_y = m_object.maxObjectCoord().getY();
        final float max_z = m_object.maxObjectCoord().getZ();

        coords.add( min_x );
        coords.add( min_y );
        coords.add( max_z ); //0
        coords.add( max_x );
        coords.add( min_y );
        coords.add( max_z ); //1
        coords.add( max_x );
        coords.add( min_y );
        coords.add( min_z ); //2
        coords.add( min_x );
        coords.add( min_y );
        coords.add( min_z ); //3
        coords.add( min_x );
        coords.add( max_y );
        coords.add( max_z ); //4
        coords.add( max_x );
        coords.add( max_y );
        coords.add( max_z ); //5
        coords.add( max_x );
        coords.add( max_y );
        coords.add( min_z ); //6
        coords.add( min_x );
        coords.add( max_y );
        coords.add( min_z ); //7

        // x_line
        connects.add( 0 );
        connects.add( 1 );
        connects.add( 3 );
        connects.add( 2 );
        connects.add( 4 );
        connects.add( 5 );
        connects.add( 7 );
        connects.add( 6 );

        // y_line
        connects.add( 4 );
        connects.add( 0 );
        connects.add( 5 );
        connects.add( 1 );
        connects.add( 7 );
        connects.add( 3 );
        connects.add( 6 );
        connects.add( 2 );

        // z_line
        connects.add( 0 );
        connects.add( 3 );
        connects.add( 1 );
        connects.add( 2 );
        connects.add( 4 );
        connects.add( 7 );
        connects.add( 5 );
        connects.add( 6 );

        m_object.setLineType( LineObject.LineType.Segment );
        m_object.setColorType( LineObject.ColorType.LineColor );
        m_object.setCoords( Utility.ListToFloatArray( coords ) );
        m_object.setConnections( IntBuffer.wrap( Utility.ListToIntArray( connects ) ) );
        m_object.setColor( Color.BLACK );
        m_object.setSize( 1.0f );
    }

    private void create_corner_bounds(){
        ArrayList<Float> coords = new ArrayList<Float>();
        ArrayList<Integer> connects = new ArrayList<Integer>();

        final float min_x = m_object.minObjectCoord().getX();
        final float min_y = m_object.minObjectCoord().getY();
        final float min_z = m_object.minObjectCoord().getZ();
        final float max_x = m_object.maxObjectCoord().getX();
        final float max_y = m_object.maxObjectCoord().getY();
        final float max_z = m_object.maxObjectCoord().getZ();

        final float corner =
                kvs.core.util.Math.min( max_x - min_x, max_y - min_y, max_z - min_z ) * m_corner_scale;

        final float max_ext_x = max_x - corner;
        final float max_ext_y = max_y - corner;
        final float max_ext_z = max_z - corner;
        final float min_ext_x = min_x + corner;
        final float min_ext_y = min_y + corner;
        final float min_ext_z = min_z + corner;

        this.set_corner( new Vector3f( min_x, min_y, min_z ),
                new Vector3f( min_ext_x, min_ext_y, min_ext_z ),
                coords, connects );
        this.set_corner( new Vector3f( max_x, min_y, min_z ),
                new Vector3f( max_ext_x, min_ext_y, min_ext_z ),
                coords, connects );
        this.set_corner( new Vector3f( max_x, min_y, max_z ),
                new Vector3f( max_ext_x, min_ext_y, max_ext_z ),
                coords, connects );
        this.set_corner( new Vector3f( min_x, min_y, max_z ),
                new Vector3f( min_ext_x, min_ext_y, max_ext_z ),
                coords, connects );
        this.set_corner( new Vector3f( min_x, max_y, min_z ),
                new Vector3f( min_ext_x, max_ext_y, min_ext_z ),
                coords, connects );
        this.set_corner( new Vector3f( max_x, max_y, min_z ),
                new Vector3f( max_ext_x, max_ext_y, min_ext_z ),
                coords, connects );
        this.set_corner( new Vector3f( max_x, max_y, max_z ),
                new Vector3f( max_ext_x, max_ext_y, max_ext_z ),
                coords, connects );
        this.set_corner( new Vector3f( min_x, max_y, max_z ),
                new Vector3f( min_ext_x, max_ext_y, max_ext_z ),
                coords, connects );

        m_object.setLineType( LineObject.LineType.Segment );
        m_object.setColorType( LineObject.ColorType.LineColor );
        m_object.setCoords( Utility.ListToFloatArray( coords ) );
        m_object.setConnections( IntBuffer.wrap( Utility.ListToIntArray( connects ) ) );
        m_object.setColor( Color.BLACK );
        m_object.setSize( 1.0f );
    }

    private void create_circle_bounds(){
        ArrayList<Float> coords = new ArrayList<Float>();
        ArrayList<Integer> connects = new ArrayList<Integer>();

        final float min_x = m_object.minObjectCoord().getX();
        final float min_y = m_object.minObjectCoord().getY();
        final float min_z = m_object.minObjectCoord().getZ();
        final float max_x = m_object.maxObjectCoord().getX();
        final float max_y = m_object.maxObjectCoord().getY();
        final float max_z = m_object.maxObjectCoord().getZ();

        final Vector3f interval = new Vector3f( max_x - min_x, max_y - min_y, max_z - min_z );
        final Vector3f center = new Vector3f( max_x + min_x, max_y + min_y, max_z + min_z ).mul( 0.5f );

        final float diff_angle = (float) (2.0f * kvs.core.util.Math.pi) / m_division;

        final float x1 = interval.getX() * 0.5f;
        final float y1 = interval.getY() * 0.5f;
        final float z1 = interval.getZ() * 0.5f;

        final float a = (float) kvs.core.util.Math.sqrt2 * x1;
        final float a2 = a * a;
        final float b = (float) kvs.core.util.Math.sqrt2 * y1;
        final float b2 = b * b;
        final float c = (float) kvs.core.util.Math.sqrt2 * z1;
        final float c2 = c * c;

        int vertex_id = 0;
        final int division = (int) (m_division);

        // Circle 1 (X-Y axis)
        final float a2b2 = a2 * b2;
        for ( int i = 0; i <= division; i++ ){
            final float angle = diff_angle * i;

            final float sin = (float) Math.sin( angle );
            final float cos = (float) Math.cos( angle );
            final float sin2 = sin * sin;
            final float cos2 = cos * cos;

            final float r = (float) Math.sqrt( a2b2 / (a2 * sin2 + b2 * cos2) );

            coords.add( r * cos + center.getX() );
            coords.add( r * sin + center.getY() );
            coords.add( center.getZ() );
        }

        connects.add( vertex_id );
        vertex_id = coords.size() / 3 - 1;
        connects.add( vertex_id );
        vertex_id++;

        // Circle 2 (Y-Z axis)
        final float b2c2 = b2 * c2;
        for ( int i = 0; i <= division; i++ ){
            final float angle = diff_angle * i;

            final float sin = (float) Math.sin( angle );
            final float cos = (float) Math.cos( angle );
            final float sin2 = sin * sin;
            final float cos2 = cos * cos;

            final float r = (float) Math.sqrt( b2c2 / (b2 * sin2 + c2 * cos2) );

            coords.add( center.getX() );
            coords.add( r * cos + center.getY() );
            coords.add( r * sin + center.getZ() );
        }

        connects.add( vertex_id );
        vertex_id = coords.size() / 3 - 1;
        connects.add( vertex_id );
        vertex_id++;

        // Circle 3 (Z-X axis)
        final float c2a2 = c2 * a2;
        for ( int i = 0; i <= division; i++ ){
            final float angle = diff_angle * i;

            final float sin = (float) Math.sin( angle );
            final float cos = (float) Math.cos( angle );
            final float sin2 = sin * sin;
            final float cos2 = cos * cos;

            final float r = (float) Math.sqrt( c2a2 / (c2 * sin2 + a2 * cos2) );

            coords.add( r * sin + center.getX() );
            coords.add( center.getY() );
            coords.add( r * cos + center.getZ() );
        }

        connects.add( vertex_id );
        vertex_id = coords.size() / 3 - 1;
        connects.add( vertex_id );

        m_object.setLineType( LineObject.LineType.Polyline );
        m_object.setColorType( LineObject.ColorType.LineColor );
        m_object.setCoords( Utility.ListToFloatArray( coords ) );
        m_object.setConnections( IntBuffer.wrap( Utility.ListToIntArray( connects ) ) );
        m_object.setColor( Color.BLACK );
        m_object.setSize( 1.0f );
    }

    private void set_corner(
            final Vector3f v1,
            final Vector3f v2,
            ArrayList<Float> coords,
            ArrayList<Integer> connects ){
        coords.add( v1.getX() );
        coords.add( v1.getY() );
        coords.add( v1.getZ() );

        coords.add( v2.getX() );
        coords.add( v1.getY() );
        coords.add( v1.getZ() );

        coords.add( v1.getX() );
        coords.add( v2.getY() );
        coords.add( v1.getZ() );

        coords.add( v1.getX() );
        coords.add( v1.getY() );
        coords.add( v2.getZ() );

        final int dimension = 3;
        final int nvertices = coords.size() / dimension;

        connects.add( nvertices - 4 );
        connects.add( nvertices - 3 );
        connects.add( nvertices - 4 );
        connects.add( nvertices - 2 );
        connects.add( nvertices - 4 );
        connects.add( nvertices - 1 );
    }
}
