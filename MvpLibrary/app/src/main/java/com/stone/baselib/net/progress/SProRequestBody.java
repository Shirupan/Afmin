package com.stone.baselib.net.progress;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * Stone
 * 2019/4/4
 **/
public class SProRequestBody extends RequestBody {
    private RequestBody delegate;
    private BufferedSink bufferedSink;
    private Set<WeakReference<SProgressListener>> listeners;

    public SProRequestBody(RequestBody delegate, Set<WeakReference<SProgressListener>> listeners) {
        this.delegate = delegate;
        this.listeners = listeners;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(new ProgressSink(sink));
        }
        try {
            delegate.writeTo(bufferedSink);
            bufferedSink.flush();
        } catch (IOException e) {
            e.printStackTrace();
            SProgressHelper.dispatchErrorEvent(listeners, e);
            throw e;
        }
    }

    final class ProgressSink extends ForwardingSink {
        private long soFarBytes = 0;
        private long totalBytes = -1;

        public ProgressSink(okio.Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(Buffer source, long byteCount) {
            try {
                super.write(source, byteCount);
            } catch (Exception e) {
                SProgressHelper.dispatchErrorEvent(listeners, e);
            }

            if (totalBytes < 0) {
                totalBytes = contentLength();
            }
            soFarBytes += byteCount;

            SProgressHelper.dispatchProgressEvent(listeners, soFarBytes, totalBytes);
        }
    }
}
