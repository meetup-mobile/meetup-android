package com.telerikacademy.meetup.view.venue_details;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import com.telerikacademy.meetup.BaseApplication;
import com.telerikacademy.meetup.R;
import com.telerikacademy.meetup.config.di.module.ControllerModule;
import com.telerikacademy.meetup.ui.component.dialog.base.IDialog;
import com.telerikacademy.meetup.ui.component.dialog.base.IDialogFactory;
import com.telerikacademy.meetup.ui.fragment.base.IGallery;
import com.telerikacademy.meetup.view.venue_details.base.IVenueDetailsContract;

import javax.inject.Inject;

public class VenueDetailsContentFragment extends Fragment
        implements IVenueDetailsContract.View {

    private static final String PACKAGE_GOOGLE_MAPS = "com.google.android.apps.maps";

    @Inject
    IDialogFactory dialogFactory;

    @BindView(R.id.tv_venue_details_title)
    TextView titleTextView;

    private IVenueDetailsContract.Presenter presenter;
    private IDialog progressDialog;
    private IGallery gallery;

    public VenueDetailsContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_venue_details_content, container, false);
        BaseApplication.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        injectDependencies();

        progressDialog = dialogFactory
                .createDialog()
                .withContent(R.string.dialog_loading_content)
                .withProgress();

        startLoading();
        presenter.loadData();
        presenter.loadPhotos();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isNetworkAvailable()) {
            presenter.subscribe();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        onStop();
    }

    @Override
    public void setPresenter(IVenueDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setTitle(CharSequence title) {
        titleTextView.setText(title);
    }

    @Override
    public void setGallery(IGallery gallery) {
        this.gallery = gallery;
    }

    @Override
    public synchronized void addPhoto(final Bitmap photo) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gallery.addPhoto(photo);
            }
        });
    }

    @Override
    public void setDefaultPhoto() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_available);
                gallery.addPhoto(photo);
            }
        });
    }

    @Override
    public void startLoading() {
        progressDialog.show();
    }

    @Override
    public void stopLoading() {
        progressDialog.hide();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(getContext(), "An error has occured", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startNavigation(Uri uri) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage(PACKAGE_GOOGLE_MAPS);
        startActivity(mapIntent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void injectDependencies() {
        BaseApplication
                .from(getContext())
                .getComponent()
                .getControllerComponent(new ControllerModule(
                        getActivity(), getFragmentManager()
                ))
                .inject(this);
    }
}
