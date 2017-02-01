package com.unum.microservices.drools.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource({ "classpath:rabbitmq.properties" })
public class RabbitMQConfig {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

	public static final String LEAVE_TRIGGER_MESSAGE_QUEUE = "UNUM.CLAIM.NOTIFICATION";

	@Autowired
	Environment env;
	
	@Bean
	public ConnectionFactory connectionFactory() {

		logger.debug("Environment value is:" + env);

		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(env.getProperty("qm.host"));
		connectionFactory.setUsername(env.getProperty("qm.user"));
		connectionFactory.setPassword(env.getProperty("qm.password"));
		return connectionFactory;
	}

	@Bean
	public Queue simpleQueue() {
		return new Queue(env.getProperty("com.unum.messaging.LEAVE_TRIGGER_MESSAGE_QUEUE"), true);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("trigger-queue-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange)
				.with(env.getProperty("com.unum.messaging.LEAVE_TRIGGER_MESSAGE_QUEUE"));
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory());
		template.setRoutingKey(env.getProperty("com.unum.messaging.LEAVE_TRIGGER_MESSAGE_QUEUE"));
		template.setMessageConverter(jsonMessageConverter());
		return template;
	}

	@Bean
	public SimpleMessageListenerContainer listenerContainer() {

		SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer();
		listenerContainer.setConnectionFactory(connectionFactory());
		listenerContainer.setQueues(simpleQueue());
		listenerContainer.setMessageConverter(jsonMessageConverter());
		listenerContainer.setAcknowledgeMode(AcknowledgeMode.NONE);
		return listenerContainer;
	}

}