package com.android.slangify.storage.implementation;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.slangify.storage.interfaces.IStorageManager;
import com.android.slangify.storage.interfaces.IStorageUploadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

/**
 * Created by limormekaiten on 4/8/17.
 */

public class FirebaseStorageManager implements IStorageManager {

    private static final String TAG = FirebaseStorageManager.class.getName();
    private static final String VIDEOS_FOLDER = "videos";
    private static final String THUMB_FOLDER = "thumbnails";

    FirebaseStorage mStorage;

    public FirebaseStorageManager() {
        mStorage = FirebaseStorage.getInstance();
    }

    @Override
    public void uploadVideo(String videoPath, final IStorageUploadCallback uploadCallback) {
        StorageReference storageRef = mStorage.getReference();

        StorageReference videoReference = storageRef.child(VIDEOS_FOLDER).child(videoPath);

        //upload the local file
        Uri file = Uri.fromFile(new File(videoPath));

        Log.d(TAG, "uploadFromUri:dst:" + videoReference.getPath());

        UploadTask uploadTask = videoReference.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                uploadCallback.onError(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                uploadCallback.onSuccess(downloadUrl);
            }
        });
    }

    @Override
    public void uploadThumbnail(String thumbPath, String challengeId, IStorageUploadCallback uploadCallback) {

    }
}
