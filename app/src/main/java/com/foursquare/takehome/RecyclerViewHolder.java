package com.foursquare.takehome;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * A Holder class for stash and display operations when layout is inflated.
 * To have more control on what we display in view.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView visitorName;
    public TextView visitorTime;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        visitorName = (TextView)itemView.findViewById(R.id.visitorName);
        visitorTime = (TextView)itemView.findViewById(R.id.visitorVisitTime);
    }
}
