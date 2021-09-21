package me.sillysock.sillypunishments.sillyapi;

import me.sillysock.sillypunishments.SillyPunishments;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.TestOnly;

import java.sql.*;
import java.time.Instant;
import java.util.UUID;

public class Database implements Listener {
    
    private String host;
    private String username;
    private String passphrase;
    private int port;
    private String name;
    
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

        if (dataTableExists()) {
            Bukkit
                    .getConsoleSender()
                    .sendMessage(C.RED + "Data table already exists...");

            return;
        }

        openConnection();

        try {
            Bukkit
                    .getConsoleSender()
                    .sendMessage(C.GREEN + "Attempting to create data table...");

            final PreparedStatement createTable = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS data (" +
                            "uuid VARCHAR(255) NOT NULL PRIMARY KEY," +
                            "expiry INTEGER NOT NULL," +
                            "punishment_type VARCHAR(10) NOT NULL" +
                            ")"
            );

            createTable.execute();

            Bukkit
                    .getConsoleSender()
                    .sendMessage(C.GREEN + "Data table created successfully.");
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

        username = SillyPunishments.getDatabaseUsername();
        passphrase = SillyPunishments.getDatabasePassphrase();
        port = SillyPunishments.getDatabasePort();
        name = SillyPunishments.getDatabaseName();
        host = SillyPunishments.getDatabaseHost();

        System.out.println("Logging in with details:\n" + "Username: " + username + "\nHost: " + host + "\nPassphrase: " + passphrase + "\nName: " + name + "\nPort: " + port);
        Bukkit
                .getConsoleSender()
                .sendMessage(C.RED + "None of your information is stored. This is purely for your eyes only.");

        try {

            if (connection != null) {
                if (!connection.isClosed())
                    return;
            }

            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + name
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
            e.printStackTrace();
        }

    }
    
    public void closeConnection() {

        if (connection == null) return;

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
            final PreparedStatement ps = connection.prepareStatement("SELECT expiry FROM data WHERE uuid=?");
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

    public PunishmentType getPunishmentType(final UUID uuid) {
        if (!isPunished(uuid)) return PunishmentType.NOT_PUNISHED;

        openConnection();

        try {
            final PreparedStatement ps = connection.prepareStatement(
                    "SELECT punishment_type FROM data WHERE uuid=?"
            );

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            if (results.next()) {
                return PunishmentType.valueOf(results.getString("punishment_type"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return PunishmentType.OTHER;
    }

    /**
     * isBannedAndNotExpired takes one parameter, {@code uuid} and checks if the user is
     * currently banned from the server, and whether the punishment is expired or not.
     * This returns a {@code boolean}, indicating whether the player is banned and their punishment
     * has not expired ({@code} true}) or otherwise {@code false}
     * @param uuid
     * @return
     */

    public boolean isBannedAndNotExpired(final UUID uuid) {
        boolean out = false;

        if (!isPunished(uuid)) return out;

        openConnection();

        try {
            final PreparedStatement ps = connection.prepareStatement(
                    "SELECT expiry FROM data WHERE uuid=? AND punishment_type='BAN'"
            );

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            if (results.next()) {
                long expiry = results.getLong("expiry");
                if (Instant.now().getEpochSecond() < expiry || expiry == -1) out = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return out;
    }

    /**
     * isPunishedAndNotExpires is a method that, given parameter {@code UUID uuid} and {@code PunishmentType type}
     * returns whether the users punishment has expired or not. If the punishment is permanent, {@code -1}, this counts as
     * not expired. If the user is not punished, {@code false} is returned.
     * @param uuid
     * @param type
     * @return
     */

    @TestOnly()
    public boolean isPunishedAndNotExpired(final UUID uuid, final PunishmentType type) {
        boolean out = false;

        if (!(isPunished(uuid))) return out;

        try {
            openConnection();

            final PreparedStatement ps = connection.prepareStatement("SELECT expiry FROM data WHERE uuid=? AND punishment_type=?");
            ps.setString(1, uuid.toString());
            ps.setString(2, type.toString());

            final ResultSet results = ps.executeQuery();

            while (results.next()) {
                final long expiry = results.getLong("expiry");

                if (Instant.now().getEpochSecond() < expiry || expiry == -1) out = true;
            }

            results.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return out;
    }

    /**
     * This method takes one parameter, UUID, and returns the time left until
     * Their punishment expires. If their punishment is permanent, it will return -1.
     * If they are not punished, it will return 0.
     * @param uuid
     * @return unix time
     */

    public long getPunishmentExpiry(final UUID uuid) {

        if (!isPunished(uuid)) return 0;

        openConnection();

        long out = 0;

        try {
            final PreparedStatement ps = connection.prepareStatement("SELECT expiry FROM data WHERE uuid=?");
            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            while (results.next()) {
                out = results.getLong("expiry");

                results.close();
                ps.close();
            }

        } catch (SQLException e) {
            e.printStackTrace(); // TODO better exception handling
        }

        closeConnection();

        return out;
    }

    @Deprecated
    public boolean isMutedAndNotExpired(final UUID uuid) {

        boolean out = false;

        if (!isPunished(uuid)) return out;

        openConnection();

        try {
            final PreparedStatement ps = connection.prepareStatement(
                    "SELECT expiry FROM data WHERE uuid=? AND punishment_type='MUTE'"
            );

            ps.setString(1, uuid.toString());

            final ResultSet results = ps.executeQuery();

            if (results.next()) {
                out = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();

        return out;
    }

    @Deprecated
    @EventHandler
    public void CheckForPunishmentsTest(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final UUID uuid = p.getUniqueId();

        if (isPunished(uuid)) p.sendMessage(C.RED + "You are punished!");
        else p.sendMessage(C.GREEN + "You are not punished!");
    }
}
