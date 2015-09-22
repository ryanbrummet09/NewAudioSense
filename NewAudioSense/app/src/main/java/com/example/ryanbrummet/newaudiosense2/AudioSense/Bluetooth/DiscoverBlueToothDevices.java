package com.example.ryanbrummet.newaudiosense2.AudioSense.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.ryanbrummet.newaudiosense2.AudioSense.Main.AudioSenseConstants;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ryanbrummet on 9/15/15.
 */
public class DiscoverBlueToothDevices {

    public static void findBlueToothDevices(Context context, final String fileName) {
        Timer timer = new Timer();

        boolean devicesConnected = false;

        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        final BlueToothDeviceHandler btDeviceHandler = new BlueToothDeviceHandler();
        final ArrayList<BluetoothDevice> devices = (ArrayList<BluetoothDevice>) bluetoothManager.getConnectedDevices(BluetoothProfile.GATT);
        final ArrayList<Integer> deviceRSSI = new ArrayList<Integer>();
        for(BluetoothDevice device : devices) {
            if(device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                if(device.getName().equals("User's Hearing Aids")) {
                    devicesConnected = true;
                    break;
                }
            }
        }

        if(devicesConnected) {
            for(BluetoothDevice device : devices) {
                Log.e("Device",device.getName());
                if(device.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                    if (device.getName().equals("User's Hearing Aids")) {
                        BluetoothGatt mBluetoothGatt1 = device.connectGatt(context, false, new BluetoothGattCallback() {

                            @Override
                            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status){
                                deviceRSSI.add(rssi);
                            }
                        });
                        try {
                            Thread.sleep(2000);
                        }catch(InterruptedException e) {};
                        mBluetoothGatt1.readRemoteRssi();
                        try {
                            Thread.sleep(2000);
                        }catch(InterruptedException e) {};

                    }
                }
            }

            FileOutputStream os = null;
            try {
                os = new FileOutputStream(fileName);
                for (int i = 0; i < devices.size(); i++) {
                    os.write(("BlueTooth:" + devices.get(i).getName() + "," + Integer.toString(deviceRSSI.get(i)) + "\n").getBytes());
                }
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            final BluetoothAdapter btAdapter = bluetoothManager.getAdapter();
            btAdapter.startLeScan(btDeviceHandler);

            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    (new Handler(Looper.getMainLooper())).post(new Runnable() {

                        @Override
                        public void run() {
                            btAdapter.stopLeScan(btDeviceHandler);
                            FileOutputStream os = null;
                            try {
                                os = new FileOutputStream(fileName);
                                for (int i = 0; i < btDeviceHandler.getDeviceNames().size(); i++) {
                                    os.write(("BlueTooth:" + btDeviceHandler.getDeviceNames().get(i) + "," + Integer.toString(btDeviceHandler.getDeviceRSSI().get(i)) + "\n").getBytes());
                                }
                                os.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }, AudioSenseConstants.defaultAudioSampleLength * 15000);
        }
    }

}
