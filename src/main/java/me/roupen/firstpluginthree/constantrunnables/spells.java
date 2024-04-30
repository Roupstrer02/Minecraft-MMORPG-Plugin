package me.roupen.firstpluginthree.constantrunnables;

//i wish i could import roupen irl D: -Armen
import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.magic.*;
import me.roupen.firstpluginthree.wands.wand;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;

public class spells extends BukkitRunnable {

    //all spells run every tick (20/s)
    protected String spellName;
    protected wand CastingWand;

    //private BukkitTask Fireball = new Fireball();

    protected DecimalFormat NumberFormat = new DecimalFormat("0.0");





    protected void setSpellName(String name)
    {
        this.spellName = name;
    }
    protected void setCastingWand(wand Wand) {this.CastingWand = Wand;}
    protected void ParticleSphere(Location loc, double radius, Particle particletype) {

        for(double phi = 0; phi <= Math.PI; phi += Math.PI / (radius * 5)) {
            double y = radius * Math.cos(phi) + 1.5;
            for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 10)) {
                double x = radius * Math.cos(theta) * Math.sin(phi);
                double z = radius * Math.sin(theta) * Math.sin(phi);

                loc.add(x, y, z);
                loc.getWorld().spawnParticle(particletype, loc, 1, 0F, 0F, 0F, 0.001);
                loc.subtract(x, y, z);
            }
        }
    }

    protected void ParticleSphere(Location loc, double radius, Particle particletype, int densityX, int densityY) {

        for(double phi = 0; phi <= Math.PI; phi += Math.PI / (radius * densityY)) {
            double y = radius * Math.cos(phi) + 1.5;
            for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * densityX)) {
                double x = radius * Math.cos(theta) * Math.sin(phi);
                double z = radius * Math.sin(theta) * Math.sin(phi);

                loc.add(x, y, z);
                loc.getWorld().spawnParticle(particletype, loc, 1, 0F, 0F, 0F, 0.001);
                loc.subtract(x, y, z);
            }
        }
    }

    protected void ParticleRing(Location loc, double radius, Particle particletype) {
        for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 10)) {
            double x = radius * Math.cos(theta);
            double z = radius * Math.sin(theta);

            loc.add(x, 0, z);
            loc.getWorld().spawnParticle(particletype, loc, 1, 0F, 0F, 0F, 0.001);
            loc.subtract(x, 0, z);
        }

    }
    protected void ParticleCircle(Location loc, double radius, Particle particletype) {

        for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 5)) {
            double x = radius * Math.cos(theta);
            double z = radius * Math.sin(theta);

            loc.add(x, 0, z);
            loc.getWorld().spawnParticle(particletype, loc, 1, 0F, 0F, 0F, 0.001);
            loc.subtract(x, 0, z);
        }
        loc.getWorld().spawnParticle(particletype, loc, (int) (5 * radius), (float) radius / 2, 0F, (float) radius / 2, 0.001);

    }

    protected double spellCooldownTextUpdate(double upperLimit, double currentProgress) {
        double increment = 1.0/upperLimit;
        return (upperLimit * 0.05) - ((upperLimit * 0.05) * (increment * currentProgress));
    }

    protected wand getCastingWand() {return this.CastingWand;}
    //The run() function in this class should NEVER run
    @Override
    public void run() {

    }

}

