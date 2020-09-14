package com.qbase.huaweiability.common;

import android.os.Handler;
import android.os.Message;

/**
 * Created by qay
 * handler
 */
public class CommonHandler extends Handler {

    private IHandlerListener listener;

    public CommonHandler(IHandlerListener listener) {
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (listener != null) {
            listener.onHandlerMessage(msg);
        }
    }
}
