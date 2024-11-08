package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.wands.wand;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Collection;



public class PyroFlameBooster extends BukkitRunnable {

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;

    private Player origin;
    private PlayerStats stats;
    private World world;
    private Location loc;
    private Collection<LivingEntity> Targets;
    private BossBar ChannelTime;
    private wand Wand;
    private DecimalFormat NumberFormat = new DecimalFormat("0.0");

    //Need a variable that holds the wand in order to easily apply the modifiers onto the spell (without coupling code)

    public PyroFlameBooster(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.loc = origin.getLocation();
        this.world = origin.getWorld();
        this.Wand = wand.ItemToWand(caster.getInventory().getItemInOffHand());
    }

    public int getProgress() {
        return progress;
    }
    public void incrementProgress() {this.progress = getProgress() + 1;}
    public void cast() {

        if (progress == 0)
        {
            //If the player has the mana for the spell
            if (stats.getActiveCurrentMana() >= ManaCostCalc(stats))
            {

                //spend the mana for the spell
                stats.spendMana(ManaCostCalc(stats));

                //creates BossBar for player's cooldown timer and shows it to player
                ChannelTime = Bukkit.createBossBar("Spell Cooldown: ", BarColor.RED, BarStyle.SOLID);
                ChannelTime.addPlayer(stats.getPlayer());
                ChannelTime.setVisible(true);

                //makes sound
                stats.getPlayer().getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 0);
                stats.getPlayer().playSound(origin, Sound.ENTITY_GENERIC_EXPLODE, 1, 0);

                //effect of spell
                MobStats mobstats;
                loc = origin.getLocation();
                origin.setVelocity(origin.getVelocity().setY(1 * Wand.getUtilitySpellPowerModifier()));
                Targets = world.getNearbyLivingEntities(loc, SpellAOE());
                world.spawnParticle(Particle.EXPLOSION_HUGE, origin.getLocation(), 1);

                for (LivingEntity target : Targets)
                {
                    if (!(target instanceof Player))
                    {
                        mobstats = MobUtility.getMobStats(target);
                        mobstats.spell_damage(FlameBoosterDamageCalc(mobstats), origin);
                        target.damage(0);

                    }
                }
            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }
        else if ((progress > 0) && (progress < 25)) {
            loc = origin.getLocation().add(0,1,0);
            double theta = (Math.PI * (0.25 * (progress % 8)));
            double x = 0.5 * Math.cos(theta);
            double z = 0.5 * Math.sin(theta);

            loc.add(x, 0, z);
            loc.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0F, 0F, 0F, 0.001);
            loc.subtract(x, 0, z);
        }

        if (progress >= 50)
        {
            this.cancel();
        }

        //progress counter
        incrementProgress();

        if (!this.isCancelled() && (progress < 50))
        {
            ChannelTime.setProgress(1.0-(0.02 * getProgress()));
            ChannelTime.setTitle("Spell Cooldown " + NumberFormat.format(spellCooldownTextUpdate(50, progress)));
        }
        else if (getProgress() == 50)
        {
            if (ChannelTime != null)
                ChannelTime.removeAll();
            stats.setCastingSpell(false);
        }



    }

    public double FlameBoosterDamageCalc(MobStats mobstats)
    {
        return 5 * (CasterSpellDamage() - (CasterSpellDamage() * (mobstats.getDefense() / (mobstats.getDefense() + 100))));
    }
    public double SpellAOE() {
        return 2.5 * Wand.getUtilitySpellPowerModifier();
    }
    public double CasterSpellDamage() {
        return stats.getWisdom() * Wand.getOffenseSpellPowerModifier();
    }

    public double ManaCostCalc(PlayerStats playerstats)
    {
        return 40.0 * Wand.getSpellCostModifier();
    }
    public double spellCooldownTextUpdate(double upperLimit, double currentProgress) {
        double increment = 1.0/upperLimit;
        return (upperLimit * 0.05) - ((upperLimit * 0.05) * (increment * currentProgress));
    }
    @Override
    public void run() {
        cast();
    }


}
