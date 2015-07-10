package com.eliasbagley.take;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by eliasbagley on 7/9/15.
 */
public interface MediaContainer {
    String getPath();
    Drawable getDrawable(Context context);
    Bitmap getBitmap();
}
