package xyz.ud.socialmediamitigationapp;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class ListElement implements Serializable {
    public App appElement;

    public ListElement(App appElement) {
        this.appElement = appElement;
    }

    public App getAppElement() {
        return appElement;
    }

    public void setAppElement(App appElement) {
        this.appElement = appElement;
    }
}
