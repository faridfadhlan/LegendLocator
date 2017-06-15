package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by farid on 12/17/2016.
 */
@Table
public class Kabupaten {
    public String kodeprop;
    public String kodekab;
    public String nama;

    public Kabupaten() {}

    public Kabupaten(String kodeprop, String kodekab, String nama) {
        this.kodeprop = kodeprop;
        this.kodekab = kodekab;
        this.nama = nama;
    }

    public Propinsi getPropinsi() {
        return SugarRecord.find(Propinsi.class, "kodeprop = ?", this.kodeprop).get(0);
    }

    public List<Kecamatan> getKecamatans() {
        return SugarRecord.find(Kecamatan.class, "kodeprop = ? AND kodekab = ?", new String[]{this.kodeprop, this.kodekab});
    }
}
