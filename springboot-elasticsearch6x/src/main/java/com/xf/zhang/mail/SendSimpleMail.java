package com.xf.zhang.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SendSimpleMail {

    @Autowired
    private JavaMailSender mailSender;

	/**
     * 发送简单邮件
	 * @throws Exception
	 */
    @Test
    @Bean
    public void sendSimpleMail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("2532466427@qq.com");
        message.setTo("mr_zxf0105@163.com");
        message.setSubject("主题：网易发腾讯邮件");
        message.setText("测试邮件内容：网易发腾讯邮件");

        mailSender.send(message);
    }





}
