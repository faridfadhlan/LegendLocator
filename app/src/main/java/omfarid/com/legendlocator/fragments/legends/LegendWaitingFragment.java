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

import java.util.ArrayList;
import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.activities.DetailLegendActivity;
import omfarid.com.legendlocator.adapters.LegendAdapter;
import omfarid.com.legendlocator.entities.Legends;

/**
 * Created by farid on 12/2/2016.
 */

public class LegendWaitingFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.waiting_legend_fragment, container, false);
        //TextView tvTitle = (TextView) view.findViewById(R.id.wf_tv);
        //tvTitle.setText("Fragment Waiting");
//        Legends legend = new Legends("1", "Masjid Baiturrahman", "Desa Sidomulyo", "1 Desember 2016 08.15");
//        List<Legends> legends = new ArrayList<>();
//        for(int i = 0; i<10;i++) {
//            legends.add(legend);
//        }
//        LegendAdapter adapter = new LegendAdapter(getActivity(), legends);
//
//        ListView lv_waiting = (ListView)view.findViewById(R.id.lv_waiting);
//        lv_waiting.setAdapter(adapter);
//
//        lv_waiting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), DetailLegendActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }
}
