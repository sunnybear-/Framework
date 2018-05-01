package com.sunnybear.framework.library.eventbus;

import java.io.Serializable;

import io.reactivex.Flowable;

/**
 * EventBus观察者消息类型
 * Created by chenkai.gu on 2018/4/29.
 */
public class ObservableEventBusMessage<T> implements Serializable {
    private String messageTag;//消息标签
    private Flowable<T> messageBody;//观察者消息体

    public String getMessageTag() {
        return messageTag;
    }

    public void setMessageTag(String messageTag) {
        this.messageTag = messageTag;
    }

    public Flowable<T> getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(Flowable<T> messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * 组装消息
     *
     * @param tag         标签
     * @param messageBody 消息体
     * @return Presenter层传递消息
     */
    public static <T> ObservableEventBusMessage assembleMessage(String tag, T messageBody) {
        ObservableEventBusMessage message = new ObservableEventBusMessage();
        message.setMessageTag(tag);
        message.setMessageBody(Flowable.just(messageBody));
        return message;
    }
}
