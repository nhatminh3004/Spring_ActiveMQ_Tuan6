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
public class ReviceController {
     String tinnhan="";
    @GetMapping("/getMesssToMQ")
    public String getMessToMq() throws NamingException {
        try {
            //thiết lập môi trường cho JMS
            BasicConfigurator.configure();
            //thiết lập môi trường cho JJNDI
            Properties settings=new Properties();
            settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                    "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
            //tạo context
            Context ctx=new InitialContext(settings);
            //lookup JMS connection factory
            Object obj=ctx.lookup("ConnectionFactory");
            ConnectionFactory factory=(ConnectionFactory)obj;
            //lookup destination
            Destination destination
                    =(Destination) ctx.lookup("dynamicQueues/lamnhatminh");
            //tạo connection
            Connection con=factory.createConnection("admin","admin");
            //nối đến MOM
            con.start();
            //tạo session
            Session session=con.createSession(
                    /*transaction*/false,
                    /*ACK*/Session.CLIENT_ACKNOWLEDGE
            );
            //tạo consumer
            MessageConsumer receiver = session.createConsumer(destination);
            //blocked-method for receiving message - sync
            //receiver.receive();
            //Cho receiver lắng nghe trên queue, chừng có message thì notify - async
            System.out.println("Minh was listened on queue...");
            receiver.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    try {
                        if(msg instanceof TextMessage){
                            TextMessage tm=(TextMessage)msg;
                            String txt=tm.getText();
                            tinnhan="Nhận được tin nhắn : "+txt;
                            System.out.println("nội dung chính :"+tinnhan);
                            System.out.println("Nhận được "+txt);
                            msg.acknowledge();//gửi tín hiệu ack
                        }
                        else if(msg instanceof ObjectMessage){
                            ObjectMessage om=(ObjectMessage)msg;
                            System.out.println(om);
                        }
                        //others message type....
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {

        }

        return tinnhan;
    }
}
