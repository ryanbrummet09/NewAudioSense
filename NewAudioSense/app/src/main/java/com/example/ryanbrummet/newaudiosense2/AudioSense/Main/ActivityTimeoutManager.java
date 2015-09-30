package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ryanbrummet on 9/25/15.
 */
public class ActivityTimeoutManager extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("APP_TIMEOUT"));
    }
}
