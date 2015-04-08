package com.eugene.toolbar.ToolbarSpinner;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eugene.toolbar.R;

public class FragmentToolbarSpinner extends Fragment {
    private FragmentCallbacks mCallbacks;

    public static interface FragmentCallbacks {
        void menuClick();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallbacks) activity;
            Log.e("onAttach", "Attached");
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement Fragment One.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        Log.e("onDetach", "Detached");
    }

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_toolbar_spinner, container, false);
        findViewsById();
        if (savedInstanceState != null) {
            Log.e("restoreInstanceState", "Restored");
        } else {
            Log.e("restoreInstanceState", "Not Restored, Nothing Saved");
        }

        return v;
    }

    // Note: Spinner keeps its position on screen rotation.
    // I am not sure how it is retaining its state without implementing it.
    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    Toolbar toolbar;
    Spinner spinner;
    CustomSpinnerAdapter customSpinnerAdapter;
    TextView textView;

    private void findViewsById() {
        toolbar = (Toolbar) v.findViewById(R.id.toolbar_spinner);
        toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.ic_menu_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.menuClick(); // Open Menu
            }
        });


        spinner = (Spinner) v.findViewById(R.id.spinner);
        String[] items = getActivity().getResources().getStringArray(R.array.spinner_items);
        customSpinnerAdapter = new CustomSpinnerAdapter(getActivity(), 0, items);
        spinner.setAdapter(customSpinnerAdapter);


        textView = (TextView) v.findViewById(R.id.textView);
        setTextFromSpinner(spinner.getSelectedItem().toString());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTextFromSpinner(spinner.getSelectedItem().toString());
                //  Toast.makeText(getActivity(), "Selected", Toast.LENGTH_SHORT).show(); // Clear to see when onItemSelected is called
                //  Note: onItemSelected is called within onCreateView.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    String string = "";

    private void setTextFromSpinner(String s) { // Set TextView based on spinner position to test screen rotation
        string = s;
        textView.setText(string);
    }


}
