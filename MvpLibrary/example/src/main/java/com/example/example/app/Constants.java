package com.example.example.app;

import android.os.Environment;

import com.example.example.R;
import com.stone.baselib.SConfig;

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
    public static final String SP_EXIT_TIME = "spExitTime";

    public static final String FILE_DIR = Environment.getExternalStorageDirectory()+ File.separator+"stone"+File.separator;

    public static void init(){
        SConfig.ISLOGDEBUG = true;
//        //日志标识
        SConfig.LOGTAG = "Stone123";
//        //SharedPreferences存储名称
//        SConfig.SP_NAME = "PainStone";
//        //imageloader
//        SConfig.IMAGE_LOADING_RES = SConfig.RES_NONE;
//        SConfig.IMAGE_ERROR_RES = SConfig.RES_NONE;
//        //activity转场动画
//        SConfig.ROUTER_ENTER_ANIM = R.anim.s_left_in;
//        SConfig.ROUTER_EXIT_ANIM = R.anim.s_right_out;
    }

}
