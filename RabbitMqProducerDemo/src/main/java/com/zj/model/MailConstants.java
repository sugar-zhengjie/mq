package com.zj.model;

/**
 * @author zhengjie
 */
public class MailConstants {
    //消息投递中
    public static final Integer DELIVERING = 0;
    //消息投递成功
    public static final Integer SUCCESS = 1;
    //消息投递失败
    public static final Integer FAILURE = 2;
    //最大重试次数
    public static final Integer MAX_TRY_COUNT = 3;
    //消息超时时间
    public static final Integer MSG_TIMEOUT = 1;
    public static final String MAIL_QUEUE_NAME = "zj.mail.type.one";
    public static final String MAIL_EXCHANGE_NAME = "zj.mail.exchange";
    public static final String MAIL_ROUTING_KEY_NAME = "zj.mail.routing.key";
}
