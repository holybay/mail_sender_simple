package by.it_academy.jd2.golubev_107.mail_sender_simple.service.job.impl;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.IMailSenderService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.EmailOutDto;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.EStatus;

import java.util.List;

public class MailSendJob implements Runnable {

    private final IMailService mailService;
    private final IMailSenderService mailSenderService;

    public MailSendJob(IMailService mailService, IMailSenderService mailSenderService) {
        this.mailService = mailService;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public void run() {
        List<EmailOutDto> allByStatus = mailService.getAllByStatus(EStatus.LOADED);
        for (EmailOutDto email : allByStatus) {
            try {
                mailService.updateStatus(email.getId(), EStatus.SENDING);
                mailSenderService.send(email);
                mailService.updateStatus(email.getId(), EStatus.FINISH);
            } catch (Exception e) {
                mailService.updateStatus(email.getId(), EStatus.ERROR);
                System.out.println("~~~~~ERROR~~~~~\n" + e.getMessage());
            }
        }
    }
}
