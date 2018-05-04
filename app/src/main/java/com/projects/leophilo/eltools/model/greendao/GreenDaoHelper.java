package com.projects.leophilo.eltools.model.greendao;

import com.projects.leophilo.eltools.model.entity.DaoSession;

/**
 * Created by Madao on 2017/5/15.
 */

public class GreenDaoHelper {

    private static DaoSession sDaoSession;

    private GreenDaoHelper() {
    }

    public static void init(DaoSession daoSession) {
        sDaoSession = daoSession;
    }


    public static DaoSession getDaoSession() {
        return sDaoSession;
    }
}
