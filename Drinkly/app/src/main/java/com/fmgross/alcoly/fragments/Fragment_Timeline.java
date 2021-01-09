package com.fmgross.alcoly.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fmgross.alcoly.R;
import com.fmgross.alcoly.backend.Backend_DatabaseHelper;
import com.fmgross.alcoly.backend.Backend_Getraenk;
import com.fmgross.alcoly.backend.Backend_GroupAdapter;
import com.fmgross.alcoly.backend.Backend_Adapter;

import java.util.ArrayList;

public class Fragment_Timeline extends Fragment implements AdapterView.OnItemSelectedListener {

    private Backend_DatabaseHelper databaseHelper;
    private ArrayList<Backend_Getraenk> arrayList;
    private ArrayList<Integer> arrayListString;
    private RecyclerView newRecyclerView;
    private Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_timeline, container, false);
        newRecyclerView = root.findViewById(R.id.mRecyclerView);
        spinner = root.findViewById(R.id.spinner2);
        getDatabase();
        createRecycler();
        generateSpinner();
        return root;
    }

    private void generateSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.timeline, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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
        Backend_GroupAdapter groupAdapter = new Backend_GroupAdapter(getContext(), getActivity().getSupportFragmentManager(), arrayListString);
        newRecyclerView.setAdapter(groupAdapter);
        newRecyclerView.setHasFixedSize(false);
    }

    private void setOnCLickListener() {
        Backend_Adapter.RecyclerViewClickListener listener = (v, position) -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Bundle arguments = new Bundle();
            arguments.putInt("intposition",position);
            Fragment_Details fragment = new Fragment_Details();
            fragment.setArguments(arguments);
            ft.replace(R.id.nav_host_fragment, fragment);
            ft.commit();
        };
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
