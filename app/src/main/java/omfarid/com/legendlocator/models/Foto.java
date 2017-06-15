package omfarid.com.legendlocator.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

import java.util.List;

import omfarid.com.legendlocator.entities.PhotosChoosen;

/**
 * Created by farid on 12/17/2016.
 */

@Table
public class Foto {
    private Long id;
    public String pathori;
    public String paththum;
    public String waktu;
    public String deskripsi;
    public Long legendaid;

    public Foto () {}

    public Long getId() {
        return this.id;
    }

    public Legenda getLegenda() {
        return SugarRecord.find(Legenda.class, "id = ?", this.legendaid.toString()).get(0);
    }

}
