package com.ihavenodomain.switter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

// WakefulBroadcastReceiver ensures the device does not go back to sleep
// during the startup of the service
public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Launch the specified service when this message is received
        Intent startServiceIntent = new Intent(context, MyService.class);
        startWakefulService(context, startServiceIntent);
    }
}
