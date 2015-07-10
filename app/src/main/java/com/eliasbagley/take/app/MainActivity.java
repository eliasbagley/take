package com.eliasbagley.take.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.eliasbagley.take.MediaContainer;
import com.eliasbagley.take.MediaPicker;
import com.eliasbagley.take.MediaPickerListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import hugo.weaving.DebugLog;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.image_button) ImageButton _imageButton;

    MediaPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        picker = new MediaPicker();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        picker.onActivityResult(this, requestCode, resultCode, data);
    }

    @DebugLog
    @OnClick(R.id.image_button)
    void imageButtonClicked(View v) {
       picker.showMediaSelectionPopup(this, v, new MediaPickerListener<MediaContainer>() {
           @DebugLog
           @Override
           public void success(MediaContainer mediaContainer) {
               Log.d("take", "success");
               _imageButton.setImageDrawable(mediaContainer.getDrawable(MainActivity.this));
           }

           @DebugLog
           @Override
           public void failure(String reason) {
               Log.d("take", "failure");
           }
       });
    }
}
