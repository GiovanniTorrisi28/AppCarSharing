package com.example.appcarsharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
        View rootView =  inflater.inflate(R.layout.fragment_pof, container, false);
        Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));

        // Collega la variabile mapView al layout
        mapView = rootView.findViewById(R.id.map_view);

        // Imposta il livello di zoom e la posizione iniziale
        mapView.getController().setZoom(15);
        mapView.getController().setCenter(new GeoPoint(37.51916027246007, 15.083418417311774));

        addMarkers();

        // Abilita il multi-touch e il pinch-to-zoom
        mapView.setMultiTouchControls(true);


        return rootView;
    }


    private void addMarkers() {
        List<OverlayItem> items = new ArrayList<>();

        // Aggiungi marcatori alle coordinate specifiche
        GeoPoint marker1 = new GeoPoint(37.52427537205766, 15.070995366336996);
        OverlayItem overlayItem1 = new OverlayItem("Cittadella Universitaria", "Descrizione Marker 1", marker1);
        overlayItem1.setMarker(getContext().getResources().getDrawable(R.drawable.ic_marker_item));
        items.add(overlayItem1);

        GeoPoint marker2 = new GeoPoint(37.50374671642855, 15.080111448460787);
        OverlayItem overlayItem2 = new OverlayItem("Monastero dei Benedettini", "Descrizione Marker 1", marker2);
        overlayItem2.setMarker(getContext().getResources().getDrawable(R.drawable.ic_marker_item));
        items.add(overlayItem2);

        GeoPoint marker3 = new GeoPoint(37.52983938041185, 15.067811450991744);
        OverlayItem overlayItem3 = new OverlayItem("Torre Biologica", "Descrizione Marker 1", marker3);
        overlayItem3.setMarker(getContext().getResources().getDrawable(R.drawable.ic_marker_item));
        items.add(overlayItem3);

        GeoPoint marker4 = new GeoPoint(37.515439382057885, 15.09544739517285);
        OverlayItem overlayItem4 = new OverlayItem("Palazzo delle Scienze", "Descrizione Marker 1", marker4);
        overlayItem4.setMarker(getContext().getResources().getDrawable(R.drawable.ic_marker_item));
        items.add(overlayItem4);

        GeoPoint marker5 = new GeoPoint(37.50660797206224, 15.084817295172298);
        OverlayItem overlayItem5 = new OverlayItem("Villa Cerami", "Descrizione Marker 1", marker5);
        overlayItem5.setMarker(getContext().getResources().getDrawable(R.drawable.ic_marker_item));
        items.add(overlayItem5);


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

