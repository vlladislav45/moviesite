package com.filmi3k.movies.services.base;

import com.filmi3k.movies.domain.entities.User;
import com.filmi3k.movies.domain.models.binding.RequestAuthorFormBindingModel;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;
import java.util.List;

public interface EmailService {
    void sendSimpleMessage(RequestAuthorFormBindingModel requestAuthorFormModel) throws SendFailedException;

    List<String> requires(RequestAuthorFormBindingModel requestAuthorFormModel);

    void constructResetTokenEmail(String contextPath, String token, User user) throws MessagingException;

    MimeMessage constructEmail(String subject, String body, User user) throws MessagingException;
}
