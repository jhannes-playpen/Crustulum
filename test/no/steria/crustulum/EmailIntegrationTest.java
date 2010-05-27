package no.steria.crustulum;

import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

public class EmailIntegrationTest {

    private String mailhost = System.getProperty("mail.smtp.host");
    private String toAddress = System.getProperty("mail.test.recipient-address");


    @Test
    public void emailingShouldNotThrow() throws Exception {
        if (mailhost == null || toAddress == null) {
            System.err.println(getClass().getName() + " skipped, set mail.smtp.host and mail.test.recipient-address properties to run");
            return;
        }

        EmailSender emailSender = new EmailSender();
        emailSender.setMailhost(mailhost);

        MimeMessage message = emailSender.createMessage();
        message.addRecipients(RecipientType.TO, toAddress);
        message.setSubject(getClass().getName());
        message.setText("");
        emailSender.sendMessage(message);
    }

}
