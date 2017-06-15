package omfarid.com.legendlocator.adapters;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import omfarid.com.legendlocator.FileHelper;
import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.models.Legenda;

/**
 * Created by farid on 12/2/2016.
 */

public class LegendaAdapter extends ArrayAdapter{
    private final Activity activity;
    private final List<Legenda> legendas;
    //private final PasarModel pm;

    public LegendaAdapter(Activity activity, List<Legenda> legendas) {
        super(activity, R.layout.adapter_legends, legendas);
        this.activity = activity;
        this.legendas = legendas;
        //this.pasars = pasars;
        //this.pm = new PasarModel(activity);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.adapter_legends, null, true);
        TextView var_tv_legend_name = (TextView) rowView.findViewById(R.id.tv_legend_name);
        TextView var_tv_legend_place = (TextView) rowView.findViewById(R.id.tv_legend_place);
        TextView var_tv_legends_time = (TextView) rowView.findViewById(R.id.tv_legend_time);
        ImageView foto = (ImageView) rowView.findViewById(R.id.foto);

        if(legendas.get(position).getFotos().size()>0) {
            Glide.with(activity)
                    .load(Uri.fromFile(new File(legendas.get(position).getFotos().get(0).paththum)))
                    .centerCrop()
                    .crossFade()
                    .into(foto);
        }

       // Log.i("tes", legendses.get(position).getPhotos().get(0).path);
        var_tv_legend_name.setText(legendas.get(position).nama);
        var_tv_legend_place.setText(legendas.get(position).getDesa().nama);
        if(legendas.get(position).getFotos().size()>0)
            var_tv_legends_time.setText(legendas.get(position).latitude+", "+legendas.get(position).longitude);
        //var_tv_pasar.setText(pasars.get(position).nama_pasar);
        //int jumlahresponden = this.pm.getCountBySql(q, new String[]{pasars.get(position).kode_pasar});
        //var_tv_jumlahresponden.setText(jumlahresponden+" Responden");
        return rowView;
    }
}
