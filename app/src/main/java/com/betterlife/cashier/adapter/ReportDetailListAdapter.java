package com.betterlife.cashier.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.betterlife.cashier.R;
import com.betterlife.cashier.entity.Order;
import com.betterlife.cashier.entity.OrderDetails;
import com.betterlife.cashier.utils.Shared;

import java.util.List;

public class ReportDetailListAdapter extends BaseAdapter {

    private List<OrderDetails> dtList;
    private LayoutInflater inflater;

    public ReportDetailListAdapter(FragmentActivity context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        TextView tanggalOrder;
        TextView totalItem;
        TextView totalHarga;
    }

    public int getCount() {
        return dtList.size();
    }

    public void set(List<OrderDetails> list) {
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
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final OrderDetails details = (OrderDetails) getItem(position);

        // set data to view
        holder.tanggalOrder.setText(details.getProductName());
        holder.totalItem.setText("Total item: " + details.getQty());
        holder.totalHarga.setText("Harga: Rp. " + details.getPrice());

        return vi;
    }
}