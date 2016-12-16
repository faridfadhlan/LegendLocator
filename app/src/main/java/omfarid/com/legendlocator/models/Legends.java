package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by BPSAdmin on 12/13/2016.
 */

public class Legends extends SugarRecord {
    public String nama;
    public String description;
    public List<Photos> photoses;

    public Legends() {}

    public Legends(String nama, String description) {

    }

    public List<Photos> getPhotos() {
        return Photos.find(Photos.class, "legend ?", getId().toString());
    }

}
