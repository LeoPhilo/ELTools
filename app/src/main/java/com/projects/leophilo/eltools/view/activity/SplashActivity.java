package com.projects.leophilo.eltools.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.app.Elements;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntityDao;
import com.projects.leophilo.eltools.model.greendao.GreenDaoHelper;
import com.projects.leophilo.eltools.tools.SPTool;
import com.projects.leophilo.eltools.view.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    public static final String SP_FIRST_OPEN = "FirstOpen";

    @Override
    protected void startEvent() {
        SharedPreferences sp = SPTool.getAppSP(this);
        boolean firstOpen = sp.getBoolean(SP_FIRST_OPEN, true);

        if (firstOpen) {
            initNormal();
            sp.edit().putBoolean(SP_FIRST_OPEN, false).apply();
        }

        jumpToMain();

    }

    private void initNormal() {
        NormalCompositionItemEntityDao normalCompositionItemEntityDao =
                GreenDaoHelper.getDaoSession().getNormalCompositionItemEntityDao();

        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "CH4/", "甲烷", Elements.Type.CombustibleGas, 0, 5.0f, 15.0f)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "C2/H6/", "乙烷", Elements.Type.CombustibleGas, 0, 2.9f, 13.0f)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "C2/H4/", "乙烯", Elements.Type.CombustibleGas, 0, 2.7f, 34.0f)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "C3/H8/", "丙烷", Elements.Type.CombustibleGas, 0, 2.1f, 9.5f)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "C3/H6/", "丙烯", Elements.Type.CombustibleGas, 0, 2.0f, 11.7f)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "CO", "一氧化碳", Elements.Type.CombustibleGas, 0, 12.5f, 74.2f)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "H2/", "氢气", Elements.Type.CombustibleGas, 0, 4.0f, 75.9f)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "H2/S", "硫化氢", Elements.Type.CombustibleGas, 0, 4.3f, 45.5f)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "N2/", "氮气", Elements.Type.NobleGas, 0, 0, 0)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "O2/", "氧气", Elements.Type.Air, 0, 0, 0)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "CO2/", "二氧化碳", Elements.Type.NobleGas, 0, 0, 0)
        );
        normalCompositionItemEntityDao.insert(
                new NormalCompositionItemEntity(null, "H2/O", "水蒸气", Elements.Type.NobleGas, 0, 0, 0)
        );
    }

    private void jumpToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_splash;
    }
}
