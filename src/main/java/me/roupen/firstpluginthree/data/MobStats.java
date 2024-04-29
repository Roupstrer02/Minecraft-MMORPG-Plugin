package me.roupen.firstpluginthree.data;

import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Particle;

import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.Random;

public class MobStats {

    private double Health;
    private double MaxHealth;
    private double Defense;
    private double ActiveDefense;
    private double Attack;
    private int Level;
    private Entity Mob;
    private HashMap<String, Double> mult_stat_change;
    private HashMap<String, Double> lin_stat_change;
    private boolean passiveMob;
    private final Random random = new Random();

    private final Particle.DustOptions dust = new Particle.DustOptions(
            Color.fromRGB((int) (0.9 * 255), (int) (0.1 * 255), (int) (0.1 * 255)), 1);

//===========================================================================================================================
// Mob spawning level and stat initialization:
// Mob level is initialized according to the "weather" of the biome it spawns in
// Variations in the stats attributed according to level are decided by the type of the mob (zombie, skeleton, creeper, spider, etc...) [To be Implemented...]
//===========================================================================================================================
    public MobStats(Entity mob, int weather)
    {
        //level = 10 * weather difficulty - (1 to 9)
        this.Level = (10 * weather) - (random.nextInt(10));

        if ((mob instanceof LivingEntity) && !((mob instanceof Monster) || (mob instanceof Ghast) || (mob instanceof Slime))) {
            this.Level = 1;
            this.passiveMob = true;
        }else {
            this.passiveMob = false;
        }

        if (mob instanceof IronGolem) {
            this.Level = 100;
        }



        this.MaxHealth = 10.0 * this.Level;
        this.Health = MaxHealth;
        this.Attack = 20.0 * (this.Level * 0.3);
        this.Defense = 35.0 * (this.Level * 0.1);
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
    public Entity getMob() {
        return Mob;
    }
    public void setMob(Entity mob) {
        Mob = mob;
    }

    //==================================================================================
    public void updateStatChanges() {
        setActiveDefense((Defense * mult_stat_change.get("Defense")) - lin_stat_change.get("Defense"));
    }

    public boolean damage(PlayerStats playerstats, double remainingmultihit)
    {
        updateStatChanges();
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
            return true;
        }
    }

    public void KillReward(PlayerStats stats) {
        int EXPtoGive = EXPtoGive();
        stats.gainExperience(EXPtoGive);
        if (!passiveMob)
        {
            if (stats.getPlayer().getInventory().firstEmpty() != -1) { //if there's an empty slot to put an item in
                stats.getPlayer().getInventory().addItem(PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateRandomEquipment((LivingEntity) getMob())));
            }else{
                stats.getPlayer().getWorld().dropItem(stats.getPlayer().getLocation().add(0,0.2,0), PlayerEquipment.EquipmentToItem(PlayerEquipment.GenerateRandomEquipment((LivingEntity) getMob())));
            }
        }
        getMob().customName(Component.text("+" + EXPtoGive + "XP" + " - " + stats.getPlayer().getName()));
    }

    private int EXPtoGive() {
        return 50 * ((int) Math.pow(getLevel(), 1.25));
    }

    public void spell_damage(double amount)
    {
        updateStatChanges();
        setHealth(this.Health - amount);
        getMob().customName(generateName());
    }

    public Component generateName() {
        return Component.text("Lv" + getLevel() + " " + getMob().getType()).append(Component.text(" [" + ((int) getHealth()) +"HP]", Style.style(NamedTextColor.RED, TextDecoration.BOLD)));
    }

    public void AlterMobDefense(double mult, double lin) {
        mult_stat_change.put("Defense", mult_stat_change.get("Defense") * mult);
        lin_stat_change.put("Defense", lin_stat_change.get("Defense") + lin);
    }

}
