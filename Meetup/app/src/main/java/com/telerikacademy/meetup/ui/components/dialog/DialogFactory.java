package com.telerikacademy.meetup.ui.components.dialog;

import android.app.Activity;
import com.telerikacademy.meetup.ui.components.dialog.base.Dialog;
import com.telerikacademy.meetup.ui.components.dialog.base.IDialogFactory;

import javax.inject.Inject;

public final class DialogFactory implements IDialogFactory {

    private Activity activity;

    @Inject
    public DialogFactory(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Dialog createDialog() {
        return new MaterialDialog(activity);
    }
}