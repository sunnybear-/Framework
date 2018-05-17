package com.sunnybear.framework.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.sunnybear.framework.entity.User;
import com.sunnybear.library.database.BaseDao;

import java.util.List;

import io.reactivex.Flowable;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/17.
 */
@Dao
public interface UserDao extends BaseDao<User> {

    @Query("select * from tb_user")
    Flowable<List<User>> getUserAll();

    @Query("select * from tb_user where id in (:ids)")
    Flowable<List<User>> getUserAllByIds(int[] ids);
}
