package com.android.slangify.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.android.slangify.R;
import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.utils.IntentUtils;
import com.devspark.robototextview.widget.RobotoTextView;

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

    @BindView(R.id.tv_challenge_text)
    RobotoTextView tvChallengeText;


    @BindView(R.id.tv_language)
    RobotoTextView tvLanguage;

    @BindView(R.id.tv_translation)
    RobotoTextView tvTranslation;

    @BindView(R.id.tv_extra)
    RobotoTextView tvDidYouKnow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_video);

        ButterKnife.bind(this);

        String path = getIntent().getStringExtra(IntentUtils.EXTRA_FILE_PATH);

        String selectedLanguage = getIntent().getStringExtra(IntentUtils.EXTRA_LANGUAGE);
        tvLanguage.setText(String.format(getString(R.string.display_language), selectedLanguage));

        PhraseModel phraseModel = getIntent().getParcelableExtra(IntentUtils.EXTRA_PHRASE);

        if (phraseModel != null) {
            tvChallengeText.setText(phraseModel.getText());

            tvTranslation.setText(String.format(getString(R.string.display_translation), phraseModel.getTranslation()));
            String extra = phraseModel.getExtra();
            if(!TextUtils.isEmpty(extra)) {
                tvDidYouKnow.setText(extra);
            } else {
                tvDidYouKnow.setVisibility(View.GONE);
            }
        }

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
