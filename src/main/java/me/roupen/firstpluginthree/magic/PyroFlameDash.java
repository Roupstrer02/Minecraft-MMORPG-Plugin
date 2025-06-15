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
import java.util.HashSet;

import static me.roupen.firstpluginthree.magic.spells.*;

public class PyroFlameDash extends BukkitRunnable {

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;

    private Player origin;
    private PlayerStats stats;
    private World world;
    private Location loc;
    private Collection<LivingEntity> Targets;
    private HashSet<LivingEntity> SavedTargets = new HashSet<LivingEntity>();
    private LivingEntity[] ArrayOfTargets;
    private BossBar ChannelTime;
    private wand Wand;
    private DecimalFormat NumberFormat = new DecimalFormat("0.0");
    public static double baseManaCost = 40.0;

    //Need a variable that holds the wand in order to easily apply the modifiers onto the spell (without coupling code)

    public PyroFlameDash(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();
        this.Wand = wand.ItemToWand(caster.getInventory().getItemInOffHand());
    }
    public void cast() {

        if (getProgress() == 0)
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
                stats.getPlayer().getWorld().playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 0);
            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }
        else if ((getProgress() < 60))
        {
            loc = origin.getLocation();

            //make this a circle particle effect instead
            ParticleCircle(loc.add(0,0.2,0), SpellAOE(), Particle.FLAME, true);

            Targets = world.getNearbyLivingEntities(loc, SpellAOE());

            SavedTargets.clear();
            SavedTargets.addAll(Targets);

            ArrayOfTargets = SavedTargets.toArray(new LivingEntity[SavedTargets.size()]);
            for (LivingEntity tempTarget : ArrayOfTargets) {
                if (!(tempTarget instanceof Player)) {
                    MobStats mobstats = MobUtility.getMobStats(tempTarget);
                    mobstats.spell_damage(FlameDashDamageCalc(mobstats), origin);
                    tempTarget.damage(0);
                    world.spawnParticle(Particle.SMALL_FLAME, tempTarget.getLocation().add(0,1,0), 10, 1, 1, 1, 0, null, true);
                    world.spawnParticle(Particle.SMALL_FLAME, tempTarget.getLocation().add(0, 2.5, 0), 1, 0, 0, 0, 0.05, null, true);
                }
            }
        }
        if (getProgress() >= 60)
        {
            for (LivingEntity target : ArrayOfTargets)
            {
                if (!(target instanceof Player))
                {
                    MobStats mobstats = MobUtility.getMobStats(target);
                    mobstats.spell_damage(15 * FlameDashDamageCalc(mobstats), origin);
                    target.damage(0);
                    world.spawnParticle(Particle.SMALL_FLAME, target.getLocation().add(0,1,0), 200, 1, 1, 1, 0, null, true);

                }

            }
            this.cancel();
        }

        incrementProgress();

        if (!this.isCancelled() && (getProgress() < 40))
        {
            ChannelTime.setProgress(1.0-(0.025 * getProgress()));
            ChannelTime.setTitle("Spell Cooldown " + NumberFormat.format(spellCooldownTextUpdate(40, progress)));
        }
        else if (getProgress() == 40)
        {
            if (ChannelTime != null)
                ChannelTime.removeAll();
            stats.setCastingSpell(false);
        }
    }

    public int getProgress() {
        return progress;
    }
    public void incrementProgress() {this.progress = getProgress() + 1;}

    public double FlameDashDamageCalc(MobStats mobstats) {
        return 0.15 * (CasterSpellDamage() - (CasterSpellDamage() * (mobstats.getDefense() / (mobstats.getDefense() + 100))));
    }

    public double CasterSpellDamage()
    {
        return stats.getCasterSpellDamage() * Wand.getOffenseSpellPowerModifier();
    }
    public double SpellAOE() {
        return 2.5 * Wand.getUtilitySpellPowerModifier();
    }

    public double ManaCostCalc(PlayerStats stats) {
        return baseManaCost * Wand.getSpellCostModifier();
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
