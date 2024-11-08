package me.roupen.firstpluginthree;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.events.MythicDamageEvent;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.roupen.firstpluginthree.CraftingRecipes.BasicTools;
import me.roupen.firstpluginthree.PlayerInteractions.*;
import me.roupen.firstpluginthree.PlayerInteractions.RuneForge;
import me.roupen.firstpluginthree.artisan.Consumable;
import me.roupen.firstpluginthree.artisan.CookingRecipes;
import me.roupen.firstpluginthree.commandkit.*;
import me.roupen.firstpluginthree.constantrunnables.actionbardisplay;
import me.roupen.firstpluginthree.magic.spellcasting;
import me.roupen.firstpluginthree.magic.spells;
import me.roupen.firstpluginthree.constantrunnables.weatherforecast;
import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.misc.misc;
import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.utility.ConsumableUtility;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.wands.wand;
import me.roupen.firstpluginthree.weather.WeatherForecast;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
import org.bukkit.event.world.WorldInitEvent;
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
    spellcasting NewSpell = new spellcasting();
    private static FirstPluginThree myPlugin;
    private static BukkitAPIHelper mmhelp = new BukkitAPIHelper();
    public static FirstPluginThree getMyPlugin()
    {
        return myPlugin;
    }
    public static BukkitAPIHelper getMMHelper() {
        return mmhelp;
    }
    private static CookingRecipes cookingrecipes;
    private static BasicTools basictoolrecipes;
    public static ArrayList<Player> PlayersInBossFight = new ArrayList<>();

    public static CookingRecipes getCookingrecipes() {return cookingrecipes;}

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("profile").setExecutor(new profileCMD());
        this.getCommand("weather_report").setExecutor(new weatherCMD());
        this.getCommand("stats").setExecutor(new statsCMD());
        this.getCommand("sethome").setExecutor(new sethomeCMD());
        this.getCommand("home").setExecutor(new homeCMD());
        this.getCommand("party").setExecutor(new partyCMD());

        myPlugin = this;

        BukkitTask Weather_Forecast = new weatherforecast().runTaskTimer(this, 0, 20);
        BukkitTask playeractionbar = new actionbardisplay().runTaskTimer(this, 0L, 5);
        cookingrecipes = new CookingRecipes();
        basictoolrecipes = new BasicTools();
        cookingrecipes.initRecipes();
        basictoolrecipes.initRecipes();

    }
   @EventHandler
   public void onWorldInit(WorldInitEvent event) {
       List<LivingEntity> allMobs = getWorlds().get(0).getLivingEntities();

       for (LivingEntity mob : allMobs) {
           MobStats.giveStatBlock(mob);
           mob.customName(MobUtility.getMobStats(mob).generateName());
       }
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

            stats.setHomeLocation(cfg.getDoubleList("stats.HomeLocation"));
            stats.setSpellbook(cfg.getStringList("stats.SpellBook").toArray(new String[4]));
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
        stats.addMemberToParty(stats.getPlayer());

        PlayerUtility.setPlayerStats(event.getPlayer(), stats);

        WeatherForecast.WeatherReport(event.getPlayer());
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();

        //delete player from all party member's lists
        PlayerStats LeavingMember = PlayerUtility.getPlayerStats(player);
        ArrayList<Player> LeavingMemberOGParty = new ArrayList<>();
        LeavingMemberOGParty.addAll(LeavingMember.getParty());

        for (Player p : LeavingMemberOGParty) {
            if (p != player) {
                PlayerStats PartyMemberStats = PlayerUtility.getPlayerStats(p);
                PartyMemberStats.removeMemberFromParty(player);
                p.sendMessage(Component.text(player.getName() + " has left the party\n", Style.style(NamedTextColor.LIGHT_PURPLE, TextDecoration.ITALIC)));
            }
        }
        PlayerUtility.SavePlayerStats(player);
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Random rd = new Random();
        String[] FlavorTexts = new String[]
                {"took an L", "is weak to damage", "involuntarily returned home"};
        int index = rd.nextInt(FlavorTexts.length);
        event.deathMessage(Component.text(event.getPlayer().getName() + " " + FlavorTexts[index]));


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
    public void onPlayerInteract(PlayerInteractEvent event) throws InvalidMobTypeException {

        //player who clicked
        Player player = event.getPlayer();
        PlayerStats stats = PlayerUtility.getPlayerStats(player);

        //Interaction handlers for each interactable
        RuneForge.Interact(event);
        wand.Interact(event);
        WandCrafting.Interact(event);
        ArtisanRestrictions.Interact(event);
        EliteFight.Interact(event);

        //effects triggered
        if (!(stats.isCastingSpell()) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && wand.IsWand(player.getInventory().getItemInOffHand())) {
            //gets Player's spellbook to cast from
            String[] playerSpellbook = stats.getSpellbook();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && misc.isInteractable(event.getClickedBlock()))
            {return;}
                //tells the game the player's casting a spell
                if (!(playerSpellbook[0] == null || playerSpellbook[1] == null || playerSpellbook[2] == null || playerSpellbook[3] == null)) {
                    stats.setCastingSpell(true);
                    //spell selected based off of context of cast
                    if (player.isSneaking()) {
                        NewSpell.cast(player, playerSpellbook[3]);
                    } else if (player.isSprinting()) {
                        NewSpell.cast(player, playerSpellbook[1]);
                    } else if (!player.isOnGround()) {
                        NewSpell.cast(player, playerSpellbook[2]);
                    } else {
                        NewSpell.cast(player, playerSpellbook[0]);
                    }
                } else {
                    //send error message for incomplete spell book
                    player.sendMessage(Component.text("Cannot cast spell: Make sure you select a spell for each of your slots", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                }

        }
    }
    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {

        if (misc.allArmorEquipmentMats.contains(event.getItem().getType())) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String invtitle = event.getView().title().toString();

        if (event.getInventory().getHolder()==null) {
            if ((event.getClickedInventory()).getHolder() == null && invtitle.contains("content=\"Rune Forge\"")) {
                RuneForge.ClickMenu(event);
                }
            else if ((event.getClickedInventory()).getHolder() == event.getWhoClicked() && event.isShiftClick()) {
                event.setCancelled(true);
            }
            else if ((event.getView().getTopInventory()).getHolder() == null &&
                    (invtitle.contains("content=\"Player Stats\"") || invtitle.contains("content=\"Stat Improvements\"") || invtitle.contains("content=\"Spell Book\""))) {
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
        Entity ent = event.getEntity();
        if (!(ent.hasMetadata("BukkitValues")) && ent instanceof Wither) {
            Location loc = ent.getLocation();
            event.setCancelled(true);
        }
        else if (!(ent.hasMetadata("BukkitValues")) && ent instanceof LivingEntity) {
            MobStats.giveStatBlock((LivingEntity) event.getEntity());
        }
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        boolean DamagerIsBossMob = false;
        if (MobUtility.hasMobStats(event.getDamager().getUniqueId())) {
            MobStats Mstats = MobUtility.getMobStats(event.getDamager());
            if (Mstats.isBoss) {
                DamagerIsBossMob = true;
            }

        }
        if (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof ArmorStand) && !DamagerIsBossMob) {

            //ALL DAMAGE LOGIC FOR PLAYERS TO MOBS AND VICE VERSA (NON-BOSS) HAPPENS HERE

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
                if (event.getDamager() instanceof Slime) {
                    event.setCancelled(false);
                }

                //Makes the health bar match the player's health stat
                if (playerstats.getActiveCurrentHealth() > 0 || !playerstats.getPlayer().isDead()) {
                    player.setHealth(Math.max(0, 20 * playerstats.getActiveCurrentHealth() / playerstats.getActiveMaxHealth()));
                    player.damage(0);
                } else {
                    player.setHealth(0);
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
                        //set aggro
                        if (event.getEntity() instanceof Creature) {
                            Creature mobC = (Creature) mob;
                            mobC.setTarget(player);
                        }
                        //event.getEntity().customName(mobstats.generateName());
                    }
                    event.getDamager().remove();
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
            else {
                event.setCancelled(true);
            }
            event.setCancelled(true);
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

        if (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof ArmorStand)) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                PlayerStats playerstats = PlayerUtility.getPlayerStats(player);

                if (!player.isDead()) { // <<< This is an attempt at fixing environmental-based multiple deaths =============================================================
                    if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {

                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, (int) (Math.min((event.getDamage() / 4), 6))));
                        player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_BREAK, 1, 1);

                        //If player is IN lava
                    } else if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {

                        //damages player by 1% current health true damage per damage tick
                        playerstats.setActiveCurrentHealth(playerstats.getActiveCurrentHealth() - ((int) (playerstats.getActiveMaxHealth() * 0.1)));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 1);

                        //If player is IN fire
                    } else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE) {

                        //damages player by 1% max health true damage per damage tick
                        playerstats.setActiveCurrentHealth(playerstats.getActiveCurrentHealth() - ((int) (playerstats.getActiveMaxHealth() * 0.01)));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 1);

                        //If player is on fire
                    } else if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {

                        //damages player by 3% max health true damage per damage tick
                        playerstats.setActiveCurrentHealth(playerstats.getActiveCurrentHealth() - ((int) (playerstats.getActiveMaxHealth() * 0.03)));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_ON_FIRE, 1, 1);

                        //If player is poisoned
                    } else if (event.getCause() == EntityDamageEvent.DamageCause.POISON) {

                        //damages player by 2% max health true damage per damage tick
                        playerstats.setActiveCurrentHealth((int) (playerstats.getActiveCurrentHealth() - (0.02 * playerstats.getActiveMaxHealth())));
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_HURT, 1, 1);

                        //If player is drowning
                    } else if (event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {

                        //damages player by 10% max health true damage per damage tick
                        playerstats.setActiveCurrentHealth((int) (playerstats.getActiveCurrentHealth() - (0.1 * playerstats.getActiveMaxHealth())));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_DROWN, 1, 1);

                        //Any custom damage sources will obviously be caused by me, so event should be cancelled by default (not in use yet)
                    } else if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT) {

                        //damages player by 3% max health true damage per damage tick
                        playerstats.setActiveCurrentHealth((playerstats.getActiveCurrentHealth() - (0.003 * playerstats.getActiveMaxHealth())));
                        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_HURT, 1, 1);

                        //Any custom damage sources will obviously be caused by me, so event should be cancelled by default (not in use yet)
                    } else if (event.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
                        event.setCancelled(true);
                    }

                    if ((!player.isDead()) && (playerstats.getActiveCurrentHealth() > 0)) {
                        event.setDamage(0.001);
                        //player.setHealth(Math.max(0,Math.min(20, 20 * (playerstats.getActiveCurrentHealth() / playerstats.getActiveMaxHealth()))));
                        //adding this duplicates the player deaths

                    } else {
                        event.setCancelled(true);
                    }
                }
                else {
                    event.setCancelled(true);
                }
            }
            //THIS IS A NEW BLOCK OF CODE TO BLOCK OUT
            else {
                event.setCancelled(true);
            }
        }



    }
    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
        Player p = event.getPlayer();

        ItemStack consumedItem = event.getItem();
        Consumable c = new Consumable(event.getItem());

       if (getCookingrecipes().getItems().containsValue(consumedItem)) {
           PlayerStats pStats = PlayerUtility.getPlayerStats(p);
           if (!pStats.hasConsumedItem()) {
               pStats.setConsumingItem(true);
               c.ConsumeItem(p);
               c.runTaskTimer(this, 1L, 1L);
               consumedItem.setAmount(consumedItem.getAmount() - 1);
               p.getInventory().setItem(event.getHand(), consumedItem);
           }
           event.setCancelled(true);
       }
    }
    //MythicMobs Event Listeners
    @EventHandler
    public void onMythicMobEntitySpawn(MythicMobSpawnEvent event) {
        ActiveMob ent = event.getMob();
        if (ent.getType().toString().equals("MythicMob{AbyssWatcherTest}")) {
            LivingEntity bossEnt = (LivingEntity) event.getEntity();
            MobStats.giveBossStatBlock(bossEnt, ent.getType().toString());
        }
    }
    @EventHandler
    public void onMythicMobDamage(MythicDamageEvent event) {
        if (PlayerUtility.hasPlayerStats(event.getTarget().getUniqueId())) {

            //the "damage" dealt by the damage skill is used as the factor towards the damage taken by the connecting of the skill
            double damageMult = event.getDamageMetadata().getAmount();

            Player p = (Player) event.getTarget().getBukkitEntity();
            PlayerStats Pstats = PlayerUtility.getPlayerStats(p);
            MobStats Mstats = MobUtility.getMobStats(event.getCaster().getEntity().getBukkitEntity());
            Pstats.damage(Mstats, damageMult);
            p.damage(0);

        }
    }
    @EventHandler
    public void onMythicEntityDeath(MythicMobDeathEvent event) {
        EliteFight.EndBossFight(event);
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