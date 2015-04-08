package com.eugene.toolbar.FloatingSearchToolbar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eugene.toolbar.R;

import java.util.ArrayList;
import java.util.Locale;


public class FragmentSearchFloating extends Fragment {
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
        v = inflater.inflate(R.layout.fragment_search_floating_toolbar, container, false);
        findViewsById();
       /* Not being used
        if (savedInstanceState != null) {
        }
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getActivity().getWindow();
            w.setStatusBarColor(getActivity().getResources().getColor(R.color.gradient_start));
        }
        return v;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    EditText etSearch;
    Toolbar toolbar_search;
    ImageView imageView;
    TextView xtxView;
    InputMethodManager imm;
    Animation fade_in, fade_out;
    boolean isSearchFocused = false;
    ListView listView;
    LinearLayout linearLayout, llDivider;

    private void findViewsById() {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        fade_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in_slow);
        fade_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_out_slow);
        etSearch = (EditText) v.findViewById(R.id.etSearch);
        toolbar_search = (Toolbar) v.findViewById(R.id.toolbar_search);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        xtxView = (TextView) v.findViewById(R.id.txtView);
        listView = (ListView) v.findViewById(R.id.listView);
        linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
        linearLayout.setVisibility(View.GONE);
        llDivider = (LinearLayout) v.findViewById(R.id.llDivider);
        llDivider.setVisibility(View.GONE);
        String[] dummyValues = new String[]{"Programming", "Is Cool",
            "Cool Stuff Bro"};

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
            android.R.layout.simple_list_item_1, android.R.id.text1, dummyValues);
        listView.setAdapter(adapter);
        listView.setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSearch.setText(adapter.getItem(position));
                listView.setVisibility(View.GONE);
                llDivider.setVisibility(View.GONE);
            }
        });
        toolbar_search.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_menu_grey));
        toolbar_search.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Changes the navigation Icon between Navigation & Arrow Back dependent on focus etSearch Focus State
                if (isSearchFocused) {
                    toolbar_search.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_menu_grey));
                    etSearch.setText("");
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                    etSearch.clearFocus();
                } else {
                    mCallbacks.menuClick();
                }
            }
        });
        toolbar_search.inflateMenu(R.menu.menu_searchbar_voice);
        toolbar_search.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() { // handles menu clicks
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

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                isSearchFocused = hasFocus;
                if (hasFocus) {
                    toolbar_search.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_arrow_back_grey));
                    listView.setVisibility(View.VISIBLE);
                    llDivider.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    imageView.startAnimation(fade_out);
                    imageView.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    llDivider.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.startAnimation(fade_in);
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


        /*
         * Calling a post delay to prevent the navigation drawer animation(close) from being interrupted due to the large image size being called
         * I also prevented the textView from being seen to make it look like the were loading on the same time.
         * It actually looks pretty cool.
         */
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(R.mipmap.materal_backdrop);
                imageView.startAnimation(fade_in);
                xtxView.setVisibility(View.VISIBLE);
                xtxView.startAnimation(fade_in);
            }
        }, 800); // 8/10 of a second

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
