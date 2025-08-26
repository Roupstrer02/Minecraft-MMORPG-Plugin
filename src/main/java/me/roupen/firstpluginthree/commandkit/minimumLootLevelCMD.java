package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class minimumLootLevelCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        //creates an enemy of a given level, for testing purposes
        if (sender instanceof Player)
        {
            Player p = (Player) sender;
            PlayerStats pStats = PlayerUtility.getPlayerStats(p);
            boolean validUse = true;
            try {
                if (args.length == 1 && Integer.parseInt(args[0]) >= 0) {
                    pStats.EquipmentLevelMinimum = Integer.parseInt(args[0]);
                }else {
                    validUse = false;
                }
            } catch (NumberFormatException e) {
                validUse = false;
            }
            if (validUse)
                p.sendMessage(Component.text("Minimum equipment drop level set to level " + pStats.EquipmentLevelMinimum, Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)));
            else
                p.sendMessage(Component.text("Invalid use of command - Usage example: /minimum_loot_level 10", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));

            return validUse;
        }
        //if sender not player
        return false;

    }
}
