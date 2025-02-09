package me.roupen.firstpluginthree.data;

import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.weather.WeatherForecast;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Particle;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Random;

public class MobStats {

    private double Health;
    private double MaxHealth;
    private double Defense;
    private double ActiveDefense;
    private double Attack;
    private int Level;
    private LivingEntity Mob;
    private HashMap<String, Double> mult_stat_change;
    private HashMap<String, Double> lin_stat_change;
    private boolean passiveMob;
    public boolean isBoss;
    private final Random random = new Random();

    private final Particle.DustOptions dust = new Particle.DustOptions(
            Color.fromRGB((int) (0.9 * 255), (int) (0.1 * 255), (int) (0.1 * 255)), 1);

//===========================================================================================================================
// Mob spawning level and stat initialization:
// Mob level is initialized according to the "weather" of the biome it spawns in
// Variations in the stats attributed according to level are decided by the type of the mob (zombie, skeleton, creeper, spider, etc...) [To be Implemented...]
//===========================================================================================================================
    public MobStats(LivingEntity mob, int weather)
    {
        this.Level = (int) Math.ceil(((10 * weather) - (random.nextInt(10))) * WeatherForecast.getBiomeModifier(mob));

        if ((mob instanceof LivingEntity) && !((mob instanceof Monster) || (mob instanceof Ghast) || (mob instanceof Slime))) {
            this.Level = 1;
            this.passiveMob = true;
        }else {
            this.passiveMob = false;
        }

        //specific mobs require more levels
        if (mob instanceof IronGolem) {
            this.Level = 50;
        }
        else if (mob instanceof Warden)
        {
            this.Level = Math.max(this.Level, 80);
        }
        else if (mob instanceof Wither)
        {
            this.Level = Math.max(this.Level, 90);
        }
        else if (mob instanceof EnderDragon)
        {
            this.Level = Math.max(this.Level, 100);
        }


        this.MaxHealth = 12.0 * this.Level;
        this.Health = MaxHealth;
        this.Attack = 20.0 * (this.Level * 0.3);
        this.Defense = 25.0 * (this.Level * 0.1);
        this.ActiveDefense = this.Defense;
        this.Mob = mob;

        this.mult_stat_change = new HashMap<>();
        this.mult_stat_change.put("MaxHealth", 1.0);
        this.mult_stat_change.put("Defense", 1.0);
        this.mult_stat_change.put("Attack", 1.0);

        this.lin_stat_change = new HashMap<>();
        this.lin_stat_change.put("MaxHealth", 0.0);
        this.lin_stat_change.put("Defense", 0.0);
        this.lin_stat_change.put("Attack", 0.0);
        this.isBoss = false;

    }

    public MobStats(LivingEntity mob, String bossType) {

        this.passiveMob = false;
        this.isBoss = true;

        //=======================================================
        //Abyss Watcher
        if (bossType.equals("MythicMob{AbyssWatcherTest}")) {
            this.Level = 200;
            this.MaxHealth = 150 * this.Level;
            this.Health = MaxHealth;
            this.Attack = 500;
            this.Defense = 125;
            this.ActiveDefense = this.Defense;
            this.Mob = mob;
        }
        //=======================================================
        //Arcane Golem
        else if (bossType.equals("MythicMob{ArcaneGolem}")) {
            this.Level = 120;
            this.MaxHealth = 120 * this.Level;
            this.Health = MaxHealth;
            this.Attack = 500;
            this.Defense = 125;
            this.ActiveDefense = this.Defense;
            this.Mob = mob;
        }
        //=======================================================
        //QuakeFish
        if (bossType.equals("MythicMob{Quakefish}")) {
            this.Level = 120;
            this.MaxHealth = 100 * this.Level;
            this.Health = MaxHealth;
            this.Attack = 500;
            this.Defense = 125;
            this.ActiveDefense = this.Defense;
            this.Mob = mob;
        }
        //=======================================================
        //Lunaris Stag
        if (bossType.equals("MythicMob{LunarisStag}")) {
            this.Level = 120;
            this.MaxHealth = 85 * this.Level;
            this.Health = MaxHealth;
            this.Attack = 500;
            this.Defense = 125;
            this.ActiveDefense = this.Defense;
            this.Mob = mob;
        }
        //=======================================================
        //universal across all bosses

        this.mult_stat_change = new HashMap<>();
        this.mult_stat_change.put("MaxHealth", 1.0);
        this.mult_stat_change.put("Defense", 1.0);
        this.mult_stat_change.put("Attack", 1.0);

        this.lin_stat_change = new HashMap<>();
        this.lin_stat_change.put("MaxHealth", 0.0);
        this.lin_stat_change.put("Defense", 0.0);
        this.lin_stat_change.put("Attack", 0.0);
    }


    //==================================================================================
    //Getters and Setters
    public double getAttack() {
        return Attack;
    }
    public void setAttack(double attack) {
        Attack = attack;
    }
    public double getDefense() {
        return Defense;
    }
    public void setDefense(double defense) {
        Defense = defense;
    }
    public double getActiveDefense() {
        return ActiveDefense;
    }
    public void setActiveDefense(double activeDefense) {
        ActiveDefense = activeDefense;
    }
    public HashMap<String, Double> getMult_stat_change() {
        return mult_stat_change;
    }
    public void setMult_stat_change(HashMap<String, Double> mult_stat_change) {
        this.mult_stat_change = mult_stat_change;
    }
    public HashMap<String, Double> getLin_stat_change() {
        return lin_stat_change;
    }
    public void setLin_stat_change(HashMap<String, Double> lin_stat_change) {
        this.lin_stat_change = lin_stat_change;
    }
    public double getHealth() {
        return Health;
    }
    public void setHealth(double health) {
        Health = health;
    }
    public int getLevel() {
        return Level;
    }
    public void setLevel(int level) {
        Level = level;
    }
    public double getMaxHealth() {
        return MaxHealth;
    }
    public void setMaxHealth(double maxHealth) {
        MaxHealth = maxHealth;
    }
    public LivingEntity getMob() {
        return Mob;
    }
    public void setMob(LivingEntity mob) {
        Mob = mob;
    }

    //==================================================================================
    //The code for giving a mob a stat block
    public static void giveStatBlock(LivingEntity entity) {

        if (!((entity instanceof Player) || !(entity instanceof LivingEntity)) && !(entity instanceof ArmorStand))
        {
                MobStats stats = new MobStats(entity, WeatherForecast.getWeather(entity));
                MobUtility.setMobStats(entity, stats);
                entity.setCustomNameVisible(true);
                entity.customName(stats.generateName());
        }
    }
    public static void giveBossStatBlock(LivingEntity entity, String Boss_Type_String) {

        if (!(entity instanceof Player) && !(entity instanceof ArmorStand))
        {
            MobStats stats = new MobStats(entity, Boss_Type_String);
            stats.isBoss = true;
            MobUtility.setMobStats(entity, stats);
            entity.setCustomNameVisible(true);

            //this needs to be replaced/removed but for now, it remains for debugging
            //later, when players hit bosses, it'll update the bossbar and not the name of the mob
            entity.customName(Component.text("Abyss Watcher", Style.style(NamedTextColor.RED)));
        }
    }
    //==================================================================================
    public void updateStatChanges() {
        setActiveDefense((Defense * mult_stat_change.get("Defense")) - lin_stat_change.get("Defense"));
    }

    public boolean damage(PlayerStats playerstats, double remainingmultihit)
    {
        updateStatChanges();

        if ((getMob() instanceof Tameable) && ((Tameable) getMob()).isTamed()) {
            return false;
        }

        if (playerstats.getActiveCurrentStamina() >= playerstats.getStaminaCost())
        {
            if (Math.random() < playerstats.getCritChance()) {
                this.Health -= (0.01 * ((int) (100 *
                        (((playerstats.getActiveDamage() * playerstats.getCritDamageMult()) - ((playerstats.getActiveDamage() * playerstats.getCritDamageMult()) * (getActiveDefense() / (getActiveDefense() + 100))))
                        ))));
                playerstats.getPlayer().getWorld().playEffect(playerstats.getPlayer().getLocation(), Effect.ANVIL_LAND, 1, 0);
            }
            else{
                this.Health -= (0.01 * ((int) (100 *
                        ((playerstats.getActiveDamage() - (playerstats.getActiveDamage() * (getActiveDefense() / (getActiveDefense() + 100))))
                        ))));
            }

            if (Math.random() < remainingmultihit) {
                playerstats.getPlayer().getWorld().spawnParticle(Particle.REDSTONE, getMob().getLocation().add(0,1,0), 10, 0.25, 0.5, 0.25, 0, dust, false);
                return damage(playerstats, remainingmultihit - 1.0);
            }
            else {
                playerstats.useStamina(playerstats.getStaminaCost());

                updateMobHealthbar();

                if (getHealth() <= 0 && !playerstats.isInBossFight())
                {
                    //"kills" the mob once 0 health is hit and awards EXP and drops
                    getMob().setHealth(0);
                    KillReward(playerstats);
                }
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public void mobDamage(MobStats mobstats) {
        this.Health -= (0.01 * ((int) (100 *
                ((mobstats.getAttack() - (mobstats.getAttack() * (getActiveDefense() / (getActiveDefense() + 100))))
                ))));
    }
    public boolean ranged_damage(PlayerStats playerstats, double remainingmultihit, double speed)
    {
        //A way to damage mobs where the stamina usage is done elsewhere
        updateStatChanges();

        if (getMob() instanceof Tameable && ((Tameable) getMob()).isTamed()) {
            return false;
        }

        if (Math.random() < playerstats.getCritChance()) {
            this.Health -= (0.01 * ((int) (100 * ((speed / 2.5) - 0.2) *
                    (((playerstats.getActiveDamage() * playerstats.getCritDamageMult()) - ((playerstats.getActiveDamage() * playerstats.getCritDamageMult()) * (getActiveDefense() / (getActiveDefense() + 100))))
                    ))));
            playerstats.getPlayer().getWorld().playEffect(playerstats.getPlayer().getLocation(), Effect.ANVIL_LAND, 1, 0);
        }
        else{
            this.Health -= (0.01 * ((int) (100 * ((speed / 2.5) - 0.2) *
                    ((playerstats.getActiveDamage() - (playerstats.getActiveDamage() * (getActiveDefense() / (getActiveDefense() + 100))))
                    ))));
        }

        if (Math.random() < remainingmultihit) {
            return ranged_damage(playerstats, remainingmultihit - 1.0, speed);
        }
        else {

            updateMobHealthbar();

            if (getHealth() <= 0 && !playerstats.isInBossFight())
            {
                //"kills" the mob once 0 health is hit and awards EXP and drops
                getMob().setHealth(0);
                KillReward(playerstats);
            }
            return true;
        }
    }

    public void updateMobHealthbar() {
        if (getHealth() > 0) {
            if (!isBoss && !getMob().isDead()) {
                getMob().customName(generateName());
                getMob().setHealth(Math.max(0, getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * (getHealth() / getMaxHealth())));
            } else if (isBoss && !getMob().isDead()) {
                //this will later change the title on the bossbar instead of the entity itself
                getMob().customName(updateBossBarName());
                getMob().setHealth(Math.max(0, getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * (getHealth() / getMaxHealth())));
            }
        }
    }

    public void KillReward(PlayerStats stats) {
        int EXPtoGive = EXPtoGive();

        for (Player p : stats.getParty()) {
            PlayerStats PMemberStats = PlayerUtility.getPlayerStats(p);
            PMemberStats.gainExperience(EXPtoGive / PMemberStats.getParty().size());
        }

        if (!passiveMob && !isBoss)
        {
            if (stats.getPlayer().getInventory().firstEmpty() != -1) { //if there's an empty slot to put an item in
                stats.getPlayer().getInventory().addItem(PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateRandomEquipment(getMob())));
            }else{
                stats.getPlayer().getWorld().dropItem(stats.getPlayer().getLocation().add(0,0.2,0), PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateRandomEquipment(getMob())));
            }
        }

        //specific mob loot drop handler, since the player is technically not the source of damage in this game, some items don't drop
        if (getMob() instanceof Blaze) {
            Random rd = new Random();
            if (rd.nextFloat() < 0.5)
                getMob().getLocation().getWorld().dropItem(stats.getPlayer().getLocation().add(0,0.2,0), new ItemStack(Material.BLAZE_ROD));
        }
        if (!isBoss)
            getMob().customName(Component.text("+" + EXPtoGive + "XP" + " - " + stats.getPlayer().getName()));
        else
            getMob().customName(Component.text("+" + EXPtoGive + "XP" + " - to party"));
    }

    public int EXPtoGive() {
        return 50 * ((int) Math.pow(getLevel(), 1.25));
    }

    public void spell_damage(double amount, Player player)
    {
        if (!(getMob() instanceof ArmorStand)) {
            updateStatChanges();

            if (getMob() instanceof Tameable && ((Tameable) getMob()).isTamed()) {
                return;
            }

            setHealth(this.Health - amount);

            if (getMob() instanceof Creature) {
                Creature mobC = (Creature) getMob();
                mobC.setTarget(player);
            }

            updateMobHealthbar();

            PlayerStats Pstats = PlayerUtility.getPlayerStats(player);
            if (getHealth() <= 0 && !Pstats.isInBossFight()) {
                if (!getMob().isDead()) {
                    KillReward(Pstats);
                }
                getMob().setHealth(0);
            }


        }
    }

    public Component generateName() {
        return Component.text("Lv" + getLevel() + " " + getMob().getType().toString().toLowerCase()).append(Component.text(" [" + ((int) getHealth()) +"HP]", Style.style(NamedTextColor.RED, TextDecoration.BOLD)));
    }
    public Component updateBossBarName() {
        return Component.text("Lv" + getLevel() + " " + getMob().getType().toString().toLowerCase()).append(Component.text(" [" + ((int) getHealth()) +"HP]", Style.style(NamedTextColor.RED, TextDecoration.BOLD)));
    }

    public void AlterMobDefense(double mult, double lin) {
        mult_stat_change.put("Defense", mult_stat_change.get("Defense") * mult);
        lin_stat_change.put("Defense", lin_stat_change.get("Defense") + lin);
    }

}
