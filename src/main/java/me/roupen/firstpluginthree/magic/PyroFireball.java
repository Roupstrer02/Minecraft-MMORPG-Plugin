package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.balance.Balance;
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
import java.util.Random;

public class PyroFireball extends BukkitRunnable {

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;
    private Player origin = null;
    private PlayerStats stats;
    private World world;
    private Location loc;
    private Location FireballLoc;
    private Collection<LivingEntity> Targets;
    private BossBar ChannelTime;
    private wand Wand;
    private boolean SpellHit = false;
    public static double baseManaCost = 50.0;
    private DecimalFormat NumberFormat = new DecimalFormat("0.0");

    private Material[] exempt_blocks = {Material.AIR, Material.GRASS, Material.TALL_GRASS, Material.FERN, Material.DEAD_BUSH};

    public PyroFireball(Player caster)
    {
        origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();

        this.Wand = (wand.ItemToWand(origin.getInventory().getItemInOffHand()));

    }

    public void setCaster(Player caster) {

    }

    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }
    public void incrementProgress() {this.progress = getProgress() + 1;}
    public void cast() {

        if (getProgress() == 0) //ikwym
        {
            //If the player has the mana for the spell
            if (stats.getActiveCurrentMana() >= ManaCostCalc(stats))
            {

                //spend the mana for the spell
                stats.spendMana(ManaCostCalc(stats));

                //set starting point of fireball
                loc = loc.add(0,1.5,0).add(origin.getLocation().getDirection().multiply(0.5));
                FireballLoc = loc;

                //creates BossBar for player's cooldown timer and shows it to player
                ChannelTime = Bukkit.createBossBar("Spell Cooldown: ", BarColor.RED, BarStyle.SOLID);
                ChannelTime.addPlayer(stats.getPlayer());
                ChannelTime.setVisible(true);

                //makes sound
                stats.getPlayer().getWorld().playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 1, 0);

            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }
        else if ((getProgress() < 100) && (!this.SpellHit))
        {

            world.spawnParticle(Particle.FLAME, FireballLoc, 5, 0.1, 0.1, 0.1, 0, null, true);
            FireballLoc = FireballLoc.add(FireballLoc.getDirection().multiply(1));
        }
        if (getProgress() >= 100)
        {
            this.cancel();
        }

        if (!(this.SpellHit) && (FireballLoc != null) && (FireballLoc.getBlock().getType() != Material.AIR || ((FireballLoc.getNearbyLivingEntities(0.5).size() > 0) && !(FireballLoc.getNearbyLivingEntities(0.5).iterator().next() instanceof Player)))) //Once the fireball hits the ground, or a target
        {//EXPLOOOOOOOOSION
            SpellHit = true;

            MobStats mobstats;
            Location animationLoc = FireballLoc;
            ParticleSphere(animationLoc, SpellAOE(), Particle.FLAME);

            Targets = world.getNearbyLivingEntities(FireballLoc, SpellAOE());

            stats.getPlayer().getWorld().playSound(FireballLoc, Sound.ENTITY_GENERIC_EXPLODE, 1, 0);

            for (LivingEntity target : Targets)
            {
                if (!(target instanceof Player))
                {
                    mobstats = MobUtility.getMobStats(target);
                    mobstats.spell_damage(FireballDmgCalc(mobstats), origin);
                    target.damage(0);

                }
            }
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

    public double FireballDmgCalc(MobStats mobstats)
    {
        return (CasterSpellDamage() - (CasterSpellDamage() * (mobstats.getDefense() / (mobstats.getDefense() + 100))));
    }

    public double CasterSpellDamage() {
        return stats.getCasterSpellDamage(Balance.levelDelta) * Wand.getOffenseSpellPowerModifier();
    }

    public double SpellAOE() {
        return 2.5 * Wand.getUtilitySpellPowerModifier();
    }

    public double ManaCostCalc(PlayerStats playerstats)
    {
        return baseManaCost * Wand.getSpellCostModifier();
    }

    public void ParticleSphere(Location loc, double radius, Particle particletype) {
        Random rd = new Random();
        double a, b, c, noise;

        for (int i = 0; i <= 100 * radius * radius; i++) {

            do {
                a = (rd.nextDouble() - 0.5) * 2;
                b = (rd.nextDouble() - 0.5) * 2;
                c = (rd.nextDouble() - 0.5) * 2;
            } while ((a*a)+(b*b)+(c*c) == 0);

            double denom = Math.sqrt((a*a)+(b*b)+(c*c));
            noise = (rd.nextDouble() / 10) + 1;
            double X = (a / denom) * radius * noise;
            double Y = (b / denom) * radius * noise;
            double Z = (c / denom) * radius * noise;

            loc.add(X, Y, Z);
            loc.getWorld().spawnParticle(particletype, loc, 1, 0F, 0F, 0F, 0.001);
            loc.subtract(X, Y, Z);
        }

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
