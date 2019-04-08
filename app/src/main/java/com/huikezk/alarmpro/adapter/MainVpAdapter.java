package com.huikezk.alarmpro.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.huikezk.alarmpro.fragment.HomeFragment;
import com.huikezk.alarmpro.fragment.MineFragment;
import com.huikezk.alarmpro.fragment.NewsFragment;
import com.huikezk.alarmpro.fragment.RepairFragment;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MainVpAdapter extends FragmentPagerAdapter {
    private HomeFragment home1Fragment = null;
    private NewsFragment home2Fragment = null;
    private RepairFragment home3Fragment = null;
    private MineFragment home4Fragment=null;

    public MainVpAdapter(FragmentManager fm) {
        super(fm);
        home1Fragment = new HomeFragment();
        home2Fragment = new NewsFragment();
        home3Fragment = new RepairFragment();
        home4Fragment = new MineFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = home1Fragment;
                break;
            case 1:
                fragment = home2Fragment;
                break;
            case 2:
                fragment = home3Fragment;
                break;
            case 3:
                fragment=home4Fragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
