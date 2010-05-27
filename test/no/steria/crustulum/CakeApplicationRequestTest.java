package no.steria.crustulum;

import static no.steria.crustulum.Weekdays.FRIDAY;
import static no.steria.crustulum.Weekdays.THURSDAY;
import static no.steria.crustulum.Weekdays.WEDNESDAY;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class CakeApplicationRequestTest {

    private DateMidnight today = new DateMidnight(2010, 4, 22).withDayOfWeek(THURSDAY);
    private CakeApplicationRequest request = new CakeApplicationRequest();

    @Test
    public void shouldFindFirstCakeEvent() throws Exception {
        request.setLastCakeEvent(null);
        assertThat(request.getNextCakeEvent(today))
            .isEqualTo(today.withDayOfWeek(FRIDAY));
    }

    @Test
    public void shouldFindFirstCakeEventWhenStartedAfterFriday() throws Exception {
        request.setLastCakeEvent(null);
        assertThat(request.getNextCakeEvent(today.withDayOfWeek(FRIDAY)))
            .isEqualTo(today.plusWeeks(1).withDayOfWeek(FRIDAY));
    }

    @Test
    public void shouldFindNextCakeEventWhenNoVacations() throws Exception {
        request.setLastCakeEvent(today.minusWeeks(1).withDayOfWeek(FRIDAY));
        assertThat(request.getNextCakeEvent(today))
            .isEqualTo(today.withDayOfWeek(FRIDAY));
    }

    @Test
    public void shouldSkipVacationDays() throws Exception {
        request.setLastCakeEvent(today.minusWeeks(1).withDayOfWeek(FRIDAY));
        request.setVacations(Arrays.asList(today.withDayOfWeek(FRIDAY), today.withDayOfWeek(FRIDAY).plusWeeks(1)));

        assertThat(request.getNextCakeEvent(today))
            .isEqualTo(today.plusWeeks(2).withDayOfWeek(FRIDAY));
    }

    @Test
    public void shouldFindNotificationTime() throws Exception {
        request.setLastCakeEvent(today.minusWeeks(1).withDayOfWeek(FRIDAY));
        assertThat(request.getCakeNotificationTime(today))
            .isEqualTo(new DateTime(today.withDayOfWeek(WEDNESDAY)).withHourOfDay(14).withMinuteOfHour(0));
    }

    @Test
    public void shouldPickTeamWhichHasWaitedLongest() throws Exception {
        request.setBakerTeams(Arrays.asList(
                new BakerTeam("A", "jhannes+a@gmail.com", new DateMidnight(2009, 1, 1)),
                new BakerTeam("B", "jhannes+b@gmail.com", new DateMidnight(2008, 1, 1)),
                new BakerTeam("C", "jhannes+c@gmail.com", new DateMidnight(2010, 1, 1))));

        assertThat(request.getNextBakerTeam().getTeamName()).isEqualTo("B");
    }

    @Test
    public void shouldPickNewTeam() throws Exception {
        request.setBakerTeams(Arrays.asList(
                new BakerTeam("A", "jhannes+a@gmail.com", new DateMidnight(2009, 1, 1)),
                new BakerTeam("B", "jhannes+b@gmail.com"),
                new BakerTeam("C", "jhannes+c@gmail.com", new DateMidnight(2010, 1, 1))));

        assertThat(request.getNextBakerTeam().getTeamName()).isEqualTo("B");
    }

    @Test
    public void shouldUpdateLastBakeTime() throws Exception {
        CakeEventRepository mockCakeEventRepository = mock(CakeEventRepository.class);
        request.setCakeEventRepository(mockCakeEventRepository);

        request.notifyNextTeam(new BakerTeam("A", "jhannes+a@gmail.com"), today);
        verify(mockCakeEventRepository).setLastCakeEvent(today);
    }

    @Test
    public void shouldUpdateBakerTeam() throws Exception {
        BakerRepository mockBakerRepository = mock(BakerRepository.class);
        request.setBakerRepository(mockBakerRepository);

        BakerTeam nextTeam = new BakerTeam("A", "jhannes+a@gmail.com");
        request.notifyNextTeam(nextTeam, today);

        verify(mockBakerRepository).updateTeam(nextTeam);
        assertThat(nextTeam.getLastBaked()).isEqualTo(today);
    }

    @Test
    public void shouldSendEmail() throws Exception {
        FakeEmailSender fakeEmailSender = new FakeEmailSender();
        request.setEmailSender(fakeEmailSender);

        BakerTeam nextTeam = new BakerTeam("A", "jhannes+a@gmail.com, jhannes+b@gmail.com");
        request.notifyNextTeam(nextTeam, today);

        assertThat(fakeEmailSender.getMessage()).isNotNull();
        assertThat(fakeEmailSender.getMessage().getRecipients(RecipientType.TO))
            .containsOnly(InternetAddress.parse("jhannes+a@gmail.com")[0], InternetAddress.parse("jhannes+b@gmail.com")[0]);
        assertThat(fakeEmailSender.getMessage().getSubject()).contains(today.toString());
        assertThat(fakeEmailSender.getMessage().getContent()).isNotNull();
    }

    @Before
    public void setDependencyStubs() {
        request.setCakeEventRepository(mock(CakeEventRepository.class));
        request.setBakerRepository(mock(BakerRepository.class));
        request.setEmailSender(new FakeEmailSender());
    }
}


