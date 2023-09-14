package com.example.appcarsharing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.TestLooperManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

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

        View rootView = inflater.inflate(R.layout.fragment_ask_ride, container, false);

        Bundle args = getArguments();

        Spinner sourceSpinner = rootView.findViewById(R.id.sourceSpinner);
        Spinner destinationSpinner = rootView.findViewById(R.id.destinationSpinner);
        String[] sourceOptions = {"Partenza", "Cittadella Universitaria", "Monastero dei Benedettini", "Torre Biologica", "Palazzo delle Scienze", "Villa Cerami"};
        String[] destinationOptions = {"Destinazione", "Cittadella Universitaria", "Monastero dei Benedettini", "Torre Biologica", "Palazzo delle Scienze", "Villa Cerami"};
        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sourceOptions);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, destinationOptions);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(sourceAdapter);
        destinationSpinner.setAdapter(destinationAdapter);
        if(args != null)
           destinationSpinner.setSelection(args.getInt("selectedDestination"));

        timeIconStart = rootView.findViewById(R.id.time_icon_start);
        timeIconEnd = rootView.findViewById(R.id.time_icon_end);
        TextView timeStartText = rootView.findViewById(R.id.timeStartText);
        TextView timeEndText = rootView.findViewById(R.id.timeEndText);

        timeStartSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                  timeStart.put("hour",hourOfDay);
                  timeStart.put("minute",minute);
            }
        };

        timeEndSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeEnd.put("hour",hourOfDay);
                timeEnd.put("minute",minute);
            }
        };
        View.OnClickListener handleClickTimeStart = new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer hour = timeStart.get("hour").equals(-1) ? Calendar.getInstance().get(Calendar.HOUR_OF_DAY) : timeStart.get("hour");
                Integer minute = timeStart.get("minute").equals(-1) ? Calendar.getInstance().get(Calendar.MINUTE) : timeStart.get("minute");
                showTimePicker(hour,minute,timeStartSetListener);
            }
        };

        View.OnClickListener handleClickTimeEnd = new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer hour = timeEnd.get("hour").equals(-1) ? Calendar.getInstance().get(Calendar.HOUR_OF_DAY) : timeEnd.get("hour");
                Integer minute = timeEnd.get("minute").equals(-1) ? Calendar.getInstance().get(Calendar.MINUTE) : timeEnd.get("minute");
                showTimePicker(hour,minute,timeEndSetListener);
            }
        };

        timeIconStart.setOnClickListener(handleClickTimeStart);
        timeIconEnd.setOnClickListener(handleClickTimeEnd);
        timeStartText.setOnClickListener(handleClickTimeStart);
        timeEndText.setOnClickListener(handleClickTimeEnd);

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

        View.OnClickListener handleClickCalendar = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer day = date.get("day").equals(-1) ? Calendar.getInstance().get(Calendar.DAY_OF_MONTH) : date.get("day");
                Integer month = date.get("month").equals(-1) ? Calendar.getInstance().get(Calendar.MONTH) : date.get("month");
                Integer year = date.get("year").equals(-1) ? Calendar.getInstance().get(Calendar.YEAR) : date.get("year");
                showDatePicker(day,month,year);
            }
        };
        calendarIcon.setOnClickListener(handleClickCalendar);

        TextView calendarText = rootView.findViewById(R.id.calendarText);
        calendarText.setOnClickListener(handleClickCalendar);

        Button submitButton = rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSourceOption = (String) sourceSpinner.getSelectedItem();
                String selectedDestinationOption = (String) destinationSpinner.getSelectedItem();

                if (selectedSourceOption.equals(sourceOptions[0])) {
                    Toast.makeText(getContext(), "Inserisci luogo di partenza", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedDestinationOption.equals(destinationOptions[0])) {
                    Toast.makeText(getContext(), "Inserisci luogo di destinazione", Toast.LENGTH_SHORT).show();
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

                //apertura nuovo fragment
                ListRideFragment newFragment =  new ListRideFragment();
                Bundle args = new Bundle();
                args.putString("date",(date.get("day")  < 10 ? "0" + date.get("day") : date.get("day")) + "/" + (date.get("month") + 1 < 10 ? "0" + (date.get("month") + 1): date.get("month") + 1) + "/" + date.get("year"));
                args.putString("timeStart",(timeStart.get("hour")  < 10 ? "0" + timeStart.get("hour") : timeStart.get("hour")) + ":" + (timeStart.get("minute")  < 10 ? "0" + timeStart.get("minute") : timeStart.get("minute")));
                args.putString("timeEnd",(timeEnd.get("hour")  < 10 ? "0" + timeEnd.get("hour") : timeEnd.get("hour")) + ":" + (timeEnd.get("minute")   < 10 ? "0" + timeEnd.get("minute") : timeEnd.get("minute")));
                args.putString("source",selectedSourceOption);
                args.putString("destination",selectedDestinationOption);
                newFragment.setArguments(args);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Sostituisci il fragment esistente con il nuovo fragment
                fragmentTransaction.replace(R.id.container, newFragment);

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }});
                return rootView;
    }
    
    private void showTimePicker(Integer hour, Integer minute,TimePickerDialog.OnTimeSetListener listener) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                R.style.DialogTheme,
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
                R.style.DialogTheme,
                dateSetListener,
                year,
                month,
                day
        );
        datePickerDialog.show();
    }


}