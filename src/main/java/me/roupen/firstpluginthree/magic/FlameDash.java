package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.constantrunnables.spells;
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
import java.util.HashSet;

public class FlameDash extends spells {

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

    //Need a variable that holds the wand in order to easily apply the modifiers onto the spell (without coupling code)

    public FlameDash(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();
        setSpellName("Flame Dash");
        setCastingWand(wand.ItemToWand(caster.getInventory().getItemInOffHand()));
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
                ChannelTime = Bukkit.createBossBar(this.spellName, BarColor.RED, BarStyle.SOLID);
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
            ParticleCircle(loc.add(0,0.2,0), SpellAOE(), Particle.FLAME);

            Targets = world.getNearbyLivingEntities(loc, SpellAOE());

            SavedTargets.addAll(Targets);

            ArrayOfTargets = SavedTargets.toArray(new LivingEntity[SavedTargets.size()]);
            for (LivingEntity tempTarget : ArrayOfTargets) {
                if (!(tempTarget instanceof Player))
                    world.spawnParticle(Particle.SMALL_FLAME, tempTarget.getLocation().add(0, 2.5, 0), 5, 0, 0, 0, 0.05, null, true);
            }


        }
        if (getProgress() >= 60)
        {
            for (LivingEntity target : ArrayOfTargets)
            {
                if (!(target instanceof Player))
                {
                    MobStats mobstats = MobUtility.getMobStats(target);
                    mobstats.spell_damage(FlameDashDamageCalc(mobstats));
                    target.damage(0);
                    world.spawnParticle(Particle.SMALL_FLAME, target.getLocation().add(0,1,0), 200, 1, 1, 1, 0, null, true);

                    if (mobstats.getHealth() <= 0) {
                        //Maybe Make a "Mob Death" function
                        if (!target.isDead()) {
                            mobstats.KillReward(stats);
                        }
                        target.setHealth(0);
                    }else{
                        target.customName(mobstats.generateName());
                    }
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
        return 5 * (CasterSpellDamage() - (CasterSpellDamage() * (mobstats.getDefense() / (mobstats.getDefense() + 100))));
    }

    public double CasterSpellDamage()
    {
        return stats.getWisdom() * CastingWand.getOffenseSpellPowerModifier();
    }
    public double SpellAOE() {
        return 2.5 * getCastingWand().getUtilitySpellPowerModifier();
    }

    public double ManaCostCalc(PlayerStats stats) {
        return 40.0 * getCastingWand().getSpellCostModifier();
    }

    @Override
    public void run() {
        cast();
    }

}
