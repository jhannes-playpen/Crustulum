package no.steria.crustulum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.joda.time.DateMidnight;
import org.joda.time.format.DateTimeFormat;

public class BakerRepository {

    private File file;

    public BakerRepository(File file) {
        this.file = file;
    }

    public void addTeam(BakerTeam bakerTeam) {
        Collection<BakerTeam> allTeams = getAllTeams();
        allTeams.add(bakerTeam);
        writeTeams(allTeams);
    }

    public Collection<BakerTeam> getAllTeams() {
        Collection<BakerTeam> result = new ArrayList<BakerTeam>();
        if (!file.exists()) return result;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                result.add(readTeamLine(line));
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
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
        writeTeams(allTeams);
    }

    private void writeTeams(Collection<BakerTeam> allTeams) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (BakerTeam team : allTeams) {
                writer.write(writeLine(team));
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BakerTeam readTeamLine(String teamLine) {
        String[] teamValues = teamLine.split(";");
        String teamName = teamValues[0].trim();
        String teamEmail = teamValues[1].trim();
        DateMidnight lastBaked = DateTimeFormat.forPattern("yyyy/MM/dd").parseDateTime(teamValues[2].trim()).toDateMidnight();
        return new BakerTeam(teamName, teamEmail, lastBaked);
    }

    public String writeLine(BakerTeam team) {
        String lastBakedTime = DateTimeFormat.forPattern("yyyy/MM/dd").print(team.getLastBaked());
        return team.getTeamName() + ";" + team.getTeamEmail() + ";" + lastBakedTime;
    }

}
