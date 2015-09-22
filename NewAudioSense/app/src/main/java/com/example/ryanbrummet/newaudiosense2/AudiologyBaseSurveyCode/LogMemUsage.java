package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.os.Handler;

/**
 * Created by ryanbrummet on 9/18/15.
 */
public class LogMemUsage {

    private final Handler handler;
    private final Context context;
    private Runnable runnable = new Runnable()
    {

        public void run()
        {
            printMemToLog();
            handler.postDelayed(this, 1000);
        }
    };

    public LogMemUsage(Context context) {
        this.handler = new Handler();
        this.context = context;
    }

    public void printMemToLog(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double percentAvail = 100.0 * mi.availMem / mi.totalMem;

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;

        double percentHeapUsedMB = 100.0 * usedMemInMB / maxHeapSizeInMB;

        Log.e("MEMORY USAGE", "RAM USED: " + Double.toString(percentAvail) + "; HEAP USED: " + Double.toString(percentHeapUsedMB) + "; " + "OS LOW MEM STATE: " + Boolean.toString(mi.lowMemory));

    }

    public void startLoggingMemUsage() {
        runnable.run();
    }

    public void stopLoggingMemUsage() {
        handler.removeCallbacks(runnable);
    }


}
