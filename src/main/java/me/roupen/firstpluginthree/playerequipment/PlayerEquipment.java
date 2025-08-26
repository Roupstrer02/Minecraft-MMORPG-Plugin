package me.roupen.firstpluginthree.playerequipment;

import me.roupen.firstpluginthree.FirstPluginThree;
import me.roupen.firstpluginthree.data.MobStats;
import me.roupen.firstpluginthree.balance.Balance;
import me.roupen.firstpluginthree.utility.MobUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.text.DecimalFormat;
import java.util.*;

public class PlayerEquipment {

    private double MaxHealth = 0.0;
    private double HealthRegen = 0.0;
    private double Defense = 0.0;
    private double Damage = 0.0;
    private double MaxMana = 0.0;
    private double ManaRegen = 0.0;
    private double MaxStamina = 0.0;
    private double StaminaRegen = 0.0;
    private double MovementSpeed = 0.0;
    private double MultiHit = 0.0;
    private double CritChance = 0.0;
    private double CritDamageMult = 0.0;
    private Material ItemType = Material.DIAMOND;
    private String Name = "Basic Item";
    private int Rarity;
    private double StaminaCost = 0.0;
    private int Level = 1;
    private boolean isMagic = false;
    private String toolType;
    private Rune[] runes;
    public static final String[] ArmorNames = {"Helmet", "Chestplate", "Leggings", "Boots", "Shield"};
    private static final String[] WeaponTypeOptions = {"Dagger", "Longsword", "Greatsword","Shortbow", "Longbow", "Crossbow"};
    private static final String[] RarityNames = {"Common", "Uncommon", "Rare", "Epic", "Legendary"};
    int model;

    public static Material[][] ArmorOptions = {
            {Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET},
            {Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE},
            {Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS},
            {Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS},
            {Material.SHIELD, Material.SHIELD, Material.SHIELD, Material.SHIELD, Material.SHIELD}
    };

    //Constructor
    public PlayerEquipment(int rarity, Material itemType, String tool_type)
    {
        this.Rarity = rarity;
        this.ItemType = itemType;
        this.toolType = tool_type;

        switch (tool_type){
            case "Dagger":
                this.model = 20020;
                break;
            case "Longsword":
                this.model = 20021;
                break;
            case "Greatsword":
                this.model = 20022;
                break;
            case "Shortbow":
                this.model = 20023;
                break;
            case "Crossbow":
                this.model = 20024;
                break;
            case "Longbow":
                this.model = 20025;
                break;
            case "Shield":
                this.model = 20026;
                break;
            default:
                this.model = 0;
                break;
        }

    }

    public PlayerEquipment(PlayerEquipment other)
    {
         this.MaxHealth = other.getMaxHealth();
         this.HealthRegen = other.getHealthRegen();
         this.Defense = other.getDefense();
         this.Damage = other.getDamage();
         this.MaxMana = other.getMaxMana();
         this.ManaRegen = other.getManaRegen();
         this.MaxStamina = other.getMaxStamina();
         this.StaminaRegen = other.getStaminaRegen();
         this.MovementSpeed = other.getMovementSpeed();
         this.MultiHit = other.getMultiHit();
         this.CritChance = other.getCritChance();
         this.CritDamageMult = other.getCritDamageMult();
         this.ItemType = other.getItemType();
         this.Name = other.getCanonName();
         this.Rarity = other.getRarity();
         this.StaminaCost = other.getStaminaCost();
         this.Level = other.getLevel();
         this.isMagic = other.isMagic();
         this.toolType = other.getToolType();
         this.runes = other.getRunes();
         this.model = other.model;
    }

    //Getters and Setters
    public boolean isMagic() {
        return isMagic;
    }
    public void setMagic(boolean magic) {
        isMagic = magic;
    }
    public int getLevel() {
        return Level;
    }
    public void setLevel(int level) {
        Level = level;
    }
    public String getToolType() {
        return toolType;
    }
    public void setToolType(String toolType) {
        this.toolType = toolType;
    }
    public Rune[] getRunes() {
        return runes;
    }
    public void setRunes(Rune[] runes) {
        this.runes = runes;
    }
    public double getStaminaCost() {
        return StaminaCost;
    }
    public void setStaminaCost(double staminaCost) {
        StaminaCost = staminaCost;
    }
    public String getName() {
        if (getRunes() != null && getRunes().length > 0)
        {
            return "Lv" + getLevel() + " " + getRunes()[0].getPrefix() + " " + Name + " " + getRunes()[0].getSuffix();
        }
        else
        {
            return "Lv" + getLevel() + " " + Name;
        }
    }
    public void setName(String name) {
        Name = name;
    }
    public String getCanonName()
    {
        return Name;
    }
    public int getRarity() {
        return Rarity;
    }
    public void setRarity(int rarity) {
        Rarity = rarity;
    }
    public double getMaxHealth() {
        return MaxHealth;
    }
    public void setMaxHealth(double maxHealth) {
        MaxHealth = maxHealth;
    }
    public double getDamage() {
        return Damage;
    }
    public void setDamage(double damage) {
        Damage = damage;
    }
    public double getMaxMana() {
        return MaxMana;
    }
    public void setMaxMana(double maxMana) {
        MaxMana = maxMana;
    }
    public double getMovementSpeed() {
        return MovementSpeed;
    }
    public void setMovementSpeed(double movementSpeed) {
        MovementSpeed = movementSpeed;
    }
    public double getMultiHit() {
        return MultiHit;
    }
    public void setMultiHit(double multiHit) {
        MultiHit = multiHit;
    }
    public double getDefense() {
        return Defense;
    }
    public void setDefense(double defense) {
        Defense = defense;
    }
    public double getHealthRegen() {
        return HealthRegen;
    }
    public void setHealthRegen(double healthRegen) {
        HealthRegen = healthRegen;
    }
    public double getManaRegen() {
        return ManaRegen;
    }
    public void setManaRegen(double manaRegen) {
        ManaRegen = manaRegen;
    }
    public double getStaminaRegen() {
        return StaminaRegen;
    }
    public void setStaminaRegen(double staminaRegen) {
        StaminaRegen = staminaRegen;
    }
    public double getCritChance() {
        return CritChance;
    }
    public void setCritChance(double critChance) {
        CritChance = critChance;
    }
    public double getCritDamageMult() {
        return CritDamageMult;
    }
    public void setCritDamageMult(double critDamageMult) {
        CritDamageMult = critDamageMult;
    }
    public double getMaxStamina() {
        return MaxStamina;
    }
    public void setMaxStamina(double maxStamina) {
        MaxStamina = maxStamina;
    }
    public Material getItemType() {
        return ItemType;
    }
    public void setItemType(Material itemType) {
        ItemType = itemType;
    }
    public static PlayerEquipment ItemToEquipment(ItemStack item)
    {
        if (item.getType() == Material.AIR) {
            return new PlayerEquipment(0, item.getType(), "");
        }
        PlayerEquipment NewEquipment;

        if (item.getItemMeta().hasCustomModelData())
            NewEquipment = new PlayerEquipment(0, item.getType(), getToolTypeFromModel(item.getItemMeta().getCustomModelData()));
        else
            NewEquipment = new PlayerEquipment(0, item.getType(), "");

        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "damage"), PersistentDataType.DOUBLE))
            NewEquipment.setDamage(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "damage"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "defense"), PersistentDataType.DOUBLE))
            NewEquipment.setDefense(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "defense"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxhealth"), PersistentDataType.DOUBLE))
            NewEquipment.setMaxHealth(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxhealth"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxstamina"), PersistentDataType.DOUBLE))
            NewEquipment.setMaxStamina(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxstamina"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxmana"), PersistentDataType.DOUBLE))
            NewEquipment.setMaxMana(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxmana"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "healthregen"), PersistentDataType.DOUBLE))
            NewEquipment.setHealthRegen(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "healthregen"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "staminaregen"), PersistentDataType.DOUBLE))
            NewEquipment.setStaminaRegen(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "staminaregen"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "manaregen"), PersistentDataType.DOUBLE))
            NewEquipment.setManaRegen(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "manaregen"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "multihit"), PersistentDataType.DOUBLE))
            NewEquipment.setMultiHit(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "multihit"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "critchance"), PersistentDataType.DOUBLE))
            NewEquipment.setCritChance(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "critchance"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "critdamagemult"), PersistentDataType.DOUBLE))
            NewEquipment.setCritDamageMult(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "critdamagemult"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "movementspeed"), PersistentDataType.DOUBLE))
            NewEquipment.setMovementSpeed(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "movementspeed"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "name"), PersistentDataType.STRING))
            NewEquipment.setName(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "name"), PersistentDataType.STRING));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "rarity"), PersistentDataType.INTEGER))
            NewEquipment.setRarity(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "rarity"), PersistentDataType.INTEGER));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "staminacost"), PersistentDataType.DOUBLE))
            NewEquipment.setStaminaCost(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "staminacost"), PersistentDataType.DOUBLE));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "runes"), PersistentDataType.STRING))
            NewEquipment.setRunes(NewEquipment.StringToRunes(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "runes"), PersistentDataType.STRING)));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "tooltype"), PersistentDataType.STRING))
            NewEquipment.setToolType(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "tooltype"), PersistentDataType.STRING));

        if (data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "level"), PersistentDataType.INTEGER))
            NewEquipment.setLevel(data.get(new NamespacedKey(FirstPluginThree.getMyPlugin(), "level"), PersistentDataType.INTEGER));


        return NewEquipment;
    }

    public PlayerEquipment applyRunes()
    {
     PlayerEquipment EquipWithRunes = new PlayerEquipment(this);
        //adds the effects of runes into the stats of the equipment and returns it as a new playerequipment
        if (EquipWithRunes.runes != null)
        {
            Material[] colors = new Material[4];
            List<Material> ColorCount = new ArrayList<>();

            //fills the list with color counts
            for (int i = 0; i < EquipWithRunes.runes.length; i++) {
                colors[2*i] = EquipWithRunes.runes[i].getColor();
                colors[2*i+1] = EquipWithRunes.runes[i].getSecondaryColor();
            }
            Collections.addAll(ColorCount, colors);

            //variables for each individual count
            int RedCount = Collections.frequency(ColorCount, Material.RED_DYE);
            int OrangeCount = Collections.frequency(ColorCount, Material.ORANGE_DYE);
            int YellowCount = Collections.frequency(ColorCount, Material.YELLOW_DYE);
            int GreenCount = Collections.frequency(ColorCount, Material.GREEN_DYE);
            int BlueCount = Collections.frequency(ColorCount, Material.BLUE_DYE);
            int PurpleCount = Collections.frequency(ColorCount, Material.PURPLE_DYE);

            //for red runes
            EquipWithRunes.setDamage(EquipWithRunes.getDamage() * (1 + (RedCount * 0.15)));
            EquipWithRunes.setCritDamageMult(EquipWithRunes.getCritDamageMult() + (0.2 * RedCount));

            //for orange runes
            EquipWithRunes.setCritChance(EquipWithRunes.getCritChance() + (0.08 * OrangeCount));
            EquipWithRunes.setMultiHit(EquipWithRunes.getMultiHit() + (0.12 * OrangeCount));

            //for yellow runes
            EquipWithRunes.setMaxStamina(EquipWithRunes.getMaxStamina() + (1.0 * YellowCount));
            EquipWithRunes.setStaminaRegen(EquipWithRunes.getStaminaRegen() + (0.25 * YellowCount));

            //for green runes
            EquipWithRunes.setMaxHealth(EquipWithRunes.getMaxHealth() * (1 + (GreenCount * 0.10)));
            EquipWithRunes.setHealthRegen(EquipWithRunes.getHealthRegen() * (1 + (GreenCount * 0.25)));
            EquipWithRunes.setDefense(EquipWithRunes.getDefense() * (1 + (GreenCount * 0.05)));

            //for blue runes
            EquipWithRunes.setMaxMana(EquipWithRunes.getMaxMana() * (1 + (BlueCount * 0.20)));
            EquipWithRunes.setManaRegen(EquipWithRunes.getManaRegen() + (BlueCount * 0.25));

            //for purple runes
            EquipWithRunes.setMovementSpeed(EquipWithRunes.getMovementSpeed() * (1 + PurpleCount));

        }
        return EquipWithRunes;
    }

    public static ItemStack EquipmentToItem(PlayerEquipment e)
    {
        ItemStack item = new ItemStack(e.getItemType(), 1);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        Style style;
        DecimalFormat df = new DecimalFormat("0.00");

        PlayerEquipment e_WithRunes;
        e_WithRunes = e.applyRunes();

        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "damage"), PersistentDataType.DOUBLE, e.getDamage());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "defense"), PersistentDataType.DOUBLE, e.getDefense());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxhealth"), PersistentDataType.DOUBLE, e.getMaxHealth());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxstamina"), PersistentDataType.DOUBLE, e.getMaxStamina());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "maxmana"), PersistentDataType.DOUBLE, e.getMaxMana());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "healthregen"), PersistentDataType.DOUBLE, e.getHealthRegen());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "staminaregen"), PersistentDataType.DOUBLE, e.getStaminaRegen());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "manaregen"), PersistentDataType.DOUBLE, e.getManaRegen());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "multihit"), PersistentDataType.DOUBLE, e.getMultiHit());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "critchance"), PersistentDataType.DOUBLE, e.getCritChance());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "critdamagemult"), PersistentDataType.DOUBLE, e.getCritDamageMult());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "movementspeed"), PersistentDataType.DOUBLE, e.getMovementSpeed());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "name"), PersistentDataType.STRING, e.getCanonName());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "rarity"), PersistentDataType.INTEGER, e.getRarity());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "staminacost"), PersistentDataType.DOUBLE, e.getStaminaCost());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "runes"), PersistentDataType.STRING, e.RunesToString());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "tooltype"), PersistentDataType.STRING, e.getToolType());
        data.set(new NamespacedKey(FirstPluginThree.getMyPlugin(), "level"), PersistentDataType.INTEGER, e.getLevel());

        ArrayList<Component> LoreSegments = new ArrayList<>();

        if (e_WithRunes.getStaminaCost() > 0.0) {
            LoreSegments.add(Component.text((e_WithRunes.getStaminaCost() + 1) + " Cost", Style.style(NamedTextColor.YELLOW))); //the +1 makes it more understandable
            LoreSegments.add(Component.text(""));
        }

        if (e_WithRunes.getDamage() > 0.0)
            LoreSegments.add(Component.text(df.format(e_WithRunes.getDamage()) + " Damage", Style.style(NamedTextColor.RED)));

        if (e_WithRunes.getDefense() > 0.0)
            LoreSegments.add(Component.text(df.format(e_WithRunes.getDefense()) + " Defense", Style.style(NamedTextColor.GREEN)));

        if (e_WithRunes.getMaxHealth() > 0.0)
            LoreSegments.add(Component.text(df.format(e_WithRunes.getMaxHealth()) + " Max Health", Style.style(NamedTextColor.DARK_RED)));

        if (e_WithRunes.getMaxStamina() > 0.0)
            LoreSegments.add(Component.text(df.format(e_WithRunes.getMaxStamina()) + " Max Stamina", Style.style(NamedTextColor.YELLOW)));

        if (e_WithRunes.getMaxMana() > 0.0)
            LoreSegments.add(Component.text(df.format(e_WithRunes.getMaxMana()) + " Max Mana", Style.style(NamedTextColor.AQUA)));

        if (e_WithRunes.getHealthRegen() > 0.0)
            LoreSegments.add(Component.text(df.format(e_WithRunes.getHealthRegen() * 4) + " Health/s", Style.style(NamedTextColor.DARK_RED, TextDecoration.ITALIC)));

        if (e_WithRunes.getStaminaRegen() > 0.0)
            LoreSegments.add(Component.text(df.format(e_WithRunes.getStaminaRegen() * 4) + " Stamina/s", Style.style(NamedTextColor.YELLOW, TextDecoration.ITALIC)));

        if (e_WithRunes.getManaRegen() > 0.0)
            LoreSegments.add(Component.text(df.format(e_WithRunes.getManaRegen() * 4) + " Mana/s", Style.style(NamedTextColor.AQUA, TextDecoration.ITALIC)));

        if (e_WithRunes.getMultiHit() > 0.0)
            LoreSegments.add(Component.text(df.format(100 * e_WithRunes.getMultiHit()) + "% Multi-Hit", Style.style(NamedTextColor.WHITE)));

        if (e_WithRunes.getCritChance() > 0.0)
            LoreSegments.add(Component.text(df.format(100 * e_WithRunes.getCritChance()) + "% Crit-Chance", Style.style(NamedTextColor.LIGHT_PURPLE)));

        if (e_WithRunes.getCritDamageMult() > 0.0)
            LoreSegments.add(Component.text(df.format(100 * e_WithRunes.getCritDamageMult()) + "% Crit-Damage", Style.style(NamedTextColor.DARK_PURPLE)));

        if (e_WithRunes.getMovementSpeed() != 0.0)
            LoreSegments.add(Component.text(df.format(1000 * e_WithRunes.getMovementSpeed()) + "% Movement Speed", Style.style(NamedTextColor.GRAY)));

        List<Component> lore = LoreSegments;

        if (e_WithRunes.getRunes() != null) {style = e_WithRunes.getRunes()[0].style;}
        else {style = Style.style(NamedTextColor.WHITE);}

        meta.displayName(Component.text(e_WithRunes.getName(), style));
        meta.setCustomModelData(e_WithRunes.model);
        meta.lore(lore);

        AttributeModifier attackSpeedModifier = new AttributeModifier(UUID.randomUUID(), String.valueOf(UUID.randomUUID()), 10f, AttributeModifier.Operation.ADD_NUMBER);

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, attackSpeedModifier);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);

        return item;
    }

    public String RunesToString()
    {
        if (runes == null)
        {
            return "";
        }
        StringBuilder totalString = new StringBuilder();

        for (int i = 0; i < runes.length; i++)
        {
            totalString.append(runes[i].getSavename());
            if (i < (runes.length - 1))
                totalString.append(",");
        }
        return totalString.toString();
    }

    public Rune[] StringToRunes(String string_rune){

        if (Objects.equals(string_rune, ""))
        {
            return (new Rune[0]);
        }

        String[] array_string_rune=string_rune.split(",");
        Rune[] array_rune = new Rune[array_string_rune.length];
        for (int i = 0; i < array_string_rune.length; i++) {
            switch(array_string_rune[i]) {
                case "red":
                    array_rune[i] = new Rune(Material.RED_DYE);
                    break;
                case "orange":
                    array_rune[i] = new Rune(Material.ORANGE_DYE);
                    break;
                case "yellow":
                    array_rune[i] = new Rune(Material.YELLOW_DYE);
                    break;
                case "green":
                    array_rune[i] = new Rune(Material.GREEN_DYE);
                    break;
                case "blue":
                    array_rune[i] = new Rune(Material.BLUE_DYE);
                    break;
                case "purple":
                    array_rune[i] = new Rune(Material.PURPLE_DYE);
                    break;
                default:
                    String[] upgraded_rune_colour_string = array_string_rune[i].split("&");
                    Material[] upgraded_rune_builder = new Material[2];
                    //hi :)
                    for (int j = 0; j < 2; j++) {
                        switch(upgraded_rune_colour_string[j]) {
                            case "red":
                                upgraded_rune_builder[j] = Material.RED_DYE;
                                break;
                            case "orange":
                                upgraded_rune_builder[j] = Material.ORANGE_DYE;
                                break;
                            case "yellow":
                                upgraded_rune_builder[j] = Material.YELLOW_DYE;
                                break;
                            case "green":
                                upgraded_rune_builder[j] = Material.GREEN_DYE;
                                break;
                            case "blue":
                                upgraded_rune_builder[j] = Material.BLUE_DYE;
                                break;
                            case "purple":
                                upgraded_rune_builder[j] = Material.PURPLE_DYE;
                                break;
                        }
                    }
                    array_rune[i]=Rune.CombineRunes(new Rune(upgraded_rune_builder[0]), new Rune(upgraded_rune_builder[1]));
            }
        }
        return array_rune;
    }

    public static int GenerateRarity()
    {
        Random rd = new Random();
        float value = rd.nextFloat();
        if (value < 0.5){
            //common
            return 0;
        }else if (0.5 <= value && value < 0.75) {
            //uncommon
            return 1;
        }else if (0.75 <= value && value < 0.875) {
            //rare
            return 2;
        }else if (0.875 <= value && value < 0.9375) {
            //epic
            return 3;
        }else{
            //legendary
            return 4;
        }
    }
    public static int GenerateArmorType()
    {
        Random rd = new Random();
        float value = rd.nextFloat();
        if (value < 0.2) {
            return 0;
        }else if (0.2 <= value && value < 0.4) {
            return 1;
        }else if (0.4 <= value && value < 0.6) {
            return 2;
        }else if (0.6 <= value && value < 0.8){
            return 3;
        }else{
            return 4;
        }
    }
    public static int GenerateWeaponType()
    {
        Random rd = new Random();
        float value = rd.nextFloat();
        if (value < 0.1667) {
            return 0;
        }else if (0.167 <= value && value < 0.333) {
            return 1;
        }else if (0.333 <= value && value < 0.5) {
            return 2;
        }else if (0.5 <= value && value < 0.667) {
            return 3;
        }else if (0.667 <= value && value < 0.833) {
            return 4;
        }else{
            return 5;
        }
    }
    public static PlayerEquipment GenerateRandomEquipment(LivingEntity mob)
    {



        MobStats mobstats = MobUtility.getMobStats(mob);
        int level = mobstats.getLevel();

        PlayerEquipment new_random_equipment;

        Random rd = new Random();
        String WeaponType;
        int rarity;
        int armorType;
        rarity = PlayerEquipment.GenerateRarity();

        float chosenOption = rd.nextFloat();

        if (chosenOption <= 0.5)
        { //Generate a Weapon
            WeaponType = WeaponTypeOptions[GenerateWeaponType()];
            switch (WeaponType) {
                case "Dagger":
                case "Longsword":
                case "Greatsword":
                    new_random_equipment = new PlayerEquipment(rarity, Material.WOODEN_SWORD, WeaponType);
                    new_random_equipment.setLevel(level);
                    new_random_equipment.setRarity(rarity);
                    new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);

                    break;
                case "Shortbow":
                case "Longbow":
                    new_random_equipment = new PlayerEquipment(rarity, Material.BOW, WeaponType);
                    new_random_equipment.setLevel(level);
                    new_random_equipment.setRarity(rarity);
                    new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);

                    break;
                case "Crossbow":
                    new_random_equipment = new PlayerEquipment(rarity, Material.CROSSBOW, WeaponType);
                    new_random_equipment.setLevel(level);
                    new_random_equipment.setRarity(rarity);
                    new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);
                    break;
                default:  //Should be impossible, but generates an error item for a player to report it
                    new_random_equipment = new PlayerEquipment(rarity, Material.DEAD_BUSH, WeaponType);
                    new_random_equipment.setLevel(level);
                    new_random_equipment.setRarity(rarity);
                    new_random_equipment.setName("Item Generation Error " + new_random_equipment.toolType);
                    break;
            }
        }else {//Generate an armor piece
            armorType = PlayerEquipment.GenerateArmorType();
            new_random_equipment = new PlayerEquipment(rarity, ArmorOptions[armorType][rarity], ArmorNames[armorType]);
            new_random_equipment.setLevel(level);
            new_random_equipment.setRarity(rarity);
            new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);
        }

            new_random_equipment.LevelUp(mob.getType().toString());

        return new_random_equipment;
    }

    public void rollSubStats(String ttype) {
        Random rd = new Random();
        int index;
        boolean isArmor = false;

        for (String armorName : ArmorNames) {
            if (armorName.equals(ttype)) {
                isArmor = true;
                break;
            }
        }

        for (int i = 0; i < Math.floorDiv(this.getLevel(), 10); i++) {


            if (isArmor) {
                index = rd.nextInt(6);
                switch (index) {
                    case 0:
                        setMaxHealth(getMaxHealth() + (Math.round(Balance.mobDmg(this.Level) / (Math.floor(this.Level / 10.0)))));
                        break;
                    case 1:
                        setHealthRegen(getHealthRegen() + Math.round(Balance.mobDmg(this.Level)) * 0.02 / 4); // every roll gives enough regen to heal 0.5% of an equal level mob attack per tick
                        break;
                    case 2:
                        setMaxMana(getMaxMana() + 20.0);
                        break;
                    case 3:
                        setManaRegen(getManaRegen() + 0.25);
                        break;
                    case 4:
                        setMaxStamina(getMaxStamina() + 2.0);
                        break;
                    case 5:
                        setMovementSpeed(getMovementSpeed() + 0.003);
                        break;
                    default:
                }
            }
            else {
                index = rd.nextInt(5);
                switch (index) {
                    case 0:
                        setStaminaRegen(getStaminaRegen() + 0.125);
                        break;
                    case 1:
                        setMultiHit(getMultiHit() + 0.1);
                        break;
                    case 2:
                        setCritChance(getCritChance() + 0.05);
                        break;
                    case 3:
                        setCritDamageMult(getCritDamageMult() + 0.1);
                        break;
                    case 4:
                        setMovementSpeed(getMovementSpeed() + 0.003);
                        break;
                    default:
                }
            }
        }



    }

    public void LevelUp(String mob) {

        String ttype = getToolType();

        rollSubStats(ttype);

        switch (ttype) {
            case "Dagger":
                setStaminaCost(3.0);
                setDamage(Balance.daggerDmg(this.Level));
                break;
            case "Longsword":
                setStaminaCost(5.0);
                setDamage(Balance.longswordDmg(this.Level));
                break;
            case "Greatsword":
                setStaminaCost(14.0);
                setDamage(Balance.greatswordDmg(this.Level));
                break;
            case "Shortbow":
                setStaminaCost(6.0);
                setDamage(Balance.shortbowDmg(this.Level));
                break;
            case "Longbow":
                setStaminaCost(11.0);
                setDamage(Balance.longbowDmg(this.Level));
                break;
            case "Crossbow":
                setStaminaCost(7.0);
                setDamage(Balance.crossbowDmg(this.Level));
                break;
            case "Helmet":
                setDefense(Balance.helmDef(this.Level));
                break;
            case "Chestplate":
                setDefense(Balance.chestDef(this.Level));
                break;
            case "Leggings":
                setDefense(Balance.legsDef(this.Level));
                break;
            case "Boots":
                setDefense(Balance.bootsDef(this.Level));
                break;
            case "Shield":
                setDefense(Balance.shieldDef(this.Level));
                break;
            default:
        }
        //add mob based modifers, amplified by the rarity
        switch (mob) {
            case "ZOMBIE":
                //+Damage | -MoveSpeed
                setDamage(getDamage() * (1.1 + (getRarity() * 0.05)));
                setMovementSpeed(getMovementSpeed() - (0.008 - (getRarity() * 0.002)));

                break;
            case "SKELETON":
                // +Defense | +CritChance
                setDefense(getDefense() * (1.1 + (getRarity() * 0.1)));
                setCritChance(getCritChance() * (1.1 + (getRarity() * 0.1)));

                break;
            case "CREEPER":
                // +CritDamage | -Defense
                setCritDamageMult(getCritDamageMult() * (1.1 + (getRarity() * 0.1)));
                setDefense(getDefense() * (0.8 + (getRarity() * 0.05)));

                break;
            case "SPIDER":
                // +MoveSpeed | +MaxStamina
                setMovementSpeed(getMovementSpeed() * (1 + (getRarity() * 0.25)));
                setMaxStamina(getMaxStamina() * (1.1 + (getRarity() * 0.1)));

                break;
            case "ENDERMAN":
                // +MaxMana | +MaxHealth
                setMaxMana(getMaxMana() * (1.1 + getRarity() * 0.1));
                setMaxHealth(getMaxHealth() * (1.1 + getRarity() * 0.1));
                break;
            case "BLAZE":
                // +MultiHit | +ManaRegen
                setMultiHit(getMultiHit() * (1.1 + getRarity() * 0.1));
                setManaRegen(getManaRegen() * (1.1 + getRarity() * 0.1));

                break;
            case "GHAST":
                // +Stamina Regen
                setStaminaRegen(getStaminaRegen() + (0.025 + (0.025 * getRarity())));
                break;
            case "WITCH":
                // +HealthRegen | +ManaRegen
                setHealthRegen(getHealthRegen() * (1.1 + getRarity() * 0.1));
                setManaRegen(getManaRegen() * (1.1 + getRarity() * 0.1));
                break;
        }
    }

    public static PlayerEquipment GenerateSpecificEquipment(int level, int rarity, String mobType, String toolType) {
        PlayerEquipment new_e = new PlayerEquipment(rarity, Material.AIR, toolType);

        new_e.setLevel(level);
        new_e.setName(RarityNames[rarity] + " " + new_e.toolType);
        switch (toolType){
            case "Dagger":
            case "Longsword":
            case "Greatsword":
                new_e.setItemType(Material.WOODEN_SWORD);
                break;
            case "Shortbow":
            case "Longbow":
                new_e.setItemType(Material.BOW);
                break;
            case "Crossbow":
                new_e.setItemType(Material.CROSSBOW);
                break;
            case "Helmet":
                new_e.setItemType(ArmorOptions[0][rarity]);
                break;
            case "Chestplate":
                new_e.setItemType(ArmorOptions[1][rarity]);
                break;
            case "Leggings":
                new_e.setItemType(ArmorOptions[2][rarity]);
                break;
            case "Boots":
                new_e.setItemType(ArmorOptions[3][rarity]);
                break;
            case "Shield":
                new_e.setItemType(Material.SHIELD);
                break;
        }

        new_e.LevelUp(mobType);

        return new_e;
    }


    public static boolean isEquip(ItemStack item)
    {
        if (item == null) {return false;}
        PersistentDataContainer data = item.getItemMeta().getPersistentDataContainer();

        return data.has(new NamespacedKey(FirstPluginThree.getMyPlugin(), "name"), PersistentDataType.STRING);
    }

    private static String getToolTypeFromModel(int modelID) {
        switch (modelID) {
            case 20020:
                return "Dagger";
            case 20021:
                return "Longsword";
            case 20022:
                return "Greatsword";
            case 20023:
                return "Shortbow";
            case 20024:
                return "Crossbow";
            case 20025:
                return "Longbow";
            case 20026:
                return "Shield";
            default:
                return "";
        }
    }

    public boolean isDagger()
    {
        return Objects.equals(getToolType(), "Dagger");
    }
    public boolean isLongSword()
    {
        return Objects.equals(getToolType(), "Longsword");
    }
    public boolean isGreatSword()
    {
        return Objects.equals(getToolType(), "Greatsword");
    }
    public boolean isShortBow()
    {
        return Objects.equals(getToolType(), "Shortbow");
    }
    public boolean isCrossbow()
    {
        return Objects.equals(getToolType(), "Crossbow");
    }
    public boolean isLongBow()
    {
        return Objects.equals(getToolType(), "Longsword");
    }
    public boolean isEquipmentShield()
    {
        return Objects.equals(getToolType(), "Shield");
    }

}

