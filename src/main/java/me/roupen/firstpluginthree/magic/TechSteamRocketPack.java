//Armen K. was here, this was my idea BTW
//sure it was
//Armen G was also here

package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.wands.wand;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;


public class TechSteamRocketPack extends BukkitRunnable {

    private Player origin;
    private PlayerStats stats;
    private World world;
    private int progress;
    private wand Wand;
    private BossBar ChannelTime;
    private DecimalFormat NumberFormat = new DecimalFormat("0.0");

    public TechSteamRocketPack(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();

        this.Wand = wand.ItemToWand(caster.getInventory().getItemInOffHand());
    }

    public void cast() {

        if (getProgress() == 0) {


            if (stats.getActiveCurrentMana() >= ManaCostCalc()) {

                stats.spendMana(ManaCostCalc());

                ChannelTime = Bukkit.createBossBar("Spell Cooldown: ", BarColor.PINK, BarStyle.SOLID);
                ChannelTime.addPlayer(stats.getPlayer());
                ChannelTime.setVisible(true);

            } else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }

            //Move player in look direction
        if(getProgress() > 0 && getProgress() < 20){
            //look vector = velocity<-------------------------------
            origin.setVelocity(origin.getLocation().getDirection().multiply(Wand.getUtilitySpellPowerModifier()));
            Vector lookVector = origin.getLocation().getDirection();
            world.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, origin.getLocation().add(0,1,0), 0, -lookVector.getX(), -lookVector.getY(), -lookVector.getZ(), 0.4, null, true);


        }
        if (getProgress() >= 20)
        {
            this.cancel();
        }

            //Controls the spell cooldown
        if (!this.isCancelled() && (getProgress() < 20))
        {
            ChannelTime.setProgress(1.0-(0.05 * getProgress()));
            ChannelTime.setTitle("Spell Cooldown " + NumberFormat.format(spellCooldownTextUpdate(20, progress)));
        }
        else if (getProgress() == 20)
        {
            if (ChannelTime != null)
                ChannelTime.removeAll();
            stats.setCastingSpell(false);
        }

        incrementProgress();
    }

    public double ManaCostCalc() {
        return 30 * Wand.getSpellCostModifier();
    }

    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void incrementProgress() {setProgress(getProgress() + 1);}
    public double spellCooldownTextUpdate(double upperLimit, double currentProgress) {
        double increment = 1.0/upperLimit;
        return (upperLimit * 0.05) - ((upperLimit * 0.05) * (increment * currentProgress));
    }

    @Override
    public void run() {
        cast();
    }

}
