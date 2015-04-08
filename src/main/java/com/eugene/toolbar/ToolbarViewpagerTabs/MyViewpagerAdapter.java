package com.eugene.toolbar.ToolbarViewpagerTabs;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.eugene.toolbar.R;

/*
 * This example I am setting views for individual page. I suggest using fragments if each page acts independently from each other.
 ***** FragmentPagerAdapter or FragmentStatePagerAdapter
 * https://github.com/EugeneHoran/Android-Easy-ViewPager-Fragment-Example
 *
 * Suggest reading more about the adapter below

 - More information is found in @link android.support.v4.view.PagerAdapter

 * <p>PagerAdapter is more general than the adapters used for
 * {@link android.widget.AdapterView AdapterViews}. Instead of providing a
 * View recycling mechanism directly ViewPager uses callbacks to indicate the
 * steps taken during an update. A PagerAdapter may implement a form of View
 * recycling if desired or use a more sophisticated method of managing page
 * Views such as Fragment transactions where each page is represented by its
 * own Fragment.</p>
 *
 * If you want more info:PC users: Hold ctrl and move mouse over PagerAdapter to visit the class
 */
public class MyViewpagerAdapter extends PagerAdapter {

    @Override
    public CharSequence getPageTitle(int position) { // Tab text
        if (position == 0) {
            return "Home";
        }
        if (position == 1) {
            return "Music";
        }
        return getPageTitle(position);
    }

    /*
    Returns provided id's view (children) to populate pages (ViewGroup, parent) inside of ViewPager
     */
    @Override
    public Object instantiateItem(ViewGroup viewGroup, int position) {
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.page1;
                break;
            case 1:
                resId = R.id.page2;
                break;
        }
        return viewGroup.findViewById(resId);
    }

    @Override

    public int getCount() {
        return 2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }
}