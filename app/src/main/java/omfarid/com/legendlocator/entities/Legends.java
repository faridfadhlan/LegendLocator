package omfarid.com.legendlocator.entities;

/**
 * Created by farid on 12/2/2016.
 */

public class Legends {

    private String id, name, place, time;

    public Legends(String id, String name, String place, String time) {
        this.id = id;
        this.name = name;
        this.place = place;
        this.time = time;
    }

    public Legends() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
