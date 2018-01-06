package com.example.ithappenedandroid.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Пользователь on 30.12.2017.
 */

@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String formattedDate;

    EditText date;

    TextView datetext;

    @SuppressLint("ValidFragment")
    public DatePickerFragment(EditText date){
        this.date = date;
    }

    public DatePickerFragment(TextView datetext){
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
        if(date!=null) {
            date.setText(c.getTime().toLocaleString());
        }
        if(datetext!=null){
            datetext.setText(c.getTime().toLocaleString());
        }
    }

    public String getFormattedDate(){
        return formattedDate;
    }

}
