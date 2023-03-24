package com.betterlife.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

import com.betterlife.cashier.entity.Setting;
import com.betterlife.cashier.fragment.LanguageListFragment;
import com.betterlife.cashier.fragment.PrinterListFragment;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

/**
 * An activity representing a single Setting detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a {@link SettingListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link SettingDetailFragment}.
 */
public class SettingDetailActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_detail);

        // Show the Up button in the action bar.
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            setScreen(getIntent().getStringExtra(SettingDetailFragment.ARG_ITEM_ID));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        Log.w("SettingListActivity", "attachBaseContext: ");
        super.attachBaseContext(newBase);
//        String lang = Shared.read("language");
//        super.attachBaseContext(Shared.updateLanguage(lang, newBase));
    }

//        @Override
//    protected void onResume() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, SettingListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
