package com.betterlife.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;

import com.betterlife.cashier.fragment.AnnualReportListFragment;
import com.betterlife.cashier.fragment.DaysReportListFragment;
import com.betterlife.cashier.fragment.MonthsReportListFragment;
import com.betterlife.cashier.fragment.WeeksReportListFragment;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

public class ReportDetailActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

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
//            Bundle arguments = new Bundle();
//            arguments.putString(ReportDetailFragment.ARG_ITEM_ID, getIntent()
//                    .getStringExtra(ReportDetailFragment.ARG_ITEM_ID));
//            ReportDetailFragment fragment = new ReportDetailFragment();
//            fragment.setArguments(arguments);
//            getFragmentManager().beginTransaction()
//                    .add(R.id.report_detail_container, fragment).commit();
            setScreen(getIntent().getStringExtra(ReportDetailFragment.ARG_ITEM_ID));
        }
    }

    private void setScreen(String id)
    {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStackImmediate();
        }

        Bundle arguments = new Bundle();
        arguments.putString(Constants.ARG_ITEM_ID, id);

        Fragment fragment  = new DaysReportListFragment();
        String tag = "";

        if(id.equals("1"))
            fragment = new DaysReportListFragment();
        else if(id.equals("2"))
            fragment = new WeeksReportListFragment();
        else if(id.equals("3"))
            fragment = new MonthsReportListFragment();
        else if (id.equals("4"))
            fragment = new AnnualReportListFragment();

        fragment.setArguments(arguments);
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(android.R.anim.fade_in)
                .replace(R.id.report_detail_container, fragment,tag)
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
            navigateUpTo(new Intent(this, ReportListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
