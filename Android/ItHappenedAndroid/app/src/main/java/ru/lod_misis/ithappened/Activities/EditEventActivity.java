package ru.lod_misis.ithappened.Activities;

import android.app.DialogFragment;
import android.app.FragmentManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

import ru.lod_misis.ithappened.Application.TrackingService;
import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.Rating;
import ru.lod_misis.ithappened.Domain.Tracking;
import ru.lod_misis.ithappened.Domain.TrackingCustomization;
import ru.lod_misis.ithappened.Fragments.DatePickerFragment;
import ru.lod_misis.ithappened.Infrastructure.ITrackingRepository;
import ru.lod_misis.ithappened.Infrastructure.InMemoryFactRepository;
import ru.lod_misis.ithappened.Infrastructure.StaticFactRepository;
import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.StaticInMemoryRepository;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;
import ru.lod_misis.ithappened.Statistics.Facts.StringParse;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditEventActivity extends AppCompatActivity {

    TrackingService trackingService;
    InMemoryFactRepository factRepository;
    ITrackingRepository trackingCollection;

    int commentState;
    int scaleState;
    int ratingState;
    UUID trackingId;
    UUID eventId;


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

    Tracking tracking;
    Event event;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        YandexMetrica.reportEvent("Пользователь зашел в изменение события");

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
        eventId = UUID.fromString(getIntent().getStringExtra("eventId"));


        commentContainer = (LinearLayout) findViewById(R.id.commentEventContainerEdit);
        ratingContainer = (LinearLayout) findViewById(R.id.ratingEventContainerEdit);
        scaleContainer = (LinearLayout) findViewById(R.id.scaleEventContainerEdit);

        commentAccess = (TextView) findViewById(R.id.commentAccessEdit);
        scaleAccess = (TextView) findViewById(R.id.scaleAccessEdit);
        ratingAccess = (TextView) findViewById(R.id.ratingAccessEdit);

        commentControl = (EditText) findViewById(R.id.eventCommentControlEdit);
        scaleControl = (EditText) findViewById(R.id.eventScaleControlEdit);
        ratingControl = (RatingBar) findViewById(R.id.ratingEventControlEdit);
        dateControl = (Button) findViewById(R.id.eventDateControlEdit);

        KeyListener keyListener = DigitsKeyListener.getInstance("-1234567890.");
        scaleControl.setKeyListener(keyListener);

        scaleType = (TextView) findViewById(R.id.scaleTypeAccessEdit);

        addEvent = (Button) findViewById(R.id.editEvent);

        tracking = trackingCollection.GetTracking(trackingId);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(tracking.GetTrackingName());

        event = tracking.GetEvent(eventId);

        commentState = calculateState(tracking.GetCommentCustomization());
        ratingState = calculateState(tracking.GetRatingCustomization());
        scaleState = calculateState(tracking.GetScaleCustomization());

        calculateUX(commentContainer, commentAccess, commentState);
        calculateUX(ratingContainer, ratingAccess, ratingState);
        calculateUX(scaleContainer, scaleAccess, scaleState);

        if(tracking.GetScaleCustomization()!=TrackingCustomization.None && tracking.getScaleName()!=null){
                scaleType.setText(tracking.getScaleName());
        }

        Date thisDate = event.GetEventDate();

        Locale loc = new Locale("ru");
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", loc);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        if((tracking.GetScaleCustomization()==TrackingCustomization.Optional
                || tracking.GetScaleCustomization()==TrackingCustomization.Required) && event.GetScale()!=null){
            scaleControl.setText(StringParse.parseDouble(event.GetScale().doubleValue()));
            if(tracking.getScaleName()!=null){
                if(tracking.getScaleName().length()>=3){
                    scaleType.setText(tracking.getScaleName().substring(0,2));
                }else{
                    scaleType.setText(tracking.getScaleName());
                }
            }
        }

        if((tracking.GetRatingCustomization()==TrackingCustomization.Optional
                || tracking.GetRatingCustomization()==TrackingCustomization.Required) && event.GetRating()!=null){
            ratingControl.setRating(event.GetRating().getRating()/2.0f);
            }

        if((tracking.GetCommentCustomization()==TrackingCustomization.Optional
                || tracking.GetCommentCustomization()==TrackingCustomization.Required) && event.GetComment()!=null){
            commentControl.setText(event.GetComment());
        }



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
                            trackingService.EditEvent(trackingId, eventId,  scale, rating, comment,eventDate);
                            YandexMetrica.reportEvent("Пользователь изменил событие");
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
                            Toast.makeText(getApplicationContext(), "Событие изменено", Toast.LENGTH_SHORT).show();
                            finishActivity();
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
                        trackingService.EditEvent(trackingId, eventId,  scale, rating, comment,eventDate);
                        YandexMetrica.reportEvent("Пользователь изменил событие");
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
                        Toast.makeText(getApplicationContext(), "Событие изменено", Toast.LENGTH_SHORT).show();
                        finishActivity();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Заполните поля с *", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    @Override
    protected void onPause() {
        super.onPause();
        YandexMetrica.reportEvent("Пользователь вышел из изменения события");
    }

    private void finishActivity(){
        this.finish();
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
