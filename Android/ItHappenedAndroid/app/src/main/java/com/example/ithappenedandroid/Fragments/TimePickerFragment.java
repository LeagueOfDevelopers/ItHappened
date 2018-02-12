package com.example.ithappenedandroid.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Пользователь on 17.01.2018.
 */

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TextView dateTimeText;
    String formatedDate;
    Calendar c;

    Date thisDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @SuppressLint("ValidFragment")
    public TimePickerFragment(TextView dateTimeText, Calendar c){
        this.dateTimeText = dateTimeText;
        this.c = c;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {

        Calendar time = Calendar.getInstance();

        Date timeDate;
        time.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-1);
        time.set(Calendar.MONTH, c.get(Calendar.MONTH));
        time.set(Calendar.YEAR, c.get(Calendar.YEAR));
        time.set(Calendar.HOUR, hour+12);
        time.set(Calendar.MINUTE, min);
        time.set(Calendar.MILLISECOND, 0);

        if(dateTimeText!=null){

            Locale loc = new Locale("ru");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);

            dateTimeText.setText(format.format(time.getTime()));

        }

    }
}
