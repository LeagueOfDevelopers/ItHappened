package com.example.ithappenedandroid.Recyclers;

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
import android.widget.Toast;

import com.example.ithappenedandroid.Activities.EditTrackingActivity;
import com.example.ithappenedandroid.Activities.EventsForTrackingActivity;
import com.example.ithappenedandroid.Domain.Tracking;
import com.example.ithappenedandroid.R;

import java.util.List;

public class TrackingsAdapter extends RecyclerView.Adapter<TrackingsAdapter.ViewHolder> implements View.OnCreateContextMenuListener{

    private List<Tracking> trackings;
    private Context context;
    FragmentManager fManage;
    FragmentTransaction fTrans;


    public TrackingsAdapter(List<Tracking> trackings, Context context) {
        this.trackings = trackings;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Tracking tracking = trackings.get(position);
        holder.trackingTitle.setText(tracking.GetTrackingName());

        holder.itemLL.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final Tracking tracking = trackings.get(position);
                String id = tracking.GetTrackingID().toString();
                Intent intent = new Intent(context, EventsForTrackingActivity.class);
                String trackId = tracking.GetTrackingID().toString();
                intent.putExtra("id", trackId);
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
                                String trackId = tracking.GetTrackingID().toString();
                                intent.putExtra("id", trackId);
                                context.startActivity(intent);

                                return true;

                            case R.id.edit_tracking:
                                String trackIdForEdit = tracking.GetTrackingID().toString();

                                Intent intent1 = new Intent((Activity) context, EditTrackingActivity.class);
                                intent1.putExtra("trackingId", trackIdForEdit);
                                context.startActivity(intent1);

                                return true;

                            case R.id.delete_tracking:
                                Toast.makeText(context, "Удаление отлеживания", Toast.LENGTH_SHORT).show();
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
        return trackings.size();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {

        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "SMS");

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView trackingTitle;
        CardView itemLL;

        public ViewHolder(View itemView) {
            super(itemView);
            trackingTitle = (TextView) itemView.findViewById(R.id.TracingTitle);
            itemLL = (CardView) itemView.findViewById(R.id.itemLL);
        }
    }

}
