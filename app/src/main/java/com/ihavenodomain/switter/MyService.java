package com.ihavenodomain.switter;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class MyService extends IntentService {

    public MyService() {
        super("MyService");
    }

    public MyService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
        startJob();
    }

    private void startJob() {
        showNotification();
    }

    private void showNotification() {

    }
}
