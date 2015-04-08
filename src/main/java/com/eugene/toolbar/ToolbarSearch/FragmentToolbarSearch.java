package com.eugene.toolbar.ToolbarSearch;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.eugene.toolbar.R;

import java.util.ArrayList;
import java.util.Locale;

public class FragmentToolbarSearch extends Fragment {

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


    String strEtSearch;
    boolean searchVisible = false;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (toolbar_search.getVisibility() == View.VISIBLE)
            outState.putBoolean("toolbar_search_visible", true);
        if (etSearch.getText().toString().length() > 0)
            outState.putString("etSearch_saved", etSearch.getText().toString());
    }

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_tolbar_search, container, false);
        findViewsById();
        if (savedInstanceState != null) {
            searchVisible = savedInstanceState.getBoolean("toolbar_search_visible");
            strEtSearch = savedInstanceState.getString("etSearch_saved");
        }
        retainState();

        return v;
    }

    private void retainState() {
        if (searchVisible) {
            toolbar_search.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window w = getActivity().getWindow();
                w.setStatusBarColor(getActivity().getResources().getColor(R.color.gradient_start));
            }
        }
        if (etSearch != null)
            etSearch.setText(strEtSearch);
    }


    Toolbar toolbar, toolbar_search;
    EditText etSearch;
    Animation fade_in;
    InputMethodManager imm;

    private void findViewsById() {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        etSearch = (EditText) v.findViewById(R.id.etSearch);
        fade_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar_main);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_menu_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.menuClick();
            }
        });
        toolbar.setTitle("Search");
        toolbar.inflateMenu(R.menu.menu_search);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                if (menuItem == R.id.search) {
                    toolbar_search.setVisibility(View.VISIBLE);
                    toolbar_search.bringToFront();
                    toolbar_search.startAnimation(fade_in);
                    etSearch.requestFocus();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window w = getActivity().getWindow();
                        w.setStatusBarColor(getActivity().getResources().getColor(R.color.gradient_start));
                    }
                }
                return false;
            }
        });

        toolbar_search = (Toolbar) v.findViewById(R.id.toolbar_search);
        toolbar_search.inflateMenu(R.menu.menu_searchbar_voice);
        toolbar_search.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_grey));
        toolbar_search.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar_search.setVisibility(View.GONE);
                etSearch.setText("");
                toolbar_search.getMenu().clear();
                toolbar_search.inflateMenu(R.menu.menu_searchbar_voice);
                toolbar.bringToFront();
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window w = getActivity().getWindow();
                    w.setStatusBarColor(getActivity().getResources().getColor(R.color.primary_dark));
                }
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 1) {
                    toolbar_search.getMenu().clear();
                    toolbar_search.inflateMenu(R.menu.menu_searchbar_clear);
                } else {
                    toolbar_search.getMenu().clear();
                    toolbar_search.inflateMenu(R.menu.menu_searchbar_voice);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar_search.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItem = item.getItemId();
                if (menuItem == R.id.voice) {
                    promptSpeechInput();
                }
                if (menuItem == R.id.clear_text) {
                    etSearch.setText("");
                }
                return false;
            }
        });
    }

    /*
   Speech Input
    */
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                etSearch.setText(result.get(0));
            }
        }
    }
}
