package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.CraftingRecipes.SmithingRecipes;
import me.roupen.firstpluginthree.Zelandris;
import me.roupen.firstpluginthree.data.PlayerStats;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collection;
import java.util.List;

public class playerLevelResetCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {

        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (args.length == 1 && player.isOp()) {

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

            } else {
                player.sendMessage(Component.text("You do not have permission to use this command",Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                return false;

            }
        }




        else if (args.length == 3) {

            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            Location loc = new Location(sender.getServer().getWorld("world"), x, y, z);
            Collection<Player> players = loc.getNearbyPlayers(4.0);

            if (players.size() == 1) {
                Player player = (Player) players.toArray()[0];
                PlayerInventory inv = player.getInventory();
                ItemStack ticketItem = inv.getItemInMainHand();
                ItemStack testItem = inv.getItemInMainHand();
                testItem.setAmount(1);

                if (Zelandris.getSmithingrecipes().getItems().containsValue(testItem)) {
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

                    ticketItem.setAmount(ticketItem.getAmount() - 1);
                    inv.setItem(inv.getHeldItemSlot(), ticketItem);
                }
            } else if (players.size() > 1) {
                for (Player p : players) {
                    p.sendMessage(Component.text("There can only be one player in range of the Skill Reallocation Machine", Style.style(NamedTextColor.RED, TextDecoration.ITALIC)));
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1f, 1f);
                }
            }




        }




        return true;
    }
}
