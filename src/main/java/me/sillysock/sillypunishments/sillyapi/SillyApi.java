package me.sillysock.sillypunishments.sillyapi;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class SillyApi {

    private static Inventory menu;
    private static Component title;

    public static Inventory createPunishMenu(final OfflinePlayer target) {
        title = Component.text("Punish " + target);

        menu = Bukkit.createInventory(null, 36, title);  // Create an inventory with the size 36.

        final ItemStack head = getHead(target);  // Create head by getting head [in. method]

        menu.setItem(4, head);  // Add head to the menu at the 4th slot.

        List<Component> test = new ArrayList<>();
        test.add(Component.text("awe"));

        /*
        TODO: Add rest of menu items
        BAN
        MUTE
        WARN
        KICK
         */

        return menu;
    }

    private static void createMenuItem(final Material material, final String name, final List<Component> lore, final int slot, final Inventory menu) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.lore(lore);
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
