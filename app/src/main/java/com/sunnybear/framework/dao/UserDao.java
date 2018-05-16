package com.sunnybear.framework.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.sunnybear.framework.entity.User;

import java.util.List;

/**
 * <p>
 * Created by chenkai.gu on 2018/5/14.
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getUserAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :firstName AND " +
            "last_name LIKE :lastName LIMIT 1")
    User findByName(String firstName, String lastName);

    @Insert
    void insertALL(User... users);

    @Delete
    void detele(User user);
}
