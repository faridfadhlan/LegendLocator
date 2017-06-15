package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

/**
 * Created by farid on 12/17/2016.
 */
@Table
public class Kecamatan {
    public String kodeprop;
    public String kodekab;
    public String kodekec;
    public String nama;

    public Kecamatan() {}

    public Kecamatan(String kodeprop, String kodekab, String kodekec, String nama) {
        this.kodeprop = kodeprop;
        this.kodekab = kodekab;
        this.kodekec = kodekec;
        this.nama = nama;
    }

    public String getFullKodeKecamatan() {
        return kodeprop+kodekab+kodekec;
    }

    public Kabupaten getKabupaten() {
        return SugarRecord.find(
                Kabupaten.class,
                "kodeprop = ? AND kodekab = ?",
                new String[]{this.kodeprop, this.kodekab}
        ).get(0);
    }

    public List<Desa> getDesas() {
        return
                SugarRecord.find(
                        Desa.class,
                        "kodeprop = ? AND kodekab = ? AND kodekec = ?",
                        new String[] {this.kodeprop, this.kodekab, this.kodekec}
                );
    }

    @Override
    public String toString() {
        return this.nama;
    }
}
