package com.android.slangify.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        phraseModel = (PhraseModel) intent.getParcelableExtra(EXTRA_PHRASE);
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

        Long currentTime = System.currentTimeMillis();
        mCamControl = new CameraControl(mPreview, this, currentTime);
    }

/*    View.OnClickListener switchCameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // get the number of cameras
            if (!recording) {
                int camerasNumber = Camera.getNumberOfCameras();
                if (camerasNumber > 1) {
                    // release the old camera instance
                    // switch camera, from the front and the back and vice versa
                    mCamControl.swapCamera();

                } else {
                    Toast toast = Toast.makeText(myContext, "Sorry, your phone has only one camera!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    };*/

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

/*    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }*/


/*    boolean recording = false;
    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recording) {
                mCamControl.stopRecording();
                Toast.makeText(CaptureVideoActivity.this, "Video captured!", Toast.LENGTH_LONG).show();
                recording = false;
            } else {
                *//*if (!prepareMediaRecorder()) {
                    Toast.makeText(MainActivity.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                    finish();
                }*//*
                // work on UiThread for better performance
                runOnUiThread(new Runnable() {
                    public void run() {
                        // If there are stories, add them to the table

                        try {
                            mCamControl.startRecording();
                        } catch (final Exception ex) {
                            // Log.i("---","Exception in thread");
                        }
                    }
                });

                recording = true;
            }
        }
    };*/


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
                tvTimeout.setText(getString(R.string.done));

                try {
                    mCamControl.stopRecording();

                } catch (final Exception ex) {
                    // Log.i("---","Exception in thread");
                }

                showTranslation();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        IntentUtils.startDisplayVideoActivity(CaptureVideoActivity.this);
                    }
                }, 2000);

            }

        };
        if(readySetGo.isShowing()) {
            showOnDialogDismiss = true;
        } else {
            waitTimer.start();
        }




    }
}
