package com.example.appcarsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;

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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

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
    HashMap<String, Integer> date,time;

    public OfferRideFragment() {
        date = new HashMap<>();
        time = new HashMap<>();
        date.put("day",0);
        date.put("month",0);
        date.put("year",0);
        time.put("hour",-1);
        time.put("minute",-1);
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
                showDatePicker(date.get("day"),date.get("month"),date.get("year"));
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
                showTimePicker(time.get("hour"),time.get("minute"));
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

            }
        });

        return rootView;
    }

    // Mostra il DatePicker
    private void showDatePicker(Integer day,Integer month, Integer year) {
        year = (year.equals(0) ? Calendar.getInstance().get(Calendar.YEAR) : year);
        month = (month.equals(0) ? Calendar.getInstance().get(Calendar.MONTH) : month);
        day = (day.equals(0) ? Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : day);

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
        hour = (hour.equals(-1) ? Calendar.getInstance().get(Calendar.HOUR_OF_DAY) : hour);
        minute = (minute.equals(-1) ? Calendar.getInstance().get(Calendar.MINUTE) : minute);

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

        targaEditText.setText(textTarga);
        dettagliEditText.setText(textDettagli);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Dati veicolo")
                .setView(dialogView)
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textTarga = targaEditText.getText().toString();
                        textDettagli = dettagliEditText.getText().toString();
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

    private void setFieldColor() {
        TextView textViewVeicolo = rootView.findViewById(R.id.textView_veicolo);
        if(!fieldFilled[0])
            textViewVeicolo.setTextColor(Color.RED);
        else textViewVeicolo.setTextColor(Color.GREEN);
    }

    //fare anche le altre caselle ??
}
