package me.roupen.firstpluginthree;

import me.roupen.firstpluginthree.PlayerInteractions.EquipmentWorkbench;
import me.roupen.firstpluginthree.PlayerInteractions.*;
import me.roupen.firstpluginthree.PlayerInteractions.RuneForge;
import me.roupen.firstpluginthree.commandkit.profileCMD;
import me.roupen.firstpluginthree.constantrunnables.actionbardisplay;
import me.roupen.firstpluginthree.constantrunnables.weatherforecast;
import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;

import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.playerequipment.Rune;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.weather.WeatherForecast;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.bukkit.Bukkit.*;

public final class FirstPluginThree extends JavaPlugin implements Listener {

    BukkitTask playeractionbar;
    BukkitTask Weather_Forecast;
    private static FirstPluginThree myPlugin;
    private void savePlayerStats(Player player) {
        PlayerStats stats = PlayerUtility.getPlayerStats(player);
        File f = new File(PlayerUtility.getFolderPath(player) + "/general.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        cfg.set("stats.Vitality", stats.getVitality());
        cfg.set("stats.Resilience", stats.getResilience());
        cfg.set("stats.Intelligence", stats.getIntelligence());

        cfg.set("stats.Strength", stats.getStrength());
        cfg.set("stats.Dexterity", stats.getDexterity());
        cfg.set("stats.Wisdom", stats.getWisdom());

        cfg.set("stats.Experience", stats.getExperience());
        cfg.set("stats.Level", stats.getLevel());
        cfg.set("stats.SkillPoints", stats.getSkillPoints());

        try {
            cfg.save(f);
        } catch (IOException e){
            e.printStackTrace();
        }

        PlayerUtility.setPlayerStats(player, null);
    }

    public static FirstPluginThree getMyPlugin()
    {
        return myPlugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("profile").setExecutor(new profileCMD());
        myPlugin = this;

        BukkitTask Weather_Forecast = new weatherforecast().runTaskTimer(this, 0, 20);
        BukkitTask playeractionbar = new actionbardisplay().runTaskTimer(this, 0L, 5);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        //Handles giving all mob's stats back to them

        //Handles giving back the player their stats
        PlayerStats stats = new PlayerStats();
        File f =new File(PlayerUtility.getFolderPath(event.getPlayer()) + "/general.yml");

        if(f.exists()) //the player's config file exists
        {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            stats.setVitality(cfg.getInt("stats.Vitality"));
            stats.setResilience(cfg.getInt("stats.Resilience"));
            stats.setIntelligence(cfg.getInt("stats.Intelligence"));

            stats.setStrength(cfg.getInt("stats.Strength"));
            stats.setDexterity(cfg.getInt("stats.Strength"));
            stats.setWisdom(cfg.getInt("stats.Strength"));

            stats.setExperience(cfg.getInt("stats.Experience"));
            stats.setLevel(cfg.getInt("stats.Level"));
            stats.setSkillPoints(cfg.getInt("stats.SkillPoints"));

            stats.setActiveCurrentHealth(cfg.getDouble("stats.CurrentHealth"));
            stats.setActiveCurrentMana(cfg.getDouble("stats.CurrentMana"));
            stats.setActiveCurrentStamina(cfg.getDouble("stats.CurrentStamina"));

        }
        else //player's file does not exist
        {
            stats.setVitality(1);
            stats.setResilience(1);
            stats.setIntelligence(1);

            stats.setStrength(1);
            stats.setDexterity(1);
            stats.setWisdom(1);

            stats.setExperience(0);
            stats.setLevel(1);
            stats.setSkillPoints(0);

            stats.setActiveMaxHealth(100 + (stats.getVitality() - 1) * 5);
            stats.setActiveCurrentHealth(stats.getActiveMaxHealth());

            stats.setActiveMaxMana(20 + (stats.getIntelligence() - 1) * 8);
            stats.setActiveCurrentMana(stats.getActiveMaxMana());
        }

        stats.setActiveMaxHealth(100 + (stats.getVitality() - 1) * 5);
        stats.setActiveDefense(stats.getResilience() * 5);
        stats.setActiveMaxMana(20 + (stats.getIntelligence() - 1) * 8);

        stats.setPlayer(event.getPlayer());

        PlayerUtility.setPlayerStats(event.getPlayer(), stats);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        PlayerUtility.SavePlayerStats(event.getPlayer());
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event){
        LivingEntity e = event.getEntity();
        LivingEntity k = e.getKiller();

        if (k!=null && k instanceof Player)
        {
            Player player = (Player) k;

            PlayerStats stats = PlayerUtility.getPlayerStats(player);


        }

    }
    //menu inventory for player
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
        {
        //trigger abilities based on itemStack held in main hand

        //player who clicked
        Player player = event.getPlayer();

        //list of items I look for
        ItemStack compass = new ItemStack(Material.COMPASS, 1);

        RuneForge.Interact(event);

        //effects triggered
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getInventory().getItemInMainHand().equals(compass)) {
            //testing purposes
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        String invtitle = event.getView().title().toString();

        if (event.getInventory().getHolder()==null) {
            if ((event.getClickedInventory()).getHolder() == null && invtitle.contains("content=\"Rune Forge\""))
                {RuneForge.ClickMenu(event);}
            else if ((event.getClickedInventory()).getHolder() == null && (invtitle.contains("content=\"Player Stats\"") || invtitle.contains("content=\"Stat Improvements\"")))
                {ProfileMenu.ClickMenu(event);}
    //      else if ((event.getClickedInventory()).getHolder() == null && invtitle.contains("content=\"Equipment Workbench\""))
    //          {EquipmentWorkbench.ClickMenu(event);}
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        RuneForge.EarlyExit(event);
    }
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event)
    {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player) && !(entity instanceof Item) && !(entity instanceof Arrow) && !(entity instanceof ThrownPotion))
        {
            Entity mob = event.getEntity();
            MobStats stats = new MobStats(mob, WeatherForecast.getWeather(event));
            MobUtility.setMobStats(mob, stats);
            event.getEntity().setCustomNameVisible(true);
            event.getEntity().customName(stats.generateName());
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        //ALL DAMAGE LOGIC FOR PLAYERS TO MOBS AND VICE VERSA HAPPENS HERE
        if (event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))
        {
            Player player = (Player) event.getDamager();
            PlayerStats playerstats = PlayerUtility.getPlayerStats(player);

            LivingEntity mob = (LivingEntity) event.getEntity();
            MobStats mobstats = MobUtility.getMobStats(mob);

            if (mobstats.damage(playerstats, playerstats.getMultihit()))
            {
                //damage animation
                mob.damage(0);
            }
            if (mobstats.getHealth() <= 0)
            {
                //"kills" the mob once 0 health is hit and awards EXP and drops
                mob.setHealth(0);
                playerstats.gainExperience(5000, player);
                player.getInventory().addItem(PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateRandomEquipment(mob)));
                event.getEntity().customName(Component.text("+" + 5000 + "XP" + " - " + player.getName()));
            }
            else
            {
                event.getEntity().customName(mobstats.generateName());
            }

        }
        else if (!(event.getDamager() instanceof Player) && event.getEntity() instanceof Player)
        {

            Player player = (Player) event.getEntity();
            PlayerStats playerstats = PlayerUtility.getPlayerStats(player);

            if (event.getDamager() instanceof Arrow)
            {
                MobStats shooterstats = MobUtility.getMobStats((LivingEntity) ((Arrow) event.getDamager()).getShooter());
                player.sendMessage("ranged hit");

                playerstats.damage(shooterstats);

                event.getDamager().remove();

            }
            else
            {
                LivingEntity mob = (LivingEntity) event.getDamager();
                MobStats mobstats = MobUtility.getMobStats(mob);

                player.sendMessage("melee hit");

                playerstats.damage(mobstats);
            }

            //Makes the health bar match the player's health stat
            if (playerstats.getActiveCurrentHealth() > 0) {
                player.setHealth(20 * playerstats.getActiveCurrentHealth() / playerstats.getActiveMaxHealth());
                player.damage(0);
            } else {
                player.setHealth(0);
                playerstats.setActiveCurrentHealth(playerstats.getActiveMaxHealth());
            }
        }
        else if (event.getDamager() instanceof Arrow && (((Arrow) event.getDamager()).getShooter() instanceof Player))
        {
            LivingEntity mob = (LivingEntity) event.getEntity();
            MobStats mobstats = MobUtility.getMobStats(mob);

            Player player = (Player) ((Arrow) event.getDamager()).getShooter();
            PlayerStats playerstats = PlayerUtility.getPlayerStats(player);

            if (mobstats.ranged_damage(playerstats, playerstats.getMultihit(), event.getDamager().getVelocity().length()))
            {
                //damage animation
                mob.damage(0);
                event.getDamager().remove();
                event.getEntity().customName(mobstats.generateName());

            }else{
                player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
            }

            if (mobstats.getHealth() <= 0)
            {
                //"kills" the mob once 0 health is hit and awards EXP
                mob.setHealth(0);
                playerstats.gainExperience(5000, player);
                player.getInventory().addItem(PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateRandomEquipment(mob)));
                event.getEntity().customName(Component.text("+" + 5000 + "XP" + " - " + player.getName()));
            }
        }

        //Damage between mobs cannot happen
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            Player player = (Player) event.getEntity();
            PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
            float force = event.getForce();
            if (force < 0.53) {
                force = 0.5f;
            }
            else if (force < 0.81) {
                force = 0.75f;
            }
            else {
                force = 1.0f;
            }
            if ((force * playerstats.getStaminaCost()) <= playerstats.getActiveCurrentStamina())
            {
                playerstats.useStamina(force * playerstats.getStaminaCost());
            }else{
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL)
        {
            Player player = (Player) event.getEntity();
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, (int) (Math.min((event.getDamage() / 4), 6))));
            player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 1,1);
            event.setCancelled(true);
        }
        else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            Player player = (Player) event.getEntity();
            PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
            playerstats.setActiveCurrentHealth( (int) (playerstats.getActiveCurrentHealth() * 0.9));
            event.setCancelled(true);
        }
        else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
            Player player = (Player) event.getEntity();
            PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
            playerstats.setActiveCurrentHealth( (int) (playerstats.getActiveCurrentHealth() * 0.95));
            event.setCancelled(true);
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM && !(event.getEntity() instanceof Player))
        {
            event.setCancelled(true);

        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        List<Player> players = (List<Player>) getOnlinePlayers();

        for (int i = 0; i < players.size(); i++)
        {
            PlayerUtility.SavePlayerStats(players.get(i));
        }
    }
}