package ba.edu.ibu.movieswatchlist.core.api.emailsender;


public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
