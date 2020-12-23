package com.fmgross.alcoly.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmgross.alcoly.Details;
import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.DatabaseHelper;
import com.fmgross.alcoly.backend.Getraenke;
import com.fmgross.alcoly.backend.GroupAdapter;
import com.fmgross.alcoly.backend.myAdapter;

import java.util.ArrayList;

public class Fragment_Fragment_Timeline extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatabase();
        createRecycler();
    }

    private DatabaseHelper databaseHelper;
    private ArrayList<Getraenke> arrayList;
    private ArrayList<Integer> arrayListString;
    RecyclerView newRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_fragment_timeline, container, false);
        newRecyclerView = root.findViewById(R.id.mRecyclerView);
        return root;
    }

    /**
     * Returns all getränke inside an arrayList
     */
    private void getDatabase() {
        databaseHelper = new DatabaseHelper(getContext().getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates();
    }

    /**
     * Creates the recyclerview
     */
    private void createRecycler() {
        newRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        GroupAdapter groupAdapter = new GroupAdapter(getContext(), arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);
    }

    private void setOnCLickListener() {
        myAdapter.RecyclerViewClickListener listener = (v, position) -> {

        };
    }
}
