package com.betterlife.cashier.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.betterlife.cashier.R;
import com.betterlife.cashier.entity.Order;
import com.betterlife.cashier.entity.OrderDetails;
import com.betterlife.cashier.fragment.ReportDetailListFragment;
import com.betterlife.cashier.fragment.UserAddFragment;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReportListAdapter extends BaseAdapter {

    private List<Order> dtList;
    private LayoutInflater inflater;

    private FragmentActivity context;

    public ReportListAdapter(FragmentActivity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        TextView tanggalOrder;
        TextView totalItem;
        TextView totalHarga;
        TextView totalDiscount;
        RelativeLayout reportListItem;
    }

    public int getCount() {
        return dtList.size();
    }

    public void set(List<Order> list) {
        dtList = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return dtList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.report_list_item, null);
            holder = new ViewHolder();

            holder.tanggalOrder = (TextView) vi.findViewById(R.id.textView1);
            holder.totalItem = (TextView) vi.findViewById(R.id.textView2);
            holder.totalHarga = (TextView) vi.findViewById(R.id.textView3);
            holder.totalDiscount= (TextView) vi.findViewById(R.id.textView4);
            holder.reportListItem = (RelativeLayout) vi.findViewById(R.id.report_list_item);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final Order order = (Order) getItem(position);
        int totalItem = 0;

        // menghitung total item dan harga
        for (OrderDetails details : order.getOrderDetails()) {
            totalItem += details.getQty();
        }

        // format tanggal
        String date = Shared.formatDate(order.getCreatedOn());

        // set data to view
        holder.tanggalOrder.setText(date);
        holder.totalItem.setText(context.getString(R.string.total_item) + totalItem);
        holder.totalHarga.setText(context.getString(R.string.total_price) + "Rp. " + order.getAmount());
        holder.totalDiscount.setText(context.getString(R.string.total_discount) + "Rp. " + order.getDiscount());
        holder.reportListItem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = new ReportDetailListFragment();
                        Bundle arguments = new Bundle();
                        arguments.putString(Constants.ARG_ORDER_ID, order.getOrderID());

                        fragment.setArguments(arguments);
                        context.getSupportFragmentManager().beginTransaction()
                                .setTransition(android.R.anim.slide_in_left)
                                .addToBackStack("add")
                                .replace(R.id.report_detail_container, fragment).commit();
                    }
                }
        );

        return vi;
    }
}