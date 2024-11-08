package me.roupen.firstpluginthree.elite;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import me.roupen.firstpluginthree.FirstPluginThree;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Boss;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

public class elite {
    private LivingEntity EliteMob;
    private String BossName;
    private World world;

    public elite(String boss_name)
    {
        BossName = boss_name;

    }

    public void spawn(Location spawnLoc) throws InvalidMobTypeException {
        if (BossName.equals("Abyss Watcher")) {
            FirstPluginThree.getMMHelper().spawnMythicMob("AbyssWatcherTest", spawnLoc);
        }
    }

}
