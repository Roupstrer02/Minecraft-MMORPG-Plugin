package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.constantrunnables.spellcasting;
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

import java.util.Collection;

public class FlameBooster extends spellcasting {

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;

    private Player origin;
    private PlayerStats stats;
    private World world;
    private Location loc;
    private Collection<LivingEntity> Targets;
    private BossBar ChannelTime;

    //Need a variable that holds the wand in order to easily apply the modifiers onto the spell (without coupling code)

    public FlameBooster(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();

        setSpellName("Flame Booster");
        setCastingWand(wand.ItemToWand(caster.getInventory().getItemInOffHand()));
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
                ChannelTime = Bukkit.createBossBar(this.spellName, BarColor.RED, BarStyle.SOLID);
                ChannelTime.addPlayer(stats.getPlayer());
                ChannelTime.setVisible(true);

                //makes sound
                stats.getPlayer().getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 0);

                //effect of spell
                loc = origin.getLocation();
                origin.setVelocity(origin.getVelocity().setY(1 * getCastingWand().getUtilitySpellPowerModifier()));
                Targets = world.getNearbyLivingEntities(loc, SpellAOE());
            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }
        else if ((progress > 0) && (progress < 50)) {
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
        return 2.5 * getCastingWand().getUtilitySpellPowerModifier();
    }
    public double CasterSpellDamage() {
        return stats.getWisdom() * getCastingWand().getOffenseSpellPowerModifier();
    }

    public double ManaCostCalc(PlayerStats playerstats)
    {
        return 40.0 * getCastingWand().getSpellCostModifier();
    }

    @Override
    public void run() {
        cast();
    }


}
