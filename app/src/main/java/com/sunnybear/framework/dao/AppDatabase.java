package com.sunnybear.framework.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sunnybear.framework.entity.User;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/14.
 */
@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
}
