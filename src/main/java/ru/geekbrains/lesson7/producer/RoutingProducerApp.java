package ru.geekbrains.lesson7.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class RoutingProducerApp {
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()){
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String message;
            switch ((int) ((Math.random()*6) + 1)) {
                case 1: message = "php";
                break;
                case 2: message = "java";
                break;
                case 3: message = "C++";
                break;
                case 4: message = "C#";
                break;
                case 5: message = "python";
                break;
                case 6: message = "JS";
                break;
                default:
                    throw new IllegalStateException("Unexpected value: " + (int) ((Math.random() * 6) + 1));
            }
            String routingKey = "some message." + message;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("Отправлена подписка по '" + message + "'");
        }
    }
}
