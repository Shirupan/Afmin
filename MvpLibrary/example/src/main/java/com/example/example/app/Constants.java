package com.example.example.app;

import android.os.Environment;

import java.io.File;

/**
 * Stone
 * 2019/4/19
 **/
public class Constants {
    //    encode
    public static final int ENCODE_SHA = 1;

    //    BusEvent
    public static final int EVENT_TEST_INT = 1;
    public static final int EVENT_TEST_STR = 2;
    public static final int EVENT_BASE_INT = 3;
    public static final int EVENT_BASE_STR = 4;

//    sp
    public static final String SP_EXIT_TIME = "sp exit time";

    public static final String FILE_DIR = Environment.getExternalStorageDirectory()+ File.separator+"stone"+File.separator;

}
