package servlet;

import mqtt.MqttClientManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet("/sensor")
public class SensorPub extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Set response content type
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();

//        Enumeration paramNames = req.getParameterNames();
//
//        while(paramNames.hasMoreElements()) {
//            String paramName = (String)paramNames.nextElement();
//            out.print(paramName + ": ");
//
//            String paramValue = req.getParameter(paramName);
//            if (paramValue.length() == 0)
//                out.println("<i>No Value</i>");
//            else
//                out.println(paramValue);
//        }

        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        out.println(data);

        ServletContext ctx = getServletContext();
        MqttClientManager mqttClientManager = (MqttClientManager) ctx.getAttribute("mqttClientManager");
        mqttClientManager.publishSensorInfo(data);
    }

}
