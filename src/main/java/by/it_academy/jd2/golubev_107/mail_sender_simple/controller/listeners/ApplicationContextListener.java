package by.it_academy.jd2.golubev_107.mail_sender_simple.controller.listeners;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.factory.ServiceFactory;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.factory.StorageFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private ServiceFactory serviceFactory;
    private StorageFactory storageFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        storageFactory = StorageFactory.getInstance();
        serviceFactory = ServiceFactory.getInstance();
        System.out.println("Context Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        storageFactory.destroy();
        serviceFactory.destroy();
        System.out.println("Context closed");
    }
}
