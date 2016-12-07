package omfarid.com.legendlocator.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.entities.Legends;

/**
 * Created by farid on 12/2/2016.
 */

public class LegendAdapter extends ArrayAdapter{
    private final Activity activity;
    private final List<Legends> legendses;
    //private final PasarModel pm;

    public LegendAdapter(Activity activity, List<Legends> legends) {
        super(activity, R.layout.adapter_legends, legends);
        this.activity = activity;
        this.legendses = legends;
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

        var_tv_legend_name.setText(legendses.get(position).getName());
        var_tv_legend_place.setText(legendses.get(position).getPlace());
        var_tv_legends_time.setText(legendses.get(position).getTime());
        //var_tv_pasar.setText(pasars.get(position).nama_pasar);
        //int jumlahresponden = this.pm.getCountBySql(q, new String[]{pasars.get(position).kode_pasar});
        //var_tv_jumlahresponden.setText(jumlahresponden+" Responden");
        return rowView;
    }
}
