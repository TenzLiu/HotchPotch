package com.tenz.hotchpotch.event;

/**
 * Author: TenzLiu
 * Date: 2018-03-20 10:39
 * Description: 极光推送消息
 */

public class JPushEvent {

    private int pushType;//1:通知  2:自定义消息
    private Notification mNotification;
    private CustomMessage mCustomMessage;

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    public Notification getNotification() {
        return mNotification;
    }

    public void setNotification(Notification notification) {
        mNotification = notification;
    }

    public CustomMessage getCustomMessage() {
        return mCustomMessage;
    }

    public void setCustomMessage(CustomMessage customMessage) {
        mCustomMessage = customMessage;
    }

    @Override
    public String toString() {
        return "JPushEvent{" +
                "pushType=" + pushType +
                ", mNotification=" + mNotification +
                ", mCustomMessage=" + mCustomMessage +
                '}';
    }

    /**
     * 通知
     */
    public static class Notification{
        private String alert;
        private int noticication_id;
        private int alert_type;
        private String notification_content_title;
        private int msg_id;

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public int getNoticication_id() {
            return noticication_id;
        }

        public void setNoticication_id(int noticication_id) {
            this.noticication_id = noticication_id;
        }

        public int getAlert_type() {
            return alert_type;
        }

        public void setAlert_type(int alert_type) {
            this.alert_type = alert_type;
        }

        public String getNotification_content_title() {
            return notification_content_title;
        }

        public void setNotification_content_title(String notification_content_title) {
            this.notification_content_title = notification_content_title;
        }

        public int getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(int msg_id) {
            this.msg_id = msg_id;
        }

        @Override
        public String toString() {
            return "Notification{" +
                    "alert='" + alert + '\'' +
                    ", noticication_id=" + noticication_id +
                    ", alert_type=" + alert_type +
                    ", notification_content_title='" + notification_content_title + '\'' +
                    ", msg_id=" + msg_id +
                    '}';
        }
    }

    /**
     * 自定义消息
     */
    public static class CustomMessage{
        private String title;
        private String message;
        private int content_type;
        private String appkey;
        private int msg_id;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getContent_type() {
            return content_type;
        }

        public void setContent_type(int content_type) {
            this.content_type = content_type;
        }

        public String getAppkey() {
            return appkey;
        }

        public void setAppkey(String appkey) {
            this.appkey = appkey;
        }

        public int getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(int msg_id) {
            this.msg_id = msg_id;
        }

        @Override
        public String toString() {
            return "CustomMessage{" +
                    "title='" + title + '\'' +
                    ", message='" + message + '\'' +
                    ", content_type=" + content_type +
                    ", appkey='" + appkey + '\'' +
                    ", msg_id=" + msg_id +
                    '}';
        }
    }

}
