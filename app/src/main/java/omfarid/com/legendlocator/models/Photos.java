package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;

/**
 * Created by BPSAdmin on 12/13/2016.
 */

public class Photos extends SugarRecord {

    public String path;
    public String description;
    public Legends legend;

    public Photos() {}

    public Photos(String path, String description) {
        this.path = path;
        this.description = description;
    }

}
