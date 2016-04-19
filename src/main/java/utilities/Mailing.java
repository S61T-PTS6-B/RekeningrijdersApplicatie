/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
/**
 *
 * @author Gijs
 */
public class Mailing {
    
    public static void SendEmail(String dest, String text, String subject) {
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "localhost");
        props.setProperty("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom("overheid@rekeningrijden.nl");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
            message.setSubject(subject);
            message.setText(text);
            message.setHeader("Content-Type", "text/html");
            Transport.send(message);
        } catch (MessagingException ex) {
            System.out.println("Email not sent: " + ex.toString());
        }
    }
}
