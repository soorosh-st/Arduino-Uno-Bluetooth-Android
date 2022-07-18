package com.example.iot;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MotorAdapter extends RecyclerView.Adapter<MotorAdapter.MyViewHolder> {

    Context context;
    List<Motor> data;

    public MotorAdapter(Context context, List<Motor> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MotorAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotorAdapter.MyViewHolder holder, int position) {

        holder.date.setText(data.get(position).getDate());
        holder.motorspeed.setText(data.get(position).getRPM());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView motorspeed;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.datentime);
            motorspeed=itemView.findViewById(R.id.value);
        }
    }
}
