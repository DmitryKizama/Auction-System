package com.stkizema.auction.db;

import android.content.Context;

import com.stkizema.auction.TopApp;
import com.stkizema.auction.model.DaoSession;
import com.stkizema.auction.model.User;
import com.stkizema.auction.model.UserDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DbUserHelper {

    private static DbUserHelper instance;
    private static DaoSession daoSession;

    private DbUserHelper(Context context) {
        daoSession = ((TopApp) context).getDaoSession();
    }

    public static synchronized DbUserHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbUserHelper(context);
        }

        return instance;
    }


    public static User getUser(String login) {
        if (login == null) {
            return null;
        }

        UserDao userDao = daoSession.getUserDao();

        QueryBuilder<User> queryBuilder = userDao.queryBuilder();
        List<User> userLists = queryBuilder.where(UserDao.Properties.Login.eq(login)).list();
        if (userLists == null || userLists.size() == 0) {
            return null;
        }

        return userLists.get(0);
    }

    public static User getUserByLoginAndPass(String login, String pass) {
        if (login == null) {
            return null;
        }

        UserDao userDao = daoSession.getUserDao();

        QueryBuilder<User> queryBuilder = userDao.queryBuilder();
        List<User> userLists = queryBuilder.where(UserDao.Properties.Login.eq(login)).where(UserDao.Properties.Password.eq(pass)).list();
        if (userLists == null || userLists.size() == 0) {
            return null;
        }

        return userLists.get(0);
    }

    public static List<User> getListUsers() {
        UserDao userDao = daoSession.getUserDao();
        Query<User> userQuery = userDao.queryBuilder().where(UserDao.Properties.Permission.eq(User.PERMISSIONUSER)).build();
        return userQuery.list();
    }

    public static User getUserById(Long userId) {
        if (userId == null) {
            return null;
        }
        UserDao userDao = daoSession.getUserDao();
        Query<User> userQuery = userDao.queryBuilder().where(UserDao.Properties.Id.eq(userId)).build();
        if (userQuery.list() == null || userQuery.list().isEmpty()) {
            return null;
        }
        return userQuery.list().get(0);
    }

    public static List<User> getList() {
        UserDao userDao = daoSession.getUserDao();
        Query<User> userQuery = userDao.queryBuilder().orderAsc(UserDao.Properties.Id).build();
        return userQuery.list();
    }

    public static UserDao getUserDao() {
        return daoSession.getUserDao();
    }

    public static User create(String login, String email, String password) {
        User u = DbUserHelper.getUserByLoginAndPass(login, password);
        if (u != null) {
            return null;
        }

        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setPassword(password);
        user.setPermission(User.PERMISSIONUSER);
        DbUserHelper.getUserDao().insert(user);

        return user;
    }

    private static boolean isContains(User user, List<User> list) {
        for (User u : list) {
            if (u.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }

}
