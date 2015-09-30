package com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.os.Handler;

import java.io.RandomAccessFile;

/**
 * Created by ryanbrummet on 9/18/15.
 */
public class LogUsage {

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

    public LogUsage(Context context) {
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

        Log.e("SYSTEM USAGE", "RAM USED: " + Double.toString(percentAvail) + "; HEAP USED: " + Double.toString(percentHeapUsedMB) + "; " + "OS LOW MEM STATE: " + Boolean.toString(mi.lowMemory) + "; CPU: " + Float.toString(readUsage()));

    }

    private float readUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" +");  // Split on one or more spaces

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    public void startLoggingMemUsage() {
        runnable.run();
    }

    public void stopLoggingMemUsage() {
        handler.removeCallbacks(runnable);
    }


}
