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
public class ReportContent {

	private Context context;
	public List<ReportContent.DummyItem> ITEMS = new ArrayList<ReportContent.DummyItem>();
	public Map<String, ReportContent.DummyItem> ITEM_MAP = new HashMap<String, ReportContent.DummyItem>();

	public ReportContent(Context context) {
		this.context = context;

		addItem(new ReportContent.DummyItem("1", context.getString(R.string.daily)));
		addItem(new ReportContent.DummyItem("2", context.getString(R.string.weekly)));
		addItem(new ReportContent.DummyItem("3", context.getString(R.string.monthly)));
		addItem(new ReportContent.DummyItem("4", context.getString(R.string.annual)));
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
