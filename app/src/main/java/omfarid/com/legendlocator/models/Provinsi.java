package omfarid.com.legendlocator.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by farid on 12/13/2016.
 */

public class Provinsi extends RealmObject {
    @PrimaryKey
    private String kode_prop;
    private String nama_prop;
    private RealmList<Kabupaten> kabupatens;

    public String getNama_prop() {
        return nama_prop;
    }

    public void setNama_prop(String nama_prop) {
        this.nama_prop = nama_prop;
    }

    public String getKode_prop() {
        return kode_prop;
    }

    public void setKode_prop(String kode_prop) {
        this.kode_prop = kode_prop;
    }

    public RealmList<Kabupaten> getKabupatens() {
        return kabupatens;
    }
}
