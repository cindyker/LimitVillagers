package com.minecats.cindyk.limitvillagers;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cindy on 5/16/14.
 */
public class Config {

    LimitVillagers plugin;
    public int MaxVillagersInChunk;
    public List<String> NoSpawnMobs;
    public List<String> NoSpawnWorldsList;
    public boolean NoSpawnWorlds;

    public Config(LimitVillagers plugin)
    {
        this.plugin= plugin;
        loadConfig();

    }

    public void loadConfig() {

        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();
        FillSettings();
        plugin.getLogger().info("Configuration loaded.");

    }

    public void reloadConfig()
    {
        //plugin.getConfig().options().copyDefaults(false);
        plugin.reloadConfig();
        FillSettings();
    }

    public void FillSettings()
    {
        NoSpawnMobs = new ArrayList<String>();

        if(plugin.getConfig().get("MaxVillagersPerChunk")!=null)
        {
           MaxVillagersInChunk = plugin.getConfig().getInt("MaxVillagersPerChunk");
            plugin.getLogger().info("Config VillagersPerCheck level has been set to - setting to " + MaxVillagersInChunk);
        }
        else
        {
            plugin.getLogger().info("Config VillagersPerCheck Not found - setting to 20");
            MaxVillagersInChunk = 20;
        }

        if(plugin.getConfig().get("NoSpawnMobs")!=null)
        {
            Configuration cfg = plugin.getConfig();
            NoSpawnMobs = (ArrayList<String>)plugin.getConfig().getList("NoSpawnMobs");
            plugin.getLogger().info("Config NoSpawnMobs now has " + NoSpawnMobs.size() + " mobs.");
        }

        if(plugin.getConfig().get("NoSpawnWorlds")!=null)
        {
            Configuration cfg = plugin.getConfig();
            NoSpawnWorlds = plugin.getConfig().getBoolean("NoSpawnWorlds");
            plugin.getLogger().info("Config NoSpawnWorlds is set to " + NoSpawnWorlds + ".");
        }

        if(plugin.getConfig().get("WorldsList")!=null)
        {
            Configuration cfg = plugin.getConfig();
            NoSpawnWorldsList = (ArrayList<String>)plugin.getConfig().getList("NoSpawnWorldsList");
            for(String worlds:NoSpawnWorldsList) {
                plugin.getLogger().info("No mobs will spawn on: " + worlds);
            }
        }


    }



}
