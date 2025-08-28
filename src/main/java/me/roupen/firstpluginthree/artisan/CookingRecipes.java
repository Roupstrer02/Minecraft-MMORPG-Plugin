package me.roupen.firstpluginthree.artisan;

import me.roupen.firstpluginthree.Zelandris;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;


/*
==================================================================================================================
Design Rule of Principle

--> No brewing stands because of technical limitations (will look into that for alchemist update)

==================================================================================================================
 */
public class CookingRecipes {

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
    private Material consumableMaterialType = Material.GOLDEN_APPLE;
    public CookingRecipes() {

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
    private ItemStack newCraftableItem(ItemStack item, int amount, String name, String... lore) {
        newItemName = name;
        item.setAmount(amount);

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
    private void applyModel(ItemStack item, int modelNumber) {

        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(modelNumber);
        item.setItemMeta(meta);
    }

    private void initCoffeeRecipes(ArrayList<Recipe> ListOfRecipes) {
        //==========================================================================================================
        //Coffee Beans

        newItem = newCraftableItem(Material.SWEET_BERRIES, 1, "coffee beans","the unsweetened raw bean with incredible potential!");
        nameKey = makeNameKey("coffeebeans");
        newFurnaceRecipe = new FurnaceRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(new ItemStack(Material.SWEET_BERRIES, 1)), 0F, SecondsToTick(10));

        addToMaps(newItemName, newItem, newFurnaceRecipe);
        ListOfRecipes.add(newFurnaceRecipe);

        //==========================================================================================================
        //Blonde Roast

        newItem = newCraftableItem(Material.COCOA_BEANS, 1, "blonde roasted coffee beans","A lightly roasted coffee bean!");

        nameKey = makeNameKey("blonderoast");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("coffee beans")), 0F, SecondsToTick(10));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Medium Roast

        newItem = newCraftableItem(Material.COCOA_BEANS, 1, "medium roasted coffee beans","Roasted. but more!");

        nameKey = makeNameKey("mediumroast");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("blonde roasted coffee beans")), 0F, SecondsToTick(10));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Dark Roast

        newItem = newCraftableItem(Material.COCOA_BEANS, 1, "dark roasted coffee beans","Please stop roasting the beans...");
        newItemMeta = newItem.getItemMeta();
        newItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        newItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        newItem.setItemMeta(newItemMeta);
        nameKey = makeNameKey("darkroast");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("medium roasted coffee beans")), 0F, SecondsToTick(10));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Coffee mixes

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.WATER)), 1, "Blonde Roast Coffee Mix","A light start to the day!", "just needs to be warmed up...");

        //blonde roast coffee needs 1 blond roast bean item
        nameKey = makeNameKey("blonderoastcoffeemix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(newPotionItem(new PotionData(PotionType.WATER)));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("blonde roasted coffee beans")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 3);

        //blonde roast coffee needs 2 medium roast bean item
        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.WATER)), 1, "Medium Roast Coffee Mix","An absolute classic!", "just needs to be warmed up...");
        nameKey = makeNameKey("mediumroastcoffeemix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(newPotionItem(new PotionData(PotionType.WATER)));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("medium roasted coffee beans")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("medium roasted coffee beans")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 4);

        //blonde roast coffee needs 3 dark roast bean item
        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.WATER)), 1, "Dark Roast Coffee Mix","A bitter tasting start to a sweet day!", "just needs to be warmed up...");
        nameKey = makeNameKey("darkroastcoffeemix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(newPotionItem(new PotionData(PotionType.WATER)));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("dark roasted coffee beans")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("dark roasted coffee beans")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("dark roasted coffee beans")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 5);

        //==========================================================================================================
        //Coffees

        newItem = (newCraftableItem(consumableMaterialType, 1, "Blonde Roast Coffee","A light start to the day!"));
        applyModel(newItem, 1);
        nameKey = makeNameKey("blonderoastcoffee");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Blonde Roast Coffee Mix")), 0F, MinutesToTick(3));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        newItem = newCraftableItem(consumableMaterialType, 1, "Medium Roast Coffee","Good Morning!");
        applyModel(newItem, 2);
        nameKey = makeNameKey("mediumroastcoffee");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Medium Roast Coffee Mix")), 0F, MinutesToTick(3));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        newItem = newCraftableItem(consumableMaterialType, 1, "Dark Roast Coffee","A bitter taste to start a sweet new day!");
        nameKey = makeNameKey("darkroastcoffee");
        applyModel(newItem, 3);
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Dark Roast Coffee Mix")), 0F, MinutesToTick(3));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Iced Coffee

        newItem = newCraftableItem(consumableMaterialType, 1, "Iced Coffee","Wakes you up in more ways than one!");
        applyModel(newItem, 4);
        nameKey = makeNameKey("icedcoffee1");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(Material.ICE);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Blonde Roast Coffee Mix")));
        addToMaps(newItemName + "1", newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        nameKey = makeNameKey("icedcoffee2");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(Material.ICE);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Medium Roast Coffee Mix")));
        addToMaps(newItemName + "2", newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        nameKey = makeNameKey("icedcoffee3");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(Material.ICE);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Dark Roast Coffee Mix")));
        addToMaps(newItemName + "3", newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 8);

        //==========================================================================================================
        //Espresso

        //Espresso dose
        newItem = giveEnchantedLook(newCraftableItem(Material.BROWN_DYE, 1, "Espresso Dose", "Packs a punch!!"));
        nameKey = makeNameKey("espressodose");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(9, Items.get("blonde roasted coffee beans"));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 12);

        //Espresso mix
        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Espresso Mix","More Caffeine!!"));
        nameKey = makeNameKey("espressomix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(Items.get("Espresso Dose"));
        newShapelessRecipe.addIngredient(newPotionItem(new PotionData(PotionType.WATER)));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 12);

        //Final item: Espresso
        newItem = newCraftableItem(consumableMaterialType, 1, "Espresso","I'M AWAKE!!! O_O");
        applyModel(newItem, 5);
        nameKey = makeNameKey("espresso");

        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Espresso Mix")), 0F, SecondsToTick(18));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Latte

        //Latte Mix
        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Latte Mix","Milk with a bit of coffee in it");
        nameKey = makeNameKey("lattemix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Espresso Mix")));
        newShapelessRecipe.addIngredient(Material.MILK_BUCKET);
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 14);

        //Final item: Latte
        newItem = newCraftableItem(consumableMaterialType, 1, "Latte", "You must like this a latte?","...");
        applyModel(newItem, 6);
        nameKey = makeNameKey("latte");

        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Latte Mix")), 0F, SecondsToTick(36));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Cappucino

        //Milk froth
        newItem = newCraftableItem(Material.WHITE_DYE, 4, "Milk Froth","It's pretty much in the name");
        nameKey = makeNameKey("milkfroth");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(Material.MILK_BUCKET);
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 16);

        //Cappucino mix
        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Cappucino Mix","It's pretty much in the name");
        nameKey = makeNameKey("cappucinomix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Espresso Mix")));
        newShapelessRecipe.addIngredient(Items.get("Milk Froth"));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 16);

        //Final item: Cappucino
        newItem = newCraftableItem(consumableMaterialType, 1, "Cappucino","Caffeine but more posh!");
        applyModel(newItem, 7);
        nameKey = makeNameKey("cappucino");

        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Cappucino Mix")), 0F, SecondsToTick(36));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //French Syrup
        //Sugar cane + Milk Bucket => French Syrup
        newItem = newCraftableItem(Material.LIGHT_GRAY_DYE, 1, "French Syrup","Très Sucré!");
        nameKey = makeNameKey("frenchsyrup");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(Material.SUGAR_CANE);
        newShapelessRecipe.addIngredient(Material.MILK_BUCKET);
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 19);

        //French Vanilla
        //Espresso + French Syrup => French vanilla "Finalement, quelque chose de bon!"
        newItem = newCraftableItem(consumableMaterialType, 1, "French Vanilla","Finalement, quelque chose de bon!");
        applyModel(newItem, 8);
        nameKey = makeNameKey("frenchvanilla");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Espresso Mix")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("French Syrup")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 19);
        //==========================================================================================================
        //Chai Tea Latte
        //Milk Froth + tea(?)
        newItem = newCraftableItem(consumableMaterialType, 1, "Chai Tea Latte","The Tea of Tea");
        applyModel(newItem, 9);
        nameKey = makeNameKey("chaitealatte");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Milk Froth")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Ithilian Pine Blend")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 20);

        //==========================================================================================================
        //pumpkin spice latte
        //pumpkin + sugar + bottle water = pumpkin spice
        newItem = newCraftableItem(Material.ORANGE_DYE, 1, "Pumpkin Spice","Made from ReAl PuMpKiN");
        nameKey = makeNameKey("pumpkinspice");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(Material.PUMPKIN);
        newShapelessRecipe.addIngredient(Material.SUGAR);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(newPotionItem(new PotionData(PotionType.WATER))));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 21);

        //pumpkin spice + espresso mix + milk froth = Pumpkin Spice Latte
        newItem = newCraftableItem(consumableMaterialType, 1, "Pumpkin Spice Latte","\"Yes, you are a basic b****\"", "- Dakoda");
        applyModel(newItem, 10);
        nameKey = makeNameKey("pumpkinspicelatte");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Pumpkin Spice")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Milk Froth")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Espresso Mix")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 21);

    }

    //====================================================================================
    private void initCookingItemRecipes(ArrayList<Recipe> ListOfRecipes) {

        //==========================================================================================================
        //Unfermented Mead [Artisan lv 10 locked]

        newItem = newCraftableItem(Material.HONEY_BOTTLE, 1, "Unfermented Mead", "The Precursor to a Dwarven Celebration");
        nameKey = makeNameKey("unfermentedmead");

        ItemStack meadIngredient = newPotionItem(new PotionData(PotionType.AWKWARD));

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-%-","%B%","-%-");

        newShapedRecipe.setIngredient('%', Material.HONEYCOMB);
        newShapedRecipe.setIngredient('B', new RecipeChoice.ExactChoice(meadIngredient));

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);
        addToLevelLockMap(newItemName, 10);

        //==========================================================================================================
        //Thorylin Mead

        newItem = newCraftableItem(consumableMaterialType, 1, "Thorylin Mead","Brothers of the Mine Rejoice!");
        applyModel(newItem, 11);
        newItemMeta = newItem.getItemMeta();
        newItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        newItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        newItem.setItemMeta(newItemMeta);
        nameKey = makeNameKey("thorylinmead");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Unfermented Mead")), 0F, MinutesToTick(5));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        // Crushed grapes [Artisan lv 8 locked]

        newItem = giveEnchantedLook(newCraftableItem(Material.POPPED_CHORUS_FRUIT, 1, "Crushed Grapes", "crushing more flavour into less space"));

        nameKey = makeNameKey("crushedgrapes");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        newShapelessRecipe.addIngredient(9, Material.SWEET_BERRIES);

        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 8);

        //==========================================================================================================
        // Grape juice [Artisan lv 8 locked]

        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.WATER)), 1, "Grape Juice", "Not that filling or tasty..."));

        nameKey = makeNameKey("grapejuice");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("-S-","-G-","-B-");

        newShapedRecipe.setIngredient('S', Material.SMOOTH_STONE_SLAB);
        newShapedRecipe.setIngredient('G', new RecipeChoice.ExactChoice(Items.get("Crushed Grapes")));
        newShapedRecipe.setIngredient('B', Material.GLASS_BOTTLE);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);
        addToLevelLockMap(newItemName, 8);

        //==========================================================================================================
        // Bottle Of Wine

        newItem = newCraftableItem(consumableMaterialType, 1, "Bottle Of Wine","Enjoyed by the upper class all around Zelandris");
        applyModel(newItem, 12);
        nameKey = makeNameKey("bottleofwine");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Grape Juice")), 0F, MinutesToTick(20));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        // Wine Glass [Artisan lv 8 locked]

        newItem = newCraftableItem(Material.GLASS_BOTTLE, 4, "Wine Glass", "Looks nicer than a bottle");
        applyModel(newItem, 13);

        nameKey = makeNameKey("wineglass");

        newShapedRecipe = new ShapedRecipe(nameKey, newItem);

        newShapedRecipe.shape("G-G","-G-","GGG");

        newShapedRecipe.setIngredient('G', Material.GLASS);

        addToMaps(newItemName, newItem, newShapedRecipe);
        ListOfRecipes.add(newShapedRecipe);
        addToLevelLockMap(newItemName, 8);

        //==========================================================================================================
        // 8x Glass Of Wine

        newItem = newCraftableItem(consumableMaterialType, 8, "Glass Of Wine", "The perfect accompaniment to a fancy night out");

        applyModel(newItem, 14);
        nameKey = makeNameKey("glassofwine");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Bottle Of Wine")));
        newShapelessRecipe.addIngredient(8, Material.GLASS_BOTTLE);

        newItem.setAmount(1);
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 8);

        //==========================================================================================================
        // Wet Mash of foods [Artisan Lv 15 locked]
        newItem = newCraftableItem(Material.BROWN_DYE, 1, "Wet Mash Of Foods", "I mean... It's homogenous");

        nameKey = makeNameKey("wetmashoffoods");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);

        newShapelessRecipe.addIngredient(1, Material.WATER_BUCKET);
        newShapelessRecipe.addIngredient(2, Material.WHEAT);
        newShapelessRecipe.addIngredient(2, Material.CARROT);
        newShapelessRecipe.addIngredient(2, Material.POTATO);
        newShapelessRecipe.addIngredient(2, Material.BEETROOT);

        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 15);

        //==========================================================================================================
        // Cooked Mash of foods

        newItem = newCraftableItem(Material.BROWN_DYE, 1, "Cooked Mash Of Foods","Now it's homogenous AND cooked");
        giveEnchantedLook(newItem);
        nameKey = makeNameKey("cookedmashoffoods");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Wet Mash Of Foods")), 0F, MinutesToTick(5));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        // Lunaris-Shine

        newItem = newCraftableItem(consumableMaterialType, 1, "Lunaris-Shine","Named for the celestial body in the sky you will surely see", "during the long process of making this drink");
        applyModel(newItem, 15);
        giveEnchantedLook(newItem);
        nameKey = makeNameKey("lunarisshine");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Cooked Mash Of Foods")), 0F, MinutesToTick(5));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);
        //==========================================================================================================
        //Beer Wort [Artisan Lv 10 locked]

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Beer Wort","It's almost ready lads!");
        nameKey = makeNameKey("beerwort");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(8, Material.WHEAT);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(newPotionItem(new PotionData(PotionType.WEAKNESS))));

        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 10);

        //==========================================================================================================
        //Embermoorian Beer

        newItem = newCraftableItem(consumableMaterialType, 1, "Embermoorian Beer","A favorite amongst the workers of the Engine!");
        applyModel(newItem, 16);
        newItem = giveEnchantedLook(newItem);
        nameKey = makeNameKey("embermoorianbeer");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Beer Wort")), 0F, MinutesToTick(10));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Ithilian Fern Powder

        newItem = newCraftableItem(Material.GREEN_DYE, 1, "Ithilian Fern Powder","An essential ingredient for tea in Iliasing");
        nameKey = makeNameKey("ithilianfernpowder1");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, Material.FERN, 0F, SecondsToTick(30));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        newItem.setAmount(2);
        nameKey = makeNameKey("ithilianfernpowder2");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, Material.LARGE_FERN, 0F, SecondsToTick(45));

        addToMaps(newItemName + "2", newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Lukewarm Tea Blend [Artisan Lv 8 Locked]

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Lukewarm Tea Blend","It's almost ready lads!");
        nameKey = makeNameKey("lukewarmteablend");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Ithilian Fern Powder")));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(newPotionItem(new PotionData(PotionType.WATER))));

        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 8);

        //==========================================================================================================
        //Ithilian Pine Blend

        newItem = newCraftableItem(consumableMaterialType, 1, "Ithilian Pine Blend","A drink fit for stargazing in Iliasing");
        applyModel(newItem, 17);
        newItem = giveEnchantedLook(newItem);
        nameKey = makeNameKey("ithilianpineblend");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Lukewarm Tea Blend")), 0F, MinutesToTick(1));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);
        addToLevelLockMap(newItemName, 8);

        initCoffeeRecipes(ListOfRecipes);
    }
    //====================================================================================

    public void initRecipes() {

        ArrayList<Recipe> ListOfRecipes = new ArrayList<>();

        initCookingItemRecipes(ListOfRecipes);

        //==========================================================================================================
        //END OF RECIPES
        //==========================================================================================================

        //Adding all the recipes defined above into the server
        for (Recipe recipe : ListOfRecipes) {
            Zelandris.getMyPlugin().getServer().addRecipe(recipe);
        }

    }
}
