package me.roupen.firstpluginthree.artifacts;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.data.PlayerStats;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class dreamerFriend {

    String statToRemove;
    public dreamerFriend(String statName) {
        statToRemove = statName;
    }

    public ItemStack toItem() {
        ItemStack item = new ItemStack(Material.SUSPICIOUS_STEW);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "stattoremove"), PersistentDataType.STRING, statToRemove);

        List<Component> lore = new ArrayList<Component>() {{
            add(Component.text("A taste so terrible it's worth forgetting:"));
            add(Component.text("- " + statToRemove));
        }};

        meta.lore(lore);

        item.setItemMeta(meta);

        return item;
    }

    public static void removeSkillPoint(PlayerStats pStats, String skillName) {



    }

}
