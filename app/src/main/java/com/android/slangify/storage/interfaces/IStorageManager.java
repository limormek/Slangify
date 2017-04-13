package com.android.slangify.storage.interfaces;


/**
 * Created by limormekaiten on 4/8/17.
 */

public interface IStorageManager {

    //upload
    void uploadVideo(String videoPath, IStorageUploadCallback uploadCallback);

    void uploadThumbnail(String thumbPath, String challengeId, IStorageUploadCallback uploadCallback);
}
