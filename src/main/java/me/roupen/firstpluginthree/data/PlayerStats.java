package me.roupen.firstpluginthree.data;

import org.bukkit.entity.Player;

public class PlayerStats {
    //Permanent Stats

    //Adds to max health
    private int Vitality;

    //Adds to defense
    private int Resilience;

    //Increases magic damage
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
    private double ActiveCurrentHealth;
    private double ActiveMaxHealth;
    private double ActiveDefense;
    private double ActiveCurrentMana;
    private double ActiveMaxMana;

    //getter and setter and adder functions for all statistics
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

    public int getExperience() {
        return Experience;
    }

    public void setExperience(int experience) {
        Experience = experience;
    }

    public void gainExperience(int exp, Player p)
    {
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
            p.chat("You Leveled up to level " + this.Level + "!");
        }
        else
        {
            p.chat("Gained " + exp + " combat EXP " + "(" + this.Experience + "/" + Levelcap + ")");
        }
    }

    //Stat calculator for each stat
    public void recalculateMaxHealth()
    {
        setActiveMaxHealth(100 + (getVitality() - 1) * 5);
    }

    public void damage(double amount) {
        ActiveCurrentHealth -= amount;
        //if 0 or less, trigger death
    }

    public void heal(double amount) {
        ActiveCurrentHealth += amount;
        //if more than max, set to max
    }


    public String toString()
    {
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
