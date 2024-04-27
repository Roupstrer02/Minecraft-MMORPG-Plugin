package me.roupen.firstpluginthree.constantrunnables;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.misc.misc;
import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.weather.WeatherForecast;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class actionbardisplay extends BukkitRunnable {
    //style
    private final Style styleHealth;
    private final Style styleStamina;
    private final Style styleMana;

    private PlayerStats stats;

    public actionbardisplay()//brad?
    {

        this.styleHealth = Style.style(NamedTextColor.RED, TextDecoration.BOLD);
        this.styleStamina = Style.style(NamedTextColor.YELLOW, TextDecoration.BOLD);
        this.styleMana = Style.style(NamedTextColor.AQUA, TextDecoration.BOLD);
    }

    @Override
    public void run() {

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        if (!players.isEmpty()){
            for (Player player : players) {
                // What you want to schedule goes here

                //This is where the player variable will be passed into the PlayerEquipment constructor to get the relevant equipment before stat updates
                stats = PlayerUtility.getPlayerStats(player);
                stats.updateEquipment(player);

                stats.recalculateMaxHealth();
                stats.recalculateDamage();
                stats.recalculateDefense();
                stats.recalculateMaxStamina();
                stats.recalculateMaxMana();

                stats.recalculateStamina();
                stats.recalculateHealth();
                stats.recalculateMana();

                stats.recalculateStaminaCost();

                stats.recalculateMovementSpeed(player);
                stats.recalculateMultihit();
                stats.recalculateCritChance();
                stats.recalculateCritDamageMult();

                if (stats.getActiveCurrentHealth() <= 0 || player.isDead())
                {
                    stats.setActiveCurrentHealth(0);
                    player.setHealth(0);
                }
                else
                {
                    player.setHealth(Math.min(20, 20 * (stats.getActiveCurrentHealth() / stats.getActiveMaxHealth())));
                }
                String playerBiome = WeatherForecast.getPlayerBiome(player);
                if (!playerBiome.equals(stats.currentBiomeGroup)) {
                    stats.currentBiomeGroup = playerBiome;
                    player.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title "+ player.getName() + " title [\"\",{\"text\":\"" + WeatherForecast.getPlayerBiome(player) + "\",\"color\":\"" + "white" + "\",\"bold\":true}]");
                    player.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title "+ player.getName() + " subtitle [\"\",{\"text\":\"" + WeatherForecast.WeatherDesigns[WeatherForecast.getPlayerWeather(player)] + "\",\"color\":\"" + WeatherForecast.colorNames[WeatherForecast.getPlayerWeather(player)] + "\",\"bold\":false}]");

                }

                player.sendActionBar(Component.text()
                        .append(Component.text((int) Math.floor(stats.getActiveCurrentHealth()) + "/" + (int) stats.getActiveMaxHealth() + " | ", styleHealth)
                                .append(Component.text(misc.round(stats.getActiveCurrentStamina()) + "/" + stats.getActiveMaxStamina() + " | ", styleStamina)))
                        .append(Component.text((int) Math.floor(stats.getActiveCurrentMana()) + "/" + (int) stats.getActiveMaxMana(), styleMana)).build());


            }
        }



    }
}
