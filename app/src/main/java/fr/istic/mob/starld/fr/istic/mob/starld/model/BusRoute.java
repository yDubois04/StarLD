package fr.istic.mob.starld.fr.istic.mob.starld.model;

public class BusRoute {

    private int id;
    private String shortName;
    private String longName;
    private String description;
    private int type;
    private String color;
    private String textColor;

    public BusRoute (int id, String shortName, String longName, String description, int type, String color, String textColor) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
        this.type = type;
        this.color = color;
        this.textColor = textColor;
    }

    public int getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public String getTextColor() {
        return textColor;
    }
}
