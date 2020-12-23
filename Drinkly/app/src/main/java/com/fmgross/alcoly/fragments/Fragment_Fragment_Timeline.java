package com.fmgross.alcoly.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_DatabaseHelper;
import com.fmgross.alcoly.backend.Backend_Getraenk;
import com.fmgross.alcoly.backend.Backend_GroupAdapter;
import com.fmgross.alcoly.backend.Backend_Adapter;

import java.util.ArrayList;

public class Fragment_Fragment_Timeline extends Fragment {

    private Backend_DatabaseHelper databaseHelper;
    private ArrayList<Backend_Getraenk> arrayList;
    private ArrayList<Integer> arrayListString;
    RecyclerView newRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDatabase();
        createRecycler();
    }

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
        databaseHelper = new Backend_DatabaseHelper(getContext().getApplicationContext());
        arrayList = databaseHelper.getAllGetraenke();
        arrayListString = databaseHelper.getAllDates();
    }

    /**
     * Creates the recyclerview
     */
    private void createRecycler() {
        newRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Backend_GroupAdapter groupAdapter = new Backend_GroupAdapter(getContext(), arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);
    }

    private void setOnCLickListener() {
        Backend_Adapter.RecyclerViewClickListener listener = (v, position) -> {

        };
    }
}
