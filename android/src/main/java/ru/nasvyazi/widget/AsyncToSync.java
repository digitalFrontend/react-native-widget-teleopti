package ru.nasvyazi.widget;

import android.content.Context;

public class AsyncToSync {

    private Object lock = new Object();
    private boolean ready = false;
    private Data data = null;

    public Data syncCallToAsyncMethod(Context context) {

        DBTools.getDataFromDB(context, new DBToolsGetCallback() {
            @Override
            public void onSuccess(Data newData) {
                synchronized (lock) {
                    data = newData;
                    ready = true;
                    lock.notifyAll();
                }
            }
        });

        synchronized (lock) {
            while (!ready) {
                try {
                    lock.wait();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return data;

    }
}
