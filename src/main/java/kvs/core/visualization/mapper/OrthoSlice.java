/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kvs.core.visualization.mapper;

import kvs.core.matrix.Matrix33f;

/**
 *
 * @author eguchi
 */
public class OrthoSlice extends SlicePlane{

    private static final long serialVersionUID = 1L;

    public enum AlignedAxis{

        XAxis( 0 ),
        YAxis( 1 ),
        ZAxis( 2 );

        private AlignedAxis( int value ){
            this.mValue = value;
        }
        private int mValue;

        public int getValue(){
            return mValue;
        }

        ;
    };
    protected AlignedAxis m_aligned_axis;

    public OrthoSlice(
            final double coordinate,
            final AlignedAxis aligned_axis ){
        super( Matrix33f.IDENTITY.getRow( aligned_axis.getValue() ).mul( (float) coordinate ),
               Matrix33f.IDENTITY.getRow( aligned_axis.getValue() ) );
    }

    public OrthoSlice(
            final double coordinate,
            final AlignedAxis aligned_axis,
            final TransferFunction transfer_function ){
        super( Matrix33f.IDENTITY.getRow( aligned_axis.getValue() ).mul( (float) coordinate ),
               Matrix33f.IDENTITY.getRow( aligned_axis.getValue() ),
               transfer_function );
    }
}
