package com.example.appcarsharing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.HashMap;

public class AskRideFragment extends Fragment {

    private TimePickerDialog.OnTimeSetListener timeStartSetListener,timeEndSetListener;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private ImageView timeIconStart,timeIconEnd,calendarIcon;
    private HashMap<String, Integer> date;
    private HashMap<String,Integer> timeStart,timeEnd;

    public AskRideFragment() {
        date = new HashMap<>();
        timeStart = new HashMap<>();
        timeEnd = new HashMap<>();
        date.put("day",-1);
        date.put("month",-1);
        date.put("year",-1);
        timeStart.put("hour",-1);
        timeStart.put("minute",-1);
        timeEnd.put("hour",-1);
        timeEnd.put("minute",-1);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ask_ride, container, false);

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


        timeIconStart = rootView.findViewById(R.id.time_icon_start);
        timeIconEnd = rootView.findViewById(R.id.time_icon_end);
        timeStartSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Qui puoi gestire l'orario selezionato dall'utente
                  timeStart.put("hour",hourOfDay);
                  timeStart.put("minute",minute);
            }
        };

        timeEndSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Qui puoi gestire l'orario selezionato dall'utente
                timeEnd.put("hour",hourOfDay);
                timeEnd.put("minute",minute);
            }
        };
        View.OnClickListener handleClickTimeStart = new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),timeStartSetListener);
            }
        };

        View.OnClickListener handleClickTimeEnd = new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE),timeEndSetListener);
            }
        };

        timeIconStart.setOnClickListener(handleClickTimeStart);
        timeIconEnd.setOnClickListener(handleClickTimeEnd);

        //icona del calendario
        calendarIcon = rootView.findViewById(R.id.calendar_icon);
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


        //gestione invio
        Button submitButton = rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 System.out.println("debug");
                String selectedSourceOption = (String) sourceSpinner.getSelectedItem();
                String selectedDestinationOption = (String) destinationSpinner.getSelectedItem();
                //controllo sulla sorgente
                if (selectedSourceOption.equals(sourceOptions[0])) {
                    Toast.makeText(getContext(), "Inserisci sorgente", Toast.LENGTH_SHORT).show();
                    return;
                }
                //controllo sulla destinazione
                if (selectedDestinationOption.equals(destinationOptions[0])) {
                    Toast.makeText(getContext(), "Inserisci destinazione", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (date.get("day").equals(-1) || date.get("month").equals(-1) || date.get("year").equals(-1)) {
                    Toast.makeText(getContext(), "Inserisci data", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (timeStart.get("hour").equals(-1) || timeStart.get("minute").equals(-1)) {
                    Toast.makeText(getContext(), "Inserisci inizio fascia oraria", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (timeEnd.get("hour").equals(-1) || timeEnd.get("minute").equals(-1)) {
                    Toast.makeText(getContext(), "Inserisci fine fascia oraria", Toast.LENGTH_SHORT).show();
                    return;
                }
            }});

                return rootView;
    }

    private void showTimePicker(Integer hour, Integer minute,TimePickerDialog.OnTimeSetListener listener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                listener,
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

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


}