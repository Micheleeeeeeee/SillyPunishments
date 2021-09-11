package me.sillysock.sillypunishments.listeners;

import me.sillysock.sillypunishments.sillyapi.PunishmentType;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    final ItemStack item = e.getCurrentItem();
    final ItemMeta meta = item.getItemMeta();
    final @Nullable Component itemName = meta.displayName();
  }
}
