package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class playerSetLevelCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {

        if (sender instanceof Player)
        {
            if (args.length > 0) {
                Player player = Bukkit.getPlayer(args[0]);

                if (player != null) {
                    PlayerStats pStats = PlayerUtility.getPlayerStats(player);

                    if (args.length == 8) {
                        int level = -5;
                        int statLevel;
                        for (int i = 1; i < args.length; i++) {
                            statLevel = Integer.parseInt(args[i]);
                            if (i != args.length-1 && statLevel > 0) level += statLevel;
                            else if (i == args.length-1 && statLevel >= 0) level += statLevel;
                            else {
                                player.sendMessage(
                                        Component.text(
                                                "All stat levels must be > 0. Artisan can be 0.",
                                                Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                                return false;
                            }
                        }

                        pStats.setLevel(level);
                        pStats.setSkillPoints(0);
                        pStats.setVitality(Integer.parseInt(args[1]));
                        pStats.setResilience(Integer.parseInt(args[2]));
                        pStats.setStrength(Integer.parseInt(args[3]));
                        pStats.setDexterity(Integer.parseInt(args[4]));
                        pStats.setIntelligence(Integer.parseInt(args[5]));
                        pStats.setWisdom(Integer.parseInt(args[6]));
                        pStats.setArtisan(Integer.parseInt(args[7]));
                        pStats.setExperience(0);

                        player.sendMessage(
                                Component.text(
                                        "Level set to " + pStats.getLevel(),
                                        Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)));
                    }else{

                        int level = Integer.parseInt(args[1]);
                        pStats.setLevel(level);
                        pStats.setSkillPoints(pStats.getLevel() - 1);
                        pStats.setVitality(1);
                        pStats.setResilience(1);
                        pStats.setStrength(1);
                        pStats.setDexterity(1);
                        pStats.setIntelligence(1);
                        pStats.setWisdom(1);
                        pStats.setArtisan(0);
                        pStats.setExperience(0);
                        player.sendMessage(
                                Component.text(
                                        "Level set to " + pStats.getLevel() + ". Spend your points by using /profile",
                                        Style.style(NamedTextColor.GREEN, TextDecoration.ITALIC)));
                    }
                }

            }

        }

        return true;
    }
}
