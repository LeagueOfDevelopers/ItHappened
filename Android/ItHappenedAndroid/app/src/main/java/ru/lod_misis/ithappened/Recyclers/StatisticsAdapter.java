package ru.lod_misis.ithappened.Recyclers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

import ru.lod_misis.ithappened.R;
import ru.lod_misis.ithappened.Statistics.Facts.Fact;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    private List<Fact> factCollection;
    private Context context;


    public StatisticsAdapter(List<Fact> factCollection, Context context) {
        this.factCollection = factCollection;
        this.context = context;
    }

    @Override
    public StatisticsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.statistics_item, parent, false);
        return new StatisticsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StatisticsAdapter.ViewHolder holder, final int position) {

        final Fact fact = factCollection.get(position);

        holder.factTitle.setText(fact.getFactName());
        holder.factDescription.setText(fact.textDescription());

        holder.pieChart.setVisibility(View.GONE);
        holder.lineChart.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return factCollection.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView factTitle;
        TextView factDescription;
        PieChart pieChart;
        LineChart lineChart;

        public ViewHolder(View itemView) {
            super(itemView);
            factTitle = (TextView) itemView.findViewById(R.id.hintForFactForTracking);
            factDescription = (TextView) itemView.findViewById(R.id.textFactForTracking);
            lineChart = (LineChart) itemView.findViewById(R.id.graphFact);
            pieChart = (PieChart) itemView.findViewById(R.id.pieFact);
        }
    }


}
