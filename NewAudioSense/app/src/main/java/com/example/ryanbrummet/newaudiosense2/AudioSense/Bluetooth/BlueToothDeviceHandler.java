package com.example.ryanbrummet.newaudiosense2.AudioSense.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ryanbrummet on 9/17/15.
 */
public class BlueToothDeviceHandler implements BluetoothAdapter.LeScanCallback {

    private ArrayList<String> deviceNames;
    private ArrayList<Integer> deviceRSSI;

    public BlueToothDeviceHandler() {
        this.deviceNames = new ArrayList<String>();
        this.deviceRSSI = new ArrayList<Integer>();
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if(!deviceNames.contains(device.getName() + "@" + device.getAddress())) {
            Log.w("BlueToothDeviceHandler", "LE BT device found");
            deviceNames.add(device.getName() + "@" + device.getAddress());
            deviceRSSI.add(rssi);
        }
    }

    public ArrayList<String> getDeviceNames() {
        return deviceNames;
    }

    public ArrayList<Integer> getDeviceRSSI() {
        return deviceRSSI;
    }

}
