package com.example.appcarsharing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

public class AskRideFragment extends Fragment {

    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private ImageView timeIconStart,timeIconEnd;
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
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Qui puoi gestire l'orario selezionato dall'utente
                  System.out.println("Debug " + hourOfDay + " " + minute);
            }
        };
        View.OnClickListener handleClickTime = new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(Calendar.getInstance().get(Calendar.HOUR_OF_DAY),Calendar.getInstance().get(Calendar.MINUTE));
            }
        };

        timeIconStart.setOnClickListener(handleClickTime);
        timeIconEnd.setOnClickListener(handleClickTime);

        return rootView;
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


}