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

        switch (name) {
            case "BAN":
                type = PunishmentType.BAN;
            case "KICK":
                type = PunishmentType.KICK;
            case "MUTE":
                type = PunishmentType.MUTE;

        }
    }
}
