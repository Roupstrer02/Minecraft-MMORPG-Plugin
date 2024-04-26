package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.weather.WeatherForecast;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class weatherCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            WeatherForecast.WeatherReport(player);

        }

        return true;
    }
}
