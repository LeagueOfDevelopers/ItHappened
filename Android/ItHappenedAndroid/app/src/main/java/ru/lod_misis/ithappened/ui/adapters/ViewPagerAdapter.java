package ru.lod_misis.ithappened.ui.adapters;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    List<Fragment> fragments = new ArrayList<>();


    public ViewPagerAdapter (FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem (int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount () {
        return fragments.size();
    }

    public void addFragment (Fragment fragment) {
        fragments.add(fragment);
    }
}