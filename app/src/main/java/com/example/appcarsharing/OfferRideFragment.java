package com.example.appcarsharing;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class OfferRideFragment extends Fragment {

    private ImageView calendarIcon,timeIcon,carIcon;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;

    private EditText sourceEditText;
    private EditText destinationEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offer_ride, container, false);

        calendarIcon = rootView.findViewById(R.id.calendar_icon);

        // Imposta il listener per gestire la selezione della data
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Qui puoi gestire la data selezionata dall'utente
            }
        };

        // Aggiungi un listener al clic sull'icona del calendario
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        timeIcon = rootView.findViewById(R.id.time_icon);
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Qui puoi gestire l'orario selezionato dall'utente
            }
        };

        // Aggiungi un listener al clic sull'icona dell'orario
        timeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
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


        //form
       /* sourceEditText = rootView.findViewById(R.id.sourceEditText);
        destinationEditText = rootView.findViewById(R.id.destinationEditText);

        Button submitButton = rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String source = sourceEditText.getText().toString();
                String destination = destinationEditText.getText().toString();

                // Fai qualcosa con i valori delle caselle di testo
            }
        });
*/
        return rootView;
    }

    // Mostra il DatePicker
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                timeSetListener,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void openCarDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_car_dialog, null);

        TextInputEditText targaEditText = dialogView.findViewById(R.id.targa_edit_text);
        TextInputEditText dettagliEditText = dialogView.findViewById(R.id.dettagli_veicolo_edit_text);

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Title")
                .setView(dialogView)
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String targa = targaEditText.getText().toString();
                        String dettagli = dettagliEditText.getText().toString();

                        // Utilizza targa e dettagli come necessario
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


}
