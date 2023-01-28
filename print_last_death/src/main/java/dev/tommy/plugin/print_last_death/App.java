package dev.tommy.plugin.print_last_death;

import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hello world!
 *
 */
public class App extends JavaPlugin
{
    @Override
    public void onEnable(){
        getConfig().options().copyDefaults(true);
        saveConfig();
        getLogger().info("Print last Death has started");
        getServer().getPluginManager().registerEvents(new death_event(this),this);

        TabExecutor cmd = new Mycommand(this);
        getCommand("printDeath").setExecutor(cmd);
        getCommand("pd").setExecutor(cmd);
        getCommand("printDeath").setTabCompleter(cmd);
        getCommand("pd").setTabCompleter(cmd);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}