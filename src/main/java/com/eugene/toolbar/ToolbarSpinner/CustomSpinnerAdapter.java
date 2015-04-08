package com.eugene.toolbar.ToolbarSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.eugene.toolbar.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    Context mContext;
    String[] items;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        items = objects;
    }

    /*
    Inflate the spinner dropdown view
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater1.inflate(R.layout.spinner_dropdown, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(items[position]);
        return convertView;
    }

    /*
    Inflate spinner view (not expanded)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater1 = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater1.inflate(R.layout.spinner_toolbar, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(items[position]);
        return convertView;
    }
}