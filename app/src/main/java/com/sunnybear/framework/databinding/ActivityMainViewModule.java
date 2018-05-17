package com.sunnybear.framework.databinding;

import android.net.Uri;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sunnybear.framework.R;
import com.sunnybear.framework.dao.UserDao;
import com.sunnybear.framework.entity.User;
import com.sunnybear.framework.library.base.BaseViewModule;
import com.sunnybear.framework.module.MainActivity;
import com.sunnybear.framework.provider.ARouterTestService;
import com.sunnybear.framework.tools.log.Logger;
import com.sunnybear.library.database.DatabaseHelper;

import org.reactivestreams.Publisher;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * <p>
 * Created by chenkai.gu on 2018/4/29.
 */
public class ActivityMainViewModule extends BaseViewModule<MainActivity, ActivityMainBinding>
        implements View.OnClickListener {

    private UserDao mUserDao;

    @Autowired(name = "/service/test")
    ARouterTestService mService;

    public ActivityMainViewModule(MainActivity mainActivity, ActivityMainBinding viewDataBinding) {
        super(mainActivity, viewDataBinding);
    }

    @Override
    public void init() {
        mViewDataBinding.setVm(this);
        ARouter.getInstance().inject(this);

        mUserDao = (UserDao) DatabaseHelper.getDao(User.class);

        User user = new User("gu", "sunnybear");
//        mUserDao.insert(user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map:
                ARouter.getInstance()
                        .build(Uri.parse("arouter://sunnybear/framework/map"))
//                        .greenChannel()
                        .navigation();
                break;
            case R.id.btn_service:
                mService.printToast("ARouter暴露服务");
                break;
            case R.id.btn_query:
                mUserDao.getUserAll()
                        .flatMap(new Function<List<User>, Publisher<User>>() {
                            @Override
                            public Publisher<User> apply(List<User> users) throws Exception {
                                return Flowable.fromIterable(users);
                            }
                        })
//                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<User>() {
                            @Override
                            public void accept(User user) throws Exception {
                                Logger.i(user.toString());
                            }
                        }).subscribe();
                break;
        }
    }
}
