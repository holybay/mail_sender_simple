package by.it_academy.jd2.golubev_107.mail_sender_simple.service.factory;

import by.it_academy.jd2.golubev_107.mail_sender_simple.platform.IPropertyReader;
import by.it_academy.jd2.golubev_107.mail_sender_simple.platform.impl.PropertyReader;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.IMailSenderService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.IMailService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.config.MailSenderConfig;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.impl.MailSenderService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.impl.MailService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.service.job.SchedulerService;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.factory.StorageFactory;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ServiceFactory {

    private static final String USER_PROP = "mail.smtp.user";
    private static final String PASSWORD_PROP = "user.password";
    private static final String DEBUG_MODE_PROP = "debug.mode";
    private static final ServiceFactory INSTANCE = new ServiceFactory(StorageFactory.getInstance(),
            "/smtpMailRu.properties");
    private final IMailService mailService;
    private final IMailSenderService mailSenderService;
    private final SchedulerService schedulerService;

    private ServiceFactory(StorageFactory storageFactory, String mailPropertiesFile) {
        mailService = new MailService(storageFactory.getMailStorage());
        MailSenderConfig config = setMailConfig(mailPropertiesFile);
        mailSenderService = new MailSenderService(config);
        schedulerService = new SchedulerService(3, TimeUnit.MINUTES, 1,
                mailService, mailSenderService);
    }

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public void destroy() {
        try {
            if (schedulerService != null) {
                schedulerService.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public IMailService getMailService() {
        return mailService;
    }

    public IMailSenderService getMailSenderService() {
        return mailSenderService;
    }

    private MailSenderConfig setMailConfig(String mailPropertiesFile) {
        IPropertyReader reader = new PropertyReader(mailPropertiesFile);
        Properties props = reader.getAll();

        MailSenderConfig config = new MailSenderConfig();

        config.setFrom(props.getProperty(USER_PROP));
        config.setUser(props.getProperty(USER_PROP));
        props.remove(USER_PROP);
        config.setPassword(props.getProperty(PASSWORD_PROP));
        props.remove(PASSWORD_PROP);
        config.setDebugModeOn(Boolean.parseBoolean(props.getProperty(DEBUG_MODE_PROP)));
        props.remove(DEBUG_MODE_PROP);

        config.setMailProps(props);
        return config;
    }
}
