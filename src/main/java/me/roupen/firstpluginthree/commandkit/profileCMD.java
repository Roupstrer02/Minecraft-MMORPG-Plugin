package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.customgui.GuiUtility;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class profileCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            GuiUtility.CreateProfileGui(player);
        }

        return true;
    }

}
