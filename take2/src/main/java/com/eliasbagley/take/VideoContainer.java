package com.eliasbagley.take;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by eliasbagley on 7/9/15.
 */
public class VideoContainer implements MediaContainer {
    private Uri uri;
    private String fileUrl;

    public VideoContainer(Context context, Uri uri) {
        this.uri = uri;
        this.fileUrl = Utils.getRealPathFromURI(context, uri);
    }

    //region getters


    public Uri getUri() {
        return this.uri;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }


    //endregion

    //region MediaContainer methods

    public String getPath() {
        return fileUrl;
    }

    public Bitmap getBitmap() {
        return ThumbnailUtils.createVideoThumbnail(fileUrl, MediaStore.Video.Thumbnails.MINI_KIND);
    }

    public Drawable getDrawable(Context context) {
        return new BitmapDrawable(context.getResources(), getBitmap());
    }

    //endregion
}
