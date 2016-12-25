package com.stkizema.auction;

import android.app.Application;
import android.content.Context;

import com.stkizema.auction.db.DbBidHelper;
import com.stkizema.auction.db.DbProductHelper;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.model.DaoMaster;
import com.stkizema.auction.model.DaoSession;
import com.stkizema.auction.util.ImageLoaderHelper;

import org.greenrobot.greendao.database.Database;

public class TopApp extends Application {

    private static DaoSession daoSession;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        ImageLoaderHelper.init(context);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "model", null);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        DbUserHelper.getInstance(this);
        DbProductHelper.getInstance(this);
        DbBidHelper.getInstance(this);
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    public static Context getContext(){
        return context;
    }

}
