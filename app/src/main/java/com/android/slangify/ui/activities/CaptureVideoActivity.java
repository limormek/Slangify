package com.android.slangify.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.slangify.R;
import com.android.slangify.dialog.FancyAlertDialog;
import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.ui.activities.Events.SurfaceCreatedEvent;
import com.android.slangify.ui.activities.camera.CameraControl;
import com.android.slangify.ui.activities.camera.CameraSurfaceView;
import com.android.slangify.utils.IntentUtils;
import com.devspark.robototextview.widget.RobotoTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.slangify.utils.IntentUtils.EXTRA_PHRASE;

public class CaptureVideoActivity extends AppCompatActivity {

    @BindView(R.id.phrase_text_view)
    RobotoTextView phraseTextView;

    @BindView(R.id.translation_title_text_view)
    RobotoTextView translationTitleTextView;

    @BindView(R.id.buttonsLayout)
    LinearLayout textContainer;

    @BindView(R.id.timeout)
    RobotoTextView tvTimeout;

    private Long currentTime;
    private String FilePath;
    private String sourceText;

    public CameraSurfaceView mPreview;


    public static boolean isRecording = false;
    private CameraControl mCamControl;
    private PhraseModel phraseModel;
    private Context myContext;
    private FancyAlertDialog readySetGo;
    private boolean showOnDialogDismiss;
    private CountDownTimer waitTimer;

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
        myContext = this;
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
            waitTimer.start();
        }
    }

    public void showPhrase() {
        textContainer.setBackgroundColor(ContextCompat.getColor(CaptureVideoActivity.this,R.color.video_display_background_color));
    }

    public void showTranslation() {
        translationTitleTextView.setVisibility(View.VISIBLE);
        phraseTextView.setText(phraseModel.getTranslation());
    }

    public void initialize() {
        mPreview = (CameraSurfaceView) findViewById(R.id.camera_preview);

        currentTime = System.currentTimeMillis();
        mCamControl = new CameraControl(mPreview, this, currentTime);
        FilePath =  String.format("/sdcard/slangify%s.mp4", String.valueOf(currentTime));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // when on Pause, release camera in order to be used from other
        // applications
        mCamControl.releaseCamera();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Events
    ///////////////////////////////////////////////////////////////////////////
    @Subscribe
    public void onSurfaceCreated(SurfaceCreatedEvent event) {
        mCamControl.startPreview();

        waitTimer = new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {

                Long delta = millisUntilFinished / 1000;
                tvTimeout.setText(delta.toString());
                if(!isRecording) {
                    try {
                        mCamControl.startRecording();

                        isRecording = true;
                    } catch (final Exception ex) {
                        // Log.i("---","Exception in thread");
                    }
                }
            }

            public void onFinish() {
                tvTimeout.setText(getString(R.string.capture_video_done));

                try {
                    mCamControl.stopRecording();

                } catch (final Exception ex) {
                    // Log.i("---","Exception in thread");
                }

                showTranslation();

                try{
                    mCamControl.swapCamera();
                }
                catch(Exception ex)
                {

                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        IntentUtils.startDisplayVideoActivity(CaptureVideoActivity.this,
//                                phraseModel,
//                                FilePath, getIntent().getStringExtra(IntentUtils.EXTRA_LANGUAGE));
                    }
                }, 6000);

            }

        };
        if(readySetGo.isShowing()) {
            showOnDialogDismiss = true;
        } else {
            waitTimer.start();
        }




    }
}
