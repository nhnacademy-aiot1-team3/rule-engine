package live.databo3.ruleengine.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.databo3.ruleengine.rabbitmq.node.input.RabbitMQListenerNode;
import live.databo3.ruleengine.rabbitmq.node.output.InfluxDBOutNode;

import java.io.IOException;
import java.util.Properties;

public class Main {
    public static final Properties properties = new Properties();

    private static void init() {
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        init();
        RabbitMQListenerNode rabbitMQListenerNode = new RabbitMQListenerNode();
        rabbitMQListenerNode.process();

        InfluxDBOutNode influxDBOutNode = new InfluxDBOutNode();

        rabbitMQListenerNode.subscribe(influxDBOutNode);
    }
}
