package init;

import mqtt.MqttClientManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitializeListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("On start web app");

        ServletContext ctx = sce.getServletContext();

        String broker = ctx.getInitParameter("BROKER");
        String topic = ctx.getInitParameter("TOPIC");
        String clientId = ctx.getInitParameter("CLIENTID");
        int qos = 2;
        try {
            qos = Integer.parseInt(ctx.getInitParameter("QOS"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        MqttClientManager mqttClientManager = new MqttClientManager(broker, topic, clientId, qos);
        mqttClientManager.initClient();

        ctx.setAttribute("mqttClientManager", mqttClientManager);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("On shutdown web app");

        ServletContext ctx = sce.getServletContext();
        MqttClientManager mqttPublishSample = (MqttClientManager) ctx.getAttribute("mqttClientManager");
        mqttPublishSample.disconnectClient();
    }
}