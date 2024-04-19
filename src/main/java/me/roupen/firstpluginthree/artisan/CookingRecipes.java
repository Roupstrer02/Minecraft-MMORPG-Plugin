package me.roupen.firstpluginthree.artisan;

import me.roupen.firstpluginthree.FirstPluginThree;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
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
        return new NamespacedKey(FirstPluginThree.getMyPlugin(), keyName);
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

        nameKey = makeNameKey("blonderoastcoffeemix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(newPotionItem(new PotionData(PotionType.WATER)));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("blonde roasted coffee beans")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 5);

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.WATER)), 1, "Medium Roast Coffee Mix","An absolute classic!", "just needs to be warmed up...");
        nameKey = makeNameKey("mediumroastcoffeemix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(newPotionItem(new PotionData(PotionType.WATER)));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("medium roasted coffee beans")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 6);

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.WATER)), 1, "Dark Roast Coffee Mix","A bitter tasting start to a sweet day!", "just needs to be warmed up...");
        nameKey = makeNameKey("darkroastcoffeemix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(newPotionItem(new PotionData(PotionType.WATER)));
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("dark roasted coffee beans")));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 7);

        //==========================================================================================================
        //Coffees

        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Blonde Roast Coffee","A light start to the day!"));
        nameKey = makeNameKey("blonderoastcoffee");

        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Blonde Roast Coffee Mix")), 0F, MinutesToTick(3));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Medium Roast Coffee","Good Morning!"));
        nameKey = makeNameKey("mediumroastcoffee");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Medium Roast Coffee Mix")), 0F, MinutesToTick(3));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Dark Roast Coffee","A bitter taste to start a sweet new day!"));
        nameKey = makeNameKey("darkroastcoffee");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Dark Roast Coffee Mix")), 0F, MinutesToTick(3));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Iced Coffee

        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Blonde Roast Coffee","Wakes you up in more ways than one!"));

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
        addToLevelLockMap(newItemName, 12);

        //==========================================================================================================
        //Espresso

        //Espresso dose
        newItem = giveEnchantedLook(newCraftableItem(Material.BROWN_DYE, 1, "Espresso Dose", "Packs a punch!!"));
        nameKey = makeNameKey("espressodose");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(9, Items.get("blonde roasted coffee beans"));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 15);

        //Espresso mix
        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Espresso Mix","More Caffeine!!"));
        nameKey = makeNameKey("espressomix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(Items.get("Espresso Dose"));
        newShapelessRecipe.addIngredient(newPotionItem(new PotionData(PotionType.WATER)));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 15);

        //Final item: Espresso
        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Espresso","I'M AWAKE!!! O_O"));
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
        addToLevelLockMap(newItemName, 16);

        //Final item: Latte
        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Latte", "You must like this a latte?","..."));
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
        addToLevelLockMap(newItemName, 18);

        //Cappucino mix
        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Cappucino Mix","It's pretty much in the name");
        nameKey = makeNameKey("cappucinomix");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(Items.get("Espresso Mix")));
        newShapelessRecipe.addIngredient(Items.get("Milk Froth"));
        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);
        addToLevelLockMap(newItemName, 18);

        //Final item: Cappucino
        newItem = giveEnchantedLook(newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Cappucino","Caffeine but more posh!"));
        nameKey = makeNameKey("cappucino");

        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Cappucino Mix")), 0F, SecondsToTick(36));
        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //French Vanilla

        //Sugar cane + Milk Bucket => French Syrup
        //Espresso + French Syrup => French vanilla "Finalement, quelque chose de bon!"

        //Find a "special" option for medium and dark roasts

    }

    //====================================================================================
    private void initCookingItemRecipes(ArrayList<Recipe> ListOfRecipes) {

        initCoffeeRecipes(ListOfRecipes);

        //==========================================================================================================
        //Sweet Bread [Artisan Lv 15 locked]

        newItem = newCraftableItem(Material.BREAD, 1, "Sweet Bread","Bread but better!");

        nameKey = makeNameKey("sweetbread");
        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(3, Material.BREAD);
        newShapelessRecipe.addIngredient(1, Material.HONEY_BOTTLE);

        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

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

        //==========================================================================================================
        //Thorylin Mead

        newItem = newCraftableItem(Material.HONEY_BOTTLE, 1, "Thorylin Mead","Brothers of the Mine Rejoice!");
        newItemMeta = newItem.getItemMeta();
        newItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        newItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        newItem.setItemMeta(newItemMeta);
        nameKey = makeNameKey("thorylinmead");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Unfermented Mead")), 0F, MinutesToTick(5));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);

        //==========================================================================================================
        //Beer Wort [Artisan Lv 18 locked]

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Beer Wort","It's almost ready lads!");
        nameKey = makeNameKey("beerwort");

        newShapelessRecipe = new ShapelessRecipe(nameKey, newItem);
        newShapelessRecipe.addIngredient(8, Material.WHEAT);
        newShapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(newPotionItem(new PotionData(PotionType.WEAKNESS))));

        addToMaps(newItemName, newItem, newShapelessRecipe);
        ListOfRecipes.add(newShapelessRecipe);

        //==========================================================================================================
        //Embermoorian Mead

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Embermoorian Beer","A favorite amongst the workers of the Engine!");
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

        //==========================================================================================================
        //Ithilian Pine Blend

        newItem = newCraftableItem(newPotionItem(new PotionData(PotionType.AWKWARD)), 1, "Ithilian Pine Blend","A drink fit for stargazing in Iliasing");
        newItem = giveEnchantedLook(newItem);
        nameKey = makeNameKey("ithilianpineblend");
        newSmokingRecipe = new SmokingRecipe(nameKey, newItem, new RecipeChoice.ExactChoice(Items.get("Lukewarm Tea Blend")), 0F, MinutesToTick(1));

        addToMaps(newItemName, newItem, newSmokingRecipe);
        ListOfRecipes.add(newSmokingRecipe);
    }
    //====================================================================================

    public void initRecipes() {

        ArrayList<Recipe> ListOfRecipes = new ArrayList<>();

        initCookingItemRecipes(ListOfRecipes);

        //==========================================================================================================
        //END OF RECIPES
        //==========================================================================================================

        //Adding all of the recipes defined above into the server
        for (Recipe recipe : ListOfRecipes) {
            FirstPluginThree.getMyPlugin().getServer().addRecipe(recipe);
        }

    }
}
