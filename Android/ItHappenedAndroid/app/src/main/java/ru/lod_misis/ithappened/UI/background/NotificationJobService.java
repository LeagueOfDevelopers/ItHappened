package ru.lod_misis.ithappened.UI.background;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.UUID;

import javax.inject.Inject;

import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.UI.Activities.AddNewEventActivity;
import ru.lod_misis.ithappened.UI.Activities.UserActionsActivity;
import ru.lod_misis.ithappened.Domain.Models.TrackingV1;
import ru.lod_misis.ithappened.Data.Repositories.ITrackingRepository;
import ru.lod_misis.ithappened.UI.ItHappenedApplication;

public class NotificationJobService extends JobService {
    TrackingV1 trackingV1;
    @Inject
    ITrackingRepository trackingRepository;

    public NotificationJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        ItHappenedApplication.getAppComponent().inject(this);
        trackingV1 = trackingRepository.GetTracking((UUID) AllId.map.get(jobParameters.getJobId()));
        Log.i("JOB SERVICE!!!", "JOB SERVICE");
        NotificationCompat.Builder builder = createBulder();
        Intent intent = new Intent(this, AddNewEventActivity.class);
        intent.putExtra("trackingId", trackingV1.GetTrackingID().toString());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(UserActionsActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel("123");
            if (mChannel == null) {
                mChannel = new NotificationChannel("123", "Default channel", importance);
            }
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(2, builder.build());
        return false;
    }

    private NotificationCompat.Builder createBulder() {
        return new NotificationCompat.Builder(this, "123")
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("Добавьте событие")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTicker("Добавьте событие")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Вы давно не добавляли событие \"" + trackingV1.GetTrackingName() + "\",что-то случилось?"));
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }


}
