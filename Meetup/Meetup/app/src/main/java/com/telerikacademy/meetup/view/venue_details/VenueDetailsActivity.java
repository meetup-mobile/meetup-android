package com.telerikacademy.meetup.view.venue_details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import com.telerikacademy.meetup.R;
import com.telerikacademy.meetup.ui.fragments.base.IToolbar;
import com.telerikacademy.meetup.model.Venue;

public class VenueDetailsActivity extends AppCompatActivity {

    private Venue currVenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);

        Intent intent = getIntent();
        this.currVenue = (Venue) intent.getSerializableExtra("venue");
        this.setTitle(currVenue.getName());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        IToolbar menuInflater = (IToolbar)
                getSupportFragmentManager().findFragmentById(R.id.fragment_home_header);

        if (menuInflater != null) {
            menuInflater.inflateMenu(R.menu.main, menu, getMenuInflater());
            menuInflater.setNavigationOnClickListener();
        }

        return true;
    }
}