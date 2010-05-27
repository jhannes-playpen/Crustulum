package no.steria.crustulum;

import java.io.File;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class VacationRepository extends FileRepository<DateMidnight> {

    public VacationRepository(File file) {
        super(file);
    }

    public void addVacationWeek(DateMidnight vacationDate) {
        List<DateMidnight> vacations = getVacations();
        vacations.add(vacationDate);
        writeContents(vacations);
    }

    public List<DateMidnight> getVacations() {
        return readItems();
    }

    private DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy/MM/dd");

    @Override
    protected String writeItem(DateMidnight item) {
        return dateTimeFormat.print(item);
    }

    @Override
    protected DateMidnight readLine(String line) {
        return dateTimeFormat.parseDateTime(line).toDateMidnight();
    }
}
