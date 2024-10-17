package by.it_academy.jd2.golubev_107.mail_sender_simple.storage.factory;

import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.connection.factory.ConnectionManagerFactory;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.impl.MailStorage;

public class StorageFactory implements AutoCloseable {

    private static final StorageFactory INSTANCE = new StorageFactory(
            ConnectionManagerFactory.getInstance());
    private final IMailStorage mailStorage;

    private StorageFactory(ConnectionManagerFactory cmf) {
        mailStorage = new MailStorage(cmf.get());
    }

    public static StorageFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public void close() throws Exception {
        ConnectionManagerFactory.getInstance().destroy();
    }

    public IMailStorage getMailStorage() {
        return mailStorage;
    }
}
