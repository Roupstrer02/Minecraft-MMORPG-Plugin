package me.roupen.firstpluginthree.wands;

import me.roupen.firstpluginthree.Zelandris;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class wand {

    private double spellCostModifier;
    private double OffenseSpellPowerModifier;
    private double DefenseSpellPowerModifier;
    private double UtilitySpellPowerModifier;
    private Material itemType;
    private String WandName;
    private int tier;

    public wand(Material mat, String name) {
        this.spellCostModifier = 1.0;
        this.OffenseSpellPowerModifier = 1.0;
        this.DefenseSpellPowerModifier = 1.0;
        this.UtilitySpellPowerModifier = 1.0;
        this.itemType = mat;
        this.WandName = name;
        switch (itemType)
        {
            case STICK:
                this.tier = 1;
                break;
            case AMETHYST_SHARD:
                this.tier = 2;
                break;
            case BLAZE_ROD:
                this.tier = 3;
                break;
            case ECHO_SHARD:
                this.tier = 4;
                break;
        }
    }

    //getters and setters
    public double getSpellCostModifier() {
        return spellCostModifier;
    }
    public void setSpellCostModifier(double spellCostModifier) {
        this.spellCostModifier = spellCostModifier;
    }
    public double getOffenseSpellPowerModifier() {
        return OffenseSpellPowerModifier;
    }
    public void setOffenseSpellPowerModifier(double offenseSpellPowerModifier) {
        OffenseSpellPowerModifier = offenseSpellPowerModifier;
    }
    public double getDefenseSpellPowerModifier() {
        return DefenseSpellPowerModifier;
    }
    public void setDefenseSpellPowerModifier(double defenseSpellPowerModifier) {
        DefenseSpellPowerModifier = defenseSpellPowerModifier;
    }
    public double getUtilitySpellPowerModifier() {
        return UtilitySpellPowerModifier;
    }
    public void setUtilitySpellPowerModifier(double utilitySpellPowerModifier) {
        UtilitySpellPowerModifier = utilitySpellPowerModifier;
    }
    public int getTier() {
        return tier;
    }
    public void setTier(int tier) {
        this.tier = tier;
    }
    public Material getItemType() {
        return itemType;
    }
    public void setItemType(Material itemType) {
        this.itemType = itemType;
    }
    public String getWandName() {
        return WandName;
    }
    public void setWandName(String wandName) {
        WandName = wandName;
    }


    //Transforming the wand object into a storable item in-game and vice versa
    public static ItemStack WandToItem(wand NewWand) {

        ItemStack NewWandItem = new ItemStack(NewWand.getItemType());
        ItemMeta meta = NewWandItem.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        Style style;
        DecimalFormat df = new DecimalFormat("0");

        data.set(new NamespacedKey(Zelandris.getMyPlugin(), "discount"), PersistentDataType.DOUBLE, NewWand.getSpellCostModifier());
        data.set(new NamespacedKey(Zelandris.getMyPlugin(), "offenseaffinity"), PersistentDataType.DOUBLE, NewWand.getOffenseSpellPowerModifier());
        data.set(new NamespacedKey(Zelandris.getMyPlugin(), "defenseaffinity"), PersistentDataType.DOUBLE, NewWand.getDefenseSpellPowerModifier());
        data.set(new NamespacedKey(Zelandris.getMyPlugin(), "utilityaffinity"), PersistentDataType.DOUBLE, NewWand.getUtilitySpellPowerModifier());

        ArrayList<Component> LoreSegments = new ArrayList<>();

        if (NewWand.getSpellCostModifier() != 1.0) {
            LoreSegments.add(Component.text(df.format(100 - (NewWand.getSpellCostModifier() * 100)) + "% Mana Efficiency", Style.style(NamedTextColor.AQUA)));
            LoreSegments.add(Component.text(""));
        }

        if (NewWand.getOffenseSpellPowerModifier() != 1.0)
            LoreSegments.add(Component.text(df.format((NewWand.getOffenseSpellPowerModifier() - 1.0) * 100) + "% Offense Affinity", Style.style(NamedTextColor.RED)));

        if (NewWand.getDefenseSpellPowerModifier() != 1.0)
            LoreSegments.add(Component.text(df.format((NewWand.getDefenseSpellPowerModifier() - 1.0) * 100) + "% Defense Affinity", Style.style(NamedTextColor.GREEN)));

        if (NewWand.getUtilitySpellPowerModifier() != 1.0)
            LoreSegments.add(Component.text(df.format((NewWand.getUtilitySpellPowerModifier() - 1.0) * 100) + "% Utility Affinity", Style.style(NamedTextColor.LIGHT_PURPLE)));

        switch (NewWand.getTier())
        {
            case 1:
                style = Style.style(NamedTextColor.WHITE);
                meta.setCustomModelData(1);
                break;
            case 2:
                style = Style.style(NamedTextColor.LIGHT_PURPLE);
                meta.setCustomModelData(2);
                break;
            case 3:
                style = Style.style(NamedTextColor.GOLD);
                meta.setCustomModelData(3);
                break;
            case 4:
                style = Style.style(NamedTextColor.BLUE);
                meta.setCustomModelData(4);
                break;
            default:
                style = Style.style(NamedTextColor.DARK_GRAY);
                meta.setCustomModelData(0);
        }

        List<Component> lore = LoreSegments;

        meta.displayName(Component.text(NewWand.getWandName(), style));
        meta.lore(lore);
        NewWandItem.setItemMeta(meta);

        return NewWandItem;
    }

    public static wand ItemToWand(ItemStack item) {
        wand Wand = new wand(item.getType(), item.getItemMeta().displayName().toString());
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

        if (data.has(new NamespacedKey(Zelandris.getMyPlugin(), "discount"), PersistentDataType.DOUBLE))
            Wand.setSpellCostModifier(data.get(new NamespacedKey(Zelandris.getMyPlugin(), "discount"), PersistentDataType.DOUBLE));
        if (data.has(new NamespacedKey(Zelandris.getMyPlugin(), "offenseaffinity"), PersistentDataType.DOUBLE))
            Wand.setOffenseSpellPowerModifier(data.get(new NamespacedKey(Zelandris.getMyPlugin(), "offenseaffinity"), PersistentDataType.DOUBLE));
        if (data.has(new NamespacedKey(Zelandris.getMyPlugin(), "defenseaffinity"), PersistentDataType.DOUBLE))
            Wand.setDefenseSpellPowerModifier(data.get(new NamespacedKey(Zelandris.getMyPlugin(), "defenseaffinity"), PersistentDataType.DOUBLE));
        if (data.has(new NamespacedKey(Zelandris.getMyPlugin(), "utilityaffinity"), PersistentDataType.DOUBLE))
            Wand.setUtilitySpellPowerModifier(data.get(new NamespacedKey(Zelandris.getMyPlugin(), "utilityaffinity"), PersistentDataType.DOUBLE));

        return Wand;
    }

    public static void Interact(PlayerInteractEvent event) {

        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getPlayer().getInventory().getItemInOffHand().equals(new ItemStack(Material.DIAMOND)))
        {
            event.getPlayer().getInventory().addItem(wand.WandToItem(new wand(Material.BLAZE_ROD, "Pyromancer's Friend")));
        }
    }

    public static boolean IsWand(ItemStack item)
    {
        if (item == null || item.getType() == Material.AIR) {return false;}
        else
        {
            PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

            return data.has(new NamespacedKey(Zelandris.getMyPlugin(), "discount"), PersistentDataType.DOUBLE);
        }

    }

}
