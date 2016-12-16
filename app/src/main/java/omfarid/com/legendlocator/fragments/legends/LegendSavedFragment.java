package omfarid.com.legendlocator.fragments.legends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.activities.DetailLegendActivity;
import omfarid.com.legendlocator.adapters.LegendAdapter;
import omfarid.com.legendlocator.models.Legends;

/**
 * Created by farid on 12/2/2016.
 */

public class LegendSavedFragment extends Fragment {

    List<Legends> legends;
    LegendAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_legend_fragment, container, false);
        //TextView tvTitle = (TextView) view.findViewById(R.id.wf_tv);
        //tvTitle.setText("Fragment Waiting");
        legends = Legends.listAll(Legends.class);
        adapter = new LegendAdapter(getActivity(), legends);

        ListView lv_saved = (ListView)view.findViewById(R.id.lv_saved);
        lv_saved.setAdapter(adapter);

        lv_saved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailLegendActivity.class);
                intent.putExtra("id", legends.get(position).getId().toString());
                Log.i("dd", legends.get(position).getId().toString());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        legends.clear();
        legends.addAll(Legends.listAll(Legends.class));
        adapter.notifyDataSetChanged();
    }
}
