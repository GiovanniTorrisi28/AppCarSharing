package com.example.appcarsharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_ride, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        passaggiList = new ArrayList<>();
        adapter = new MyRideAdapter(passaggiList);
        recyclerView.setAdapter(adapter);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("passaggi");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                passaggiList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Ride ride = snapshot.getValue(Ride.class);
                    if(checkRide(ride))
                       passaggiList.add(0,ride);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        return rootView;
    }

    private boolean checkRide(Ride ride) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userKey = email.substring(0, email.indexOf("@"));

        if(ride.getGuidatore().getEmail().substring(0,ride.getGuidatore().getEmail().indexOf("@")).equals(userKey))
            return true;

        for(Utente u: ride.getUtenti()){
            if(u.getKey().equals(userKey)){
                return true;
            }
        }

        return false;
    }
}