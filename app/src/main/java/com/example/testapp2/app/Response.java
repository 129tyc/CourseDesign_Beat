package com.example.testapp2.app;

/**
 * Created by Code on 2016/11/8 0008.
 * 服务器返回类
 *
 * @deprecated 暂不使用
 */
public class Response<T> {
    /**
     * 时间
     */
    private String event;
    /**
     * 消息
     */
    private String msg;
    /**
     * 返回的object
     */
    private T object;
    /**
     * 返回的的object表
     */
    private T objectList;

    public Response() {
    }

    @Override
    public String toString() {
        return "Response{" +
                "event='" + event + '\'' +
                ", msg='" + msg + '\'' +
                ", object=" + object +
                ", objectList=" + objectList +
                '}';
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public T getObjectList() {
        return objectList;
    }

    public void setObjectList(T objectList) {
        this.objectList = objectList;
    }
}
