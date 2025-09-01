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

import java.text.DecimalFormat;
import java.util.*;

import static me.roupen.firstpluginthree.magic.spells.*;

public class TechSnipe extends BukkitRunnable {

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;
    private int spellActiveTime = 0;
    private Player origin;
    private PlayerStats stats;
    private World world;
    private Location loc;
    private Entity spellblock;
    private Iterator<LivingEntity> Targets;
    private BossBar ChannelTime;
    private HashMap<MobStats, Double> defenseRemoved;
    private MobStats mobstats;
    private wand Wand;
    private boolean spellHit;
    private Location bulletLoc;
    private Material[] exempt_blocks = {Material.AIR, Material.GRASS, Material.TALL_GRASS, Material.WATER, Material.CAVE_AIR};
    private DecimalFormat NumberFormat = new DecimalFormat("0.0");

    public static double baseManaCost = 150.0;
    public static double spellCooldown = 150.0;

    public TechSnipe(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();
        this.defenseRemoved = new HashMap<>();
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
                stats.getPlayer().getWorld().playSound(loc, Sound.ITEM_TRIDENT_THROW, 3, 0);
                stats.getPlayer().getWorld().playSound(loc, Sound.ENTITY_GOAT_RAM_IMPACT, 2, 0);
                stats.getPlayer().getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 3, -5);

                loc.add(0, origin.getEyeHeight(), 0).add(loc.getDirection().multiply(2.0));
                ParticleVerticalCircle(loc, 1.0, 50, Particle.SOUL_FIRE_FLAME);
                loc.subtract(0, origin.getEyeHeight(), 0).add(loc.getDirection().multiply(2.0));

                loc.add(0, origin.getEyeHeight(), 0).add(loc.getDirection().multiply(5.0));
                ParticleVerticalCircle(loc, 0.75, 50, Particle.SOUL_FIRE_FLAME);
                loc.subtract(0, origin.getEyeHeight(), 0).add(loc.getDirection().multiply(5.0));

                loc.add(0, origin.getEyeHeight(), 0).add(loc.getDirection().multiply(8.0));
                ParticleVerticalCircle(loc, 0.5, 50, Particle.SOUL_FIRE_FLAME);
                loc.subtract(0, origin.getEyeHeight(), 0).add(loc.getDirection().multiply(8.0));

                //effect of spell
                bulletLoc = origin.getLocation().add(0,1.5,0).add(loc.getDirection().multiply(0.5));

                for (int i = 0; i < spellRange(); i++)
                {

                    if (!Arrays.asList(exempt_blocks).contains(bulletLoc.getBlock().getType())) {
                        spellHit = true;
                    }

                    if (!spellHit) {
                        //move particle and bullet forward
                        Targets = bulletLoc.getNearbyLivingEntities(0.75).iterator();
                        world.spawnParticle(Particle.ELECTRIC_SPARK, bulletLoc, 5, 0, 0, 0, 0, null, true);
                        bulletLoc = bulletLoc.add(bulletLoc.getDirection());

                        //target hit
                        if (Targets.hasNext())
                        {
                            //get entity hit
                            LivingEntity Target = Targets.next();
                            if (!(Target instanceof Player))
                            {
                                //Mark spell as having hit
                                spellHit = true;

                                //damage entity
                                MobStats mobstats = MobUtility.getMobStats(Target);
                                mobstats.spell_damage(TechSnipeDmgCalc(mobstats), origin);

                                Target.damage(0);

                                if (Target instanceof Tameable && ((Tameable) Target).isTamed()) {
                                    spellHit = false;
                                }

                            }
                        }
                    }

                }

            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }

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

    public double spellRange() {
        return 20 * Wand.getUtilitySpellPowerModifier();
    }
    public double CasterSpellDamage() {
        return stats.getCasterSpellDamage(4 * Balance.levelDelta) * Wand.getOffenseSpellPowerModifier();
    }
    public double TechSnipeDmgCalc(MobStats mobstats)
    {
        return CasterSpellDamage();
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
