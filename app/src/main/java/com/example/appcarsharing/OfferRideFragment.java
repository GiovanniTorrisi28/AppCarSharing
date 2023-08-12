package com.example.appcarsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class OfferRideFragment extends Fragment {

    private View rootView;
    private ImageView calendarIcon,timeIcon,carIcon;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private EditText sourceEditText;
    private EditText destinationEditText;

    private boolean[] fieldFilled = new boolean[1];

    private String textTarga,textDettagli;
    private String numPosti;
    HashMap<String, Integer> date,time;

    private DatabaseReference myRef;

    public OfferRideFragment() {
        date = new HashMap<>();
        time = new HashMap<>();
        date.put("day",-1);
        date.put("month",-1);
        date.put("year",-1);
        time.put("hour",-1);
        time.put("minute",-1);
        myRef = FirebaseDatabase.getInstance().getReference();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_offer_ride, container, false);

      /*  for(int i=0;i< fieldFilled.length;i++)
                fieldFilled[i] = false;

        setFieldColor();
     */


        calendarIcon = rootView.findViewById(R.id.calendar_icon);
        // Imposta il listener per gestire la selezione della data
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Qui puoi gestire la data selezionata dall'utente
                date.put("day",dayOfMonth);
                date.put("month",month);
                date.put("year",year);
            }
        };

        // Aggiungi un listener al clic sull'icona del calendario
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(Calendar.getInstance().get(Calendar.DAY_OF_MONTH),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.YEAR));
            }
        });

        //icona orologio
        timeIcon = rootView.findViewById(R.id.time_icon);
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Qui puoi gestire l'orario selezionato dall'utente
                time.put("hour",hourOfDay);
                time.put("minute",minute);
            }
        };

        // Aggiungi un listener al clic sull'icona dell'orario
        timeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE));
            }
        });

        //icona auto
        carIcon = rootView.findViewById(R.id.car_icon);
        carIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creazione e visualizzazione del dialog
                openCarDialog();
            }
        });


         //spinner per sorgente e destinazione
        Spinner sourceSpinner = rootView.findViewById(R.id.sourceSpinner);
        Spinner destinationSpinner = rootView.findViewById(R.id.destinationSpinner);

       // Popolare gli spinner con le opzioni desiderate
        String[] sourceOptions = {"Sorgente", "Opzione 2", "Opzione 3"};
        String[] destinationOptions = {"Destinazione", "Opzione B", "Opzione C"};

        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sourceOptions);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, destinationOptions);

        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sourceSpinner.setAdapter(sourceAdapter);
        destinationSpinner.setAdapter(destinationAdapter);

        //gestione invio
        Button submitButton = rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedSourceOption = (String) sourceSpinner.getSelectedItem();
                String selectedDestinationOption = (String) destinationSpinner.getSelectedItem();
                //controllo sulla sorgente
                if(selectedSourceOption.equals(sourceOptions[0])){
                    Toast.makeText(getContext(), "Inserisci sorgente", Toast.LENGTH_SHORT).show();
                    return;
                }
                //controllo sulla destinazione
                if(selectedDestinationOption.equals(destinationOptions[0])) {
                    Toast.makeText(getContext(), "Inserisci destinazione", Toast.LENGTH_SHORT).show();
                    return;
                }
                //controllo sulla targa
                if(TextUtils.isEmpty(textTarga)) {
                    Toast.makeText(getContext(), "Inserisci targa, clicca su Veicolo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(date.get("day").equals(-1) || date.get("month").equals(-1) || date.get("year").equals(-1)) {
                    Toast.makeText(getContext(), "Inserisci data", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(time.get("hour").equals(-1) || time.get("minute").equals(-1)) {
                    Toast.makeText(getContext(), "Inserisci orario", Toast.LENGTH_SHORT).show();
                    return;
                }

                //carica il nuovo passaggio nel db di realtime
                ArrayList<Utente> utenti = new ArrayList<>();
                utenti.add(new Utente("gio","pass","n","c","1222"));
                Ride ride = new Ride(selectedSourceOption,selectedDestinationOption,
                        LocalDate.of(date.get("year"),date.get("month") + 1,date.get("day")).toString(),
                        LocalTime.of(time.get("hour"),time.get("minute"),0).toString(),
                        textTarga,textDettagli,numPosti,utenti);
                myRef.child("test").setValue(ride)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // L'operazione di scrittura è stata completata con successo
                                            // Esegui qui le azioni desiderate in caso di successo
                                            Toast.makeText(getContext(), "Passaggio creato", Toast.LENGTH_LONG).show();

                                        } else {
                                            // Si è verificato un errore durante l'operazione di scrittura
                                            // Esegui qui le azioni desiderate in caso di errore
                                            Toast.makeText(getContext(), "Errore nella creazione del passaggio", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                }
                        );

            }
        });

        return rootView;
    }

    // Mostra il DatePicker
    private void showDatePicker(Integer day,Integer month, Integer year) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void showTimePicker(Integer hour, Integer minute) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                timeSetListener,
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    private void openCarDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_car_dialog, null);

        TextInputEditText targaEditText = dialogView.findViewById(R.id.targa_edit_text);
        TextInputEditText dettagliEditText = dialogView.findViewById(R.id.dettagli_veicolo_edit_text);
        TextInputEditText postiEditText = dialogView.findViewById(R.id.posti_edit_text);

        targaEditText.setText(textTarga);
        dettagliEditText.setText(textDettagli);
        postiEditText.setText(numPosti);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Dati veicolo")
                .setView(dialogView)
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textTarga = targaEditText.getText().toString();
                        textDettagli = dettagliEditText.getText().toString();
                        numPosti = postiEditText.getText().toString();

                        /*
                        Era codice per il colore della text view, da rivedere
                        if(!textTarga.equals("") && !textDettagli.equals("")) {
                            fieldFilled[0] = true;
                        }
                        else fieldFilled[0] = false;
                            setFieldColor();
                        */
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    //da rivedere
    private void setFieldColor() {
        TextView textViewVeicolo = rootView.findViewById(R.id.textView_veicolo);
        if(!fieldFilled[0])
            textViewVeicolo.setTextColor(Color.RED);
        else textViewVeicolo.setTextColor(Color.GREEN);
    }

    //fare anche le altre caselle ??
}
