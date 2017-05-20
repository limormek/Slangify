package com.android.slangify.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.android.slangify.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by limormekaiten on 4/15/17.
 */

public class IOUtils {

    private static String TAG = IOUtils.class.getName();

    public static String getSlangifyInternalPath(Context context) throws StorageUnavailableException {
        File slangifyInternalDir = getSlangifyInternalDir(context);
        if (slangifyInternalDir != null) {
            return slangifyInternalDir.getAbsolutePath();
        } else {
            return "";
        }
    }

    public static File getSlangifyInternalDir(Context context) throws StorageUnavailableException {
        File filesDir = context.getFilesDir();
        if (filesDir == null) {
            throw new StorageUnavailableException("unable to write to internal storage");
        }

        try {
            File test = new File(filesDir, "test");
            test.createNewFile();
            test.delete();
        } catch (IOException e) {
            throw new StorageUnavailableException("unable to write to internal storage");
        }
        return filesDir;
    }

    public static String getSlangifyDirectoryPath(Context context) throws StorageUnavailableException {

        //check in Shared Preferences first
        String result = SharedPreferencesUtils.getSlangifyDirectoryPath(context);
        if(result != "")
            return result;

        File dir = new File(getStorageDir(context), context.getResources().getString(R.string.app_name));
        if (!dir.exists()) {

            if (!dir.mkdirs()) {
                Log.d(TAG, "getSlangifyDirectoryPath: failed creating the Slangify Images directory");
                return null;
            }

            dir.setReadable(true, false);
        }

        result = dir.getAbsolutePath();
        SharedPreferencesUtils.setSlangifyDirectoryPath(context, result);

        return result;
    }

    private static File getStorageDir(Context context) throws StorageUnavailableException {
        if (isExternalStorageWritable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            File filesDir = context.getFilesDir();
            if (filesDir == null) {
                throw new StorageUnavailableException("unable to write to internal storage");
            }

            try {
                File test = new File(filesDir, "test");
                test.createNewFile();
                test.delete();
            } catch (IOException e) {
                throw new StorageUnavailableException("unable to write to internal storage");
            }
            return filesDir;
        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    public static class StorageUnavailableException extends Exception {
        public StorageUnavailableException(String s) {

        }
    }
}
