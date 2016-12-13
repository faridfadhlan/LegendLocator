package omfarid.com.legendlocator.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by farid on 12/13/2016.
 */

public class Kabupaten extends RealmObject {
    @PrimaryKey
    private String kode_kab;
    private String nama_kab;
}