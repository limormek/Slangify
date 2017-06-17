package com.android.slangify.ui.activities;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.android.slangify.R;
import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.utils.Constants;
import com.android.slangify.utils.IOUtils;
import com.android.slangify.utils.IntentUtils;
import com.android.slangify.utils.MediaEditUtils;
import com.android.slangify.utils.UiUtils;
import com.devspark.robototextview.widget.RobotoTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by limormekaiten on 3/8/17.
 */

public class DisplayVideoActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.video)
    VideoView video;

    @BindView(R.id.iv_play)
    ImageView playBtn;

    @BindView(R.id.tv_challenge_text)
    RobotoTextView tvChallengeText;

    @BindView(R.id.share_video)
    ImageView ivShare;

    @BindView(R.id.tv_language)
    RobotoTextView tvLanguage;

    @BindView(R.id.tv_translation)
    RobotoTextView tvTranslation;

    @BindView(R.id.tv_extra)
    RobotoTextView tvDidYouKnow;

    @BindView(R.id.btn_finish)
    Button btnFinish;

    private String videoPathBack;
    private String videoPathFront;
    private String videoPathMerged;

    @Override
    public void onBackPressed() {
        IntentUtils.startCreateActivity(DisplayVideoActivity.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_video);

        ButterKnife.bind(this);

        //align play button to center of the video
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) playBtn.getLayoutParams();
        //set the play button margins
        int playBtnSize = (int) getResources().getDimension(R.dimen.play_size);
        int margin = (UiUtils.getDeviceWidth(DisplayVideoActivity.this) - playBtnSize)/2;
        lp.setMargins(margin, margin, lp.rightMargin, lp.bottomMargin);
        playBtn.setLayoutParams(lp);

        videoPathBack = getIntent().getStringExtra(IntentUtils.EXTRA_FILE_PATH_BACK);
        videoPathFront = getIntent().getStringExtra(IntentUtils.EXTRA_FILE_PATH_FRONT);

        String selectedLanguage = getIntent().getStringExtra(IntentUtils.EXTRA_LANGUAGE);
        tvLanguage.setText(String.format(getString(R.string.display_language), selectedLanguage));

        PhraseModel phraseModel = getIntent().getParcelableExtra(IntentUtils.EXTRA_PHRASE);

        if (phraseModel != null) {
            tvChallengeText.setText(phraseModel.getText());

            tvTranslation.setText(String.format(getString(R.string.display_translation), phraseModel.getTranslation()));
            String extra = phraseModel.getExtra();
            if (!TextUtils.isEmpty(extra)) {
                tvDidYouKnow.setText(extra);
            } else {
                tvDidYouKnow.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(videoPathBack) && !TextUtils.isEmpty(videoPathFront)) {
            playVideo();

            String slangifyDirectoryPath = "/sdcard/Slangify";
            try {
                slangifyDirectoryPath = IOUtils.getSlangifyDirectoryPath(DisplayVideoActivity.this);

            } catch (IOUtils.StorageUnavailableException e) {
                //fail quietly
            }

            //Start merge videos.
            videoPathMerged = String.format((slangifyDirectoryPath + Constants.Media.MERGED_VIDEO_NAME), String.valueOf(System.currentTimeMillis()));
            Boolean isSucceeded = MediaEditUtils.merge2VideosFFMPEG(videoPathBack, videoPathFront, videoPathMerged, slangifyDirectoryPath, getApplicationContext());

            if (isSucceeded) {
                //upload to server
                //start uploading the merged video
                //TODO
                //Put here the merged video

/*            startService(new Intent(DisplayVideoActivity.this, UploadService.class)
                    .setAction(UploadService.ACTION_UPLOAD)
                    .putExtra(IntentUtils.EXTRA_FILE_PATH, videoPath));*/

            }



        }

        setListeners();

    }

    private void playVideo() {
        video.setVideoURI(Uri.parse(videoPathBack));
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.setVideoURI(Uri.parse(videoPathFront));
                video.start();
                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playBtn.setVisibility(View.VISIBLE);
                        playBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playBtn.setVisibility(View.GONE);
                                playVideo();
                            }
                        });
                    }
                });
            }
        });
        video.start();
    }

    private void setListeners() {
        btnFinish.setOnClickListener(this);
        ivShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_video:
                //TODO
                //need to share the merged video
                IntentUtils.shareVideoUri(DisplayVideoActivity.this, Uri.parse(videoPathBack));
                break;
            case R.id.btn_finish:
                IntentUtils.startCreateActivity(DisplayVideoActivity.this);
                break;
        }
    }
}
