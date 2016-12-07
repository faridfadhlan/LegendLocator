package omfarid.com.legendlocator.fragments.legends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import omfarid.com.legendlocator.R;

/**
 * Created by farid on 12/2/2016.
 */

public class LegendDisapprovedFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.disapproved_legend_fragment, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.df_tv);
        tvTitle.setText("Fragment Disapproved");
        return view;
    }
}
