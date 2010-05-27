package no.steria.crustulum;

import org.joda.time.DateMidnight;

public class FakeCakeEventRepository implements CakeEventRepository {

    private DateMidnight lastCakeEvent;

    @Override
    public DateMidnight getLastCakeEvent() {
        return lastCakeEvent;
    }

    @Override
    public void setLastCakeEvent(DateMidnight lastCakeEvent) {
        this.lastCakeEvent = lastCakeEvent;
    }

}
