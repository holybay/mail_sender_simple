package by.it_academy.jd2.golubev_107.mail_sender_simple.service.impl;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.IMailSenderService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.config.MailSenderConfig;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.EmailOutDto;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class MailSenderService implements IMailSenderService {

    private final MailSenderConfig config;

    public MailSenderService(MailSenderConfig config) {
        this.config = config;
    }

    @Override
    public void send(EmailOutDto email) {
        Session mailSession = Session.getInstance(config.getMailProps(),
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getUser(), config.getPassword());
                    }
                });

        mailSession.setDebug(config.isDebugModeOn());
        try {
            Message message = new MimeMessage(mailSession);

            message.setFrom(new InternetAddress(config.getFrom()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getRecipientTo()));
            message.setSubject(email.getTitle());

            BodyPart mailBody = new MimeBodyPart();
            mailBody.setContent(email.getText(), "text/html; charset=utf-8");

            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(mailBody);

            message.setContent(multiPart);

            Transport.send(message);
        } catch (AddressException e) {
            throw new RuntimeException("Incorrect address" + e);
        } catch (AuthenticationFailedException e) {
            throw new RuntimeException("Incorrect login or password" + e);
        } catch (MessagingException e) {
            throw new RuntimeException("Something went wrong: " + e);
        }
    }
}
