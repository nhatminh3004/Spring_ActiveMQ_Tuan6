package com.example.springmq_nhatminh_tuan6.restcontroller;


import com.example.springmq_nhatminh_tuan6.data.Person;
import com.example.springmq_nhatminh_tuan6.helpper.XMLConvert;
import org.apache.log4j.BasicConfigurator;
import org.springframework.web.bind.annotation.GetMapping;
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
public class Surcriber1Controller {
    public String tinnhan = "";

    @GetMapping("/nhantinnhantuSub")
    public String recevierMessage() throws NamingException, JMSException {
        BasicConfigurator.configure();
        // thiết lập môi trường cho JJNDI
        Properties settings = new Properties();
        settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
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
        Session session = con.createSession(/* transaction */false, /* ACK */Session.CLIENT_ACKNOWLEDGE);
        // tạo consumer
        Destination destination = (Destination) ctx.lookup("dynamicTopics/lamnhatminh");
        MessageConsumer receiver = session.createConsumer(destination);
        // receiver.receive();//blocked method
        // Cho receiver lắng nghe trên queue, chừng có message thì notify
        receiver.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage tm = (TextMessage) message;
                        String txt = tm.getText();
                        tinnhan = "Tin nhắn nhận được từ Publisher:" + txt;
                        System.out.println(tinnhan);

                        message.acknowledge();// gửi tín hiệu ack
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return tinnhan;
    }
    @GetMapping("/dangkyPublisher")
    public String dangkyMessage() throws NamingException, JMSException {
        BasicConfigurator.configure();
        // thiết lập môi trường cho JJNDI
        Properties settings = new Properties();
        settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
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
        Session session = con.createSession(/* transaction */false, /* ACK */Session.CLIENT_ACKNOWLEDGE);
        // tạo consumer
        Destination destination = (Destination) ctx.lookup("dynamicTopics/lamnhatminh");
        MessageConsumer receiver = session.createConsumer(destination);
        // receiver.receive();//blocked method
        // Cho receiver lắng nghe trên queue, chừng có message thì notify
        receiver.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage tm = (TextMessage) message;
                        String txt = tm.getText();
                        tinnhan = "Tin nhắn nhận được từ Publisher:" + txt;


                        message.acknowledge();// gửi tín hiệu ack
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return "Đăng ký thành công";
    }
}
