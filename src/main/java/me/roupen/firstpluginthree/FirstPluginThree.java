package me.roupen.firstpluginthree;

import me.roupen.firstpluginthree.commandkit.profileCMD;
import me.roupen.firstpluginthree.constantrunnables.actionbardisplay;
import me.roupen.firstpluginthree.customgui.GuiUtility;
import me.roupen.firstpluginthree.data.PlayerStats;

import me.roupen.firstpluginthree.utility.PlayerUtility;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class FirstPluginThree extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("profile").setExecutor(new profileCMD());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
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
        }

        stats.setActiveMaxHealth(100 + (stats.getVitality() - 1) * 5);
        stats.setActiveCurrentHealth(stats.getActiveMaxHealth()); //To be changed to avoid combat logging exploit (full refill)
        stats.setActiveDefense(stats.getResilience() * 5);
        stats.setActiveMaxMana(20 + (stats.getIntelligence() - 1) * 8);
        stats.setActiveCurrentMana(stats.getActiveMaxMana()); //To be changed to avoid combat logging exploit (full refill)

        PlayerUtility.setPlayerStats(event.getPlayer(), stats);
        BukkitTask task = new actionbardisplay(event.getPlayer()).runTaskTimer(this, 20, 10);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        //ONLY WORKS IF PLAYER QUITS WILLINGLY, GETTING KICKED DOES **NOT** SAVE PROGRESS -----> FIND THE OTHER EVENT FOR KICKING/SERVER CLOSING ON PLAYER TO SAVE PROGRESS
        PlayerStats stats = PlayerUtility.getPlayerStats(event.getPlayer());
        File f = new File(PlayerUtility.getFolderPath(event.getPlayer()) + "/general.yml");
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


        try { cfg.save(f); } catch (IOException e){ e.printStackTrace(); }
        PlayerUtility.setPlayerStats(event.getPlayer(), null);
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent event){
        LivingEntity e = event.getEntity();
        LivingEntity k = e.getKiller();

        if (k!=null && k instanceof Player)
        {
            Player player = (Player) k;

            PlayerStats stats = PlayerUtility.getPlayerStats(player);
            stats.gainExperience(5000, player);

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

        //effects triggered
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.getInventory().getItemInMainHand().equals(compass)) {
            //use abilities
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        PlayerStats stats = PlayerUtility.getPlayerStats(player);

        String invtitle = event.getView().title().toString();

        if (event.getInventory().getHolder()==null) {

            //when the player clicks in the "Player stats" menu
            if (Objects.requireNonNull(event.getCurrentItem()).getType() == Material.DIAMOND && invtitle.contains("content=\"Player Stats\"")) //has to be a better way to discern what menu I'm in (can't find a way to get the name of the inventory)
            {

                player.closeInventory();
                GuiUtility.CreateUpgradeGui(player);

            }
            //when the player clicks in the "Stat Improvements" menu
            else if (invtitle.contains("content=\"Stat Improvements\""))
            {
                if (stats.getSkillPoints() > 0) {
                    if (event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE) {
                        stats.addVitality(1);
                        stats.addSkillPoints(-1);
                        GuiUtility.CreateUpgradeGui(player);
                    }
                    if (event.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE) {
                        stats.addResilience(1);
                        stats.addSkillPoints(-1);
                        GuiUtility.CreateUpgradeGui(player);
                    }
                    if (event.getCurrentItem().getType() == Material.BLUE_STAINED_GLASS_PANE) {
                        stats.addIntelligence(1);
                        stats.addSkillPoints(-1);
                        GuiUtility.CreateUpgradeGui(player);
                    }
                    if (event.getCurrentItem().getType() == Material.ORANGE_STAINED_GLASS_PANE) {
                        stats.addStrength(1);
                        stats.addSkillPoints(-1);
                        GuiUtility.CreateUpgradeGui(player);
                    }
                    if (event.getCurrentItem().getType() == Material.CYAN_STAINED_GLASS_PANE) {
                        stats.addWisdom(1);
                        stats.addSkillPoints(-1);
                        GuiUtility.CreateUpgradeGui(player);
                    }
                    if (event.getCurrentItem().getType() == Material.YELLOW_STAINED_GLASS_PANE) {
                        stats.addDexterity(1);
                        stats.addSkillPoints(-1);
                        GuiUtility.CreateUpgradeGui(player);
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}