package no.steria.crustulum;

import org.joda.time.DateMidnight;

public class BakerTeam {

    private static final DateMidnight LONG_PAST = new DateMidnight(-2000, 1, 1);
    private DateMidnight lastBaked;
    private final String teamName;
    private String teamEmail;

    public BakerTeam(String teamName, String teamEmail, DateMidnight lastBaked) {
        this.teamName = teamName;
        this.teamEmail = teamEmail;
        this.lastBaked = lastBaked;
    }

    public BakerTeam(String teamName, String teamEmail) {
        this(teamName, teamEmail, LONG_PAST);
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamEmail() {
        return teamEmail;
    }

    public void setTeamEmail(String teamEmail) {
        this.teamEmail = teamEmail;
    }

    public DateMidnight getLastBaked() {
        return lastBaked;
    }

    public void setLastBaked(DateMidnight lastBaked) {
        this.lastBaked = lastBaked;
    }

    @Override
    public String toString() {
        return "BakerTeam<" + getTeamName() + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BakerTeam)) return false;
        return nullSafeEquals(getTeamName(), ((BakerTeam)obj).getTeamName());
    }

    @Override
    public int hashCode() {
        return getTeamName() != null ? getTeamName().hashCode() : -1;
    }

    private<T> boolean nullSafeEquals(T a, T b) {
        return a != null ? a.equals(b) : b == null;
    }
}
