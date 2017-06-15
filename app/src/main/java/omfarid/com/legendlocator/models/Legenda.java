package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.ArrayList;
import java.util.List;

import omfarid.com.legendlocator.entities.PhotosChoosen;

/**
 * Created by farid on 12/17/2016.
 */
@Table
public class Legenda {
    private Long id;
    public String kodeprop;
    public String kodekab;
    public String kodekec;
    public String kodedesa;
    public String kodekategori;
    public String nama;
    public double latitude;
    public double longitude;
    public String deskripsi;
    public String kodestatus;
    public String waktu;
    public String idserver;

    public Legenda() {}

    public Long getId() {
        return this.id;
    }

    public List<Foto> getFotos() {
        return SugarRecord.find(Foto.class, "legendaid = ?", getId().toString());
    }

    public List<PhotosChoosen> getPhotosChoosen() {
        List<Foto> fotos = getFotos();
        List<PhotosChoosen> fotos2 = new ArrayList<PhotosChoosen>();
        for(int i=0;i<fotos.size();i++) {
            fotos2.add(new PhotosChoosen(fotos.get(i).paththum));
        }
        return fotos2;
    }

    public Status getStatus() {
        return SugarRecord.find(Status.class, "kode = ?", this.kodestatus).get(0);
    }

    public Desa getDesa() {
        return SugarRecord.find(
                Desa.class,
                "kodeprop = ? AND kodekab = ? AND kodekec = ? AND kodedesa = ?",
                new String[]{this.kodeprop, this.kodekab, this.kodekec, this.kodedesa}
        ).get(0);
    }

    public Kategori getKategori() {
        return SugarRecord.find(
                Kategori.class,
                "kode = ?",
                this.kodekategori
        ).get(0);
    }
}
