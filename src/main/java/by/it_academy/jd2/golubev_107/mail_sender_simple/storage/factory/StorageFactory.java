package by.it_academy.jd2.golubev_107.mail_sender_simple.storage.factory;

import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.connection.HibernateManager;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.impl.MailStorageHibernate;

public class StorageFactory implements AutoCloseable {

    private static final StorageFactory INSTANCE = new StorageFactory();
    private final IMailStorage mailStorage;
    private final HibernateManager hibernateManager;

    private StorageFactory() {
        hibernateManager = new HibernateManager("unit");
        mailStorage = new MailStorageHibernate(hibernateManager);
    }

    public static StorageFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public void close() throws Exception {
        hibernateManager.close();
    }

    public IMailStorage getMailStorage() {
        return mailStorage;
    }
}
