package com.android.slangify.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slangify.R;


/**
 * Created by limormekaiten on 2/16/17.
 */

public class FancyAlertDialog extends Dialog implements View.OnClickListener {

    private int mAlertType;

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;

    private Context mContext;

    private View mDialogView;
    //Views
    private TextView mTitleTextView;
    private TextView mContentTextView;
//    private ImageView mCustomImage;
//    private Button mConfirmButton;
    private ImageView mCancelButton;

    //Data
    private String mTitleText;
    private String mContentText;
    private boolean mShowContent;
//    private String mCancelText;
    private boolean mShowCancel;
    private String mConfirmText;
    private Drawable mCustomImgDrawable;

    public FancyAlertDialog(Context context) {
        this(context, CUSTOM_IMAGE_TYPE);
    }

    public FancyAlertDialog(Context context, int alertType) {
        super(context, R.style.fancy_alert_dialog);
        mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);//todo change ?

        mAlertType = alertType;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alert_fancy_dialog);

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);

        mTitleTextView = (TextView)findViewById(R.id.title_text);
        mContentTextView = (TextView)findViewById(R.id.content_text);
        mCancelButton = (ImageView)findViewById(R.id.btn_close);

//        mConfirmButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        //FREEMIUM
        mTitleText = mContext.getString(R.string.ready_set_dialog_title);
        mContentText = mContext.getString(R.string.ready_set_dialog_subtitle);
//        setCustomImage(R.drawable.ic_freemium_dialog);


        setTitleText(mTitleText);
        setContentText(mContentText);
//        setConfirmText(mConfirmText);
        changeAlertType(mAlertType, true);
    }

    public FancyAlertDialog setTitleText (String text) {
        mTitleText = text;
        if (mTitleTextView != null && mTitleText != null) {
            mTitleTextView.setText(mTitleText);
        }
        return this;
    }

    public FancyAlertDialog setContentText (String text) {
        mContentText = text;
        if (mContentTextView != null && mContentText != null) {
            showContentText(true);
            mContentTextView.setText(mContentText);
        }
        return this;
    }

    public boolean isShowContentText () {
        return mShowContent;
    }

    public FancyAlertDialog showContentText (boolean isShow) {
        mShowContent = isShow;
        if (mContentTextView != null) {
            mContentTextView.setVisibility(mShowContent ? View.VISIBLE : View.GONE);
        }
        return this;
    }



    public boolean isShowCancelButton () {
        return mShowCancel;
    }

    public FancyAlertDialog showCancelButton (boolean isShow) {
        mShowCancel = isShow;
        if (mCancelButton != null) {
            mCancelButton.setVisibility(mShowCancel ? View.VISIBLE : View.GONE);
        }
        return this;
    }

    public String getConfirmText () {
        return mConfirmText;
    }

//    public FancyAlertDialog setConfirmText (String text) {
//        mConfirmText = text;
//        if (mConfirmButton != null && mConfirmText != null) {
//            mConfirmButton.setText(mConfirmText);
//        }
//        return this;
//    }

//    public FancyAlertDialog setCustomImage (Drawable drawable) {
//        mCustomImgDrawable = drawable;
//        if (mCustomImage != null && mCustomImgDrawable != null) {
//            mCustomImage.setVisibility(View.VISIBLE);
//            mCustomImage.setImageDrawable(mCustomImgDrawable);
//        }
//        return this;
//    }

//    public FancyAlertDialog setCustomImage (int resourceId) {
//        return setCustomImage(ContextCompat.getDrawable(mContext, resourceId));
//    }

    private void changeAlertType(int alertType, boolean fromCreate) {
        mAlertType = alertType;
        // call after created views
        if (mDialogView != null) {
            if (!fromCreate) {
                // restore all of views state before switching alert type
//                restore();
            }
            switch (mAlertType) {
                case NORMAL_TYPE:
                    break;
//                case ERROR_TYPE:
//                    mErrorFrame.setVisibility(View.VISIBLE);
//                    break;
//                case SUCCESS_TYPE:
//                    mSuccessFrame.setVisibility(View.VISIBLE);
//                    // initial rotate layout of success mask
//                    mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
//                    mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
//                    break;
//                case WARNING_TYPE:
//                    mConfirmButton.setBackgroundResource(R.drawable.red_button_background);
//                    mWarningFrame.setVisibility(View.VISIBLE);
//                    break;
//                case CUSTOM_IMAGE_TYPE:
//                    setCustomImage(mCustomImgDrawable);
//                    showCancelButton(true);
//                    break;
//                case PROGRESS_TYPE:
//                    mProgressFrame.setVisibility(View.VISIBLE);
//                    mConfirmButton.setVisibility(View.GONE);
//                    break;
            }
//            if (!fromCreate) {
//                playAnimation();
//            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_close) {
            dismiss();
//            if (mCancelClickListener != null) {
//                mCancelClickListener.onClick(FancyAlertDialog.this);
//            } else {
//                dismissWithAnimation();
//            }
//        } else if (view.getId() == R.id.confirm_button) {
//            //take to the petpace store
//            IntentUtils.startPetPaceShopActivity(mContext);
////            if (mConfirmClickListener != null) {
////                mConfirmClickListener.onClick(SweetAlertDialog.this);
////            } else {
////                dismissWithAnimation();
////            }
        }
    }
}
