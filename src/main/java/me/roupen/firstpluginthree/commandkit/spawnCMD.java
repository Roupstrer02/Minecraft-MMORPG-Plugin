package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.Zelandris;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class spawnCMD implements CommandExecutor {

    private final String worldName = "world";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;
            PlayerStats pStats = PlayerUtility.getPlayerStats(p);

            Location spawnLoc = new Location(p.getWorld(), -75.5, 111, 524.5);

            if (!pStats.isInBossFight())
                if (p.getWorld().getName().equals(worldName))
                    p.teleport(spawnLoc);
                else
                    p.sendMessage(Component.text("Cannot Teleport: Spawn in different dimension", Style.style(NamedTextColor.RED, TextDecoration.ITALIC))); //put error messages here
            else
                p.sendMessage(Component.text("You should've thought of that before challenging an elite!", Style.style(NamedTextColor.RED, TextDecoration.ITALIC))); //put error messages here

            return true;
        }

        return false;
    }
}
