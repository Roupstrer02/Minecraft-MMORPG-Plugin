package me.roupen.firstpluginthree.misc;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;

import java.util.List;

public class misc {
    public static double round(double x) {
        return ((Math.floor(100 * x)) / 100);
    }

    public static void UndeadBurn(World world) {
        List<LivingEntity> mobs = world.getLivingEntities();

        for (LivingEntity mob : mobs) {
            if ((mob instanceof Zombie || mob instanceof Skeleton) && mob.getFireTicks() > 1) {
                mob.remove();
            }
        }
    }

}
