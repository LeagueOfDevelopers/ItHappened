package ru.lod_misis.ithappened.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.lod_misis.ithappened.R;

public class ProfileSettingsFragment extends Fragment {

    TextView userMail;
    TextView userNickName;
    TextView logOut;

    TextView policy;

    CircleImageView urlUser;

    ProgressBar syncPB;
    FrameLayout layoutFrg;

    Button editNickName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_settings, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);


    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Настройки профиля");

        syncPB = (ProgressBar) getActivity().findViewById(R.id.syncPB);
        layoutFrg = (FrameLayout) getActivity().findViewById(R.id.trackingsFrg);

        userMail =(TextView) getActivity().findViewById(R.id.mail);
        userNickName = (TextView) getActivity().findViewById(R.id.nickname);
        logOut = (TextView) getActivity().findViewById(R.id.logout);
        editNickName = (Button) getActivity().findViewById(R.id.editNickName);
        urlUser = (CircleImageView) getActivity().findViewById(R.id.userAvatar);

        policy = (TextView) getActivity().findViewById(R.id.policy);


        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);

        new ProfileSettingsFragment.DownLoadImageTask(urlUser).execute(sharedPreferences.getString("Url", ""));

        userMail.setText(sharedPreferences.getString("UserId", ""));
        userNickName.setText(sharedPreferences.getString("Nick", ""));


        String mystring=new String("Выйти");
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        logOut.setText(content);

        mystring = new String("Политика конфиденциальности");
        content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);
        policy.setText(content);

        editNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNicknameDialogFragment dialogFragment = new EditNicknameDialogFragment();
                dialogFragment.show(getFragmentManager(), "editNickName");
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOutDailogFragment logout = new LogOutDailogFragment();
                logout.show(getFragmentManager(), "Logout");
            }
        });

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebURL("http://85.143.104.47:1080/privacy/policy");
            }
        });
    }



    public void openWebURL( String inURL ) {
        Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );

        startActivity( browse );
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MAIN_KEYS", Context.MODE_PRIVATE);
        userMail.setText(sharedPreferences.getString("UserId", ""));
        userNickName.setText(sharedPreferences.getString("Nick", ""));
    }


    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        CircleImageView imageView;

        public DownLoadImageTask(CircleImageView imageView){
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
