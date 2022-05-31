package com.romanckua.carcanremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ScanningBluetooth extends AppCompatActivity {

    BluetoothLeScanner scanner;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    BluetoothDevice device;
    List<ScanFilter> filters = null;
    ListView listView;
    HashSet<String> hashSetBTaddList = new HashSet<>();
    ArrayList<String> listViewBTlist = new ArrayList<>();
    ArrayList<String> macAddressList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private final ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            device = result.getDevice();
            System.out.println(device);
            if (hashSetBTaddList != null) {
                if (!hashSetBTaddList.contains(device.getAddress())) {
                    hashSetBTaddList.add(device.getAddress());
                    listViewBTlist.add(device.getName() + ": " + device.getAddress());
                    macAddressList.add(device.getAddress());
                    adapter.notifyDataSetChanged();
                }
            } else {

                hashSetBTaddList.add(device.getAddress());
                listViewBTlist.add(device.getName() + ": " + device.getAddress());
                macAddressList.add(device.getAddress());
                adapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {

        }

        @Override
        public void onScanFailed(int errorCode) {

        }
    };

    @Override
    protected void onPause() {
        if (scanner != null) {
            scanner.stopScan(scanCallback);
        }
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanningbluetooth);
        setTitle("Select device");
        listView = findViewById(R.id.scanBTList);

        if (listViewBTlist != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listViewBTlist);
            listView.setAdapter(adapter);
        }

        scanner = bluetoothAdapter.getBluetoothLeScanner();

        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
                .setReportDelay(0L)
                .build();
        if (scanner != null) {
            scanner.startScan(filters, scanSettings, scanCallback);
        } else { }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                String data = macAddressList.get(position);
                Setting setting = new Setting(ScanningBluetooth.this);
                setting.setSetting("device", data);
                onPause();
            }
        });

    }

}

