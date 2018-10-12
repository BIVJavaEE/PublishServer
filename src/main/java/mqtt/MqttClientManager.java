package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientManager {

    private MqttClient sampleClient;

    private String broker;
    private String topic;
    private String clientId;
    private int qos;
    private MemoryPersistence persistence;

    public MqttClientManager(String broker, String topic, String clientId, int qos) {
        this.broker = broker;
        this.topic = topic;
        this.clientId = clientId;
        this.qos = qos;
        this.persistence = new MemoryPersistence();
    }

    public void initClient() {
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
        } catch (MqttException me) {
            printError(me);
        }
    }

    public void disconnectClient() {
        try {
            sampleClient.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException me) {
            printError(me);
        }
    }

    public void publishSensorInfo(String content) {
        try {
            System.out.println("Publishing message: " + content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
        } catch (MqttException me) {
            printError(me);
        }
    }

    private void printError(MqttException me) {
        System.out.println("reason " + me.getReasonCode());
        System.out.println("msg " + me.getMessage());
        System.out.println("loc " + me.getLocalizedMessage());
        System.out.println("cause " + me.getCause());
        System.out.println("excep " + me);
        me.printStackTrace();
    }

    @Override
    public String toString() {
        return this.broker + " " + this.topic + " " + this.qos;
    }
}
