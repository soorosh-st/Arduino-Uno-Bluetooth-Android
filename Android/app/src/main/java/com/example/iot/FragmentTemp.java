package com.example.iot;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.owl93.dpb.CircularProgressView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;



public class FragmentTemp extends Fragment {


    View view;
    Button acturnonBTN;
    CircularProgressView progressview;
    RecyclerView temprecyclerView;
    TempAdapter tempAdapter;
    List<Temp> data_temp= new ArrayList<>();
    TextView texttemp;
    Button refreshbtn;

    private BluetoothSocket socket;
    private BluetoothDevice device;
    public OutputStream os;
    public InputStream is;
    private static final String TAG = "BluetoothHomeAutomation";
    public  String temp="0";
    public  String hum="0";


    SharedPreferences sharedPreferences;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_temp, container, false);
        texttemp=view.findViewById(R.id.temper);
        progressview = view.findViewById(R.id.progress);
        refreshbtn=view.findViewById(R.id.refresh);

        sharedPreferences= requireContext().getSharedPreferences("TEMP", Context.MODE_PRIVATE);
        temprecyclerView = view.findViewById(R.id.history_temp);
        temprecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        temprecyclerView.setHasFixedSize(true);


        readData();


        tempAdapter = new TempAdapter(getContext(), data_temp);
        temprecyclerView.setAdapter(tempAdapter);







        ////////////////////////////////////////////////////


        socket=SocketSingleton.getSocket();





        refreshbtn.setOnClickListener(v -> {
            if (socket != null) {
                try {
                    is = socket.getInputStream();
                    byte[] buffer = new byte[256];
                    if (is.available() > 0) {
                        is.read(buffer);
                            parser(buffer);
                            if (temp!=null) {
                                texttemp.setText(temp);
                                progressview.animateProgressChange(Float.parseFloat(temp) / 2, 500);
                                // adding new Entery to last ones
                                if (sharedPreferences != null) {
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString("temp", "");
                                    if (json.isEmpty())
                                        Toast.makeText(getContext(), "No record to read", Toast.LENGTH_LONG).show();
                                    else {
                                        Type type = new TypeToken<List<Temp>>() {
                                        }.getType();
                                        data_temp = gson.fromJson(json, type);
                                        String TempAndHu = "H: " + hum + " T:" + temp;
                                        Date date = Calendar.getInstance().getTime();
                                        data_temp.add(new Temp(TempAndHu, date.toString()));
                                    }

                                }
                                if (sharedPreferences != null) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(data_temp);
                                    editor.putString("lasttemp", temp);
                                    editor.putString("temp", json);
                                    editor.apply();
                                    tempAdapter = new TempAdapter(getContext(), data_temp);
                                    temprecyclerView.setAdapter(tempAdapter);
                                }

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });


        return view;



    }
    //   {*temp#humidity*%distance%$rpm$@Detection@} <- output form
    // {*10#20*%1500%$4000$@1@}
    private void parser(byte[] buffer) {
        int i = 0;
        try {
            if (buffer[0] != '{') {
                for (; buffer[i] != 0 && buffer[i] != '{'; i++) ;
            } else {

                for (; buffer[i] != '*'&& buffer[i] != 0; i++) ;
                i++;

                int j = i;
                int k = 0;
                for (; buffer[j] != '*' && buffer[j] != 0 ; j++) {
                    if (buffer[j] == '#')
                        k = j;
                }
                j--;

                temp = new String(buffer, i, k - i);
                hum = new String(buffer, k + 1, j - k);

            }
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

    }


    private void readData() {
        sharedPreferences= requireContext().getSharedPreferences("TEMP", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("temp", "");
            if (json.isEmpty()) {
                Toast.makeText(getContext(), "There is something error", Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<Temp>>() {
                }.getType();
                data_temp = gson.fromJson(json, type);
            }
            if (sharedPreferences!=null) {
                String lasttemp = sharedPreferences.getString("lasttemp", "");
                if (lasttemp!=null)
                    texttemp.setText(lasttemp);
            }


        }
    }



}