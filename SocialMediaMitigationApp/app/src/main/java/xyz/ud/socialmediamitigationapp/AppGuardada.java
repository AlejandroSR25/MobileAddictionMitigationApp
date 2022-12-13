package xyz.ud.socialmediamitigationapp;

public class AppGuardada {
    private String name;
    private String app_package;
    private Stats statistics;

    public AppGuardada() {
        this.name = "";
        this.app_package = "";
        this.statistics = null;
    }
    public AppGuardada(String name, String app_package, Stats statistics) {
        this.name = name;
        this.app_package = app_package;
        this.statistics = statistics;
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

    public Stats getStatistics() {
        return statistics;
    }

    public void setStatistics(Stats statistics) {
        this.statistics = statistics;
    }
}
