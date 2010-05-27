package no.steria.crustulum;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.format.DateTimeFormat;

public class CakeApplication extends TimerTask {

    private BakerRepository bakerRepository;
    private VacationRepository vacationRepository;
    private CakeEventRepository eventRepository;
    private EmailSender emailSender;

    public CakeApplication(File cakeDirectory, String mailhost) {
        bakerRepository = new BakerRepository(new File(cakeDirectory, "bakers.csv"));
        vacationRepository = new VacationRepository(new File(cakeDirectory, "vacations.txt"));
        eventRepository = new CakeEventRepository(new File(cakeDirectory, "event.txt"));
        emailSender = new EmailSender();
        emailSender.setMailhost(mailhost);
    }

    public CakeApplication() {
    }

    public void setBakerRepository(BakerRepository bakerRepository) {
        this.bakerRepository = bakerRepository;
    }

    public void setVacationRepository(VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    public void setEventRepository(CakeEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void run() {
        CakeApplicationRequest request = new CakeApplicationRequest();
        request.setBakerRepository(bakerRepository);
        request.setCakeEventRepository(eventRepository);
        request.setEmailSender(emailSender);

        request.setBakerTeams(bakerRepository.getAllTeams());
        request.setLastCakeEvent(eventRepository.getLastCakeEvent());
        request.setVacations(vacationRepository.getVacations());

        request.run(new DateTime());
    }

    public static void main(String[] args) throws IOException {
        String mailhost = System.getProperty("mail.smtp.host");

        if (System.getProperty("test.startTime")!=null) {
            DateTime startTime = DateTimeFormat.forPattern("yyyy/MM/ddTHH:mm").
                parseDateTime(System.getProperty("test.startTime"));
            DateTimeUtils.setCurrentMillisOffset(System.currentTimeMillis() -
                    startTime.getMillis());

            System.out.println("Simulating start time " + new DateTime());
        }

        File cakeDirectory = new File("data");
        cakeDirectory.mkdirs();

        if (System.getProperty("test.templateDir") != null) {
            File templateDir = new File(System.getProperty("test.templateDir"));
            for (File file : templateDir.listFiles()) {
                copyTo(file, cakeDirectory);
            }
            System.out.println("Replaced files in " + cakeDirectory + " with " + templateDir);
        }

        TimerTask application = new CakeApplication(cakeDirectory, mailhost);

        new Timer(false).schedule(application, 60*1000, 0);
    }

    private static void copyTo(File inputFile, File outputDir) throws IOException {
        File outputFile = new File(outputDir, inputFile.getName());
        if (outputFile.delete()) {
            System.out.println("Replaced " + outputFile);
        }

        FileReader in = new FileReader(inputFile);
        FileWriter out = new FileWriter(outputFile);
        int c;

        while ((c = in.read()) != -1)
          out.write(c);

        in.close();
        out.close();
    }

}
