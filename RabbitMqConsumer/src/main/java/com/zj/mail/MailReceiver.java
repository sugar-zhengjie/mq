package com.zj.mail;

import com.rabbitmq.client.Channel;
import com.zj.model.MailConstants;
import com.zj.model.MailUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;


/**
 * @author zhengjie
 */
@Component
public class MailReceiver {

    private static final Logger logger = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    MailProperties mailProperties;

    @Autowired
    TemplateEngine templateEngine;

    /*
      1.接收消息，转换成实体类
      2.使用Redis判断是否已经被消费，被消费NACK
      3.发送邮件，成功ACK 失败NACK
     */

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) throws IOException {

        MailUser user = (MailUser) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        String messageId = (String) headers.get("spring_returned_message_correlation");
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        if(!Optional.ofNullable(tag).isPresent() || !Optional.ofNullable(messageId).isPresent()){
            return;
        }

        if(redisTemplate.opsForHash().entries("mail_log").containsKey(messageId)){
            logger.info(messageId + ":消息已经被消费");
            channel.basicAck(tag,true);
            return;
        }
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg);
            helper.setTo(user.getEmail());
            helper.setFrom(mailProperties.getUsername());
            helper.setSubject("邮件主题");
            helper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("变量", "实际值");
            context.setVariable("name", user.getName());

            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            javaMailSender.send(msg);

            redisTemplate.opsForHash().put("mail_log", messageId, messageId);
            channel.basicAck(tag, false);
            logger.info(messageId + ":邮件发送成功");

        } catch (Exception e1) {
            try {
                channel.basicNack(tag, false, true);
            }catch (Exception e2) {
                // 打印日志，后续做补偿处理
                e2.printStackTrace();
                logger.error("邮件发送失败：" + e2.getMessage());
            }
            e1.printStackTrace();
            logger.error("邮件发送失败：" + e1.getMessage());
        }
    }


    // 配置了MessageConverter可直接转换实体类
//    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
//    public void handler(MailUser user, Channel channel){
//    }
}
