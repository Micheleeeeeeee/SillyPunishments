package me.sillysock.sillypunishments.sillyapi;

import org.bukkit.ChatColor;

public class C {

    public static final ChatColor GREEN = ChatColor.GREEN;
    public static final ChatColor DARK_GREEN = ChatColor.DARK_GREEN;
    public static final ChatColor BOLD = ChatColor.BOLD;
    public static final ChatColor RESET = ChatColor.RESET;
    public static final ChatColor RED = ChatColor.RED;
    public static final ChatColor DARK_RED = ChatColor.DARK_RED;
    public static final ChatColor PINK = ChatColor.LIGHT_PURPLE;
    public static final ChatColor PURPLE = ChatColor.DARK_PURPLE;
    public static final ChatColor GOLD = ChatColor.GOLD;
    public static final ChatColor YELLOW = ChatColor.YELLOW;
    public static final ChatColor AQUA = ChatColor.AQUA;
    public static final ChatColor DARK_AQUA = ChatColor.DARK_AQUA;
    public static final ChatColor GRAY = ChatColor.GRAY;
    public static final ChatColor DARK_GRAY = ChatColor.DARK_GRAY;
    public static final ChatColor DARK_BLUE = ChatColor.DARK_BLUE;
    public static final ChatColor BLUE = ChatColor.BLUE;


    public static String format(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
