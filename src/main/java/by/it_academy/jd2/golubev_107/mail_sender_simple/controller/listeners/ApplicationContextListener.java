package by.it_academy.jd2.golubev_107.mail_sender_simple.controller.listeners;

import by.it_academy.jd2.golubev_107.mail_sender_simple.service.factory.ServiceFactory;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.factory.StorageFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.ArrayList;
import java.util.List;

@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private List<AutoCloseable> closeables;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        closeables = new ArrayList<>();
        closeables.add(StorageFactory.getInstance());
        closeables.add(ServiceFactory.getInstance());
        sce.getServletContext().setRequestCharacterEncoding("UTF-8");
        sce.getServletContext().setResponseCharacterEncoding("text/html; charset=UTF-8");

        System.out.println("Context Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        for (AutoCloseable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception e) {
                System.out.println("FAILED TO CLOSE" + e.getMessage());
            }
        }
        System.out.println("Context closed");
    }
}
