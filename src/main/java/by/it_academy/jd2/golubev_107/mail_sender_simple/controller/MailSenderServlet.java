package by.it_academy.jd2.golubev_107.mail_sender_simple.controller;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.CreateEmailDto;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.dto.EmailOutDto;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.factory.ServiceFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/mail")
public class MailSenderServlet extends HttpServlet {

    private static final String RECIPIENT_TO_PARAM = "recipientsTO";
    private static final String TITLE_PARAM = "messageTitle";
    private static final String TEXT_BODY_PARAM = "messageBody";
    private final IMailService mailService = ServiceFactory.getInstance().getMailService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<EmailOutDto> all = mailService.getAll();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        mailService.create(toDto(req));
    }

    private CreateEmailDto toDto(HttpServletRequest req) {
        return CreateEmailDto.builder()
                             .setRecipientTo(req.getParameter(RECIPIENT_TO_PARAM))
                             .setTitle(req.getParameter(TITLE_PARAM))
                             .setText(req.getParameter(TEXT_BODY_PARAM))
                             .build();
    }
}
