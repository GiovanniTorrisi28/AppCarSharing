package com.example.appcarsharing;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.*;

public class ProfileFragment extends Fragment {

    private DatabaseReference myRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ImageView imageView;
    private ProgressBar progressBar;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public ProfileFragment() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Utenti")
                .child(user.getEmail().substring(0, user.getEmail().indexOf("@")));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        progressBar = rootView.findViewById(R.id.progressBar);
        //riempie la view con i dati utente
        getUserData(rootView);

        //pulsante logout
        TextView logout = rootView.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent loginIntent = new Intent(getContext(),Login.class);
                startActivity(loginIntent);
                getActivity().finish();
            }
        });

        //pulsante dei miei passaggi
        Button myRideButton = rootView.findViewById(R.id.myRide_btn);
        myRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cambio di fragment
                MyRideFragment newFragment = new MyRideFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //click sulla foto profilo
        imageView = rootView.findViewById(R.id.profile_image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission();
            }
        });

        return rootView;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //Permessi non concessi, richiedili
            if (isPermissionPermanentlyDenied(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // L'utente ha selezionato "Non chiedere mai più" per l'autorizzazione
                showPermissionDeniedDialog();
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);

        } else {
            // Hai già i permessi
            openImagePicker();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // L'autorizzazione è stata concessa
                openImagePicker();
            } else {
                // L'autorizzazione è stata negata
            }
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
        builder.setMessage("Per cambiare foto, devi concedere l'autorizzazione per accedere ai file. Vuoi aprire le impostazioni dell'app per abilitare l'autorizzazione?");
        builder.setPositiveButton("Apri impostazioni", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annulla", null);

        AlertDialog dialog = builder.show();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference imageRef = storage.getReference().child("fotoProfilo").child(user.getEmail().substring(0, user.getEmail().indexOf("@")));
            UploadTask uploadTask = imageRef.putFile(data.getData());
            progressBar.setVisibility(View.VISIBLE);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Upload completato con successo
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Picasso.get().load(imageUrl).into(imageView);
                    });
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Gestisci l'errore durante l'upload
                    Toast.makeText(getContext(), "Error loading image",
                            Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void getUserData(View rootView) {
        final Utente[] utente = new Utente[1];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                    utente[0] = snapshot.getValue(Utente.class);

                updateRootView(utente[0], rootView);
                getUserPhoto(rootView,utente[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateRootView(Utente utente, View rootView) {

        EditText emailEditText = rootView.findViewById(R.id.profile_email);
        EditText phoneEditText = rootView.findViewById(R.id.profile_phone);
        EditText nameSurnameEditText = rootView.findViewById(R.id.profile_name_surname);

        emailEditText.setText(utente.getEmail());
        phoneEditText.setText(utente.getTelefono());
        nameSurnameEditText.setText(utente.getNome() + " " + utente.getCognome());
    }

    private void getUserPhoto(View rootView,Utente utente){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child("fotoProfilo/" + utente.getKey());

        ImageView imageView = rootView.findViewById(R.id.profile_image_view);

        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            Picasso.get().load(imageUrl).into(imageView);
        }).addOnFailureListener(exception -> {
            System.out.println("Errore nel caricamento della foto profilo");
        });
    }

    private void changeUserPhoto(View rootView,Utente utente){


    }
}

