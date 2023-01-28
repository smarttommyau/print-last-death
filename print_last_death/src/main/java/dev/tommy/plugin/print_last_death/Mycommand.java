package dev.tommy.plugin.print_last_death;

import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Mycommand implements TabExecutor{
    private final Plugin plugin;
    public Mycommand(Plugin plugin) {this.plugin = plugin;}
    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label,String[] args) {
        boolean isplayer = false;
        if((sender instanceof Player)){
            isplayer = true;
        }
        if(args.length == 0){
            sendERRMessage(sender);
            return true;
        }
        if(args[0].equals("enabled")){
            if(!isplayer){
                sender.sendMessage("Only player can use this args");
                return true;
            }
            Player p = (Player) sender;
            if(args.length == 1){
                ToogleEnable(p.getUniqueId(),sender);
            }else if(args.length == 2){
                if(args[1].equals("true")){
                    ToogleEnable(p.getUniqueId(),true);
                    sender.sendMessage("Immediate Print Death set to true");
                }else if(args[1].equals("false")){
                    ToogleEnable(p.getUniqueId(),false);
                    sender.sendMessage("Immediate Print Death set to false");
                }else{
                    sendERRMessage(sender);
                }
            }else{
                sendERRMessage(sender);
            }
        }else if(args[0].equals("print")){
            if(args.length == 1){
               if(!isplayer){
                sender.sendMessage("Only player can use this args");
                return true;
               } 
               Player p = (Player) sender;
               print_last_death(p.getUniqueId(), sender);
            }else if(args.length == 2){
                print_last_death(args[1], sender);
            }else{
                sendERRMessage(sender);
            }
        }else if(args[0].equals("help")||args[0].equals("?")){
            sender.sendMessage("Print last Death help menu \n/pd || /printDeath\n help|?: show this menu\nprint: print your last death\nprint Player: print the player last death\nenabled: toogle the enable of immediate auto print on death\nenabled [true|false]: set the enable of immediate auto print on death");
        }else{
            sendERRMessage(sender);
        }

        return true;
    }
    @Override
    public  List<String> onTabComplete( CommandSender sender,  Command cmd,
             String label,  String[] args) {
        List<String> str = new ArrayList<>();
        boolean isplayer = false;
        if(sender instanceof Player)
            isplayer = true;

        if(args.length <= 1){
            str.add("help");
            str.add("?");
            str.add("print");
            if(isplayer)
                str.add("enabled");
        }else if(args.length <= 2){
            if(args[0].equals("enabled")){
                str.add("true");
                str.add("false");
            }else if(args[0].equals("print")){
                OfflinePlayer[] p = Bukkit.getOfflinePlayers();
                for(OfflinePlayer x:p){
                    str.add(x.getName());
                }
            }else{
                return null;
            }
        }else{
            return null;
        }
        
        return str;
    }
    private void sendERRMessage(CommandSender sender){
        sender.sendMessage(ChatColor.RED + "use [/pd help|/printDeath help] to see the usage of this command");
        return;
    }

    private boolean checkuuid(UUID uuid){
        boolean immediate_print = plugin.getConfig().getBoolean(uuid.toString()+".enabled");
        return immediate_print;
    }

    private void ToogleEnable(UUID uuid,CommandSender sender){
        boolean status = !checkuuid(uuid);
        ToogleEnable(uuid,status);
        sender.sendMessage("Imediate Print Death set to " + status);
    }
    private void ToogleEnable(UUID uuid,boolean enabled){
        plugin.getConfig().set(uuid.toString()+".enabled", enabled);
        plugin.saveConfig();
    }
    private void print_last_death(String PlayerName,CommandSender sender){
        @Nullable OfflinePlayer p = Bukkit.getOfflinePlayerIfCached(PlayerName);
        if(p == null){
            sender.sendMessage("invalid player name");
            return;
        }
        print_last_death(p.getUniqueId(), sender);
    }
    private void print_last_death(UUID uuid,CommandSender sender){
        FileConfiguration config = plugin.getConfig();
        if(config.get(uuid.toString()+".time") == null){
            sender.sendMessage("There is no death record");
            return;
        }
        String string =  org.bukkit.ChatColor.GRAY + "Time:" + config.get(uuid.toString()+".time") + "\n" +  org.bukkit.ChatColor.RED + "World:" + config.get(uuid.toString()+".world") + org.bukkit.ChatColor.AQUA + " X:"  + config.get(uuid.toString()+".x") + org.bukkit.ChatColor.GREEN + " Y:" + config.get(uuid.toString()+".y") + org.bukkit.ChatColor.YELLOW + " Z:" + config.get(uuid.toString()+".z");
        sender.sendMessage(string);
    }
}
