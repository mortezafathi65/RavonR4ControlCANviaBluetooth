package com.romanckua.carcanremote;

import android.content.*;
import android.os.Build;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION = "com.romanckua.carcanremote";
    public static final String PARAM_MSG = "CONTROL";
    private MyService service;
    boolean bound = false;
    private ServiceConnection connectionService;
    private Intent intent;
    public Setting setting;
    public Car car;
    ListView listViewCarFunc;
    ArrayAdapter<String> adapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         setting = new Setting(this);
         car = new Car(setting.getSetting("car"));
         listViewCarFunc = findViewById(R.id.listViewCarFunc);
        ArrayList<String> arrayListCarFunc = car.getListCarFunc();


        if (arrayListCarFunc != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListCarFunc);
            listViewCarFunc.setAdapter(adapter);
        }

        new BluetoothPermits(this).verificationOfPermits();
        new GeolocationPermits(this).verificationOfPermits();

        startService();

        listViewCarFunc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                System.out.println(arrayListCarFunc.get(position));
            }
        });

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

            return true;
        }
        if (id == R.id.action_scanbt) {
            Intent intent = new Intent(MainActivity.this, ScanningBluetooth.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_connect) {
           // item.setTitle("Disconnect");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startService() {

        intent = new Intent(MainActivity.this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }

        connectionService = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = ((MyService.MyServiceBinder) binder).getService();
                bound = true;

            }

            public void onServiceDisconnected(ComponentName name) {
                bound = false;
            }
        };

        bindService(intent, connectionService, BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ACTION));

    }

}