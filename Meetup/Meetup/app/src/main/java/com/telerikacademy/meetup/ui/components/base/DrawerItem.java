package com.telerikacademy.meetup.ui.components.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;

import java.lang.reflect.Type;

public abstract class DrawerItem {

    private final Context context;
    private final Type drawerItemType;

    private long identifier;

    private String name;
    private int nameRes;

    private Drawable icon;
    private IIcon iicon;
    private int iconRes;

    public DrawerItem(Context context) {
        this.context = context;
        this.drawerItemType = this.getClass();
        this.identifier = -1;
        this.nameRes = -1;
        this.iconRes = -1;
    }

    public final Context getContext() {
        return this.context;
    }

    public final Type getDrawerItemType() {
        return this.drawerItemType;
    }

    public DrawerItem withIdentifier(long identifier) {
        this.identifier = identifier;
        return this;
    }

    public DrawerItem withName(@NonNull String name) {
        this.name = name;
        return this;
    }

    public DrawerItem withName(@StringRes int nameRes) {
        this.nameRes = nameRes;
        return this;
    }

    public DrawerItem withIcon(@NonNull Drawable icon) {
        this.icon = icon;
        return this;
    }

    public DrawerItem withIcon(@DrawableRes int iconRes) {
        this.iconRes = iconRes;
        return this;
    }

    public DrawerItem withIcon(@NonNull IIcon icon) {
        this.iicon = icon;
        return this;
    }

    public long getIdentifier() {
        return this.identifier;
    }

    public String getName() {
        if (name != null) {
            return name;
        } else if (nameRes != -1) {
            return getContext().getString(nameRes);
        }

        return null;
    }

    public Drawable getIcon() {
        if (icon != null) {
            return icon;
        } else if (iconRes != -1) {
            return getContext().getDrawable(iconRes);
        } else if (iicon != null) {
            return new IconicsDrawable(getContext(), iicon).actionBar();
        }

        return null;
    }
}
