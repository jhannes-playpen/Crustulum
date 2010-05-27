package no.steria.crustulum;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.joda.time.DateMidnight;
import org.junit.Test;

public class CakeEventRepositoryTest {

    private static int index;
    private File file = new File("bin/events-" + index++ +".txt");
    private CakeEventRepository repository = new CakeEventRepository(file);

    @Test
    public void shouldRetrieveSavedEvents() throws Exception {
        DateMidnight lastCakeEvent = new DateMidnight(2010, 12, 12);

        repository.setLastCakeEvent(lastCakeEvent.plusMonths(1));
        repository.setLastCakeEvent(lastCakeEvent.minusMonths(1));
        repository.setLastCakeEvent(lastCakeEvent);

        assertThat(repository.getLastCakeEvent()).isEqualTo(lastCakeEvent);
    }

    @Test
    public void shouldReturnNullWhenFileNotExists() throws Exception {
        assertThat(repository.getLastCakeEvent()).isNull();
    }

}
