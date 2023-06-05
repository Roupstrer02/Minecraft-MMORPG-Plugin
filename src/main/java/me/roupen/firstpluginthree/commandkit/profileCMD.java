package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.customgui.GuiUtility;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class profileCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            PlayerStats stats = PlayerUtility.getPlayerStats(player);
            GuiUtility gui = new GuiUtility(player, 45, "Player Stats");

            gui.addGuiItem(Material.RED_STAINED_GLASS_PANE, "Vitality: ", 4, Integer.toString(stats.getVitality()));
            gui.addGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Resilience: ", 11, Integer.toString(stats.getResilience()));
            gui.addGuiItem(Material.BLUE_STAINED_GLASS_PANE, "Intelligence: ", 15, Integer.toString(stats.getIntelligence()));
            gui.addGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "Strength: ", 29, Integer.toString(stats.getStrength()));
            gui.addGuiItem(Material.CYAN_STAINED_GLASS_PANE, "Wisdom: ", 33, Integer.toString(stats.getWisdom()));
            gui.addGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "Dexterity: ", 40, Integer.toString(stats.getDexterity()));
            gui.addGuiItem(Material.GRAY_STAINED_GLASS_PANE, "Level: ", 22, Integer.toString(stats.getLevel()));
            gui.addGuiItem(Material.DIAMOND, "Unused Skill points: ", 0, Integer.toString(stats.getSkillPoints()));

            gui.openInventory(player);
        }
        return true;
    }

}
