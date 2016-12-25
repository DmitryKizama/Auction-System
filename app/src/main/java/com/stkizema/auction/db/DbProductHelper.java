package com.stkizema.auction.db;

import android.content.Context;

import com.stkizema.auction.TopApp;
import com.stkizema.auction.model.Bid;
import com.stkizema.auction.model.DaoSession;
import com.stkizema.auction.model.Product;
import com.stkizema.auction.model.ProductDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class DbProductHelper {

    private static DbProductHelper instance;
    private static DaoSession daoSession;

    private DbProductHelper(Context context) {
        daoSession = TopApp.getDaoSession();
    }

    public static synchronized DbProductHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbProductHelper(context);
        }

        return instance;
    }

    public static Product getProductById(Long id) {
        Query<Product> query = getProductDao().queryBuilder().where(ProductDao.Properties.IdProduct.eq(id)).build();
        return query.list().get(0);
    }

    public static boolean delete(Product product) {
        if (product == null) {
            return false;
        }
        getProductDao().delete(product);
        return true;
    }

    public static Product create(String name, double price, String imgUrl, Long creatorId) {
        for (Product p : getListProduct()) {
            if (p.getName().equals(name)) {
                return null;
            }
        }

        Product p = new Product();
        p.setIsWon(false);
        p.setIsAuctioned(false);
        p.setName(name);
        p.setStartPrice(price);
        if (imgUrl != null) {
            p.setImageUrl(imgUrl);
        }
        p.setCreatorId(creatorId);
        getProductDao().insert(p);

        Bid b = new Bid();
        b.setPrice(price);
        b.setCreatorId(creatorId);
        b.setProductId(DbProductHelper.getProductByName(name).getIdProduct());
        DbBidHelper.getBidDao().insert(b);

        return p;
    }

    public static Product getProductByName(String name) {
        if (name == null) {
            return null;
        }
        Query<Product> query = getProductDao().queryBuilder().where(ProductDao.Properties.Name.eq(name)).build();
        if (query.list().isEmpty()) {
            return null;
        }
        return query.list().get(0);
    }

    public static ProductDao getProductDao() {
        return daoSession.getProductDao();
    }

    public static List<Product> getListProduct() {
        Query<Product> query = getProductDao().queryBuilder().orderAsc(ProductDao.Properties.IdProduct).build();
        return query.list();
    }

    public static List<Product> getListWonProduct(Long userId) {
        if (userId == null) {
            return getProductDao().queryBuilder().where(ProductDao.Properties.IsWon.eq(true),
                    ProductDao.Properties.IsAuctioned.eq(false)).build().list();
        }
        Query<Product> query = getProductDao().queryBuilder().where(ProductDao.Properties.IsWon.eq(true),
                ProductDao.Properties.IsAuctioned.eq(false), ProductDao.Properties.WinnerId.eq(userId)).build();
        return query.list();
    }

    public static List<Product> getListBidProduct() {
        Query<Product> query = getProductDao().queryBuilder().where(ProductDao.Properties.IsWon.eq(false), ProductDao.Properties.IsAuctioned.eq(true)).build();
        return query.list();
    }

    public static List<Product> getListUnregisteredProduct(Long userId) {
        if (userId == null) {
            return getProductDao().queryBuilder().where(ProductDao.Properties.IsWon.eq(false),
                    ProductDao.Properties.IsAuctioned.eq(false)).build().list();
        }
        Query<Product> query = getProductDao().queryBuilder().where(ProductDao.Properties.IsWon.eq(false), ProductDao.Properties.IsAuctioned.eq(false),
                ProductDao.Properties.CreatorId.eq(userId)).build();
        return query.list();
    }

}
