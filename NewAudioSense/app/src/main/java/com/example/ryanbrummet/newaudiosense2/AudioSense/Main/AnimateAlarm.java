package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Handler;
import android.util.Log;



/*

Credit to authors of first audioSense
 */
public class AnimateAlarm {
    //private static final SharedPreferences;
    private static int FLASH_PERIOD = 250;
    private static int FLASH_COUNT = 240;
    protected int flashes;
    protected Camera camera = null;

    private Runnable flashTask = new Runnable() {
        @Override
        public void run() {
            if (camera == null)
                return;
            if (flashes > 0) {
                if (flashes % 2 == 0) {
                    Parameters params = camera.getParameters();
                    params.setFlashMode(Parameters.FLASH_MODE_TORCH);

                    camera.setParameters(params);
                    camera.startPreview();
/*
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            camera.autoFocus(new Camera.AutoFocusCallback() {
                                public void onAutoFocus(boolean success, Camera camera) {
                                }
                            });
                        }
                    }, 200);*/
                    //view.setBackgroundColor(0xffff0000);
                } else {
                    Parameters params = camera.getParameters();
                    params.setFlashMode(Parameters.FLASH_MODE_OFF);
                    camera.setParameters(params);
                    //view.setBackgroundColor(0);
                }
                flashes = flashes - 1;
                flashTimer.postDelayed(this, FLASH_PERIOD);
            } else {
                stopAlarm();
            }
        }
    };
    private Handler flashTimer;
    private Drawable originalBackground;

    // http://stackoverflow.com/questions/8876843/led-flashlight-on-galaxy-nexus-controllable-by-what-api
    public AnimateAlarm(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        flashes = FLASH_COUNT * preferences.getInt("surveyTimeout",1);

        try {
            Log.e("Camera","started");
            camera = Camera.open();
            //SurfaceView preview = (SurfaceView) view.findViewById(R.id.PREVIEW);
            //SurfaceHolder mHolder = preview.getHolder();
            //mHolder.addCallback(cameraCallback);

            flashTimer = new Handler();
            flashTimer.postDelayed(flashTask, FLASH_PERIOD);
            //originalBackground = view.getBackground();

        } catch (RuntimeException e) {
            camera = null;
        }
    }

    public void stopAlarm() {
        flashes = 0;
        if (camera != null) {
            Log.e("camera","stopped");
            Parameters params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

        //view.setBackgroundDrawable(originalBackground);
    }

}
