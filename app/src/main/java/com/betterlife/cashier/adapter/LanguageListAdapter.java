package com.betterlife.cashier.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.betterlife.cashier.R;
import com.betterlife.cashier.dummy.LanguageContent;

import java.util.List;
import java.util.Locale;

public class LanguageListAdapter extends BaseAdapter {

    private List<LanguageContent> dtList;
    private FragmentActivity context;
    private LayoutInflater inflater;
    private String itemID;

    public LanguageListAdapter(FragmentActivity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        TextView title;
    }

    public int getCount() {
        return dtList.size();
    }

    public void set(List<LanguageContent> list) {
        dtList = list;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return dtList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.language_list_item, null);
            holder = new ViewHolder();

            holder.title = (TextView) vi.findViewById(R.id.textView1);
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        final LanguageContent item = (LanguageContent) getItem(position);
        holder.title.setText(item.getLabel());
        holder.title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale(item.getId());
            }
        });

        return vi;
    }

    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        context.recreate();
    }
}