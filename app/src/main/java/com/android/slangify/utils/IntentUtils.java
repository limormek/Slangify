package com.android.slangify.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.android.slangify.R;
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
    public static final String EXTRA_FILE_PATH_BACK = "EXTRA_FILE_PATH_BACK";
    public static final String EXTRA_FILE_PATH_FRONT = "EXTRA_FILE_PATH_FRONT";
    public static final String EXTRA_LANGUAGE = "EXTRA_LANGUAGE";

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
        context.startActivity(new Intent(context, CreateChallengeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    public static void startVideoCaptureActivity(Context context, PhraseModel phrase, String language) {
        context.startActivity(new Intent(context, CaptureVideoActivity.class)
        .putExtra(EXTRA_PHRASE, phrase)
        .putExtra(EXTRA_LANGUAGE, language));
    }

    public static void startDisplayVideoActivity(Context context, PhraseModel phrase, String filePath1, String filePath2, String language) {

        Intent intent = new Intent(context, DisplayVideoActivity.class)
                .putExtra(EXTRA_PHRASE, phrase)
                .putExtra(EXTRA_FILE_PATH_BACK, filePath1)
                .putExtra(EXTRA_FILE_PATH_FRONT, filePath2)
                .putExtra(EXTRA_LANGUAGE,language);
        context.startActivity(intent);
    }


    public static void shareVideoUri(Context context, Uri videoUri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
        shareIntent.setType("video/mp4");
        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.share_video_title)));
    }
}
