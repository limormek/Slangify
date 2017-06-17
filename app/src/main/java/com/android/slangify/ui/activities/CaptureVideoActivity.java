package com.android.slangify.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.slangify.R;
import com.android.slangify.application_logic.CaptureManager;
import com.android.slangify.application_logic.interfaces.CaptureManagerInterface;
import com.android.slangify.application_logic.interfaces.CaptureVideoListener;
import com.android.slangify.dialog.FancyAlertDialog;
import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.ui.activities.Events.SurfaceCreatedEvent;
import com.android.slangify.ui.activities.camera.CameraSurfaceView;
import com.android.slangify.utils.Constants;
import com.android.slangify.utils.IOUtils;
import com.android.slangify.utils.IntentUtils;
import com.android.slangify.utils.SharedPreferencesUtils;
import com.android.slangify.utils.UiUtils;
import com.devspark.robototextview.widget.RobotoTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.slangify.utils.IntentUtils.EXTRA_PHRASE;

public class CaptureVideoActivity extends AppCompatActivity {

    private static final String TAG = CaptureVideoActivity.class.getName();
    private static final long DIALOG_TIMEOUT_MILLISEC = 3000;

    ///////////////////////////////////////////////////////////////////////////
    // Views
    ///////////////////////////////////////////////////////////////////////////
    @BindView(R.id.phrase_layout)
    LinearLayout phraseLayout;

    @BindView(R.id.phrase_text_view)
    RobotoTextView phraseTextView;

    @BindView(R.id.translation_title_text_view)
    RobotoTextView translationTitleTextView;

    @BindView(R.id.timeout)
    RobotoTextView tvTimeout;

    @BindView(R.id.camera_preview)
    CameraSurfaceView mPreview;

    ///////////////////////////////////////////////////////////////////////////
    // Other Params
    ///////////////////////////////////////////////////////////////////////////

    private PhraseModel phraseModel;
    private FancyAlertDialog readySetGoDialog;
    private boolean surfaceReadyForCapturing;

    private CaptureManagerInterface captureManager;

    private String videoPathBack;
    private String videoPathFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initViews();

        initPaths();//todo - calculate before, pass to captureManager / save in prefs

        captureManager = new CaptureManager(CaptureVideoActivity.this, mPreview,
                videoPathBack, videoPathFront);

        //Update phrase data
        Intent intent = getIntent();
        phraseModel = intent.getParcelableExtra(EXTRA_PHRASE);
        if (phraseModel != null) {
            phraseTextView.setText(phraseModel.getText());

            setFilmingTime();
        }

        //Start dialog timeout to show the phrase
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showContent();
            }
        }, DIALOG_TIMEOUT_MILLISEC);
    }

    private void setFilmingTime() {
        int textLength = phraseModel.getText().length();
        int filmingTimeSec = 6;
        if(textLength <= 20) {
            filmingTimeSec = 4;
        } else if (textLength > 42) {
            filmingTimeSec = 7;
        }
        SharedPreferencesUtils.setVideoFilmingCountDown(CaptureVideoActivity.this, filmingTimeSec*1000);
    }

    private void initViews() {
        ButterKnife.bind(this);

        //set the phrase layout correct height
        int phraseHeight = UiUtils.getDeviceHeight(CaptureVideoActivity.this) - UiUtils.getDeviceWidth(CaptureVideoActivity.this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) phraseLayout.getLayoutParams();
        params.height = phraseHeight;
        phraseLayout.setLayoutParams(params);

        //init dialog
        readySetGoDialog = new FancyAlertDialog(CaptureVideoActivity.this);
        readySetGoDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                showContent();
            }
        });
        readySetGoDialog.show();
    }

    private void showContent() {
        readySetGoDialog.dismiss();
        showPhrase();

        if (surfaceReadyForCapturing) {
            startCameraFlow();
        }
    }

    public void showPhrase() {
        phraseLayout.setVisibility(View.VISIBLE);
        phraseTextView.setVisibility(View.VISIBLE);
    }

    public void showTranslation() {
        translationTitleTextView.setVisibility(View.VISIBLE);
        phraseTextView.setText(phraseModel.getTranslation());
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
        if (captureManager != null) {
            captureManager.release();
        }
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
    private void startCameraFlow() {

        //3 listeners:
        //1- finish recording back camera - in it: update UI.
        //2 - finish front recording - in it: update UI + intent for start next activity
        //3 - onTick - update countdown UI component
        captureManager.startCapturing(new CaptureVideoListener() {
                                          @Override
                                          public void onTick(long timePassed) {
                                              tvTimeout.setText(String.valueOf(timePassed));
                                          }

                                          @Override
                                          public void onFinishBack() {
                                              tvTimeout.setText(getString(R.string.capture_video_done));
                                              showTranslation();
                                          }

                                          @Override
                                          public void onFinishFront() {
                                              //Start next activity
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
                                      }
        );
    }

    private void initPaths() {
        long currentTime = System.currentTimeMillis();

        //mCamControl = new CameraControl(mPreview, this);

        String slangifyDirectoryPath = "";
        try {
            slangifyDirectoryPath = IOUtils.getSlangifyDirectoryPath(CaptureVideoActivity.this);

        } catch (IOUtils.StorageUnavailableException e) {
            //fail quietly
        }

        videoPathBack = String.format((slangifyDirectoryPath + Constants.Media.FILMED_VIDEO_NAME_BACK), String.valueOf(currentTime));

        currentTime = System.currentTimeMillis();
        videoPathFront = String.format((slangifyDirectoryPath + Constants.Media.FILMED_VIDEO_NAME_FRONT), String.valueOf(currentTime));
    }

    ///////////////////////////////////////////////////////////////////////////
    // Events
    ///////////////////////////////////////////////////////////////////////////
    @Subscribe
    public void onSurfaceCreated(SurfaceCreatedEvent event) {

        if (captureManager != null) {
            captureManager.onSurfaceCreated();
        }
        //camera issues handled inside the manager

        if (readySetGoDialog.isShowing()) {
            surfaceReadyForCapturing = true;
        } else {
            //Dialog already dismissed - start camera flow safely.
            startCameraFlow();
        }
    }

}
