package com.stone.baselib.net;

import com.stone.baselib.net.progress.SProgressHelper;
import com.stone.baselib.utils.SEmptyCheckUtils;

import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Stone
 * 2019/4/4
 **/

public class SHttpUtils {

    public static final long connectTimeout = 15; //链接超时时间，单位为秒
    public static final long readTimeout = 15; //数据返回超时时间，单位为秒

//    private Map<String, SNetProvider> providerMap = new HashMap<>();
//    private Map<String, Retrofit> retrofitMap = new HashMap<>();
//    private Map<String, OkHttpClient> clientMap = new HashMap<>();

    private static SHttpUtils instance;
    private static SNetProvider sProvider = null;

    private SHttpUtils() {

    }

    public static SHttpUtils getInstance() {
        if (instance == null) {
            synchronized (SHttpUtils.class) {
                if (instance == null) {
                    instance = new SHttpUtils();
                }
            }
        }
        return instance;
    }

    public static <S> S get(String baseUrl, Class<S> service) {
        return getInstance().getRetrofit(baseUrl, true).create(service);
    }

    public static void registerProvider(SNetProvider provider) {
        sProvider = provider;
    }

//    public static void registerProvider(String baseUrl, NetProvider provider) {
//        getInstance().providerMap.put(baseUrl, provider);
//    }

    public Retrofit getRetrofit(String baseUrl, boolean useRx) {
        return getRetrofit(baseUrl, null, useRx);
    }

    //传入provider用此provider并更新map中对应url的provider，未传入则取map中provider，map中没有则取默认，默认没有则抛异常
    //不适合拦截器、cookie、头部信息字段会变化的请求
//    public Retrofit getRetrofit(String baseUrl, SNetProvider provider, boolean useRx) {
//        if (Kits.Empty.check(baseUrl)) {
//            throw new IllegalStateException("baseUrl can not be null");
//        }
//        //若此url的client有被保存在map里，则直接返回client对象，provider设置无法更新
//        if (retrofitMap.get(baseUrl) != null){
//            return retrofitMap.get(baseUrl);
//        }
//        //若传入provider为空，则判断map中此url有没有provider。若没有则使用默认provider。
//        if (provider == null) {
//            provider = providerMap.get(baseUrl);
//            if (provider == null) {
//                provider = sProvider;
//            }
//        }
//        //provider为空抛出异常
//        checkProvider(provider);
//        //只有在map传入url没有缓存的情况下才创建新对象，导致provider无法更新
//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .client(getClient(baseUrl, provider))
//                .addConverterFactory(GsonConverterFactory.create());
//        if (useRx) {
//            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
//        }
//
//        Retrofit retrofit = builder.build();
//        retrofitMap.put(baseUrl, retrofit);//将此url的retrofit存入map
//        providerMap.put(baseUrl, provider);//将此url的provider存入map
//
//        return retrofit;
//    }

    //可在外部获取Retrofit对象时缓存对应的Retrofit对象
    public Retrofit getRetrofit(String baseUrl, SNetProvider provider, boolean useRx) {
        checkUrl(baseUrl);

        //若没有provider则使用默认。
        if(provider==null){
            provider = sProvider;
        }
        //provider为空抛出未设置异常
        checkProvider(provider);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl, provider))
                .addConverterFactory(GsonConverterFactory.create());
        if (useRx) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }

        return builder.build();

    }

    private OkHttpClient getClient(String baseUrl, SNetProvider provider) {
        checkUrl(baseUrl);

        checkProvider(provider);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //判断provider有没有设置超时，没有则使用默认
        builder.connectTimeout(provider.configConnectTimeoutSecond() != 0
                ? provider.configConnectTimeoutSecond()
                : connectTimeout, TimeUnit.SECONDS);
        builder.readTimeout(provider.configReadTimeoutSecond() != 0
                ? provider.configReadTimeoutSecond()
                : readTimeout, TimeUnit.SECONDS);

        CookieJar cookieJar = provider.configCookie();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        provider.configHttps(builder);

        SRequestHandler handler = provider.configHandler();
        if (handler != null) {
            builder.addInterceptor(new SInterceptor(handler));
        }

        if (provider.dispatchProgressEnable()) {
            builder.addInterceptor(SProgressHelper.get().getInterceptor());
        }

        Interceptor[] interceptors = provider.configInterceptors();
        if (!SEmptyCheckUtils.check(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        if (provider.configLogEnable()) {
            SLogInterceptor logInterceptor = new SLogInterceptor();
            builder.addInterceptor(logInterceptor);
        }

        return builder.build();
    }

    private void checkUrl(String baseUrl) {
        //TODO
        if (SEmptyCheckUtils.check(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }
    }

    private void checkProvider(SNetProvider provider) {
        if (provider == null) {
            throw new IllegalStateException("must register provider first");
        }
    }

    public static SNetProvider getCommonProvider() {
        return sProvider;
    }

//    public Map<String, Retrofit> getRetrofitMap() {
//        return retrofitMap;
//    }
//
//    public Map<String, OkHttpClient> getClientMap() {
//        return clientMap;
//    }
//
//    public static void clearCache() {
//        getInstance().retrofitMap.clear();
//        getInstance().clientMap.clear();
//    }

    /**
     * 线程切换
     *
     * @return
     */
    public static <T extends SModel> FlowableTransformer<T, T> getScheduler() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 异常处理变换
     *
     * @return
     */
    public static <T extends SModel> FlowableTransformer<T, T> getApiTransformer() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.flatMap(new Function<T, Publisher<T>>() {
                    @Override
                    public Publisher<T> apply(T model) throws Exception {
                        if (model == null || model.isNull()) {
                            return Flowable.error(new SNetError(model.getErrorMsg(), SNetError.NoDataError));
                        } else if (model.isAuthError()) {
                            return Flowable.error(new SNetError(model.getErrorMsg(), SNetError.AuthError));
                        } else if (model.isBusinessError()) {
                            return Flowable.error(new SNetError(model.getErrorMsg(), SNetError.BusinessError));
                        } else {
                            return Flowable.just(model);
                        }
                    }
                });
            }
        };
    }

//    public static <T extends BaseModel> FlowableTransformer<T, T> getNoData() {
//        return new FlowableTransformer<T, T>() {
//            @Override
//            public Publisher<T> apply(Flowable<T> upstream) {
//                return upstream.flatMap(new Function<T, Publisher<T>>() {
//                    @Override
//                    public Publisher<T> apply(T model) throws Exception {
//                        if (model != null && model.getCode() == 0) {
//                            return Flowable.just(model);
//                        } else {
//                            return Flowable.error(new SNetError(model.getMessage(), model.getCode()));
//                        }
//
//                    }
//                });
//            }
//        };
//    }
}
