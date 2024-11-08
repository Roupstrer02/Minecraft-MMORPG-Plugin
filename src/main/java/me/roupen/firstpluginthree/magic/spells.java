package me.roupen.firstpluginthree.magic;

//i wish i could import roupen irl D: -Armen
import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.magic.*;
import me.roupen.firstpluginthree.wands.wand;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

public class spells {

    //all spells run every tick (20/s)
    protected wand CastingWand;

    private DecimalFormat NumberFormat = new DecimalFormat("0.0");

    protected void ParticleRing(Location loc, double radius, Particle particletype) {
        for(double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / (radius * 10)) {
            double x = radius * Math.cos(theta);
            double z = radius * Math.sin(theta);

            loc.add(x, 0, z);
            loc.getWorld().spawnParticle(particletype, loc, 1, 0F, 0F, 0F, 0.001);
            loc.subtract(x, 0, z);
        }

    }
    public static void ParticleCircle(Location loc, double radius, Particle particletype, boolean continuous) {
        Random rd = new Random();
        double x, z, randTheta, noise;
        double factor = 1.0;

        if (continuous)
            factor = 0.25;

        for (int i = 0; i <= factor * 20 * radius; i++) {
            randTheta = rd.nextDouble() * 2 * Math.PI;
            noise = (rd.nextDouble() / 10) + 1;
            x = radius * Math.cos(randTheta) * noise;
            z = radius * Math.sin(randTheta) * noise;

            loc.add(x, 0, z);
            loc.getWorld().spawnParticle(particletype, loc, 1, 0F, 0F, 0F, 0.001);

            loc.subtract(x, 0, z);
        }

        loc.getWorld().spawnParticle(particletype, loc, (int) (factor * 5 * radius * radius), (float) radius / 2, 0F, (float) radius / 2, 0.001);

    }

    public static void ParticleVerticalCircle(Location loc, double radius, int numberOfDots, Particle particletype) {

        Vector lookVector = loc.getDirection();


        double[] originalCircleX = new double[numberOfDots];
        double[] originalCircleY = new double[numberOfDots];
        double[] originalCircleZ = new double[numberOfDots];
        Arrays.fill(originalCircleX, 0);

        for (int i = 0; i < numberOfDots; i++) {
            originalCircleY[i] = Math.sin(i * 2 * (Math.PI / (double) numberOfDots));
            originalCircleZ[i] = Math.cos(i * 2 * (Math.PI / (double) numberOfDots));
        }

        double playerYaw = Math.atan2(lookVector.getZ(), lookVector.getX());
        double playerPitch = -Math.atan2(lookVector.getY(), Math.sqrt(Math.pow(lookVector.getZ(), 2) + Math.pow(lookVector.getX(), 2)));


        //not pretty code >:(
        double a = Math.cos(playerYaw);
        double b = Math.sin(playerYaw);
        double c = Math.cos(playerPitch);
        double d = Math.sin(playerPitch);

        double[] theCircleOfAllTimeX = new double[numberOfDots];
        double[] theCircleOfAllTimeY = new double[numberOfDots];
        double[] theCircleOfAllTimeZ = new double[numberOfDots];

        for (int i = 0; i < numberOfDots; i++) {
            theCircleOfAllTimeX[i] = (a * d * originalCircleY[i]) - (b * originalCircleZ[i]);
            theCircleOfAllTimeZ[i] = (a * originalCircleZ[i]) + (b * d * originalCircleY[i]);
            theCircleOfAllTimeY[i] = (c * originalCircleY[i]);
            //draw a particle here
            loc = loc.add(radius * theCircleOfAllTimeX[i], radius * theCircleOfAllTimeY[i], radius * theCircleOfAllTimeZ[i]);
            loc.getWorld().spawnParticle(particletype, loc, 1, 0, 0, 0, 0, null, true);
            loc = loc.subtract(radius * theCircleOfAllTimeX[i], radius * theCircleOfAllTimeY[i], radius * theCircleOfAllTimeZ[i]);
        }

    }



    protected wand getCastingWand() {return this.CastingWand;}
    //The run() function in this class should NEVER run

}

