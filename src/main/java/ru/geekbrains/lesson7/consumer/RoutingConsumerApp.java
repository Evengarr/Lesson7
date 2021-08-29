package ru.geekbrains.lesson7.consumer;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class RoutingConsumerApp {
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();

        System.out.println("Введите интересующий язык для подписки на рассылку");
        System.out.println("Используйте команду [set_topic] [пробел] [название языка] (ПРИМЕР: set_topic php)");

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        String[] language = command.split(" ");

        System.out.println("Вы подписаны на расслыку по " + language[1]);

        String routingKey = "some message." + language[1] + ".#";
        channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("получена рассылка по '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}
