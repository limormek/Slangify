package com.android.slangify.utils;

import java.io.File;

/**
 * Created by avishai on 3/25/2017.
 */

public class FilesManager {

    //check if file exists, if so, add integer to name
    public static String getFilePath(String filePath){

        File f = new File(filePath);
        String folder = f.getParent();
        String fileName = f.getName();

        Integer counter = 1;
        while(f.exists()){
            String nameWithNoExtension = fileName.substring(0, fileName.lastIndexOf("."));
            nameWithNoExtension += counter.toString();

            f = new File(folder + "/" + nameWithNoExtension + ".mp4");
            counter++;
        }

        return f.getPath();
    }
}
