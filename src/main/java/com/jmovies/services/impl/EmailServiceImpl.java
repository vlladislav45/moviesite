package com.jmovies.services.impl;

import com.jmovies.domain.entities.User;
import com.jmovies.domain.models.binding.RequestAuthorFormBindingModel;
import com.jmovies.services.base.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import java.util.*;

import static com.jmovies.config.Config.companyEmail;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * These methods are for request author form (for problems with the authors)
     * @param requestAuthorFormModel
     */
    @Override
    public void sendSimpleMessage(RequestAuthorFormBindingModel requestAuthorFormModel) throws SendFailedException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(requestAuthorFormModel.getEmail());
        message.setTo(companyEmail);
        message.setSubject(requestAuthorFormModel.getSubject());
        message.setText(requestAuthorFormModel.getMessage() + "\n\n" + " Sender name " + requestAuthorFormModel.getFullName());
        emailSender.send(message);
    }

    @Override
    public List<String> requires(RequestAuthorFormBindingModel requestAuthorFormModel) {
        List<String> errors = new ArrayList<>();
        if(requestAuthorFormModel.getFullName().isEmpty()) {
            String errFullName = "* Full name is required";
            errors.add(errFullName);
        }
        if(requestAuthorFormModel.getEmail().isEmpty()) {
            String errFullName = "* Email is required";
            errors.add(errFullName);
        }
        if(requestAuthorFormModel.getSubject().isEmpty()) {
            String errFullName = "* Subject is required";
            errors.add(errFullName);
        }
        if(requestAuthorFormModel.getMessage().isEmpty()) {
            String errFullName = "* Message is required";
            errors.add(errFullName);
        }
        return errors;
    }

    /**
     * These methods are for reset password
     */
    @Override
    public void constructResetTokenEmail(String domain, String token, User user) throws MessagingException {
        String html = "<h2>Hello!</h2>\n\n" +
                "<p>You are receiving this email because we received a password reset request for your account.</p>\n";
        String http = "http://";
        html += "<a href=\"" + http + domain + "/user/security/changePassword?token=" + token + "\">" + "Reset Password" + "</a>\n\n";
        html += "Regards OmegaTwentyOne";
        emailSender.send(constructEmail("Reset Password", html, user));
    }

    @Override
    public MimeMessage constructEmail(String subject, String body, User user) throws MessagingException {
        //MimeMessage for HTML email
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        mimeMessage.setSubject(subject);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        mimeMessage.setText(body, "UTF-8", "html");
        helper.setTo(user.getEmail());

        return mimeMessage;
    }
}
