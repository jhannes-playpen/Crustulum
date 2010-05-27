package no.steria.crustulum;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.joda.time.DateMidnight;
import org.junit.After;
import org.junit.Test;

public class BakerRepositoryTest {

    private static int index;
    private File file = new File("bin/baker-" + index++ +".csv");
    private BakerRepository bakerRepository = new BakerRepository(file);

    @After
    public void removeTeamFile() {
        file.delete();
    }

    @Test
    public void shouldSaveBakerTeam() throws Exception {
        BakerTeam team = new BakerTeam("A", "jhannes+a@gmail.com", new DateMidnight(2010, 3, 1));
        bakerRepository.addTeam(team);

        assertThat(bakerRepository.getAllTeams()).contains(team);
        assertThat(bakerRepository.getTeamByName(team.getTeamName())).isEqualTo(team);
    }

    @Test
    public void shouldSaveSeveralTeams() throws Exception {
        BakerTeam teamA = new BakerTeam("A", "jhannes+a@gmail.com", new DateMidnight(2010, 3, 1));
        BakerTeam teamB = new BakerTeam("B", "jhannes+b@gmail.com", new DateMidnight(2010, 4, 1));
        BakerTeam teamC = new BakerTeam("C", "jhannes+c@gmail.com", new DateMidnight(2010, 5, 1));

        bakerRepository.addTeam(teamA); bakerRepository.addTeam(teamB); bakerRepository.addTeam(teamC);

        assertThat(bakerRepository.getAllTeams()).hasSize(3)
            .contains(teamA, teamB, teamC);
    }

    @Test
    public void shouldUpdateBakerTeam() throws Exception {
        BakerTeam teamA = new BakerTeam("A", "jhannes+a@gmail.com", new DateMidnight(2010, 3, 1));
        BakerTeam teamB = new BakerTeam("B", "jhannes+b@gmail.com", new DateMidnight(2010, 4, 1));

        bakerRepository.addTeam(teamA); bakerRepository.addTeam(teamB);
        teamA.setLastBaked(new DateMidnight(2011, 1, 1));
        teamA.setTeamEmail("jhannes+c@gmail.com");
        bakerRepository.updateTeam(teamA);

        assertThat(bakerRepository.getAllTeams()).containsOnly(teamA, teamB).hasSize(2);
        assertThat(bakerRepository.getTeamByName("A").getLastBaked()).isEqualTo(new DateMidnight(2011, 1, 1));
        assertThat(bakerRepository.getTeamByName("A").getTeamEmail()).isEqualTo("jhannes+c@gmail.com");

        assertThat(bakerRepository.getTeamByName("B").getTeamEmail()).isEqualTo("jhannes+b@gmail.com");
    }

    @Test
    public void shouldReadTeamLine() throws Exception {
        BakerTeam team = bakerRepository.readLine("team a; jhannes+a@gmail.com, jhannes+b@gmail.com  ;2010/11/20");
        assertThat(team.getTeamName()).isEqualTo("team a");
        assertThat(team.getTeamEmail()).isEqualTo("jhannes+a@gmail.com, jhannes+b@gmail.com");
        assertThat(team.getLastBaked()).isEqualTo(new DateMidnight(2010, 11, 20));
    }

    @Test
    public void shouldWriteTeamLine() throws Exception {
        BakerTeam team = new BakerTeam("team a", "jhannes+b@gmail.com, jhannes+c@gmail.com", new DateMidnight(2010, 1, 13));
        String teamLine = bakerRepository.writeItem(team);

        BakerTeam reconstitutedTeam = bakerRepository.readLine(teamLine);
        assertThat(team.getTeamName()).isEqualTo(reconstitutedTeam.getTeamName());
        assertThat(team.getTeamEmail()).isEqualTo(reconstitutedTeam.getTeamEmail());
        assertThat(team.getLastBaked()).isEqualTo(reconstitutedTeam.getLastBaked());
    }
}
