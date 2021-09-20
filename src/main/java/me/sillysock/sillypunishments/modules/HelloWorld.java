package me.sillysock.sillypunishments.modules;

import me.sillysock.sillypunishments.SillyPunishments;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class HelloWorld implements CommandExecutor {

    final String prefix = ChatColor.translateAlternateColorCodes(
            '&',
            Objects.requireNonNull(SillyPunishments
                    .getLangFile()
                    .getString("prefix"))
    );

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    prefix + ChatColor.GREEN + "Only players may execute this command."
            );
            return false;
        }

        final Player p = (Player) sender;

        if (args.length < 1) {
            p.sendMessage(prefix + ChatColor.RED + " lease use the correct arguments. </ban [player] {reason}");
            return false;
        }

        p.sendMessage("Test...");

        return false;
    }
}
