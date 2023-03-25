package com.betterlife.cashier.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.betterlife.cashier.MainActivity;
import com.betterlife.cashier.R;
import com.betterlife.cashier.adapter.CategorySpinnerAdapter;
import com.betterlife.cashier.dummy.MasterContent;
import com.betterlife.cashier.entity.Product;
import com.betterlife.cashier.entity.ProductCategory;
import com.betterlife.cashier.sqlite.DatabaseManager;
import com.betterlife.cashier.sqlite.ds.ProductCategoryDataSource;
import com.betterlife.cashier.sqlite.ds.ProductDataSource;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

public class ProductAddFragment extends Fragment {
    private MasterContent.DummyItem mItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {
            mItem = (new MasterContent(getActivity())).ITEM_MAP.get(getArguments().getString(Constants.ARG_ITEM_ID));
        }
    }

    private EditText txtName;
    private EditText txtPrice;
    private EditText txtStock;
    private EditText txtDiscount;
    private EditText txtDescription;

    private RadioGroup radioGroup;
    private RadioButton radio1;
    private RadioButton radio2;

    private ImageButton btnSave;
    private boolean isEdit = false;
    private String lastCode;
    private String lastName;
    private TextView subtitle;

    private ImageView icon;
    private Bitmap bitmapImage;

    private Spinner spinnerCategory;
    private CategorySpinnerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_add, container, false);

        TextView title = (TextView) rootView.findViewById(R.id.item_detail);
        subtitle = (TextView) rootView.findViewById(R.id.subtitle);
        TextView chev = (TextView) rootView.findViewById(R.id.chevron);

        TextView t1 = (TextView) rootView.findViewById(R.id.textView1);
        TextView t2 = (TextView) rootView.findViewById(R.id.textView2);
        TextView t3 = (TextView) rootView.findViewById(R.id.textView3);
        TextView t4 = (TextView) rootView.findViewById(R.id.textView4);
        TextView t5 = (TextView) rootView.findViewById(R.id.textViewDiscount);
        TextView t6 = (TextView) rootView.findViewById(R.id.textView6);
        TextView t7 = (TextView) rootView.findViewById(R.id.textView7);
        TextView tStock = (TextView) rootView.findViewById(R.id.textViewStock);

        if (mItem != null) {
            title.setText(mItem.content);
        }

        subtitle.setTypeface(Shared.OpenSansSemibold);
        chev.setTypeface(Shared.OpenSansSemibold);
        title.setTypeface(Shared.OpenSansSemibold);

        t1.setTypeface(Shared.OpenSansRegular);
        t2.setTypeface(Shared.OpenSansRegular);
        t3.setTypeface(Shared.OpenSansRegular);
        t4.setTypeface(Shared.OpenSansRegular);
        t5.setTypeface(Shared.OpenSansRegular);
        t6.setTypeface(Shared.OpenSansRegular);
        t7.setTypeface(Shared.OpenSansRegular);
        tStock.setTypeface(Shared.OpenSansRegular);

        txtName = (EditText) rootView.findViewById(R.id.editText2);
        txtName.setTypeface(Shared.OpenSansRegular);

        txtPrice = (EditText) rootView.findViewById(R.id.editText1);
        txtPrice.setTypeface(Shared.OpenSansRegular);

        txtStock = (EditText) rootView.findViewById(R.id.editTextStock);
        txtStock.setTypeface(Shared.OpenSansRegular);

        txtDiscount = (EditText) rootView.findViewById(R.id.editTextDiscount);
        txtDiscount.setTypeface(Shared.OpenSansRegular);

        txtDescription = (EditText) rootView.findViewById(R.id.editText4);
        txtDescription.setTypeface(Shared.OpenSansRegular);

        radio1 = (RadioButton) rootView.findViewById(R.id.radio0);
        radio2 = (RadioButton) rootView.findViewById(R.id.radio1);

        radio1.setTypeface(Shared.OpenSansRegular);
        radio2.setTypeface(Shared.OpenSansRegular);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup1);

        btnSave = (ImageButton) rootView.findViewById(R.id.imageButton1);
        btnSave.setOnClickListener(saveOnclick);

        icon = (ImageView) rootView.findViewById(R.id.imageView1);
        icon.setOnClickListener(iconOnclick);

        spinnerCategory = (Spinner) rootView.findViewById(R.id.spinner1);
        adapter = new CategorySpinnerAdapter(getActivity());
        spinnerCategory.setAdapter(adapter);
        populateField();

        return rootView;
    }


    private void populateField() {

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ProductCategoryDataSource catds = new ProductCategoryDataSource(db);
        adapter.set(catds.getAll());

        if (getArguments().containsKey(Constants.ARG_PRODUCT_ID)) {
            isEdit = true;
            lastCode = getArguments().getString(Constants.ARG_PRODUCT_ID);
            subtitle.setText(getString(R.string.edit));


            ProductDataSource ds = new ProductDataSource(db);

            Product dt = ds.get(lastCode);
            txtName.setText(dt.getProductName());
            txtPrice.setText(Shared.decimalformat2.format(dt.getPrice()));
            txtStock.setText(String.valueOf(dt.getStock()));
            txtDiscount.setText(String.valueOf((int)dt.getDiscount()));
            txtDescription.setText(dt.getDescription());

            radio1.setChecked(true);
            if (dt.getStatus().equals("0")) radio2.setChecked(true);

            if (dt.getImage() != null) {
                bitmapImage = Shared.convertBase64ToBitmap(dt.getImage());
                icon.setImageBitmap(bitmapImage);
            } else {
                icon.setImageResource(R.drawable.ic_noimage_square);
            }

            spinnerCategory.setSelection(adapter.indexOf(dt.getCategoryID()));
            lastName = dt.getProductName();

        }

        DatabaseManager.getInstance().closeDatabase();
    }

    private OnClickListener saveOnclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String name = txtName.getText().toString();
            String price = txtPrice.getText().toString();
            String stock = txtStock.getText().toString();
            String discount = txtDiscount.getText().toString();
            String description = txtDescription.getText().toString();

            ProductCategory cat = (ProductCategory) spinnerCategory.getSelectedItem();
            String category = cat.getCategoryID();

            if (name.equals("") || price.equals("") || stock.equals("") || category == null) {
                Toast.makeText(getActivity(), getString(R.string.error_field_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                if (Double.parseDouble(price) < 0) {
                    Toast.makeText(getActivity(), getString(R.string.price_less_zero), Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (Exception e) {
                Toast.makeText(getActivity(), getString(R.string.invalid_price_format), Toast.LENGTH_SHORT).show();
                return;
            }


            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ProductDataSource ds = new ProductDataSource(db);

            Product data = new Product();

            data.setProductID(isEdit ? lastCode : Shared.getProductID());
            data.setProductName(name);
            data.setStatus(radio1.isChecked() ? "1" : "0");
            data.setMerchantID(Shared.read(Constants.KEY_SETTING_MERCHANT_ID));
            data.setRefID(MainActivity.SesID);
            data.setImage(saveImage());
            data.setPrice(Double.parseDouble(price));
            data.setStock(Integer.parseInt(stock));
            data.setDiscount(Double.parseDouble(discount));
            data.setDescription(description);
            data.setCategoryID(category);
            data.setBranchID(Shared.read(Constants.KEY_SETTING_BRANCH_ID));
            if (isEdit) {
                if (!lastName.equals(name)) {
                    if (ds.cekName(name)) {
                        Toast.makeText(getActivity(), getString(R.string.name_exist), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                ds.update(data, lastCode);
            } else {
                if (ds.cekName(name)) {
                    Toast.makeText(getActivity(), getString(R.string.name_exist), Toast.LENGTH_SHORT).show();
                    return;
                }

                ds.insert(data);
            }


            DatabaseManager.getInstance().closeDatabase();

            Toast.makeText(getActivity(), getString(R.string.save_succeed), Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
            hideKeyboard();

        }
    };

    private OnClickListener iconOnclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            chooseAction();
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK) return;

        switch (requestCode) {
            case Constants.PICK_IMAGE_FROM_GALLERY:
                Uri iconUri = data.getData();
                Cursor cursor = getActivity().getContentResolver().query(iconUri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                cursor.moveToFirst();
                iconUri = Uri.parse(cursor.getString(0));

                File f = new File(iconUri.getPath());
                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                float scale = Shared.scaleFactor(bitmap.getWidth());
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale), (int) (bitmap.getHeight() * scale), false);
                icon.setImageBitmap(bitmap);
                bitmapImage = bitmap;
                break;
            case Constants.PICK_IMAGE_FROM_CAMERA:

                Bitmap bitmap2 = (Bitmap) data.getExtras().get("data");
                float scale2 = Shared.scaleFactor(bitmap2.getWidth());
                bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int) (bitmap2.getWidth() * scale2), (int) (bitmap2.getHeight() * scale2), false);
                icon.setImageBitmap(bitmap2);
                bitmapImage = bitmap2;
                break;
        }
    }

    private void chooseAction() {
        final CharSequence[] items = {getString(R.string.from_camera), getString(R.string.from_gallery), getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.complete_action_using));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.from_camera))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Constants.PICK_IMAGE_FROM_CAMERA);
                } else if (items[item].equals(getString(R.string.from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), Constants.PICK_IMAGE_FROM_GALLERY);
                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private String saveImage() {
        String res = null;

        Log.w("Gambar", String.valueOf(bitmapImage != null));
        if (bitmapImage != null) {
            res = Shared.convertBitmapToBase64(bitmapImage);
        }

        return res;

    }

    private void hideKeyboard() {

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}

