package com.rabbit.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;
	private final Consumer receiver;
	private final ConfigurableApplicationContext context;

	public Runner(Consumer receiver, RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context) {
		this.receiver = receiver;
		this.rabbitTemplate = rabbitTemplate;
		this.context = context;
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 1; i++) {
			System.out.println("Sending message...");
//			System.out.println(
//					".........No queue bound to exchange..hence message is discarded ....you can see exchange is different than queue name....");
//			rabbitTemplate.convertAndSend("included","included","Hello from RabbitMQ!");
			rabbitTemplate.convertAndSend("my-exchange", "ru.interosite.queue1", "ttt1233");//sending to only queue1
			rabbitTemplate.convertAndSend("my-exchange", "ru.interosite.queue2", "ttt1233");//sending to queue2
			rabbitTemplate.convertAndSend("my-exchange", "ru.interosite.queue3", "ttt1233");//sending to queue3
			rabbitTemplate.convertAndSend("my-exchange", "ru.interosite.queue.*", "ttt1233");//sending to all queue
			
		}
	}

}