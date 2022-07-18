package com.example.iot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button btn_temp;
    ChipNavigationBar chipNavigationBar;

    private BluetoothSocket socket;
    private BluetoothDevice device;
    public OutputStream os;
    public InputStream is;
    Button Reconnect;
    private static final String TAG = "BluetoothHomeAutomation";
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Reconnect=findViewById(R.id.reconnect);
        chipNavigationBar=findViewById(R.id.navigation);

        chipNavigationBar.setItemSelected(R.id.unltrasonic,true);
        Fragment_ultra fragment_ultra1 = new Fragment_ultra();
        FragmentTransaction fragmentTransaction_ultra1 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction_ultra1.replace(R.id.mainframe,fragment_ultra1);
        fragmentTransaction_ultra1.commit();


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Reconnect.setOnClickListener(v -> {
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                device = mBluetoothAdapter.getRemoteDevice("48:E7:DA:F5:DA:32");
                //40:9F:38:18:1C:3E
                //48:E7:DA:F5:DA:32 man
                try{
                    socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                }catch (SecurityException e){}
            } catch ( IOException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }

            try{
                mBluetoothAdapter.cancelDiscovery();
                if(socket != null) {
                    try {
                        socket.connect();
                    } catch (IOException e) {
                        Toast.makeText(this, "Couldn't establish Bluetooth connection!", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (SecurityException e){}

            SocketSingleton.setSocket(socket);
        });

        if(socket != null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            device = mBluetoothAdapter.getRemoteDevice("48:E7:DA:F5:DA:32");
            //40:9F:38:18:1C:3E
            //48:E7:DA:F5:DA:32 man
            try{
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }catch (SecurityException e){}
        } catch ( IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        try{
            mBluetoothAdapter.cancelDiscovery();
            if(socket != null) {
                try {
                    socket.connect();
                } catch (IOException e) {
                    Toast.makeText(this, "Couldn't establish Bluetooth connection!", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (SecurityException e){}

        SocketSingleton.setSocket(socket);



        chipNavigationBar.setOnItemSelectedListener(Position -> {

            switch (Position){
                case R.id.temp:
                    FragmentTemp fragmentTemp = new FragmentTemp();
                    FragmentTransaction fragmentTransaction_temp = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_temp.replace(R.id.mainframe,fragmentTemp);
                    fragmentTransaction_temp.commit();
                    break;
                case R.id.unltrasonic:
                    Fragment_ultra fragment_ultra = new Fragment_ultra();
                    FragmentTransaction fragmentTransaction_ultra = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_ultra.replace(R.id.mainframe,fragment_ultra);
                    fragmentTransaction_ultra.commit();
                    break;

                case R.id.irnmotor:
                    FragmentMotor fragmentMotor = new FragmentMotor();
                    FragmentTransaction fragmentTransaction_motor = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_motor.replace(R.id.mainframe,fragmentMotor);
                    fragmentTransaction_motor.commit();
                    break;

                case R.id.rip:
                    FragmentRIP fragmentRIP = new FragmentRIP();
                    FragmentTransaction fragmentTransaction_RIP = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction_RIP.replace(R.id.mainframe,fragmentRIP);
                    fragmentTransaction_RIP.commit();
                    break;
            }

        });

    }
}