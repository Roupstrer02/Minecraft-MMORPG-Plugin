package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.balance.Balance;
import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.wands.wand;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

import static me.roupen.firstpluginthree.magic.spells.*;

public class TechFaultInArmor extends BukkitRunnable {

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;
    private int spellActiveTime = 0;
    private Player origin;
    private PlayerStats stats;
    private World world;
    private Location loc;
    private Entity spellblock;
    private Collection<LivingEntity> Targets;
    private BossBar ChannelTime;
    private HashMap<MobStats, Double> defenseRemoved;
    private MobStats mobstats;
    private wand Wand;
    private boolean spellHit;
    private DecimalFormat NumberFormat = new DecimalFormat("0.0");
    public static double baseManaCost = 120.0;
    public static double spellCooldown = 80.0;


    public TechFaultInArmor(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();
        this.defenseRemoved = new HashMap<>();
        this.Targets = new HashSet<>();
        this.spellHit = false;

        this.Wand = (wand.ItemToWand(caster.getInventory().getItemInOffHand()));
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
                ChannelTime = Bukkit.createBossBar("Spell Cooldown", BarColor.PINK, BarStyle.SOLID);
                ChannelTime.addPlayer(stats.getPlayer());
                ChannelTime.setVisible(true);

                //makes sound
                stats.getPlayer().getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1f, 0.5f);

                //initial effect of spell (none)
            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }
        else if (progress < spellCooldown)
        {
            if (progress < 10 && !spellHit) {
                Vector launchVector = origin.getLocation().getDirection().multiply(0.9 * Wand.getUtilitySpellPowerModifier());
                launchVector.setY(0);
                origin.setVelocity(launchVector);
                world.spawnParticle(Particle.SMOKE_LARGE, origin.getLocation().add(0,1.2,0), 5, 0.1, 0.1, 0.1, 0, null, true);
            }

            Targets = origin.getLocation().getNearbyLivingEntities(2 * origin.getVelocity().length());

            if (!Targets.isEmpty() && !(Targets.iterator().next() instanceof Player) && !spellHit) {
                LivingEntity target = Targets.iterator().next();

                ParticleVerticalCircle(loc.add(origin.getLocation().getDirection()), 1.0, 20, Particle.CLOUD);

                world.spawnParticle(Particle.SMOKE_LARGE, origin.getLocation().add(0,1.2,0), 5, 0.1, 0.1, 0.1, 0, null, true);

                //pass velocity to target hit
                target.setVelocity(origin.getVelocity().multiply(0.8));
                origin.setVelocity(new Vector(0,0,0));

                //Damage target hit
                MobStats mobstats = MobUtility.getMobStats(target);
                mobstats.spell_damage(FaultInTheArmorDmgCalc(mobstats), origin);
                target.damage(0);

                //Mark spell as having hit
                spellHit = true;
            }
        }
        //movement logic


        //spell ending logic
        if (progress >= spellCooldown) {
            this.cancel();
        }

        //progress counter
        incrementProgress();

        if (!this.isCancelled() && (progress < spellCooldown))
        {
            ChannelTime.setProgress(1.0-((1.0 / spellCooldown) * progress));
            ChannelTime.setTitle("Spell Cooldown " + NumberFormat.format(spellCooldownTextUpdate(spellCooldown, progress)));
        }
        else if (getProgress() == spellCooldown)
        {
            if (ChannelTime != null)
                ChannelTime.removeAll();
            stats.setCastingSpell(false);
        }
    }

    public double CasterSpellDamage() {
        return stats.getCasterSpellDamage(1.25 * Balance.levelDelta) * Wand.getOffenseSpellPowerModifier();
    }

    public double FaultInTheArmorDmgCalc(MobStats mobstats)
    {
        double loweredDefense = mobstats.getDefense() * 0.5;
        return CasterSpellDamage() - (CasterSpellDamage() * (loweredDefense / (loweredDefense + 100)));
    }

    public double SpellAOE() {
        return 5 * Wand.getUtilitySpellPowerModifier();
    }
    public double ManaCostCalc(PlayerStats playerstats)
    {
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
