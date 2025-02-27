package com.smeup.kokos.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EventConsumer extends DefaultConsumer {
    public EventConsumer(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope,
                               AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("Message received (From "  + this.getConsumerTag()+ ") : " + new String(body, StandardCharsets.UTF_8));
        this.getChannel().basicAck(envelope.getDeliveryTag(), false);
    }

}
