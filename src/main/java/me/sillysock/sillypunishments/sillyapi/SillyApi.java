package me.sillysock.sillypunishments.sillyapi;

import net.kyori.adventure.text.Component; // Text
import org.bukkit.Bukkit; // buwukkit
import org.bukkit.Material; // Item i.e DIAMOND_SWORD
import org.bukkit.OfflinePlayer; // OfflinePlayer, players that aren't online :tro:
import org.bukkit.inventory.Inventory; // Create GUIs
import org.bukkit.inventory.ItemStack; // ItemStack
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList; //
import java.util.List;

public class SillyApi {

    private static Inventory menu;

    public static Inventory createPunishMenu(final OfflinePlayer target) {
        String title = "Punish " + target;

        menu = Bukkit.createInventory(null, 36, title); // Create an inventory with the size 36.

        final ItemStack head = getHead(target);  // Create head by getting head [in. method]

        menu.setItem(4, head);  // Add head to the menu at the 4th slot.

        return menu;
    }

    private static void createMenuItem(final Material material, final String name, final List<String> lore, final int slot, final Inventory menu) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setLore(lore);
        meta.setLocalizedName(name);

        item.setItemMeta(meta);

        menu.setItem(slot, item);

    }

    private static ItemStack getHead(final OfflinePlayer target) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        final SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(target);

        head.setItemMeta(meta);

        return head;
    }

}
