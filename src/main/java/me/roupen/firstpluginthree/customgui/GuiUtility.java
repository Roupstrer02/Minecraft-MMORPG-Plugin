package me.roupen.firstpluginthree.customgui;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import me.roupen.firstpluginthree.playerequipment.Rune;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.util.Arrays;

public class GuiUtility {

    private final Inventory inv;
    public GuiUtility(Player player, int size, String name)
    {
        inv = Bukkit.createInventory(null, size, name);
    }

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    public static ItemStack createGuiItem(final Material material, final String name, int model, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));
        
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack createGuiItem(final Material material, final String name, int model, final Component... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.lore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public void addGuiItem(final Material material, final String name, final int slot, int model, final String... lore)
    {
        ItemStack item = createGuiItem(material, name, model, lore);
        inv.setItem(slot, item);
    }

    public void addGuiItem(final Material material, final String name, final int slot, int model, final Component... lore)
    {
        ItemStack item = createGuiItem(material, name, model, lore);
        inv.setItem(slot, item);
    }

    public static void CreateProfileGui(Player player)
    {
        PlayerStats stats = PlayerUtility.getPlayerStats(player);
        GuiUtility gui = new GuiUtility(player, 45, "Player Stats");

        gui.addGuiItem(Material.RED_STAINED_GLASS_PANE, "Vitality: ", 4, 0, Integer.toString(stats.getVitality()));
        gui.addGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Resilience: ", 11, 0, Integer.toString(stats.getResilience()));
        gui.addGuiItem(Material.BLUE_STAINED_GLASS_PANE, "Intelligence: ", 15, 0, Integer.toString(stats.getIntelligence()));
        gui.addGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "Strength: ", 29, 0, Integer.toString(stats.getStrength()));
        gui.addGuiItem(Material.CYAN_STAINED_GLASS_PANE, "Wisdom: ", 33, 0, Integer.toString(stats.getWisdom()));
        gui.addGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "Dexterity: ", 40, 0, Integer.toString(stats.getDexterity()));
        gui.addGuiItem(Material.LIME_STAINED_GLASS_PANE, "Artisan: ", 36, 0, Integer.toString(stats.getArtisan()));
        gui.addGuiItem(Material.GRAY_STAINED_GLASS_PANE, "Level: ", 22, 0, Integer.toString(stats.getLevel()));
        gui.addGuiItem(Material.DIAMOND, "Unused Skill points: ", 0, 0, Integer.toString(stats.getSkillPoints()));

        gui.openInventory(player);
    }

    public static void CreateUpgradeGui(Player player)
    {
        GuiUtility upgradegui = new GuiUtility(player,45, "Stat Improvements");
        PlayerStats stats = PlayerUtility.getPlayerStats(player);

        upgradegui.addGuiItem(Material.GRAY_DYE, "Back to profile menu", 8, 0, "");
        upgradegui.addGuiItem(Material.RED_STAINED_GLASS_PANE, "Vitality: ", 10, 0, Integer.toString(stats.getVitality()));
        upgradegui.addGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Resilience: ", 13, 0, Integer.toString(stats.getResilience()));
        upgradegui.addGuiItem(Material.BLUE_STAINED_GLASS_PANE, "Intelligence: ", 16, 0, Integer.toString(stats.getIntelligence()));
        upgradegui.addGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "Strength: ", 28, 0, Integer.toString(stats.getStrength()));
        upgradegui.addGuiItem(Material.CYAN_STAINED_GLASS_PANE, "Wisdom: ", 31, 0, Integer.toString(stats.getWisdom()));
        upgradegui.addGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "Dexterity: ", 34, 0, Integer.toString(stats.getDexterity()));
        upgradegui.addGuiItem(Material.LIME_STAINED_GLASS_PANE, "Artisan: ", 36, 0, Integer.toString(stats.getArtisan()));
        upgradegui.addGuiItem(Material.DIAMOND, "Unused Skill points: ", 0, 0, Integer.toString(stats.getSkillPoints()));

        upgradegui.openInventory(player);
    }

    public static void CreateRuneGui(Player player)
    {
        GuiUtility runegui = new GuiUtility(player,45, "Rune Forge");

        for (int i = 0; i < 45; i++)
        {
            if (!( i == 0 || i == 12 || i == 23 || i == 30))
            {runegui.addGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", i,  0, "");}
        }
        runegui.addGuiItem(Material.EMERALD, "This is the Rune Forge!", 0,  0, Component.text("Place a piece of equipment in the top slot to engrave a rune on it,", Style.style(NamedTextColor.RED)),
                Component.text("or place a rune in the top slot to combine it", Style.style(NamedTextColor.GOLD)),
                Component.text("with a rune in the bottom slot", Style.style(NamedTextColor.YELLOW)));
        runegui.openInventory(player);
    }

    public static void CreateEquipmentWorkbenchGui(Player player)
    {
        GuiUtility equipmentworkbenchgui = new GuiUtility(player,45, "Equipment Workbench");
        PlayerStats stats = PlayerUtility.getPlayerStats(player);

        equipmentworkbenchgui.addGuiItem(Material.HONEYCOMB, "This is the equipment workbench", 0, 0, "To craft any tool, just make sure you have", "the needed materials, and just click on the item.");

        //dagger, longsword, greatsword, shortbow, longbow, crossbow
        equipmentworkbenchgui.addGuiItem(Material.WOODEN_SWORD, "Dagger", 11, 20020, "Stamina cost: 3.0", "", "Scaling per level:", "0.5x Damage", "2x Multi-Hit", "1.25x Crit-Chance", "", "Materials:", "Iron Ingot x4", "Stick x2");
        equipmentworkbenchgui.addGuiItem(Material.WOODEN_SWORD, "Longsword", 13, 20021, "Stamina cost: 5.0", "", "Scaling per level:", "1.1x Damage", "", "Materials:", "Iron Ingot x8", "Stick x4");
        equipmentworkbenchgui.addGuiItem(Material.WOODEN_SWORD, "Greatsword", 15, 20022, "Stamina cost: 14.0", "", "Scaling per level:", "2.5x Damage", "0.25x Multi-Hit", "1.5x Crit-Damage", "", "Materials:", "Iron Ingot x16", "Stick x8");

        equipmentworkbenchgui.addGuiItem(Material.BOW, "shortbow", 29, 20023, "Stamina cost: 6.0", "", "Scaling per level:", "0.8x Damage", "1.25x Movement speed", "1.25x Crit-Chance", "0.5x Multi-Hit", "", "Materials:", "String x5", "Stick x12");
        equipmentworkbenchgui.addGuiItem(Material.BOW, "longbow", 31, 20024, "Stamina cost: 11.0", "", "Scaling per level:", "1.5x Damage", "0.5x Multi-Hit", "1.5x Crit-Damage", "", "Materials:", "String x8", "Stick x20");
        equipmentworkbenchgui.addGuiItem(Material.CROSSBOW, "crossbow", 33, 20025, "Stamina cost: 7.0", "", "Scaling per level:", "0.5x Multi-Hit", "", "Materials:", "String x5", "Stick x8", "Iron Ingot x2");

        equipmentworkbenchgui.openInventory(player);

    }

}
