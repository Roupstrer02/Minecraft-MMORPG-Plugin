package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.wands.wand;
import org.bukkit.boss.BossBar;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Collection;

import static me.roupen.firstpluginthree.magic.spells.*;

public class DivineStrengthOfFaith extends BukkitRunnable {

    //Progress dictates what stage of the spell has been reached, measured in ticks (20/s -> 20 == 1s)
    private int progress = 0;

    //the cooldown (or in-game terminology "Arcane Overheat" for reasons explained some other time) given to the player where they can no longer cast ANY spell
    private double spellCooldown = 40;

    //for the sake of preventing ghost spells from sticking around, this value auto-ends the spell subprocess when progress reaches this count
    private int timeOut = 41;

    //Base mana cost (without reduction from wand)
    public static double baseManaCost = 40;

    //if you wish to use the standard damage calculation provided, this value is simply a factor towards how much damage the spell deals
    private double spellDamage = 5;

    //========================================================================================================================================================

    // 1.2 - General spell variables

    //player casting the spell <-> if other sources of damage or effects come of this spell, the origin may need to change (or there may be more origins, add them)
    private Player origin;

    //self-explanatory
    private PlayerStats stats;
    private World world;

    //originally set as the location of the player/caster/origin
    private Location loc;

    //if you need an AOE target selection, this is how it's stored commonly
    private Collection<Player> Targets;
    private PlayerStats TargetStats;

    //This is being handled for you
    private BossBar ChannelTime;

    //any attributes about the wand's stats can be obtained here
    private wand Wand;
    private DecimalFormat NumberFormat = new DecimalFormat("0.0");

    // Particle animation config
    private final Particle.DustOptions dust = new Particle.DustOptions(
            Color.fromRGB((int) (0.7 * 255), (int) (0.3 * 255), (int) (0.1 * 255)), 1);

    //If your spell requires to damage the target(s) only once, set this flag in your logic
    private boolean SpellHit = false;

    //Constructor
    //Should any of the initial values for the spell variables mentioned below need to change, this is where you'd change them
    public DivineStrengthOfFaith(Player caster)
    {
        origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();

        this.Wand = (wand.ItemToWand(origin.getInventory().getItemInOffHand()));

    }

    //======================================================================================================================================================

    // 2. Spell Methods

    /*
       Common Functions used in spells:
       targeting:
       damage:
     */
    // 2.1 - Calculation Methods

    //the standard "player spell damage" or "arcane damage potential" is their wisdom level * wand offense affinity --> intended to be used IN the damage calculation, not standalone
    public double CasterSpellPower() {
        return stats.getCasterSpellDamage() * Wand.getDefenseSpellPowerModifier();
    }
    public double DamageIncreaseFactor() { return 1.05 + (CasterSpellPower() / 200 );}

    //used for determining the radius of circular AOE targeting (this considers the wand's properties)
    public double SpellAOE(int baseRadius) {
        return baseRadius * Wand.getUtilitySpellPowerModifier();
    }

    //determines the mana cost of the spell, considering the mana efficiency of the wand that casted it
    public double ManaCostCalc(PlayerStats playerstats)
    {
        return baseManaCost * Wand.getSpellCostModifier();
    }
    //Create the damage formula for your spell, alternate versions can be created for spells with multiple hitboxes/damage ranges

    //======================================================================================================================================================

    //2.2 - Logic Methods

    public void spellStartup() {
        //Any code written here will happen immediately upon casting the spell (progress == 0) ------- (if the player is able to cast it)
        Targets = loc.getNearbyPlayers(SpellAOE(3));
        spells.ParticleCircle(loc, SpellAOE(3), Particle.GLOW_SQUID_INK, false);
        for (Player player: Targets) {
            if (!player.getName().equals(origin.getName())) {
                TargetStats = PlayerUtility.getPlayerStats(player);
                TargetStats.changeMultiplicativeStats("Damage", DamageIncreaseFactor());
            }

        }
    }

    public void spellPerTick() {
        //any code written here will **attempt** to run every tick
        for (Player player: Targets) {
            if (!player.getName().equals(origin.getName())) {
                loc = player.getLocation();
                origin.getWorld().spawnParticle(Particle.REDSTONE, loc, 100, 0.25, 0, 0.25, 0, dust, false);
                if (progress == spellCooldown - 1) {
                    TargetStats.changeMultiplicativeStats("Damage", 1 / DamageIncreaseFactor());
                }
            }
        }
    }


    //======================================================================================================================================================
    //This method combines all previous methods and variables into a logic structure that fits a basic spell (initial startup, maybe does something over time, and ends)
    //If you think you fully understand the logic of what's written in the cast() spell, then feel free to change it to make more complex spells

    //ALTHOUGH RUN IT BY ME FIRST, I'M NOT COMPILING CODE STRUCTURE I HAVEN'T SEEN
    public void cast() {

        if (progress == 0)
        {
            //If the player has the mana for the spell
            if (stats.getActiveCurrentMana() >= ManaCostCalc(stats))
            {

                //spend the mana for the spell
                stats.spendMana(ManaCostCalc(stats));

                //creates BossBar for player's cooldown timer and shows it to player
                ChannelTime = Bukkit.createBossBar("Spell Cooldown: ", BarColor.WHITE, BarStyle.SOLID);
                ChannelTime.addPlayer(stats.getPlayer());
                ChannelTime.setVisible(true);

                //makes sound
                spellStartup();

            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }
        else if (progress > 0)
        {
            spellPerTick();
        }
        if (progress >= timeOut)
        {
            this.cancel();
        }

        incrementProgress();

        if (!this.isCancelled() && (progress < spellCooldown))
        {
            ChannelTime.setProgress(1.0-((1 / spellCooldown) * progress));
            ChannelTime.setTitle("Spell Cooldown " + NumberFormat.format(spellCooldownTextUpdate(spellCooldown, progress)));
        }
        else if (progress == spellCooldown)
        {
            if (ChannelTime != null)
                ChannelTime.removeAll();
            stats.setCastingSpell(false);
        }
    }
    //======================================================================================================================================================


    //increment handlers, shouldn't need to be touched
    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public void incrementProgress() {this.progress = getProgress() + 1;}
    public double spellCooldownTextUpdate(double upperLimit, double currentProgress) {
        double increment = 1.0/upperLimit;
        return (upperLimit * 0.05) - ((upperLimit * 0.05) * (increment * currentProgress));
    }
    @Override
    public void run() {
        cast();
    }

}
