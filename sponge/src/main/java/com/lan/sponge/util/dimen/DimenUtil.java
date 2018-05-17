package com.lan.sponge.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.lan.sponge.config.Sponge;

public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = Sponge.getAppContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = Sponge.getAppContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
