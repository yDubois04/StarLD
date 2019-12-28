package fr.istic.mob.starld.model;

public class Stop {

    private int id;
    private String name;
    private String description;
    private String latitude;
    private String longitude;
    private int wheelchairBoarding;

    public Stop (int id, String name, String description, String latitude,String longitude, int wheelchairBoarding) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wheelchairBoarding = wheelchairBoarding;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getWheelchairBoarding() {
        return wheelchairBoarding;
    }
}
