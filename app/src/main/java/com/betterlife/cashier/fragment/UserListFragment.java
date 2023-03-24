package com.betterlife.cashier.fragment;

import java.util.ArrayList;

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

import com.betterlife.cashier.R;
import com.betterlife.cashier.adapter.UserListAdapter;
import com.betterlife.cashier.dummy.MasterContent;
import com.betterlife.cashier.entity.User;
import com.betterlife.cashier.sqlite.DatabaseManager;
import com.betterlife.cashier.sqlite.ds.UserDataSource;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

public class UserListFragment extends Fragment implements OnClickListener{
	private MasterContent.DummyItem mItem;
	private ListView lv;
	private UserListAdapter adapter;
	private ImageButton imageButton;
	private ArrayList<User> dtlist;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(Constants.ARG_ITEM_ID)) {
			mItem = (new MasterContent(getActivity())).ITEM_MAP.get(getArguments().getString(Constants.ARG_ITEM_ID));
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_user_list,container, false);
		
		lv = (ListView)rootView.findViewById(R.id.listView1);
		imageButton = (ImageButton) rootView.findViewById(R.id.imageButton1);
		adapter = new UserListAdapter(getActivity(),mItem.id);
		
		popolateAdapter();
		lv.setAdapter(adapter);
				
		TextView title = (TextView)rootView.findViewById(R.id.item_detail);
		title.setTypeface(Shared.OpenSansSemibold);
		if (mItem != null) {
			title.setText(mItem.content);
		}

		if (Shared.read("level").equals(Constants.ROLE_CASHIER)) {
			imageButton.setVisibility(View.INVISIBLE);
		}
		
		imageButton.setOnClickListener(this);;
		
		return rootView;
	}
	
	private void popolateAdapter()
	{
		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
        UserDataSource ds = new UserDataSource(db);
        dtlist = ds.getAll();
     
		adapter.set(dtlist);
		DatabaseManager.getInstance().closeDatabase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Fragment fragment  = new UserAddFragment();
		Bundle arguments = new Bundle();
		arguments.putString(Constants.ARG_ITEM_ID, mItem.id);
		fragment.setArguments(arguments);
		getFragmentManager().beginTransaction()
		.setTransition(android.R.anim.slide_in_left)
		.addToBackStack("add")
		.replace(R.id.master_detail_container, fragment).commit();
	}

}


