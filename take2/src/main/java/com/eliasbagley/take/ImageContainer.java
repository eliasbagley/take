package com.eliasbagley.take;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by eliasbagley on 7/8/15.
 */


public class ImageContainer implements MediaContainer {
    private String filename;
    private Bitmap _bitmap;

    //region constructors

    public ImageContainer(String filename) {
        this.filename = filename;
    }

    //endregion

    //region getters

    public String getFilename() {
        return this.filename;
    }

    public String getBase64encodedImage() throws FileNotFoundException {
        return ImageUtils.fileToBase64(filename);
    }

    // region media container methods

    public String getPath() {
        return this.filename;
    }

    public Bitmap getBitmap() {
        if (_bitmap == null) {
            _bitmap = fixOrientation();
        }

        return _bitmap;
    }

    public Drawable getDrawable(Context context) {
        return new BitmapDrawable(context.getResources(), getBitmap());
    }

    private Bitmap fixOrientation() {
        String file = getFilename();
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(file, opts);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        return rotatedBitmap;
    }


    //endregion
}
