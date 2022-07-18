package com.example.iot;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class TempAdapter extends RecyclerView.Adapter<TempAdapter.MyViewHolder> {
    Context context;
    List<Temp> data;

    public TempAdapter(Context context, List<Temp> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public TempAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempAdapter.MyViewHolder holder, int position) {

        holder.date.setText(data.get(position).getDate());
        holder.temp.setText(data.get(position).getTemp());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView temp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.datentime);
            temp=itemView.findViewById(R.id.value);
        }
    }
}
