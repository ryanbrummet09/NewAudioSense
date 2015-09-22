package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by ryanbrummet on 9/5/15.
 *
 * credit Billie: http://stackoverflow.com/questions/3474567/problem-acquiring-wake-lock-from-broadcast-receiver
 */
public class AlarmAlertWakeLock {

    private static PowerManager.WakeLock sCpuWakeLock;

    private static boolean audioLock = false;

    public static void acquireCpuWakeLock(Context context) {

        if (sCpuWakeLock != null) {
            return;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        sCpuWakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP,"okTag");
        sCpuWakeLock.acquire();
    }

    public static void releaseCpuLock() {
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }
}
