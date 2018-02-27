package com.example.ithappenedandroid.Fragments.Statistics;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ithappenedandroid.Fragments.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class DatePickerSatisticsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String formattedDate;

    EditText date;

    TextView datetext;

    TimePickerFragment timePickerFragment;

    public DatePickerSatisticsFragment(TextView datetext){
        this.datetext = datetext;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        if(datetext!=null){
            Locale loc = new Locale("ru");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", loc);

            datetext.setText(format.format(c.getTime()));
        }
    }

}
