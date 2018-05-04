package com.projects.leophilo.eltools.app;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.projects.leophilo.eltools.model.entity.DaoMaster;
import com.projects.leophilo.eltools.model.entity.DaoSession;
import com.projects.leophilo.eltools.model.greendao.GreenDaoHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "ELTools-DB");
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoSession daoSession = new DaoMaster(database).newSession();
        GreenDaoHelper.init(daoSession);
    }

}
