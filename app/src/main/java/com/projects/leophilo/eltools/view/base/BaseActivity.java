package com.projects.leophilo.eltools.view.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.projects.leophilo.eltools.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Nullable
    @BindView(R.id.tool_bar)
    protected Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(attachLayoutRes());

        ButterKnife.bind(this);

        if (null != toolbar)
            setSupportActionBar(toolbar);

        startEvent();
    }

    protected abstract void startEvent();

    @LayoutRes
    protected abstract int attachLayoutRes();
}
