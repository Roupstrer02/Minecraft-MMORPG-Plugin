package me.roupen.firstpluginthree.utility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsumableUtility {
    public static String getFolderPath(){
        return Bukkit.getPluginsFolder().getAbsolutePath() + "/FirstPluginThree/Artisan/ItemEffects.yml";
    }
}
