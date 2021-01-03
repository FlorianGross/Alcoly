package com.fmgross.alcoly.backend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmgross.alcoly.Activity_MainPage;
import com.fmgross.alcoly.R;
import com.fmgross.alcoly.fragments.Fragment_Details;

import java.util.ArrayList;

public class Backend_GroupAdapter extends RecyclerView.Adapter<Backend_GroupAdapter.myViewHolder> {
    private final Context context;
    private final ArrayList<Integer> arrayListGroup;
    private Backend_Adapter.RecyclerViewClickListener listener;
    private FragmentManager activity;

    public Backend_GroupAdapter(Context context, FragmentManager activity, ArrayList<Integer> arrayListGroup) {
        this.context = context;
        this.arrayListGroup = arrayListGroup;
        this.activity = activity;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_group, parent, false);
        return new Backend_GroupAdapter.myViewHolder(view);
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

        Backend_DatabaseHelper databaseHelper = new Backend_DatabaseHelper(context);
        ArrayList<Backend_Getraenk> newGetraenkeList = databaseHelper.getAllOfDate(arrayListGroup.get(position));
        int numberOfColumns = 3;

        setOnCLickListener();
        holder.rv.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
        Backend_Adapter adapter = new Backend_Adapter(context, newGetraenkeList, listener);
        holder.rv.setAdapter(adapter);
        holder.rv.setHasFixedSize(false);


    }

    private void setOnCLickListener() {
        listener = (v, position) -> {
            FragmentManager fm = activity;
            FragmentTransaction ft = fm.beginTransaction();
            Bundle arguments = new Bundle();
            arguments.putInt("intposition",position);
            Fragment_Details fragment = new Fragment_Details();
            fragment.setArguments(arguments);
            ft.replace(R.id.nav_host_fragment, fragment);
            ft.commit();
        };
    }

    /**
     * Returns the amount of items inside the Adapter
     */
    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv;
        public final RecyclerView rv;

        public myViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.DateView);
            rv = view.findViewById(R.id.FirstRecyclerView);
        }
    }
}


