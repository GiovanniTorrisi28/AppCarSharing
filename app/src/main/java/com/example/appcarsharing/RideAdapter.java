package com.example.appcarsharing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {

    private List<Ride> passaggiList;

    public RideAdapter(List<Ride> passaggiList) {
        this.passaggiList = passaggiList;
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passaggio, parent, false);
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

        private TextView seatsTextView, driverTextView, timeTextView;
        private Button prenotaBtn;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            seatsTextView = itemView.findViewById(R.id.posti_text_view);
            driverTextView = itemView.findViewById(R.id.guidatore_text_view);
            timeTextView = itemView.findViewById(R.id.ora_text_view);
            prenotaBtn = itemView.findViewById(R.id.prenotaButton);
        }

        public void bind(Ride passaggio) {

            seatsTextView.setText("Posti disponibili: " + String.valueOf(passaggio.getPosti()));
            driverTextView.setText("Guidatore: " + passaggio.getGuidatore().getNome() + " " + passaggio.getGuidatore().getCognome());
            timeTextView.setText("Ora di partenza: " + passaggio.getOrario());
            prenotaBtn.setOnClickListener(new View.OnClickListener() {
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
                                loadPasseggero(passaggio, utente[0], v);
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

        public void loadPasseggero(Ride passaggio, Utente utente, View v) {
            List<Utente> passeggeri = passaggio.getUtenti();
            passeggeri.add(utente);
            passaggio.setUtenti(passeggeri);
            //aggiornare i dati su firebase
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("passaggi");
            Map<String, Object> updates = new HashMap<>();
            updates.put("utenti", passeggeri);
            updates.put("posti", String.valueOf(Integer.parseInt(passaggio.getPosti()) - 1));
            myRef.child(passaggio.getId()).updateChildren(updates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(v.getContext(), "Passaggio prenotato", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(v.getContext(), "Errore nella prenotazione del passaggio", Toast.LENGTH_LONG).show();
                    }
                }
            });

            //carica la notifica
            myRef = FirebaseDatabase.getInstance().getReference("notifiche");
            String notificaId = myRef.push().getKey();
            myRef.child(passaggio.getGuidatore().getKey()).child(notificaId).
                    setValue(new Notification(utente.getNome() + " " + utente.getCognome(),
                            "notifica", "Nuova prenotazione per il tuo passaggio del " + passaggio.getData()))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //sostituire con notifica vera
                                Toast.makeText(v.getContext(), "Notifica", Toast.LENGTH_LONG).show();
                            } else {
                                System.out.println("errore nel caricamneto della notifica");
                            }
                        }
                    });
        }


    }
}
