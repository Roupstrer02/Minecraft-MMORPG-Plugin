package me.roupen.firstpluginthree.magic;

import me.roupen.firstpluginthree.constantrunnables.spellcasting;
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

public class Turret extends spellcasting {

    //Progress dictates what stage of the spell has been reached
    private int progress = 0;

    private Player origin;
    private PlayerStats stats;
    private World world;
    private Location loc;
    private Collection<LivingEntity> Targets;
    private BossBar ChannelTime;

    //Need a variable that holds the wand in order to easily apply the modifiers onto the spell (without coupling code)

    public Turret(Player caster)
    {
        this.origin = caster;
        this.stats = PlayerUtility.getPlayerStats(this.origin);
        this.world = origin.getWorld();

        setSpellName("Clockwork Turret");
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
                ChannelTime = Bukkit.createBossBar(this.spellName, BarColor.RED, BarStyle.SOLID);
                ChannelTime.addPlayer(stats.getPlayer());
                ChannelTime.setVisible(true);

                //makes sound
                stats.getPlayer().getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 0);

                //effect of spell

            }

            //If the player doesn't have the mana for the spell
            else {
                stats.setCastingSpell(false);
                this.cancel();
            }
        }
        else if ((progress > 0) && (progress < 100)) {

        }

        if (progress >= 50)
        {
            this.cancel();
        }

        //progress counter
        incrementProgress();

        if (!this.isCancelled() && (progress < 50))
        {
            ChannelTime.setProgress(1.0-(0.02 * getProgress()));
            ChannelTime.setTitle("Spell Cooldown " + NumberFormat.format(spellCooldownTextUpdate(50, progress)));
        }
        else if (getProgress() == 50)
        {
            if (ChannelTime != null)
                ChannelTime.removeAll();
            stats.setCastingSpell(false);
        }



    }

    public double FlameBoosterDamageCalc(MobStats mobstats)
    {
        return 5 * (CasterSpellDamage() - (CasterSpellDamage() * (mobstats.getDefense() / (mobstats.getDefense() + 100))));
    }
    public double SpellAOE() {
        return 2.5 * getCastingWand().getUtilitySpellPowerModifier();
    }
    public double CasterSpellDamage() {
        return stats.getWisdom() * getCastingWand().getOffenseSpellPowerModifier();
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
