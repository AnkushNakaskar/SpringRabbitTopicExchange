package com.rabbit.config;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.omg.CORBA.portable.RemarshalException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;

@Configuration
public class RabbitMqConfig {

	public static final String topicQueueIncluded = "included.topic.queue";
	public static final String topicQueueIncluded1 = "included.topic.queue1";
	public static final String topicQueueIncluded2 = "included.topic.queue2";

	@Bean
	public ConnectionFactory rabbitConnectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setAddresses("localhost");
		factory.setUsername("guest");
		factory.setPassword("guest");
		return factory;
	}

	@Bean
	public RabbitTemplate template() {
		RabbitTemplate template = new RabbitTemplate();
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
		return new Queue(topicQueueIncluded2, true);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange("my-exchange", false, true);
	}



	@Bean
	public Advice[] advices() {
		ExponentialBackOffPolicy backoffPolicy = new ExponentialBackOffPolicy();
		backoffPolicy.setInitialInterval(10);
		backoffPolicy.setMaxInterval(1000);
		backoffPolicy.setMultiplier(2);
		Advice[] adviceChain = new Advice[1];
		List<Advice> listOfAdvices = new LinkedList<>();
		Advice advice = RetryInterceptorBuilder.stateless().backOffPolicy(backoffPolicy).maxAttempts(3).build();
		listOfAdvices.add(advice);
		return listOfAdvices.toArray(adviceChain);
	}

	@Bean
	List<Binding> bindings() {
		return Arrays.asList(BindingBuilder.bind(topicQueue1()).to(exchange()).with("ru.interosite.queue1"),BindingBuilder.bind(topicQueue1()).to(exchange()).with("ru.interosite.common.all"),
				BindingBuilder.bind(topicQueue2()).to(exchange()).with("ru.interosite.queue2"),BindingBuilder.bind(topicQueue2()).to(exchange()).with("ru.interosite.common.all"),
				BindingBuilder.bind(topicQueue3()).to(exchange()).with("ru.interosite.queue3"),BindingBuilder.bind(topicQueue3()).to(exchange()).with("ru.interosite.common.all"));
	}

	@Bean
	SimpleMessageListenerContainer queue1Listener() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitConnectionFactory());
		container.setQueueNames(new String[] { topicQueueIncluded });
		container.setMessageListener(new Consumer());
		container.setAdviceChain(advices());
		container.setConcurrentConsumers(1);//two consumer(thread will listen at the same time to this queues).
		return container;
	}

	@Bean
	SimpleMessageListenerContainer queue2Listener() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitConnectionFactory());
		container.setQueueNames(new String[] { topicQueueIncluded1 });
		container.setMessageListener(new Consumer1());
		container.setAdviceChain(advices());
		container.setConcurrentConsumers(1);//two consumer(thread will listen at the same time to this queues).
		return container;
	}

	@Bean
	SimpleMessageListenerContainer queue3Listener() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(rabbitConnectionFactory());
		container.setQueueNames(new String[] { topicQueueIncluded2});
		container.setMessageListener(new Consumer2());
		container.setAdviceChain(advices());
		container.setConcurrentConsumers(1);//two consumer(thread will listen at the same time to this queues).
		return container;
	}

}
