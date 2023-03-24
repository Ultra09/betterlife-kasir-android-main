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
public class SettingContent {

	private Context context;
	public List<DummyItem> ITEMS = new ArrayList<DummyItem>();
	public Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

	public SettingContent(Context context) {
		this.context = context;

		addItem(new DummyItem("1", context.getString(R.string.language)));
		addItem(new DummyItem("2", context.getString(R.string.printer)));

	}

	private void addItem(DummyItem item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

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
