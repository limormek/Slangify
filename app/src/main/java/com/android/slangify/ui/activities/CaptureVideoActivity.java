package com.android.slangify.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.slangify.R;
import com.android.slangify.dialog.FancyAlertDialog;
import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.ui.activities.Events.SurfaceCreatedEvent;
import com.android.slangify.ui.activities.camera.CameraControl;
import com.android.slangify.ui.activities.camera.CameraSurfaceView;
import com.android.slangify.utils.Constants;
import com.android.slangify.utils.IOUtils;
import com.android.slangify.utils.IntentUtils;
import com.devspark.robototextview.widget.RobotoTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.slangify.utils.IntentUtils.EXTRA_PHRASE;

public class CaptureVideoActivity extends AppCompatActivity {

    private static final String TAG = CaptureVideoActivity.class.getName();
    @BindView(R.id.phrase_text_view)
    RobotoTextView phraseTextView;

    @BindView(R.id.translation_title_text_view)
    RobotoTextView translationTitleTextView;

    @BindView(R.id.challenge_content_layout)
    LinearLayout textContainer;

    @BindView(R.id.timeout)
    RobotoTextView tvTimeout;


    private String videoPathBack;
    private String videoPathFront;

    public CameraSurfaceView mPreview;

    private CameraControl mCamControl;
    private PhraseModel phraseModel;
    private FancyAlertDialog readySetGo;
    private boolean showOnDialogDismiss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        phraseModel = intent.getParcelableExtra(EXTRA_PHRASE);
        if (phraseModel != null) {
            phraseTextView.setText(phraseModel.getText());
        }
        initialize();

        readySetGo = new FancyAlertDialog(CaptureVideoActivity.this);
        readySetGo.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                showContent();
            }
        });
        readySetGo.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showContent();
            }
        }, 3000);
    }

    private void showContent() {
        readySetGo.dismiss();
        showPhrase();

        if(showOnDialogDismiss) {
            startCameraFlow();
        }
    }

    public void showPhrase() {
        phraseTextView.setVisibility(View.VISIBLE);
        textContainer.setBackgroundColor(ContextCompat.getColor(CaptureVideoActivity.this,R.color.video_display_background_color));
    }

    public void showTranslation() {
        translationTitleTextView.setVisibility(View.VISIBLE);
        phraseTextView.setText(phraseModel.getTranslation());
    }

    public void initialize() {
        long currentTime = System.currentTimeMillis();
        mPreview = (CameraSurfaceView) findViewById(R.id.camera_preview);
        mCamControl = new CameraControl(mPreview, this);

        String slangifyDirectoryPath = "";
        try {
            slangifyDirectoryPath = IOUtils.getSlangifyDirectoryPath(CaptureVideoActivity.this);

        } catch (IOUtils.StorageUnavailableException e) {
            //fail quietly
        }

        videoPathBack =  String.format((slangifyDirectoryPath + Constants.Media.FILMED_VIDEO_NAME), String.valueOf(currentTime));

        currentTime = System.currentTimeMillis();
        videoPathFront = String.format((slangifyDirectoryPath + Constants.Media.FILMED_VIDEO_NAME), String.valueOf(currentTime));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // when on Pause, release camera in order to be used from other applications
        mCamControl.releaseCamera();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        IntentUtils.startCreateActivity(CaptureVideoActivity.this);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Events
    ///////////////////////////////////////////////////////////////////////////

    //Entry point code for starting the video recording flow
    private void startCameraFlow(){

        waitTimer.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCamControl.swapCamera();
                waitTimer.start();
            }
        }, 7000);
    }

    private CountDownTimer waitTimer = new CountDownTimer(6000, 1000) {

        boolean isRecording = false;
        boolean isFirstVideo = true;

        public void onTick(long millisUntilFinished) {

            Long delta = millisUntilFinished / 1000;
            tvTimeout.setText(delta.toString());

            if(!isRecording) {
                try {
                    if(isFirstVideo)
                        mCamControl.startRecording(videoPathBack);
                    else
                        mCamControl.startRecording(videoPathFront);

                    isRecording = true;
                } catch (final Exception ex) {
                    // Log.i("---","Exception in thread");
                }
            }

            if(!isFirstVideo) {
                showTranslation();
            }
        }

        public void onFinish() {
            if(isFirstVideo) {
                tvTimeout.setText(getString(R.string.capture_video_done));
            }

            try {
                mCamControl.stopRecording();
                isRecording = false;
            } catch (final Exception ex) {
                Log.e(TAG, "onFinish: error finish recording: " + ex.getMessage());
            }

            if(!isFirstVideo) {

                //todo - move to a more logical location:
                Log.e(TAG, "onFinish: start display video act!");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IntentUtils.startDisplayVideoActivity(CaptureVideoActivity.this,
                                phraseModel,
                                videoPathBack,
                                videoPathFront,
                                getIntent().getStringExtra(IntentUtils.EXTRA_LANGUAGE));
                        finish();
                    }
                }, 1500);
            }

            isFirstVideo = false;
        }
    };

    @Subscribe
    public void onSurfaceCreated(SurfaceCreatedEvent event) {
        mCamControl.startPreview();
        //Log.i("MESSAGE!!", "got into onSurfaceCreated event");

        if(readySetGo.isShowing()) {
            showOnDialogDismiss = true;
        } else {
            //Log.i("MESSAGE!!", "got into onSurfaceCreated event - start camera flow");
            startCameraFlow();
        }
    }

}
