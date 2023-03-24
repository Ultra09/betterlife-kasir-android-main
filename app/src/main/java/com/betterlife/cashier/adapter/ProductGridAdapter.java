package com.betterlife.cashier.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.betterlife.cashier.R;
import com.betterlife.cashier.entity.Product;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

public class ProductGridAdapter extends BaseAdapter {

    private List<Product> dtList = new ArrayList<Product>();
    private List<String> selected = new ArrayList<>();
    private Activity context;
    private LayoutInflater inflater;
    private String menu = "GRID";

    public ProductGridAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        TextView name;
        TextView stock;
        TextView price;
        ImageView image;
        ImageView imageChecklist;
        ImageView imageSoldOut;
        RelativeLayout selectedWrapper;
    }

    public int getCount() {
        return dtList.size();
    }

    @Override
    public Object getItem(int i) {
        return dtList.get(i);
    }

    public void setSelection(String id) {
        int index = selected.indexOf(id);
        Product product = (Product) getProductById(id);

        if (product.getStock() > 0) {
            if (index == -1)
                selected.add(id);
            else
                selected.remove(id);
        }

        this.notifyDataSetChanged();
    }

    public boolean isSelected(String id) {
        return selected.contains(id);
    }


    public void set(List<Product> list) {
        dtList = list;
        this.notifyDataSetChanged();
    }


    public void unCheckAll() {
        selected.clear();
        this.notifyDataSetChanged();
    }

    public void reset() {
        selected.clear();
        this.notifyDataSetChanged();
    }

    public void add(Product product) {
        dtList.add(product);
        this.notifyDataSetChanged();
    }

    public void insert(Product product, int index) {
        dtList.add(index, product);
    }

    public Product getProductById(String id) {
        Product product = null;
        for (int i = 0; i < dtList.size(); i++) {
            if (dtList.get(i).getProductID().equals(id)) {
                product = dtList.get(i);
            }
        }

        return product;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            if (this.menu.equals("GRID"))
                vi = inflater.inflate(R.layout.product_grid_item, null);
            else
                vi = inflater.inflate(R.layout.product_grid_item, null);

            holder = new ViewHolder();

            holder.name = (TextView) vi.findViewById(R.id.textView1);
            holder.stock = (TextView) vi.findViewById(R.id.textViewStock);
            holder.price = (TextView) vi.findViewById(R.id.textView2);
            holder.image = (ImageView) vi.findViewById(R.id.imageView1);
            holder.imageChecklist = (ImageView) vi.findViewById(R.id.imageView3);
            holder.imageSoldOut = (ImageView) vi.findViewById(R.id.imageViewSoldOut);
            holder.selectedWrapper = (RelativeLayout) vi.findViewById(R.id.relativeLayout1);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final Product product = (Product) getItem(position);
        String stock = context.getString(R.string.stock);

        holder.name.setText(product.getCategoryName());
        holder.stock.setText(stock + " : " + product.getStock());
        holder.price.setText(Shared.read(Constants.KEY_SETTING_CURRENCY_SYMBOL, Constants.VAL_DEFAULT_CURRENCY_SYMBOL) + "" + Shared.decimalformat.format(product.getPrice()));

        // check stock
        if (product.getStock() <= 0) {
            holder.imageSoldOut.setVisibility(View.VISIBLE);
            holder.selectedWrapper.setVisibility(View.VISIBLE);
            holder.imageChecklist.setVisibility(View.GONE);
        } else {
            holder.selectedWrapper.setVisibility(View.GONE);
        }

        if (selected.contains(product.getProductID()))
            holder.selectedWrapper.setVisibility(View.VISIBLE);

        holder.name.setTypeface(Shared.OpenSansRegular);
        holder.price.setTypeface(Shared.OpenSansBold);

        if (product.getImage() != null) {
            Bitmap bitmap = Shared.convertBase64ToBitmap(product.getImage());
            holder.image.setImageBitmap(bitmap);
        }

        return vi;
    }
}