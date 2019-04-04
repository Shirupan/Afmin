package com.stone.baselib.net.subscriber;

import android.app.Application;
import android.content.Context;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.painstone.mvplibrary.R;
import com.stone.baselib.net.SHttpUtils;
import com.stone.baselib.net.SModel;
import com.stone.baselib.net.SNetError;
import com.stone.baselib.utils.SLogUtils;
import com.stone.baselib.utils.SNetworkUtils;

import org.json.JSONException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Stone
 * 2019/4/4
 **/
public abstract class SNetSubscriber<T extends SModel> extends ResourceSubscriber<T> {

    public static final String TAG = "SNetSubscriber";

    @Override
    public void onError(Throwable e) {
        SNetError error;
        String detailMessage;
        int code;
        //TODO 获取context并释放代码
//        Context context = Application.getContext();
//        if (e instanceof SNetError) {
//            error = (SNetError) e;
//        } else {
//            if (e instanceof UnknownHostException) {
//                detailMessage = context.getString(R.string.net_host_error);
//                code = SNetError.NoConnectError;
//            } else if (e instanceof JSONException || e instanceof JsonParseException || e instanceof JsonSyntaxException) {
//                detailMessage = context.getString(R.string.net_date_error);
//                code = SNetError.ParseError;
//            } else if (e instanceof SocketTimeoutException) {
//                detailMessage = context.getString(R.string.net_time_out_error);
//                code = SNetError.SocketTimeOut;
//            } else {
//                detailMessage = context.getString(R.string.net_time_out_error);
//                code = SNetError.OtherError;
//            }
//            e.printStackTrace();
//            error = new SNetError(detailMessage, code);
//            SLogUtils.d(TAG, error.getMessage());
//        }
//        if (useCommonErrorHandler() && SHttpUtils.getCommonProvider() != null) {
//            if (SHttpUtils.getCommonProvider().handleError(error)) {        //使用通用异常处理
//                return;
//            }
//        }
//        onFail(error);
    }

    protected abstract void onFail(SNetError error);

    @Override
    public void onComplete() {

    }

    protected boolean useCommonErrorHandler() {
        return false;
    }
}
