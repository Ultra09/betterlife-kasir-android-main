package com.betterlife.cashier.sqlite.ds;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.betterlife.cashier.entity.Order;
import com.betterlife.cashier.entity.OrderDetails;
import com.betterlife.cashier.sqlite.DbSchema;
import com.betterlife.cashier.utils.Shared;

public class OrderDataSource {
    private SQLiteDatabase db;

    public OrderDataSource(SQLiteDatabase db) {
        this.db = db;
    }

    public long truncate() {
        return db.delete(DbSchema.TBL_ORDER, null, null);
    }

    public Order get(String code) {

        Order item = new Order();

        String selectQuery = " SELECT  o.*,u." + DbSchema.COL_USER_NAME + "  FROM " + DbSchema.TBL_ORDER + " o " +
                " LEFT JOIN " + DbSchema.TBL_USER + " u ON u." + DbSchema.COL_USER_CODE + " = o." + DbSchema.COL_ORDER_USER_ID +
                " Where " + DbSchema.COL_ORDER_CODE + " = '" + code + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                item.setOrderID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CODE)));
                item.setDescription(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
                item.setAmount(c.getDouble(c.getColumnIndex(DbSchema.COL_ORDER_AMOUNT)));
                item.setDiscount(c.getDouble(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
                item.setBranchID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_BRANCH_ID)));
                item.setUserID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_USER_ID)));

                try {
                    item.setCreatedOn(Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ORDERED_ON))));
                    item.setUpdatedOn(Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_ON))));
                    item.setSycnOn(Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_SYCN_ON))));
                } catch (Exception e) {
                }


                String selectQueryDetail = " SELECT  o.*,p." + DbSchema.COL_PRODUCT_NAME + ",c." + DbSchema.COL_PRODUCT_CATEGORY_NAME + "  FROM " + DbSchema.TBL_PRODUCT_ORDER_DETAIL + " o " +
                        " LEFT JOIN " + DbSchema.TBL_PRODUCT + " p ON p." + DbSchema.COL_PRODUCT_CODE + " = o." + DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE +
                        " LEFT JOIN " + DbSchema.TBL_PRODUCT_CATEGORY + " c ON c." + DbSchema.COL_PRODUCT_CATEGORY_CODE + " = p." + DbSchema.COL_PRODUCT_CATEGORY_CODE +
                        " WHERE " + DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + " = '" + code + "'";
                Cursor cDetail = db.rawQuery(selectQueryDetail, null);

                ArrayList<OrderDetails> details = new ArrayList<OrderDetails>();
                if (cDetail.moveToFirst()) {
                    do {

                        OrderDetails order = new OrderDetails();
                        order.setDetailID(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE)));
                        order.setName(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_NAME)));
                        order.setOrderID(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE)));
                        order.setProductID(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE)));
                        order.setCategoryName(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_CATEGORY_NAME)));
                        order.setQty(cDetail.getInt(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY)));
                        order.setDiscount(cDetail.getDouble(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT)));
                        order.setPrice(cDetail.getDouble(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE)));

                        details.add(order);
                    } while (cDetail.moveToNext());
                }

                item.setOrderDetails(details);

            } while (c.moveToNext());
        }
        return item;
    }

    public ArrayList<Order> getAllPerDays() {
        return getAll(1);
    }

    public ArrayList<Order> getAllPerWeeks() {
        return getAll(2);
    }

    public ArrayList<Order> getAllPerMonths() {
        return getAll(3);
    }

    public ArrayList<Order> getAllPerYear() {
        return getAll(4);
    }

    private ArrayList<Order> getAll(int type) {
        ArrayList<Order> items = new ArrayList<Order>();
        String selectQuery = "SELECT * FROM " + DbSchema.TBL_ORDER;

        if (type == 1) {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            selectQuery += " WHERE date(" + DbSchema.COL_ORDER_ORDERED_ON + ") = '" + date + "'";
        } else if (type == 2) {
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            calendar1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            String date1 = new SimpleDateFormat("yyyy-MM-dd").format(calendar1.getTime());
            String date2 = new SimpleDateFormat("yyyy-MM-dd").format(calendar2.getTime());

            selectQuery += " WHERE date(" + DbSchema.COL_ORDER_ORDERED_ON + ") >= '" + date1 + "' AND date(" + DbSchema.COL_ORDER_ORDERED_ON + ") <= '" + date2 + "'";
        } else if (type == 3) {
            String date = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
            selectQuery += " WHERE strftime('%m', " + DbSchema.COL_ORDER_ORDERED_ON + ") = '" + date + "'";
        } else if (type == 4) {
            String date = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
            selectQuery += " WHERE strftime('%Y', " + DbSchema.COL_ORDER_ORDERED_ON + ") = '" + date + "'";
        }

        selectQuery += " ORDER BY " + DbSchema.COL_ORDER_ORDERED_ON + " DESC";

        Log.w("Query", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                Order item = new Order();
                item.setOrderID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_CODE)));
                item.setDescription(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
                item.setAmount(c.getDouble(c.getColumnIndex(DbSchema.COL_ORDER_AMOUNT)));
                item.setDiscount(c.getDouble(c.getColumnIndex(DbSchema.COL_ORDER_DESCRIPTION)));
                item.setBranchID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_BRANCH_ID)));
                item.setUserID(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_USER_ID)));

                try {
                    item.setCreatedOn(Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_ORDERED_ON))));
                    item.setUpdatedOn(Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_UPDATED_ON))));
                    item.setSycnOn(Shared.dateformat.parse(c.getString(c.getColumnIndex(DbSchema.COL_ORDER_SYCN_ON))));
                } catch (Exception e) {
                }


                String selectQueryDetail = " SELECT o.*, p." + DbSchema.COL_PRODUCT_NAME + ", c." + DbSchema.COL_PRODUCT_CATEGORY_NAME + " AS " + DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_NAME + " FROM " + DbSchema.TBL_PRODUCT_ORDER_DETAIL + " o" +
                        " LEFT JOIN " + DbSchema.TBL_PRODUCT + " p ON p." + DbSchema.COL_PRODUCT_CODE + " = o." + DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE +
                        " LEFT JOIN " + DbSchema.TBL_PRODUCT_CATEGORY + " c ON c." + DbSchema.COL_PRODUCT_CATEGORY_CODE + " = p." + DbSchema.COL_PRODUCT_CATEGORY_CODE +
                        " WHERE " + DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE + " = '" + item.getOrderID() + "'";
                Cursor cDetail = db.rawQuery(selectQueryDetail, null);

                ArrayList<OrderDetails> details = new ArrayList<OrderDetails>();
                if (cDetail.moveToFirst()) {
                    do {
                        OrderDetails order = new OrderDetails();
                        order.setDetailID(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE)));
                        order.setName(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_NAME)));
                        order.setOrderID(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE)));
                        order.setProductID(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE)));
                        order.setCategoryName(cDetail.getString(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_PRODUCT_CATEGORY_NAME)));
                        order.setQty(cDetail.getInt(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY)));
                        order.setDiscount(cDetail.getDouble(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT)));
                        order.setPrice(cDetail.getDouble(cDetail.getColumnIndex(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE)));

                        details.add(order);
                    } while (cDetail.moveToNext());
                }

                item.setOrderDetails(details);

                items.add(item);
            } while (c.moveToNext());
        }

        return items;
    }

    public long insert(Order item) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.COL_ORDER_CODE, item.getOrderID());
        values.put(DbSchema.COL_ORDER_DESCRIPTION, item.getDescription());
        values.put(DbSchema.COL_ORDER_TAX, item.getTax());
        values.put(DbSchema.COL_ORDER_AMOUNT, item.getAmount());
        values.put(DbSchema.COL_ORDER_DISCOUNT, item.getDiscount());
        values.put(DbSchema.COL_ORDER_BRANCH_ID, item.getBranchID());
        values.put(DbSchema.COL_ORDER_USER_ID, item.getUserID());
        values.put(DbSchema.COL_ORDER_ORDERED_ON, Shared.dateformat.format(item.getCreatedOn()));
        values.put(DbSchema.COL_ORDER_UPDATED_ON, Shared.dateformat.format(item.getUpdatedOn()));
        //	values.put(DbSchema.COL_ORDER_SYCN_ON, Shared.dateformat.format(item.getSycnOn()));
        db.insert(DbSchema.TBL_ORDER, null, values);

        for (OrderDetails detail : item.getOrderDetails()) {
            ContentValues valuesDetails = new ContentValues();
            valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_CODE, detail.getDetailID());
            valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_ORDER_CODE, detail.getOrderID());
            valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRODUCT_CODE, detail.getProductID());
            valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_PRICE, detail.getPrice());
            valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_QTY, detail.getQty());
            valuesDetails.put(DbSchema.COL_PRODUCT_ORDER_DETAIL_DISCOUNT, detail.getDiscount());
            db.insert(DbSchema.TBL_PRODUCT_ORDER_DETAIL, null, valuesDetails);
        }

        return 1;
    }
}
