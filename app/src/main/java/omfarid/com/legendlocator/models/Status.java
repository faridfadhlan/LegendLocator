package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Table;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by farid on 12/17/2016.
 */
@Table
public class Status {
    @Unique
    public String kode;
    public String keterangan;

    @Ignore
    public final static String TERSIMPAN = "1";
    @Ignore
    public final static String MENUNGGU = "2";
    @Ignore
    public final static String DITERIMA = "3";
    @Ignore
    public final static String DITOLAK = "4";


    public Status() {}

    public Status(String kode, String keterangan) {
        this.kode = kode;
        this.keterangan = keterangan;
    }

    public List<Legenda> getLegendas() {
        return SugarRecord.find(Legenda.class, "kodestatus = ?", this.kode);
    }
}
