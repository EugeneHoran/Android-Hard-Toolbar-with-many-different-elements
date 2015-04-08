package com.eugene.toolbar.TransparentToolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.eugene.toolbar.R;

public class FragmentTransparentToolbar extends Fragment {
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
            throw new ClassCastException("Activity must implement Fragment One.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private View v;
    int oldToolbarHeight, oldImageHeight;
    boolean isSaved = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_transparent_toolbar, container, false);
        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt("scrollview");
            oldToolbarHeight = savedInstanceState.getInt("toolbar_height");
            oldImageHeight = savedInstanceState.getInt("image_height");
            isSaved = savedInstanceState.getBoolean("saved");
        }
        findViewsById();
        fadeToolbar();
        updateScrollView();
        return v;
    }

    private void updateScrollView() {
        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() { // I have no idea how I got this to work and I am currently brain dead so it probably needs some refactoring
                if (isSaved)
                    if (imageHeight > oldImageHeight) {
                        int updateImage = imageHeight - oldImageHeight;
                        int updateToolbar = toolbarHeight - oldToolbarHeight;
                        int difference = updateImage - updateToolbar;
                        int updateScroll = scrollPosition + difference;
                        scrollView.scrollTo(0, updateScroll);
                        Log.e("OKAY", "FIRST");
                    } else if (oldImageHeight > imageHeight) {
                        int updateImage = oldImageHeight - imageHeight;
                        int updateToolbar = oldToolbarHeight - toolbarHeight;
                        int difference = updateImage - updateToolbar;
                        int updateScroll = scrollPosition - difference;
                        scrollView.scrollTo(0, updateScroll);
                        Log.e("OKAY", "SECOND");
                    }
            }
        }, 410);
    }

    int scrollPosition = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("scrollview", scrollView.getScrollY());
        outState.putInt("toolbar_height", toolbarHeight);
        outState.putInt("image_height", imageHeight);
        outState.putBoolean("saved", isSaved = true);
    }

    Toolbar toolbar;
    ImageView imageView;
    TextView xtxView;
    Animation fade_in;
    Drawable drawable, drawableShadow;
    ScrollViewX scrollView;
    ImageView imShadow;
    TextView toolbarTile;

    private void findViewsById() {
        scrollView = (ScrollViewX) v.findViewById(R.id.scrollView);
        drawable = getResources().getDrawable(R.color.primary);
        drawable.setAlpha(1);
        drawableShadow = getResources().getDrawable(R.drawable.bottom_shadow);
        drawableShadow.setAlpha(1);
        toolbarTile = (TextView) v.findViewById(R.id.toolbarTile);
        toolbarTile.setAlpha(0);
        imShadow = (ImageView) v.findViewById(R.id.imShadow);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_trans);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            toolbar.setBackground(drawable);
            imShadow.setBackground(drawableShadow);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_menu_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.menuClick(); // Open Menu
            }
        });
        toolbar.inflateMenu(R.menu.menu_search);
        fade_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in_slow);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        xtxView = (TextView) v.findViewById(R.id.txtView);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.mipmap.materal_backdrop);
                imageView.startAnimation(fade_in);
                xtxView.setVisibility(View.VISIBLE);
                xtxView.startAnimation(fade_in);
                getHeight();
            }
        }, 400);
    }

    float titleAlpha;
    int alpha = 0;

    /*
    * @Reference
    * https://github.com/ramanadv/fadingActionBar
    */
    private void fadeToolbar() {
        scrollView.setOnScrollViewListener(new ScrollViewX.OnScrollViewListener() {
            @Override
            public void onScrollChanged(ScrollViewX v, int l, int t, int oldL, int oldT) {
                drawable.setAlpha(getAlphaForToolbar(v.getScrollY()));
                drawableShadow.setAlpha(getAlphaForToolbar(v.getScrollY()));
                toolbarTile.setAlpha(getAlphaForToolbarText(v.getScrollY()));
            }

            private int getAlphaForToolbar(int scrollY) {  // For the Drawables
                int minDist = 0, maxDist = imageHeight - toolbarHeight;
                if (scrollY > maxDist) {
                    return 255;
                } else if (scrollY < minDist) {
                    return 0;
                } else {
                    alpha = (int) ((255.0 / maxDist) * scrollY);
                    return alpha;
                }
            }

            private float getAlphaForToolbarText(float scrollY) { // For the TextView in the toolbar
                int minDist = 0, maxDist = imageHeight - toolbarHeight;
                if (scrollY > maxDist) {
                    return 1;
                } else if (scrollY < minDist) {
                    return 0;
                } else {
                    titleAlpha = scrollY / maxDist; // 0-1
                    return titleAlpha;
                }
            }
        });
    }


    int imageHeight, toolbarHeight;

    private void getHeight() {
        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                toolbarHeight = toolbar.getHeight();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                imageHeight = imageView.getHeight();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                else
                    imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }
}
