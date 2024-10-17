package by.it_academy.jd2.golubev_107.mail_sender_simple.controller.listeners;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.factory.ServiceFactory;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.factory.StorageFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (StorageFactory storageFactory = StorageFactory.getInstance();
             ServiceFactory serviceFactory = ServiceFactory.getInstance()) {
            sce.getServletContext().setRequestCharacterEncoding("UTF-8");
            sce.getServletContext().setResponseCharacterEncoding("text/html; charset=UTF-8");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        System.out.println("Context Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Context closed");
    }
}
