package com.inn.cafe.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class EmailUtils {

    @Autowired
    private JavaMailSender emailSender;


    /**
     * 1. `to`: The recipient's email address.
     * 2. `subject`: The subject line of the email.
     * 3. `text`: The body content of the email (text).
     * 4. `list`: A list of attachments to be included in the email. *
     */
    public void sendSimpleMessage(String to, String subject, String text, List<String> list) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kattyboy785@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        if (list != null && list.size() > 0)
            message.setCc(getCcArray(list));
        emailSender.send(message);
    }

    private String[] getCcArray(List<String> ccList) {
        String[] cc = new String[ccList.size()];
        for (int i = 0; i < ccList.size(); i++) {
            cc[i] = ccList.get(i);
        }
        return cc;
    }

/** The `forgotMail` method is used to send an email containing the user's login credentials (email and password) when they request a password reset, ensuring they can log in again. */
    public void forgotMail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("kattyboy785@gmail.com");
        helper.setTo(to);
        message.setSubject(subject);
        String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg, "text/html");
        emailSender.send(message);
    }
}
