package me.roupen.firstpluginthree.playerequipment;

import me.roupen.firstpluginthree.FirstPluginThree;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class Rune {

    Material color;
    Material secondary_color;
    Style style;
    int level;
    String name;
    String savename;
    String prefix;
    String suffix = "";
    ItemStack item;

    public Rune()
    {
        this.color = Material.AIR;
        this.level = 1;
        this.name = "Unnamed rune";
    }

    public Rune(Material type)
    {
        this.color = type;
        this.level = 1;
        this.item = new ItemStack(type);

        if (color == Material.RED_DYE) {
            this.name = "rune of fury";
            this.savename = "red";
            this.prefix = "Furious";
            this.style = Style.style(NamedTextColor.RED);
        }
        else if (color == Material.ORANGE_DYE)
        {
            this.name = "rune of order";
            this.savename = "orange";
            this.prefix = "Orderly";
            this.style = Style.style(NamedTextColor.GOLD);
        }
        else if (color == Material.YELLOW_DYE)
        {
            this.name = "rune of excitement";
            this.savename = "yellow";
            this.prefix = "Excited";
            this.style = Style.style(NamedTextColor.YELLOW);
        }
        else if (color == Material.GREEN_DYE)
        {
            this.name = "rune of joy";
            this.savename = "green";
            this.prefix = "Joyful";
            this.style = Style.style(NamedTextColor.GREEN);
        }
        else if (color == Material.BLUE_DYE)
        {
            this.name = "rune of patience";
            this.savename = "blue";
            this.prefix = "Patient";
            this.style = Style.style(NamedTextColor.BLUE);
        }
        else if (color == Material.PURPLE_DYE)
        {
            this.name = "rune of insight";
            this.savename = "purple";
            this.prefix = "Insightful";
            this.style = Style.style(NamedTextColor.LIGHT_PURPLE);
        }

    }

    public Material getColor() {
        return color;
    }
    public void setColor(Material color) {
        this.color = color;
    }
    public Material getSecondaryColor() {
        return secondary_color;
    }
    public void setSecondaryColor(Material color) {
        this.secondary_color = color;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public ItemStack getItem() {
        return item;
    }
    public void setItem(ItemStack item) {
        this.item = item;
    }
    public void setSecondary_color(Material secondary_color) {
        this.secondary_color = secondary_color;
    }
    public Style getStyle() {
        return style;
    }
    public void setStyle(Style style) {
        this.style = style;
    }
    public String getSavename() {
        return savename;
    }
    public void setSavename(String savename) {
        this.savename = savename;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(Material color) {
        if (color == Material.RED_DYE)
            this.suffix = "of Fury";
        if (color == Material.ORANGE_DYE)
            this.suffix = "of Order";
        if (color == Material.YELLOW_DYE)
            this.suffix = "of Excitement";
        if (color == Material.GREEN_DYE)
            this.suffix = "of Joy";
        if (color == Material.BLUE_DYE)
            this.suffix = "of Patience";
        if (color == Material.PURPLE_DYE)
            this.suffix = "of Insight";
    }

    public ItemStack BuildItem()
    {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        meta.displayName(Component.text(name, style));
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setCustomModelData(123456);
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maincolor"), PersistentDataType.STRING, getColor().toString());

        if (getSecondaryColor() != null)
            data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "secondarycolor"), PersistentDataType.STRING, getSecondaryColor().toString());

        item.setItemMeta(meta);

        return item;
    }

    public static Rune ItemToRune(ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();

        Material secondarycolor = null;
        String secondcolorstring;

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maincolor"), PersistentDataType.STRING))
        {
            Rune newrune = new Rune(item.getType());

            if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "secondarycolor"), PersistentDataType.STRING))
            {
                secondcolorstring = data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "secondarycolor"), PersistentDataType.STRING);
                if (secondcolorstring.equals("RED_DYE"))
                    secondarycolor = Material.RED_DYE;
                if (secondcolorstring.equals("ORANGE_DYE"))
                    secondarycolor = Material.ORANGE_DYE;
                if (secondcolorstring.equals("YELLOW_DYE"))
                    secondarycolor = Material.YELLOW_DYE;
                if (secondcolorstring.equals("GREEN_DYE"))
                    secondarycolor = Material.GREEN_DYE;
                if (secondcolorstring.equals("BLUE_DYE"))
                    secondarycolor = Material.BLUE_DYE;
                if (secondcolorstring.equals("PURPLE_DYE"))
                    secondarycolor = Material.PURPLE_DYE;

                newrune = Rune.CombineRunes(newrune, new Rune(secondarycolor));
            }
            return newrune;
        }
        return null;
    }


    public static Rune CombineRunes(Rune a, Rune b)
    {
        if (a.getLevel() == 1 && b.getLevel() == 1) {
            a.setName(b.getPrefix() + " " + a.getName());
            a.setSecondaryColor(b.getColor());
            a.setSuffix(b.getColor());
            a.setSavename(a.getSavename() + "&" + b.getSavename());
            a.setLevel(2);
            return a;
        }
        return null;
    }

    public static boolean isRune(ItemStack item)
    {
        return item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maincolor"), PersistentDataType.STRING);
    }


}
