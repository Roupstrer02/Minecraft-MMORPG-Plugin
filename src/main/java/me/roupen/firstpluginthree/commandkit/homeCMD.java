package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class homeCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            PlayerStats stats = PlayerUtility.getPlayerStats(player);
            List<Double> HomeLocation = stats.getHomeLocation();

            if (HomeLocation != null && HomeLocation.size() == 4) {

                //checks if player is in boss fight
                if (!stats.isInBossFight()) {
                    if (Objects.equals(stats.getPlayerDimensionID(), stats.getHomeDimension())) {
                        player.teleport(new Location(player.getWorld(), HomeLocation.get(0), HomeLocation.get(1), HomeLocation.get(2)));
                        player.sendMessage(Component.text("Teleported Home!", Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)));
                    }else{
                        player.sendMessage(Component.text("Cannot teleport home: Home is not set", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                        player.getWorld().playSound(player, Sound.ENTITY_BLAZE_HURT, 1.0f, 1.0f);
                    }
                }
                else
                {
                    player.sendMessage(Component.text("Cannot teleport home: You have challenged an arbiter", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                    player.getWorld().playSound(player, Sound.ENTITY_BLAZE_HURT, 1.0f, 1.0f);
                }
            }
            else {
                player.sendMessage(Component.text("Cannot teleport home: Home is not set", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                player.getWorld().playSound(player, Sound.ENTITY_BLAZE_HURT, 1.0f, 1.0f);
            }
        }

        return true;
    }
}
