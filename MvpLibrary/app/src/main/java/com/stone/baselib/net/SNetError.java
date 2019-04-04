package com.stone.baselib.net;

/**
 * Stone
 * 2019/4/4
 **/
public class SNetError extends Exception {

    public static final int ParseError = 0;   //数据解析异常
    public static final int NoConnectError = 1;   //连接异常
    public static final int AuthError = 2;   //用户验证异常
    public static final int NoDataError = 3;   //无数据返回异常
    public static final int BusinessError = 4;   //业务异常
    public static final int OtherError = 5;   //其他异常
    public static final int SocketTimeOut = 6;   //链接超时

    private Throwable exception;
    private int type = NoConnectError;

    public SNetError(Throwable exception, int type) {
        this.exception = exception;
        this.type = type;
    }

    public SNetError(String detailMessage, int type) {
        super(detailMessage);
        this.type = type;
    }

    public SNetError(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public String getMessage() {
        if (exception != null){
            return exception.getMessage();
        }
        return super.getMessage();
    }

    public int getType() {
        return type;
    }

}
