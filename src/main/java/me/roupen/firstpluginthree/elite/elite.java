package me.roupen.firstpluginthree.elite;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.playerequipment.Rune;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class elite {
    private LivingEntity EliteMob;
    private String BossName;
    private World world;


    public static ArrayList<String> roamingEliteNames = new ArrayList<String>() {{
        add("MythicMob{ArcaneGolem}");
        add("MythicMob{Quakefish}");
        add("MythicMob{LunarisStag}");
    }};

    public static ArrayList<String> ArenaEliteNames = new ArrayList<String>() {{
        add("MythicMob{Larian}");
        add("MythicMob{AbyssWatcherTest}");
        add("MythicMob{Alarak}");
        add("MythicMob{QuakeFish}");
    }};
    public elite(String boss_name)
    {
        BossName = boss_name;

    }

    public static void generateRoamingEliteDrops(MythicMobDeathEvent event) {
        Random rd = new Random();
        ActiveMob mmob = event.getMob();

        //drop rates
        final double runeDropRate = 0.3;
        final double netherStarDropRate = 0.25;

        if (roamingEliteNames.contains(mmob.getType().toString())) {

            Entity ent = mmob.getEntity().getBukkitEntity();
            Location entLoc = ent.getLocation();

            //rolls for each mob drop
            if (rd.nextDouble() >= runeDropRate) {
                ent.getWorld().dropItem(entLoc, Rune.GenerateRandomTier1Rune());
            }

            if (rd.nextDouble() >= netherStarDropRate) {
                ent.getWorld().dropItem(entLoc, new ItemStack(Material.NETHER_STAR));
            }
        }


    }


    public void spawn(Location spawnLoc) throws InvalidMobTypeException {
        if (BossName.equals("Abyss Watcher")) {
            FirstPluginThree.getMMHelper().spawnMythicMob("AbyssWatcherTest", spawnLoc);
        }
    }

}
