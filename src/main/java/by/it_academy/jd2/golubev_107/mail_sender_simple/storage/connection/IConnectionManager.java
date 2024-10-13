package by.it_academy.jd2.golubev_107.mail_sender_simple.storage.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionManager extends AutoCloseable {

    Connection getConnection() throws SQLException;
}
