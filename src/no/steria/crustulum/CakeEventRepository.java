package no.steria.crustulum;

import org.joda.time.DateMidnight;

public interface CakeEventRepository {

    DateMidnight getLastCakeEvent();

    void setLastCakeEvent(DateMidnight lastCakeEvent);

}
