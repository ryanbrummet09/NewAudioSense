package com.example.ryanbrummet.newaudiosense2.AudioSense.Main;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.IBinder;
import android.media.AudioRecord;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.util.Log;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Bluetooth.DiscoverBlueToothDevices;
import com.example.ryanbrummet.newaudiosense2.AudiologyBaseSurveyCode.LogUsage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ryanbrummet on 8/24/15.
 *
 * Credit Rahul Baradia, http://stackoverflow.com/questions/8499042/android-audiorecord-example
 */
public class AudioSampleService extends Service{

    private String fileOutputLocation;

    private AudioRecord recorder = null;

    private final int RECORDER_SAMPLERATE = 44100;
    private final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private final int BUFFER_SIZE_IN_BYTES = 8192;
    private boolean isRecording = false;
    private Thread recordingThread = null;
    private Timer timer;
    private Context context;
    private final LogUsage logUsage = new LogUsage(this);


    public IBinder onBind(Intent intent) {
        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        this.context = this;

        startForeground( 99996, new Notification() );

        //TimingManager.rescheduleAudioSample(context);

        //logUsage.startLoggingMemUsage();

        try {
            fileOutputLocation = intent.getStringExtra("fileOutPutLocation");
            if(fileOutputLocation == null) {
                throw new RuntimeException("intent is missing file name for some reason");
            }
        } catch(RuntimeException e) {
            e.printStackTrace();
        }

        timer = new Timer();

        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            @Override
            public void run() {
                DiscoverBlueToothDevices.findBlueToothDevices(context, getBlueToothFileName());
            }
        });

        startRecording();
        Log.w("AudioSample", "Running");

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                (new Handler(Looper.getMainLooper())).post(new Runnable() {

                    @Override
                    public void run() {
                        stopRecording();
                        //logUsage.stopLoggingMemUsage();
                        Log.w("AudioSample", "Stopped");
                        //TimingManager.rescheduleAudioSample(context);
                        stopForeground(true);
                        AlarmAlertWakeLock.releaseCpuLock();
                        stopSelf();
                    }
                });

            }
        }, AudioSenseConstants.defaultAudioSampleLength * 60000);

        return START_NOT_STICKY;
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
        }
    }

    public String getBlueToothFileName() {
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

        return "/sdcard/BTNS_PID" + Integer.toString(patientID) + "_CID" + Integer.toString(conditionID) +
                "_DATE" + Integer.toString(year) + "-" + Integer.toString(month) + "-" + Integer.toString(day) +
                "_TIME" + Integer.toString(hour) + "-" + Integer.toString(min) + "-" + Integer.toString(sec) + ".bluetooth";
    }
}



