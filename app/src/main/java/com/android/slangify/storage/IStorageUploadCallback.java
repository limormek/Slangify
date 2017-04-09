package com.android.slangify.storage;

import android.net.Uri;

/**
 * Created by limormekaiten on 4/8/17.
 */

public interface IStorageUploadCallback {

    void onSuccess(Uri downloadUri);

    void onError(Exception exception);
}
