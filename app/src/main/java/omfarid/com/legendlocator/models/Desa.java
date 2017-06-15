package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by farid on 12/17/2016.
 */
@Table
public class Desa {
    public String kodeprop;
    public String kodekab;
    public String kodekec;
    public String kodedesa;
    public String nama;

    public Desa() {}

    public String getFullKodeDesa() {
        return kodeprop+kodekab+kodekec+kodedesa;
    }

    public Desa(String kodeprop, String kodekab, String kodekec, String kodedesa, String nama) {
        this.kodeprop = kodeprop;
        this.kodekab = kodekab;
        this.kodekec = kodekec;
        this.kodedesa = kodedesa;
        this.nama = nama;
    }

    public Kecamatan getKecamatan() {
        return SugarRecord.find(
                Kecamatan.class,
                "kodeprop = ? AND kodekab = ? AND kodekec = ?",
                new String[]{this.kodeprop, this.kodekab, this.kodekec}
        ).get(0);
    }

    @Override
    public String toString() {
        return this.nama;
    }

}
