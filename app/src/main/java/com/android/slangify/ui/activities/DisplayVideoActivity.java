package com.android.slangify.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.slangify.R;
import com.android.slangify.dialog.FancyAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limormekaiten on 3/8/17.
 */

public class DisplayVideoActivity extends AppCompatActivity {

    @BindView(R.id.dialog_btn)
    TextView btnSubmit;
    @BindView(R.id.video)
    VideoView video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_video);

        ButterKnife.bind(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FancyAlertDialog fancyAlertDialog = new FancyAlertDialog(DisplayVideoActivity.this);
                fancyAlertDialog.show();
            }
        });

        video.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=MaHq0Gh6e2k"));

        video.start();
    }
}
