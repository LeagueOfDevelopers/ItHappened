package ru.lod_misis.ithappened.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("ValidFragment")
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    TextView dateTimeText;
    String formatedDate;
    Calendar c;
    Button date;

    Date thisDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
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

    public TimePickerFragment(Button date, Calendar c){
        this.date = date;
        this.c = c;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {

        Calendar time = Calendar.getInstance();
        time.setTimeZone(TimeZone.getDefault());
        Date timeDate;
        time.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH));
        time.set(Calendar.MONTH, c.get(Calendar.MONTH));
        time.set(Calendar.YEAR, c.get(Calendar.YEAR));
        time.set(Calendar.HOUR_OF_DAY, hour);
        time.set(Calendar.MINUTE, min);
        time.set(Calendar.MILLISECOND, 0);

        if(dateTimeText!=null){

            Locale loc = new Locale("ru");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
            format.setTimeZone(TimeZone.getDefault());

            dateTimeText.setText(format.format(time.getTime()));

        }

        if(date!=null){
            Locale loc = new Locale("ru");
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
            format.setTimeZone(TimeZone.getDefault());

            date.setTextSize(10);
            date.setText(format.format(time.getTime()));
        }

    }
}
