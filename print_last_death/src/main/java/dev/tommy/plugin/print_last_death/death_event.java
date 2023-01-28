package dev.tommy.plugin.print_last_death;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;


public class death_event implements Listener{
    private final Plugin plugin;
    public death_event(Plugin plugin) {this.plugin = plugin;}

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player p = event.getPlayer();
        Location loc = p.getLocation();
        UUID uuid = p.getUniqueId();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        if(checkuuid(uuid)){
            String string =  org.bukkit.ChatColor.GRAY + "Time:" + dtf.format(now) + "\n" + org.bukkit.ChatColor.RED + "World:" + p.getWorld().getWorldFolder().getName() + org.bukkit.ChatColor.AQUA + " X:"  + loc.getBlockX() + org.bukkit.ChatColor.GREEN + " Y:" + loc.getBlockY() + org.bukkit.ChatColor.YELLOW + " Z:" + loc.getBlockZ();
            p.sendMessage(string);
        }
        UpdateLastDeath(uuid,p,dtf.format(now));    
    }
    private void UpdateLastDeath(UUID uuid,Player p,String time){
        FileConfiguration config = plugin.getConfig();
        Location loc = p.getLocation();
        config.set(uuid.toString()+".world", p.getWorld().getWorldFolder().getName());
        config.set(uuid.toString()+".x", loc.getBlockX());
        config.set(uuid.toString()+".y", loc.getBlockY());
        config.set(uuid.toString()+".z", loc.getBlockZ());
        config.set(uuid.toString()+".time", time);
        plugin.saveConfig();
    }

    private boolean checkuuid(UUID uuid){
        boolean immediate_print = plugin.getConfig().getBoolean(uuid.toString()+".enabled");
        return immediate_print;
    }
}
