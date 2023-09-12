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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ListRideFragment extends Fragment {

    private RecyclerView recyclerView;
    private RideAdapter adapter;
    private List<Ride> passaggiList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_ride, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        passaggiList = new ArrayList<>();
        adapter = new RideAdapter(passaggiList);
        recyclerView.setAdapter(adapter);

        Bundle args = getArguments();

        TextView textView = rootView.findViewById(R.id.textView);
        textView.setText("Risultati:");

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
                    if(checkRide(ride,userId,args))
                        passaggiList.add(ride);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

        });

        return rootView;
    }

    private boolean checkRide(Ride ride,String userId, Bundle args) {

        //controllo se ci sono posti
       if(ride.getPosti().equals("0"))
            return false;

        //controllo sull'essere il guidatore
        if(ride.getGuidatore().getEmail().substring(0,ride.getGuidatore().getEmail().indexOf("@")).equals(userId))
            return false;

        //controllo sulla correttezza della data
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate inputDate = LocalDate.parse(args.getString("date"),dateFormatter);
        LocalDate rideDate = LocalDate.parse(ride.getData());
        if(inputDate.compareTo(rideDate) != 0)
            return false;

        //controllo sulla correttezza della fascia oraria
        LocalTime startTime = LocalTime.parse(args.getString("timeStart"));
        LocalTime endTime = LocalTime.parse(args.getString("timeEnd"));
        LocalTime rideTime = LocalTime.parse(ride.getOrario());
        if(startTime.compareTo(rideTime) > 0 || endTime.compareTo(rideTime) < 0)
            return false;

        //controllo sulla correttezza di sorgente e destinazione
        if(!args.getString("source").equals(ride.getSorgente()) || !args.getString("destination").equals(ride.getDestinazione()))
            return false;

        //controllo sull'essere passeggero
        for(Utente u: ride.getUtenti()){
            if(u.getEmail().substring(0,u.getEmail().indexOf("@")).equals(userId))
                return false;
        }

        return true;
    }
}

