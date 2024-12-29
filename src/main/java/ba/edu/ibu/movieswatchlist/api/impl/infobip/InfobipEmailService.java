package ba.edu.ibu.movieswatchlist.api.impl.infobip;

import ba.edu.ibu.movieswatchlist.core.api.emailsender.EmailService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class InfobipEmailService implements EmailService {

    @Value("${infobip.api.key}")
    private String apiKey;

    @Value("${infobip.sender.email}")
    private String senderEmail;

    @Value("${infobip.api.url}")
    private String sendingUrl;

    @Override
    public void sendEmail(String to, String subject, String body) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("from", "Ajla's Email Server <" + senderEmail + ">")
                .addFormDataPart("subject", subject)
                .addFormDataPart("to", to)
                .addFormDataPart("text", body)
                .build();

        Request request = new Request.Builder()
                .url(sendingUrl)
                .post(requestBody)
                .addHeader("Authorization", "App " + apiKey)
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Email sent successfully to: " + to);
            } else {
                System.err.println("Failed to send email. Response: " + response.body().string());
            }
        } catch (IOException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }


}
