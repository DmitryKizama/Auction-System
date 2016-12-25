package com.stkizema.auction.activities;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stkizema.auction.db.DbBidHelper;
import com.stkizema.auction.db.DbProductHelper;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.db.DemoDb;
import com.stkizema.auction.model.Bid;
import com.stkizema.auction.model.Product;

import java.util.List;
import java.util.Random;

public class BotService extends Service {

    public static final String BROADCAST_ACTION_BID = "BROADCASTACTIO";
    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";
    public static final int TIMEBOT = 100000;
    private LocalBinder binder;
    private Handler handler;
    private int counter = 0;
    private RandomString randomString;
    private Runnable runnable;

    public class LocalBinder extends Binder {
        public BotService getService() {
            return BotService.this;
        }
    }

    private void doAction() {
        ++counter;
        if (counter % 2 == 0) {
            doBid();
        } else {
            doCreateProduct();
        }
    }

    private void doBid() {
        List<Product> list = DbProductHelper.getListBidProduct();
        if (list.isEmpty()) {
            return;
        }
        int randomNum = (new Random()).nextInt(list.size() - 1);
        Product p = list.get(randomNum);
        Bid b = DbBidHelper.getBidByProductId(p.getIdProduct());
        b.setPrice(b.getPrice() + randomNum + 1);
        b.setCreatorId(DbUserHelper.getUser(DemoDb.BOT).getId());
        DbBidHelper.getBidDao().update(b);
        Intent intent = new Intent(BROADCAST_ACTION_BID);
        intent.putExtra(EXTRA_PRODUCT_ID, p.getIdProduct());
        sendBroadcast(intent);
    }

    private void doCreateProduct() {
        randomString = new RandomString(8);
        String newNameProduct = randomString.nextString();
        Product product = DbProductHelper.getProductByName(newNameProduct);
        if (product != null) {
            return;
        }
        DbProductHelper.create(newNameProduct, 1, null, DbUserHelper.getUser(DemoDb.BOT).getId());
        DbBidHelper.create(DbUserHelper.getUser(DemoDb.BOT).getId(), DbProductHelper.getProductByName(newNameProduct).getIdProduct(), 15);
        Product p = DbProductHelper.getProductByName(newNameProduct);
        p.setIsAuctioned(true);
        p.setIsWon(false);
        DbProductHelper.getProductDao().update(p);
        sendBroadcast(new Intent(ListOfProductsActivity.BROADCAST_ACTION_BOT));
        Log.d("BotServiceLog", "ADD SOME SHIT");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        doAction();
        runnable = new Runnable() {
            @Override
            public void run() {
                doAction();
                handler.postDelayed(this, TIMEBOT);
            }
        };
        handler.postDelayed(runnable, TIMEBOT);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BotServiceLog", "Destroy");
        handler.removeCallbacks(runnable);
    }

    public class RandomString {

        private final char[] symbols;

        {
            StringBuilder tmp = new StringBuilder();
            for (char ch = '0'; ch <= '9'; ++ch)
                tmp.append(ch);
            for (char ch = 'a'; ch <= 'z'; ++ch)
                tmp.append(ch);
            symbols = tmp.toString().toCharArray();
        }

        private final Random random = new Random();

        private final char[] buf;

        public RandomString(int length) {
            if (length < 1)
                throw new IllegalArgumentException("length < 1: " + length);
            buf = new char[length];
        }

        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx)
                buf[idx] = symbols[random.nextInt(symbols.length)];
            return new String(buf);
        }
    }
}
