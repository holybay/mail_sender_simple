package by.it_academy.jd2.golubev_107.mail_sender_simple.service;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.EmailOutDto;

public interface IMailSenderService {

    void send(EmailOutDto dto);

}
