package no.steria.crustulum;

import static no.steria.crustulum.Weekdays.WEDNESDAY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

public class CakeApplicationRequest {

    private DateMidnight lastCakeEvent;
    private List<DateMidnight> vacations = new ArrayList<DateMidnight>();
    private Collection<BakerTeam> bakerTeams;
    private CakeEventRepository cakeEventRepository;
    private BakerRepository bakerRepository;
    private EmailSender emailSender;

    public void setLastCakeEvent(DateMidnight lastCakeEvent) {
        this.lastCakeEvent = lastCakeEvent;
    }

    public DateMidnight getNextCakeEvent() {
        DateMidnight nextCakeEvent = lastCakeEvent.plusWeeks(1);
        while (vacations.contains(nextCakeEvent)) {
            nextCakeEvent = nextCakeEvent.plusWeeks(1);
        }
        return nextCakeEvent;
    }

    public void setVacations(List<DateMidnight> vacations) {
        this.vacations = vacations;
    }

    public DateTime getCakeNotificationTime() {
        return new DateTime(getNextCakeEvent().withDayOfWeek(WEDNESDAY)).withHourOfDay(14);
    }

    public void setBakerTeams(Collection<BakerTeam> bakerTeams) {
        this.bakerTeams = bakerTeams;
    }

    public BakerTeam getNextBakerTeam() {
        BakerTeam nextTeam = bakerTeams.iterator().next();
        for (BakerTeam team : bakerTeams) {
            if (team.getLastBaked().isBefore(nextTeam.getLastBaked())) {
                nextTeam = team;
            }
        }
        return nextTeam;
    }

    public void setCakeEventRepository(CakeEventRepository cakeEventRepository) {
        this.cakeEventRepository = cakeEventRepository;
    }

    public void notifyNextTeam(BakerTeam nextTeam, DateMidnight nextCakeEvent) {
        MimeMessage message = emailSender.createMessage();
        try {
            message.setRecipients(RecipientType.TO, nextTeam.getTeamEmail());
            message.setSubject("Get ready to bake a cake on " + nextCakeEvent);
            message.setText("");
        } catch (MessagingException e) {
            throw new RuntimeException("Illegal team email for " + nextTeam);
        }
        emailSender.sendMessage(message);

        nextTeam.setLastBaked(nextCakeEvent);
        cakeEventRepository.setLastCakeEvent(nextCakeEvent);
        bakerRepository.updateTeam(nextTeam);
    }

    public void setBakerRepository(BakerRepository bakerRepository) {
        this.bakerRepository = bakerRepository;
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void run(DateTime now) {
        if (!now.isBefore(getCakeNotificationTime())) {
            notifyNextTeam(getNextBakerTeam(), getNextCakeEvent());
        }
    }

}
