package no.steria.crustulum;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    private Properties props = new Properties();



    public MimeMessage createMessage() {
        Session session = Session.getDefaultInstance(props);
        return new MimeMessage(session);
    }

    public void sendMessage(MimeMessage message) {
        try {
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setMailhost(String mailhost) {
        props.setProperty("mail.smtp.host", mailhost);
    }

}
