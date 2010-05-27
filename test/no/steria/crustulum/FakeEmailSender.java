package no.steria.crustulum;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

public class FakeEmailSender extends EmailSender {

    private MimeMessage message;


    @Override
    public void sendMessage(MimeMessage message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

}
