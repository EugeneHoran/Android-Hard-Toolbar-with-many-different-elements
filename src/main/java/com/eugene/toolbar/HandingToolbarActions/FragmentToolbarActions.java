package com.eugene.toolbar.HandingToolbarActions;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eugene.toolbar.R;

public class FragmentToolbarActions extends Fragment {
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
            throw new ClassCastException("Activity must implement Fragment Two.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private View v;
    LinearLayout shadow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_toolbar_actions, container, false);
        findViewsById();
        if (savedInstanceState != null) {
            isMenuCleared = savedInstanceState.getInt("menu_cleared");
            isInActionMode = savedInstanceState.getBoolean("ActionMode");
        }
        retainState();
        return v;
    }

    private void retainState() {
        if (isMenuCleared > 0) {
            toolbar.getMenu().clear();
        }
        if (isInActionMode)
            getActivity().startActionMode(new MyActionMode());
    }

    int isMenuCleared = 0;
    private boolean isInActionMode = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (isMenuCleared > 0)
            outState.putInt("menu_cleared", isMenuCleared);
        outState.putBoolean("ActionMode", isInActionMode);

    }

    Toolbar toolbar;
    Button btnInflateMenu, btnClearMenu, btnHideToolbar, btnShowToolbar, btnContextMenu;
    CheckBox cbSubtitle;

    private void findViewsById() {
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_action);
        shadow = (LinearLayout) v.findViewById(R.id.shadow);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_menu_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.menuClick();
            }
        });
        toolbar.setTitle("Toolbar Title");// Set Title
        toolbar.setSubtitle("Toolbar Subtitle");
        toolbar.inflateMenu(R.menu.menu_actions);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int he = item.getItemId();
                if (he == R.id.action) {
                    Toast.makeText(getActivity(), "Menu Item Clicked", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        btnClearMenu = (Button) v.findViewById(R.id.btnClearMenu);
        btnClearMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.getMenu().clear();
                isMenuCleared = 1;
            }
        });

        btnInflateMenu = (Button) v.findViewById(R.id.btnInflateMenu);
        btnInflateMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.menu_actions);
                isMenuCleared = 0;
            }
        });

        cbSubtitle = (CheckBox) v.findViewById(R.id.cbSubtitle);
        cbSubtitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toolbar.setSubtitle("Toolbar Subtitle");
                } else {
                    toolbar.setSubtitle(""); // Know that there is no subtitle
                }
            }
        });

        btnHideToolbar = (Button) v.findViewById(R.id.btnHideToolbar);
        btnHideToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                shadow.animate().translationY(-shadow.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
            }
        });

        btnShowToolbar = (Button) v.findViewById(R.id.btnShowToolbar);
        btnShowToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                shadow.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
            }
        });

        btnContextMenu = (Button) v.findViewById(R.id.btnContextMenu);
        registerForContextMenu(btnContextMenu);
        btnContextMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActionMode(new MyActionMode());
            }
        });
    }

    public class MyActionMode implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Hello ActionMode!");
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            isInActionMode = true;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.close_menu) {
                Toast.makeText(getActivity(), "Item Clicked!", Toast.LENGTH_SHORT).show();
                mode.finish();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isInActionMode = false;
        }
    }
}
