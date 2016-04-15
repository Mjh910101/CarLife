package com.itkc_carlife.tool;

import android.os.Handler;
import android.os.Message;


public class PPTFlish implements Runnable {

    private boolean isRun = true;
    private boolean isFirst;

    private Handler handler;

    public final static int FLISHFORPPT = 1000;
    public final static long INDEXPPTTIME = 1000 * 5;

    public PPTFlish(Handler handler) {
        this.handler = handler;
        isFirst = true;
    }

    @Override
    public void run() {
        while (isRun) {
            if (isRun) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    Message.obtain(handler, FLISHFORPPT)
                            .sendToTarget();
                }
            }
            try {
                Thread.sleep(INDEXPPTTIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRun = false;
    }

}
