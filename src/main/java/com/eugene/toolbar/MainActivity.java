package com.eugene.toolbar;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Window;

import com.eugene.toolbar.FloatingSearchToolbar.FragmentSearchFloating;
import com.eugene.toolbar.HandingToolbarActions.FragmentToolbarActions;
import com.eugene.toolbar.ToolbarSearch.FragmentToolbarSearch;
import com.eugene.toolbar.ToolbarSpinner.FragmentToolbarSpinner;
import com.eugene.toolbar.ToolbarViewpagerTabs.FragmentToolbarTabs;
import com.eugene.toolbar.TransparentToolbar.FragmentTransparentToolbar;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
    FragmentToolbarSpinner.FragmentCallbacks, FragmentToolbarActions.FragmentCallbacks, FragmentToolbarSearch.FragmentCallbacks,
    FragmentSearchFloating.FragmentCallbacks, FragmentToolbarTabs.FragmentCallbacks, FragmentTransparentToolbar.FragmentCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsById();
    }

    private DrawerLayout drawer;

    private void findViewsById() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    Fragment fragment = null;
    FragmentManager fm = getSupportFragmentManager();

    /*
     * Handles fragment transactions
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (drawer != null) {
            drawer.closeDrawer(Gravity.START);
        }
        switch (position) {
            case 0:
                fragment = new FragmentToolbarActions();
                break;
            case 1:
                fragment = new FragmentToolbarSpinner();
                break;
            case 2:
                fragment = new FragmentToolbarSearch();
                break;
            case 3:
                fragment = new FragmentSearchFloating();
                break;
            case 4:
                fragment = new FragmentToolbarTabs();
                break;
            case 5:
                fragment = new FragmentTransparentToolbar();
                break;
        }
        if (fragment != null) {
            fm.beginTransaction()
                .replace(R.id.container, fragment).commit();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // Might need to change the status bar color if it is not the theme
            Window w = getWindow();
            if (w.getStatusBarColor() != getResources().getColor(R.color.primary_dark))
                w.setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }
    }

    @Override
    public void menuClick() { // From fragment handles opening of the navigation drawer
        if (drawer != null) drawer.openDrawer(Gravity.START);
    }
}
