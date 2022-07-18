package com.example.iot;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class UltrasonicAdapter extends RecyclerView.Adapter<UltrasonicAdapter.MyViewHolder> {

    Context context;
    List<Distance> data;

    public UltrasonicAdapter(Context context, List<Distance> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public UltrasonicAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UltrasonicAdapter.MyViewHolder holder, int position) {

        holder.date.setText(data.get(position).getDate());
        holder.Distance.setText(data.get(position).getDistance());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView Distance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.datentime);
            Distance=itemView.findViewById(R.id.value);
        }
    }
}
