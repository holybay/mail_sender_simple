package by.it_academy.jd2.golubev_107.mail_sender_simple.service.impl;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.CreateEmailDto;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.EmailOutDto;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.EStatus;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.Email;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class MailService implements IMailService {

    private final IMailStorage mailStorage;

    public MailService(IMailStorage mailStorage) {
        this.mailStorage = mailStorage;
    }

    @Override
    public void create(CreateEmailDto dto) {
        validate(dto);
        Email emailEntity = toEmailEntity(dto);
        mailStorage.create(emailEntity);
    }

    @Override
    public EmailOutDto getById(UUID id) {
        Email email = mailStorage.readById(id);
        if (email == null) {
            throw new NoSuchElementException("There is no email with id: " + id);
        }
        return toEmailOutDto(email);
    }

    @Override
    public List<EmailOutDto> getAll() {
        List<Email> allEmails = mailStorage.readAll();
        return allEmails.stream()
                        .map(this::toEmailOutDto)
                        .toList();
    }

    @Override
    public void updateStatus(UUID id, EStatus newStatus) {
        Email email = mailStorage.readById(id);
        email.setEmailStatus(newStatus);
        email.setUpdatedAt(LocalDateTime.now());
        mailStorage.update(email);
    }

    @Override
    public List<EmailOutDto> getAllByStatus(EStatus status) {
        List<Email> allEmails = mailStorage.readAllByStatus(status);
        return allEmails.stream()
                        .map(this::toEmailOutDto)
                        .toList();
    }

    private void validate(CreateEmailDto dto) {
        if (dto.getRecipientTo() == null) {
            throw new IllegalArgumentException("Didn't receive email \"TO\" recipient param!");
        }
    }


    private Email toEmailEntity(CreateEmailDto dto) {
        LocalDateTime timeCreated = LocalDateTime.now();
        return Email.builder()
                    .setId(UUID.randomUUID())
                    .setRecipientTo(dto.getRecipientTo())
                    .setTitle(dto.getTitle())
                    .setText(dto.getText())
                    .setEmailStatus(EStatus.LOADED)
                    .setCreatedAt(timeCreated)
                    .setUpdatedAt(timeCreated)
                    .build();
    }

    private EmailOutDto toEmailOutDto(Email email) {
        return EmailOutDto.builder()
                          .setId(email.getId())
                          .setRecipientTo(email.getRecipientTo())
                          .setTitle(email.getTitle())
                          .setText(email.getText())
                          .setEmailStatus(email.getEmailStatus())
                          .setCreatedAt(email.getCreatedAt())
                          .setUpdatedAt(email.getUpdatedAt())
                          .build();
    }
}
