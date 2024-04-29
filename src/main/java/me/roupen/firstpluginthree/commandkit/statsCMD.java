package me.roupen.firstpluginthree.commandkit;

import me.roupen.firstpluginthree.customgui.GuiUtility;
import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class statsCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            PlayerStats stats = PlayerUtility.getPlayerStats(player);
            DecimalFormat df = new DecimalFormat("0.00");


            player.sendMessage(
                    Component.text("Max Health: ", Style.style(NamedTextColor.DARK_RED)).append(Component.text(df.format(stats.getActiveMaxHealth()) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Health Regen/s: ", Style.style(NamedTextColor.DARK_RED))).append(Component.text(df.format(stats.getActiveHealthRegen()) + "\n", Style.style(NamedTextColor.WHITE))) //make getActiveHealthRegen()
                            .append(Component.text("Defense: ", Style.style(NamedTextColor.GREEN))).append(Component.text(df.format(stats.getActiveDefense()) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Damage: ", Style.style(NamedTextColor.RED))).append(Component.text(df.format(stats.getActiveDamage()) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Max Mana: ", Style.style(NamedTextColor.BLUE))).append(Component.text(df.format(stats.getActiveMaxMana()) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Mana Regen/s: ", Style.style(NamedTextColor.BLUE))).append(Component.text(df.format(stats.getActiveManaRegen()) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Max Stamina: ", Style.style(NamedTextColor.YELLOW))).append(Component.text(df.format(stats.getActiveMaxStamina()) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Stamina Regen/s: ", Style.style(NamedTextColor.YELLOW))).append(Component.text(df.format(stats.getActiveStaminaRegen()) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Movement Speed%: ", Style.style(NamedTextColor.LIGHT_PURPLE))).append(Component.text(df.format((stats.getMovementSpeed() * 100)) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Multi-hit%: ", Style.style(NamedTextColor.WHITE))).append(Component.text(df.format((stats.getMultihit() * 100)) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Crit-chance%: ", Style.style(NamedTextColor.LIGHT_PURPLE))).append(Component.text(df.format((stats.getCritChance() * 100)) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Crit-damage%: ", Style.style(NamedTextColor.DARK_PURPLE))).append(Component.text(df.format((stats.getCritDamageMult() * 100)) + "\n", Style.style(NamedTextColor.WHITE)))
                            .append(Component.text("Healing Received%: ", Style.style(NamedTextColor.DARK_GREEN))).append(Component.text(df.format(stats.getHealingReceivedModifier() * 100), Style.style(NamedTextColor.WHITE)))
                    );
        }

        return true;
    }
}
