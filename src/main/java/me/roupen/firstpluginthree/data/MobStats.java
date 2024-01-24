package me.roupen.firstpluginthree.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Effect;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;

import java.text.DecimalFormat;

public class MobStats {

    private double Health;
    private double MaxHealth;
    private double Defense;
    private double Attack;
    private int Level;
    private Entity Mob;

//===========================================================================================================================
// Mob spawning level and stat initialization:
// Mob level is initialized according to the "weather" of the biome it spawned in
// Variations in the stats attributed according to level are decided by the type of the mob (zombie, skeleton, creeper, spider, etc...)
//===========================================================================================================================
    public MobStats(Entity mob, int weather)
    {
        this.Level = 10 * weather;
        this.MaxHealth = 10.0 * this.Level;
        this.Health = MaxHealth;
        this.Attack = 20.0 * this.Level;
        this.Defense = 35.0 * (this.Level * 0.1);
        this.Mob = mob;
    }
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
    public boolean damage(PlayerStats playerstats, double remainingmultihit)
    {
        if (playerstats.getActiveCurrentStamina() >= playerstats.getStaminaCost())
        {
            if (Math.random() < playerstats.getCritChance()) {
                this.Health -= (0.01 * ((int) (100 *
                        (((playerstats.getActiveDamage() * playerstats.getCritDamageMult()) - ((playerstats.getActiveDamage() * playerstats.getCritDamageMult()) * (getDefense() / (getDefense() + 100))))
                        ))));
                playerstats.getPlayer().getWorld().playEffect(playerstats.getPlayer().getLocation(), Effect.ANVIL_LAND, 1, 0);
            }
            else{
                this.Health -= (0.01 * ((int) (100 *
                        ((playerstats.getActiveDamage() - (playerstats.getActiveDamage() * (getDefense() / (getDefense() + 100))))
                        ))));
            }

            playerstats.useStamina(playerstats.getStaminaCost());

            if (Math.random() < remainingmultihit) {
                playerstats.getPlayer().getWorld().playEffect(playerstats.getPlayer().getLocation(), Effect.BLAZE_SHOOT, 1, 0);
                return damage(playerstats, remainingmultihit - 1.0);
            }
            else {
                return true;
            }
        }
        else
        {
            return false;
        }
    }
    public boolean ranged_damage(PlayerStats playerstats, double remainingmultihit, double speed)
    {
        //A way to damage mobs where the stamina usage is done elsewhere
            if (Math.random() < playerstats.getCritChance()) {
                this.Health -= (0.01 * ((int) (100 * ((speed / 2.5) - 0.2) *
                        (((playerstats.getActiveDamage() * playerstats.getCritDamageMult()) - ((playerstats.getActiveDamage() * playerstats.getCritDamageMult()) * (getDefense() / (getDefense() + 100))))
                        ))));
                playerstats.getPlayer().getWorld().playEffect(playerstats.getPlayer().getLocation(), Effect.ANVIL_LAND, 1, 0);
            }
            else{
                this.Health -= (0.01 * ((int) (100 * ((speed / 2.5) - 0.2) *
                        ((playerstats.getActiveDamage() - (playerstats.getActiveDamage() * (getDefense() / (getDefense() + 100))))
                        ))));
            }

            if (Math.random() < remainingmultihit) {
                return ranged_damage(playerstats, remainingmultihit - 1.0, speed);
            }
            else {
                return true;
            }

    }

    public Component generateName() {
        return Component.text("Lv" + getLevel() + " " + getMob().getType()).append(Component.text(" [" + ((int) getHealth()) +"HP]", Style.style(NamedTextColor.RED, TextDecoration.BOLD)));
    }
}
