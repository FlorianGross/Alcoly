package com.example.drinkly.NonMain;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkly.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class mAdapter extends RecyclerView.Adapter<mAdapter.MyViewHolder> {
    private ArrayList<Getränke> mdrinks;

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
        Date newDate = new Date(mdrinks.get(position).getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.M.yyyy");
        String strDate = formatter.format(newDate);
        holder.date.setText(strDate);
        holder.vol.setText(new StringBuilder().append(mdrinks.get(position).getVolume()).append("L").toString());
        holder.volP.setText(new StringBuilder().append(mdrinks.get(position).getVolumePart()).append("").toString());
        holder.imgV.setImageBitmap(mdrinks.get(position).getUri());
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
