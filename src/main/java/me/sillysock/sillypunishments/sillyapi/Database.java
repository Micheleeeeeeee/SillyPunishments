package me.sillysock.sillypunishments.sillyapi;

import me.sillysock.sillypunishments.SillyPunishments;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class Database {
    
    private final String host = SillyPunishments.getDatabaseHost();
    private final String username = SillyPunishments.getDatabaseUsername();
    private final String passphrase = SillyPunishments.getDatabasePassphrase();
    private final int port = SillyPunishments.getDatabasePort();
    private final String name = SillyPunishments.getDatabaseName();
    
    private int retries = 0;
    private Connection connection;
    
    public void openConnection() {
        
        if (retries > 3) {
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
            
        } catch (ClassNotFoundException | SQLException e) {
            retries += 1;
            openConnection();
            System.out.println("An error has occurred, retrying.\nRetries: " + retries);
        }

        try {
            if (connection.isClosed()) {
                resetConnection();
                System.out.println("Connection issues...");
                retries++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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



        return false;
    }

    public long getPunishmentExpiry(final UUID uuid) {

        return 0;
    }
}
