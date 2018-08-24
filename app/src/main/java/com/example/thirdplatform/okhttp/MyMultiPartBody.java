package com.example.thirdplatform.okhttp;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

public class MyMultiPartBody extends RequestBody {
    private MultipartBody multipartBody;
    private long mTotalCount = 0;
    private long mCurrentCount = 0;
    private MyProgressListener mListener;

    public MyMultiPartBody(MultipartBody multipartBody) {
        this.multipartBody = multipartBody;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return multipartBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mTotalCount <= 0) {
            mTotalCount = multipartBody.contentLength();
        }
        ForwardingSink forwardingSink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                mCurrentCount += byteCount;
                if (mListener != null) {
                    mListener.onProgress(mCurrentCount * 1.0f / mTotalCount);
                }
            }
        };

        BufferedSink bufferedSink = Okio.buffer(forwardingSink);
        multipartBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    public void setListener(MyProgressListener listener) {
        this.mListener = listener;
    }
}
