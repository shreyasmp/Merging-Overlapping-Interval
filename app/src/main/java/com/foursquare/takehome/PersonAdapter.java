package com.foursquare.takehome;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import static com.foursquare.takehome.Constants.FADED_GREY;
import static com.foursquare.takehome.Constants.KEY_NO_VISITORS;

/**
 *  Person Adapter extending Recycler View Holder pattern for better control of the view elements.
 */

final public class PersonAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final String TAG = PersonAdapter.class.getSimpleName();

    private ArrayList<Person> visitorsList;
    private Context context;

    // Adapter Constructor for getting the visitorsList from MainActivity

    public PersonAdapter(Context context, ArrayList<Person> visitorsList) {
        this.context = context;
        this.visitorsList = visitorsList;
    }

    // Inflating the recycler view item row
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.visitor_single_row, null);
        RecyclerViewHolder rcv = new RecyclerViewHolder(layoutView);
        return rcv;
    }

    // bind the view using view holder pattern
    // Also used for conditional display of no Visitors idle time in faded text compared to visitors text
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if(visitorsList.get(position).getName().equals(KEY_NO_VISITORS)) {
            holder.visitorName.setTextColor(Color.parseColor(FADED_GREY));
            holder.visitorTime.setTextColor(Color.parseColor(FADED_GREY));
        }
        holder.visitorName.setText(visitorsList.get(position).getName());
        holder.visitorTime.setText(Utility.TimeBuilderForView(Utility.convertUnixTimeToRealTime(visitorsList.get(position).getArriveTime()),
                Utility.convertUnixTimeToRealTime(visitorsList.get(position).getLeaveTime())));
    }

    @Override
    public int getItemCount() {
        return this.visitorsList.size();
    }
}


