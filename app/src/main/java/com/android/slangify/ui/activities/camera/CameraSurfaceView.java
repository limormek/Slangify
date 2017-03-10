package com.android.slangify.ui.activities.camera;

/**
 * Created by avishai on 3/10/2017.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.slangify.ui.activities.Events.SurfaceCreatedEvent;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by avishai on 3/3/2017.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private CameraControl camControl;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
        //initSurface();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setWillNotDraw(true);
        initSurface();
    }

    public CameraSurfaceView(Context context, CameraControl control){//}, Camera camera) {
        super(context);
        //mCamera = camera;
        camControl = control;
        initSurface();
    }

    private void initSurface() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        EventBus.getDefault().post(new SurfaceCreatedEvent());
        //camControl.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        // mCamera.release();

    }
}