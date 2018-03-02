package ru.lod_misis.ithappened.Fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.lod_misis.ithappened.R;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Пользователь on 16.02.2018.
 */

public class EditNicknameDialogFragment extends DialogFragment {

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_edit_nickname, null)).setTitle("Изменение имени")
                .setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText nicname = getDialog().findViewById(R.id.username);
                        if(nicname.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(), "Имя не может быть пустым!", Toast.LENGTH_SHORT).show();
                        }else {
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Nick", nicname.getText().toString());
                            editor.putLong("NickDate", Calendar.getInstance(TimeZone.getDefault()).getTime().getTime());
                            editor.commit();

                            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                            View hView =  navigationView.getHeaderView(0);
                            TextView user = (TextView) hView.findViewById(R.id.userNickname);
                            user.setText(nicname.getText().toString());

                            RelativeLayout rl = (RelativeLayout) getActivity().findViewById(R.id.rl);

                            FragmentManager fm = getActivity().getFragmentManager();

                            ProfileSettingsFragment prof = new ProfileSettingsFragment();

                            FragmentTransaction fTrans = getFragmentManager().beginTransaction();
                            fTrans.replace(R.id.trackingsFrg, prof);
                            fTrans.commit();
                        }
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

}
