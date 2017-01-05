# SpringRabbitTopicExchange
Spring Rabbit topic exchange example where queue3 is excluded because different routing key in bindding .you can also add different topic also.
Here topic is my-exchange, you can add new one and my List<Topic> and you can bind diferent queue with different exchange.
here we have segregated with routing key (ru.interosite.*) and (excluded.*).
all messages are delivered to queue1 and queue2 because those queue are bound to that exchange and pattern are same.
if you publish using 
  rabbitTemplate.convertAndSend("my-exchange", "excluded.1", "ttt1233");
  then queue3 will going to receive all the message.
