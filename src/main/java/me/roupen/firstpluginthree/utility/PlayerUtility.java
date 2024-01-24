package me.roupen.firstpluginthree.utility;

import me.roupen.firstpluginthree.data.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class PlayerUtility {
    private static Map<String, PlayerStats> playerStatsMap = new HashMap<>();

    public static PlayerStats getPlayerStats(Player p){
        if(!playerStatsMap.containsKey(p.getUniqueId().toString())){
            PlayerStats s = new PlayerStats();
            playerStatsMap.put(p.getUniqueId().toString(), s);

            return s;
        }
        return playerStatsMap.get(p.getUniqueId().toString());
    }

    public static void setPlayerStats(Player p, PlayerStats stats){
        String uuid = p.getUniqueId().toString();

        playerStatsMap.put(uuid, stats);
    }

    public static void SavePlayerStats(Player player)
    {
        PlayerStats stats = PlayerUtility.getPlayerStats(player);
        File f = new File(PlayerUtility.getFolderPath(player) + "/general.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        cfg.set("stats.Vitality", stats.getVitality());
        cfg.set("stats.Resilience", stats.getResilience());
        cfg.set("stats.Intelligence", stats.getIntelligence());

        cfg.set("stats.Strength", stats.getStrength());
        cfg.set("stats.Dexterity", stats.getDexterity());
        cfg.set("stats.Wisdom", stats.getWisdom());

        cfg.set("stats.Experience", stats.getExperience());
        cfg.set("stats.Level", stats.getLevel());
        cfg.set("stats.SkillPoints", stats.getSkillPoints());

        cfg.set("stats.CurrentHealth", stats.getActiveCurrentHealth());
        cfg.set("stats.CurrentMana", stats.getActiveCurrentMana());
        cfg.set("stats.CurrentStamina", stats.getActiveCurrentStamina());


        try { cfg.save(f); } catch (IOException e){ e.printStackTrace(); }
        PlayerUtility.setPlayerStats(player, null);
    }

    public static void printPlayerStats(Player p)
    {
        PlayerStats s = getPlayerStats(p);
        String str = s.toString();
        String[] messages = str.split("\n");
        for (String m : messages)
        {
            p.chat(m);
        }
    }

    /*
    -
    - Server file related functionality
    -
     */

    public static String getFolderPath(Player p){
        return Bukkit.getPluginsFolder().getAbsolutePath() + "/FirstPluginThree/player/" + p.getUniqueId();
    }

}
