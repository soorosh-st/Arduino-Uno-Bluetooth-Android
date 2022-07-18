package com.example.iot;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class PirAdapter extends RecyclerView.Adapter<PirAdapter.MyViewHolder> {

    Context context;
    List<PIR> data;

    public PirAdapter(Context context, List<PIR> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public PirAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.item_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PirAdapter.MyViewHolder holder, int position) {

        holder.date.setText(data.get(position).getDate());
        holder.state.setText(data.get(position).getState());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView date;
        TextView state;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.datentime);
            state=itemView.findViewById(R.id.value);
        }
    }
}
