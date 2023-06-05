package me.roupen.firstpluginthree.utility;

import me.roupen.firstpluginthree.data.PlayerStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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

    public static void UpdatePlayerStats()
    {

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
