package com.example.countsksh.helpers;

import android.os.Environment;

public class StaticsVariablesValue {
    public static final int CAMERA_REQUEST_COD=1000;
    public static final int CAMERA_REQUEST_COD_EDit=10001;
    public static final int CAMERA_REQUEST_COD_Add_Account=10002;
//String imagePath = Environment.getExternalStorageDirectory() + File.separator + "your_image.jpg";
    public static final String IMAGES_FOLDER_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+"/PicsFolder";

    public static final int CALL_REQUEST_COD=1001;

    public static final int MANAGE_STORAGE_REQUEST_PERMISSION_COD=1002;
    public static final int READ_STORAGE_REQUEST_PERMISSION_COD=10020;
    public static final int WRITE_STORAGE_REQUEST_PERMISSION_COD=10030;

    public static final int CAMERA_REQUEST_PERMISSION_COD=1011;

    public static final int CALL_REQUEST_PERMISSION_COD=1012;
    public static final int STORAGE_REQUEST_PERMISSION_COD=1013;

    public static final int DB_REQUEST_COD=1331;

}
