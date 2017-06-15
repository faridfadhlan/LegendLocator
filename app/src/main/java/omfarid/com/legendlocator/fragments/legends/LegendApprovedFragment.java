package omfarid.com.legendlocator.fragments.legends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.orm.SugarRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.activities.DetailLegendActivity;
import omfarid.com.legendlocator.adapters.LegendaAdapter;
import omfarid.com.legendlocator.helpers.FaridHelpers;
import omfarid.com.legendlocator.models.Legenda;
import omfarid.com.legendlocator.models.Status;

/**
 * Created by farid on 12/2/2016.
 */

public class LegendApprovedFragment extends Fragment {
    List<Legenda> legendas;
    LegendaAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.approved_legend_fragment, container, false);
        //TextView tvTitle = (TextView) view.findViewById(R.id.wf_tv);
        //tvTitle.setText("Fragment Waiting");
        legendas = SugarRecord.find(Legenda.class, "KODESTATUS = ?", new String[]{Status.DITERIMA}, null, "WAKTU DESC", null);
        adapter = new LegendaAdapter(getActivity(), legendas);

        ListView lv_approved = (ListView) view.findViewById(R.id.lv_approved);
        lv_approved.setAdapter(adapter);

        lv_approved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailLegendActivity.class);
                intent.putExtra("id", legendas.get(position).getId().toString());
                startActivity(intent);
            }
        });

        //registerForContextMenu(lv_approved);

        return view;
    }
}
