package by.it_academy.jd2.golubev_107.mail_sender_simple.storage.impl;

import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.connection.HibernateManager;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.EStatus;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.Email;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.UUID;

public class MailStorageHibernate implements IMailStorage {

    private final HibernateManager hibernateManager;

    public MailStorageHibernate(HibernateManager hibernateManager) {
        this.hibernateManager = hibernateManager;
    }

    @Override
    public void create(Email email) {
        hibernateManager.inTransaction(manager -> {
            manager.persist(email);
            return email;
        });
    }

    @Override
    public Email readById(UUID id) {
        return hibernateManager.inTransaction(manager -> manager.find(Email.class, id));
    }

    @Override
    public List<Email> readAll() {
        return hibernateManager.inTransaction(manager -> manager.createQuery("from Email", Email.class)
                                                                .getResultList());
    }

    @Override
    public List<Email> readAllByStatus(EStatus newStatus) {
        return hibernateManager.inTransaction(manager -> {
            CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
            CriteriaQuery<Email> query = criteriaBuilder.createQuery(Email.class);
            Root<Email> entity = query.from(Email.class);
            query.where(criteriaBuilder.equal(entity.get("emailStatus"), newStatus));
            return manager.createQuery(query).getResultList();
        });
    }

    @Override
    public void update(Email email) {
        hibernateManager.inTransaction(manager -> manager.merge(email));
    }
}
