package me.roupen.firstpluginthree.artisan;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.ConsumableUtility;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Consumable extends BukkitRunnable {

    int progress = 0;
    int duration;
    PlayerStats pStats;
    //potion effect storage
    private Map<String, PotionEffectType> PotionNameToEffect = new HashMap<String, PotionEffectType>() {{
       put("Night Vision", PotionEffectType.NIGHT_VISION);
       put("Invisibility", PotionEffectType.INVISIBILITY);
       put("Jump Boost", PotionEffectType.JUMP);
       put("Fire Resistance", PotionEffectType.FIRE_RESISTANCE);
       put("Speed", PotionEffectType.SPEED);
       put("Water Breathing", PotionEffectType.WATER_BREATHING);
       put("Poison", PotionEffectType.POISON);
       put("Slow Falling", PotionEffectType.SLOW_FALLING);
       put("Slowness", PotionEffectType.SLOW);
       put("Haste", PotionEffectType.FAST_DIGGING);
       put("Mining Fatigue", PotionEffectType.SLOW_DIGGING);
    }};
    private Map<String, Integer[]> ConsumablePotionEffects = new HashMap<String, Integer[]>() {{
        put("Night Vision", new Integer[2]);
        put("Invisibility", new Integer[2]);
        put("Jump Boost", new Integer[2]);
        put("Fire Resistance", new Integer[2]);
        put("Speed", new Integer[2]);
        put("Water Breathing", new Integer[2]);
        put("Poison", new Integer[2]);
        put("Slow Falling", new Integer[2]);
        put("Slowness", new Integer[2]);
        put("Haste", new Integer[2]);
        put("Mining Fatigue", new Integer[2]);
    }};

    //stat change storage maps

    private ArrayList<String> statNames = new ArrayList<String>() {{
        add("Max HP");
        add("HP Regen");
        add("Defense");
        add("Damage");
        add("Max Mana");
        add("Mana Regen");
        add("Stamina Cap");
        add("Stamina Regen");
        add("Multi Hit");
        add("Crit Chance");
        add("Crit Damage Mult");
        add("Movement Speed");
    }};
    private Map<String, Double[]> LinearStatChanges = new HashMap<String, Double[]>() {{
        put("Max HP", new Double[2]);
        put("HP Regen", new Double[2]);
        put("Defense", new Double[2]);
        put("Damage", new Double[2]);
        put("Max Mana", new Double[2]);
        put("Mana Regen", new Double[2]);
        put("Stamina Cap", new Double[2]);
        put("Stamina Regen", new Double[2]);
        put("Multi Hit", new Double[2]);
        put("Crit Chance", new Double[2]);
        put("Crit Damage Mult", new Double[2]);
        put("Movement Speed", new Double[2]);
    }};

    private Map<String, Double[]> MultiplicativeStatChanges = new HashMap<String, Double[]>() {{
        put("Max HP", new Double[2]);
        put("HP Regen", new Double[2]);
        put("Defense", new Double[2]);
        put("Damage", new Double[2]);
        put("Max Mana", new Double[2]);
        put("Mana Regen", new Double[2]);
        put("Stamina Cap", new Double[2]);
        put("Stamina Regen", new Double[2]);
        put("Multi Hit", new Double[2]);
        put("Crit Chance", new Double[2]);
        put("Crit Damage Mult", new Double[2]);
        put("Movement Speed", new Double[2]);
    }};
    public Consumable(ItemStack consumed_item) {
        //Go through YML file and set the consumable's params
        File f = new File(ConsumableUtility.getFolderPath());
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);

        String key = consumed_item.getItemMeta().getDisplayName();

        if (cfg.getKeys(false).contains(key)) {

            if (cfg.contains(key + ".Potion")) {

                //add all potion effects to Map
                ConfigurationSection potionEffects = (ConfigurationSection) cfg.get(key + ".Potion");

                for (String effectName : potionEffects.getKeys(false)) {
                    if (ConsumablePotionEffects.containsKey(effectName)) {
                        Integer[] effects = potionEffects.getIntegerList(effectName).toArray(new Integer[2]);
                        ConsumablePotionEffects.put(effectName, effects);
                    }
                }

            }
            if (cfg.contains(key + ".Linear")) {
                ConfigurationSection statChanges = (ConfigurationSection) cfg.get(key + ".Linear");

                for (String statName : statNames) {
                    if (LinearStatChanges.containsKey(statName)) {
                        Double[] effects = statChanges.getDoubleList(statName).toArray(new Double[2]);
                        LinearStatChanges.put(statName, effects);
                    }
                }
            }
            if (cfg.contains(key + ".Multiplicative")) {
                ConfigurationSection statChanges = (ConfigurationSection) cfg.get(key + ".Multiplicative");

                for (String statName : statNames) {
                    if (MultiplicativeStatChanges.containsKey(statName)) {
                        Double[] effects = statChanges.getDoubleList(statName).toArray(new Double[2]);
                        MultiplicativeStatChanges.put(statName, effects);
                    }
                }
            }
        }
    }


    public void ConsumeItem(Player p) {
        pStats = PlayerUtility.getPlayerStats(p);
        for (String potionEffectName : ConsumablePotionEffects.keySet()) {
            Integer[] effectParams = ConsumablePotionEffects.get(potionEffectName);
            if (effectParams[0] != null && effectParams[1] != null) {
                p.addPotionEffect(new PotionEffect(PotionNameToEffect.get(potionEffectName), effectParams[0], effectParams[1]));
            }
        }

        for (String statName : statNames) {
            Double[] linstatChangeParams = LinearStatChanges.get(statName);
            Double[] multstatChangeParams = MultiplicativeStatChanges.get(statName);
            if (linstatChangeParams[0] != null && linstatChangeParams[1] != null) {
                pStats.changeLinearStats(statName, linstatChangeParams[1]);
                duration = Math.max(duration,  (int) Math.round(linstatChangeParams[0]));
            }
            if (multstatChangeParams[0] != null && multstatChangeParams[1] != null) {
                pStats.changeMultiplicativeStats(statName, multstatChangeParams[1]);
                duration = Math.max(duration,  (int) Math.round(multstatChangeParams[0]));
            }
        }
    }

    @Override
    public void run() {

        for (String statName : statNames) {
            if (LinearStatChanges.get(statName)[0] != null && LinearStatChanges.get(statName)[1] != null && progress == LinearStatChanges.get(statName)[0]) {
                pStats.changeLinearStats(statName, -LinearStatChanges.get(statName)[1]);
            }
            if (MultiplicativeStatChanges.get(statName)[0] != null && MultiplicativeStatChanges.get(statName)[1] != null && progress == MultiplicativeStatChanges.get(statName)[0]) {
                pStats.changeMultiplicativeStats(statName, 1 / MultiplicativeStatChanges.get(statName)[1]);
            }
        }

        if (progress >= duration) {
            pStats.setConsumingItem(false);
            this.cancel();
        }

        progress += 1;
    }
}
