package com.zj.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 13:31
 */
@Component
public class CustomizePartitioner implements Partitioner {
    @Override
    public int partition(String s, Object key, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        //定义自己的分区策略,如果key以0开头，发到0号分区,其他都扔到1号分区
        String keyStr = key+"";
        if (keyStr.startsWith("0")){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
