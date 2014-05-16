package com.minecats.cindyk.limitvillagers;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Cindy on 5/16/14.
 */
public class LimitVillagers extends JavaPlugin implements Listener, CommandExecutor {

    public LimitVillagers plugin;

    Config config;

    @Override
    public void onEnable() {
        super.onEnable();

        plugin = this;
        config = new Config(this);

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("LimitVillagers").setExecutor(this);

        getLogger().info("Enabled!");

    }

    @Override
    public void onDisable() {
        super.onDisable();

        getLogger().info("Disabled!");
    }

    @EventHandler
    void onVillagerSpawn(CreatureSpawnEvent ev)
    {
        if(ev.getEntity().getType() == EntityType.VILLAGER)
        {
            Villager vg = (Villager) ev.getEntity();
            Chunk ch = vg.getLocation().getChunk();
            int CountThem = 0;
            for( Entity e : ch.getEntities())
            {
                if(e.getType()==EntityType.VILLAGER)
                    CountThem++;
            }

            if(CountThem > config.MaxVillagersInChunk)
            {
                ev.setCancelled(true);
                getLogger().info("There are more than " + config.MaxVillagersInChunk + " in chunk "+ch.getChunkSnapshot().hashCode()+"cancelling villager creation" );
            }
        }
    }

   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
   {

       if(cmd.getName().compareToIgnoreCase("LimitVillagers")==0)
       {
           if(sender instanceof Player)
           {
               Player p = (Player)sender;
              if( p.hasPermission("LimitVillager.Admin") || p.isOp() )
              {

                if(args.length == 1)
                {
                    if(args[0].compareToIgnoreCase("reload")==0)
                    {

                        sender.sendMessage("Reloading LimitVillagers config.yml...");
                        config.reloadConfig();
                        return true;
                    }

                }
                  sender.sendMessage("Limit Villagers : Keep villagers at bay since 2014");
                  sender.sendMessage("/limitvillagers reload");
              }
           }
       }



    sender.sendMessage("Done");
       return true;
   }

}
