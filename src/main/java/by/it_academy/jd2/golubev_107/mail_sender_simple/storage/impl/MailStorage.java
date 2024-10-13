package by.it_academy.jd2.golubev_107.mail_sender_simple.storage.impl;

import by.it_academy.jd2.golubev_107.mail_sender_simple.platform.DBUtil;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.connection.IConnectionManager;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.Email;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.EmailStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MailStorage implements IMailStorage {

    private static final String INSERT_EMAIL_QUERY = """
            INSERT INTO app.emails (id, recipient_to, title, body_text, status_id, created_at, updated_at) 
            VALUES(?, ?, ?, ?, ?, ?, ?)""";
    private static final String UPDATE_EMAIL_QUERY = """
            UPDATE app.emails
            SET recipient_to = ?, title = ?, body_text = ?, status_id = ?, updated_at = ? WHERE id = ?;""";
    private static final String SELECT_EMAIL_QUERY =
            "SELECT id, recipient_to, title, body_text, status_id, created_at, updated_at FROM app.emails WHERE id = ?;";
    private static final String SELECT_ALL_EMAIL_QUERY =
            "SELECT id, recipient_to, title, body_text, status_id, created_at, updated_at FROM app.emails;";
    private static final String SELECT_ALL_EMAIL_BY_STATUS_QUERY =
            "SELECT id, recipient_to, title, body_text, status_id, created_at, updated_at FROM app.emails WHERE status_id = ?;";
    private static final String SELECT_EMAIL_STATUS_BY_NAME_QUERY = "SELECT id, name FROM app.email_status WHERE name = ?;";
    private static final String SELECT_EMAIL_STATUS_BY_ID_QUERY = "SELECT id, name FROM app.email_status WHERE id = ?;";
    private final IConnectionManager connectionManager;

    public MailStorage(IConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void create(Email email) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            EmailStatus emailStatus = getEmailStatus(email.getEmailStatus().getStatus().name(), connection,
                    SELECT_EMAIL_STATUS_BY_NAME_QUERY);

            insertEmail(email, emailStatus.getId(), connection);

            connection.commit();
        } catch (SQLException | RuntimeException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to create an email: " + email, e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    @Override
    public Email readById(UUID id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectEmailStmt = connection.prepareStatement(SELECT_EMAIL_QUERY)) {
            selectEmailStmt.setObject(1, id);
            try (ResultSet rs = selectEmailStmt.executeQuery()) {
                selectEmailStmt.clearParameters();
                if (rs.next()) {
                    Long statusId = rs.getLong("status_id");
                    EmailStatus status = getEmailStatus(statusId, connection,
                            SELECT_EMAIL_STATUS_BY_ID_QUERY);
                    return mapper(rs, status);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read an email by id: " + id, e);
        }
        return null;
    }

    @Override
    public List<Email> readAll() {
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement selectEmailStmt = connection.prepareStatement(SELECT_ALL_EMAIL_QUERY)) {
                try (ResultSet rs = selectEmailStmt.executeQuery()) {
                    selectEmailStmt.clearParameters();
                    List<Email> allEmails = new ArrayList<>();
                    while (rs.next()) {
                        Long statusId = rs.getLong("status_id");
                        EmailStatus status = getEmailStatus(statusId, connection,
                                SELECT_EMAIL_STATUS_BY_ID_QUERY);
                        allEmails.add(mapper(rs, status));
                    }
                    return allEmails;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read all emails", e);
        }
    }

    @Override
    public List<Email> readAllByStatus(EmailStatus.EStatus eStatus) {
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement selectEmailsByStatusStmt = connection.prepareStatement(SELECT_ALL_EMAIL_BY_STATUS_QUERY)) {
                EmailStatus status = getEmailStatus(eStatus.name(), connection,
                        SELECT_EMAIL_STATUS_BY_NAME_QUERY);
                selectEmailsByStatusStmt.setLong(1, status.getId());
                try (ResultSet rs = selectEmailsByStatusStmt.executeQuery()) {
                    selectEmailsByStatusStmt.clearParameters();
                    List<Email> allEmails = new ArrayList<>();
                    while (rs.next()) {
                        allEmails.add(mapper(rs, status));
                    }
                    return allEmails;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read all emails with status = " + eStatus.name(), e);
        }
    }

    @Override
    public void update(Email email) {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            connection.setAutoCommit(false);
            DBUtil.transactionBegin(connection);

            EmailStatus emailStatus = getEmailStatus(email.getEmailStatus().getStatus().name(), connection,
                    SELECT_EMAIL_STATUS_BY_NAME_QUERY);

            updateEmail(email, emailStatus.getId(), connection);

            connection.commit();
        } catch (SQLException | RuntimeException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to update an email with id: " + email, e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    private <T> EmailStatus getEmailStatus(T paramToFindBy, Connection connection, String sqlQuery) throws SQLException {
        try (PreparedStatement selectEStatusStmt = connection.prepareStatement(sqlQuery)) {
            selectEStatusStmt.setObject(1, paramToFindBy);
            try (ResultSet rs = selectEStatusStmt.executeQuery()) {
                selectEStatusStmt.clearParameters();
                if (rs.next()) {
                    EmailStatus status = new EmailStatus();
                    status.setId(rs.getLong("id"));
                    status.setStatus(EmailStatus.EStatus.valueOf(rs.getString("name")));
                    return status;
                }
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Can't get the email status by param: " + paramToFindBy);
            }
        }
    }

    private Email mapper(ResultSet rs, EmailStatus status) throws SQLException {
        return Email.builder()
                    .setId(rs.getObject("id", UUID.class))
                    .setRecipientTo(rs.getString("recipient_to"))
                    .setTitle(rs.getString("title"))
                    .setText(rs.getString("body_text"))
                    .setEmailStatus(status)
                    .setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                    .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
    }

    private void insertEmail(Email email, Long statusId, Connection connection) throws SQLException {
        try (PreparedStatement insrtEmail = connection.prepareStatement(INSERT_EMAIL_QUERY)) {
            insrtEmail.setObject(1, email.getId());
            insrtEmail.setString(2, email.getRecipientTo());
            insrtEmail.setString(3, email.getTitle());
            insrtEmail.setString(4, email.getText());
            insrtEmail.setLong(5, statusId);
            insrtEmail.setTimestamp(6, Timestamp.valueOf(email.getCreatedAt()));
            insrtEmail.setTimestamp(7, Timestamp.valueOf(email.getUpdatedAt()));
            int rowsInserted = insrtEmail.executeUpdate();
            insrtEmail.clearParameters();

            if (rowsInserted != 1) {
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Inserted more than one row!");
            }
        }
    }

    private void updateEmail(Email email, Long statusId, Connection connection) throws SQLException {
        try (PreparedStatement updateEmail = connection.prepareStatement(UPDATE_EMAIL_QUERY)) {
            updateEmail.setString(1, email.getRecipientTo());
            updateEmail.setString(2, email.getTitle());
            updateEmail.setString(3, email.getText());
            updateEmail.setLong(4, statusId);
            updateEmail.setTimestamp(5, Timestamp.valueOf(email.getUpdatedAt()));
            updateEmail.setObject(6, email.getId());
            int rowsUpdated = updateEmail.executeUpdate();
            updateEmail.clearParameters();

            if (rowsUpdated != 1) {
                DBUtil.transactionRollback(connection);
                throw new IllegalStateException("Updated more than one row!");
            }
        }
    }
}
