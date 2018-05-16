package com.sunnybear.framework.library.eventbus;

import org.greenrobot.eventbus.EventBus;

/**
 * 事件总线助手
 * Created by chenkai.gu on 2018/1/23.
 */
public final class EventBusHelper {
    private static EventBus mEventBus = EventBus.getDefault();

    /**
     * 注册EventBus
     *
     * @param subscriber 订阅者
     */
    public static void register(Object subscriber) {
        if (!mEventBus.isRegistered(subscriber))
            mEventBus.register(subscriber);
    }

    /**
     * 反注册EventBus
     *
     * @param subscriber 订阅者
     */
    public static void unregister(Object subscriber) {
        if (mEventBus.isRegistered(subscriber))
            mEventBus.unregister(subscriber);
        mEventBus.removeAllStickyEvents();
    }

    /**
     * 移除EventBus粘滞事件
     *
     * @param message
     */
    public static void removeStickyEvent(EventBusMessage message) {
        mEventBus.removeStickyEvent(message);
    }

    /**
     * 发送EventBus事件
     *
     * @param message EventBus消息
     */
    public static void post(EventBusMessage message) {
        mEventBus.post(message);
    }

    /**
     * 发送EventBus粘滞事件
     *
     * @param message EventBus消息
     */
    public static void postSticky(EventBusMessage message) {
        mEventBus.postSticky(message);
    }
}
