package com.projects.leophilo.eltools.view.activity;

import android.graphics.Color;
import android.view.MenuItem;

import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.view.base.BaseActivity;
import com.projects.leophilo.eltools.view.fragment.HistoryFragment;

public class HistoryActivity extends BaseActivity {

    @Override
    protected void startEvent() {
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (toolbar != null) {
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                toolbar.setTitleTextColor(Color.WHITE);
            }
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, HistoryFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_base;
    }
}
