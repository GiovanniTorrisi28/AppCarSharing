package com.example.appcarsharing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.*;

public class TestFragment extends Fragment {

    private DatabaseReference myRef;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public TestFragment() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Utenti")
                .child(user.getEmail().substring(0, user.getEmail().indexOf("@")));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test, container, false);

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

        return rootView;
    }

    private void getUserData(View rootView) {
        final Utente[] utente = new Utente[1];
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                    utente[0] = snapshot.getValue(Utente.class);

                updateRootView(utente[0], rootView);
                getUserPhoto(rootView);
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

    private void getUserPhoto(View rootView){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child("fotoProfilo/user");
        ImageView imageView = rootView.findViewById(R.id.profile_image_view);

        final long MAX_SIZE = 1024 * 1024; // Dimensione massima dei dati binari da scaricare (1 MB)

        imageRef.getBytes(MAX_SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Converte i byte in un oggetto Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                // Visualizza l'immagine nell'ImageView desiderato
                imageView.setImageBitmap(bitmap);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Gestisci l'errore di caricamento dell'immagine
                System.out.println("fallimento " + exception.toString());
            }
        });
    }
}

