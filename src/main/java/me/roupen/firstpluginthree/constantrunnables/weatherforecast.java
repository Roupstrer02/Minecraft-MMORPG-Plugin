package me.roupen.firstpluginthree.constantrunnables;

import me.roupen.firstpluginthree.Zelandris;
import me.roupen.firstpluginthree.misc.misc;
import me.roupen.firstpluginthree.weather.WeatherForecast;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class weatherforecast extends BukkitRunnable {
    public void run() {


        World w = Zelandris.getMyPlugin().getServer().getWorld("world");
        long time = w.getTime();
        if ((time >= 0) && (time < 20)) {
            WeatherForecast.DisplayForecast();
            misc.UndeadBurn(w);
        }


    }
}
