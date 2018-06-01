package com.sunnybear.library.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 数据库工具
 * Created by chenkai.gu on 2018/5/17.
 */
public class DatabaseHelper {

    private static DatabaseHelper INSTANCE;

    private RoomDatabase mDatabase;

    private RoomDatabase getDatabase() {
        return mDatabase;
    }

    private DatabaseHelper(Context context, Class<? extends RoomDatabase> clazz, String databaseName, Migration... migrations) {
        mDatabase = Room.databaseBuilder(context, clazz, databaseName)
                .addCallback(new RoomDatabase.Callback() {
                    /**
                     * 第一次创建数据库时调用,但是在创建所有表之后调用的
                     * @param db
                     */
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                    }

                    /**
                     * 当数据库被打开时调用
                     * @param db
                     */
                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        super.onOpen(db);
                    }
                })
//                .allowMainThreadQueries()//允许在主线程查询数据
                .addMigrations(migrations)//迁移数据库使用,下面会单独拿出来讲
//                .fallbackToDestructiveMigration()//迁移数据库如果发生错误,将会重新创建数据库,而不是发生崩溃
                .build();
    }

    /**
     * 数据库的初始化
     *
     * @param context
     * @param clazz
     * @param databaseName
     * @param migrations
     */
    public static void initialize(Context context, Class<? extends RoomDatabase> clazz, String databaseName, Migration... migrations) {
        if (INSTANCE == null)
            synchronized (DatabaseHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new DatabaseHelper(context, clazz, databaseName, migrations);
            }
    }

    /**
     * 获取Dao对象
     *
     * @param entity
     * @return
     */
    public static BaseDao getDao(Class<? extends Serializable> entity) {
        BaseDao dao = null;
        if (INSTANCE != null) {
            try {
                Class<?> clazz = INSTANCE.getDatabase().getClass();
                Method method = clazz.getMethod("get" + entity.getSimpleName() + "Dao");
                dao = (BaseDao) method.invoke(INSTANCE.getDatabase());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dao;
    }
}
