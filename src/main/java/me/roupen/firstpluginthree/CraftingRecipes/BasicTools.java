package me.roupen.firstpluginthree.CraftingRecipes;

import me.roupen.firstpluginthree.Zelandris;
import me.roupen.firstpluginthree.balance.Balance;
import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import java.util.ArrayList;
import java.util.HashMap;

public class BasicTools {


    private HashMap<String, Recipe> Recipes;
    private HashMap<String, ItemStack> Items;
    private HashMap<String, Integer> ItemLevelLock;


    private NamespacedKey nameKey;
    private ItemStack newItem;
    private ItemMeta newItemMeta;
    private String newItemName;
    private ArrayList<Component> newItemLore;
    private ShapedRecipe newShapedRecipe;
    private ShapelessRecipe newShapelessRecipe;
    private FurnaceRecipe newFurnaceRecipe;
    private SmokingRecipe newSmokingRecipe;
    public BasicTools() {

        this.Recipes = new HashMap<>();
        this.Items = new HashMap<>();
        this.ItemLevelLock = new HashMap<>();

        //I honestly wrote this so that I stop getting suggestions from IntelliJ saying that I can localise these variables in initRecipes()
        this.newItem = null;
        this.newItemMeta = null;
        this.newItemName = "";
        this.newShapedRecipe = null;
        this.newShapelessRecipe = null;
        this.newFurnaceRecipe = null;
        this.newSmokingRecipe = null;
        this.newItemLore = new ArrayList<>();

    }

    public HashMap<String, Recipe> getRecipes() {
        return Recipes;
    }
    public HashMap<String, ItemStack> getItems() {
        return Items;
    }
    public HashMap<String, Integer> getItemLevelLock() {
        return ItemLevelLock;
    }
    private void addToLevelLockMap(String key, int minLevel) {
        ItemLevelLock.put(key, minLevel);
    }
    private NamespacedKey makeNameKey(String keyName) {
        return new NamespacedKey(Zelandris.getMyPlugin(), keyName);
    }
    private void addToMaps(String name, ItemStack item, Recipe recipe) {
        Items.put(name, item);
        Recipes.put(name, recipe);
    }

    private int SecondsToTick(int seconds) {
        return 20 * seconds;
    }
    private int MinutesToTick(int minutes) {
        return 20 * 60 * minutes;
    }

    private ItemStack newCraftableItem(Material material, int amount, String name, String... lore) {
        newItemName = name;

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        ArrayList<Component> itemLore = new ArrayList<>();
        for (String line : lore) {
            itemLore.add(Component.text(line));
        }
        meta.lore(itemLore);
        item.setItemMeta(meta);

        return item;
    }
    private ItemStack newCraftableItem(ItemStack item, int amount, String name) {
        newItemName = name;

        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        item.setItemMeta(meta);

        return item;
    }
    private ItemStack newPotionItem(PotionData data) {
        ItemStack newPotion = new ItemStack(Material.POTION);
        PotionMeta pm = (PotionMeta) newPotion.getItemMeta();
        pm.setBasePotionData(data);
        newPotion.setItemMeta(pm);

        return newPotion;
    }
    private ItemStack giveEnchantedLook(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    private void initBasicTools(ArrayList<Recipe> ListOfRecipes) {
        PlayerEquipment newEquip;
        ItemMeta itemMeta;
        RecipeChoice.MaterialChoice PlankChoice = new RecipeChoice.MaterialChoice(
                Material.ACACIA_PLANKS, Material.OAK_PLANKS, Material.BIRCH_PLANKS,
                Material.JUNGLE_PLANKS, Material.SPRUCE_PLANKS, Material.CRIMSON_PLANKS,
                Material.DARK_OAK_PLANKS, Material.MANGROVE_PLANKS, Material.WARPED_PLANKS
        );
        RecipeChoice.MaterialChoice CobbleChoice = new RecipeChoice.MaterialChoice(Material.COBBLESTONE, Material.COBBLED_DEEPSLATE, Material.BLACKSTONE);
        //==================================================================================================================
        //Wooden Sword
        newEquip = new PlayerEquipment(0, Material.WOODEN_SWORD, "");
        newEquip.setDamage(Balance.craftedSwordDmg(2));
        newEquip.setStaminaCost(4.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-wooden-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', PlankChoice);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Stone Sword
        newEquip = new PlayerEquipment(0, Material.STONE_SWORD, "");
        newEquip.setDamage(Balance.craftedSwordDmg(5));
        newEquip.setStaminaCost(4.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-stone-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', CobbleChoice);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Gold Sword
        newEquip = new PlayerEquipment(0, Material.GOLDEN_SWORD, "");
        newEquip.setDamage(Balance.craftedSwordDmg(1));
        newEquip.setStaminaCost(4.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-golden-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.GOLD_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Iron Sword
        newEquip = new PlayerEquipment(0, Material.IRON_SWORD, "");
        newEquip.setDamage(Balance.craftedSwordDmg(10));
        newEquip.setStaminaCost(4.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-iron-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.IRON_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Diamond Sword
        newEquip = new PlayerEquipment(0, Material.DIAMOND_SWORD, "");
        newEquip.setDamage(Balance.craftedSwordDmg(15));
        newEquip.setStaminaCost(4.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-diamond-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.DIAMOND);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Netherite Sword
        newEquip = new PlayerEquipment(0, Material.NETHERITE_SWORD, "");
        newEquip.setDamage(Balance.craftedSwordDmg(25));
        newEquip.setStaminaCost(4.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Sword");
        nameKey = makeNameKey("basic-netherite-sword");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","-%-","-S-");

        newShapedRecipe.setIngredient('%', Material.NETHERITE_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Wooden Axe
        newEquip = new PlayerEquipment(0, Material.WOODEN_AXE, "");
        newEquip.setDamage(Balance.craftedAxeDmg(2));
        newEquip.setStaminaCost(7.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Axe");

        nameKey = makeNameKey("basic-wooden-axe");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("%%-","%S-","-S-");
        newShapedRecipe.setIngredient('%', PlankChoice);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-wooden-axe2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("-%%","-S%","-S-");
        newShapedRecipe.setIngredient('%', PlankChoice);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Stone Axe
        newEquip = new PlayerEquipment(0, Material.STONE_AXE, "");
        newEquip.setDamage(Balance.craftedAxeDmg(5));
        newEquip.setStaminaCost(7.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Axe");

        nameKey = makeNameKey("basic-stone-axe");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("%%-","%S-","-S-");
        newShapedRecipe.setIngredient('%', CobbleChoice);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-stone-axe2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("-%%","-S%","-S-");
        newShapedRecipe.setIngredient('%', CobbleChoice);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Iron Axe
        newEquip = new PlayerEquipment(0, Material.IRON_AXE, "");
        newEquip.setDamage(Balance.craftedAxeDmg(10));
        newEquip.setStaminaCost(7.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Axe");

        nameKey = makeNameKey("basic-iron-axe");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("%%-","%S-","-S-");
        newShapedRecipe.setIngredient('%', Material.IRON_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-iron-axe2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("-%%","-S%","-S-");
        newShapedRecipe.setIngredient('%', Material.IRON_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Golden Axe
        newEquip = new PlayerEquipment(0, Material.GOLDEN_AXE, "");
        newEquip.setDamage(Balance.craftedAxeDmg(1));
        newEquip.setStaminaCost(7.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Axe");

        nameKey = makeNameKey("basic-golden-axe");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("%%-","%S-","-S-");
        newShapedRecipe.setIngredient('%', Material.GOLD_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-golden-axe2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("-%%","-S%","-S-");
        newShapedRecipe.setIngredient('%', Material.GOLD_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Diamond Axe
        newEquip = new PlayerEquipment(0, Material.DIAMOND_AXE, "");
        newEquip.setDamage(Balance.craftedAxeDmg(15));
        newEquip.setStaminaCost(7.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Axe");

        nameKey = makeNameKey("basic-diamond-axe");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("%%-","%S-","-S-");
        newShapedRecipe.setIngredient('%', Material.DIAMOND);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-diamond-axe2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("-%%","-S%","-S-");
        newShapedRecipe.setIngredient('%', Material.DIAMOND);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Netherite Axe
        newEquip = new PlayerEquipment(0, Material.NETHERITE_AXE, "");
        newEquip.setDamage(Balance.craftedAxeDmg(25));
        newEquip.setStaminaCost(7.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Axe");

        nameKey = makeNameKey("basic-netherite-axe");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("%%-","%S-","-S-");
        newShapedRecipe.setIngredient('%', Material.NETHERITE_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-netherite-axe2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("-%%","-S%","-S-");
        newShapedRecipe.setIngredient('%', Material.NETHERITE_INGOT);
        newShapedRecipe.setIngredient('S', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Bow
        newEquip = new PlayerEquipment(0, Material.BOW, "");
        newEquip.setDamage(Balance.craftedRangedWeaponDmg(7));
        newEquip.setStaminaCost(5.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Bow");

        nameKey = makeNameKey("basic-bow");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("sB-","s-B","sB-");
        newShapedRecipe.setIngredient('s', Material.STRING);
        newShapedRecipe.setIngredient('B', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-bow2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("-Bs","B-s","-Bs");
        newShapedRecipe.setIngredient('s', Material.STRING);
        newShapedRecipe.setIngredient('B', Material.STICK);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Crossbow
        newEquip = new PlayerEquipment(0, Material.CROSSBOW, "");
        newEquip.setDamage(Balance.craftedRangedWeaponDmg(7));
        newEquip.setStaminaCost(5.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Crossbow");

        nameKey = makeNameKey("basic-crossbow");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("sIs","STS","-s-");
        newShapedRecipe.setIngredient('s', Material.STICK);
        newShapedRecipe.setIngredient('S', Material.STRING);
        newShapedRecipe.setIngredient('T', Material.TRIPWIRE_HOOK);
        newShapedRecipe.setIngredient('I', Material.IRON_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Shield
        newEquip = new PlayerEquipment(0, Material.SHIELD, "");
        newEquip.setDefense(5.0);
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Common Shield");

        nameKey = makeNameKey("basic-shield");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("PIP","PPP","-P-");
        newShapedRecipe.setIngredient('P', PlankChoice);
        newShapedRecipe.setIngredient('I', Material.IRON_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Charcoal block

        newItem = new ItemStack(Material.COAL_BLOCK, 1);
        nameKey = makeNameKey("charcoal-block");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        newShapelessRecipe.addIngredient(9, Material.CHARCOAL);

        addToMaps(newItem.toString(), newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==================================================================================================================
        //slime ball

        newItem = new ItemStack(Material.SLIME_BALL, 4);
        nameKey = makeNameKey("slime-ball");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        newShapelessRecipe.addIngredient(1, Material.SOUL_SAND);
        newShapelessRecipe.addIngredient(1, Material.HONEY_BOTTLE);
        newShapelessRecipe.addIngredient(1, Material.WATER_BUCKET);

        addToMaps(newItem.toString(), newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==================================================================================================================
        //crouton

        newItem = new ItemStack(Material.BREAD, 1);
        itemMeta = newItem.getItemMeta();
        itemMeta.displayName(Component.text("CROUTON"));
        newItem.setItemMeta(itemMeta);
        nameKey = makeNameKey("crouton");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        newShapelessRecipe.addIngredient(2, Material.BREAD);

        addToMaps(newItem.toString(), newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==================================================================================================================
        //coin

        newItem = new ItemStack(Material.GOLD_NUGGET, 12);
        itemMeta = newItem.getItemMeta();
        itemMeta.displayName(Component.text("Coin"));
        newItem.setItemMeta(itemMeta);
        nameKey = makeNameKey("coin");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        newShapelessRecipe.addIngredient(2, Material.GOLD_INGOT);

        addToMaps(newItem.toString(), newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==================================================================================================================
        //Final Remaining Brain Cell

        newItem = new ItemStack(Material.DEAD_BUSH, 1);
        itemMeta = newItem.getItemMeta();
        itemMeta.displayName(Component.text("Final Remaining Brain Cell"));
        newItem.setItemMeta(itemMeta);
        nameKey = makeNameKey("braincell");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("CCP","GSG","LL-");

        newShapedRecipe.setIngredient('C', Material.CYAN_BED);
        newShapedRecipe.setIngredient('G', Material.GREEN_BED);
        newShapedRecipe.setIngredient('L', Material.LIME_BED);
        newShapedRecipe.setIngredient('P', Material.ACACIA_PLANKS);
        newShapedRecipe.setIngredient('S', Material.STICK);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Leather Armor

        //Helmet
        newEquip = new PlayerEquipment(0, Material.LEATHER_HELMET, "Helmet");
        newEquip.setDefense(Balance.helmDef(2));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Leather Helmet");

        nameKey = makeNameKey("basic-leather-helmet");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","---");
        newShapedRecipe.setIngredient('L', Material.LEATHER);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-leather-helmet2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","LLL","L-L");
        newShapedRecipe.setIngredient('L', Material.LEATHER);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Chestplate
        newEquip = new PlayerEquipment(0, Material.LEATHER_CHESTPLATE, "Chestplate");
        newEquip.setDefense(Balance.chestDef(2));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Leather Chestplate");

        nameKey = makeNameKey("basic-leather-chestplate");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","LLL","LLL");
        newShapedRecipe.setIngredient('L', Material.LEATHER);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Leggings
        newEquip = new PlayerEquipment(0, Material.LEATHER_LEGGINGS, "Leggings");
        newEquip.setDefense(Balance.legsDef(2));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Leather Leggings");

        nameKey = makeNameKey("basic-leather-leggings");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.LEATHER);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Boots
        newEquip = new PlayerEquipment(0, Material.LEATHER_BOOTS, "Boots");
        newEquip.setDefense(Balance.bootsDef(2));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Leather Boots");

        nameKey = makeNameKey("basic-leather-boots");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","L-L","---");
        newShapedRecipe.setIngredient('L', Material.LEATHER);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-leather-boots2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.LEATHER);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Gold Armor

        //Helmet
        newEquip = new PlayerEquipment(0, Material.GOLDEN_HELMET, "Helmet");
        newEquip.setDefense(Balance.helmDef(2));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Golden Helmet");

        nameKey = makeNameKey("basic-golden-helmet");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","---");
        newShapedRecipe.setIngredient('L', Material.GOLD_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-golden-helmet2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","LLL","L-L");
        newShapedRecipe.setIngredient('L', Material.GOLD_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Chestplate
        newEquip = new PlayerEquipment(0, Material.GOLDEN_CHESTPLATE, "Chestplate");
        newEquip.setDefense(Balance.chestDef(2));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Golden Chestplate");

        nameKey = makeNameKey("basic-golden-chestplate");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","LLL","LLL");
        newShapedRecipe.setIngredient('L', Material.GOLD_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Leggings
        newEquip = new PlayerEquipment(0, Material.GOLDEN_LEGGINGS, "Leggings");
        newEquip.setDefense(Balance.legsDef(2));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Golden Leggings");

        nameKey = makeNameKey("basic-golden-leggings");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.GOLD_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Boots
        newEquip = new PlayerEquipment(0, Material.GOLDEN_BOOTS, "Boots");
        newEquip.setDefense(Balance.bootsDef(2));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Golden Boots");

        nameKey = makeNameKey("basic-golden-boots");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","L-L","---");
        newShapedRecipe.setIngredient('L', Material.GOLD_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-golden-boots2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.GOLD_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Iron Armor

        //Helmet
        newEquip = new PlayerEquipment(0, Material.IRON_HELMET, "Helmet");
        newEquip.setDefense(Balance.helmDef(8));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Iron Helmet");

        nameKey = makeNameKey("basic-iron-helmet");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","---");
        newShapedRecipe.setIngredient('L', Material.IRON_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-iron-helmet2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","LLL","L-L");
        newShapedRecipe.setIngredient('L', Material.IRON_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Chestplate
        newEquip = new PlayerEquipment(0, Material.IRON_CHESTPLATE, "Chestplate");
        newEquip.setDefense(Balance.chestDef(8));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Iron Chestplate");

        nameKey = makeNameKey("basic-iron-chestplate");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","LLL","LLL");
        newShapedRecipe.setIngredient('L', Material.IRON_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Leggings
        newEquip = new PlayerEquipment(0, Material.IRON_LEGGINGS, "Leggings");
        newEquip.setDefense(Balance.legsDef(8));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Iron Leggings");

        nameKey = makeNameKey("basic-iron-leggings");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.IRON_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Boots
        newEquip = new PlayerEquipment(0, Material.IRON_BOOTS, "Boots");
        newEquip.setDefense(Balance.bootsDef(8));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Iron Boots");

        nameKey = makeNameKey("basic-iron-boots");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","L-L","---");
        newShapedRecipe.setIngredient('L', Material.IRON_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-iron-boots2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.IRON_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Diamond Armor

        //Helmet
        newEquip = new PlayerEquipment(0, Material.DIAMOND_HELMET, "Helmet");
        newEquip.setDefense(Balance.helmDef(15));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Diamond Helmet");

        nameKey = makeNameKey("basic-diamond-helmet");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","---");
        newShapedRecipe.setIngredient('L', Material.DIAMOND);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-diamond-helmet2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","LLL","L-L");
        newShapedRecipe.setIngredient('L', Material.DIAMOND);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Chestplate
        newEquip = new PlayerEquipment(0, Material.DIAMOND_CHESTPLATE, "Chestplate");
        newEquip.setDefense(Balance.chestDef(15));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Diamond Chestplate");

        nameKey = makeNameKey("basic-diamond-chestplate");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","LLL","LLL");
        newShapedRecipe.setIngredient('L', Material.DIAMOND);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Leggings
        newEquip = new PlayerEquipment(0, Material.DIAMOND_LEGGINGS, "Leggings");
        newEquip.setDefense(Balance.legsDef(15));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Diamond Leggings");

        nameKey = makeNameKey("basic-diamond-leggings");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.DIAMOND);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Boots
        newEquip = new PlayerEquipment(0, Material.DIAMOND_BOOTS, "Boots");
        newEquip.setDefense(Balance.bootsDef(15));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Diamond Boots");

        nameKey = makeNameKey("basic-diamond-boots");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","L-L","---");
        newShapedRecipe.setIngredient('L', Material.DIAMOND);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-diamond-boots2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.DIAMOND);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //==================================================================================================================
        //Netherite Armor

        //Helmet
        newEquip = new PlayerEquipment(0, Material.NETHERITE_HELMET, "Helmet");
        newEquip.setDefense(Balance.helmDef(25));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Netherite Helmet");

        nameKey = makeNameKey("basic-netherite-helmet");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","---");
        newShapedRecipe.setIngredient('L', Material.NETHERITE_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-netherite-helmet2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","LLL","L-L");
        newShapedRecipe.setIngredient('L', Material.NETHERITE_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Chestplate
        newEquip = new PlayerEquipment(0, Material.NETHERITE_CHESTPLATE, "Chestplate");
        newEquip.setDefense(Balance.chestDef(25));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Netherite Chestplate");

        nameKey = makeNameKey("basic-netherite-chestplate");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","LLL","LLL");
        newShapedRecipe.setIngredient('L', Material.NETHERITE_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Leggings
        newEquip = new PlayerEquipment(0, Material.NETHERITE_LEGGINGS, "Leggings");
        newEquip.setDefense(Balance.legsDef(25));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Netherite Leggings");

        nameKey = makeNameKey("basic-netherite-leggings");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("LLL","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.NETHERITE_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        //Boots
        newEquip = new PlayerEquipment(0, Material.NETHERITE_BOOTS, "Boots");
        newEquip.setDefense(Balance.bootsDef(25));
        newItem = newCraftableItem(PlayerEquipment.EquipmentToItem(newEquip), 1, "Basic Netherite Boots");

        nameKey = makeNameKey("basic-netherite-boots");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("L-L","L-L","---");
        newShapedRecipe.setIngredient('L', Material.NETHERITE_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

        nameKey = makeNameKey("basic-netherite-boots2");
        newShapedRecipe = new ShapedRecipe(nameKey, newItem);
        newShapedRecipe.shape("---","L-L","L-L");
        newShapedRecipe.setIngredient('L', Material.NETHERITE_INGOT);
        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);

    }


    public void initRecipes() {

        ArrayList<Recipe> ListOfRecipes = new ArrayList<>();

        initBasicTools(ListOfRecipes);

        //==========================================================================================================
        //END OF RECIPES
        //==========================================================================================================

        //Adding all the recipes defined above into the server
        for (Recipe recipe : ListOfRecipes) {
            Zelandris.getMyPlugin().getServer().addRecipe(recipe);
        }

    }
}
