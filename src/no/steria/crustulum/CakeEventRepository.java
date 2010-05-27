package no.steria.crustulum;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateMidnight;

public class CakeEventRepository extends FileRepository<DateMidnight> {

    public CakeEventRepository(File file) {
        super(file);
    }

    DateMidnight getLastCakeEvent() {
        List<DateMidnight> fileContents = readItems();
        return fileContents.isEmpty() ? null : fileContents.get(0);
    }

    void setLastCakeEvent(DateMidnight lastCakeEvent) {
        super.writeContents(Arrays.asList(lastCakeEvent));
    }

    @Override
    protected DateMidnight readLine(String line) {
        return dateTimeFormat.parseDateTime(line).toDateMidnight();
    }

    @Override
    protected String writeItem(DateMidnight item) {
        return dateTimeFormat.print(item);
    }

}
