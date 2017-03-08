package utils;

import android.content.Context;
import android.content.Intent;

import com.android.slangify.ui.activities.CaptureVideoActivity;
import com.android.slangify.ui.activities.CreateMemeActivity;
import com.android.slangify.ui.activities.DisplayVideoActivity;
import com.android.slangify.ui.activities.FeedActivity;
import com.android.slangify.ui.activities.LoginActivity;

/**
 * Created by limormekaiten on 3/8/17.
 */

public class IntentUtils {

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
        context.startActivity(new Intent(context, CreateMemeActivity.class));
    }

    public static void startVideoCaptureActivity(Context context) {
        context.startActivity(new Intent(context, CaptureVideoActivity.class));
    }

    public static void startDisplayVideoActivity(Context context) {
        context.startActivity(new Intent(context, DisplayVideoActivity.class));
    }

}
