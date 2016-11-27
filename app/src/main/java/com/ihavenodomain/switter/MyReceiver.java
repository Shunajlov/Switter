package com.ihavenodomain.switter;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


public class MyReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MyService.class);
        Log.i("MyService", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }
}
