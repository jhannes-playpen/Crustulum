package no.steria.crustulum;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateMidnight;

public class FakeVacationRepository implements VacationRepository {

    private List<DateMidnight> vacations = new ArrayList<DateMidnight>();

    @Override
    public void addVacationWeek(DateMidnight vacation) {
        vacations.add(vacation);
    }

    @Override
    public List<DateMidnight> getVacations() {
        return vacations;
    }
}
