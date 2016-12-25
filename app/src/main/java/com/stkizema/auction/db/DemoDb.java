package com.stkizema.auction.db;

import com.stkizema.auction.R;
import com.stkizema.auction.TopApp;
import com.stkizema.auction.model.Product;
import com.stkizema.auction.model.User;
import com.stkizema.auction.model.UserDao;
import com.stkizema.auction.util.ImageLoaderHelper;

public class DemoDb {

    private static final String ADMIN_DEMO_USER = "admin1";
    private static final String DEMO_USER_1 = "user1";
    private static final String DEMO_USER_2 = "user2";
    private static final String DEMO_PRODUCT_1 = "product1";
    private static final String DEMO_PRODUCT_2 = "product2";
    private static final String DEMO_PRODUCT_3 = "product3";
    private static final String DEMO_PRODUCT_4 = "product4";
    private static final String DEMO_PRODUCT_5 = "product5";
    private static final String DEMO_PRODUCT_6 = "product6";
    public static final String BOT = "BotUser";

    public static void setDemoDb() {
        UserDao userDao = TopApp.getDaoSession().getUserDao();

        {
            User user = DbUserHelper.getUser(BOT);
            if (user == null) {
                user = new User();
                user.setEmail("userbotversion42@gmail.com");
                user.setLogin(BOT);
                user.setPassword("12345678");
                user.setPermission(User.PERMISSIONUSER);
                userDao.insert(user);
            }
        }

        {
            User user = DbUserHelper.getUser(DEMO_USER_1);
            if (user == null) {
                user = new User();
                user.setEmail("demouser1@gmail.com");
                user.setLogin(DEMO_USER_1);
                user.setPassword("12345678");
                user.setPermission(User.PERMISSIONUSER);
                userDao.insert(user);
            }
        }

        {
            User user = DbUserHelper.getUser(DEMO_USER_2);
            if (user == null) {
                user = new User();
                user.setEmail("demouser2@gmail.com");
                user.setLogin(DEMO_USER_2);
                user.setPassword("12345678");
                user.setPermission(User.PERMISSIONUSER);
                userDao.insert(user);
            }
        }

        {
            Product product = DbProductHelper.getProductByName(DEMO_PRODUCT_1);
            if (product == null) {
                DbProductHelper.create(DEMO_PRODUCT_1, 14, ImageLoaderHelper.DRAWABLE_PREF + R.drawable.ic_award_second_512, DbUserHelper.getUser(DEMO_USER_1).getId());
            }
        }

        {
            Product product = DbProductHelper.getProductByName(DEMO_PRODUCT_2);
            if (product == null) {
                DbProductHelper.create(DEMO_PRODUCT_2, 66.6, ImageLoaderHelper.DRAWABLE_PREF + R.drawable.ic_github, DbUserHelper.getUser(DEMO_USER_2).getId());
            }
        }

        {
            Product product = DbProductHelper.getProductByName(DEMO_PRODUCT_3);
            if (product == null) {
                DbProductHelper.create(DEMO_PRODUCT_3, 323, ImageLoaderHelper.DRAWABLE_PREF + R.drawable.ic_healthcare_icon, DbUserHelper.getUser(DEMO_USER_2).getId());
                DbBidHelper.create(DbUserHelper.getUser(DEMO_USER_2).getId(), DbProductHelper.getProductByName(DEMO_PRODUCT_3).getIdProduct(), 400);
                Product p = DbProductHelper.getProductByName(DEMO_PRODUCT_3);
                p.setIsAuctioned(true);
                p.setIsWon(false);
                DbProductHelper.getProductDao().update(p);
            }
        }

        {
            Product product = DbProductHelper.getProductByName(DEMO_PRODUCT_4);
            if (product == null) {
                DbProductHelper.create(DEMO_PRODUCT_4, 323, ImageLoaderHelper.DRAWABLE_PREF + R.drawable.ic_heart_font_awesome, DbUserHelper.getUser(DEMO_USER_1).getId());
                DbBidHelper.create(DbUserHelper.getUser(DEMO_USER_1).getId(), DbProductHelper.getProductByName(DEMO_PRODUCT_4).getIdProduct(), 1000);
                Product p = DbProductHelper.getProductByName(DEMO_PRODUCT_4);
                p.setIsAuctioned(true);
                p.setIsWon(false);
                DbProductHelper.getProductDao().update(p);
            }
        }

        {
            Product product = DbProductHelper.getProductByName(DEMO_PRODUCT_5);
            if (product == null) {
                DbProductHelper.create(DEMO_PRODUCT_5, 3, ImageLoaderHelper.DRAWABLE_PREF + R.drawable.ic_menu, DbUserHelper.getUser(DEMO_USER_1).getId());
                DbBidHelper.create(DbUserHelper.getUser(DEMO_USER_2).getId(), DbProductHelper.getProductByName(DEMO_PRODUCT_5).getIdProduct(), 10);
                Product p = DbProductHelper.getProductByName(DEMO_PRODUCT_5);
                p.setIsAuctioned(false);
                p.setWinnerId(DbUserHelper.getUser(DEMO_USER_2).getId());
                p.setIsWon(true);
                DbProductHelper.getProductDao().update(p);
            }
        }

        {
            Product product = DbProductHelper.getProductByName(DEMO_PRODUCT_6);
            if (product == null) {
                DbProductHelper.create(DEMO_PRODUCT_6, 1, null, DbUserHelper.getUser(DEMO_USER_2).getId());
                DbBidHelper.create(DbUserHelper.getUser(DEMO_USER_1).getId(), DbProductHelper.getProductByName(DEMO_PRODUCT_6).getIdProduct(), 3);
                Product p = DbProductHelper.getProductByName(DEMO_PRODUCT_6);
                p.setWinnerId(DbUserHelper.getUser(DEMO_USER_1).getId());
                p.setIsAuctioned(false);
                p.setIsWon(true);
                DbProductHelper.getProductDao().update(p);
            }
        }

    }
}
