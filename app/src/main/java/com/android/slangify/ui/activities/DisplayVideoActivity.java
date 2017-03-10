package com.android.slangify.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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

    @BindView(R.id.video)
    VideoView video;

    @BindView(R.id.iv_play)
    ImageView playBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_video);

        ButterKnife.bind(this);

        String path = getIntent().getStringExtra("filePath");

        if(!TextUtils.isEmpty(path)) {
            video.setVideoURI(Uri.parse(path));

            video.start();
//            playBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

        }

    }
}
