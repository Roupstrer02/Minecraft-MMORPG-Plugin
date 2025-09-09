package me.roupen.firstpluginthree.magic;

import io.papermc.paper.entity.TeleportFlag;
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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;

public class VoidRealitySplit extends BukkitRunnable {

    //Progress dictates what stage of the spell has been reached, measured in ticks (20/s -> 20 == 1s)
    private int progress = 0;

    //the cooldown (or in-game terminology "Arcane Overheat" for reasons explained some other time) given to the player where they can no longer cast ANY spell
    private final int blinkStepDuration = 20;
    private final double spellCooldown = blinkStepDuration + 80;

    //for the sake of preventing ghost spells from sticking around, this value auto-ends the spell subprocess when progress reaches this count
    private final int timeOut = (int) spellCooldown + 1;

    //Base mana cost (without reduction from wand)
    public final int baseManaCost = 100;

    //if you wish to use the standard damage calculation provided, this value is simply a factor towards how much damage the spell deals
    private final double spellDamage = 2;

    //========================================================================================================================================================

    //player casting the spell <-> if other sources of damage or effects come of this spell, the origin may need to change (or there may be more origins, add them)
    private Player origin;

    //self-explanatory
    private PlayerStats stats;
    private World world;

    //originally set as the location of the player/caster/origin
    private Location loc;

    //if you need an AOE target selection, this is how it's stored commonly
    private Iterator<LivingEntity> Targets;
    private Set<LivingEntity> MobsHit = new TreeSet<>();

    //This is being handled for you
    private BossBar ChannelTime;

    //any attributes about the wand's stats can be obtained here
    private wand Wand;

    //If your spell requires to damage the target(s) only once, set this flag in your logic
    private boolean SpellHit = false;
    private final Material[] exempt_blocks = {Material.AIR, Material.GRASS, Material.TALL_GRASS, Material.WATER, Material.CAVE_AIR, Material.VINE, Material.CAVE_VINES, Material.FERN, Material.DEAD_BUSH, Material.SNOW};
    private DecimalFormat NumberFormat = new DecimalFormat("0.0");

    //Constructor
    //Should any of the initial values for the spell variables mentioned below need to change, this is where you'd change them
    public VoidRealitySplit(Player caster)
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
    public double CasterSpellDamage() {
        return Balance.spellPowerCalc(stats.getLevel(), stats.getWisdom()) * Wand.getOffenseSpellPowerModifier();
    }

    //used for determining the radius of circular AOE targeting (this considers the wand's properties)
    public double SpellRange(int baseRange) {
        return baseRange * Wand.getUtilitySpellPowerModifier();
    }
    public double SpellAOE(int baseRadius) {
        return baseRadius * Wand.getUtilitySpellPowerModifier();
    }

    //determines the mana cost of the spell, considering the mana efficiency of the wand that casted it
    public double ManaCostCalc()
    {
        return baseManaCost * Wand.getSpellCostModifier();
    }
    //Create the damage formula for your spell, alternate versions can be created for spells with multiple hitboxes/damage ranges
    public double DamageCalc(MobStats mobstats)
    {
        //if a spell has multiple components dealing different damage counts, many of these can be created or switch cased through
        return spellDamage * (CasterSpellDamage() - (CasterSpellDamage() * (mobstats.getDefense() / (mobstats.getDefense() + 100))));
    }
    //======================================================================================================================================================

    //2.2 - Logic Methods

    public void spellStartup() {
        //Any code written here will happen immediately upon casting the spell (progress == 0) ------- (if the player is able to cast it)
        origin.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, blinkStepDuration * 3, 5));

        Location spellLoc = loc;

        //targets are found during the for loop of the blink
        //character blinks to furthest possible air block within range
        for (int i = 0; i < SpellRange(10); i++) {
            if (!Arrays.asList(exempt_blocks).contains(spellLoc.getBlock().getType())) {
                SpellHit = true;
            }

            if (!SpellHit) {
                //move particle and bullet forward
                Targets = spellLoc.getNearbyLivingEntities(SpellAOE(2)).iterator();
                world.spawnParticle(Particle.SONIC_BOOM, spellLoc, 1, 0, 0, 0, 0, null, true);
                spellLoc = spellLoc.add(spellLoc.getDirection());

                //target hit
                if (Targets.hasNext()) {
                    //get entity hit
                    LivingEntity Target = Targets.next();
                    for (Iterator<LivingEntity> it = Targets; it.hasNext(); ) {
                        LivingEntity t = it.next();

                        if (!(Target instanceof Player) && !(Target instanceof Tameable && ((Tameable) Target).isTamed()))
                            MobsHit.add(t);


                    }
                }
            }
        }

        if (!Arrays.asList(exempt_blocks).contains(spellLoc.getBlock().getType()))
            spellLoc = spellLoc.subtract(spellLoc.getDirection());

        spellLoc.setPitch(loc.getPitch());
        spellLoc.setYaw(loc.getYaw());
        origin.teleport(spellLoc);
        world.playSound(origin, Sound.ENTITY_WARDEN_HEARTBEAT, 1, 0);

    }
    public void spellPerTick() {
        //any code written here will **attempt** to run every tick

        //small particle mark above all targets
        for (LivingEntity t : MobsHit) {
            Location tLoc = t.getLocation().add(0,t.getHeight() + 0.2, 0);
            world.spawnParticle(Particle.SOUL, tLoc, 1, 0.15, 0, 0.15, 0, null, true);

            if (progress == blinkStepDuration) {
                //damage is dealt to targets hit
                MobStats mStats = MobUtility.getMobStats(t);

                mStats.spell_damage(DamageCalc(mStats), origin);
                world.playSound(origin, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 0.3f, 0);
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
            if (stats.getActiveCurrentMana() >= ManaCostCalc())
            {

                //spend the mana for the spell
                stats.spendMana(ManaCostCalc());

                //creates BossBar for player's cooldown timer and shows it to player
                ChannelTime = Bukkit.createBossBar("Spell Cooldown: ", BarColor.RED, BarStyle.SOLID);
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
