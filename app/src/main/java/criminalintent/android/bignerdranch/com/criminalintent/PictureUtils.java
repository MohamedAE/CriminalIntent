package criminalintent.android.bignerdranch.com.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {

    /*
    This method is not dependant on the caller knowing the dimensions of the given image
    Checks display size; scales image down accordingly
    */
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }

    /*
    This method is predicated on the caller knowing the dimensions of the given image within
    the PhotoView. This will not be known until onCreate()/onStart()/onResume() is called.
    */
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        //Read image dimensions from disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //Figure out how much to scale down by
        /*
        inSampleSize - factor of image scaling
        value of 1 indicates one horizontal pixel in scaled down image for each in original image
        value of 2 indicates one horizontal pixel in scaled down image for every two in original image
        */
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > srcWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Read in and create final Bitmap
        return BitmapFactory.decodeFile(path, options);
    }

}