package fr.istic.mob.starld.fr.istic.mob.starld.model;

public class Calendar {

    private int id;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
    private int start_date;
    private int end_date;

    public Calendar (int id, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, int start_date, int end_date) {
        this.id = id;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public int getMonday() {
        return monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public int getFriday() {
        return friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public int getStart_date() {
        return start_date;
    }

    public int getEnd_date() {
        return end_date;
    }
}
