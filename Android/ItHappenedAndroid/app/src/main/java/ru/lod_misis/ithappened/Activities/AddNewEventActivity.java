package ru.lod_misis.ithappened.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.NewEvent;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.NewTracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Fragments.DatePickerFragment;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddNewEventActivity extends AppCompatActivity {

    TrackingService trackingService;
    InMemoryFactRepository factRepository;
    ITrackingRepository trackingCollection;

    int commentState;
    int scaleState;
    int ratingState;
    UUID trackingId;

    int year = 0;
    int month = 0;
    int dayOfMonth = 0;

    LinearLayout commentContainer;
    LinearLayout scaleContainer;
    LinearLayout ratingContainer;

    TextView commentAccess;
    TextView scaleAccess;
    TextView ratingAccess;

    EditText commentControl;
    EditText scaleControl;
    RatingBar ratingControl;
    Button dateControl;

    TextView scaleType;

    Button addEvent;

    NewTracking newTracking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        YandexMetrica.reportEvent("Пользователь вошел в создание события");

        SharedPreferences sharedPreferences = getSharedPreferences("MAIN_KEYS", MODE_PRIVATE);
        if(sharedPreferences.getString("LastId","").isEmpty()) {
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("UserId", ""));
            trackingCollection = StaticInMemoryRepository.getInstance();
        }else{
            StaticInMemoryRepository.setUserId(sharedPreferences.getString("LastId", ""));
            trackingCollection = StaticInMemoryRepository.getInstance();
        }
        trackingService = new TrackingService(sharedPreferences.getString("UserId", ""), trackingCollection);

        factRepository = StaticFactRepository.getInstance();

        trackingId = UUID.fromString(getIntent().getStringExtra("trackingId"));


        commentContainer = (LinearLayout) findViewById(R.id.commentEventContainer);
        ratingContainer = (LinearLayout) findViewById(R.id.ratingEventContainer);
        scaleContainer = (LinearLayout) findViewById(R.id.scaleEventContainer);

        commentAccess = (TextView) findViewById(R.id.commentAccess);
        scaleAccess = (TextView) findViewById(R.id.scaleAccess);
        ratingAccess = (TextView) findViewById(R.id.ratingAccess);

        commentControl = (EditText) findViewById(R.id.eventCommentControl);
        scaleControl = (EditText) findViewById(R.id.eventScaleControl);
        ratingControl = (RatingBar) findViewById(R.id.ratingEventControl);
        dateControl = (Button) findViewById(R.id.eventDateControl);

        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);

        scaleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleControl.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(scaleControl, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        scaleType = (TextView) findViewById(R.id.scaleTypeAccess);

        addEvent = (Button) findViewById(R.id.addEvent);

        newTracking = trackingCollection.GetTracking(trackingId);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(newTracking.GetTrackingName());

        commentState = calculateState(newTracking.GetCommentCustomization());
        ratingState = calculateState(newTracking.GetRatingCustomization());
        scaleState = calculateState(newTracking.GetScaleCustomization());

        calculateUX(commentContainer, commentAccess, commentState);
        calculateUX(ratingContainer, ratingAccess, ratingState);
        calculateUX(scaleContainer, scaleAccess, scaleState);

        if(newTracking.GetScaleCustomization()!=TrackingCustomization.None && newTracking.getScaleName()!=null){
                scaleType.setText(newTracking.getScaleName());
        }

        final Date thisDate = Calendar.getInstance(TimeZone.getDefault()).getTime();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getDefault());

        dateControl.setText(format.format(thisDate).toString());


        dateControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DialogFragment picker = new DatePickerFragment(dateControl);
                picker.show(fragmentManager, "from");

            }
        });


        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean commentFlag = true;
                boolean scaleFlag = true;
                boolean ratingFlag = true;

                if(commentState == 2 && commentControl.getText().toString().isEmpty()){
                    commentFlag=false;
                }

                if(ratingState == 2 && ratingControl.getRating() == 0){
                    ratingFlag = false;
                }

                if(scaleState == 2 && scaleControl.getText().toString().isEmpty()){
                    scaleFlag = false;
                }

                String comment = null;
                Double scale = null;
                Rating rating = null;

                if(commentFlag&&ratingFlag&&scaleFlag){
                    if(!commentControl.getText().toString().isEmpty()&&!commentControl.getText().toString().trim().isEmpty()){
                        comment = commentControl.getText().toString().trim();
                    }
                    if(!(ratingControl.getRating()==0)){
                        rating = new Rating((int) (ratingControl.getRating()*2));
                    }
                    if(!scaleControl.getText().toString().isEmpty()){
                        try {
                            scale = Double.parseDouble(scaleControl.getText().toString());
                            Date eventDate = null;
                            Locale locale = new Locale("ru");
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                            simpleDateFormat.setTimeZone(TimeZone.getDefault());
                            try {
                                eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            trackingService.AddEvent(trackingId,new NewEvent(UUID.randomUUID(), trackingId, eventDate, scale, rating, comment));
                            factRepository.onChangeCalculateOneTrackingFacts(trackingCollection.GetTrackingCollection(), trackingId)
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Fact>() {
                                        @Override
                                        public void call(Fact fact) {
                                            Log.d("Statistics", "calculate");
                                        }
                                    });
                            factRepository.calculateAllTrackingsFacts(trackingCollection.GetTrackingCollection())
                                    .subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Fact>() {
                                        @Override
                                        public void call(Fact fact) {
                                            Log.d("Statistics", "calculate");
                                        }
                                    });
                            YandexMetrica.reportEvent("Пользователь добавил событие");
                            Toast.makeText(getApplicationContext(), "Событие добавлено", Toast.LENGTH_SHORT).show();
                            finish();
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "Введите число", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Date eventDate = null;
                        Locale locale = new Locale("ru");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", locale);
                        simpleDateFormat.setTimeZone(TimeZone.getDefault());
                        try {
                            eventDate = simpleDateFormat.parse(dateControl.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        trackingService.AddEvent(trackingId, new NewEvent(UUID.randomUUID(), trackingId, eventDate, scale, rating, comment));
                        factRepository.onChangeCalculateOneTrackingFacts(trackingCollection.GetTrackingCollection(), trackingId)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("Statistics", "calculate");
                                    }
                                });
                        factRepository.calculateAllTrackingsFacts(trackingCollection.GetTrackingCollection())
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Fact>() {
                                    @Override
                                    public void call(Fact fact) {
                                        Log.d("Statistics", "calculate");
                                    }
                                });
                        YandexMetrica.reportEvent("Пользователь добавил событие");
                        Toast.makeText(getApplicationContext(), "Событие добавлено", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Заполните поля с *", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent("Пользователь вышел из создания события");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private int calculateState(TrackingCustomization customization){
        switch (customization){
            case None:
                return 0;
            case Optional:
                return 1;
            case Required:
                return 2;
            default:
                break;
        }
        return 0;
    }

    private void calculateUX(LinearLayout container, TextView access, int state){
        switch (state){
            case 0:
                container.setVisibility(View.GONE);
                break;
            case 1:
                break;
            case 2:
                access.setText(access.getText().toString()+"*");
                break;
            default:
                break;
        }
    }
}


