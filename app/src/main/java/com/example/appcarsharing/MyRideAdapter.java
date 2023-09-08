package com.example.appcarsharing;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRideAdapter extends RecyclerView.Adapter<MyRideAdapter.RideViewHolder> {

    private List<Ride> passaggiList;
    private Context context;
    private FirebaseUser user;

    public MyRideAdapter(List<Ride> passaggiList) {
        this.passaggiList = passaggiList;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
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

        private TextView guidatoreTextView, dataOraTextView;
        private Button cancellaBtn, infoBtn;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            dataOraTextView = itemView.findViewById(R.id.data_ora_text_view);
            guidatoreTextView = itemView.findViewById(R.id.guidatore_text_view);
            cancellaBtn = itemView.findViewById(R.id.cancellaButton);
            infoBtn = itemView.findViewById(R.id.infoButton);
        }

        public void bind(Ride passaggio) {

            dataOraTextView.setText(passaggio.getData() + ", " + passaggio.getOrario());

            if(passaggio.getGuidatore().getEmail().equals(user.getEmail()))
               guidatoreTextView.setText("Tu");
            else
                guidatoreTextView.setText(passaggio.getGuidatore().getNome() + " " + passaggio.getGuidatore().getCognome());

            //pulsante info
            infoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInfoDialog(passaggio);
                }
            });

            //pulsante cancella
            cancellaBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //se il guidatore è l'utente loggato
                    if (user.getEmail().substring(0, user.getEmail().indexOf("@")).equals(passaggio.getGuidatore().getKey())) {
                        if(passaggio.getUtenti().size() > 1)
                            showCancellaDialog(passaggio);
                        else{
                            caricaNotifica(passaggio.getUtenti(), getUtenteByEmail(passaggio, user.getEmail()), passaggio.getData());
                            cancellaPassaggio(passaggio);
                        }
                    }
                    //è un passeggero
                    else {
                        caricaNotifica(passaggio.getGuidatore(),getUtenteByEmail(passaggio,user.getEmail()),passaggio.getData());
                        aggiornaPassaggio(passaggio);
                    }
                }
            });

        }

        private void showCancellaDialog(Ride passaggio){
            new MaterialAlertDialogBuilder(context).setTitle("Cancella Passaggio")
                    .setMessage("Sei il guidatore di questo passaggio e ci sono passeggeri, sei sicuro di volerlo cancellare ?")
                    .setPositiveButton("Cancella", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            caricaNotifica(passaggio.getUtenti(), getUtenteByEmail(passaggio, user.getEmail()), passaggio.getData());
                            cancellaPassaggio(passaggio);
                        }
                    }).setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }

        private void cancellaPassaggio(Ride passaggio) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("passaggi").child(passaggio.getId());
            myRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Passaggio eliminato", Toast.LENGTH_LONG);
                        }
                    });
        }

        private void aggiornaPassaggio(Ride passaggio) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("passaggi").child(passaggio.getId());
            Map<String,Object> updates = new HashMap<>();
            passaggio.getUtenti().remove(new Utente(user.getEmail()));
            passaggio.getUtenti();
            updates.put("utenti",passaggio.getUtenti());
            updates.put("posti",String.valueOf(Integer.parseInt(passaggio.getPosti()) + 1));
            myRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error == null)
                        Toast.makeText(context,"Prenotazione cancellata",Toast.LENGTH_LONG);
                    else Toast.makeText(context,"Errore nella cancellazione della prenotazione",Toast.LENGTH_LONG);
                }
            });
        }

        //cosi evito la richiesta a firebase
        private Utente getUtenteByEmail(Ride passaggio, String email){
            for(Utente u: passaggio.getUtenti()){
                if(u.getEmail().equals(email))
                    return u;
            }
            return null;
        }
        private void caricaNotifica(Utente destinatario, Utente mittente, String dataPassaggio) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notifiche");
            String notificaId = myRef.push().getKey();
            myRef.child(destinatario.getKey()).child(notificaId).
                    setValue(new Notification(mittente,
                            "notifica", "Cancellata la prenotazione per il tuo passaggio del " + dataPassaggio,LocalDate.now().toString()))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //sostituire con notifica vera
                                System.out.println("notifica della cancellazione inviata con successo");
                            } else {
                                System.out.println("errore nel caricamneto della notifica");
                            }
                        }
                    });
        }

        private void caricaNotifica(List<Utente> destinatari, Utente mittente, String dataPassaggio) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("notifiche");
            String notificaId = myRef.push().getKey();
            for(int i = 1; i < destinatari.size(); i++) {
                myRef.child(destinatari.get(i).getKey()).child(notificaId).
                        setValue(new Notification(mittente,
                                "notifica", "Cancellato il passaggio del " + dataPassaggio,LocalDate.now().toString()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //sostituire con notifica vera
                                    System.out.println("notifica della cancellazione passaggio inviata con successo");
                                } else {
                                    System.out.println("errore nel caricamneto della notifica cancellazione passaggio");
                                }
                            }
                        });
            }
        }


        private void showInfoDialog(Ride passaggio) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.my_ride_dialog, null);

            TextView sorgenteTextView = dialogView.findViewById(R.id.sorgente_textView);
            TextView destinazioneTextView = dialogView.findViewById(R.id.destinazione_textView);
            TextView targaTextView = dialogView.findViewById(R.id.targa_textView);
            TextView dettagliTextView = dialogView.findViewById(R.id.dettagli_textView);
            TextView passeggeriTextView = dialogView.findViewById(R.id.passeggeri_textView);

            sorgenteTextView.setText(passaggio.getSorgente());
            destinazioneTextView.setText(passaggio.getDestinazione());
            targaTextView.setText(passaggio.getTarga());
            dettagliTextView.setText(passaggio.getDettagliVeicolo());

            String passeggeri = "";
            for(int i = 1; i < passaggio.getUtenti().size(); i++)
                passeggeri += passaggio.getUtenti().get(i).getNome() + " " + passaggio.getUtenti().get(i).getCognome() + ", ";

            passeggeriTextView.setText(passeggeri);

            new MaterialAlertDialogBuilder(context).setTitle("Info passaggio").setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }


    }
}
