package com.betterlife.cashier;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.betterlife.cashier.fragment.LanguageListFragment;
import com.betterlife.cashier.fragment.PrinterListFragment;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

/**
 * An activity representing a list of Settings. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link SettingDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SettingListFragment} and the item details (if present) is a
 * {@link SettingDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link SettingListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class SettingListActivity extends BaseFragmentActivity implements
        SettingListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_list);

        if (findViewById(R.id.setting_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((SettingListFragment) getFragmentManager().findFragmentById(
                    R.id.setting_list)).setActivateOnItemClick(true);

            setScreen("language");
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        Log.w("SettingListActivity", "attachBaseContext: ");
//        super.attachBaseContext(newBase);
////        String lang = Shared.read("language");
////        super.attachBaseContext(Shared.updateLanguage(lang, newBase));
//    }

//    @Override
//    protected void onResume() {
//        Shared.didLanguageChange(this);
//        Log.w("Setting List Activity", "onResume: ");
//
//        super.onResume();
//    }

    private void setScreen(String id) {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        Bundle arguments = new Bundle();
        arguments.putString(Constants.ARG_ITEM_ID, id);

        Fragment fragment = new LanguageListFragment();
        String tag = "";

        if (id.equals("1"))
            fragment = new LanguageListFragment();
        else if (id.equals("2"))
            fragment = new PrinterListFragment();

        fragment.setArguments(arguments);
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(android.R.anim.fade_in)
                .replace(R.id.setting_detail_container, fragment, tag)
                .commit();
    }

    /**
     * Callback method from {@link SettingListFragment.Callbacks} indicating
     * that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            setScreen(id);
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(SettingDetailFragment.ARG_ITEM_ID, id);
//            SettingDetailFragment fragment = new SettingDetailFragment();
//            fragment.setArguments(arguments);
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.setting_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, SettingDetailActivity.class);
            detailIntent.putExtra(SettingDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
