package com.example.appcarsharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import java.util.*;

public class ListRideFragment extends Fragment {

    private RecyclerView recyclerView;
    private RideAdapter adapter;
    private List<Ride> passaggiList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_ride, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        // Inizializza e imposta il LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Inizializza la lista dei passaggi (puoi popolarla con i dati da Firebase)
        passaggiList = new ArrayList<>();

        // Inizializza l'adattatore e collegalo al RecyclerView
        adapter = new RideAdapter(passaggiList);
        recyclerView.setAdapter(adapter);


        //lettura dei parametri
        TextView textView = rootView.findViewById(R.id.textView);
        Bundle args = getArguments();
        textView.setText("Data: " + args.getString("date") +  "\n"
                + "Fascia Oraria: " + args.getString("timeStart") + "-" + args.getString("timeEnd") + "\n"
                + "Sorgente: " + args.getString("source") + "\n"
                + "Destinazione: " + args.getString("destination"));

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userId = email.substring(0, email.indexOf("@"));

        //aggiungi dati alla lista
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("passaggi");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                passaggiList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Ride ride = snapshot.getValue(Ride.class);
                    //controlli
                    boolean isPasseggero = false;
                    if(ride.getGuidatore().equals(userId))
                        continue;
                    for(Utente u: ride.getUtenti()){
                        if(u.getEmail().substring(0,u.getEmail().indexOf("@")).equals(userId)){
                            isPasseggero = true;
                            break;
                        }
                    }
                    if(!isPasseggero)
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

