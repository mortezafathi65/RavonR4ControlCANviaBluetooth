package com.romanckua.carcanremote;

import android.bluetooth.*;
import android.os.Build;
import android.os.Handler;

import java.util.List;
import java.util.UUID;

import static android.bluetooth.BluetoothDevice.*;
import static android.bluetooth.BluetoothDevice.BOND_BONDED;
import static android.bluetooth.BluetoothGatt.GATT_FAILURE;
import static android.bluetooth.BluetoothGatt.GATT_SUCCESS;
import static android.bluetooth.BluetoothProfile.GATT_SERVER;
import static java.lang.Thread.sleep;

public class Ble implements Runnable {
    private MyService myService;
    private String blueToothDeviceAddress;
    private BluetoothDevice device;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothGatt gatt;
    Runnable discoverServicesRunnable;
    private Handler bleHandler = new Handler();
    private List<BluetoothGattService> services;
    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        int delay;
        int bondstate;

        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {

            if (status == GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    bondstate = device.getBondState();
                    if (bondstate == BOND_BONDING) {
                        while (bondstate == BOND_BONDING) {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            bondstate = device.getBondState();
                        }
                    }

                    if (bondstate == BOND_NONE || bondstate == BOND_BONDED) {
                        discoverServicesRunnable = new Runnable() {
                            @Override
                            public void run() {
                                int delayWhenBonded = 0;
                                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
                                    delayWhenBonded = 1500;
                                }
                                delay = bondstate == BOND_BONDED ? delayWhenBonded : 0;
                                boolean result = gatt.discoverServices();
                                try {
                                    sleep(3000);
                                    setCharacteristic();
                                    sleep(500);
                                    buttonUnLock();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (!result) {
                                }
                                discoverServicesRunnable = null;
                            }
                        };
                    }

                    bleHandler.postDelayed(discoverServicesRunnable, delay);

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    disconnect();
                } else {

                }
            } else {

                switch (status) {
                    case GATT_SERVER:
                        buttonLock();
                        gatt.close();
                        try {
                            sleep(500);
                            connectGatt();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GATT_FAILURE:
                        disconnect();
                        buttonLock();
                        break;
                    case 19:
                        disconnect();
                        buttonLock();
                        break;
                    default:
                        disconnect();
                        buttonLock();
                }
            }
        }
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
            } else {
            }
        }
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                readCharacteristic(characteristic);
            }
        }
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            readCharacteristic(characteristic);
        }
    };


    public Ble(MyService myService, String blueToothDeviceAddress) {
        this.blueToothDeviceAddress = blueToothDeviceAddress;
        this.myService = myService;
    }

    @Override
    public void run() {

        device = bluetoothAdapter.getRemoteDevice(blueToothDeviceAddress);
        connectGatt();



    }

    private void connectGatt() {
        gatt = device.connectGatt(myService, true, mGattCallback, TRANSPORT_LE);
    }

    private void setCharacteristic() {
        services = gatt.getServices();
        for (BluetoothGattService index : services
        ) {
            if ("0000ffe0-0000-1000-8000-00805f9b34fb".equals(index.getUuid().toString())) {
                characteristicTX = index.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                characteristicRX = index.getCharacteristic(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                gatt.setCharacteristicNotification(characteristicRX, true);
            }
        }
    }

    private void readCharacteristic(BluetoothGattCharacteristic characteristic) {

        final byte[] data = characteristic.getValue();
        String str = "";
        for (int i = 0; i < data.length; i++) {
            str = str + (char) data[i];
        }
        System.out.println(str);
    }

    private void buttonUnLock() {

    }

    private void buttonLock() {


    }

    public void disconnect() {
        buttonLock();
        if (gatt != null) {
            gatt.close();
        }
        Thread.interrupted();
    }


}
