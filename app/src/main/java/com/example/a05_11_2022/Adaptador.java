package com.example.a05_11_2022;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter{
    private ArrayList<Button> mButtons;
    private int mColumnWidth, mColumnHeight;

    public Adaptador(ArrayList<Button> buttons, int columnWidth, int columnHeight) {
        mButtons = buttons;
        mColumnWidth = columnWidth;
        mColumnHeight = columnHeight;
    }

    @Override
    public int getCount() {
        return mButtons.size();
    }

    @Override
    public Object getItem(int i) {
        return (Object) mButtons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        Button button;

        if (convertView ==null) {
            button = mButtons.get(i);
        } else {
            button = (Button) convertView;
        }

        android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        button.setLayoutParams(params);

        return button;
    }
}
