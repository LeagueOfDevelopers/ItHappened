package ru.lod_misis.ithappened.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;


@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String formattedDate;

    TextView datetext;

    Button date;

    TimePickerFragment timePickerFragment;

    public DatePickerFragment(TextView datetext){
        this.datetext = datetext;
    }
    public DatePickerFragment(Button date){
        this.date = date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        c.set(year, month, day);
        if(datetext!=null){

            timePickerFragment = new TimePickerFragment(datetext, c);
            timePickerFragment.show(getFragmentManager(), "TimePicker");
        }
        if(date!=null){
            timePickerFragment = new TimePickerFragment(date, c);
            timePickerFragment.show(getFragmentManager(), "TimePicker");
        }
    }

    public String getFormattedDate(){
        return formattedDate;
    }

}
