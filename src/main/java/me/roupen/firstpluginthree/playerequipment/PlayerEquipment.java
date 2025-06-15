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
import org.bukkit.entity.*;
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
    private static final String[] ArmorNames = {"Helmet", "Chestplate", "Leggings", "Boots", "Shield"};
    private static final String[] WeaponTypeOptions = {"Dagger", "Longsword", "Greatsword","Shortbow", "Longbow", "Crossbow"};
    int model;

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
        if (getRunes() != null)
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

    public void applyRunes()
    {
        //adds the effects of runes into the stats of the equipment and returns it as a new playerequipment
        if (runes != null)
        {
            Material[] colors = new Material[4];
            List<Material> ColorCount = new ArrayList<>();

            //fills the list with color counts
            for (int i = 0; i < runes.length; i++) {
                colors[2*i] = runes[i].getColor();
                colors[2*i+1] = runes[i].getSecondaryColor();
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
            setDamage(getDamage() * (1 + (RedCount * 0.15)));
            setCritDamageMult(getCritDamageMult() + (0.05 * RedCount));

            //for orange runes
            setCritChance(getCritChance() + (0.06 * OrangeCount));
            setMultiHit(getMultiHit() + (0.12 * OrangeCount));

            //for yellow runes
            setMaxStamina(getMaxStamina() + (0.75 * YellowCount));
            setStaminaRegen(getStaminaRegen() + (0.25 * YellowCount));

            //for green runes
            setMaxHealth(getMaxHealth() * (1 + (GreenCount * 0.10)));
            setHealthRegen(getHealthRegen() * (1 + (GreenCount * 0.20)));
            setDefense(getDefense() * (1 + (GreenCount * 0.05)));

            //for blue runes
            setMaxMana(getMaxMana() * (1 + (BlueCount * 0.15)));
            setManaRegen(getManaRegen() + (BlueCount * 0.5));

            //for purple runes
            setMovementSpeed(getMovementSpeed() * (1 + (PurpleCount * 0.005)) + (0.005 * PurpleCount));

        }
    }

    public static ItemStack EquipmentToItem(PlayerEquipment e)
    {
        ItemStack item = new ItemStack(e.getItemType(), 1);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        Style style;
        DecimalFormat df = new DecimalFormat("0.00");

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

        if (e.getStaminaCost() > 0.0) {
            LoreSegments.add(Component.text((e.getStaminaCost() + 1) + " Cost", Style.style(NamedTextColor.YELLOW))); //the +1 makes it more understandable
            LoreSegments.add(Component.text(""));
        }

        if (e.getDamage() > 0.0)
            LoreSegments.add(Component.text(df.format(e.getDamage()) + " Damage", Style.style(NamedTextColor.RED)));

        if (e.getDefense() > 0.0)
            LoreSegments.add(Component.text(df.format(e.getDefense()) + " Defense", Style.style(NamedTextColor.GREEN)));

        if (e.getMaxHealth() > 0.0)
            LoreSegments.add(Component.text(df.format(e.getMaxHealth()) + " Max Health", Style.style(NamedTextColor.DARK_RED)));

        if (e.getMaxStamina() > 0.0)
            LoreSegments.add(Component.text(df.format(e.getMaxStamina()) + " Max Stamina", Style.style(NamedTextColor.YELLOW)));

        if (e.getMaxMana() > 0.0)
            LoreSegments.add(Component.text(df.format(e.getMaxMana()) + " Max Mana", Style.style(NamedTextColor.AQUA)));

        if (e.getHealthRegen() > 0.0)
            LoreSegments.add(Component.text(df.format(e.getHealthRegen() * 4) + " Health/s", Style.style(NamedTextColor.DARK_RED, TextDecoration.ITALIC)));

        if (e.getStaminaRegen() > 0.0)
            LoreSegments.add(Component.text(df.format(e.getStaminaRegen() * 4) + " Stamina/s", Style.style(NamedTextColor.YELLOW, TextDecoration.ITALIC)));

        if (e.getManaRegen() > 0.0)
            LoreSegments.add(Component.text(df.format(e.getManaRegen() * 4) + " Mana/s", Style.style(NamedTextColor.AQUA, TextDecoration.ITALIC)));

        if (e.getMultiHit() > 0.0)
            LoreSegments.add(Component.text(df.format(100 * e.getMultiHit()) + "% Multi-Hit", Style.style(NamedTextColor.WHITE)));

        if (e.getCritChance() > 0.0)
            LoreSegments.add(Component.text(df.format(100 * e.getCritChance()) + "% Crit-Chance", Style.style(NamedTextColor.LIGHT_PURPLE)));

        if (e.getCritDamageMult() > 0.0)
            LoreSegments.add(Component.text(df.format(100 * e.getCritDamageMult()) + "% Crit-Damage", Style.style(NamedTextColor.DARK_PURPLE)));

        if (e.getMovementSpeed() != 0.0)
            LoreSegments.add(Component.text(df.format(1000 * e.getMovementSpeed()) + "% Movement Speed", Style.style(NamedTextColor.GRAY)));

        List<Component> lore = LoreSegments;

        if (e.getRunes() != null) {style = e.getRunes()[0].style;}
        else {style = Style.style(NamedTextColor.WHITE);}

        meta.displayName(Component.text(e.getName(), style));
        meta.setCustomModelData(e.model);
        meta.lore(lore);

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

        Material[][] ArmorOptions = {
                {Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET},
                {Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE},
                {Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS},
                {Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS},
                {Material.SHIELD, Material.SHIELD, Material.SHIELD, Material.SHIELD, Material.SHIELD}
        };

        String[] RarityNames = {"Common", "Uncommon", "Rare", "Epic", "Legendary"};

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
            if (WeaponType.equals("Dagger") || WeaponType.equals("Longsword") || WeaponType.equals("Greatsword")) {
                new_random_equipment = new PlayerEquipment(rarity, Material.WOODEN_SWORD, WeaponType);
                new_random_equipment.setLevel(level);
                new_random_equipment.setRarity(rarity);
                new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);

            }else if (WeaponType.equals("Shortbow") || WeaponType.equals("Longbow")) {
                new_random_equipment = new PlayerEquipment(rarity, Material.BOW, WeaponType);
                new_random_equipment.setLevel(level);
                new_random_equipment.setRarity(rarity);
                new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);

            }else if (WeaponType.equals("Crossbow")) {
                new_random_equipment = new PlayerEquipment(rarity, Material.CROSSBOW, WeaponType);
                new_random_equipment.setLevel(level);
                new_random_equipment.setRarity(rarity);
                new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);
            }else{ //Generate a Shield
                new_random_equipment = new PlayerEquipment(rarity, Material.SHIELD, WeaponType);
                new_random_equipment.setLevel(level);
                new_random_equipment.setRarity(rarity);
                new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);
            }
        }else {//Generate an armor piece
            armorType = PlayerEquipment.GenerateArmorType();
            new_random_equipment = new PlayerEquipment(rarity, ArmorOptions[armorType][rarity], ArmorNames[armorType]);
            new_random_equipment.setLevel(level);
            new_random_equipment.setRarity(rarity);
            new_random_equipment.setName(RarityNames[rarity] + " " + new_random_equipment.toolType);
        }

            new_random_equipment.LevelUp(mob);

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
                        setMaxHealth(getMaxHealth() + 50);
                        break;
                    case 1:
                        setHealthRegen(getHealthRegen() + 0.25);
                        break;
                    case 2:
                        setDefense(getDefense() + 10);
                        break;
                    case 3:
                        setMaxMana(getMaxMana() + 20);
                        break;
                    case 4:
                        setManaRegen(getManaRegen() + 0.25);
                        break;
                    case 5:
                        setMaxStamina(getMaxStamina() + 2.0);
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
                    default:
                }
            }
        }



    }

    public void LevelUp(LivingEntity mob) {

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
        if (mob instanceof Zombie) {
            setDamage(getDamage() * (1.1 + (getRarity() * 0.05)));
            setMovementSpeed(getMovementSpeed() - (0.008 - (getRarity() * 0.002)));

        }else if (mob instanceof Skeleton) {
            setDefense(getDefense() * 1.1);
            setCritChance(getCritChance() * (1.0 + (getRarity() * 0.05)));

        } else if (mob instanceof Creeper) {
            setCritDamageMult(getCritDamageMult() * 1.1);
            setStaminaRegen(getStaminaRegen() * (1.0 + (getRarity() * 0.05)));

        }else if (mob instanceof Spider) {
            setMovementSpeed(getMovementSpeed() + (0.01 + (getRarity() * 0.002)));
            setMaxMana(getMaxMana() * (1.1 + (getRarity() * 0.05)));

        }else if (mob instanceof Enderman) {
            setManaRegen(getManaRegen() * (1.1 + getRarity() * 0.05));
            setMaxHealth(getMaxHealth() * (1.1 + getRarity() * 0.05));

        }

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

