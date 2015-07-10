package com.eliasbagley.take;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.io.File;
import java.io.IOException;

/**
 * Created by eliasbagley on 7/8/15.
 */
public class MediaPicker {
    private static final int REQUEST_IMAGE_CAPTURE = 9234;
    private static final int REQUEST_VIDEO_CAPTURE = 9235;
    private static final int REQUEST_LOAD_IMAGE = 9236;

    private MediaPickerListener<MediaContainer> _imageCallback;
    private MediaPickerListener<MediaContainer> _videoCallback;
    private StringBuilder _strBuilder;

    public MediaPicker() {
    }

    //region photo

    public void takePicture(Activity activity, MediaPickerListener<MediaContainer> callback) {
        _strBuilder = new StringBuilder();
        _imageCallback = callback;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = ImageUtils.createImageFile(_strBuilder);
            } catch (IOException e) {
                callback.failure("Error occured while creating the file");
                return;
            }

            if (photoFile != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    public void pickPhotoFromGallery(Activity activity, MediaPickerListener<MediaContainer> callback) {
        _imageCallback = callback;

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        activity.startActivityForResult(galleryIntent, REQUEST_LOAD_IMAGE);
    }

    //endregion


    //region video

    public void takeVideo(Activity activity, MediaPickerListener<MediaContainer> callback) {
        _videoCallback = callback;

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {

            activity.startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
        }
    }


    public void pickVideoFromGallery(Activity activity, MediaPickerListener<MediaContainer> callback) {
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
                   _imageCallback.success(imageContainer);
                }
            } else if (requestCode == REQUEST_LOAD_IMAGE) {
                Uri imageUri = data.getData();

                if (imageUri != null) {
                    String path = Utils.getRealPathFromURI(activity, imageUri);
                    ImageContainer imageContainer = new ImageContainer(path);
                    if (_imageCallback != null) {
                        _imageCallback.success(imageContainer);
                    }
                } else {
                    if (_imageCallback != null) {
                        _imageCallback.failure("Image URI is null");
                    }
                }
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                Uri videoUri = data.getData();

                if (videoUri != null) {
                    VideoContainer videoContainer = new VideoContainer(activity, videoUri);
                    if (_videoCallback != null) {
                        _videoCallback.success(videoContainer);
                    }
                } else {
                    if (_videoCallback != null) {
                        _videoCallback.failure("Video URI is null");
                    }
                }
            }
        }
    }

    public void showImageSelectionPopup(final Activity activity, View v, final MediaPickerListener<MediaContainer> callback) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.take_picture) {
                    takePicture(activity, callback);
                    return true;
                } else if (id == R.id.picture_from_gallery) {
                    pickPhotoFromGallery(activity, callback);
                    return false;
                } else {
                    return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_photo_media_options, popup.getMenu());
        popup.show();

    }

    public void showVideoSelectionPopup(final Activity activity, View v, final MediaPickerListener<MediaContainer> callback) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.take_video) {
                    takeVideo(activity, callback);
                    return true;
                } else if (id == R.id.video_from_gallery) {
                    pickVideoFromGallery(activity, callback);
                    return false;
                } else {
                    return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_video_media_options, popup.getMenu());
        popup.show();
    }


    public void showMediaSelectionPopup(final Activity activity, View v, final MediaPickerListener<MediaContainer> callback) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.take_video) {
                    takeVideo(activity, callback);
                    return true;
                } else if (id == R.id.video_from_gallery) {
                    pickVideoFromGallery(activity, callback);
                    return false;
                } else if (id == R.id.take_picture) {
                    takePicture(activity, callback);
                    return true;
                } else if (id == R.id.picture_from_gallery) {
                    pickPhotoFromGallery(activity, callback);
                    return false;
                } else {
                    return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_media_options, popup.getMenu());
        popup.show();
    }


}
