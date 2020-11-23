package com.fmgross.drinkly.backend;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fmgross.drinkly.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {
    private final ArrayList<Getraenke> mdrinks;
    private final LayoutInflater mInflater;
    private final RecyclerViewClickListener listener;

    // data is passed into the constructor
    public myAdapter(Context context, ArrayList<Getraenke> mdrinks, RecyclerViewClickListener listener) {
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
        Date newDate = mdrinks.get(position).getDate();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String strDate = formatter.format(newDate);

        holder.date.setText(strDate);
        holder.vol.setText(mdrinks.get(position).getVolume() + "L");
        holder.volP.setText(mdrinks.get(position).getVolumePart() + " \u2030");
        holder.imgV.setImageBitmap(mdrinks.get(position).getUri());
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mdrinks.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView date;
        public final TextView vol;
        public final TextView volP;
        public final ImageView imgV;

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

    public interface RecyclerViewClickListener {
        void onClick(View v, int position);
    }

}