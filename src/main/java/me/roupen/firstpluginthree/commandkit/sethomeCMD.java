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
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class sethomeCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            Location loc = player.getLocation();
            PlayerStats stats = PlayerUtility.getPlayerStats(player);

            DecimalFormat df = new DecimalFormat("0.00");

            stats.updateHomeLocation();
            player.sendMessage(Component.text("New home set to: " + df.format(loc.getX()) + " " + df.format(loc.getY()) + " " + df.format(loc.getZ()), Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)));
            player.getWorld().playSound(player, Sound.ENTITY_VILLAGER_TRADE, 1.0f, 1.0f);
        }

        return true;
    }

}
