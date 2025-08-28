package me.roupen.firstpluginthree.elite;

import io.lumine.mythic.api.exceptions.InvalidMobTypeException;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.roupen.firstpluginthree.Zelandris;
import me.roupen.firstpluginthree.playerequipment.Rune;
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


    public static ArrayList<String> roamingEliteTypes = new ArrayList<String>() {{
        add("MythicMob{ArcaneGolem}");
        add("MythicMob{Quakefish}");
        add("MythicMob{LunarisStag}");
    }};

    public static final ArrayList<String> roamingEliteNames = new ArrayList<String>() {{
        add("Arcane Golem");
        add("Quake Fish");
        add("Lunaris Stag");
    }};


    public static ArrayList<String> ArenaEliteTypes = new ArrayList<String>() {{
        add("MythicMob{LarianLow}");
        add("MythicMob{LarianMid}");
        add("MythicMob{Larian}");
        add("MythicMob{AbyssWatcherTest}");
    }};
    public elite(String boss_name)
    {
        BossName = boss_name;

    }
    public static void generateArenaEliteDrops(MythicMobDeathEvent event) {
        Random rd = new Random();
        ActiveMob mmob = event.getMob();

        //drop rates

        //this item always drops, but can be changed later
        final double NetherStarDropRate = 0.5;
        final double Tier2RuneDropRate = 1.0;
        final double Tier1RuneDropRate = 0.25;
        String mmobName = mmob.getType().toString();
        int RewardRollCount;
        switch (mmobName) {
            case "MythicMob{LarianLow}":
                RewardRollCount = 1;
                break;
            case "MythicMob{LarianMid}":
                RewardRollCount = 2;
                break;
            case "MythicMob{Larian}":
                RewardRollCount = 3;
                break;
            default:
                RewardRollCount = 0;
        }

        if (ArenaEliteTypes.contains(mmobName)) {

            Entity ent = mmob.getEntity().getBukkitEntity();
            Location entLoc = ent.getLocation();
            for (int i = 0; i < RewardRollCount; i++) {

                //rolls for each mob drop
                for (Player p : Zelandris.PlayersInBossFight) {

                    //Nether Star
                    if (rd.nextDouble() < NetherStarDropRate) {
                        if (p.getInventory().firstEmpty() != -1)
                            p.getInventory().addItem(new ItemStack(Material.NETHER_STAR, 1));
                        else
                            p.getWorld().dropItem(entLoc, new ItemStack(Material.NETHER_STAR, 1));
                    }

                    //tier 2 rune
                    if (rd.nextDouble() <= Tier2RuneDropRate) {

                        p.getInventory().addItem(Rune.GenerateRandomTier2Rune());

                    }

                    //tier 1 rune
                    if (rd.nextDouble() < Tier1RuneDropRate) {
                        p.getInventory().addItem(Rune.GenerateRandomTier1Rune());
                    }
                }
            }
        }
    }
    public static void generateRoamingEliteDrops(MythicMobDeathEvent event) {
        Random rd = new Random();
        ActiveMob mmob = event.getMob();

        //drop rates
        final double runeDropRate = 0.3;
        final double netherStarDropRate = 0.25;

        if (roamingEliteTypes.contains(mmob.getType().toString())) {

            Entity ent = mmob.getEntity().getBukkitEntity();
            Location entLoc = ent.getLocation();

            for (int i = 0; i < 4; i++) {
                //rolls for each mob drop
                if (rd.nextDouble() < runeDropRate) {
                    ent.getWorld().dropItem(entLoc, Rune.GenerateRandomTier1Rune());
                }
            }

            if (rd.nextDouble() < netherStarDropRate) {
                ent.getWorld().dropItem(entLoc, new ItemStack(Material.NETHER_STAR));
            }
        }


    }


    public void spawn(Location spawnLoc) throws InvalidMobTypeException {
        switch (BossName) {
            case "Abyss Watcher":
                Zelandris.getMMHelper().spawnMythicMob("AbyssWatcherTest", spawnLoc);
                break;
            case "Larian the Night Hunter":
                Zelandris.getMMHelper().spawnMythicMob("LarianLow", spawnLoc);
                break;
            case "Larian the Berserker":
                Zelandris.getMMHelper().spawnMythicMob("LarianMid", spawnLoc);
                break;
            case "Larian the Nightmare":
                Zelandris.getMMHelper().spawnMythicMob("Larian", spawnLoc);
                break;
        }
    }

}
