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

public class FragmentRIP extends Fragment {
    View view;


    RecyclerView PirRecyclerView;
    PirAdapter PIRAdapter;
    List<PIR> data_PIR= new ArrayList<>();
    TextView text_detection;
    Button RefreshButton;
    public OutputStream os;
    public InputStream is;
    public String detect;
    private static final String TAG = "BluetoothHomeAutomation";
    SharedPreferences sharedPreferences;
    private BluetoothSocket socket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_r_i_p, container, false);
        sharedPreferences= requireContext().getSharedPreferences("PIR", Context.MODE_PRIVATE);
        text_detection = view.findViewById(R.id.motion_PIR);
        RefreshButton= view.findViewById(R.id.refresh_pir);
        socket=SocketSingleton.getSocket();
        PirRecyclerView =view.findViewById(R.id.history_PIR);
        readData();
        PIRAdapter = new PirAdapter(getContext(),data_PIR);

        PirRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        PirRecyclerView.setHasFixedSize(true);
        PirRecyclerView.setAdapter(PIRAdapter);




        RefreshButton.setOnClickListener(v -> {
            if (socket != null) {
                try {
                    is = socket.getInputStream();
                    byte[] buffer = new byte[256];
                    if (is.available() > 0) {
                        is.read(buffer);


                            parser(buffer);
                            if (detect!=null) {
                                text_detection.setText(detect);
                                // adding new Entery to last ones
                                if (sharedPreferences != null) {
                                    Gson gson = new Gson();
                                    String json = sharedPreferences.getString("pir", "");
                                    if (json.isEmpty())
                                        Toast.makeText(getContext(), "No record to read", Toast.LENGTH_LONG).show();
                                    else {
                                        Type type = new TypeToken<List<PIR>>() {
                                        }.getType();
                                        data_PIR = gson.fromJson(json, type);
                                        Date date = Calendar.getInstance().getTime();
                                        data_PIR.add(new PIR(detect, date.toString()));
                                    }

                                }
                                if (sharedPreferences != null) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(data_PIR);
                                    editor.putString("lastpir", detect);
                                    editor.putString("pir", json);
                                    editor.apply();
                                    PIRAdapter = new PirAdapter(getContext(), data_PIR);
                                    PirRecyclerView.setAdapter(PIRAdapter);
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

    private void readData() {
        sharedPreferences= requireContext().getSharedPreferences("PIR", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("pir", "");
            if (json.isEmpty()) {
                Toast.makeText(getContext(), "There is some error", Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<PIR>>() {
                }.getType();
                data_PIR = gson.fromJson(json, type);
            }
            if (sharedPreferences!=null) {
                String lastpir = sharedPreferences.getString("lastpir", "");
                if (lastpir!=null)
                    text_detection.setText(lastpir);
            }

        }
    }
//{*10#20*%1500%$4000$@1@}
    private void parser(byte[] buffer) {

        try {
            int i = 0;
                for (; buffer[i] != '@' ; i++) ;
                i++;
                char det = (char)buffer[i];
                if (det=='1')
                    detect="Detected";
                if (det=='0')
                    detect="Not Detected";

        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

    }


}