package com.example.iot;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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


public class Fragment_ultra extends Fragment {

    View view;


    RecyclerView UltraRecyclerView;
    UltrasonicAdapter ultraAdapter;
    List<Distance> data_distance= new ArrayList<>();
    TextView text_distance;
    Button RefreshButton;
    public OutputStream os;
    public String distance;
    public InputStream is;
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_ultra, container, false);

        sharedPreferences= requireContext().getSharedPreferences("ULTRA", Context.MODE_PRIVATE);
        text_distance = view.findViewById(R.id.distance);
        RefreshButton= view.findViewById(R.id.refresh_ultra);
        socket=SocketSingleton.getSocket();
        UltraRecyclerView =view.findViewById(R.id.history_distance);

        readData();

        ultraAdapter = new UltrasonicAdapter(getContext(),data_distance);

        UltraRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        UltraRecyclerView.setHasFixedSize(true);
        UltraRecyclerView.setAdapter(ultraAdapter);



        RefreshButton.setOnClickListener(v -> {
            if (socket != null) {
                try {
                    is = socket.getInputStream();
                    byte[] buffer = new byte[256];
                    if (is.available() > 0) {
                        is.read(buffer);
                            parser(buffer);
                            if (distance != null) {
                                text_distance.setText(distance);
                                // adding new Entery to last ones
                                if (sharedPreferences != null) {
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString("ultra", "");
                                    if (json.isEmpty())
                                        Toast.makeText(getContext(), "No record to read", Toast.LENGTH_LONG).show();
                                    else {
                                        Type type = new TypeToken<List<Distance>>() {
                                        }.getType();
                                        data_distance = gson.fromJson(json, type);
                                        Date date = Calendar.getInstance().getTime();
                                        data_distance.add(new Distance(distance, date.toString()));
                                    }

                                }

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(data_distance);
                                editor.putString("lastultra", distance);
                                editor.putString("ultra", json);
                                editor.apply();

                                ultraAdapter = new UltrasonicAdapter(getContext(), data_distance);
                                UltraRecyclerView.setAdapter(ultraAdapter);
                            }

                    }
                }
                    catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });



        return view;
    }
   // {*10#20*%1500%$4000$@1@}
   private void parser(byte[] buffer) {

       try {
           int i = 0;

               for (; buffer[i] != '%' && buffer[i] != 0; i++) ;
               i++;

               int j = i;

               for (; buffer[j] != '%' && buffer[j] != 0; j++) {

               }
               j--;

               distance = new String(buffer, i, j - i);

       }
       catch (IndexOutOfBoundsException e){
           e.printStackTrace();
       }

   }

    private void readData() {
        sharedPreferences= requireContext().getSharedPreferences("ULTRA", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("ultra", "");
            if (json.isEmpty()) {
                Toast.makeText(getContext(), "There is something error", Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<Distance>>() {
                }.getType();
                data_distance = gson.fromJson(json, type);
            }
            if (sharedPreferences!=null) {
                String lastult = sharedPreferences.getString("lastultra", "");
                if (lastult!=null)
                    text_distance.setText(lastult);
            }


        }
    }
}