package com.android.slangify.ui.activities.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by avishai on 5/13/2017.
 */

public class PhraseView extends LinearLayout {
    public PhraseView(Context context) {
        super(context);
    }

    public PhraseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhraseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //TODO
        //implement this method
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //TODO
        //implement this method

        Log.d("PhraseView", "width: " + widthMeasureSpec + ", height: " +  heightMeasureSpec);
    }
}
