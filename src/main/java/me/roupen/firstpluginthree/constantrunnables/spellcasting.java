package me.roupen.firstpluginthree.constantrunnables;

//i wish i could import roupen irl D: -Armen
import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.magic.Fireball;
import me.roupen.firstpluginthree.magic.FlameBooster;
import me.roupen.firstpluginthree.magic.FlameDash;
import me.roupen.firstpluginthree.magic.SteamRocketPack;
import me.roupen.firstpluginthree.wands.wand;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class spellcasting extends BukkitRunnable {

    //all spells run every tick
    protected String spellName;
    protected wand CastingWand;


    public static void cast(Player caster, String input_spell)
    {
        int period = 1;
        long delay = 0L;

        switch (input_spell)
        {
            case "Fireball":
               BukkitTask Fireball = new Fireball(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
               break;
            case "Flame Dash":
                BukkitTask FlameDash = new FlameDash(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
                break;
            case "Steam Rocket Pack":
                BukkitTask SteamRocketPack = new SteamRocketPack(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
            case "Flame Booster":
                BukkitTask FlameBooster = new FlameBooster(caster).runTaskTimer(FirstPluginThree.getMyPlugin(), delay, period);
            default:
        }
    }

    protected void setSpellName(String name)
    {
        this.spellName = name;
    }
    protected void setCastingWand(wand Wand) {this.CastingWand = Wand;}
    protected void ParticleSphere(Location loc, double radius) {

        for(double phi = 0; phi <= Math.PI; phi += Math.PI / (radius * 5)) {
            double y = radius * Math.cos(phi) + 1.5;
            for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 10)) {
                double x = radius * Math.cos(theta) * Math.sin(phi);
                double z = radius * Math.sin(theta) * Math.sin(phi);

                loc.add(x, y, z);
                loc.getWorld().spawnParticle(Particle.FLAME, loc, 1, 0F, 0F, 0F, 0.001);
                loc.subtract(x, y, z);
            }
        }
    }

    protected wand getCastingWand() {return this.CastingWand;}
    //The run() function in this class should NEVER run
    @Override
    public void run() {

    }

}

