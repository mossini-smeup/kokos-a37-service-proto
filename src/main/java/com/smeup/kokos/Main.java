package com.smeup.kokos;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.smeup.kokos.consumer.EventConsumer;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class Main {
    private static final String SMEUP_EXCHANGE = "smeupEvtExchange";

    public static void main(String[] args) throws Throwable {


        Map<String, EventConsumer> consumerList = new HashMap<>();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("localhost");
        factory.setPort(5672);
        Connection connection = factory.newConnection();

        Thread thread = new Thread(() -> {

            while (true) {
                try {
                    Set<String> fileNames = getConfiFileNames("C:\\Users\\paolo.mossini\\Desktop\\A37Config");

                    //Check if a consumer is active, but his config is missing -> stop consumer
                    Iterator<String> consumerIter = consumerList.keySet().iterator();
                    while (consumerIter.hasNext()) {
                        String consumer = consumerIter.next();
                        if (!fileNames.contains(consumer)) {
                            consumerList.get(consumer).getChannel().basicCancel(consumer);
                            consumerList.get(consumer).getChannel().close();
                            consumerIter.remove();
                        }
                    }
                    // Check if a config file doesn't have an active consumer -> create consumer
                    for (String filename : fileNames) {
                        if (!consumerList.containsKey(filename)) {
                            Channel channel = connection.createChannel();
                            channel.exchangeDeclare(SMEUP_EXCHANGE, "topic", true);
                            channel.queueDeclare(filename, true, false, false, null);
                            channel.queueBind(filename, SMEUP_EXCHANGE, filename + ".in");
                            EventConsumer consumer = new EventConsumer(channel);
                            channel.basicConsume(filename, false, filename, consumer);
                            consumerList.put(filename, consumer);
                        }
                    }
                    Thread.sleep(10000);

                } catch (IOException | InterruptedException |
                         TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();

    }


    public static Set<String> getConfiFileNames(String dir) {
        Set<String> fileNames = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path entry : stream) {
                if (entry.getFileName().toString().endsWith(".yaml"))
                    fileNames.add(entry.getFileName().toString().replace(".yaml", ""));
            }
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
        return fileNames;
    }
}
