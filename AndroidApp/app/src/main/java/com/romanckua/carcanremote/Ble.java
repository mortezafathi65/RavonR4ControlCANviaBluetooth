package com.romanckua.carcanremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class Ble implements Runnable {
    private MyService myService;
    private String blueToothDeviceAddress;
    private BluetoothDevice device;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public Ble(MyService myService, String blueToothDeviceAddress) {
        this.blueToothDeviceAddress = blueToothDeviceAddress;
        this.myService = myService;
    }

    @Override
    public void run() {

        device = bluetoothAdapter.getRemoteDevice(blueToothDeviceAddress);



    }


}
