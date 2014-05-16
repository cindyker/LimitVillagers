package com.minecats.cindyk.limitvillagers;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Created by Cindy on 5/16/14.
 */
public class Config {

    LimitVillagers plugin;
    int MaxVillagersInChunk;

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
        plugin.getConfig().options().copyDefaults(false);
        FillSettings();
    }

    public void FillSettings()
    {
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
    }



}
