package com.lanslot.fastvideo.DB;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

public class UserInfoDao {
    private DbManager dbManager;
    private volatile static UserInfoDao instance = null;

    static public UserInfoDao getInstance() {
        if (instance == null) {
            synchronized (AuthUtils.class) {
                if (instance == null) {
                    instance = new UserInfoDao();
                }
            }
        }
        return instance;
    }

    private UserInfoDao() {
        try {
            DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                    .setDbName("fastvideo_db") //设置数据库名
                    .setDbVersion(1) //设置数据库版本
                    .setDbOpenListener(new DbManager.DbOpenListener() {
                        @Override
                        public void onDbOpened(DbManager db) {
                            db.getDatabase().enableWriteAheadLogging();
                            //开启WAL, 对写入加速提升巨大(作者原话)
                        }
                    })
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                            //数据库升级操作
                        }
                    });
            dbManager = x.getDb(daoConfig);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void addOrUpdate(UserInfo userInfo) {
        try {
            UserInfo ui = find();
            if(ui != null){
                userInfo.setId(0);
                userInfo.setPassword(ui.getPassword());
                update(userInfo);
            }else {
                userInfo.setId(0);
                dbManager.save(userInfo);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public UserInfo find() {
        UserInfo userInfo = null;
        try {
            userInfo = dbManager.findFirst(UserInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }finally {
            return userInfo;
        }
    }

    public void update(UserInfo userInfo) {
        try {
            userInfo.setId(0);
            dbManager.update(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            dbManager.delete(find());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
