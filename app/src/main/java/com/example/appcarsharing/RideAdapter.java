package com.example.appcarsharing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        private TextView seatsTextView,driverTextView,timeTextView;
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
            driverTextView.setText("Guidatore: " + passaggio.getGuidatore());
            timeTextView.setText("Ora di partenza: " + passaggio.getOrario());
            prenotaBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Esegui l'azione desiderata quando il pulsante viene cliccato
                    List<Utente> passeggeri = passaggio.getUtenti();
                    passeggeri.add(new Utente("email","password","nome","cognome","telefono"));
                    passaggio.setUtenti(passeggeri);
                    //aggiornare i dati su firebase
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("passaggi");
                    Map<String,Object> updates = new HashMap<>();
                    updates.put("utenti",passeggeri);
                    updates.put("posti",String.valueOf(Integer.parseInt(passaggio.getPosti()) - 1));
                    myRef.child(passaggio.getId()).updateChildren(updates);
                }
            });
        }
    }
}
