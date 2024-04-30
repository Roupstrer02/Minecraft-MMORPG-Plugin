package me.roupen.firstpluginthree;

//===
//An attempt at preventing NoClassDefFoundError randomly appearing at runtime (i don't think it's going to work)
//===
import me.roupen.firstpluginthree.CraftingRecipes.BasicTools;
import me.roupen.firstpluginthree.PlayerInteractions.*;
import me.roupen.firstpluginthree.PlayerInteractions.RuneForge;
import me.roupen.firstpluginthree.artisan.CookingRecipes;
import me.roupen.firstpluginthree.commandkit.profileCMD;
import me.roupen.firstpluginthree.commandkit.statsCMD;
import me.roupen.firstpluginthree.commandkit.weatherCMD;
import me.roupen.firstpluginthree.constantrunnables.actionbardisplay;
import me.roupen.firstpluginthree.constantrunnables.spellcasting;
import me.roupen.firstpluginthree.constantrunnables.spells;
import me.roupen.firstpluginthree.constantrunnables.weatherforecast;
import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.wands.wand;
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
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

import static org.bukkit.Bukkit.*;

public final class FirstPluginThree extends JavaPlugin implements Listener {

    //Attempt at fixing NoClassDefFoundError
    //=============================================================================
    ProfileMenu dummy_x = new ProfileMenu();
    spells dummy_y = new spells();
    //=============================================================================

    BukkitTask playeractionbar;
    BukkitTask Weather_Forecast;
    private static FirstPluginThree myPlugin;
    public static FirstPluginThree getMyPlugin()
    {
        return myPlugin;
    }
    private static CookingRecipes cookingrecipes;
    private static BasicTools basictoolrecipes;

    public static CookingRecipes getCookingrecipes() {return cookingrecipes;}

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("profile").setExecutor(new profileCMD());
        this.getCommand("weather_report").setExecutor(new weatherCMD());
        this.getCommand("stats").setExecutor(new statsCMD());
        myPlugin = this;

        BukkitTask Weather_Forecast = new weatherforecast().runTaskTimer(this, 0, 20);
        BukkitTask playeractionbar = new actionbardisplay().runTaskTimer(this, 0L, 5);
        cookingrecipes = new CookingRecipes();
        basictoolrecipes = new BasicTools();
        cookingrecipes.initRecipes();
        basictoolrecipes.initRecipes();

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
            stats.setDexterity(cfg.getInt("stats.Dexterity"));
            stats.setWisdom(cfg.getInt("stats.Wisdom"));

            stats.setArtisan(cfg.getInt("stats.Artisan"));

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
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.deathMessage(Component.text(event.getPlayer().getName() + " took an L"));
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

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        PlayerStats stats = PlayerUtility.getPlayerStats(event.getPlayer());
        stats.respawnStatReset();

    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        //trigger abilities based on itemStack held in main hand

        //player who clicked
        Player player = event.getPlayer();
        PlayerStats stats = PlayerUtility.getPlayerStats(player);


        RuneForge.Interact(event);
        wand.Interact(event);
        WandCrafting.Interact(event);
        ArtisanRestrictions.Interact(event);

        //effects triggered
        if (!(stats.isCastingSpell()) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && wand.IsWand(player.getInventory().getItemInOffHand())) {
            //testing purposes
            stats.setCastingSpell(true);
            if (player.isSneaking()) {
                spellcasting.cast(player, "Chrono Thief");
            }
            else if (!player.isOnGround()) {
                spellcasting.cast(player, "Flame Booster");
            }
            else if (player.isSprinting()) {
                spellcasting.cast(player, "Flame Dash");
            }
            else {
                spellcasting.cast(player, "Fireball");
            }
        }
    }
    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        Material itemMat = event.getItem().getType();

        if (itemMat == Material.WOODEN_SWORD) {
            event.setCancelled(true);
        }
        else if (itemMat == Material.SHIELD) {

        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String invtitle = event.getView().title().toString();

        if (event.getInventory().getHolder()==null) {
            if ((event.getClickedInventory()).getHolder() == null && invtitle.contains("content=\"Rune Forge\"")) {
                RuneForge.ClickMenu(event);
                }
            else if ((event.getView().getTopInventory()).getHolder() == null && (invtitle.contains("content=\"Player Stats\"") || invtitle.contains("content=\"Stat Improvements\""))) {
                    ProfileMenu.ClickMenu(event);
                    event.setCancelled(true);
                }
        }

        ArtisanRestrictions.ClickMenu(event);

    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        RuneForge.EarlyExit(event);
    }
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!((entity instanceof Player) || !(entity instanceof LivingEntity)))
        {
            Entity mob = event.getEntity();
            MobStats stats = new MobStats(mob, WeatherForecast.getWeather(event));
            MobUtility.setMobStats(mob, stats);
            event.getEntity().setCustomNameVisible(true);
            event.getEntity().customName(stats.generateName());
        }
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            //ALL DAMAGE LOGIC FOR PLAYERS TO MOBS AND VICE VERSA HAPPENS HERE

            //if player melee attacks the mob
            if (event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))
            {
                Player player = (Player) event.getDamager();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
                LivingEntity mob = (LivingEntity) event.getEntity();
                MobStats mobstats = MobUtility.getMobStats(mob);

                if (mobstats.damage(playerstats, playerstats.getMultihit()))
                {
                    //greatsword knockback
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.getType() != Material.AIR && PlayerEquipment.ItemToEquipment(item).isGreatSword())
                        mob.setVelocity(player.getLocation().getDirection().multiply(0.75).add(new Vector(0, 0.25, 0)));

                    //mob re-aggro
                    if (event.getEntity() instanceof Creature) {
                        Creature mobC = (Creature) mob;
                        mobC.setTarget(player);
                    }
                }

                //mob death check
                if (mobstats.getHealth() <= 0)
                {
                    //"kills" the mob once 0 health is hit and awards EXP and drops
                    mob.setHealth(0);
                    mobstats.KillReward(playerstats);
                }
                else
                {
                    event.getEntity().customName(mobstats.generateName());
                }

            }
            //if mob hits player
            else if (!(event.getDamager() instanceof Player) && event.getEntity() instanceof Player)
            {

                Player player = (Player) event.getEntity();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);

                //if the damage source is a thrown potion (witches)
                if (event.getDamager() instanceof Projectile) {
                    MobStats shooterstats = MobUtility.getMobStats((LivingEntity) ((Projectile) event.getDamager()).getShooter());

                    playerstats.damage(shooterstats);
                    if (event.getDamager() instanceof Arrow)
                    {event.getDamager().remove();}
                }
                //standard melee attack + creeper explosions
                else
                {
                    LivingEntity mob = (LivingEntity) event.getDamager();
                    MobStats mobstats = MobUtility.getMobStats(mob);

                    playerstats.damage(mobstats);

                }

                //Makes the health bar match the player's health stat
                if (playerstats.getActiveCurrentHealth() > 0) {
                    player.setHealth(20 * playerstats.getActiveCurrentHealth() / playerstats.getActiveMaxHealth());
                    player.damage(0);
                } else {
                    player.setHealth(0);
                    playerstats.setActiveCurrentHealth(playerstats.getActiveMaxHealth());
                    playerstats.setActiveCurrentMana(playerstats.getActiveMaxMana());
                }
            }
            //if player shoots a projectile (Arrow, Fireball redirection, potion, etc...)
            else if (event.getDamager() instanceof Projectile) {
                if ((((Projectile) event.getDamager()).getShooter() instanceof Player) && !(event.getEntity() instanceof Player))
                {
                    LivingEntity mob = (LivingEntity) event.getEntity();
                    MobStats mobstats = MobUtility.getMobStats(mob);

                    Player player = (Player) ((Projectile) event.getDamager()).getShooter();
                    PlayerStats playerstats = PlayerUtility.getPlayerStats(player);

                    if (mobstats.ranged_damage(playerstats, playerstats.getMultihit(), event.getDamager().getVelocity().length()))
                    {
                        //damage animation
                        mob.damage(0);
                        if (event.getEntity() instanceof Creature) {
                            Creature mobC = (Creature) mob;
                            mobC.setTarget(player);
                        }
                        mob.setLastDamageCause(event);
                        event.getDamager().remove();
                        event.getEntity().customName(mobstats.generateName());

                    }else{
                        player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                    }

                    if (mobstats.getHealth() <= 0)
                    {
                        //"kills" the mob once 0 health is hit and awards EXP
                        mob.damage(1000000, player);

                        mobstats.KillReward(playerstats);
                    }
                }
            }

            //Iron Golem specific behaviour
            else if ((event.getDamager() instanceof IronGolem) && (event.getEntity() instanceof Monster)) {
                LivingEntity attackedMob = (LivingEntity) event.getEntity();
                MobStats attacker = MobUtility.getMobStats(event.getDamager());
                MobStats attacked = MobUtility.getMobStats(event.getEntity());

                attacked.mobDamage(attacker);
                if (attacked.getHealth() <= 0) {
                    attackedMob.setHealth(0);
                }else{
                    event.getEntity().customName(attacked.generateName());
                }
            }

            //Damage between mobs cannot happen
            event.setCancelled(true);
        }
        //damage from non-living sources is cancelled
        else {
            event.setCancelled(true);
        }
        //damage animation for player hits + damage cause tracking
        if (event.getDamager() instanceof Player) {
            event.setDamage(0);
            event.setCancelled(false);
        }
    }
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
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
    public void onEntityDamage(EntityDamageEvent event) {
        //All following logic applies to Non-Items
        if (!(event.getEntity() instanceof Item)) {
            if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Player player = (Player) event.getEntity();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, (int) (Math.min((event.getDamage() / 4), 6))));
                player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 1, 1);

                //If player is IN lava
            } else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                Player player = (Player) event.getEntity();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
                //damages player by 1% current health true damage per damage tick
                playerstats.setActiveCurrentHealth(playerstats.getActiveCurrentHealth() - ((int) (playerstats.getActiveMaxHealth() * 0.1)));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 1);

                //If player is IN fire
            } else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                Player player = (Player) event.getEntity();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
                //damages player by 1% max health true damage per damage tick
                playerstats.setActiveCurrentHealth(playerstats.getActiveCurrentHealth() - ((int) (playerstats.getActiveMaxHealth() * 0.01)));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 1);

                //If player is on fire
            } else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                Player player = (Player) event.getEntity();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
                //damages player by 3% max health true damage per damage tick
                playerstats.setActiveCurrentHealth(playerstats.getActiveCurrentHealth() - ((int) (playerstats.getActiveMaxHealth() * 0.03)));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 1);

                //If player is poisoned
            } else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.POISON) {
                Player player = (Player) event.getEntity();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
                //damages player by 2% max health true damage per damage tick
                playerstats.setActiveCurrentHealth((int) (playerstats.getActiveCurrentHealth() - (0.02 * playerstats.getActiveMaxHealth())));
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_HURT, 1, 1);

                //If player is drowning
            } else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                Player player = (Player) event.getEntity();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
                //damages player by 10% max health true damage per damage tick
                playerstats.setActiveCurrentHealth((int) (playerstats.getActiveCurrentHealth() - (0.1 * playerstats.getActiveMaxHealth())));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_DROWN, 1, 1);

                //Any custom damage sources will obviously be caused by me, so event should be cancelled by default (not in use yet)
            } else if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                Player player = (Player) event.getEntity();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);
                //damages player by 3% max health true damage per damage tick
                playerstats.setActiveCurrentHealth((playerstats.getActiveCurrentHealth() - (0.003 * playerstats.getActiveMaxHealth())));
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_HURT, 1, 1);

                //Any custom damage sources will obviously be caused by me, so event should be cancelled by default (not in use yet)
            }
            else if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
                event.setCancelled(true);
            }
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
        playeractionbar.cancel();
    }
}