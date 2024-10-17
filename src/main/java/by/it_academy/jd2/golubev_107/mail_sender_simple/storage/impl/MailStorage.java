package by.it_academy.jd2.golubev_107.mail_sender_simple.storage.impl;

import by.it_academy.jd2.golubev_107.mail_sender_simple.platform.DBUtil;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.IMailStorage;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.connection.IConnectionManager;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.EStatus;
import by.it_academy.jd2.golubev_107.mail_sender_simple.storage.entity.Email;

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
            INSERT INTO app.emails (id, recipient_to, title, body_text, status, created_at, updated_at) 
            VALUES(?, ?, ?, ?, ?, ?, ?)""";
    private static final String UPDATE_EMAIL_QUERY = """
            UPDATE app.emails
            SET recipient_to = ?, title = ?, body_text = ?, status = ?, updated_at = ? WHERE id = ?;""";
    private static final String SELECT_EMAIL_QUERY =
            "SELECT id, recipient_to, title, body_text, status, created_at, updated_at FROM app.emails WHERE id = ?;";
    private static final String SELECT_ALL_EMAIL_QUERY =
            "SELECT id, recipient_to, title, body_text, status, created_at, updated_at FROM app.emails;";
    private static final String SELECT_ALL_EMAIL_BY_STATUS_QUERY =
            "SELECT id, recipient_to, title, body_text, status, created_at, updated_at FROM app.emails WHERE status = ?;";
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

            insertEmail(email, connection);

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

                    return mapper(rs);
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
                        allEmails.add(mapper(rs));
                    }
                    return allEmails;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read all emails", e);
        }
    }

    @Override
    public List<Email> readAllByStatus(EStatus eStatus) {
        try (Connection connection = connectionManager.getConnection()) {
            try (PreparedStatement selectEmailsByStatusStmt = connection.prepareStatement(SELECT_ALL_EMAIL_BY_STATUS_QUERY)) {
                selectEmailsByStatusStmt.setString(1, eStatus.name());
                try (ResultSet rs = selectEmailsByStatusStmt.executeQuery()) {
                    selectEmailsByStatusStmt.clearParameters();
                    List<Email> allEmails = new ArrayList<>();
                    while (rs.next()) {
                        allEmails.add(mapper(rs));
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

            updateEmail(email, connection);

            connection.commit();
        } catch (SQLException | RuntimeException e) {
            DBUtil.transactionRollback(connection);
            throw new RuntimeException("Failed to update an email with id: " + email, e);
        } finally {
            DBUtil.connectionClose(connection);
        }
    }

    private Email mapper(ResultSet rs) throws SQLException {
        return Email.builder()
                    .setId(rs.getObject("id", UUID.class))
                    .setRecipientTo(rs.getString("recipient_to"))
                    .setTitle(rs.getString("title"))
                    .setText(rs.getString("body_text"))
                    .setEmailStatus(EStatus.valueOf(rs.getString("status")))
                    .setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                    .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
    }

    private void insertEmail(Email email, Connection connection) throws SQLException {
        try (PreparedStatement insrtEmail = connection.prepareStatement(INSERT_EMAIL_QUERY)) {
            insrtEmail.setObject(1, email.getId());
            insrtEmail.setString(2, email.getRecipientTo());
            insrtEmail.setString(3, email.getTitle());
            insrtEmail.setString(4, email.getText());
            insrtEmail.setString(5, email.getEmailStatus().name());
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

    private void updateEmail(Email email, Connection connection) throws SQLException {
        try (PreparedStatement updateEmail = connection.prepareStatement(UPDATE_EMAIL_QUERY)) {
            updateEmail.setString(1, email.getRecipientTo());
            updateEmail.setString(2, email.getTitle());
            updateEmail.setString(3, email.getText());
            updateEmail.setString(4, email.getEmailStatus().name());
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
