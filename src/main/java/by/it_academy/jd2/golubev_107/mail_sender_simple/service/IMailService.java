package by.it_academy.jd2.golubev_107.mail_sender_simple.service;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.CreateEmailDto;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.EmailOutDto;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.EStatus;

import java.util.List;
import java.util.UUID;

public interface IMailService {

    void create(CreateEmailDto dto);

    EmailOutDto getById(UUID id);

    List<EmailOutDto> getAll();

    void updateStatus(UUID id, EStatus newStatus);

    List<EmailOutDto> getAllByStatus(EStatus status);
}
