package me.roupen.firstpluginthree.customgui;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GuiUtility {

    //WIP

    private final Inventory inv;
    public GuiUtility(Player player, int size, String name)
    {
        inv = Bukkit.createInventory(null, size, name);

    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void addGuiItem(final Material material, final String name, final int slot, final String... lore)
    {
        ItemStack item = createGuiItem(material, name, lore);
        inv.setItem(slot, item);
    }

    public static void CreateUpgradeGui(Player player)
    {
        GuiUtility upgradegui = new GuiUtility(player,45, "Stat Improvements");
        PlayerStats stats = PlayerUtility.getPlayerStats(player);

        upgradegui.addGuiItem(Material.RED_STAINED_GLASS_PANE, "Vitality: ", 10, Integer.toString(stats.getVitality()));
        upgradegui.addGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Resilience: ", 13, Integer.toString(stats.getResilience()));
        upgradegui.addGuiItem(Material.BLUE_STAINED_GLASS_PANE, "Intelligence: ", 16, Integer.toString(stats.getIntelligence()));
        upgradegui.addGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "Strength: ", 28, Integer.toString(stats.getStrength()));
        upgradegui.addGuiItem(Material.CYAN_STAINED_GLASS_PANE, "Wisdom: ", 31, Integer.toString(stats.getWisdom()));
        upgradegui.addGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "Dexterity: ", 34, Integer.toString(stats.getDexterity()));
        upgradegui.addGuiItem(Material.DIAMOND, "Unused Skill points: ", 0, Integer.toString(stats.getSkillPoints()));

        upgradegui.openInventory(player);
    }

}
