package me.sillysock.sillypunishments.modules;

import me.sillysock.sillypunishments.SillyPunishments;
import me.sillysock.sillypunishments.sillyapi.PunishmentType;
import me.sillysock.sillypunishments.sillyapi.SillyApi;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;

/**
 * <h1 style="font-family: --apple-system"><code style="font-size: 20px;">{@code
 * BanCommand.java}</code> is used for punishing any player on the current
 * server, Using GUI.</h1>
 *
 * <h2 style="font-family: --apple-system">Requires: permroot.admin |
 * permroot.mod | permroot.*</h2>
 */

public class BanCommand implements CommandExecutor {

  private static HashMap<Player, PunishmentType> punishers = new HashMap<>();

  private final FileConfiguration langFile = SillyPunishments.getLangFile();
  private final SillyApi api = SillyPunishments.getApi();

  private final String prefix = langFile.getString("prefix");
  private final String noPermission = langFile.getString("no_permission");
  private final String permissionRoot = langFile.getString("perms");
  private final String modRoot = permissionRoot + langFile.getString("moderator");
  private final String adminRoot = permissionRoot + langFile.getString("administrator");

  @Override
  public boolean
  onCommand(@NonNull final CommandSender sender, @NonNull final Command cmd,
            @NonNull final String label, @NonNull final String[] args) {

    if (!(sender instanceof Player)) {
      sender.sendMessage(prefix + ChatColor.RED +
                         "Only players may execute this command.");
      return true;
    }

    final Player p = (Player)sender;

    if (p.hasPermission(modRoot) || p.hasPermission(adminRoot) ||
        p.hasPermission(prefix + "*")) {
      p.openInventory(api.createPunishMenu(p));
    } else {
      p.sendMessage(
              noPermission != null
                      ? noPermission
                      : "You do not have the required permission to execute this command.");

      return true;
    }

    return false;
  }

  private boolean checkPermission(final Player p) {
    return false; // TODO
  }

  public static HashMap<Player, PunishmentType> getPunishers() {
    return punishers;
  }
}
