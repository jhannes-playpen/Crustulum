package no.steria.crustulum;

import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

public class EmailIntegrationTest {

    @Test
    public void emailingShouldNotThrow() throws Exception {
        EmailSender emailSender = new EmailSender();
        emailSender.setMailhost("mail.broadpark.no");

        MimeMessage message = emailSender.createMessage();
        message.addRecipients(RecipientType.TO, "jhannes@gmail.com");
        message.setSubject(getClass().getName());
        message.setText("");
        emailSender.sendMessage(message);
    }

}
