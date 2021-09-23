package me.sillysock.sillypunishments.listeners;

import me.sillysock.sillypunishments.SillyPunishments;
import me.sillysock.sillypunishments.modules.BanCommand;
import me.sillysock.sillypunishments.sillyapi.C;
import me.sillysock.sillypunishments.sillyapi.Database;
import me.sillysock.sillypunishments.sillyapi.PunishmentType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;

public class PunishListeners implements Listener {

    /*
    Idea before I forget:

    Put lang.yml stuff on database???
    Allow people to change config stuff with a GUI
     */

    private static HashMap<Player, PunishmentType> punishers;
    private PunishmentType type;
    private static final Database db = SillyPunishments.getDb();

    @EventHandler
    public void ParseReasons(final @NotNull AsyncPlayerChatEvent e) {

        final Player p = e.getPlayer();

        if (!SillyPunishments.getTypingPlayers().contains(p)) return;

        e.setCancelled(true);

        p.sendMessage(SillyPunishments.getPrefix() + " Reason submitted...");
    }

    /**
     * This method cancels a player from speaking when they are currently muted.
     * @param e
     */

    @TestOnly
    @EventHandler
    public void HandleMute(final @NotNull AsyncPlayerChatEvent e) {

        /*
        TODO new Player variable.
         */

        final Player p = e.getPlayer();

        if (db.isMutedAndNotExpired(p.getUniqueId())) {
            e.setCancelled(true);

            p.sendMessage(SillyPunishments.getPrefix() + C.RED + " You cannot speak, as you are muted.");
        };
    }

    @EventHandler
    public void PunishMainMenuHandler(final @NotNull InventoryClickEvent e) {
        final InventoryView view = e.getView();
        final String title = view.getTitle();

        final Player p = (Player) e.getWhoClicked();

        if (!title.contains("Punish") || e.getCurrentItem() == null || !e.getCurrentItem().getType().equals(Material.TERRACOTTA))
            return;

        e.setCancelled(true); // Don't let user grab item out of menu :Scared:

        final ItemStack item = e.getCurrentItem();
        final ItemMeta meta = item.getItemMeta();
        final String name = meta.getDisplayName();

        type = parseType(name, p);
        SillyPunishments.getTypingPlayers().add(p);
        p.sendMessage(SillyPunishments.getPrefix() + C.AQUA + " Please type the reason for the punishment.");

        p.closeInventory(InventoryCloseEvent.Reason.UNKNOWN);
    }

    /**
     * The {@code parseType} method takes one parameter, a {@code String type}, and parses
     * and determines which PunishmentType it is. If the String does not match any of the
     * normal types, it returns {@code PunishmentType.OTHER}
     * @param type
     * @return PunishmentType
     */

    private PunishmentType parseType(final String type, final Player p) {
        PunishmentType t = PunishmentType.OTHER;
        punishers = BanCommand.getPunishers();

        if (type.equalsIgnoreCase("ban")) {
            t = PunishmentType.BAN;
            punishers.put(p, t);
        }

        if (type.equalsIgnoreCase("mute")) {
            t = PunishmentType.MUTE;
            punishers.put(p, t);
        }

        if (type.equalsIgnoreCase("kick")) {
            t = PunishmentType.KICK;
            punishers.put(p, t);
        }

        return t;
    }
}
