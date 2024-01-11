package com.serenespotssqlite.ass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize UserAdapter
        userAdapter = new UserAdapter(getActivity());

        // Set UserAdapter to RecyclerView
        recyclerView.setAdapter(userAdapter);

        // Load data into the adapter (replace with your actual data loading mechanism)
        loadDataIntoAdapter();

        return rootView;
    }

    private void loadDataIntoAdapter() {
        // Example: Load data from SQLite database and set it to the adapter
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        List<User> userList = databaseHelper.getAllUsers();

        // Set the data to the adapter
        userAdapter.setUserList(userList);
    }
}
