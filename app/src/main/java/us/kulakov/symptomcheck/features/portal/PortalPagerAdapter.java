package us.kulakov.symptomcheck.features.portal;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Pair;

import java.util.List;

public class PortalPagerAdapter extends FragmentStatePagerAdapter {
    private List<Pair<Fragment, String>> fragments;

    public PortalPagerAdapter(FragmentManager fm, List<Pair<Fragment, String>> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).first;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).second;
    }
}
