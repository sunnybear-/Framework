package com.sunnybear.framework.library.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.sunnybear.framework.R;
import com.sunnybear.framework.library.eventbus.EventBusMessage;
import com.sunnybear.framework.tools.ActivityStackManager;
import com.sunnybear.framework.tools.AppUtils;
import com.sunnybear.framework.tools.KeyboardUtils;
import com.sunnybear.framework.tools.ResourcesUtils;
import com.sunnybear.framework.tools.Toasty;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * 基础FragmentActivity,主管模组分发
 * Created by chenkai.gu on 2018/1/14.
 */
public abstract class BaseActivity<VDB extends ViewDataBinding, VM extends BaseViewModule> extends RxAppCompatActivity
        implements Presenter {

    protected Context mContext;
    private VDB mViewDataBinding;

    protected VM mViewModule;

    private Toolbar mToolbar;
    private TextView mToolTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mViewModule = bindingViewModule(mViewDataBinding);
        getLifecycle().addObserver(mViewModule);
        /*设置Toolbar*/
        mToolbar = mViewDataBinding.getRoot().findViewById(R.id.toolbar);
        mToolTitle = mViewDataBinding.getRoot().findViewById(R.id.toolbar_title);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("");
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationClick(v);
                }
            });
        }
        setDisplayHomeAsUpEnabled(true);

        /*沉浸式状态栏*/
        setStatusBar();

        Bundle args = getIntent().getExtras();//接收前一个Activity传递的参数
        onBundle(args);

        /*添加Activity到栈管理*/
        ActivityStackManager.getInstance().addActivity(this);
    }

    /**
     * 设置状态栏样式
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, ResourcesUtils.getColor(mContext, R.color.colorPrimary), 0);
    }

    @Override
    protected void onDestroy() {
        ActivityStackManager.getInstance().removeActivity(this);
        super.onDestroy();
        getLifecycle().removeObserver(mViewModule);
    }

    /**
     * 设置是否有返回箭头
     *
     * @param showHomeAsUp
     */
    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setDisplayHomeAsUpEnabled(showHomeAsUp);
    }

    /**
     * 设置返回图标
     *
     * @param iconResId
     */
    public void setNavigationIcon(int iconResId) {
        if (mToolbar != null)
            mToolbar.setNavigationIcon(iconResId);
    }

    /**
     * 设置返回图标
     *
     * @param icon
     */
    public void setNavigationIcon(Drawable icon) {
        if (mToolbar != null)
            mToolbar.setNavigationIcon(icon);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (mToolTitle != null)
            mToolTitle.setText(title);
    }

    /**
     * Toolbar左上角图片点击监听
     *
     * @param v
     */
    public void onNavigationClick(View v) {
        onBackPressed();
    }

    /**
     * 设置布局id
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 绑定ViewModule
     *
     * @param viewDataBinding
     * @return
     */
    protected abstract VM bindingViewModule(VDB viewDataBinding);

    /**
     * 接收bundle传递参数
     *
     * @param args bundle参数
     */
    protected void onBundle(Bundle args) {
        mViewModule.onBundle(args);
    }

    /**
     * EventBus接收方法
     *
     * @param message
     * @param <T>
     */
    @Deprecated
    public <T> void onSubscriberEvent(EventBusMessage<T> message) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //判断软键盘是否展开
        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            android.view.View view = getCurrentFocus();
            if (isShouldHideInput(view, ev))
                KeyboardUtils.closeKeyboard(mContext, view);
            return super.dispatchTouchEvent(ev);
        }
        //必不可少,否则所有的组件都不会有TouchEvent事件了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    /**
     * 是否隐藏软键盘
     *
     * @param view  对应View
     * @param event 事件
     */
    private boolean isShouldHideInput(android.view.View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            view.getLocationInWindow(leftTop);
            //获取输入框当前位置
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /*退出时间*/
    private long exitTime = 0;

    /**
     * 双击退出app
     *
     * @param exit 退出间隔时间(毫秒数)
     */
    protected void exit(long exit) {
        if (System.currentTimeMillis() - exitTime > exit) {
            String message = mContext.getResources().getString(R.string.exit_message);
            Toasty.normal(mContext, message, Toast.LENGTH_LONG).show();
            exitTime = System.currentTimeMillis();
        } else {
            ActivityStackManager.getInstance().finishAllActivity();
            AppUtils.gc(mContext);
        }
    }

    /**
     * 双击退出app
     */
    protected void exit() {
        exit(2000);
    }
}
