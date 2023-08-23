package com.example.appcarsharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificheList;
    private FirebaseUser user;

    public NotificationFragment(){
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        // Inizializza e imposta il LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        notificheList = new ArrayList<>();
        adapter = new NotificationAdapter(notificheList);
        recyclerView.setAdapter(adapter);


        //lettura da firebase
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notifiche")
                .child(user.getEmail().substring(0,user.getEmail().indexOf("@")));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificheList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notifica = snapshot.getValue(Notification.class);
                    notificheList.add(0,notifica);
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