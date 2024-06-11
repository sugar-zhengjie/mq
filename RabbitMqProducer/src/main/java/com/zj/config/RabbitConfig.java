package com.zj.config;

import com.zj.model.MailConstants;
import com.zj.service.MailSendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhengjie
 */
@Configuration
public class RabbitConfig {

    public final static Logger logger = LoggerFactory.getLogger(RabbitConfig.class);

    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Autowired
    MailSendLogService mailSendLogService;

    @Bean
    RabbitTemplate rabbitTemplate() {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            String msgId = Objects.requireNonNull(data).getId();
            if (ack) {
                logger.info(msgId + ":消息发送成功");
                mailSendLogService.updateMailSendLogStatus(msgId, 1);//修改数据库中的记录，消息投递成功
            } else {
                logger.info(msgId + ":消息发送失败");
            }
        });

        // Spring AMQP 2.3.0 该方法被废弃
        /*rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            logger.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });*/

        rabbitTemplate.setReturnsCallback(returned -> {
            String correlationId = returned.getMessage().getMessageProperties().getCorrelationId();
            logger.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, returned.getReplyCode(), returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
        });


        return rabbitTemplate;
    }

   /*************************************** email mq ***************************************/
    @Bean
    Queue mailQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", MailConstants.DEAD_LETTER_EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", MailConstants.DEAD_LETTER_ROUTING_KEY_NAME);
        return new Queue(MailConstants.MAIL_QUEUE_NAME, true, false, false, args);
    }


    @Bean
    DirectExchange mailExchange() {
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME);
    }


    /*************************************** sms mq ***************************************/
    @Bean
    Queue smsQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", MailConstants.DEAD_LETTER_EXCHANGE_NAME);
        args.put("x-dead-letter-routing-key", MailConstants.DEAD_LETTER_ROUTING_KEY_NAME);
        return new Queue(MailConstants.SMS_QUEUE_NAME, true, false, false, args);
    }

    @Bean
    DirectExchange smsExchange() {
        return new DirectExchange(MailConstants.SMS_EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding smsBinding() {
        return BindingBuilder.bind(smsQueue()).to(smsExchange()).with(MailConstants.SMS_ROUTING_KEY_NAME);
    }

}
