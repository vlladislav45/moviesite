package com.filmi3k.movies.services.impl;

import com.filmi3k.movies.domain.models.binding.RequestAuthorFormBindingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.SendFailedException;
import java.util.*;

import static com.filmi3k.movies.config.Config.companyEmail;

@Service
public class EmailServiceImpl {
    private final JavaMailSender emailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(RequestAuthorFormBindingModel requestAuthorFormModel) throws SendFailedException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(requestAuthorFormModel.getEmail());
        message.setTo(companyEmail);
        message.setSubject(requestAuthorFormModel.getSubject());
        message.setText(requestAuthorFormModel.getMessage() + "\n\n" + " Regards " + requestAuthorFormModel.getFullName());
        emailSender.send(message);
    }

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
}
