package com.stone.baselib;

/**
 * Stone
 * 2019/4/3
 **/
public class SConfig {

    //是否开启应用调试模式
    public static boolean ISDEBUG = false;

    //是否开启日志调试模式
    public static boolean ISLOGDEBUG = true;

    //日志标识
    public static String LOGTAG = "Stone";

    //SharedPreferences存储名称
    public static String SP_NAME = "PainStone";

    //imageloader
    public static int RES_NONE = -1;
    public static int IMAGE_LOADING_RES = RES_NONE;
    public static int IMAGE_ERROR_RES = RES_NONE;

    //activity转场动画
    public static int ROUTER_ENTER_ANIM = RES_NONE;
    public static int ROUTER_EXIT_ANIM = RES_NONE;

}
