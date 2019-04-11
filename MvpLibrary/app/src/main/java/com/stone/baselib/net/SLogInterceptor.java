package com.stone.baselib.net;

import android.util.Log;

import com.stone.baselib.utils.SLogUtils;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Stone
 * 2019/4/4
 **/
public class SLogInterceptor implements Interceptor {
    public static final String TAG = "SHttpFactory";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logRequest(request);
        Response response = chain.proceed(request);
        return logResponse(response);
    }


    private void logRequest(Request request) {
        StringBuffer log = new StringBuffer();
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            if (headers != null && headers.size() > 0) {
                log.append("\nmethod : " + request.method())
                        .append("\nurl : " + url)
                        .append("\nheaders : " + headers.toString());
            } else {
                log.append("\nmethod : " + request.method())
                        .append("\nurl : " + url);
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        log.append("\nparams : " + URLDecoder.decode(bodyToString(request), "UTF-8"));
                    } else {
                        log.append("\nparams : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            SLogUtils.d(TAG, log.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response logResponse(Response response) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        String resp = body.string();
                        SLogUtils.d(TAG, "body:"+resp);

                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    } else {
                        SLogUtils.d(TAG, "data : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    private boolean isText(MediaType mediaType) {
        if (mediaType == null) return false;

        return ("text".equals(mediaType.subtype())
                || "json".equals(mediaType.subtype())
                || "xml".equals(mediaType.subtype())
                || "html".equals(mediaType.subtype())
                || "webviewhtml".equals(mediaType.subtype())
                || "x-www-form-urlencoded".equals(mediaType.subtype()));
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
