package com.telerikacademy.meetup.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.telerikacademy.meetup.R;
import com.telerikacademy.meetup.activities.NearbyVenuesActivity;
import com.telerikacademy.meetup.models.Venue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NearbyVenuesRecyclerAdapter
        extends RecyclerView.Adapter<NearbyVenuesRecyclerAdapter.VenueHolder>
        implements Filterable {

    private static String VENUE_KEY = "Venue";

    private List<Venue> venues;
    private List<Venue> filteredVenues;

    private VenueFilter venueFilter;

    public NearbyVenuesRecyclerAdapter(List<Venue> venues) {
        this.venues = venues;
        this.filteredVenues = new ArrayList<>(venues);
    }

    @Override
    public VenueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_venue_item_row, parent, false);

        return new VenueHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(VenueHolder holder, int position) {
        Venue venue = this.filteredVenues.get(position);
        holder.bindVenue(venue);
    }

    @Override
    public int getItemCount() {
        return filteredVenues.size();
    }

    @Override
    public Filter getFilter() {
        if (this.venueFilter == null) {
            this.venueFilter = new VenueFilter(this, venues);
        }

        return venueFilter;
    }

    static class VenueHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Venue venue;
        private TextView venueName;
        private TextView venueAddress;

        private VenueHolder(View itemView) {
            super(itemView);

            this.venueName = (TextView) itemView.findViewById(R.id.venue_name);
            this.venueAddress = (TextView) itemView.findViewById(R.id.venue_address);

            itemView.setOnClickListener(this);
        }

        // TODO: Fix accordingly
        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();

            Intent showVenueIntent = new Intent(context, NearbyVenuesActivity.class);
            showVenueIntent.putExtra(VENUE_KEY, this.venue);
            context.startActivity(showVenueIntent);
        }

        void bindVenue(Venue venue) {
            this.venue = venue;
            this.venueName.setText(venue.getName());
            this.venueAddress.setText(venue.getAddress());
        }
    }

    private class VenueFilter extends Filter {

        private NearbyVenuesRecyclerAdapter adapter;
        private List<Venue> originalList;
        private List<Venue> filteredList;

        private VenueFilter(NearbyVenuesRecyclerAdapter adapter, List<Venue> venues) {
            super();

            this.adapter = adapter;
            this.originalList = venues;
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();

            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Venue venue : originalList) {
                    if (venue.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(venue);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredVenues.clear();
            adapter.filteredVenues.addAll((ArrayList<Venue>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
