package com.betterlife.cashier.widget;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.betterlife.cashier.R;
import com.betterlife.cashier.entity.Order;
import com.betterlife.cashier.sqlite.DatabaseHelper;
import com.betterlife.cashier.sqlite.DatabaseManager;
import com.betterlife.cashier.sqlite.ds.OrderDataSource;
import com.betterlife.cashier.utils.Shared;
import com.zj.btsdk.BluetoothService;

public class CustomConfirm extends Dialog implements android.view.View.OnClickListener{
	private Order order;
	private Context context;
	private Button btnOk;
	private Button btnCancel;
	private EditText txtdesc;
	
	private ConfirmListener listener;
	
	private LoadingDialog loading;
	private BluetoothService mService;
	public CustomConfirm(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public CustomConfirm(Context context,Order order) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.order = order;
		this.mService = mService;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.confirm_dialog);
		
		btnOk = (Button)findViewById(R.id.btnOrder);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		txtdesc = (EditText)findViewById(R.id.editText1);
		
		btnOk.setTypeface(Shared.OpenSansBold);
		btnCancel.setTypeface(Shared.OpenSansBold);
		
		txtdesc.setTypeface(Shared.openSansLightItalic);
		
		TextView t1 =(TextView)findViewById(R.id.textView1);
		TextView t2 =(TextView)findViewById(R.id.textView2);
		
		t1.setTypeface(Shared.OpenSansSemibold);
		t2.setTypeface(Shared.OpenSansRegular);
		
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		btnOk.setTypeface(Shared.OpenSansSemibold);
		btnCancel.setTypeface(Shared.OpenSansSemibold);
		loading = new LoadingDialog(context);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCancel:
			dismiss();
			break;
		case R.id.btnOrder:
			save();
			break;	
		default:
			break;
		}
	}
	
	private void save()
	{
		SaveAsync s = new SaveAsync();
		s.execute(txtdesc.getText().toString());
	}
	
	public class SaveAsync extends AsyncTask<String, String, String>
	{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
		
			String result = "0";
			
			try {
				DatabaseManager.initializeInstance(new DatabaseHelper(context));
				SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
				OrderDataSource DS = new OrderDataSource(db);
				order.setDescription(params[0]);
				
				DS.insert(order);
				DatabaseManager.getInstance().closeDatabase();
				
				result = "1";
				
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			loading.dismiss();
			dismiss();
			if(listener != null)
				listener.onFinish(result);
			
		}
	}
	
	public void setConfirmListener(ConfirmListener listener)
    {
    	this.listener = listener;
    }
    
    public interface ConfirmListener {
        public void onFinish(String result);
    }
 }
