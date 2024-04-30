package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.constantrunnables.spells;
import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.wands.wand;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.*;

public class Chronothief extends spells {

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;
    private int spellActiveTime = 0;
    private Player origin;
    private PlayerStats stats;
    private World world;
    private Location loc;
    private Entity spellblock;
    private HashSet<LivingEntity> TargetsAffected;
    private BossBar ChannelTime;
    private HashMap<MobStats, Double> defenseRemoved;
    private MobStats mobstats;

    //Need a variable that holds the wand in order to easily apply the modifiers onto the spell (without coupling code)

    public Chronothief(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();
        this.defenseRemoved = new HashMap<>();
        this.TargetsAffected = new HashSet<>();

        setSpellName("Chrono Thief");
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
                ChannelTime = Bukkit.createBossBar("Spell Cooldown", BarColor.RED, BarStyle.SOLID);
                ChannelTime.addPlayer(stats.getPlayer());
                ChannelTime.setVisible(true);

                //makes sound
                stats.getPlayer().getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 2, 0);

                //effect of spell
                spellblock = world.spawnFallingBlock(loc.add(0,0.8,0), Material.REDSTONE_LAMP.createBlockData());
                spellblock.customName(Component.text("Chrono Thief"));
                spellblock.setCustomNameVisible(false);
                spellblock.setVelocity(loc.getDirection().multiply(1.5));

            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }
        else if (progress > 0) {
            if (spellblock.isOnGround() || spellblock.isDead()) {

                Collection<Entity> nearbyThings = spellblock.getNearbyEntities(1,1,1);
                spellActiveTime++;
                for (Entity thing : nearbyThings) {
                    if (thing instanceof Item) {
                        Item item = (Item) thing;
                        if (item.getItemStack().getType() == Material.REDSTONE_LAMP) {
                            thing.remove();
                        }
                    }
                }

                if (progress % 2 == 0)
                {ParticleSphere(spellblock.getLocation(), SpellAOE(), Particle.ELECTRIC_SPARK, 3, 3);}
                if (progress % 20 == 0)
                {world.playSound(spellblock.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 3, 0);}

                world.setBlockData(spellblock.getLocation(), Material.AIR.createBlockData());

                //for all mobs within a range
                for (LivingEntity mob : spellblock.getLocation().getNearbyLivingEntities(SpellAOE()))
                {
                    if (mob instanceof Mob) {
                        mob.setVelocity(new Vector(0,0,0));
                        mobstats = MobUtility.getMobStats(mob);

                        //Set Defense to half.
                        if (!TargetsAffected.contains(mob)) {
                            mobstats.AlterMobDefense(0.75, 0.0);
                        }
                        TargetsAffected.add(mob);
                    }
                }
            }
        }
        //spell ending logic
        if (progress >= 100)
        {
            if (!((spellblock.isDead()) || spellblock.isOnGround()) && (spellActiveTime == 0)) {
                spellblock.remove();
                this.cancel();
            }
            else if (spellActiveTime >= 100) {

                for (LivingEntity mob : TargetsAffected) {
                    mobstats = MobUtility.getMobStats(mob);
                    mobstats.AlterMobDefense(1/0.75,0.0);
                }

                this.cancel();
            }
        }

        //progress counter
        incrementProgress();

        if (!this.isCancelled() && (progress < 100))
        {
            ChannelTime.setProgress(1.0-(0.01 * progress));
            ChannelTime.setTitle("Spell Cooldown " + NumberFormat.format(spellCooldownTextUpdate(100, progress)));
        }
        else if (getProgress() == 100)
        {
            if (ChannelTime != null)
                ChannelTime.removeAll();
            stats.setCastingSpell(false);
        }
    }


    public double SpellAOE() {
        return 5 * getCastingWand().getUtilitySpellPowerModifier();
    }

    public double ManaCostCalc(PlayerStats playerstats)
    {
        return 150.0 * getCastingWand().getSpellCostModifier();
    }

    @Override
    public void run() {
        cast();
    }


}
