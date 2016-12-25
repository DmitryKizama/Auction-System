package com.stkizema.auction.db;

import android.content.Context;

import com.stkizema.auction.TopApp;
import com.stkizema.auction.model.Bid;
import com.stkizema.auction.model.BidDao;
import com.stkizema.auction.model.DaoSession;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class DbBidHelper {

    private static DbBidHelper instance;
    private static DaoSession daoSession;

    private DbBidHelper(Context context) {
        daoSession = TopApp.getDaoSession();
    }

    public static synchronized DbBidHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbBidHelper(context);
        }

        return instance;
    }

    public static BidDao getBidDao() {
        return daoSession.getBidDao();
    }

    public static List<Bid> getListBid() {
        Query<Bid> query = getBidDao().queryBuilder().orderAsc(BidDao.Properties.IdBid).build();
        return query.list();
    }

    public static double getTopPriceByProductId(Long productId) {
        if (productId == null) {
            return 0;
        }
        Query<Bid> query = getBidDao().queryBuilder().where(BidDao.Properties.ProductId.eq(productId)).build();
        if (query.list().isEmpty()) {
            return DbProductHelper.getProductById(productId).getStartPrice();
        }
        return query.list().get(0).getPrice();
    }

    public static Bid getBidByProductId(Long productId) {
        if (productId == null) {
            return null;
        }
        Query<Bid> query = getBidDao().queryBuilder().where(BidDao.Properties.ProductId.eq(productId)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list().get(0);
    }

    public static void create(Long userId, Long productId, double cost) {
        Bid bid = getBidByProductId(productId);
        if (bid != null) {
            bid.setPrice(cost);
            bid.setCreatorId(userId);
            DbBidHelper.getBidDao().update(bid);
        }
        bid = new Bid();
        bid.setCreatorId(userId);
        bid.setPrice(cost);
        bid.setProductId(productId);
        DbBidHelper.getBidDao().insert(bid);
    }
}
