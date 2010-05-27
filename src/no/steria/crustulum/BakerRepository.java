package no.steria.crustulum;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.joda.time.DateMidnight;

public class BakerRepository extends FileRepository<BakerTeam> {

    public BakerRepository(File file) {
        super(file);
    }

    public void addTeam(BakerTeam bakerTeam) {
        Collection<BakerTeam> allTeams = getAllTeams();
        allTeams.add(bakerTeam);
        writeContents(allTeams);
    }

    public Collection<BakerTeam> getAllTeams() {
        return readItems();
    }

    public BakerTeam getTeamByName(String teamName) {
        for (BakerTeam team : getAllTeams()) {
            if (team.getTeamName().equals(teamName)) return team;
        }
        return null;
    }

    public void updateTeam(BakerTeam bakerTeam) {
        Collection<BakerTeam> allTeams = getAllTeams();
        for (Iterator<BakerTeam> iterator = allTeams.iterator(); iterator.hasNext();) {
            BakerTeam team = iterator.next();
            if (team.getTeamName().equals(bakerTeam.getTeamName())) {
                iterator.remove();
                allTeams.add(bakerTeam);
                break;
            }
        }
        writeContents(allTeams);
    }

    @Override
    public BakerTeam readLine(String teamLine) {
        String[] teamValues = teamLine.split(";");
        String teamName = teamValues[0].trim();
        String teamEmail = teamValues[1].trim();
        DateMidnight lastBaked = dateTimeFormat.parseDateTime(teamValues[2].trim()).toDateMidnight();
        return new BakerTeam(teamName, teamEmail, lastBaked);
    }

    @Override
    public String writeItem(BakerTeam team) {
        String lastBakedTime = dateTimeFormat.print(team.getLastBaked());
        return team.getTeamName() + ";" + team.getTeamEmail() + ";" + lastBakedTime;
    }

}
