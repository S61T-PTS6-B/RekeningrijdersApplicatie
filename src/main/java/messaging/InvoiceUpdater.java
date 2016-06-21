/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messaging;
import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Queue;
import javax.jms.*;
/**
 *
 * @author Max
 */
public class InvoiceUpdater {
    
    public InvoiceUpdater() {}
    
    public static boolean sendPackage(Long invoiceId, String type) throws JMSException {
        ConnectionFactory connFactory = new ConnectionFactory();
        connFactory.setProperty(ConnectionConfiguration.imqAddressList, "145.93.105.201:7676");
        Queue myQueue = new Queue("PaidQueue");
        try (Connection connection = connFactory.createConnection(); 
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); 
                MessageProducer producer = session.createProducer(myQueue)) {
            Message message = session.createTextMessage();
            message.setLongProperty("id", invoiceId);
            message.setStringProperty("type", type);
            producer.send(message);
        }
        catch (Exception ex) {
            System.out.println("Message not sent: " + ex.toString());
            return false;
        }
        return true;
    }
}