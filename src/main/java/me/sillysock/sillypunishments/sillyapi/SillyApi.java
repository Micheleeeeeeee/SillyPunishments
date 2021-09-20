package me.sillysock.sillypunishments.sillyapi;

import me.sillysock.sillypunishments.SillyPunishments;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SillyApi {

  /*
  SillyReminders:

  Flow Chart for Menus:

  Punishment Main Menu
  --> User Selection [KICK, BAN, MUTE]
  --> Punishment Duration
  --> Reason [or custom]
  --> Database Entry for punishment added
  --> Punishment Executed on server
   */

  private Inventory menu;
  private Inventory banMenu;
  private String title;
  private static final SillyPunishments main = SillyPunishments.getInstance();

  public static final String BUMPER = StringUtils.repeat("\n", 25);

  public Inventory createPunishMenu(final OfflinePlayer target) {
    title = "Punish " + C.YELLOW + target.getName();

    menu = Bukkit.createInventory(
        null, 36, title); // Create an inventory with the size 36.

    final ItemStack head =
        getHead(target); // Create head by getting head [in. method]

    menu.setItem(4, head); // Add head to the menu at the 4th slot.

    createMenuItem(Material.TERRACOTTA, "Exclude", "",
                   "Exclude " + target.getName(), "From the server.", "[BETA]",
                   19, menu);

    createMenuItem(Material.TERRACOTTA, "Mute", "",
                 "Prevent " + target.getName(), "From speaking on the server.", C.RED + "BETA"
                   , 22, menu);

    createMenuItem(Material.TERRACOTTA, "Kick", "",
                  "Remove " + target.getName(), "From the server.", C.RED + "BETA",
                  25, menu);

    return menu;
  }

  /**
   * <p style="font-size: 12px; color: rgb(200, 151, 51);">
   * createBanMenu creates the exclusion menu for Minecraft.
   * This menu is used exclusively for excluding players from the server.
   * </p>
   * @param target
   * @return {@code Inventory}
   */

  public Inventory createBanMenu(final OfflinePlayer target) {
    banMenu = Bukkit.createInventory(null, 36, "Exclude " + target);

    banMenu.setItem(4, getHead(target));

    return banMenu;
  }

  private static void createMenuItem(final Material material, final String name,
                                     final String lore1, final String lore2,
                                     final String lore3, final String lore4,
                                     final int slot,
                                     final @NotNull Inventory menu) {

    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();

    List<String> lore = new ArrayList<>();

    if (lore1 != null)
      lore.add(C.WHITE + lore1);

    if (lore2 != null)
      lore.add(C.WHITE + lore2);

    if (lore3 != null)
      lore.add(C.WHITE + lore3);

    if (lore4 != null)
      lore.add(C.WHITE + lore4);

    meta.setLore(lore); // Lore [description of item]
    meta.setDisplayName(name); // Set name of item to [name]..

    item.setItemMeta(meta);

    menu.setItem(slot, item);
  }

  private static ItemStack getHead(final OfflinePlayer target) {
    ItemStack head = new ItemStack(Material.PLAYER_HEAD);

    final SkullMeta meta = (SkullMeta)head.getItemMeta();
    meta.setOwningPlayer(target);

    head.setItemMeta(meta);

    return head;
  }

  /**
   * Deprecated as this plugin will migrate to MySQL.
   * @param p
   * @param expires
   * @param reason
   * @return
   */

  @Deprecated()
  public static void banPlayer(final Player p, final Date expires, final String reason) { }

  /**
   * Deprecated as this plugin will migrate to MySQL.
   * @param player
   * @param reason
   * @return
   */

  @Deprecated()
  public static void permanentBan(final Player player, final String reason) { }
}
