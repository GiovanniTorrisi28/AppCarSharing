package com.example.appcarsharing;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
    private Marker myMarker;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationListener locationListener;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pof, container, false);
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);

        // Collega la variabile mapView al layout
        mapView = rootView.findViewById(R.id.map_view);

        // Imposta il livello di zoom e la posizione iniziale
        mapView.getController().setZoom(15);
        mapView.getController().setCenter(new GeoPoint(37.51916027246007, 15.083418417311774));

        addMarkers();

        // Abilita il multi-touch e il pinch-to-zoom
        mapView.setMultiTouchControls(true);

        //controllo dei permessi sulla localizzazione
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permessi non concessi, richiedili
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            if (isPermissionPermanentlyDenied(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // L'utente ha selezionato "Non chiedere mai più" per l'autorizzazione
                showPermissionDeniedDialog();
            }
        } else {
            // Hai già i permessi
            addMyPositionMarker();
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               //permessi concessi
                addMyPositionMarker();
            } else {
               //permessi negati
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void addMyPositionMarker() {
        //posizione dell'utente
        myMarker = new Marker(mapView);
        myMarker.setIcon(getContext().getResources().getDrawable(R.drawable.baseline_circle_24));
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        myMarker.setPosition(new GeoPoint(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
        myMarker.setTitle("Posizione corrente");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                GeoPoint myLocation = new GeoPoint(lat, lon);
                myMarker.setPosition(myLocation);
            }
        };

        mapView.getOverlays().add(myMarker);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

    private boolean isPermissionPermanentlyDenied(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(permission)) {
                return ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    private boolean setting = false;
    private void showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Autorizzazione richiesta");
        builder.setMessage("Per visualizzare la tua posizione corrente sulla mappa è necessaria l'autorizzazione. Vuoi aprire le impostazioni dell'app per abilitare l'autorizzazione?");
        builder.setPositiveButton("Apri impostazioni", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                setting = true;
            }
        });
        builder.setNegativeButton("Annulla", null);

        androidx.appcompat.app.AlertDialog dialog = builder.show();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(setting){
           if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
               addMyPositionMarker();
               setting = false;
           }
        }
    }
}

