package com.example.appcarsharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class PofFragment extends Fragment {

    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_test, container, false);
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));

        // Collega la variabile mapView al layout
        mapView = rootView.findViewById(R.id.map_view);

        // Imposta il livello di zoom e la posizione iniziale
        mapView.getController().setZoom(20);
        mapView.getController().setCenter(new GeoPoint(37.502497, 15.087232)); // Esempio: New York City

        addMarkers();

        // Abilita il multi-touch e il pinch-to-zoom
        mapView.setMultiTouchControls(true);


        return rootView;
    }


    private void addMarkers() {
        List<OverlayItem> items = new ArrayList<>();

        // Aggiungi marcatori alle coordinate specifiche
        GeoPoint marker1 = new GeoPoint(37.502497, 15.087232);
        OverlayItem overlayItem1 = new OverlayItem("Marker 1", "Descrizione Marker 1", marker1);
        items.add(overlayItem1);

        // Puoi aggiungere più marcatori in modo simile

        // Crea l'overlay dei marcatori
        ItemizedOverlayWithFocus<OverlayItem> overlay = new ItemizedOverlayWithFocus<>(getContext(), items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        // Mostra la dialog al click su un marker
                        showDialog(item.getTitle(), item.getSnippet());
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });

        // Aggiungi l'overlay alla mappa
        mapView.getOverlays().add(overlay);
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

