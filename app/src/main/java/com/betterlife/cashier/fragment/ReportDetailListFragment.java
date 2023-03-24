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
import com.betterlife.cashier.adapter.ReportDetailListAdapter;
import com.betterlife.cashier.adapter.ReportListAdapter;
import com.betterlife.cashier.dummy.ReportContent;
import com.betterlife.cashier.entity.Order;
import com.betterlife.cashier.entity.OrderDetails;
import com.betterlife.cashier.sqlite.DatabaseManager;
import com.betterlife.cashier.sqlite.ds.OrderDataSource;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.PdfGenerator;
import com.betterlife.cashier.utils.Shared;

import java.util.ArrayList;

public class ReportDetailListFragment extends Fragment {
    private ReportContent.DummyItem mItem;
    private String orderId;
    private ListView lv;
    private ImageButton downloadButton;
    private ReportDetailListAdapter adapter;
    private ArrayList<OrderDetails> dtlist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {
            mItem = (new ReportContent(getActivity())).ITEM_MAP.get(getArguments().getString(Constants.ARG_ITEM_ID));
        }
        if (getArguments().containsKey(Constants.ARG_ORDER_ID)) {
            orderId = getArguments().getString(Constants.ARG_ORDER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_list, container, false);

        lv = (ListView) rootView.findViewById(R.id.listView1);

        // set download button invisible
        downloadButton = (ImageButton) rootView.findViewById(R.id.imageButton1);
        downloadButton.setVisibility(View.INVISIBLE);

        adapter = new ReportDetailListAdapter(getActivity());

        popolateAdapter();
        lv.setAdapter(adapter);

        TextView title = (TextView) rootView.findViewById(R.id.item_detail);
        title.setTypeface(Shared.OpenSansSemibold);
        title.setText("Report Detail");

        return rootView;
    }

    private void popolateAdapter() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        OrderDataSource ds = new OrderDataSource(db);
        dtlist = ds.get(orderId).getOrderDetails();

        adapter.set(dtlist);
        DatabaseManager.getInstance().closeDatabase();
    }

}


