package com.example.iot;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class FragmentMotor extends Fragment {

    View view;


    RecyclerView MotorRecyclerView;
    MotorAdapter motorAdapter;
    List<Motor> data_MOTOR= new ArrayList<>();
    TextView text_RPM;
    Button RefreshButton;
    public OutputStream os;
    public InputStream is;
    public String rpm;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket;
    SeekBar seekBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_motor, container, false);

        sharedPreferences= requireContext().getSharedPreferences("MOTOR", Context.MODE_PRIVATE);
        text_RPM = view.findViewById(R.id.RPM);
        RefreshButton= view.findViewById(R.id.refresh_motor);
        socket=SocketSingleton.getSocket();
        MotorRecyclerView =view.findViewById(R.id.history_Motor);
        seekBar=view.findViewById(R.id.seekbar);

        readData();

        motorAdapter = new MotorAdapter(getContext(),data_MOTOR);

        MotorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        MotorRecyclerView.setHasFixedSize(true);
        MotorRecyclerView.setAdapter(motorAdapter);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if(socket != null){
                    try {
                        os = socket.getOutputStream();
                    } catch (IOException e) {

                    }
                    if(socket.isConnected()) {
                        try {
                            if (os != null){

                                    os.write(progressChangedValue);


                            }
                        } catch (IOException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "Connect to the selected bluetooth device first", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), " Connect to the selected bluetooth device first", Toast.LENGTH_LONG).show();
                }
            }
        });

        RefreshButton.setOnClickListener(v -> {
            if (socket != null) {
                try {
                    is = socket.getInputStream();
                    byte[] buffer = new byte[256];
                    if (is.available() > 0) {
                        is.read(buffer);



                            parser(buffer);
                            if (rpm!=null) {
                                text_RPM.setText(rpm);

                                // adding new Entery to last ones
                                if (sharedPreferences != null) {
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString("motor", "");
                                    if (json.isEmpty())
                                        Toast.makeText(getContext(), "No record to read", Toast.LENGTH_LONG).show();
                                    else {
                                        Type type = new TypeToken<List<Motor>>() {
                                        }.getType();
                                        data_MOTOR = gson.fromJson(json, type);
                                        Date date = Calendar.getInstance().getTime();
                                        data_MOTOR.add(new Motor(rpm, date.toString()));
                                    }

                                }
                                if (sharedPreferences != null) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(data_MOTOR);
                                    editor.putString("motorlast", rpm);
                                    editor.putString("motor", json);
                                    editor.apply();
                                    motorAdapter = new MotorAdapter(getContext(), data_MOTOR);
                                    MotorRecyclerView.setAdapter(motorAdapter);
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

                for (; buffer[i] != '$' && buffer[i] != 0; i++) ;
                i++;

                int j = i;

                for (; buffer[j] != '$' && buffer[j] != 0; j++) {
                }
                j--;

                rpm = new String(buffer, i, j - i);
            }
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }



    }

    private void readData() {
        sharedPreferences= requireContext().getSharedPreferences("MOTOR", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("motor", "");
            if (json.isEmpty()) {
                Toast.makeText(getContext(), "There is something error", Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<Motor>>() {
                }.getType();
                data_MOTOR = gson.fromJson(json, type);
            }

        }
        if (sharedPreferences!=null) {
            String lastrpm = sharedPreferences.getString("motorlast", "");
            if (lastrpm!=null)
                text_RPM.setText(lastrpm);
        }
    }
}