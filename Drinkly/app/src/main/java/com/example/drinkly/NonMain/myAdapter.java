package com.example.drinkly.NonMain;


import android.content.Context;
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


public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private ArrayList<Getränke> mdrinks;
    private LayoutInflater mInflater;
    private RecyclerViewClickListener listener;

    // data is passed into the constructor
    public myAdapter(Context context, ArrayList<Getränke> mdrinks, RecyclerViewClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mdrinks = mdrinks;
        this.listener = listener;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.my_grid, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Insert New Data
        Date newDate = new Date(mdrinks.get(position).getDate());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String strDate = formatter.format(newDate);
        holder.date.setText(strDate);
        holder.vol.setText(new StringBuilder().append(mdrinks.get(position).getVolume()).append("L").toString());
        holder.volP.setText(new StringBuilder().append(mdrinks.get(position).getVolumePart()).append(" \u2030").toString());
        Bitmap bitmap = BitmapFactory.decodeFile(mdrinks.get(position).getUri());
        holder.imgV.setImageBitmap(bitmap);


    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mdrinks.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        public TextView date;
        public TextView vol;
        public TextView volP;
        public ImageView imgV;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.time);
            vol = itemView.findViewById(R.id.newvolume);
            volP = itemView.findViewById(R.id.newvolumePerc);
            imgV = itemView.findViewById(R.id.currentImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
    listener.onClick(v, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }

}