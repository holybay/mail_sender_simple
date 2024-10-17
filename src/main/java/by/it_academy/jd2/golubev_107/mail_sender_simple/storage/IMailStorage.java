package by.it_academy.jd2.golubev_107.mail_sender_simple.storage;

import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.EStatus;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.Email;

import java.util.List;
import java.util.UUID;

public interface IMailStorage {

    void create(Email email);

    Email readById(UUID id);

    List<Email> readAll();

    List<Email> readAllByStatus(EStatus newStatus);

    void update(Email email);
}
