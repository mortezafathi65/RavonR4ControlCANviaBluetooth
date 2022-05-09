package com.romanckua.carcanremote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION = "com.romanckua.carcanremote";
    public static final String PARAM_MSG = "CONTROL";

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new BluetoothPermits(this).verificationOfPermits();
        new GeolocationPermits(this).verificationOfPermits();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 17890) {
            new BluetoothPermits(this).verificationOfPermits();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 17990) {
            new GeolocationPermits(this).verificationOfPermits();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_car) {
            // Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            // intent.putExtra("ScanBluetoothActivity", bluetoothScanMethod);
            //startActivity(intent);
            return true;
        }
        if (id == R.id.action_scanbt) {
            // Intent intent = new Intent(MainActivity.this, ScanBluetoothActivity.class);
            // intent.putExtra("ScanBluetoothActivity", bluetoothScanMethod);
            // startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}