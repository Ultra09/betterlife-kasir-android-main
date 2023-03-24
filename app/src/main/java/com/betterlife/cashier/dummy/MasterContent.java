package com.betterlife.cashier.dummy;

import android.content.Context;

import com.betterlife.cashier.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MasterContent {

	private Context context;
	public List<MasterContent.DummyItem> ITEMS = new ArrayList<MasterContent.DummyItem>();
	public Map<String, MasterContent.DummyItem> ITEM_MAP = new HashMap<String, MasterContent.DummyItem>();

	public MasterContent(Context context) {
		this.context = context;

		addItem(new MasterContent.DummyItem("1", context.getString(R.string.data_user)));
		addItem(new MasterContent.DummyItem("2", context.getString(R.string.data_category)));
		addItem(new MasterContent.DummyItem("3", context.getString(R.string.data_product)));
	}

	private void addItem(DummyItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class DummyItem {
		public String id;
		public String content;

		public DummyItem(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
