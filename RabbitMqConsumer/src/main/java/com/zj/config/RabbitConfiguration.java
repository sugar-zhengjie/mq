package com.zj.config;

import com.zj.model.MailConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.MessageConverter;


/**
 * @author zhengjie
 */
@Configuration
public class RabbitConfiguration {

    @Bean
    Queue mailOneQueue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }

//    @Bean
//    public MessageConverter jsonMessageConverter(){
//        return new Jackson2JsonMessageConverter();
//    }

}
