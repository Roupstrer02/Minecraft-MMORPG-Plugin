package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.MobUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;
import java.util.Objects;

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

                    pStats.setLevel(1);
                    pStats.setExperience(0);
                }

            }
        }

        return true;
    }
}
