package com.sunnybear.library.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.io.Serializable;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/17.
 */
public interface BaseDao<T extends Serializable> {

    /**
     * 插入数据表,当有该条数据时就替换
     *
     * @param ts
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T... ts);

    @Delete
    void delete(T t);

    @Update
    void update(T t);
}
