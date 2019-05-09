package ru.lod_misis.ithappened.ui.fragments;

import android.Manifest;
import android.app.Fragment;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.domain.models.TrackingV1;
import ru.lod_misis.ithappened.ui.ItHappenedApplication;
import ru.lod_misis.ithappened.ui.activities.AddNewEventActivity;
import ru.lod_misis.ithappened.ui.activities.AddNewTrackingActivity;
import ru.lod_misis.ithappened.ui.background.GeopositionService;
import ru.lod_misis.ithappened.ui.presenters.BillingPresenter;
import ru.lod_misis.ithappened.ui.presenters.BillingView;
import ru.lod_misis.ithappened.ui.presenters.TrackingsContract;
import ru.lod_misis.ithappened.ui.recyclers.TrackingsAdapter;

public class TrackingsFragment extends Fragment implements TrackingsContract.TrackingsView, BillingView {

    TextView hintForTrackings;
    RecyclerView trackingsRecycler;
    TrackingsAdapter trackAdpt;
    FloatingActionButton addTracking;
    List<TrackingV1> trackingV1s;
    @Inject
    TrackingsContract.TrackingsPresenter trackingsPresenter;
    BillingPresenter billingPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trackings, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ItHappenedApplication.getAppComponent().inject(this);
        getActivity().setTitle("Что произошло?");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                createGeopositionJobService();
            }
        }
        billingPresenter = new BillingPresenter(getActivity());
        billingPresenter.attachView(this);
        hintForTrackings = (TextView) getActivity().findViewById(R.id.hintForTrackingsFragment);
        trackingsPresenter.onViewAttach(this);
        trackingsRecycler = (RecyclerView) getActivity().findViewById(R.id.tracingsRV);
        trackingsRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        trackingsPresenter.loadTrackings();

        addTracking = (FloatingActionButton) getActivity().findViewById(R.id.addNewTracking);
        addTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  billingPresenter.checkPurchase();Для подписки
                Intent intent = new Intent(getActivity(), AddNewTrackingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void showTrackings(List<TrackingV1> visibleTrackingV1s) {
        trackingV1s = visibleTrackingV1s;
        if (visibleTrackingV1s.size() != 0) {
            hintForTrackings.setVisibility(View.INVISIBLE);
        }
        trackAdpt = new TrackingsAdapter(visibleTrackingV1s, getActivity(), trackingsPresenter);
        trackingsRecycler.setAdapter(trackAdpt);

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void getPurchase(Boolean purchase) {
        if (!purchase && trackingV1s.size() >= 5) {
            showMessage("В пробной версии можно создавать только до 5 отслеживаний");
        } else {
            Intent intent = new Intent(getActivity(), AddNewTrackingActivity.class);
            startActivity(intent);

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        billingPresenter.detachView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void createGeopositionJobService() {
        ComponentName notificationJobServiece = new ComponentName(getContext(), GeopositionService.class);
        JobInfo.Builder jobBuilder = new JobInfo.Builder(534324, notificationJobServiece);
        jobBuilder.setMinimumLatency(1000 * 60L);
        JobScheduler jobScheduler =
                (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(jobBuilder.build());
        }
    }
}