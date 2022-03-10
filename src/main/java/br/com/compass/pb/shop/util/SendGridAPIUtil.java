package br.com.compass.pb.shop.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class SendGridAPIUtil {

    private static final String SENDER = "aryellesiqueira@hotmail.com";


    public static void sendEmail(String recipient, String subject, String message) {
        try {
            Email from = new Email(SENDER);
            Email to = new Email(recipient);
            Content content = new Content("text/plain", message);
            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
