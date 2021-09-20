package me.sillysock.sillypunishments.listeners;

import me.sillysock.sillypunishments.sillyapi.PunishmentType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class PunishListeners implements Listener {

    private PunishmentType type;

    @EventHandler
    public void PunishMainMenuHandler(final @NotNull InventoryClickEvent e) {
        final InventoryView view = e.getView();

        final String title = view.getTitle();

        if (!title.contains("Punish"))
            return;

        if (e.getCurrentItem() == null)
            return;

        e.setCancelled(true); // Don't let user grab item out of menu :Scared:

        final ItemStack item = e.getCurrentItem();
        final ItemMeta meta = item.getItemMeta();
        final String name = meta.getDisplayName();

        type = parseType(name);
    }

    /**
     * The {@code parseType} method takes one parameter, a {@code String type}, and parses
     * and determines which PunishmentType it is. If the String does not match any of the
     * normal types, it returns {@code PunishmentType.OTHER}
     * @param type
     * @return PunishmentType
     */

    private PunishmentType parseType(final String type) {
        PunishmentType t = PunishmentType.OTHER;

        if (type.equalsIgnoreCase("ban"))
            t = PunishmentType.BAN;

        if (type.equalsIgnoreCase("mute"))
            t = PunishmentType.MUTE;

        if (type.equalsIgnoreCase("kick"))
            t = PunishmentType.KICK;

        return t;
    }
}
