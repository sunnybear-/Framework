package com.sunnybear.framework.library.eventbus;

import java.io.Serializable;

/**
 * EventBus消息类型
 * Created by chenkai.gu on 2018/1/23.
 */
public class EventBusMessage<T> implements Serializable {
    private String messageTag;//消息标签
    private T messageBody;//消息体

    public String getMessageTag() {
        return messageTag;
    }

    public void setMessageTag(String messageTag) {
        this.messageTag = messageTag;
    }

    public T getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(T messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * 组装消息
     *
     * @param tag         标签
     * @param messageBody 消息体
     * @return Presenter层传递消息
     */
    public static <T> EventBusMessage assembleMessage(String tag, T messageBody) {
        EventBusMessage message = new EventBusMessage();
        message.setMessageTag(tag);
        message.setMessageBody(messageBody);
        return message;
    }
}
