package com.minecats.cindyk.limitvillagers;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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

        if(ev.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER){
            getLogger().info("Spawner is spawning mob of: " + ev.getEntity().getType().toString() + " or name " + ev.getEntity().getName());
            for(String mob:config.NoSpawnMobs) {
                if (ev.getEntity().getType().toString().equals(mob)){
                    ev.setCancelled(true);
                }
            }
        }

        if(config.NoSpawnWorlds){
            //Lets not block pets. Hopefully they are listed as custom.
            if(ev.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) return;
            for(String world:config.NoSpawnWorldsList){
                if (ev.getEntity().getLocation().getWorld().getName().compareToIgnoreCase(world)==0){
                    ev.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event)
    {
        if (event.getAction()== Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock().getType() == Material.MOB_SPAWNER){
                plugin.getLogger().info("Cancelling Right Click Event on Mob Spawner");
                event.setCancelled(true);
            }
        }
    }

//
//    private void blockPlace(HangingPlaceEvent event)
//    {
//
//        Block block = event.getBlock();
//        World world = block.getWorld();
//        int type = block.getTypeId();
//        Player player = event.getPlayer();
//        ItemStack item = player.getItemInHand();
//
//
//        getLogger().info("HangingPlace - " + block.getType().name());
//        if ((item.getType() == Material.MINECART
//                || item.getType() == Material.POWERED_MINECART
//                || item.getType() == Material.STORAGE_MINECART
//                || item.getType() == Material.EXPLOSIVE_MINECART
//                || item.getType() == Material.HOPPER_MINECART)) {
//
//            //COUNT!
//            int MineCount = 0;
//            for(Entity mine: event.getPlayer().getLocation().getChunk().getEntities())
//            {
//                if((mine.getType() == EntityType.MINECART
//                        || mine.getType() == EntityType.MINECART_TNT
//                        || mine.getType() == EntityType.MINECART_CHEST
//                        || mine.getType() == EntityType.MINECART_HOPPER
//                        || mine.getType() == EntityType.MINECART_COMMAND)) {
//                    MineCount++;
//                    getLogger().info("Minecart Count - " + MineCount);
//                }
//
//            }
//
//            if(MineCount > 10)
//            {
//                player.sendMessage(ChatColor.DARK_RED + "You can not place vehicles here. Too many in the area.");
//                event.setCancelled(true);
//            }
//            return;
//
//        }
//    }

    @EventHandler
    private void handleVehicleSpawn(VehicleCreateEvent event) {


        Vehicle vec = event.getVehicle();
        World world = event.getVehicle().getWorld();

        // int type = block.getTypeId();
        //Player player = event.getPlayer();

        //ItemStack item = player.getItemInHand();

        getLogger().info("CreateEvent - " + vec.getType().name());

        if((vec.getType() == EntityType.MINECART
                || vec.getType() == EntityType.MINECART_TNT
                || vec.getType() == EntityType.MINECART_CHEST
                || vec.getType() == EntityType.MINECART_HOPPER
                || vec.getType() == EntityType.MINECART_COMMAND))  {

            //COUNT!
            int MineCount = 0;
            for(Entity mine: vec.getLocation().getChunk().getEntities())
            {
                if((mine.getType() == EntityType.MINECART
                        || mine.getType() == EntityType.MINECART_TNT
                        || mine.getType() == EntityType.MINECART_CHEST
                        || mine.getType() == EntityType.MINECART_HOPPER
                        || mine.getType() == EntityType.MINECART_COMMAND)) {
                    MineCount++;

                }

                if(MineCount > 10)
                {
                    getLogger().info("Removing Minecarts - Count - " + MineCount);
                    mine.remove();
                }
            }

                if(MineCount > 10)
                {
                    vec.remove();
                }
                return;
            }

    }

//    @EventHandler
//    private void handleVehicleSomething(VehicleMoveEvent event)
//    {
//        Vehicle vec = event.getVehicle();
//        World world = event.getVehicle().getWorld();
//
//        // int type = block.getTypeId();
//        //Player player = event.getPlayer();
//
//        //ItemStack item = player.getItemInHand();
//
//        getLogger().info("VehicleMoveEvent - " + vec.getType().name());
//
//        if((vec.getType() == EntityType.MINECART
//                || vec.getType() == EntityType.MINECART_TNT
//                || vec.getType() == EntityType.MINECART_CHEST
//                || vec.getType() == EntityType.MINECART_HOPPER
//                || vec.getType() == EntityType.MINECART_COMMAND))  {
//
//            //COUNT!
//            int MineCount = 0;
//            for(Entity mine: vec.getLocation().getChunk().getEntities())
//            {
//                if((mine.getType() == EntityType.MINECART
//                        || mine.getType() == EntityType.MINECART_TNT
//                        || mine.getType() == EntityType.MINECART_CHEST
//                        || mine.getType() == EntityType.MINECART_HOPPER
//                        || mine.getType() == EntityType.MINECART_COMMAND)) {
//                    MineCount++;
//
//                }
//
//                if(MineCount > 10)
//                {
//                    getLogger().info("Removing Minecarts Move - Count - " + MineCount);
//                    mine.remove();
//                }
//            }
//
//            if(MineCount > 10)
//            {
//                vec.remove();
//            }
//            return;
//        }
//
//    }

    @EventHandler
    private void handleVehicleMove(VehicleBlockCollisionEvent event)
    {
        Vehicle vec = event.getVehicle();
        World world = event.getVehicle().getWorld();

        // int type = block.getTypeId();
        //Player player = event.getPlayer();

        //ItemStack item = player.getItemInHand();

       // getLogger().info("VehicleBlockCollisionEvent - " + vec.getType().name());

        if((vec.getType() == EntityType.MINECART
                || vec.getType() == EntityType.MINECART_TNT
                || vec.getType() == EntityType.MINECART_CHEST
                || vec.getType() == EntityType.MINECART_HOPPER
                || vec.getType() == EntityType.MINECART_COMMAND
                || vec.getType() == EntityType.MINECART_FURNACE))  {

            //COUNT!
            int MineCount = 0;
            for(Entity mine: vec.getLocation().getChunk().getEntities())
            {
                if((mine.getType() == EntityType.MINECART
                        || mine.getType() == EntityType.MINECART_TNT
                        || mine.getType() == EntityType.MINECART_CHEST
                        || mine.getType() == EntityType.MINECART_HOPPER
                        || mine.getType() == EntityType.MINECART_COMMAND
                        || vec.getType() == EntityType.MINECART_FURNACE)) {
                    MineCount++;

                }

                if(MineCount > 10)
                {
                    getLogger().info("Removing Minecarts Block Collision - Count - " + MineCount);
                    mine.remove();
                }
            }

            if(MineCount > 10)
            {
                vec.remove();
            }
            return;
        }


    }

    @EventHandler
    private void handleVehicleMove(VehicleEntityCollisionEvent event)
    {
        Vehicle vec = event.getVehicle();
        World world = event.getVehicle().getWorld();

        // int type = block.getTypeId();
        //Player player = event.getPlayer();

        //ItemStack item = player.getItemInHand();

    //    getLogger().info("VehicleEntityCollisionEvent - " + vec.getType().name());

        if((vec.getType() == EntityType.MINECART
                || vec.getType() == EntityType.MINECART_TNT
                || vec.getType() == EntityType.MINECART_CHEST
                || vec.getType() == EntityType.MINECART_HOPPER
                || vec.getType() == EntityType.MINECART_COMMAND
                || vec.getType() == EntityType.MINECART_FURNACE))  {

            //COUNT!
            int MineCount = 0;
            for(Entity mine: vec.getLocation().getChunk().getEntities())
            {
                if((mine.getType() == EntityType.MINECART
                        || mine.getType() == EntityType.MINECART_TNT
                        || mine.getType() == EntityType.MINECART_CHEST
                        || mine.getType() == EntityType.MINECART_HOPPER
                        || mine.getType() == EntityType.MINECART_COMMAND
                        || vec.getType() == EntityType.MINECART_FURNACE)) {
                    MineCount++;

                }

                if(MineCount > 10)
                {
                    getLogger().info("Removing Minecarts Entity Collision - Count - " + MineCount);
                    mine.remove();
                }
            }

            if(MineCount > 10)
            {
                vec.remove();
            }
            return;
        }


    }



    @EventHandler
    private void handleBlockDispense(BlockDispenseEvent event) {


        ItemStack item = event.getItem();
        World world = event.getBlock().getWorld();
       // int type = block.getTypeId();
        //Player player = event.getPlayer();

        //ItemStack item = player.getItemInHand();


        if ((item.getType() == Material.MINECART
                || item.getType() == Material.POWERED_MINECART
                || item.getType() == Material.STORAGE_MINECART
                || item.getType() == Material.EXPLOSIVE_MINECART
                || item.getType() == Material.HOPPER_MINECART
                )) {

                //COUNT!
                int MineCount = 0;
                for(Entity mine: event.getBlock().getLocation().getChunk().getEntities())
                {
                    if((mine.getType() == EntityType.MINECART
                            || mine.getType() == EntityType.MINECART_TNT
                            || mine.getType() == EntityType.MINECART_CHEST
                            || mine.getType() == EntityType.MINECART_HOPPER
                            || mine.getType() == EntityType.MINECART_COMMAND
                            || mine.getType() == EntityType.MINECART_FURNACE)){
                       MineCount++;

                    }
                }
                if(MineCount > 10)
                {
                    getLogger().info(" BlockDispenseEvent - Preventing Minecarts " + MineCount);
                    event.setCancelled(true);
                }
                return;

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
