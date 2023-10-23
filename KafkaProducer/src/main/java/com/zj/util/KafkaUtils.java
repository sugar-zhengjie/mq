package com.zj.util;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 15:03
 */

import com.google.common.collect.Lists;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 操作kafka的工具类
 */

@Component
public class KafkaUtils {

    @Value("${spring.kafka.bootstrap-servers}")
    private String springKafkaBootstrapServers;

    private AdminClient adminClient;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * 初始化AdminClient
     * '@PostConstruct该注解被用来修饰一个非静态的void（）方法。
     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
     * PostConstruct在构造函数之后执行，init（）方法之前执行。
     */
    @PostConstruct
    private void initAdminClient() {
        Map<String, Object> props = new HashMap<>(1);
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, springKafkaBootstrapServers);
        adminClient = KafkaAdminClient.create(props);
    }

    /**
     * 新增topic，支持批量
     */
    public void createTopic(Collection<NewTopic> newTopics) {
        adminClient.createTopics(newTopics);
    }

    /**
     * 删除topic，支持批量
     */
    public void deleteTopic(Collection<String> topics) {
        adminClient.deleteTopics(topics);
    }

    /**
     * 获取指定topic的信息
     */
    public String getTopicInfo(Collection<String> topics) {
        AtomicReference<String> info = new AtomicReference<>("");
        try {
            adminClient.describeTopics(topics).all().get().forEach((topic, description) -> {
                for (TopicPartitionInfo partition : description.partitions()) {
                    info.set(info + partition.toString() + "\n");
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return info.get();
    }

    /**
     * 获取全部topic
     */
    public List<String> getAllTopic() {
        try {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(true); // 是否包含内部主题

            ListTopicsResult topics = adminClient.listTopics(options);
            return topics.listings().get().stream()
                    .map(TopicListing::name)
                    .collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            adminClient.close();
        }
        return Lists.newArrayList();
    }

    /**
     * 往topic中发送消息
     */
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

}

