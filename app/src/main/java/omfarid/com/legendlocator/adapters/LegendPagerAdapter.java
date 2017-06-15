package omfarid.com.legendlocator.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import omfarid.com.legendlocator.fragments.legends.LegendApprovedFragment;
import omfarid.com.legendlocator.fragments.legends.LegendDisapprovedFragment;
import omfarid.com.legendlocator.fragments.legends.LegendSavedFragment;
import omfarid.com.legendlocator.fragments.legends.LegendWaitingFragment;

/**
 * Created by farid on 12/1/2016.
 */

public class LegendPagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES = { "Tersimpan", "Menunggu", "Disetujui", "Ditolak" };
    Context context = null;

    public LegendPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = new LegendSavedFragment();
                break;
            case 1:
                fragment = new LegendWaitingFragment();
                break;
            case 2:
                fragment = new LegendApprovedFragment();
                break;
            case 3:
                fragment = new LegendDisapprovedFragment();
                break;
        }
//        return LegendChildFragment.newInstance(position);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
