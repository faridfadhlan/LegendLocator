package omfarid.com.legendlocator.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by farid on 12/13/2016.
 */

public class Legends extends RealmObject {
    @PrimaryKey private int id;
    private String name;
    public RealmList<Photos> photos;
    private String latlong;
    private String description;
    private String kat_id;
    private String kode_prop;
    private String kode_kab;
    private String kode_kec;
    private String kode_desa;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Photos> getPhotos() {
        return photos;
    }

    public void setPhotos(RealmList<Photos> photos) {
        this.photos = photos;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKat_id() {
        return kat_id;
    }

    public void setKat_id(String kat_id) {
        this.kat_id = kat_id;
    }

    public String getKode_prop() {
        return kode_prop;
    }

    public void setKode_prop(String kode_prop) {
        this.kode_prop = kode_prop;
    }

    public String getKode_kab() {
        return kode_kab;
    }

    public void setKode_kab(String kode_kab) {
        this.kode_kab = kode_kab;
    }

    public String getKode_kec() {
        return kode_kec;
    }

    public void setKode_kec(String kode_kec) {
        this.kode_kec = kode_kec;
    }

    public String getKode_desa() {
        return kode_desa;
    }

    public void setKode_desa(String kode_desa) {
        this.kode_desa = kode_desa;
    }
}
