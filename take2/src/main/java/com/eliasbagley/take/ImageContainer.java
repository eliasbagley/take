package com.eliasbagley.take;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

/**
 * Created by eliasbagley on 7/8/15.
 */


public class ImageContainer implements MediaContainer {
    private String filename;

    //region constructors

    public ImageContainer(String filename) {
        this.filename = filename;
    }

    //endregion

    //region getters

    public String getFilename() {
        return this.filename;
    }

    public String getBase64encodedImage() {
        return ImageUtils.fileToBase64(filename);
    }

    // region media container methods

    public String getPath() {
        return this.filename;
    }

    public Bitmap getBitmap() {
        return BitmapFactory.decodeFile(filename);
    }

    public Drawable getDrawable(Context context) {
        return Drawable.createFromPath(filename);
    }

    //endregion
}
