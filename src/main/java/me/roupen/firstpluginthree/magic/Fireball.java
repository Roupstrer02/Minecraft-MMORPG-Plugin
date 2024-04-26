package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.constantrunnables.spellcasting;
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
import java.util.Collection;

public class Fireball extends spellcasting{

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;

    private final Player origin;
    private final PlayerStats stats;
    private final World world;
    private Location loc;
    private Location FireballLoc;
    private Collection<LivingEntity> Targets;
    private BossBar ChannelTime;
    private boolean SpellHit = false;

    public Fireball(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();

        setSpellName("Fireball");
        setCastingWand(wand.ItemToWand(caster.getInventory().getItemInOffHand()));
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
                ChannelTime = Bukkit.createBossBar(this.spellName, BarColor.RED, BarStyle.SOLID);
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
            FireballLoc = FireballLoc.add(FireballLoc.getDirection().multiply(1.5));
        }
        if (getProgress() >= 100)
        {
            this.cancel();
        }

        if (!(this.SpellHit) && (FireballLoc != null) && (FireballLoc.getBlock().getType() != Material.AIR || ((FireballLoc.getNearbyLivingEntities(0.2).size() > 0) && !(FireballLoc.getNearbyLivingEntities(0.2).iterator().next() instanceof Player)))) //Once the fireball hits the ground, or a target
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
                    mobstats.spell_damage(FireballDmgCalc(mobstats));
                    target.damage(0);
                    if (mobstats.getHealth() <= 0) {

                        if (!target.isDead()) {
                            mobstats.KillReward(stats);
                        }
                        target.setHealth(0);
                    }else{
                        target.customName(mobstats.generateName());
                    }
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
        return 5 * (CasterSpellDamage() - (CasterSpellDamage() * (mobstats.getDefense() / (mobstats.getDefense() + 100))));
    }

    public double CasterSpellDamage() {
        return stats.getWisdom() * getCastingWand().getOffenseSpellPowerModifier();
    }

    public double SpellAOE() {
        return 2 * getCastingWand().getUtilitySpellPowerModifier();
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
