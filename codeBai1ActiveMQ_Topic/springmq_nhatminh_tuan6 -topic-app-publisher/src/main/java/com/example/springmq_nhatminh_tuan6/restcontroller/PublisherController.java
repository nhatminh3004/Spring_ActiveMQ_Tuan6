package com.example.springmq_nhatminh_tuan6.restcontroller;


import com.example.springmq_nhatminh_tuan6.data.Person;
import com.example.springmq_nhatminh_tuan6.helpper.XMLConvert;
import org.apache.log4j.BasicConfigurator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.Properties;

@RestController
public class PublisherController {
    @PostMapping("/sendToSubcriber")
    public String sendMessage(@PathParam("tinnhan") String tinnhan) throws NamingException {
        try {
            // thiết lập môi trường cho JMS logging
            BasicConfigurator.configure();
            // thiết lập môi trường cho JJNDI
            Properties settings = new Properties();
            settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            // tạo context
            Context ctx = new InitialContext(settings);
            // lookup JMS connection factory
            Object obj = ctx.lookup("TopicConnectionFactory");
            ConnectionFactory factory = (ConnectionFactory) obj;
            // tạo connection
            Connection con = factory.createConnection("admin", "admin");
            // nối đến MOM
            con.start();
            // tạo session
            Session session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);
            Destination destination = (Destination) ctx.lookup("dynamicTopics/lamnhatminh");
            // tạo producer
            MessageProducer producer = session.createProducer(destination);
            // Tạo 1 message

            Message msg = session.createTextMessage(tinnhan);
            // gửi
            producer.send(msg);

            // shutdown connection
            session.close();
            con.close();
            System.out.println("Finished...");

        } catch (Exception e2) {
            System.out.println(e2);
        }
        return "Sended";
    }
}
