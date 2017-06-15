package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by farid on 12/17/2016.
 */
@Table
public class Propinsi {
    @Unique
    public String kodeprop;
    public String nama;

    public Propinsi() {}

    public Propinsi(String kodeprop, String nama) {
        this.kodeprop = kodeprop;
        this.nama = nama;
    }

    public List<Kabupaten> getKabupatens() {
        return SugarRecord.find(Kabupaten.class, "kodeprop = ?", this.kodeprop);
    }
}
