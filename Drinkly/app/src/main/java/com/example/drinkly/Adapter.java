package com.example.drinkly;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private ArrayList<Getränke> drink1 = new ArrayList<Getränke>();

    public Adapter(ArrayList<Getränke> drink) {
        drink = this.drink1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Getränke currentGetränk = drink1.get(position);
        holder.time.setText(currentGetränk.getDate().toString());
        holder.vol.setText(currentGetränk.getVolume() + "");
        holder.volP.setText(currentGetränk.getVolumePart() + "");
        holder.img.setImageURI(currentGetränk.getUri());

    }

    @Override
    public int getItemCount() {
        return drink1.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time, vol, volP;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.timeStamp);
            vol = itemView.findViewById(R.id.volume);
            volP = itemView.findViewById(R.id.volumePerc);
            img = itemView.findViewById(R.id.imageView3);
        }
    }
}
