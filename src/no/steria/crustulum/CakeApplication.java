package no.steria.crustulum;

import org.joda.time.DateTime;

public class CakeApplication {

    private BakerRepository bakerRepository;
    private VacationRepository vacationRepository;
    private CakeEventRepository eventRepository;
    private EmailSender emailSender;

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

    public void run(DateTime now) {
        CakeApplicationRequest request = new CakeApplicationRequest();
        request.setBakerRepository(bakerRepository);
        request.setCakeEventRepository(eventRepository);
        request.setEmailSender(emailSender);

        request.setBakerTeams(bakerRepository.getAllTeams());
        request.setLastCakeEvent(eventRepository.getLastCakeEvent());
        request.setVacations(vacationRepository.getVacations());

        request.run(now);
    }

}
