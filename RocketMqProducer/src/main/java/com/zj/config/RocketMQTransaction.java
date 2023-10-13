package com.zj.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/13 10:43
 */
@Component
@RocketMQTransactionListener(rocketMQTemplateBeanName = "rocketMQTemplate")
public class RocketMQTransaction implements RocketMQLocalTransactionListener {

    private final ConcurrentHashMap map = new ConcurrentHashMap();

    /**
     * 生产者发送消息，执行本地事务
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        System.out.println("msg:  "+msg);
        System.out.println("arg:  "+arg);
        map.put(msg, arg);
        if(arg instanceof Map) {
            Map map = (Map)arg;
            String age = (String)map.get("sAge");
            if(StringUtils.isNotEmpty(age) && age.equals("3")) {
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            if(StringUtils.isNotEmpty(age) && age.equals("2")) {
                return RocketMQLocalTransactionState.COMMIT;
            }
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    /**
     * 检查本地事务
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        map.forEach((Object t, Object u) -> {System.out.println(t+" - "+u);});
        return RocketMQLocalTransactionState.COMMIT;
    }

}
