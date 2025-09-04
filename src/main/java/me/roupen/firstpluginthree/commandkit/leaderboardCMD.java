package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class leaderboardCMD implements CommandExecutor {
    public static final Style[] leaderboardStyles = {Style.style(NamedTextColor.GOLD), Style.style(NamedTextColor.AQUA), Style.style(NamedTextColor.DARK_AQUA), Style.style(NamedTextColor.WHITE), Style.style(NamedTextColor.WHITE)};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            String path = PlayerUtility.getPlayersFolder();
            Player p = (Player) sender;

            Component message = Component.text("");

            HashMap<String, Integer> playerLevels = new HashMap<>();

            message = message.append(Component.text("Top players:\n\n", Style.style(NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD)));

            File[] Fs =(new File(path)).listFiles();

            for (File F : Fs) {
                String uuid = F.getName();
                File f = new File(path + uuid + "/general.yml");
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

                String playerName = "";
                if (Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
                    playerName = Bukkit.getPlayer(UUID.fromString(uuid)).getName();
                } else {
                    playerName = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
                }



                playerLevels.put(playerName, cfg.getInt("stats.Level"));
            }
            for (int i = 1; i <= Math.min(5,playerLevels.size()); i++) {
                int maxValue = -1;
                String pn = "";
                for (String pName : playerLevels.keySet()) {

                    if (playerLevels.get(pName) != null) {
                        if (playerLevels.get(pName) > maxValue) {
                            maxValue = playerLevels.get(pName);
                            pn = pName;
                        }
                    }
                }
                message = message.append(Component.text( i + ". " + pn + ": Level " + maxValue + "\n", leaderboardStyles[i-1]));
                playerLevels.remove(pn);
            }
            p.sendMessage(message);
        }

        return true;
    }

}
