package by.it_academy.jd2.golubev_107.mail_sender_simple.storage;

import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.Email;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.EmailStatus;

import java.util.List;
import java.util.UUID;

public interface IMailStorage {

    void create(Email email);

    Email readById(UUID id);

    List<Email> readAll();

    List<Email> readAllByStatus(EmailStatus.EStatus newStatus);

    void update(Email email);
}
