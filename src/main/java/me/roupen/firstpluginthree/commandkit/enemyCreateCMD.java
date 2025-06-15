package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.MobUtility;
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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class enemyCreateCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        //creates an enemy of a given level, for testing purposes
        if (sender instanceof Player)
        {
            if (args.length > 0) {
                int count = 1;
                if (args.length > 1) {
                    count *= Integer.parseInt(args[1]);
                }

                int levelToSpawn = Integer.parseInt(args[0]);
                Player player = (Player) sender;

                for (int i = 0; i < count; i++) {
                    LivingEntity new_mob = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
                    MobStats.giveCustomStatBlock(new_mob, levelToSpawn);
                }
            }
        }

        return true;
    }
}
