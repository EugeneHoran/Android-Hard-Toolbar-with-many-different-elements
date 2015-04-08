package com.eugene.toolbar.ToolbarViewpagerTabs;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.eugene.toolbar.R;

public class FragmentToolbarTabs extends Fragment {
    private FragmentCallbacks mCallbacks;

    public static interface FragmentCallbacks {
        void menuClick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Fragment Three.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }


    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tabs, container, false);
        findViewsById();
        /* Not being used
        if (savedInstanceState != null) {
        }
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.setStatusBarColor(getResources().getColor(R.color.primary_dark_two));
        }
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    Toolbar toolbar;
    ViewPager viewPager;
    MyViewpagerAdapter viewpagerArrayAdapter;
    PagerSlidingTabStrip pagerSlidingTabStrip;

    private void findViewsById() {
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_tabs);
        toolbar.setTitle("Tabs Example");
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_menu_black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.menuClick();
            }
        });
        toolbar.inflateMenu(R.menu.menu_tabs);

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        viewpagerArrayAdapter = new MyViewpagerAdapter();
        viewPager.setAdapter(viewpagerArrayAdapter);

        pagerSlidingTabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        pagerSlidingTabStrip.setViewPager(viewPager);
        pagerSlidingTabStrip.setTypeface(Typeface.DEFAULT, 4);
    }
}
