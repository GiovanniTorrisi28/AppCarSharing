package com.example.appcarsharing;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRideAdapter extends RecyclerView.Adapter<MyRideAdapter.RideViewHolder> {

    private List<Ride> passaggiList;
    Context context;

    public MyRideAdapter(List<Ride> passaggiList) {
        this.passaggiList = passaggiList;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_ride, parent, false);
        context = parent.getContext();
        return new RideViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride passaggio = passaggiList.get(position);
        holder.bind(passaggio);
    }

    @Override
    public int getItemCount() {
        return passaggiList.size();
    }

    public class RideViewHolder extends RecyclerView.ViewHolder {

        private TextView  guidatoreTextView,postiTextView;
        private Button cancellaBtn, infoBtn;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            postiTextView = itemView.findViewById(R.id.posti_text_view);
            guidatoreTextView = itemView.findViewById(R.id.guidatore_text_view);
            cancellaBtn = itemView.findViewById(R.id.cancellaButton);
            infoBtn = itemView.findViewById(R.id.infoButton);
        }

        public void bind(Ride passaggio) {
            postiTextView.setText("Posti disponibili: " + passaggio.getPosti());
            guidatoreTextView.setText("Guidatore: " + passaggio.getGuidatore().getNome() + " " + passaggio.getGuidatore().getCognome());

            //pulsante info
            infoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(passaggio);
                }
            });

            //pulsante cancella
            cancellaBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //prendi i dati dell'utente
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String id = email.substring(0, email.indexOf("@"));
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Utenti").child(id);
                    final Utente[] utente = {new Utente()};
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                utente[0] = dataSnapshot.getValue(Utente.class);
                                //carica l'utente nella lista dei passeggeri

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("errore nella lettura dell'utente da firebase, in ride adapter");
                        }
                    });

                }
            });
        }

        private void showDialog(Ride passaggio) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.my_ride_dialog, null);

            TextView sorgenteTextView = dialogView.findViewById(R.id.sorgente_textView);
            TextView destinazioneTextView = dialogView.findViewById(R.id.destinazione_textView);
            TextView dataTextView = dialogView.findViewById(R.id.data_textView);
            TextView oraTextView = dialogView.findViewById(R.id.ora_textView);
            TextView targaTextView = dialogView.findViewById(R.id.targa_textView);
            TextView dettagliTextView = dialogView.findViewById(R.id.dettagli_textView);

            sorgenteTextView.setText(passaggio.getSorgente());
            destinazioneTextView.setText(passaggio.getDestinazione());
            dataTextView.setText(passaggio.getData());
            oraTextView.setText(passaggio.getOrario());
            targaTextView.setText(passaggio.getTarga());
            dettagliTextView.setText(passaggio.getDettagliVeicolo());

            new MaterialAlertDialogBuilder(context).setTitle("Info passaggio").setView(dialogView).setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();

        }


    }
}
