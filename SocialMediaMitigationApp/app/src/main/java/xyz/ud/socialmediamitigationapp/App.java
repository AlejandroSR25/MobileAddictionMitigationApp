package xyz.ud.socialmediamitigationapp;

import android.graphics.drawable.Drawable;

public class App {
    private String name;
    private String app_package;
    private Drawable icon;

    public App(String name, String app_package, Drawable icon) {
        this.name = name;
        this.app_package = app_package;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApp_package() {
        return app_package;
    }

    public void setApp_package(String app_package) {
        this.app_package = app_package;
    }
}
