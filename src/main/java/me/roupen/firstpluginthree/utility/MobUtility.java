package me.roupen.firstpluginthree.utility;

import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.weather.WeatherForecast;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static org.bukkit.Bukkit.selectEntities;

public class MobUtility {

    private static Map<String, MobStats> mobStatsMap = new HashMap<>();

    public static MobStats getMobStats(Entity entity)
    {
        if(!mobStatsMap.containsKey(entity.getUniqueId().toString())){
            MobStats s = new MobStats(entity, WeatherForecast.getWeather(entity));
            mobStatsMap.put(entity.getUniqueId().toString(), s);

            return s;
        }
        return mobStatsMap.get(entity.getUniqueId().toString());
    }

    public static void setMobStats(Entity e, MobStats stats)
    {
        String uuid = e.getUniqueId().toString();

        mobStatsMap.put(uuid, stats);
    }

}
