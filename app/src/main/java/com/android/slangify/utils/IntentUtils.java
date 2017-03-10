package com.android.slangify.utils;

import android.content.Context;
import android.content.Intent;

import com.android.slangify.repository.models.PhraseModel;
import com.android.slangify.ui.activities.CaptureVideoActivity;
import com.android.slangify.ui.activities.CreateChallengeActivity;
import com.android.slangify.ui.activities.DisplayVideoActivity;
import com.android.slangify.ui.activities.FeedActivity;
import com.android.slangify.ui.activities.LoginActivity;

/**
 * Created by limormekaiten on 3/8/17.
 */

public class IntentUtils {

    public static final String EXTRA_PHRASE= "EXTRA_PHRASE";

    /**
     * Start the feed activity on a new stack
     * @param context
     */
    public static void startFeedActivity(Context context) {
        context.startActivity(new Intent(context, FeedActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public static void startLoginActivity(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void startCreateActivity(Context context) {
        context.startActivity(new Intent(context, CreateChallengeActivity.class));
    }

    public static void startVideoCaptureActivity(Context context, PhraseModel phrase) {
        context.startActivity(new Intent(context, CaptureVideoActivity.class)
        .putExtra(EXTRA_PHRASE, phrase));
    }

    public static void startDisplayVideoActivity(Context context, String sourceText,
                                                 String translationText, String filePath) {

        Intent intent = new Intent(context, DisplayVideoActivity.class);
        intent.putExtra("sourceText", sourceText);
        intent.putExtra("transText", translationText);
        intent.putExtra("filePath", filePath);
        context.startActivity(intent);
    }

}
