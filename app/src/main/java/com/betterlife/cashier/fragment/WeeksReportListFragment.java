package com.betterlife.cashier.fragment;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.betterlife.cashier.R;
import com.betterlife.cashier.adapter.ReportListAdapter;
import com.betterlife.cashier.dummy.ReportContent;
import com.betterlife.cashier.entity.Order;
import com.betterlife.cashier.sqlite.DatabaseHelper;
import com.betterlife.cashier.sqlite.DatabaseManager;
import com.betterlife.cashier.sqlite.ds.OrderDataSource;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.PdfGenerator;
import com.betterlife.cashier.utils.Shared;

import java.util.ArrayList;

public class WeeksReportListFragment extends Fragment implements OnClickListener {
    private ReportContent.DummyItem mItem;
    private ListView lv;
    private ImageButton downloadButton;
    private ReportListAdapter adapter;
    private ArrayList<Order> dtlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {
            mItem = (new ReportContent(getActivity())).ITEM_MAP.get(getArguments().getString(Constants.ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_list, container, false);

        lv = (ListView) rootView.findViewById(R.id.listView1);
        downloadButton = (ImageButton) rootView.findViewById(R.id.imageButton1);
        adapter = new ReportListAdapter(getActivity());

        popolateAdapter();
        lv.setAdapter(adapter);

        TextView title = (TextView) rootView.findViewById(R.id.item_detail);
        title.setTypeface(Shared.OpenSansSemibold);
        if (mItem != null) {
            title.setText(mItem.content);
        }

        downloadButton.setOnClickListener(this);

        return rootView;
    }

    private void popolateAdapter() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        OrderDataSource ds = new OrderDataSource(db);
        dtlist = ds.getAllPerWeeks();

        adapter.set(dtlist);
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public void onClick(View v) {
        PdfGenerator.generate("Weekly Report", dtlist);

        Toast.makeText(getActivity(), "Success download report", Toast.LENGTH_SHORT).show();
    }

}


