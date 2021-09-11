package me.sillysock.sillypunishments;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.sillysock.sillypunishments.modules.BanCommand;
import me.sillysock.sillypunishments.modules.HelloWorld;
import me.sillysock.sillypunishments.sillyapi.SillyApi;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SillyPunishments extends JavaPlugin {

  protected static File lang;         // lang.yml
  protected FileConfiguration config; // config.yml

  protected static FileConfiguration langConfig; // lang.yml
  protected Logger log;                          // log stuff to console

  protected static SillyPunishments instance; // instance of main class
  protected static SillyApi api;

  /**
   * <h1 id="text" style="color: rgb(255, 192, 203);">
   *     Plugin Startup Logic
   *
   *     <p>Developed by Silly.</p>
   * </h1>
   */

  @Override
  public void onEnable() {

    instance = this;
    api = new SillyApi();

    log = getLogger();

    saveDefaultConfig();
    reloadConfig();
    saveConfig();

    config = getConfig();

    log.log(Level.INFO, "Logger initialised");
    log.log(Level.INFO, "Config initialised");
    log.log(Level.INFO, "Generating lang.yml...");

    createLangFile();

    log.log(Level.INFO, "Registering commands");

    register("helloworld",
             new HelloWorld()); // Registers the command from the HelloWorld
                                // class (modules --> HelloWorld.java) and
                                // prints a log message.
    register("ban",
             new BanCommand()); // Registers the command from the BanCommand
                                // class (modules --> BanCommand.java) and
                                // prints a log message.
  }

  /**
   * <h1>
   *     Plugin shutdown Logic
   *
   *     <p>Developed by Silly.</p>
   * </h1>
   */

  @Override
  public void onDisable() {
    log = null;
  }

  /**
   * Get lang file
   */

  public static FileConfiguration getLangFile() { return langConfig; }

  private void createLangFile() {
    lang = new File(getDataFolder(), "lang.yml");

    if (!lang.exists()) {
      lang.getParentFile().mkdir();
      saveResource("lang.yml", false);
    }

    langConfig = new YamlConfiguration();

    try {
      langConfig.load(lang);
    } catch (IOException | InvalidConfigurationException e) {
      createLangFile();
    }
  }

  /**
   * <h1 style="font-family: sans-serif; font-size: 10px;">
   *
   *     The register method takes two arguments, a string, for the command
   * name, and a CommandExecutor class, which implements the CommandExecutor
   * interface. It registers the command, and then outputs a log message.
   *
   *     i.e:
   *     register("helloworld", new HelloWorld());
   *
   *     Would Output:
   *
   *     Command helloworld
   *     Registered with me.sillysock.sillypunishments.modules.helloworld
   *
   * </h1>
   * @param cmd
   * @param executor
   */

  private void register(final String cmd, final CommandExecutor executor) {
    getCommand(cmd).setExecutor(executor);
    System.out.println("Command: " + cmd + "\nRegistered with " +
                       executor.toString());
  }

  public static SillyPunishments getInstance() { return instance; }

  public static SillyApi getApi() { return api; }
}

/* Doregon was here */
