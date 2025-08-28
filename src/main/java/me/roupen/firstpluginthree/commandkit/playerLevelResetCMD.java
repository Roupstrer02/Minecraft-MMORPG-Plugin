package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class playerLevelResetCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {

        if (sender instanceof Player)
        {
            if (args.length == 1) {
                Player player = Bukkit.getPlayer(args[0]);

                if (player != null) {
                    PlayerStats pStats = PlayerUtility.getPlayerStats(player);
                    pStats.setSkillPoints(pStats.getLevel() - 1);
                    pStats.setVitality(1);
                    pStats.setResilience(1);
                    pStats.setStrength(1);
                    pStats.setDexterity(1);
                    pStats.setIntelligence(1);
                    pStats.setWisdom(1);
                    pStats.setArtisan(0);

                    pStats.setExperience(0);
                }

            }
        }

        return true;
    }
}
