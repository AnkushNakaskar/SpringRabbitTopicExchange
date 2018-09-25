package com.rabbit.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer2 implements MessageListener {

    @Override
    public void onMessage(Message message) {

        System.out.println("consumer2..............................." + message.getMessageProperties().getConsumerQueue());
        System.out.println("Only topic message are printed here");
    }

}
