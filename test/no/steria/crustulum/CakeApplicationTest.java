package no.steria.crustulum;

import static no.steria.crustulum.Weekdays.FRIDAY;
import static no.steria.crustulum.Weekdays.WEDNESDAY;
import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import javax.mail.Message;
import javax.mail.Message.RecipientType;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class CakeApplicationTest {

    private File bakerFile = new File("bin/bakers-" + getClass().getName() + ".csv");
    private File vacationFile = new File("bin/vacations-" + getClass().getName() + ".txt");
    private File eventFile = new File("bin/events-" + getClass().getName() + ".txt");

    @Before
    public void deletePreviousRuns() {
        bakerFile.delete();
        vacationFile.delete();
        eventFile.delete();
    }

    @Test
    public void shouldNotifyNextBakerTeam() throws Exception {
        DateMidnight today = new DateMidnight(2010, 1, 2).withDayOfWeek(WEDNESDAY);

        CakeApplication cakeApplication = new CakeApplication();

        BakerRepository bakerRepository = new BakerRepository(bakerFile);
        BakerTeam upcomingTeam = new BakerTeam("The Next Team", "jhannes+test1@gmail.com");
        bakerRepository.addTeam(new BakerTeam("Team Bravo", "jhannes+test4@gmail.com", new DateMidnight(2009, 10, 1)));
        bakerRepository.addTeam(upcomingTeam);
        bakerRepository.addTeam(new BakerTeam("The Old Team", "jhannes+test3@gmail.com", new DateMidnight(2008, 10, 1)));
        cakeApplication.setBakerRepository(bakerRepository);

        VacationRepository vacationRepository = new VacationRepository(vacationFile);
        vacationRepository.addVacationWeek(today.minusWeeks(1).withDayOfWeek(FRIDAY));
        cakeApplication.setVacationRepository(vacationRepository);

        CakeEventRepository eventRepository = new CakeEventRepository(eventFile);
        eventRepository.setLastCakeEvent(today.minusWeeks(2).withDayOfWeek(FRIDAY));
        cakeApplication.setEventRepository(eventRepository);

        FakeEmailSender emailSender = new FakeEmailSender();
        cakeApplication.setEmailSender(emailSender);

        DateTime eventNotificationTime = new DateTime(today).withHourOfDay(14).withMinuteOfHour(0);
        cakeApplication.run(eventNotificationTime);


        DateMidnight nextCakeEvent = today.withDayOfWeek(FRIDAY);

        assertThat(eventRepository.getLastCakeEvent()).isEqualTo(nextCakeEvent);

        Message sentMessage = emailSender.getMessage();
        assertThat(sentMessage.getRecipients(RecipientType.TO)[0].toString()).isEqualTo("jhannes+test1@gmail.com");
        assertThat(sentMessage.getSubject()).contains(nextCakeEvent.toString());

        BakerTeam selectedTeam = bakerRepository.getTeamByName(upcomingTeam.getTeamName());
        assertThat(selectedTeam.getLastBaked()).isEqualTo(nextCakeEvent);


        emailSender = new FakeEmailSender();
        cakeApplication.setEmailSender(emailSender);
        cakeApplication.run(eventNotificationTime);
        assertThat(emailSender.getMessage()).isNull();
    }

}
