package com.sunnybear.framework.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sunnybear.framework.entity.User;

/**
 * 定义数据库
 * Created by chenkai.gu on 2018/5/17.
 */
@Database(entities = {User.class}, version = 1)
public abstract class FrameworkDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();
}
