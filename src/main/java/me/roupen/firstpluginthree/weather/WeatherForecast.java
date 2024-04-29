package me.roupen.firstpluginthree.weather;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntitySpawnEvent;
import net.kyori.adventure.text.Component;

import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class WeatherForecast {
/*
0. "PLAINS", "SUNFLOWER_PLAINS", "RIVER", "MUSHROOM_FIELDS", "MEADOW"
1. "DESERT", "BEACH"
2. "FOREST", "BIRCH_FOREST", "DARK_FOREST", "WINDSWEPT_FOREST", "FLOWER_FOREST", "OLD_GROWTH_BIRCH_FOREST"
3. "TAIGA", "SNOWY_TAIGA", "OLD_GROWTH_PINE_TAIGA", "OLD_GROWTH_SPRUCE_TAIGA", "FROZEN_RIVER", "SNOWY_PLAINS", "ICE_SPIKES", "SNOWY_BEACH", "FROZEN_PEAKS", "SNOWY_SLOPES", "GROVE", "JAGGED_PEAKS"
4. "SWAMP", "MANGROVE_SWAMP"
5. "FROZEN_OCEAN", "WARM_OCEAN", "LUKEWARM_OCEAN", "COLD_OCEAN", "DEEP_LUKEWARM_OCEAN", "DEEP_COLD_OCEAN", "DEEP_FROZEN_OCEAN", "DEEP_OCEAN"
6. "JUNGLE", "SPARSE_JUNGLE", "BAMBOO_JUNGLE"
7. "DRIPSTONE_CAVES", "LUSH_CAVES", "DEEP_DARK"
8. "SAVANNA", "SAVANNA_PLATEAU", "BADLANDS", "WOODED_BADLANDS", "ERODED_BADLANDS"
9. "WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_SAVANNA", "WINDSWEPT_HILLS"
10. "STONY_SHORE", "STONY_PEAKS",
11. "THE_END", "SMALL_END_ISLANDS", "END_MIDLANDS", "END_HIGHLANDS", "END_BARRENS", "THE_VOID"
12. "NETHER_WASTES", "SOUL_SAND_VALLEY", "CRIMSON_FOREST", "WARPED_FOREST", "BASALT_DELTAS"
* */

    public static final String[] PlainsBiomeGroup = {"PLAINS", "SUNFLOWER_PLAINS", "RIVER", "MUSHROOM_FIELDS", "MEADOW"};
    public static final String[] SandBiomeGroup = {"DESERT", "BEACH"};
    public static final String[] ForestBiomeGroup = {"FOREST", "BIRCH_FOREST", "DARK_FOREST", "WINDSWEPT_FOREST", "FLOWER_FOREST", "OLD_GROWTH_BIRCH_FOREST"};
    public static final String[] TundraBiomeGroup = {"TAIGA", "SNOWY_TAIGA", "OLD_GROWTH_PINE_TAIGA", "OLD_GROWTH_SPRUCE_TAIGA", "FROZEN_RIVER", "SNOWY_PLAINS", "ICE_SPIKES", "SNOWY_BEACH", "FROZEN_PEAKS", "SNOWY_SLOPES", "GROVE", "JAGGED_PEAKS"};
    public static final String[] SwampBiomeGroup = {"SWAMP", "MANGROVE_SWAMP"};
    public static final String[] OceanBiomeGroup = {"OCEAN", "FROZEN_OCEAN", "WARM_OCEAN", "LUKEWARM_OCEAN", "COLD_OCEAN", "DEEP_LUKEWARM_OCEAN", "DEEP_COLD_OCEAN", "DEEP_FROZEN_OCEAN", "DEEP_OCEAN"};
    public static final String[] JungleBiomeGroup = {"JUNGLE", "SPARSE_JUNGLE", "BAMBOO_JUNGLE"};
    public static final String[] CavesBiomeGroup = {"DRIPSTONE_CAVES", "LUSH_CAVES", "DEEP_DARK"};
    public static final String[] SavannaBiomeGroup = {"SAVANNA", "SAVANNA_PLATEAU", "BADLANDS", "WOODED_BADLANDS", "ERODED_BADLANDS"};
    public static final String[] MountainousBiomeGroup = {"WINDSWEPT_GRAVELLY_HILLS", "WINDSWEPT_SAVANNA", "WINDSWEPT_HILLS"};
    public static final String[] StonyBiomeGroup = {"STONY_SHORE", "STONY_PEAKS",};
    public static final String[] EndBiomeGroup = {"THE_END", "SMALL_END_ISLANDS", "END_MIDLANDS", "END_HIGHLANDS", "END_BARRENS", "THE_VOID"};
    public static final String[] NetherBiomeGroup = {"NETHER_WASTES", "SOUL_SAND_VALLEY", "CRIMSON_FOREST", "WARPED_FOREST", "BASALT_DELTAS"};
    public static final ArrayList<String[]> AllBiomesList = new ArrayList<>(Arrays.asList(
            PlainsBiomeGroup, SandBiomeGroup, ForestBiomeGroup, TundraBiomeGroup, SwampBiomeGroup, OceanBiomeGroup, JungleBiomeGroup, CavesBiomeGroup, SavannaBiomeGroup, MountainousBiomeGroup, StonyBiomeGroup, EndBiomeGroup, NetherBiomeGroup
    ));
    public static final ArrayList<Style> styles = new ArrayList<>(Arrays.asList(
            Style.style(NamedTextColor.DARK_GRAY), Style.style(NamedTextColor.DARK_GREEN), Style.style(NamedTextColor.GREEN), Style.style(NamedTextColor.YELLOW), Style.style(NamedTextColor.GOLD), Style.style(NamedTextColor.AQUA), Style.style(NamedTextColor.DARK_AQUA),
            Style.style(NamedTextColor.BLUE), Style.style(NamedTextColor.DARK_BLUE), Style.style(NamedTextColor.LIGHT_PURPLE), Style.style(NamedTextColor.DARK_PURPLE), Style.style(NamedTextColor.RED), Style.style(NamedTextColor.DARK_RED)
    ));

    public static final String[] colorNames = new String[]
            {"dark_gray", "dark_green", "green", "yellow",
             "gold", "aqua", "dark_aqua", "blue", "dark_blue",
             "light_purple", "dark_purple", "red", "dark_red"};

    public static final String[] WeatherDesigns = {
            "Beautiful day", "Pleasant breeze", "Windy", "Strong gusts", "Harsh Rainfall", "Stormy day",
            "Torrential downpour", "Hurricane", "Poseidon's Wrath", "Zeus's Fury", "Hades' Revenge", "The River Styx cries...",
            "The frenzy has begun!"
    };

    public static void Cycle() { //I lost my byke
        File f = new File(getFolderPath() + "/WeatherForecast.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        ArrayList<String> weather = new ArrayList<>(cfg.getKeys(false));

        int weather_indexes = weather.size();
        int old_value = (int) cfg.get(weather.get(0));
        int new_value;

        cfg.set(weather.get(0), cfg.get(weather.get(12)));

        for (int i = 1; i < weather_indexes; i++)
        {
            new_value = (int) cfg.get(weather.get(i));
            cfg.set(weather.get(i), old_value);
            old_value = new_value;

        }

        try {
            cfg.save(f);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static int getWeather(EntitySpawnEvent event) {
        File f = new File(getFolderPath() + "/WeatherForecast.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        ArrayList<String> weather = new ArrayList<>(cfg.getKeys(false));

        String biomeName = event.getLocation().getBlock().getBiome().name();

        //return the integer that belongs to the biome group this biome belongs to

        for (int i = 0; i < AllBiomesList.size(); i++) {
            if (Arrays.asList(AllBiomesList.get(i)).contains(biomeName)) {
                return ((int) cfg.get(weather.get(i))) + 1;
            }
        }

        //default value that should NEVER be returned, but won't crash the game if it does
        return 0;

    }

    //Make another getWeather function that uses the entities biome and works out the difficulty
    public static int getWeather(Entity entity)
    {
        File f = new File(getFolderPath() + "/WeatherForecast.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        ArrayList<String> weather = new ArrayList<>(cfg.getKeys(false));
        String biomeName = entity.getLocation().getBlock().getBiome().name();

        //return the integer that belongs to the biome group this biome belongs to

        for (int i = 0; i < AllBiomesList.size(); i++) {
            if (Arrays.asList(AllBiomesList.get(i)).contains(biomeName)) {
                return ((int) cfg.get(weather.get(i))) + 1;
            }
        }

        //default value that should NEVER be returned, but won't crash the game if it does
        return 0;

    }

    public static void DisplayForecast() {
        Cycle();
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        File f = new File(getFolderPath() + "/WeatherForecast.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        for (Player player : players)
        {
            player.sendMessage(Component.text("Today's Weather Forecast\n", Style.style(NamedTextColor.WHITE, TextDecoration.BOLD))
                    .append(Component.text("In the plains: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("plains")] + '\n', styles.get( (int) (cfg.get("plains")))))
                    .append(Component.text("In the sands: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("sand")] + '\n', styles.get( (int) (cfg.get("sand")))))
                    .append(Component.text("In the forests: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("forest")] + '\n', styles.get( (int) (cfg.get("forest")))))
                    .append(Component.text("In the tundra: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("tundra")] + '\n', styles.get( (int) (cfg.get("tundra")))))
                    .append(Component.text("In the swamps: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("swamp")] + '\n', styles.get( (int) (cfg.get("swamp")))))
                    .append(Component.text("In the oceans: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("ocean")] + '\n', styles.get( (int) (cfg.get("ocean")))))
                    .append(Component.text("In the jungle: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("jungle")] + '\n', styles.get( (int) (cfg.get("jungle")))))
                    .append(Component.text("In the caves: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("caves")] + '\n', styles.get( (int) (cfg.get("caves")))))
                    .append(Component.text("In the savanna: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("savanna")] + '\n', styles.get( (int) (cfg.get("savanna")))))
                    .append(Component.text("In the mountainous: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("mountainous")] + '\n', styles.get( (int) (cfg.get("mountainous")))))
                    .append(Component.text("In the rocky shores: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("stony")] + '\n', styles.get( (int) (cfg.get("stony")))))
                    .append(Component.text("In the end: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("end")] + '\n', styles.get( (int) (cfg.get("end")))))
                    .append(Component.text("In the nether: "))
                    .append(Component.text(WeatherDesigns[(int) cfg.get("nether")] + '\n', styles.get( (int) (cfg.get("nether")))))
            );
        }
    }

    public static void WeatherReport(Player player) {
        File f = new File(getFolderPath() + "/WeatherForecast.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        player.sendMessage(Component.text(WeatherDesigns[(int) cfg.get(getPlayerBiome(player))], styles.get( (int) (cfg.get(getPlayerBiome(player)))))
                .append(Component.text(" Lv" + (((((int) cfg.get(getPlayerBiome(player))) + 1) * 10) - 9) + "-" + (((int) cfg.get(getPlayerBiome(player))) + 1) * 10, Style.style(NamedTextColor.WHITE))));
    }

    public static int getPlayerWeather(Player player) {
        File f = new File(getFolderPath() + "/WeatherForecast.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        return (int) cfg.get(getPlayerBiome(player));
    }

    public static String getPlayerBiome(Player player) {
        String playerBiome = player.getLocation().getBlock().getBiome().toString();
        String playerBiomeGroup = "";

        if (BiomeGroupContains(PlainsBiomeGroup, playerBiome)) {
            return "plains";
        }
        else if (BiomeGroupContains(SandBiomeGroup, playerBiome)) {
            return "sand";
        }
        else if (BiomeGroupContains(ForestBiomeGroup, playerBiome)) {
            return "forest";
        }
        else if (BiomeGroupContains(TundraBiomeGroup, playerBiome)) {
            return "tundra";
        }
        else if (BiomeGroupContains(SwampBiomeGroup, playerBiome)) {
            return "swamp";
        }
        else if (BiomeGroupContains(OceanBiomeGroup, playerBiome)) {
            return "ocean";
        }
        else if (BiomeGroupContains(JungleBiomeGroup, playerBiome)) {
            return "jungle";
        }
        else if (BiomeGroupContains(CavesBiomeGroup, playerBiome)) {
            return "caves";
        }
        else if (BiomeGroupContains(SavannaBiomeGroup, playerBiome)) {
            return "savanna";
        }
        else if (BiomeGroupContains(MountainousBiomeGroup, playerBiome)) {
            return "mountainous";
        }
        else if (BiomeGroupContains(StonyBiomeGroup, playerBiome)) {
            return "stony";
        }
        else if (BiomeGroupContains(EndBiomeGroup, playerBiome)) {
            return "end";
        }
        else if (BiomeGroupContains(NetherBiomeGroup, playerBiome)) {
            return "nether";
        }

        //what is this?...
        return playerBiomeGroup;
    }

    //Returns the Biome weather modifier for the given biome group a mob is in
    public static double getBiomeModifier(Entity entity) {
        String playerBiome = entity.getLocation().getBlock().getBiome().toString();

        if (BiomeGroupContains(PlainsBiomeGroup, playerBiome)) {
            return 0.2;
        }
        else if (BiomeGroupContains(SandBiomeGroup, playerBiome)) {
            return 1.0;
        }
        else if (BiomeGroupContains(ForestBiomeGroup, playerBiome)) {
            return 0.3;
        }
        else if (BiomeGroupContains(TundraBiomeGroup, playerBiome)) {
            return 0.5;
        }
        else if (BiomeGroupContains(SwampBiomeGroup, playerBiome)) {
            return 0.5;
        }
        else if (BiomeGroupContains(OceanBiomeGroup, playerBiome)) {
            return 1.0;
        }
        else if (BiomeGroupContains(JungleBiomeGroup, playerBiome)) {
            return 0.75;
        }
        else if (BiomeGroupContains(CavesBiomeGroup, playerBiome)) {
            return 1.0;
        }
        else if (BiomeGroupContains(SavannaBiomeGroup, playerBiome)) {
            return 0.25;
        }
        else if (BiomeGroupContains(MountainousBiomeGroup, playerBiome)) {
            return 0.65;
        }
        else if (BiomeGroupContains(StonyBiomeGroup, playerBiome)) {
            return 0.6;
        }
        else if (BiomeGroupContains(EndBiomeGroup, playerBiome)) {
            return 1.25;
        }
        else if (BiomeGroupContains(NetherBiomeGroup, playerBiome)) {
            return 1.1;
        }

        return 1.0;
    }

    public static boolean BiomeGroupContains(String[] biomelist, String player_biome) {
        for (String biome : biomelist) {
            if (biome.equals(player_biome))
            {
                return true;
            }
        }
        return false;
    }

    public static String getFolderPath() {
        return Bukkit.getPluginsFolder().getAbsolutePath() + "/FirstPluginThree/weather";
    }
}
