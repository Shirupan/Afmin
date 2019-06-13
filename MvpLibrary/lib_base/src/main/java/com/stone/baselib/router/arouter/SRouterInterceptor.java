package com.stone.baselib.router.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.stone.baselib.utils.SLogUtils;

/**
 * Stone
 * 2019/6/12
 * 不需要调用，实现IInterceptor就会自动拦截
 * priority 优先级，值越小优先级越先被拦截
 **/
@Interceptor(priority = 7)
public class SRouterInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        callback.onContinue(postcard);//处理完成，交还控制权
    }

    @Override
    public void init(Context context) {
    }
}
