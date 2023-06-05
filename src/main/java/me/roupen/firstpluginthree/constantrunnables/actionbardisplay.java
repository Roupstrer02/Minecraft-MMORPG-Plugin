package me.roupen.firstpluginthree.constantrunnables;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.UUID;

public class actionbardisplay extends BukkitRunnable {

    private final Player player;
    private final Style styleHealth;
    private final Style styleDefense;
    private final Style styleMana;

    private PlayerStats stats;

    public actionbardisplay(Player player)
    {
        this.player = player;
        this.stats = PlayerUtility.getPlayerStats(player);
        this.styleHealth = Style.style(NamedTextColor.RED, TextDecoration.BOLD);
        this.styleDefense = Style.style(NamedTextColor.GREEN, TextDecoration.BOLD);
        this.styleMana = Style.style(NamedTextColor.AQUA, TextDecoration.BOLD);
    }

    @Override
    public void run() {

        // What you want to schedule goes here
        stats = PlayerUtility.getPlayerStats(player);

        player.sendActionBar(Component.text()
                .append(Component.text(stats.getActiveCurrentHealth() + "/" + stats.getActiveMaxHealth() + " ", styleHealth)
                        .append(Component.text(stats.getActiveDefense() + "Armor ", styleDefense)))
                .append(Component.text(stats.getActiveCurrentMana() + "/" + stats.getActiveMaxMana(), styleMana)).build());


    }
}
