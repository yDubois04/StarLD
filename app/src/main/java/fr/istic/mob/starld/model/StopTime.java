package fr.istic.mob.starld.model;

public class StopTime {

    private int id;
    private int trip_id;
    private String arrivalTime;
    private String departureTime;
    private int stop_id;
    private int stopSequence;

    public StopTime (int id, int trip_id,String arrivalTime, String departureTime,int stop_id, int stopSequence) {
        this.id = id;
        this.trip_id = trip_id;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stop_id = stop_id;
        this.stopSequence = stopSequence;
    }

    public int getId() {
        return id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getStop_id() {
        return stop_id;
    }

    public int getStopSequence() {
        return stopSequence;
    }
}
