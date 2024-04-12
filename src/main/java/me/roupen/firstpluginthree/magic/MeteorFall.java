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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class MeteorFall extends spellcasting{

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;

    private final Player origin;
    private final PlayerStats stats;
    private final World world;
    private final double MAX_METEOR_OFFSET = 10;
    private Location loc;
    private ArrayList<Location> meteors;
    private Location meteor;
    private MobStats mobstats;
    private Collection<LivingEntity> Targets;
    private Random rand;
    private BossBar ChannelTime;
    private boolean SpellHit = false;

    //Need a variable that holds the wand in order to easily apply the modifiers onto the spell (without coupling code)

    public MeteorFall(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();
        this.loc = origin.getLocation();
        this.meteors = new ArrayList<>();
        this.rand = new Random();

        setSpellName("Meteor Fall");
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
        else if ((getProgress() > 0) && (getProgress() <= 200))
        {
            //character's particle animation for meteor fall
            loc = origin.getLocation().add(0,0.1,0);
            double theta = (Math.PI * (0.25 * (progress % 8)));
            double x = Math.cos(theta);
            double z = Math.sin(theta);

            loc.add(x, 0, z);
            loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 1, 0F, 0F, 0F, 0.001);
            loc.subtract(x, 0.1, z);

            //summoning the meteors + meteor behaviour && animation
            if (getProgress() % 10 == 0 && getProgress() <= 180) {
                meteors.add(loc.add((-MAX_METEOR_OFFSET) + (2 * MAX_METEOR_OFFSET) * rand.nextDouble(),10,(-MAX_METEOR_OFFSET) + (2 * MAX_METEOR_OFFSET) * rand.nextDouble()));
            }
            for (int i = 0; i < meteors.size(); i++) {
                if (meteors.get(i) != null) {

                    //declaration of current meteor
                    meteor = meteors.get(i);

                    //animation of meteors
                    world.spawnParticle(Particle.SMOKE_NORMAL, meteors.get(i), 1, 0.1F, 0F, 0.1F, 0);
                    if (getProgress() % 2 == 1) {world.spawnParticle(Particle.VILLAGER_ANGRY, meteors.get(i), 1, 0.1F, 0F, 0.1F, 0);}

                    //position update of meteors
                    meteors.set(i, meteor.add(0,-0.5,0));

                    //target detection of meteors
                    if (meteor.getBlock().getType() != Material.AIR || ((meteor.getNearbyLivingEntities(0.2).size() > 0) && !(meteor.getNearbyLivingEntities(0.2).iterator().next() instanceof Player))) {

                        //SFX
                        ParticleSphere(meteor, SpellAOE());
                        stats.getPlayer().getWorld().playSound(meteor, Sound.ENTITY_GENERIC_EXPLODE, 1, 0);

                        //Getting targets hit
                        Targets = world.getNearbyLivingEntities(meteor, SpellAOE());

                        //damage handling
                        for (LivingEntity target : Targets)
                        {
                            if (!(target instanceof Player))
                            {
                                mobstats = MobUtility.getMobStats(target);
                                mobstats.spell_damage(MeteorDmgCalc(mobstats));
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
                        //deleting meteor
                        meteors.set(i, null);
                    }
                }
            }




        }
        if (getProgress() >= 200)
        {
            this.cancel();
        }

        incrementProgress();

        if (!this.isCancelled() && (getProgress() < 200))
        {
            ChannelTime.setProgress(1.0-(0.005 * getProgress()));
        }
        else if (getProgress() == 200)
        {
            if (ChannelTime != null)
                ChannelTime.removeAll();
            stats.setCastingSpell(false);
        }
    }

    public double MeteorDmgCalc(MobStats mobstats)
    {
        return 7.5 * (CasterSpellDamage() - (CasterSpellDamage() * (mobstats.getDefense() / (mobstats.getDefense() + 100))));
    }
    public double CasterSpellDamage() {
        return stats.getWisdom() * getCastingWand().getOffenseSpellPowerModifier();
    }

    public double SpellAOE() {
        return 2 * getCastingWand().getUtilitySpellPowerModifier();
    }

    public double ManaCostCalc(PlayerStats playerstats)
    {
        return 250.0 * getCastingWand().getSpellCostModifier();
    }

    @Override
    public void run() {
        cast();
    }

}
