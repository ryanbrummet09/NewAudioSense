package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.app.Service;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Bluetooth.DiscoverBlueToothDevices;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.LogMemUsage;
import com.example.ryanbrummet.newaudiosense2.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ryanbrummet on 9/7/15.
 */
public class AudioSurveySampleService  extends Service{

    private String fileOutputLocation;

    private AudioRecord recorder = null;
    private static boolean canStopAlarm;
    private final int RECORDER_SAMPLERATE = 44100;
    private final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final int BUFFER_SIZE_IN_BYTES = 8192;
    private boolean isRecording = false;
    private Thread recordingThread = null;
    private Timer timer;
    private int surveyTimeout;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private AudioSenseAlarmManager alarmManager;
    private AnimateAlarm animateAlarm;
    private Vibrator vibrator;
    private final LogMemUsage logMemUsage = new LogMemUsage(this);

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //logMemUsage.startLoggingMemUsage();

        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        preferences.edit().putBoolean("userLock",true).commit();

        timer = new Timer();

        final Context context = this;

        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                DiscoverBlueToothDevices.findBlueToothDevices(context, getBlueToothSurveyFileName());
            }
        });

        fileOutputLocation = intent.getStringExtra("fileOutPutLocation");
        surveyTimeout = intent.getIntExtra("surveyTimeout", AudioSenseConstants.defaultSurveyTimeout);
        startRecording();

        Log.w("AudioSurveySample", "Listening prior to survey");

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                (new Handler(Looper.getMainLooper())).post(new Runnable() {

                    @Override
                    public void run() {
                        stopRecording();

                        startAlarm();
                        canStopAlarm = true;
                        Log.w("AudioSurveySample", "StartingAlarm");
                    }
                });

            }
        }, AudioSenseConstants.defaultAudioSampleLength * 60000);

        return START_NOT_STICKY;
    }

    public void startAlarm() {


        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        if(!preferences.getBoolean("vibrationOnly",false)) {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
            audioManager.setStreamVolume(AudioManager.STREAM_RING, audioManager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);

            int ringSelection = preferences.getInt("ringSelection",1);
            if(ringSelection == 1) {
                mediaPlayer = MediaPlayer.create(this, R.raw.appmusic);
            } else if(ringSelection == 2) {
                mediaPlayer = MediaPlayer.create(this, R.raw.best_wake_up_sound);
            } else if (ringSelection == 3) {
                mediaPlayer = MediaPlayer.create(this, R.raw.extreme_clock_alarm);
            } else if (ringSelection == 4) {
                mediaPlayer = MediaPlayer.create(this, R.raw.galaxy_s6_latest);
            } else if (ringSelection == 5) {
                mediaPlayer = MediaPlayer.create(this, R.raw.galaxy_s6_splash);
            } else if (ringSelection == 6) {
                mediaPlayer = MediaPlayer.create(this, R.raw.message);
            } else if (ringSelection == 7) {
                mediaPlayer = MediaPlayer.create(this, R.raw.newappmusic);
            } else if (ringSelection == 8) {
                mediaPlayer = MediaPlayer.create(this, R.raw.sad_love_violin);
            } else if (ringSelection == 9) {
                mediaPlayer = MediaPlayer.create(this, R.raw.samsung);
            } else if (ringSelection == 10) {
                mediaPlayer = MediaPlayer.create(this, R.raw.viber_original);
            } else {
                throw new RuntimeException("Invalid ringer Selection.  This should be impossible");
            }
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        animateAlarm = new AnimateAlarm(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 500, 1000};
        vibrator.vibrate(pattern, 0);

        if(isScreenOn(this)) {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    (new Handler(Looper.getMainLooper())).post(new Runnable() {

                        @Override
                        public void run() {
                            stopAlarm(true, false);
                            Log.w("AudioSurveySample", "User was using phone and survey was displayed");
                        }
                    });

                }
            }, 3000);
        } else {

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            alarmManager = new AudioSenseAlarmManager();
            registerReceiver(alarmManager, filter);

            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    (new Handler(Looper.getMainLooper())).post(new Runnable() {

                        @Override
                        public void run() {
                            unregisterReceiver(alarmManager);
                            try{ Thread.sleep(1000); }catch(InterruptedException e){ }
                            stopAlarm(false, false);
                            Log.w("AudioSurveySample", "User Failed to answer Survey Alarm");
                        }
                    });

                }
            }, surveyTimeout * 60000);
        }

    }

    public void stopAlarm(boolean userAnswered, boolean unRegisterAlarmReceiver) {
        //logMemUsage.stopLoggingMemUsage();
        if(unRegisterAlarmReceiver) {
            try {
                unregisterReceiver(alarmManager);
            } catch(Exception e){}
        }

        if(canStopAlarm) {
            canStopAlarm = false;
            SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
            if(!preferences.getBoolean("vibrationOnly",false)) {
                //mediaPlayer.setLooping(false);
                Log.e("mediaPlayer","should be stopped");
                mediaPlayer.pause();
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            animateAlarm.stopAlarm();
            animateAlarm = null;
            vibrator.cancel();
            TimingManager.rescheduleAudioSurveySample(getApplicationContext(), false);
            preferences.edit().putBoolean("userLock",false).commit();
            if (userAnswered) {
                enterSurvey();
            } else {
                preferences.edit().putInt("dailyGivenSurveys",preferences.getInt("dailyGivenSurveys",0) + 1).commit();
                preferences.edit().putInt("totalGivenSurveys",preferences.getInt("totalGivenSurveys",0) + 1).commit();
                //TimingManager.rescheduleAudioSurveySample(getApplicationContext(), false);
                Log.w("AudioSurveySample", "Ending Audio Survey Sampling");
                AlarmAlertWakeLock.releaseCpuLock();
                stopSelf();
            }
        }
    }

    public void enterSurvey() {
        timer.cancel();
        timer.purge();

        SharedPreferences preferences = getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);
        preferences.edit().putBoolean("unlockSurveyScreen",true).commit();

        Intent intent = new Intent(this, AudioSenseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("AUDIOSENSE_STARTED_BY_SERVICE", true);
        startActivity(intent);
        AlarmAlertWakeLock.releaseCpuLock();
        stopSelf();
    }


    public void startRecording() {

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BUFFER_SIZE_IN_BYTES);

        recorder.startRecording();
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            public void run() {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    /**
     * credit userM1433372, http://stackoverflow.com/questions/5960924/how-to-detect-whether-screen-is-on-or-off-if-api-level-is-4
     *
     * Is the screen of the device on.
     * @param context the context
     * @return true when (at least one) screen is on
     */
    public boolean isScreenOn(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for (Display display : dm.getDisplays()) {
                if (display.getState() != Display.STATE_OFF) {
                    screenOn = true;
                }
            }
            return screenOn;
        } else {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //noinspection deprecation
            return pm.isScreenOn();
        }
    }

    //convert short to byte
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private void writeAudioDataToFile() {
        // Write the output audio in byte

        short sData[] = new short[BUFFER_SIZE_IN_BYTES / 2];

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(fileOutputLocation);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording) {
            // gets the voice output from microphone to byte format

            recorder.read(sData, 0, BUFFER_SIZE_IN_BYTES / 2);
            try {
                // // writes the data to file from buffer
                // // stores the voice buffer
                byte bData[] = short2byte(sData);
                os.write(bData, 0, BUFFER_SIZE_IN_BYTES);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        // stops the recording activity
        if (null != recorder) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if(recordingThread != null) {
            stopRecording();
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } catch(Exception e){}
        }
    }

    private String getBlueToothSurveyFileName() {
        SharedPreferences preferences =  getSharedPreferences(AudioSenseConstants.sharedPrefName, 0);

        int patientID = preferences.getInt("patientID", -1);
        int conditionID = preferences.getInt("conditionID", -1);
        int sessionID = preferences.getInt("sessionID",-1);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        return "/sdcard/BTS_PID" + Integer.toString(patientID) + "_CID" + Integer.toString(conditionID) + "_SID" + Integer.toString(sessionID) +
                "_DATE" + Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) +
                "_TIME" + Integer.toString(hour) + "-" + Integer.toString(min) + "-" + Integer.toString(sec) + ".bluetooth";
    }

}

