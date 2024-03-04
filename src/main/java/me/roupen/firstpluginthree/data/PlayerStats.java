package me.roupen.firstpluginthree.data;


import me.roupen.firstpluginthree.playerequipment.PlayerEquipment;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerStats {
    //Permanent Stats

    //Adds to max health
    private int Vitality;

    //Adds to defense
    private int Resilience;

    //Increases mana pool
    private int Intelligence;

    //Each point increases damage from Strength based weapons by 1% (additive)
    private int Strength;

    //Increases the damage of dexterity based weapons by 0.5% and improves movement speed by 0.5% (additive)
    private int Dexterity;

    //Increases mana pool and number of active spells at once (every few points)
    private int Wisdom;
    private int Experience;
    private int Level;

    //Number of unused skill points
    private int SkillPoints;

    //Total Stats (in-game)

    private Player player; //you are a player, I am a developer, we are not the same.

    private double ActiveCurrentHealth;
    private double ActiveMaxHealth;
    private double ActiveDefense;
    private double ActiveDamage;
    private double ActiveCurrentMana;
    private double ActiveMaxMana;
    private double ActiveCurrentStamina;
    private double ActiveMaxStamina;
    private double MovementSpeed = 0.1;
    private double MultiHit;
    private double CritChance = 0.01;
    private double CritDamageMult = 1.5;
    private double StaminaCost = 1.0;

    //update & regen values
    private double ActiveStaminaRegen = 0.5;
    private double ActiveHealthRegen = 0.25;
    private double ActiveManaRegen = 0.125;

    //equipment related stats and values
    private PlayerEquipment equipment = new PlayerEquipment(0, Material.AIR, "");

    //Spellcasting related variables
    private boolean castingSpell = false;

    //getter and setter and adder functions for all statistics
    public double getStaminaCost() {
        return StaminaCost;
    }
    public void setStaminaCost(double staminaCost) {
        StaminaCost = staminaCost;
    }
    public double getActiveDamage() {
        return ActiveDamage;
    }
    public void setActiveDamage(double activeDamage) {
        ActiveDamage = activeDamage;
    }
    public double getMultiHit() {
        return MultiHit;
    }
    public void setMultiHit(double multiHit) {
        MultiHit = multiHit;
    }
    public double getActiveMaxMana() {return ActiveMaxMana;}
    public void setActiveMaxMana(double activeMaxMana) {ActiveMaxMana = activeMaxMana;}
    public int getLevel() {
        return Level;
    }
    public void setLevel(int level) {
        Level = level;
    }
    public double getActiveCurrentHealth() {
        return ActiveCurrentHealth;
    }
    public void setActiveCurrentHealth(double activeCurrentHealth) {
        ActiveCurrentHealth = activeCurrentHealth;
    }
    public double getActiveMaxHealth() {
        return ActiveMaxHealth;
    }
    public void setActiveMaxHealth(double activeMaxHealth) {
        ActiveMaxHealth = activeMaxHealth;
    }
    public double getActiveDefense() {
        return ActiveDefense;
    }
    public void setActiveDefense(double activeDefense) {
        ActiveDefense = activeDefense;
    }
    public double getActiveCurrentMana() {
        return ActiveCurrentMana;
    }
    public void setActiveCurrentMana(double activeMana) {
        ActiveCurrentMana = activeMana;
    }
    public int getVitality() {
        return Vitality;
    }
    public void setVitality(int vitality) {
        Vitality = vitality;
    }
    public void addVitality(int addedvitality) {Vitality += addedvitality;}
    public int getResilience() {
        return Resilience;
    }
    public void setResilience(int defense) {
        Resilience = defense;
    }
    public double getMovementSpeed() {
        return MovementSpeed;
    }
    public void setMovementSpeed(double movementSpeed) {
        MovementSpeed = movementSpeed;
    }
    public double getMultihit() {
        return MultiHit;
    }
    public void setMultihit(double multihit) {
        MultiHit = multihit;
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
    public double getActiveStaminaRegen() {
        return ActiveStaminaRegen;
    }
    public void setActiveStaminaRegen(double activeStaminaRegen) {
        ActiveStaminaRegen = activeStaminaRegen;
    }
    public double getActiveHealthRegen() {
        return ActiveHealthRegen;
    }
    public void setActiveHealthRegen(double activeHealthRegen) {
        ActiveHealthRegen = activeHealthRegen;
    }
    public double getActiveManaRegen() {
        return ActiveManaRegen;
    }
    public void setActiveManaRegen(double activeManaRegen) {
        ActiveManaRegen = activeManaRegen;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public boolean isCastingSpell() {
        return castingSpell;
    }
    public void setCastingSpell(boolean castingSpell) {
        this.castingSpell = castingSpell;
    }
    public void AddEquipmentStats(PlayerEquipment e) {
        equipment.setDamage(equipment.getDamage() + e.getDamage());
        equipment.setDefense(equipment.getDefense() + e.getDefense());
        equipment.setCritChance(equipment.getCritChance() + e.getCritChance());
        equipment.setMaxHealth(equipment.getMaxHealth() + e.getMaxHealth());
        equipment.setMaxMana(equipment.getMaxMana() + e.getMaxMana());
        equipment.setMaxStamina(equipment.getMaxStamina() + e.getMaxStamina());
        equipment.setHealthRegen(equipment.getHealthRegen() + e.getHealthRegen());
        equipment.setStaminaRegen(equipment.getStaminaRegen() + e.getStaminaRegen());
        equipment.setManaRegen(equipment.getManaRegen() + e.getManaRegen());
        equipment.setMultiHit(equipment.getMultiHit() + e.getMultiHit());
        equipment.setMovementSpeed(equipment.getMovementSpeed() + e.getMovementSpeed());
        equipment.setCritDamageMult(equipment.getCritDamageMult() + e.getCritDamageMult());
        equipment.setStaminaCost(equipment.getStaminaCost() + e.getStaminaCost());
    }
    public PlayerEquipment getEquipment() {
        return equipment;
    }
    public void setEquipment(PlayerEquipment equipment) {
        this.equipment = equipment;
    }
    public void updateEquipment(Player p) {

        equipment = new PlayerEquipment(0, Material.AIR, "");
        PlayerEquipment TempEquipment;

        //updates this playerStats object with the armor of the inputted player (the player in question)
        PlayerInventory inventory = p.getInventory();
        ItemStack MainHand = inventory.getItemInMainHand();
        ItemStack OffHand = inventory.getItemInOffHand();
        ItemStack Helmet = inventory.getHelmet();
        ItemStack Chestplate = inventory.getChestplate();
        ItemStack Leggings = inventory.getLeggings();
        ItemStack Boots = inventory.getBoots();


        ItemStack[] AllItems = {MainHand, OffHand, Helmet, Chestplate, Leggings, Boots};

        for (int i = 0; i < AllItems.length; i++)
        {
            if (AllItems[i] != null && AllItems[i].getType() != Material.AIR) {
                TempEquipment = PlayerEquipment.ItemToEquipment(AllItems[i]);
                if (AllItems[i] == OffHand)
                    {TempEquipment.setDamage(0.0);
                    TempEquipment.setStaminaCost(0.0);}
                TempEquipment.applyRunes();
                AddEquipmentStats(TempEquipment);
            }
        }
    }
    public void addResilience(int addedresilience){Resilience+=addedresilience;}
    public int getIntelligence() {
        return Intelligence;
    }
    public void setIntelligence(int mana) {
        Intelligence = mana;
    }
    public void addIntelligence(int addedintelligence){Intelligence+=addedintelligence;}
    public int getStrength() {return Strength;}
    public void setStrength(int strength) {Strength = strength;}
    public void addStrength(int addedstrength) {Strength += addedstrength;}
    public int getSkillPoints() {return SkillPoints;}
    public void setSkillPoints(int skillPoints) {SkillPoints = skillPoints;}
    public void addSkillPoints(int addedskillpoints) {SkillPoints += addedskillpoints;}
    public int getDexterity() {return Dexterity;}
    public void setDexterity(int dexterity) {Dexterity = dexterity;}
    public void addDexterity(int addeddexterity) {Dexterity += addeddexterity;}
    public int getWisdom() {return Wisdom;}
    public void setWisdom(int wisdom) {Wisdom = wisdom;}
    public void addWisdom(int addedwisdom) {Wisdom += addedwisdom;}
    public double getActiveCurrentStamina() {
        return ActiveCurrentStamina;
    }
    public void setActiveCurrentStamina(double activeCurrentStamina) {
        ActiveCurrentStamina = activeCurrentStamina;
    }
    public void useStamina(double amount) {
        //this function will have an equipment parameter as input
        setActiveCurrentStamina(getActiveCurrentStamina() - amount);
    }
    public void spendMana(double amount) {
        setActiveCurrentMana(getActiveCurrentMana() - amount);
    }
    public void useMana(double amount){
        setActiveCurrentMana(getActiveCurrentMana() - amount);
    }
    public double getActiveMaxStamina() {
        return ActiveMaxStamina;
    }
    public void setActiveMaxStamina(double activeMaxStamina) {
        ActiveMaxStamina = activeMaxStamina;
    }
    public int getExperience() {
        return Experience;
    }
    public void setExperience(int experience) {
        Experience = experience;
    }
    public void gainExperience(int exp) {
        //value subject to change
        int Levelcap = (int) (1000 + (this.Level * 150) + (Math.floor((this.Level / 10)) * 200) + (Math.floor((this.Level / 25)) * 500));
        this.Experience += exp;

        if (this.Experience >= Levelcap)
        {
            //Make this a while loop so the player can level up multiple times in one go if necessary
            while (this.Experience >= Levelcap) {
                this.Experience -= Levelcap;
                this.Level += 1;
                Levelcap = (int) (1000 + (this.Level * 150) + (Math.floor((this.Level / 10)) * 200) + (Math.floor((this.Level / 25)) * 500));
                this.SkillPoints += 1;
            }
            getPlayer().chat("You Leveled up to level " + this.Level + "!");
        }
    }

    //Stat calculator for each stat
    public void recalculateMaxHealth() {
        setActiveMaxHealth(100 + (equipment.getMaxHealth() * (1 + (0.01 * getVitality()))) + (getVitality() - 1) * 5);
    }//Vitality
    public void recalculateHealth() {
        if (getActiveCurrentHealth() < getActiveMaxHealth()) {
            setActiveCurrentHealth(getActiveCurrentHealth() + ActiveHealthRegen + equipment.getHealthRegen());
        }
        else
        {
            setActiveCurrentHealth(getActiveMaxHealth());
        }
    }
    public void recalculateDamage() {
        setActiveDamage(getStrength() + (equipment.getDamage() * (1 + (0.01 * getStrength()))));
    }//Strength
    public void recalculateCritDamageMult(){
        //[(1.5 +(0.01 * getStrength())] * [1 + equip.getcritdmgmult()]
        setCritDamageMult(1.5 + ((0.01 * getStrength()) * equipment.getCritDamageMult()));
    }//Strength
    public void recalculateDefense() {
        setActiveDefense((getResilience() * 5) + equipment.getDefense());
    }//Resilience
    public void recalculateMaxMana() {
        setActiveMaxMana((20 + (getIntelligence() - 1) * 8) + equipment.getMaxMana());
    }//Intelligence
    public void recalculateMovementSpeed(Player p){
        setMovementSpeed(0.1 + (0.001 * getDexterity()) + equipment.getMovementSpeed());
        p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(getMovementSpeed());
    }//Dexterity
    public void recalculateMana() {
        if (getActiveCurrentMana() < getActiveMaxMana()) {
            setActiveCurrentMana(getActiveCurrentMana() + ActiveManaRegen + equipment.getManaRegen());
        }
        else
        {
            setActiveCurrentMana(getActiveMaxMana());
        }
    }
    public void recalculateStamina() {
        if (getActiveCurrentStamina() < getActiveMaxStamina()) {
            setActiveCurrentStamina(getActiveCurrentStamina() + ActiveStaminaRegen + equipment.getStaminaRegen());
        }
        else
        {
            setActiveCurrentStamina(getActiveMaxStamina());
        }
    }
    public void recalculateStaminaCost() {
        setStaminaCost(1.0 + equipment.getStaminaCost());
    }
    public void recalculateMaxStamina() {
        //this function will have a player equipment input eventually to add-on to the calculations
        setActiveMaxStamina(20.0 + equipment.getMaxStamina());
    }//None
    public void recalculateMultihit(){
        setMultihit((0.01 * getDexterity()) + equipment.getMultiHit());
    } //Dexterity
    public void recalculateCritChance(){
        setCritChance(0.01 + equipment.getCritChance());
    } //None

    //Wisdom will add %dmg increase towards all spells

    public void damage(MobStats mobstats) {


        ActiveCurrentHealth -= mobstats.getAttack() - (mobstats.getAttack() * (getActiveDefense() / (getActiveDefense() + 100)));

    }

    public void respawnStatReset() {
        setActiveCurrentHealth(getActiveMaxHealth());
        setActiveCurrentStamina(getActiveMaxStamina());
        setActiveCurrentMana(getActiveMaxMana());
    }

    public void heal(double amount) {
        ActiveCurrentHealth += amount;
        //if more than max, set to max
    }

    public void KillReward(LivingEntity mob)
    {

    }


    public String toString()
    {
        //outdated, some stats missing
        return "Constitution: " + this.Vitality + "\n" +
                "Resilience: " + this.Resilience + "\n" +
                "Intelligence: " + this.Intelligence + "\n" +
                "Strength: " + this.Strength + "\n" +
                "Dexterity: " + this.Dexterity + "\n" +
                "Wisdom: " + this.Wisdom + "\n" +
                "Maximum Health: " + this.ActiveMaxHealth + "\n" +
                "Armor: " + this.ActiveDefense + "\n" +
                "Mana Pool: " + this.ActiveMaxMana + "\n" +
                "Level: " + this.Level + "\n" +
                "Unused Skill points: " + this.SkillPoints;
    }


}
