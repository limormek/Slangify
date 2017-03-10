package com.android.slangify.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.slangify.R;
import com.android.slangify.ui.activities.Events.SurfaceCreatedEvent;
import com.android.slangify.ui.activities.camera.CameraControl;
import com.android.slangify.ui.activities.camera.CameraSurfaceView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CaptureVideoActivity extends AppCompatActivity {

    public CameraSurfaceView mPreview;

    private Button capture, switchCamera;

    private CameraControl mCamControl;

    private Context myContext;

    private static final int LOCATION_PERMISSION_CODE = 000;
    private static final int STORAGE_PERMISSION_CODE = 111;
    private static final int AUDIO_PERMISSION_CODE = 222;
    private static final int CAMERA_PERMISSION_CODE = 333;
    private boolean hasPermissions = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        myContext = this;

        if (!checkStoragePermission())
            requestStoragePermission();
        if (!checkAudioPermission())
            requestAudioPermission();
        if (!checkCameraPermission())
            requestCameraPermission();

        initialize();
    }

    public void initialize() {
        mPreview = (CameraSurfaceView) findViewById(R.id.camera_preview);

        mCamControl = new CameraControl(mPreview, this);
        capture = (Button) findViewById(R.id.button_capture);
        capture.setOnClickListener(captrureListener);

        switchCamera = (Button) findViewById(R.id.button_ChangeCamera);
        switchCamera.setOnClickListener(switchCameraListener);
    }

    View.OnClickListener switchCameraListener = new View.OnClickListener() {
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
    };


    /**
     * @return true if WRITE_EXTERNAL_STORAGE already granted, false otherwise
     */
    public boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @return true if RECORD_AUDIO already granted, false otherwise
     */
    public boolean checkAudioPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @return true if CAMERA already granted, false otherwise
     */
    public boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                STORAGE_PERMISSION_CODE);

    }

    public void requestAudioPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.RECORD_AUDIO,},
                AUDIO_PERMISSION_CODE);
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.CAMERA,},
                CAMERA_PERMISSION_CODE);

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

/*    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }*/


    boolean recording = false;
    View.OnClickListener captrureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recording) {
                mCamControl.stopRecording();
                Toast.makeText(CaptureVideoActivity.this, "Video captured!", Toast.LENGTH_LONG).show();
                recording = false;
            } else {
                /*if (!prepareMediaRecorder()) {
                    Toast.makeText(MainActivity.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                    finish();
                }*/
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
    };


    ///////////////////////////////////////////////////////////////////////////
    // Events
    ///////////////////////////////////////////////////////////////////////////
    @Subscribe
    public void onSurfaceCreated(SurfaceCreatedEvent event) {
        if (!hasPermissions) {
            //save state - surface ready
        } else {

            mCamControl.startPreview();
        }
    }
}
