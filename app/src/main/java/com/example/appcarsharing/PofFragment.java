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
import android.widget.Toast;

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
    private boolean setting = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pof, container, false);

        mapView = rootView.findViewById(R.id.map_view);
        mapView.getController().setZoom(15);
        mapView.getController().setCenter(new GeoPoint(37.51916027246007, 15.083418417311774));

        addMarkers();

        mapView.setMultiTouchControls(true);

        //controllo dei permessi sulla localizzazione
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permessi non concessi, richiedili
            if (isPermissionPermanentlyDenied(Manifest.permission.ACCESS_FINE_LOCATION)) {
                // L'utente ha selezionato "Non chiedere mai più" per l'autorizzazione
                showPermissionDeniedDialog();
            }
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

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
        locationManager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);
        myMarker = new Marker(mapView);
        myMarker.setIcon(getContext().getResources().getDrawable(R.drawable.baseline_circle_24));
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastKnownLocation != null)
           myMarker.setPosition(new GeoPoint(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
        myMarker.setTitle("Posizione corrente");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("GPS disabilitato");
        alertDialogBuilder.setMessage("Riattivalo per vedere la tua posizione corrente");
        AlertDialog alertDialog = alertDialogBuilder.create();
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                GeoPoint myLocation = new GeoPoint(lat, lon);
                myMarker.setPosition(myLocation);
            }

            @Override
            public void onProviderEnabled(String provider) {
                // GPS attivato
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                   // alertDialog.dismiss();
                    mapView.getOverlays().add(myMarker);
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                //GPS disabilitato
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    System.out.println("Gps disattivato");
                    Toast.makeText(getContext(), "Per vedere la tua posizione riattiva il GPS", Toast.LENGTH_LONG).show();
                    mapView.getOverlays().remove(myMarker);
                    mapView.invalidate();
                   // alertDialog.show();
                }
            }

        };

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            // GPS non abilitato, richiedi all'utente di attivarlo
           /* alertDialogBuilder.setTitle("GPS disabilitato");
            alertDialogBuilder.setMessage("Il GPS è disabilitato. Vuoi andare alle impostazioni per abilitarlo?");
            alertDialogBuilder.setPositiveButton("Impostazioni", (dialog, which) -> {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                setting = true;
            });
            alertDialogBuilder.setNegativeButton("Annulla", (dialog, which) -> dialog.cancel());
            alertDialog.show();
            */
            Toast.makeText(getContext(), "Per vedere la tua posizione attiva il GPS", Toast.LENGTH_LONG).show();
        } else {
            // GPS abilitato, procedi con la richiesta di aggiornamenti sulla posizione
            mapView.getOverlays().add(myMarker);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private boolean isPermissionPermanentlyDenied(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(permission)) {
                return ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }


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
           if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
               addMyPositionMarker();
               setting = false;
           }
        }
    }
}

