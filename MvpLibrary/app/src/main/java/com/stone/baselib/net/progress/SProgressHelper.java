package com.stone.baselib.net.progress;

import android.os.Handler;
import android.os.Looper;

import com.stone.baselib.utils.SEmptyCheckUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Stone
 * 2019/4/4
 **/

public class SProgressHelper {
    private final Map<String, Set<WeakReference<SProgressListener>>> requestListenerMap = new WeakHashMap<>();
    private final Map<String, Set<WeakReference<SProgressListener>>> responseListenerMap = new WeakHashMap<>();

    private static Handler mainHandler = new Handler(Looper.getMainLooper());

    private Interceptor interceptor;

    private SProgressHelper() {
        interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return wrapResponseBody(chain.proceed(wrapRequestBody(chain.request())));
            }
        };
    }

    public static SProgressHelper get() {
        return Holder.instance;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }


    public void addRequestListener(String url, SProgressListener listener) {
        if (SEmptyCheckUtils.check(url) || listener == null) return;

        Set<WeakReference<SProgressListener>> listeners = null;
        synchronized (SProgressHelper.class) {
            listeners = requestListenerMap.get(url);
            if (listeners == null) {
                listeners = new HashSet<>();
                requestListenerMap.put(url, listeners);
            }
            listeners.add(new WeakReference<SProgressListener>(listener));
        }
    }

    public void addResponseListener(String url, SProgressListener listener) {
        if (SEmptyCheckUtils.check(url) || listener == null) return;

        Set<WeakReference<SProgressListener>> listeners = null;
        synchronized (SProgressHelper.class) {
            listeners = responseListenerMap.get(url);
            if (listeners == null) {
                listeners = new HashSet<>();
                responseListenerMap.put(url, listeners);
            }
            listeners.add(new WeakReference<SProgressListener>(listener));
        }
    }

    public void delRequestListener(String url, SProgressListener listener) {
        if (SEmptyCheckUtils.check(requestListenerMap)) return;

        if (SEmptyCheckUtils.check(url)) {

            if (listener != null) {
                for (String key : requestListenerMap.keySet()) {
                    delReference(requestListenerMap.get(key), listener);
                }
            }
        } else {
            if (listener != null) {
                delReference(requestListenerMap.get(url), listener);
            } else {
                requestListenerMap.remove(url);
            }
        }
    }

    public void delResponseListener(String url, SProgressListener listener) {
        if (SEmptyCheckUtils.check(responseListenerMap)) return;

        if (SEmptyCheckUtils.check(url)) {

            if (listener != null) {
                for (String key : responseListenerMap.keySet()) {
                    delReference(responseListenerMap.get(key), listener);
                }
            }
        } else {
            if (listener != null) {
                delReference(responseListenerMap.get(url), listener);
            } else {
                responseListenerMap.remove(url);
            }
        }
    }

    public void clearAll() {
        requestListenerMap.clear();
        responseListenerMap.clear();
    }


    public static void dispatchProgressEvent(Set<WeakReference<SProgressListener>> listeners,
                                             final long soFarBytes, final long totalBytes) {
        if (!SEmptyCheckUtils.check(listeners)) {
            for (final WeakReference<SProgressListener> reference : listeners) {
                if (reference.get() != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            reference.get().onProgress(soFarBytes, totalBytes);
                        }
                    });
                }
            }
        }
    }

    public static void dispatchErrorEvent(Set<WeakReference<SProgressListener>> listeners,
                                          final Throwable throwable) {
        if (!SEmptyCheckUtils.check(listeners)) {
            for (final WeakReference<SProgressListener> reference : listeners) {
                if (reference.get() != null) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            reference.get().onError(throwable);
                        }
                    });
                }
            }
        }
    }

    private Request wrapRequestBody(Request request) {
        if (request == null || request.body() == null)
            return request;

        String key = request.url().toString();
        if (requestListenerMap.containsKey(key)) {
            Set<WeakReference<SProgressListener>> listeners = requestListenerMap.get(key);
            return request.newBuilder()
                    .method(request.method(), new SProRequestBody(request.body(), listeners))
                    .build();
        }
        return request;
    }

    private Response wrapResponseBody(Response response) {
        if (response == null || response.body() == null) return response;

        String key = response.request().url().toString();
        if (responseListenerMap.containsKey(key)) {
            Set<WeakReference<SProgressListener>> listeners = responseListenerMap.get(key);
            return response.newBuilder()
                    .body(new SProResponseBody(response.body(), listeners))
                    .build();
        }
        return response;
    }


    private void delReference(Set<WeakReference<SProgressListener>> references,
                              SProgressListener listener) {
        if (!SEmptyCheckUtils.check(references)) {
            for (WeakReference<SProgressListener> reference : references) {
                if (reference.get() != null && reference.get() == listener) {
                    references.remove(reference);
                }
            }
        }
    }


    private static class Holder {
        private static SProgressHelper instance = new SProgressHelper();
    }
}
