package com.betterlife.cashier.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.betterlife.cashier.R;
import com.betterlife.cashier.adapter.LanguageListAdapter;
import com.betterlife.cashier.dummy.LanguageContent;
import com.betterlife.cashier.dummy.SettingContent;
import com.betterlife.cashier.entity.User;
import com.betterlife.cashier.sqlite.DatabaseManager;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

import java.util.ArrayList;

public class PrinterListFragment extends Fragment {
    private SettingContent.DummyItem mItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {
            mItem = new SettingContent(getActivity()).ITEM_MAP.get(getArguments().getString(Constants.ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_printer_list, container, false);

        TextView title = (TextView) rootView.findViewById(R.id.item_detail);
        title.setTypeface(Shared.OpenSansSemibold);
        if (mItem != null) {
            title.setText(mItem.content);
        }

        return rootView;
    }
}


