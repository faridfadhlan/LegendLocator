package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by farid on 12/17/2016.
 */
@Table
public class Kategori {
    private long id;
    @Unique
    public String kode;
    public String nama;

    public Kategori() {}

    public Kategori(String kode, String nama) {
        this.kode = kode;
        this.nama = nama;
    }

    public List<Legenda> getLegendas() {
        return SugarRecord.find(
                Legenda.class,
                "kodekategori = ?",
                this.kode
        );
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.nama;
    }

    @Override
    public boolean equals(Object obj) {
        Kategori kat = (Kategori) obj;
        if(kat.kode.equals(this.kode)) return true;
        return false;
    }
}
