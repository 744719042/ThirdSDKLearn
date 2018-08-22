package com.example.thirdplatform.constant;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

public class CounterOutputStream extends OutputStream {
    private AtomicLong mAtomicLong = new AtomicLong();

    public long get() {
        return mAtomicLong.get();
    }

    @Override
    public void write(int b) throws IOException {
        mAtomicLong.incrementAndGet();
    }

    @Override
    public void write(@NonNull byte[] b) throws IOException {
        mAtomicLong.addAndGet(b.length);
    }

    @Override
    public void write(@NonNull byte[] b, int off, int len) throws IOException {
        mAtomicLong.addAndGet(len);
    }

    public void write(long fileSize) {
        mAtomicLong.addAndGet(fileSize);
    }
}
