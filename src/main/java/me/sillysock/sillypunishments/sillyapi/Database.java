package me.sillysock.sillypunishments.sillyapi;

import me.sillysock.sillypunishments.SillyPunishments;
import org.jetbrains.annotations.TestOnly;

import java.sql.*;
import java.util.UUID;

public class Database {
    
    private final String host = SillyPunishments.getDatabaseHost();
    private final String username = SillyPunishments.getDatabaseUsername();
    private final String passphrase = SillyPunishments.getDatabasePassphrase();
    private final int port = SillyPunishments.getDatabasePort();
    private final String name = SillyPunishments.getDatabaseName();
    
    private int retries = 0;
    private Connection connection;

    @TestOnly()
    public boolean dataTableExists() {
        openConnection();

        try {
            final DatabaseMetaData meta = connection.getMetaData();

            final ResultSet exists = meta.getTables(null, null, "data", null);

            if (exists.next()) return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return false;

    }

    @TestOnly()
    public void createDataTable() {
        openConnection();

        try {
            final PreparedStatement createTable = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS data (" +
                            "uuid VARCHAR(255) NOT NULL PRIMARY KEY," +
                            "expiry INTEGER NOT NULL," +
                            "punishment_type VARCHAR(10) NOT NULL" +
                            ")"
            );

            createTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();
    }
    
    public void openConnection() {
        
        if (retries >= 2) {
            retries = 0;
            return;
        }

        try {

            if (connection != null) {
                if (!connection.isClosed())
                    return;
            }

            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + name
                    , username
                    , passphrase
            );

            if (connection.isClosed()) {
                resetConnection();
                System.out.println("Connection issues...");
                retries++;
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            retries += 1;
            openConnection();
            System.out.println("An error has occurred, retrying.\nRetries: " + retries);
        }

    }
    
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetConnection() {
        closeConnection();
        openConnection();
    }

    public boolean isPunished(final UUID uuid) {
        openConnection();

        try {
            final PreparedStatement ps = connection.prepareStatement("SELECT expiry FROM " + name + " WHERE uuid=?");
            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();
            if (!results.next()) {
                ps.close();
                results.close();

                return false;
            }

            ps.close();
            results.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return false;
    }

    /**
     * This method takes one parameter, UUID, and returns the time left until
     * Their punishment expires. If their punishment is permanent, it will return -1.
     * If they are not punished, it will return 0.
     * @param uuid
     * @return unix
     */

    public long getPunishmentExpiry(final UUID uuid) {

        if (!isPunished(uuid)) return 0;

        long out = 0;
        openConnection();

        try {
            final PreparedStatement ps = connection.prepareStatement("SELECT expiry FROM " + name + " WHERE uuid=?");
            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            while (results.next()) {
                out = results.getLong("expiry");

                results.close();
                ps.close();
                closeConnection();
            }

            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace(); // TODO better exception handling
        }

        return out;
    }
}
