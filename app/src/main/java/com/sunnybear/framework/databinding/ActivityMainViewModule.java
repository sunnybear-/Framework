package com.sunnybear.framework.databinding;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.sunnybear.framework.R;
import com.sunnybear.framework.dao.UserDao;
import com.sunnybear.framework.entity.User;
import com.sunnybear.framework.library.base.BaseViewModule;
import com.sunnybear.framework.module.BannerActivity;
import com.sunnybear.framework.module.MainActivity;
import com.sunnybear.framework.provider.ARouterTestService;
import com.sunnybear.framework.tools.StartHelper;
import com.sunnybear.framework.tools.log.Logger;
import com.sunnybear.library.database.DatabaseHelper;

import org.reactivestreams.Publisher;

import java.util.Date;
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

    private ObjectAnimator mAnimator;

    private TimePickerView mTimePickerView;

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

//        mAnimator = ObjectAnimator
//                .ofFloat(mViewDataBinding.target, "scaleY", 1, 1.5f, 1);
//        mAnimator.setDuration(1000);
//        mAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        mTimePickerView = initTimePicker();
    }

    private TimePickerView initTimePicker() {
        TimePickerView timePickerView = new TimePickerBuilder(mContext,
                new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {

                    }
                }).setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true)
                .build();
        Dialog mDialog = timePickerView.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);
            params.leftMargin = 0;
            params.rightMargin = 0;
            timePickerView.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
//                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
        return timePickerView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_map:
//                ARouter.getInstance()
//                        .build(Uri.parse("arouter://sunnybear/framework/map"))
////                        .greenChannel()
//                        .navigation();
                StartHelper.with(mContext).startActivity(BannerActivity.class);
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
//            case R.id.btn_anim:
////                mAnimator.start();
//                Toast toast = Toasty.normal(mContext, "居中显示");
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//                break;
//            case R.id.btn_clear_anim:
////                mAnimator.end();
//                break;
            case R.id.btn_date:
                mTimePickerView.show(v);
                break;
        }
    }
}
