package com.eliasbagley.take;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.timemachine.timemachineandroid.R;
import com.timemachine.timemachineandroid.utils.ImageUtils;
import com.timemachine.timemachineandroid.utils.Utils;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import retrofit.Callback;
import timber.log.Timber;

/**
 * Created by eliasbagley on 7/8/15.
 */
public class MediaPicker {
    private static final int REQUEST_IMAGE_CAPTURE = 9234;
    private static final int REQUEST_VIDEO_CAPTURE = 9235;
    private static final int REQUEST_LOAD_IMAGE = 9236;

    private Callback<MediaContainer> _imageCallback;
    private Callback<MediaContainer> _videoCallback;
    private StringBuilder _strBuilder;

    @Inject
    public MediaPicker() {
    }

    //region photo

    public void takePicture(Activity activity, Callback<MediaContainer> callback) {
        _strBuilder = new StringBuilder();
        _imageCallback = callback;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = ImageUtils.createImageFile(_strBuilder);
            } catch (IOException e) {
                Timber.e("Error occured while creating the file");
                callback.failure(null);
                return;
            }

            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    public void pickPhotoFromGallery(Activity activity, Callback<MediaContainer> callback) {
        _imageCallback = callback;

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        activity.startActivityForResult(galleryIntent, REQUEST_LOAD_IMAGE);
    }

    //endregion


    //region video

    public void takeVideo(Activity activity, Callback<MediaContainer> callback) {
        _videoCallback = callback;

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {

            activity.startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
        }
    }


    public void pickVideoFromGallery(Activity activity, Callback<MediaContainer> callback) {
        _videoCallback = callback;

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("video/*");
        activity.startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
    }

    //endregion

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (_imageCallback != null) {
                    ImageContainer imageContainer = new ImageContainer(_strBuilder.toString());
                   _imageCallback.success(imageContainer, null);
                } else {
                    _imageCallback.failure(null);
                }
            } else if (requestCode == REQUEST_LOAD_IMAGE) {
                Uri imageUri = data.getData();

                if (imageUri != null) {
                    String path = Utils.getRealPathFromURI(activity, imageUri);
                    ImageContainer imageContainer = new ImageContainer(path);
                    _imageCallback.success(imageContainer, null);
                } else {
                    _imageCallback.failure(null);
                }
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                Uri videoUri = data.getData();

                if (videoUri != null) {
                    VideoContainer videoContainer = new VideoContainer(activity, videoUri);
                    _videoCallback.success(videoContainer, null);
                } else {
                    _videoCallback.failure(null);
                }
            }
        }
    }

    public void showImageSelectionPopup(final Activity activity, View v, final Callback<MediaContainer> callback) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.take_picture:
                        takePicture(activity, callback);
                        return true;
                    case R.id.picture_from_gallery:
                        pickPhotoFromGallery(activity, callback);
                        break;
                    default:
                        return false;
                }

                return false;
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_photo_media_options, popup.getMenu());
        popup.show();

    }

    public void showVideoSelectionPopup(final Activity activity, View v, final Callback<MediaContainer> callback) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.take_video:
                        takeVideo(activity, callback);
                        return true;
                    case R.id.video_from_gallery:
                        pickVideoFromGallery(activity, callback);
                        break;
                    default:
                        return false;
                }

                return false;
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_video_media_options, popup.getMenu());
        popup.show();
    }


    public void showMediaSelectionPopup(final Activity activity, View v, final Callback<MediaContainer> callback) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.take_picture:
                        takePicture(activity, callback);
                        return true;
                    case R.id.picture_from_gallery:
                        pickPhotoFromGallery(activity, callback);
                        break;
                    case R.id.take_video:
                        takeVideo(activity, callback);
                        return true;
                    case R.id.video_from_gallery:
                        pickVideoFromGallery(activity, callback);
                        break;
                    default:
                        return false;
                }

                return false;
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_media_options, popup.getMenu());
        popup.show();
    }


}
