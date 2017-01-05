package com.rabbit.config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.portable.RemarshalException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	public static final String topicQueueIncluded = "included.topic.queue";
	public static final String topicQueueIncluded1 = "included.topic.queue1";
	public static final String topicQueueexcluded = "included.topic.queue2";

	@Bean
	public ConnectionFactory rabbitConnectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setAddresses("localhost");
		factory.setUsername("guest");
		factory.setPassword("guest");
		return factory;
	}
	
	@Bean
	public RabbitTemplate template(){
		RabbitTemplate template =new RabbitTemplate();
		template.setRoutingKey("included");
		template.setConnectionFactory(rabbitConnectionFactory());
		template.setExchange("included");
		return template;
	}

	@Bean
	public Queue topicQueue1() {
		return new Queue(topicQueueIncluded, true);
	}

	@Bean
	public Queue topicQueue2() {
		return new Queue(topicQueueIncluded1, true);
	}

	@Bean
	public Queue topicQueue3() {
		return new Queue(topicQueueexcluded, true);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange("my-exchange", false, true);
	}
//	 @Bean
//     Binding binding() {
//         return BindingBuilder.bind(topicQueue1()).to(exchange()).with("ru.interosite.*");
//     }

	@Bean
	List<Binding> bindings() {
		return Arrays.asList(BindingBuilder.bind(topicQueue1()).to(exchange()).with("ru.interosite.*"),
				BindingBuilder.bind(topicQueue2()).to(exchange()).with("ru.interosite.*"),
				BindingBuilder.bind(topicQueue3()).to(exchange()).with("excluded.*"));
	}

	@Bean
	SimpleMessageListenerContainer container() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitConnectionFactory());
		container.setQueueNames(new String[] { topicQueueexcluded, topicQueueIncluded, topicQueueIncluded1 });
		container.setMessageListener(new Consumer());
		return container;
	}

}