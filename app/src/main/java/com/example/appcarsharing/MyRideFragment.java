package com.example.appcarsharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyRideFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyRideAdapter adapter;
    private List<Ride> passaggiList;

    /*
    public static MyRideFragment newInstance(String param1, String param2) {
        MyRideFragment fragment = new MyRideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_ride, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        // Inizializza e imposta il LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        passaggiList = new ArrayList<>();
        adapter = new MyRideAdapter(passaggiList);
        recyclerView.setAdapter(adapter);

        //lettura da firebase
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("passaggi");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                passaggiList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Ride ride = snapshot.getValue(Ride.class);
                        passaggiList.add(ride);

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        return rootView;
    }
}