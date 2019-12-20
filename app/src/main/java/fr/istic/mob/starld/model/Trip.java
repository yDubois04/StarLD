package fr.istic.mob.starld.model;

public class Trip {

    private int id;
    private int route_id;
    private int service_id;
    private String headsign;
    private int direction_id;
    private String block_id;
    private int wheelChairAccessible;

    public Trip (int id, int route_id, int service_id, String headsign, int direction_id, String block_id, int wheelChairAccessible) {
        this.id = id;
        this.route_id = route_id;
        this.service_id =service_id;
        this.headsign = headsign;
        this.direction_id = direction_id;
        this.block_id = block_id;
        this.wheelChairAccessible = wheelChairAccessible;
    }

    public int getId() {
        return id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public int getService_id() {
        return service_id;
    }

    public String getHeadsign() {
        return headsign;
    }

    public int getDirection_id() {
        return direction_id;
    }

    public String getBlock_id() {
        return block_id;
    }

    public int getWheelChairAccessible() {
        return wheelChairAccessible;
    }
}
