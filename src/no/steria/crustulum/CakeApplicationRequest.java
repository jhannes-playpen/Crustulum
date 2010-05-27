package no.steria.crustulum;

import static no.steria.crustulum.Weekdays.FRIDAY;
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

    public DateMidnight getNextCakeEvent(DateMidnight today) {
        DateMidnight nextCakeEvent = null;
        if (lastCakeEvent != null) {
            nextCakeEvent = nextFridayAfter(lastCakeEvent);
        }

        if (nextCakeEvent == null || nextCakeEvent.isBefore(today)) {
            nextCakeEvent = nextFridayAfter(today);
        }

        while (vacations.contains(nextCakeEvent)) {
            nextCakeEvent = nextCakeEvent.plusWeeks(1);
        }
        return nextCakeEvent;
    }

    public void setVacations(List<DateMidnight> vacations) {
        this.vacations = vacations;
    }

    public DateTime getCakeNotificationTime(DateMidnight today) {
        return new DateTime(getNextCakeEvent(today).withDayOfWeek(WEDNESDAY)).withHourOfDay(14);
    }

    public void setBakerTeams(Collection<BakerTeam> bakerTeams) {
        this.bakerTeams = bakerTeams;
    }

    public BakerTeam getNextBakerTeam() {
        if (bakerTeams.isEmpty()) return null;

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
        if (nextTeam != null) {
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
            bakerRepository.updateTeam(nextTeam);
        }
        cakeEventRepository.setLastCakeEvent(nextCakeEvent);
    }

    public void setBakerRepository(BakerRepository bakerRepository) {
        this.bakerRepository = bakerRepository;
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void run(DateTime now) {
        if (!now.isBefore(getCakeNotificationTime(now.toDateMidnight()))) {
            notifyNextTeam(getNextBakerTeam(), getNextCakeEvent(now.toDateMidnight()));
        }
    }

    public DateMidnight nextFridayAfter(DateMidnight today) {
        return today.getDayOfWeek() < FRIDAY
            ? today.withDayOfWeek(FRIDAY)
            : today.plusWeeks(1).withDayOfWeek(FRIDAY);

    }

}
