package no.steria.crustulum;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.joda.time.DateMidnight;
import org.junit.After;
import org.junit.Test;

public class VacationRepositoryTest {

    private static int index;
    private File file = new File("bin/vacations-" + index++ +".csv");
    private VacationRepository vacationRepository = new VacationRepository(file);

    @Test
    public void shouldFindSavedVacations() throws Exception {
        DateMidnight vacation1 = new DateMidnight(2010, 11, 10);
        DateMidnight vacation2 = new DateMidnight(2010, 12, 10);
        vacationRepository.addVacationWeek(vacation1);
        vacationRepository.addVacationWeek(vacation2);

        assertThat(vacationRepository.getVacations())
            .containsOnly(vacation1, vacation2);
    }


    @After
    public void removeTeamFile() {
        file.delete();
    }
}
