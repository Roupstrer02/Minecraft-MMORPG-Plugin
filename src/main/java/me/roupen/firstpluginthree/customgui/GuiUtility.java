package me.roupen.firstpluginthree.customgui;

import me.roupen.firstpluginthree.data.PlayerStats;
import me.roupen.firstpluginthree.magic.*;
import me.roupen.firstpluginthree.utility.PlayerUtility;
import me.roupen.firstpluginthree.weather.WeatherForecast;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class GuiUtility {

    private final Inventory inv;
    private static DecimalFormat df = new DecimalFormat("0.00");
    private static Material[] weatherIcons = new Material[]
            {Material.SUNFLOWER, Material.OAK_SAPLING, Material.OAK_LEAVES, Material.WHITE_CONCRETE_POWDER,
             Material.WATER_BUCKET, Material.LIGHTNING_ROD, Material.PRISMARINE, Material.COBWEB,
             Material.TRIDENT, Material.REDSTONE, Material.MAGMA_BLOCK, Material.LAVA_BUCKET, Material.CRIMSON_HYPHAE};
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

    public void addGuiItem(final Material material, final String name, final int slot, int model, boolean enchanted, final String... lore)
    {
        ItemStack item = createGuiItem(material, name, model, lore);
        if (enchanted) {
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        inv.setItem(slot, item);
    }

    public void addGuiItem(final Material material, final String name, final int slot, int model, boolean enchanted, final Component... lore)
    {
        ItemStack item = createGuiItem(material, name, model, lore);
        if (enchanted) {
            ItemMeta meta = item.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        inv.setItem(slot, item);
    }

    public static void CreateProfileGui(Player player)
    {
        PlayerStats stats = PlayerUtility.getPlayerStats(player);
        GuiUtility gui = new GuiUtility(player, 45, "Player Stats");

        gui.addGuiItem(Material.GRAY_STAINED_GLASS_PANE, "Level " + stats.getLevel(), 3, 0, false, df.format((((double) stats.getExperience()) / stats.getLevelCap()) * 100) + "%");
        gui.addGuiItem(Material.DIAMOND, "Level Up: ", 8, 0,false, "Unused Skill Points: " + stats.getSkillPoints());
        gui.addGuiItem(Material.LIME_STAINED_GLASS_PANE, "Resilience ", 10, 0,false, Component.text("Lv ", Style.style(NamedTextColor.WHITE)).append(Component.text(stats.getResilience(), Style.style(NamedTextColor.GREEN))));
        gui.addGuiItem(Material.BLUE_STAINED_GLASS_PANE, "Intelligence ", 14, 0,false, Component.text("Lv ", Style.style(NamedTextColor.WHITE)).append(Component.text(stats.getIntelligence(), NamedTextColor.BLUE)));

        gui.addGuiItem(weatherIcons[WeatherForecast.getPlayerWeather(player)], WeatherForecast.WeatherDesigns[WeatherForecast.getPlayerWeather(player)], 17, 0,false,"");

        gui.addGuiItem(Material.RED_STAINED_GLASS_PANE, "Vitality ", 18, 0,false, Component.text("Lv ", Style.style(NamedTextColor.WHITE)).append(Component.text(stats.getVitality(), NamedTextColor.RED)));
        gui.addGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "Strength ", 20, 0,false, Component.text("Lv ", Style.style(NamedTextColor.WHITE)).append(Component.text(stats.getStrength(), NamedTextColor.GOLD)));
        gui.addGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "Dexterity ", 22, 0,false, Component.text("Lv ", Style.style(NamedTextColor.WHITE)).append(Component.text(stats.getDexterity(), NamedTextColor.YELLOW)));
        gui.addGuiItem(Material.CYAN_STAINED_GLASS_PANE, "Wisdom ", 24, 0,false, Component.text("Lv ", Style.style(NamedTextColor.WHITE)).append(Component.text(stats.getWisdom(), NamedTextColor.AQUA)));
        gui.addGuiItem(Material.AMETHYST_BLOCK, "Spell Book", 35, 0,false, "Pick your spell Arsenal");
        gui.addGuiItem(Material.MAGENTA_STAINED_GLASS_PANE, "Artisan ", 39, 0,false, Component.text("Lv ", Style.style(NamedTextColor.WHITE)).append(Component.text(stats.getArtisan(), NamedTextColor.LIGHT_PURPLE)));
        gui.addGuiItem(Material.WRITTEN_BOOK, "Tutorial", 44, 0,false, "");

        for (int slot : new int[]{7, 16, 25, 26, 34, 43}) {
            gui.addGuiItem(Material.GLASS_PANE, "", slot, 0,false, "");
        }

        gui.openInventory(player);
    }

    public static void CreateUpgradeGui(Player player)
    {
        GuiUtility upgradegui = new GuiUtility(player,45, "Stat Improvements");
        PlayerStats stats = PlayerUtility.getPlayerStats(player);

        upgradegui.addGuiItem(Material.GRAY_DYE, "Back to profile menu", 8, 0,false, "");
        upgradegui.addGuiItem(Material.RED_STAINED_GLASS_PANE, "Level up Vitality", 10, 0,false, (stats.getVitality()) + " (+" + 1 + ")");
        upgradegui.addGuiItem(Material.LIME_STAINED_GLASS_PANE, "Level up Resilience", 13, 0,false, (stats.getResilience()) + " (+" + 1 + ")");
        upgradegui.addGuiItem(Material.ORANGE_STAINED_GLASS_PANE, "Level up Strength", 16, 0,false, (stats.getStrength()) + " (+" + 1 + ")");
        upgradegui.addGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "Level up Dexterity", 28, 0,false, (stats.getDexterity()) + " (+" + 1 + ")");
        upgradegui.addGuiItem(Material.BLUE_STAINED_GLASS_PANE, "Level up Intelligence", 31, 0,false, (stats.getIntelligence()) + " (+" + 1 + ")");
        upgradegui.addGuiItem(Material.CYAN_STAINED_GLASS_PANE, "Level up Wisdom", 34, 0,false, (stats.getWisdom()) + " (+" + 1 + ")");
        upgradegui.addGuiItem(Material.MAGENTA_STAINED_GLASS_PANE, "Level up Artisan", 36, 0,false, (stats.getArtisan()) + " (+" + 1 + ")");
        upgradegui.addGuiItem(Material.DIAMOND, "Unused Skill points: ", 0, 0,false, (stats.getSkillPoints()) + "");

        upgradegui.openInventory(player);
    }

    public static void CreateSpellBookGui(Player player)
    {
        GuiUtility spellbookgui = new GuiUtility(player,54, "Spell Book");
        PlayerStats stats = PlayerUtility.getPlayerStats(player);
        ArrayList<String> selected_spells = new ArrayList<String>() {{
            this.addAll(Arrays.asList(stats.getSpellbook()));
        }};

        int[] glass_pane_slots = new int[]{0,2,4,6,8,11,13,15,17,20,22,24,26,29,31,33,35,38,40,42,44,45,46,47,48,51,52,53};

        for (int slot : glass_pane_slots) {
            spellbookgui.addGuiItem(Material.GLASS_PANE, "", slot, 0,false, "");
        }
        spellbookgui.addGuiItem(Material.GRAY_DYE, "Back to profile menu", 49, 0,false, "");
        spellbookgui.addGuiItem(Material.REDSTONE_TORCH, "Next Page", 50, 0,false, "Coming soon...");

        spellbookgui.addGuiItem(Material.MAGMA_BLOCK, "Pyromancy", 1, 0,false, "All-out offense fit for those", " ", "in the deepest pits of the underworld");
        spellbookgui.addGuiItem(Material.PISTON, "Technomancy", 3, 0,false, "Clockwork efficiency", " ", "made to see past the feeble defenses of your enemies");
        spellbookgui.addGuiItem(Material.ENCHANTED_GOLDEN_APPLE, "Divinity", 5, 0,false, "Blessed-be the arbiter of the Creator", " ", "For the benevolent and selfless" ,"who put others ahead of themselves");
        spellbookgui.addGuiItem(Material.OAK_SAPLING, "Druidomancy", 7, 0,false, "Nature's touch", " ", "magic fit for the day to day needs of a spell slinger");

        spellbookgui.addGuiItem(Material.BLAZE_ROD, "Standard Spells", 9, 0,false, "Right-Click");
        spellbookgui.addGuiItem(Material.IRON_BOOTS, "Dash Spells", 18, 0,false, "Sprint + Right-Click");
        spellbookgui.addGuiItem(Material.FEATHER, "Aerial Spells", 27, 0,false, "Midair + Right-Click");
        spellbookgui.addGuiItem(Material.END_CRYSTAL, "Ultimate Spells", 36, 0,false, "Shift + Right-Click");

        spellbookgui.addGuiItem(Material.FIRE_CHARGE, "Fireball", 10, 0,selected_spells.contains("Fireball"), PyroFireball.baseManaCost + " mana"," ", "Shoots a fireball forward"," ", "explodes on terrain or first entity hit");
        spellbookgui.addGuiItem(Material.FIRE_CHARGE, "Fiery Step", 19, 0,selected_spells.contains("Flame Dash"), PyroFlameDash.baseManaCost + " mana", " ", "Summons a ring of fire around the caster", " ", "Marks enemies in range, ", "making them explode after a few seconds");
        spellbookgui.addGuiItem(Material.FIRE_CHARGE, "Explosive leap", 28, 0,selected_spells.contains("Flame Booster"), PyroFlameBooster.baseManaCost + " mana", " ", "Create an explosion launching the caster up", " ", "damages enemies caught in the explosion");
        spellbookgui.addGuiItem(Material.FIRE_CHARGE, "Meteor Fall", 37, 0,selected_spells.contains("Meteor Fall"), PyroMeteorFall.baseManaCost + " mana", " ", "The world burn in hellfire!", " ", "May you find solace in the flames","for it is all you will see");

        spellbookgui.addGuiItem(Material.REDSTONE, "Snipe", 12, 0,selected_spells.contains("Snipe"), TechSnipe.baseManaCost + " mana", " ", "Shoots an instantly travelling bullet"," ", "Ignores armor of enemy hit");
        spellbookgui.addGuiItem(Material.REDSTONE, "Fault In The Armor", 21, 0,selected_spells.contains("Fault In The Armor"), TechFaultInArmor.baseManaCost + " mana", " ", "Launches the caster forward", " ", "Damages first enemy hit and knocks them back");
        spellbookgui.addGuiItem(Material.REDSTONE, "Aerial Supremacy", 30, 0,selected_spells.contains("Steam Rocket Pack"), TechSteamRocketPack.baseManaCost + " mana", " ", "Caster flies in the direction they're looking");
        spellbookgui.addGuiItem(Material.REDSTONE, "Chronomancer's prison", 39, 0,selected_spells.contains("Chrono Thief"), TechChronothief.baseManaCost + " mana", " ", "Throws device that creates a time-stopping field upon impact", " ", "Enemies caught in the field have their armor reduced", "and movement frozen");

        spellbookgui.addGuiItem(Material.GLOWSTONE_DUST, "Healing Orb", 14, 0, selected_spells.contains("Healing Orb"),DivineHealingOrb.baseManaCost + " mana", " ", "Shoots a slow moving orb"," ", "Heals all players in it's vicinity");
        spellbookgui.addGuiItem(Material.GLOWSTONE_DUST, "Strength Of Faith", 23, 0,selected_spells.contains("Strength Of Faith"), DivineStrengthOfFaith.baseManaCost + " mana", " ", "Creates an area around the caster"," ", "Strengthens the attacks of allies in range");
        spellbookgui.addGuiItem(Material.GLOWSTONE_DUST, "Angelic Wings", 32, 0,selected_spells.contains("Angel Wings"), DivineAngelWings.baseManaCost + " mana", " ", "Teleports caster just in time to save an injured ally"," ", "Target receives minor healing and a temporary defense increase");
        spellbookgui.addGuiItem(Material.GLOWSTONE_DUST, "We Yield To None", 41, 0,selected_spells.contains("We Yield To None"),DivineWeYieldToNone.baseManaCost + " mana", " ", "I see no Deities here..."," ", "Other than ME!!!");

        spellbookgui.addGuiItem(Material.OAK_WOOD, "Nature's Bounty", 16, 0,selected_spells.contains("Nature's Bounty"), NatureNaturesBounty.baseManaCost + " mana", " ", "Breaks the first block within range in front of the player"," ", "Grants EXP when breaking ores. Chance for bonus drops");
        spellbookgui.addGuiItem(Material.OAK_WOOD, "Nature's Storage", 25, 0, selected_spells.contains("Nature's Storage"),NatureNaturesStorage.baseManaCost + " mana", " ", "Opens your personal ender chest");
        spellbookgui.addGuiItem(Material.OAK_WOOD, "Nature's Workbench", 34, 0,selected_spells.contains("Nature's Workbench"), NatureNaturesWorkbench.baseManaCost + " mana", " ", "Opens a convenient crafting table wherever you may be");
        spellbookgui.addGuiItem(Material.OAK_WOOD, "Nature's Diversity", 43, 0,selected_spells.contains("Nature's Diversity"),NatureNaturesDiversity.baseManaCost + " mana", " ", "Cycles through the block subtypes of the targeted block"," ", "affects saplings, logs, woods and planks");

        spellbookgui.addGuiItem(Material.GRAY_DYE, "Back to profile menu", 49, 0,false, "");

        spellbookgui.openInventory(player);
    }

    public static void CreateRuneGui(Player player)
    {
        GuiUtility runegui = new GuiUtility(player,45, "Rune Forge");

        for (int i = 0; i < 45; i++)
        {
            if (!( i == 0 || i == 12 || i == 23 || i == 30))
            {runegui.addGuiItem(Material.GRAY_STAINED_GLASS_PANE, " ", i,  0,false, "");}
        }
        runegui.addGuiItem(Material.EMERALD, "This is the Rune Forge!", 0,  0,false, Component.text("Place a piece of equipment in the top slot to engrave a rune on it,", Style.style(NamedTextColor.RED)),
                Component.text("or place a rune in the top slot to combine it", Style.style(NamedTextColor.GOLD)),
                Component.text("with a rune in the bottom slot", Style.style(NamedTextColor.YELLOW)));
        runegui.openInventory(player);
    }

    public static void CreateEquipmentWorkbenchGui(Player player)
    {
        GuiUtility equipmentworkbenchgui = new GuiUtility(player,45, "Equipment Workbench");
        PlayerStats stats = PlayerUtility.getPlayerStats(player);

        equipmentworkbenchgui.addGuiItem(Material.HONEYCOMB, "This is the equipment workbench", 0, 0, false,"To craft any tool, just make sure you have", "the needed materials, and just click on the item.");

        //dagger, longsword, greatsword, shortbow, longbow, crossbow
        equipmentworkbenchgui.addGuiItem(Material.WOODEN_SWORD, "Dagger", 11, 20020, false,"Stamina cost: 3.0", "", "Scaling per level:", "0.5x Damage", "2x Multi-Hit", "1.25x Crit-Chance", "", "Materials:", "Iron Ingot x4", "Stick x2");
        equipmentworkbenchgui.addGuiItem(Material.WOODEN_SWORD, "Longsword", 13, 20021, false,"Stamina cost: 5.0", "", "Scaling per level:", "1.1x Damage", "", "Materials:", "Iron Ingot x8", "Stick x4");
        equipmentworkbenchgui.addGuiItem(Material.WOODEN_SWORD, "Greatsword", 15, 20022, false,"Stamina cost: 14.0", "", "Scaling per level:", "2.5x Damage", "0.25x Multi-Hit", "1.5x Crit-Damage", "", "Materials:", "Iron Ingot x16", "Stick x8");

        equipmentworkbenchgui.addGuiItem(Material.BOW, "shortbow", 29, 20023, false,"Stamina cost: 6.0", "", "Scaling per level:", "0.8x Damage", "1.25x Movement speed", "1.25x Crit-Chance", "0.5x Multi-Hit", "", "Materials:", "String x5", "Stick x12");
        equipmentworkbenchgui.addGuiItem(Material.BOW, "longbow", 31, 20024, false,"Stamina cost: 11.0", "", "Scaling per level:", "1.5x Damage", "0.5x Multi-Hit", "1.5x Crit-Damage", "", "Materials:", "String x8", "Stick x20");
        equipmentworkbenchgui.addGuiItem(Material.CROSSBOW, "crossbow", 33, 20025, false,"Stamina cost: 7.0", "", "Scaling per level:", "0.5x Multi-Hit", "", "Materials:", "String x5", "Stick x8", "Iron Ingot x2");

        equipmentworkbenchgui.openInventory(player);

    }

}
