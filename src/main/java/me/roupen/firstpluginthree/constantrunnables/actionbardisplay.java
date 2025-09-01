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

import javax.inject.Named;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class actionbardisplay extends BukkitRunnable {
    //style
    private final Style styleHealth;
    private final Style styleStamina;
    private final Style styleMana;
    private final Style styleConsumableIndicator;
    private String ConsumableIndicatorState;
    private String ConsumableIndicateSeparator;
    private String uselessEquipmentIndicator;
    private String uselessEquipmentSeparator;

    private PlayerStats stats;



    public actionbardisplay()//brad?
    {

        this.styleHealth = Style.style(NamedTextColor.RED, TextDecoration.BOLD);
        this.styleStamina = Style.style(NamedTextColor.YELLOW, TextDecoration.BOLD);
        this.styleMana = Style.style(NamedTextColor.DARK_AQUA, TextDecoration.BOLD);
        this.styleConsumableIndicator = Style.style(NamedTextColor.GREEN, TextDecoration.BOLD);
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

                player.setLevel(stats.getLevel());
                player.setExp(0F);
                player.setExp(stats.getExperience() / (float) stats.getLevelCap());

                if (stats.getActiveCurrentHealth() <= 0 || player.isDead())
                {
                    stats.setActiveCurrentHealth(0);
                    player.setHealth(0);
                }
                else
                {
                    player.setHealth(
                            Math.max(1, Math.min(20, Math.round(20 * (stats.getActiveCurrentHealth() / stats.getActiveMaxHealth())))));
                }

                if (stats.hasConsumedItem()) {
                    ConsumableIndicatorState = "Well Fed";
                    ConsumableIndicateSeparator = " | ";
                }
                else {
                    ConsumableIndicatorState = "";
                    ConsumableIndicateSeparator = "";
                }

                if (stats.equippedUselessGear) {
                    uselessEquipmentIndicator = "!Equipment too high level!";
                    uselessEquipmentSeparator = " | ";
                }
                else {
                    uselessEquipmentIndicator = "";
                    uselessEquipmentSeparator = "";
                }
                player.setSaturation(10);

                String playerBiome = WeatherForecast.getPlayerBiome(player);
                if (!playerBiome.equals(stats.currentBiomeGroup)) {
                    stats.currentBiomeGroup = playerBiome;
                    player.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title "+ player.getName() + " title [\"\",{\"text\":\"\"}]");
                    player.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title "+ player.getName() +
                            " subtitle [\"\"," +
                            "{\"text\":\"" + WeatherForecast.WeatherDesigns[WeatherForecast.getPlayerWeather(player)] + "\",\"color\":\"" + WeatherForecast.colorNames[WeatherForecast.getPlayerWeather(player)] + "\",\"bold\":false}," +
                            "{\"text\":\"" + WeatherForecast.getLevelRangeDisplayName(player) + "\",\"color\":\"none\",\"bold\":false}]");

                }

                player.sendActionBar(Component.text()
                        .append(Component.text(uselessEquipmentIndicator + uselessEquipmentSeparator, Style.style(NamedTextColor.DARK_RED)))
                        .append(Component.text(misc.getplayerHPDisplay(stats.getActiveCurrentHealth())  + "/" + misc.getplayerHPDisplay(stats.getActiveMaxHealth()), styleHealth)
                        .append(Component.text(" | ", Style.style(NamedTextColor.GOLD, TextDecoration.BOLD)))
                        .append(Component.text(misc.round(stats.getActiveCurrentStamina()) + "/" + stats.getActiveMaxStamina(), styleStamina)))
                        .append(Component.text(" | ", Style.style(NamedTextColor.GREEN, TextDecoration.BOLD)))
                        .append(Component.text((int) Math.floor(stats.getActiveCurrentMana()) + "/" + (int) stats.getActiveMaxMana(), styleMana))
                        .append(Component.text(ConsumableIndicateSeparator, Style.style(NamedTextColor.AQUA, TextDecoration.BOLD)))
                        .append(Component.text(ConsumableIndicatorState, styleConsumableIndicator)).build());


            }
        }



    }
}
