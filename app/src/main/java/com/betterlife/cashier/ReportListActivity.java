package com.betterlife.cashier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.betterlife.cashier.fragment.AnnualReportListFragment;
import com.betterlife.cashier.fragment.DaysReportListFragment;
import com.betterlife.cashier.fragment.MonthsReportListFragment;
import com.betterlife.cashier.fragment.WeeksReportListFragment;
import com.betterlife.cashier.utils.Constants;
import com.betterlife.cashier.utils.Shared;

public class ReportListActivity extends BaseFragmentActivity implements
        ReportListFragment.Callbacks {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        if (findViewById(R.id.report_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and∂
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ReportListFragment) getFragmentManager().findFragmentById(
                    R.id.report_list)).setActivateOnItemClick(true);

            setScreen("1");
        }
    }

    private void setScreen(String id) {
        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate();
        }

        Bundle arguments = new Bundle();
        arguments.putString(Constants.ARG_ITEM_ID, id);

        Fragment fragment = new DaysReportListFragment();
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
                .replace(R.id.report_detail_container, fragment, tag)
                .commit();
    }

    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(ReportDetailFragment.ARG_ITEM_ID, id);
//            ReportDetailFragment fragment = new ReportDetailFragment();
//            fragment.setArguments(arguments);
//            getFragmentManager().beginTransaction()
//                    .replace(R.id.report_detail_container, fragment).commit();
            setScreen(id);
        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ReportDetailActivity.class);
            detailIntent.putExtra(ReportDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
