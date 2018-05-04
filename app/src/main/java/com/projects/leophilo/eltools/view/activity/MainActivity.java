package com.projects.leophilo.eltools.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;

import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.view.base.BaseActivity;
import com.projects.leophilo.eltools.view.dialog.CreateItemDialog;
import com.projects.leophilo.eltools.view.fragment.MainFragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void startEvent() {
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            toolbar.setTitleTextColor(Color.WHITE);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_base;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tool_bar_operations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_operation_history:
                showHistory();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }


}
