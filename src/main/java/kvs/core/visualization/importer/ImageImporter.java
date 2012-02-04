package kvs.core.visualization.importer;

import kvs.core.fileformat.FileFormatBase;
import kvs.core.fileformat.bmp.Bmp;
import kvs.core.visualization.object.ImageObject;
import kvs.core.visualization.object.ObjectBase;
import kvs.core.visualization.object.ImageObject.PixelType;

public class ImageImporter implements ImporterBase {
    
    public ImageImporter(){
        
    }

    public ObjectBase exec( FileFormatBase file_format ) {
        if ( file_format instanceof Bmp )
        {
            return importFile( (Bmp)file_format );
        }/*
        else if ( class_name == "Tiff" )
        {
            this->import( reinterpret_cast<const kvs::Tiff*>( file_format ) );
        }
        else if ( class_name == "Ppm" )
        {
            this->import( reinterpret_cast<const kvs::Ppm*>( file_format ) );
        }
        else if ( class_name == "Pgm" )
        {
            this->import( reinterpret_cast<const kvs::Pgm*>( file_format ) );
        }
        else if ( class_name == "Pbm" )
        {
            this->import( reinterpret_cast<const kvs::Pbm*>( file_format ) );
        }
        else if ( class_name == "Dicom" )
        {
            this->import( reinterpret_cast<const kvs::Dicom*>( file_format ) );
        }*/
        else
        {
            //kvsMessageError( "Unsupported class '""'." );
        }

        return null;
    }
    
    private static ObjectBase importFile( final Bmp bmp )
    {
        ImageObject image = new ImageObject();
        image.setWidth( bmp.width() );
        image.setHeight( bmp.height() );
        image.setData( bmp.data() );
        image.setType( getPixelType( bmp.bitsPerPixel() ) );
        return image;
    }
    
    private static PixelType getPixelType( int bpp ){
        switch( bpp ){
        case 8: return PixelType.Gray8;
        case 16: return PixelType.Gray16;
        case 24: return PixelType.Color24;
        case 32: return PixelType.Color32;
        }
        return null;
    }

}
