package com.android.slangify.storage.services;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.slangify.storage.IStorageManager;
import com.android.slangify.storage.IStorageUploadCallback;
import com.android.slangify.storage.FirebaseStorageManager;
import com.android.slangify.utils.IntentUtils;

/**
 * Created by limormekaiten on 4/8/17.
 */

public class UploadService extends BaseTaskService {

    public static final String TAG = UploadService.class.getName();

    /** Intent Actions **/
    public static final String ACTION_UPLOAD = "action_upload";
    public static final String UPLOAD_COMPLETED = "upload_completed";
    public static final String UPLOAD_ERROR = "upload_error";

    /** Intent Extras **/
    public static final String EXTRA_FILE_URI = "extra_file_uri";
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";


    private IStorageManager mStorageManager;


    @Override
    public void onCreate() {
        super.onCreate();

        mStorageManager = new FirebaseStorageManager();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mStorageManager == null) {
            mStorageManager = new FirebaseStorageManager();
        }

        Log.d(TAG, "onStartCommand:" + intent + ":" + startId);
        if (ACTION_UPLOAD.equals(intent.getAction())) {
            String filePath = intent.getStringExtra(IntentUtils.EXTRA_FILE_PATH);
            if(!TextUtils.isEmpty(filePath)){

                uploadFromUri(filePath);
            }
        }

        return START_REDELIVER_INTENT;
    }

    // [START upload_from_uri]
    private void uploadFromUri(String filePath) {
        Log.d(TAG, "uploadFromUri:src:" + filePath);

        // [START_EXCLUDE]
        taskStarted();
//        showProgressNotification(getString(R.string.progress_uploading), 0, 0);
        // [END_EXCLUDE]

        // Upload file to Firebase Storage


        mStorageManager.uploadVideo(filePath, new IStorageUploadCallback() {
            @Override
            public void onSuccess(Uri downloadUri) {
                // Upload succeeded
                Log.d(TAG, "uploadFromUri:onSuccess");


                // [START_EXCLUDE]
//                broadcastUploadFinished(downloadUri, fileUri);
//                showUploadFinishedNotification(downloadUri, fileUri);

                //todo - upload all the challenge data to the DB


                taskCompleted();
                // [END_EXCLUDE]
            }

            @Override
            public void onError(Exception exception) {
                // Upload failed
                Log.w(TAG, "uploadFromUri:onFailure", exception);

                // [START_EXCLUDE]
//                broadcastUploadFinished(null, fileUri);
//                showUploadFinishedNotification(null, fileUri);
                taskCompleted();
                // [END_EXCLUDE]
            }
        });

        // [START get_child_ref]

    }
    // [END upload_from_uri]


//    /**
//     * Broadcast finished upload (success or failure).
//     * @return true if a running receiver received the broadcast.
//     */
//    private boolean broadcastUploadFinished(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
//        boolean success = downloadUrl != null;
//
//        String action = success ? UPLOAD_COMPLETED : UPLOAD_ERROR;
//
//        Intent broadcast = new Intent(action)
//                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
//                .putExtra(EXTRA_FILE_URI, fileUri);
//        return LocalBroadcastManager.getInstance(getApplicationContext())
//                .sendBroadcast(broadcast);
//    }
//
//    /**
//     * Show a notification for a finished upload.
//     */
//    private void showUploadFinishedNotification(@Nullable Uri downloadUrl, @Nullable Uri fileUri) {
//        // Hide the progress notification
//        dismissProgressNotification();
//
//        // Make Intent to MainActivity
//        Intent intent = new Intent(this, MainActivity.class)
//                .putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
//                .putExtra(EXTRA_FILE_URI, fileUri)
//                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        boolean success = downloadUrl != null;
//        String caption = success ? getString(R.string.upload_success) : getString(R.string.upload_failure);
//        showFinishedNotification(caption, intent, success);
//    }
//
//    public static IntentFilter getIntentFilter() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(UPLOAD_COMPLETED);
//        filter.addAction(UPLOAD_ERROR);
//
//        return filter;
//    }
}
