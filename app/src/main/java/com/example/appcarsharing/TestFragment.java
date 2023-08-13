package com.example.appcarsharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import java.util.*;

public class TestFragment extends Fragment {

    private RecyclerView recyclerView;
    private RideAdapter adapter;
    private List<Ride> passaggiList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);

        // Inizializza e imposta il LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Inizializza la lista dei passaggi (puoi popolarla con i dati da Firebase)
        passaggiList = new ArrayList<>();

        // Inizializza l'adattatore e collegalo al RecyclerView
        adapter = new RideAdapter(passaggiList);
        recyclerView.setAdapter(adapter);
        passaggiList.add(new Ride("sorgente","destinazione","3"));
        // Recupera i dati dei passaggi da Firebase e aggiungili alla lista passaggiList
        // ...

        return rootView;
    }
}

