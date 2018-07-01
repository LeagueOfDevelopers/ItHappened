package ru.lod_misis.ithappened.Recyclers;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import jp.shts.android.library.TriangleLabelView;
import ru.lod_misis.ithappened.Activities.AddNewEventActivity;
import ru.lod_misis.ithappened.Activities.EditTrackingActivity;
import ru.lod_misis.ithappened.Activities.EventsForTrackingActivity;
import ru.lod_misis.ithappened.Domain.TrackingV1;
import ru.lod_misis.ithappened.Fragments.DeleteTrackingFragment;
import ru.lod_misis.ithappened.Presenters.TrackingsContract;
import ru.lod_misis.ithappened.R;

public class TrackingsAdapter extends RecyclerView.Adapter<TrackingsAdapter.ViewHolder> implements View.OnCreateContextMenuListener{

    private List<TrackingV1> trackingV1s;
    private Context context;
    FragmentManager fManage;
    FragmentTransaction fTrans;
    TrackingsContract.TrackingsPresenter trackingsPresenter;

    public TrackingsAdapter(List<TrackingV1> trackingV1s,
                            Context context,
                            TrackingsContract.TrackingsPresenter trackingsPresenter) {
        this.trackingV1s = trackingV1s;
        this.context = context;
        this.trackingsPresenter = trackingsPresenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final TrackingV1 trackingV1 = trackingV1s.get(position);
        holder.trackingTitle.setText(trackingV1.GetTrackingName());
        if(trackingV1.getColor()!=null)
            holder.trackingColor.setTriangleBackgroundColor(Integer.parseInt(trackingV1.getColor()));

        holder.itemLL.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final TrackingV1 trackingV1 = trackingV1s.get(position);
                String id = trackingV1.GetTrackingID().toString();
                Intent intent = new Intent(context, AddNewEventActivity.class);
                String trackId = trackingV1.GetTrackingID().toString();
                intent.putExtra("trackingId", trackId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


        holder.itemLL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                PopupMenu popup = new PopupMenu(view.getContext(), view);

                popup.inflate(R.menu.context_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        int id = menuItem.getItemId();

                        switch (id){
                            case R.id.history_for_tracking:
                                Intent intent = new Intent(context, EventsForTrackingActivity.class);
                                String trackId = trackingV1.GetTrackingID().toString();
                                intent.putExtra("id", trackId);
                                context.startActivity(intent);
                                return true;

                            case R.id.edit_tracking:
                                String trackIdForEdit = trackingV1.GetTrackingID().toString();
                                Intent intent1 = new Intent((Activity) context, EditTrackingActivity.class);
                                intent1.putExtra("trackingId", trackIdForEdit);
                                context.startActivity(intent1);
                                return true;

                            case R.id.delete_tracking:
                                DeleteTrackingFragment delete = new DeleteTrackingFragment();
                                delete.setTrackingId(trackingV1.GetTrackingID());
                                delete.show(((Activity) context).getFragmentManager(), "DeleteEvent");
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackingV1s.size();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {

        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Call");
        menu.add(0, v.getId(), 0, "SMS");

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        CardView itemLL;
        TriangleLabelView trackingColor;

        public ViewHolder(View itemView) {
            super(itemView);
            trackingTitle = (TextView) itemView.findViewById(R.id.TracingTitle);
            itemLL = (CardView) itemView.findViewById(R.id.itemLL);
            trackingColor = itemView.findViewById(R.id.trackingColor);
        }
    }

}
