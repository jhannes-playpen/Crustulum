package no.steria.crustulum;

import java.util.List;

import org.joda.time.DateMidnight;

public interface VacationRepository {

    void addVacationWeek(DateMidnight withDayOfWeek);

    List<DateMidnight> getVacations();

}
