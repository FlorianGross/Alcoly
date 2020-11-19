package com.example.drinkly.NonMain;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinkly.Details;
import com.example.drinkly.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.myViewHolder> {
    private Context context;
    ArrayList<Integer> arrayListGroup;
    private myAdapter.RecyclerViewClickListener listener;

    public GroupAdapter(Context context, ArrayList<Integer> arrayListGroup) {
        this.context = context;
        this.arrayListGroup = arrayListGroup;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_group, parent, false);
        return new GroupAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        int preString = arrayListGroup.get(position);
        int day = preString / 1000000;
        preString = preString - day * 1000000;
        int month = preString / 10000;
        preString = preString - month * 10000;
        int year = preString;
        String postString = day + "." + month + "." + year;
        holder.tv.setText(postString);

        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        //ArrayList<Getränke> getränkeList = databaseHelper.getAllGetraenke();
        ArrayList<Getränke> newGetränkeList = databaseHelper.getAllOfDate(arrayListGroup.get(position));
        int numberOfColumns = 3;

        setOnCLickListener();
        holder.rv.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
        myAdapter adapter = new myAdapter(context, newGetränkeList, listener);
        holder.rv.setAdapter(adapter);
        holder.rv.setHasFixedSize(false);


    }

    private void setOnCLickListener() {
        listener = new myAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(context, Details.class);
                intent.putExtra("intPosition", position);
                context.startActivity(intent);
            }
        };
    }

    /**
     * Returns the amount of items inside the Adapter
     */
    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public RecyclerView rv;

        public myViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.DateView);
            rv = view.findViewById(R.id.FirstRecyclerView);
        }
    }
}


