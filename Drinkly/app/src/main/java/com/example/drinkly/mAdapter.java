package com.example.drinkly;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class mAdapter<G> extends RecyclerView.Adapter<mAdapter.MyViewHolder> {
    ArrayList<Getränke> mdrinks;

    public mAdapter(ArrayList<Getränke> drinks) {
        this.mdrinks = drinks;
    }

    @NonNull
    @Override
    public mAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mAdapter.MyViewHolder holder, int position) {
        holder.date.setText(new StringBuilder().append(mdrinks.get(position).getDate()).append("").toString());
        holder.vol.setText(new StringBuilder().append(mdrinks.get(position).getVolume()).append("L").toString());
        holder.volP.setText(new StringBuilder().append(mdrinks.get(position).getVolumePart()).append("").toString());
        Bitmap bitmap = BitmapFactory.decodeFile(mdrinks.get(position).getUri());
        holder.imgV.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return mdrinks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView vol;
        public TextView volP;
        public ImageView imgV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            vol = itemView.findViewById(R.id.volume);
            volP = itemView.findViewById(R.id.volumePerc);
            imgV = itemView.findViewById(R.id.drinkImage);
        }
    }
}
