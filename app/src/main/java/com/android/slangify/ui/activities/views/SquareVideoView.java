package com.android.slangify.ui.activities.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by limormekaiten on 3/10/17.
 */

public class SquareVideoView extends VideoView {

    public SquareVideoView(Context context) {
        super(context);
    }

    public SquareVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
//    }
}
