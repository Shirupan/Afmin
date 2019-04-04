package com.stone.baselib.net.progress;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Stone
 * 2019/4/4
 **/
public class SProResponseBody extends ResponseBody {
    private ResponseBody delegate;
    private BufferedSource bufferedSource;
    private Set<WeakReference<SProgressListener>> listeners;

    public SProResponseBody(ResponseBody delegate, Set<WeakReference<SProgressListener>> listeners) {
        this.delegate = delegate;
        this.listeners = listeners;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        return delegate.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(new ProgressSource(delegate.source()));
        }
        return bufferedSource;
    }


    final class ProgressSource extends ForwardingSource {
        private long soFarBytes = 0;
        private long totalBytes = -1;

        public ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {
            long bytesRead = 0L;
            try {
                bytesRead = super.read(sink, byteCount);

                if (totalBytes < 0) {
                    totalBytes = contentLength();
                }
                soFarBytes += (bytesRead != -1 ? bytesRead : 0);

                SProgressHelper.dispatchProgressEvent(listeners, soFarBytes, totalBytes);
            } catch (IOException e) {
                SProgressHelper.dispatchErrorEvent(listeners, e);
                throw e;
            }

            return bytesRead;
        }
    }
}
