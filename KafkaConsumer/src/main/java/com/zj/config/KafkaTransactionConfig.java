package com.zj.config;

import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.BatchMessagingMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

/**
 * @author zhengjie
 * @version 1.0
 * @date 2023/10/23 15:38
 */
@Configuration
public class KafkaTransactionConfig {
    /**
     * 设置KAFKA监听工厂， 支持批量消息处理
     * @param configurer
     * @param kafkaConsumerFactory
     * @param template
     * @return
     */
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
//            ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
//            ConsumerFactory<Object, Object> kafkaConsumerFactory,
//            KafkaTemplate<Object, Object> template) {
//        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        configurer.configure(factory, kafkaConsumerFactory);
//        factory.setBatchListener(true); //配置文件中也可以设置
//        factory.setMessageConverter(batchConverter());
//        return factory;
//    }


    /**
     * 设置消息转换器
     * @return
     */
//    @Bean
//    public RecordMessageConverter converter() {
//        return new StringJsonMessageConverter();
//    }

    /**
     * 设置批量消息处理器
     * @return
     */
//    @Bean
//    public BatchMessagingMessageConverter batchConverter() {
//        return new BatchMessagingMessageConverter(converter());
//    }
}
