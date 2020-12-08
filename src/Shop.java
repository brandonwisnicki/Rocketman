import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Shop extends GameObject {

    private static final String SHOP_FILE = "files/shop.png";
    private static BufferedImage shopUnderlay;

    private int ironSupply;

    private int goldSupply;
    private int silverSupply;

    private int ironValue;
    private int goldValue;
    private int silverValue;

    private int costPerHP;
    private int costPerGallon;

    private int hullUpgradeCreditCost;
    private int hullUpgradeIronCost;

    private int fuelUpgradeCreditCost;
    private int fuelUpgradeIronCost;

    private int powerUpgradeCreditCost;
    private int powerUpgradeGoldCost;

    private int credits;

    private GameCourt court;

    public Shop(GameCourt court) {
        super(0, 0, court.getWidth(), court.getHeight());
        this.court = court;
        try {
            if (shopUnderlay == null) {
                shopUnderlay = ImageIO.read(new File(SHOP_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        ironValue = court.getEconomy().getIronPrice();
        silverValue = court.getEconomy().getSilverPrice();
        goldValue = court.getEconomy().getGoldPrice();

        ironSupply = court.getEconomy().getIronSupply();
        silverSupply = court.getEconomy().getSilverSupply();
        goldSupply = court.getEconomy().getGoldSupply();

        credits = (int) (Math.random() * 9000 + 1000);

        costPerHP = (int) (275 + (Math.random() * 50));
        costPerGallon = (int) (155 + (Math.random() * 70));

        hullUpgradeCreditCost = (int) (2000 + (Math.random() * 5000));
        hullUpgradeIronCost = (int) (40 + (20 * Math.random()));

        fuelUpgradeCreditCost = (int) (3000 + (Math.random() * 5000));
        fuelUpgradeIronCost = (int) (25 + (15 * Math.random()));

        powerUpgradeCreditCost = (int) (4000 + (Math.random() * 5000));
        powerUpgradeGoldCost = (int) (10 + (10 * Math.random()));

    }

    public Shop(GameCourt court, int ironValue, int silverValue, int goldValue, int ironSupply,
            int silverSupply, int goldSupply, int credits, int costPerHP, int costPerGallon,
            int hullUpgradeCreditCost, int hullUpgradeIronCost, int fuelUpgradeCreditCost,
            int fuelUpgradeIronCost, int powerUpgradeCreditCost, int powerUpgradeGoldCost) {
        super(0, 0, court.getWidth(), court.getHeight());
        this.court = court;
        try {
            if (shopUnderlay == null) {
                shopUnderlay = ImageIO.read(new File(SHOP_FILE));
            }

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        this.ironValue = ironValue;
        this.silverValue = silverValue;
        this.goldValue = goldValue;

        this.ironSupply = ironSupply;
        this.silverSupply = silverSupply;
        this.goldSupply = goldSupply;

        this.credits = credits;

        this.costPerHP = costPerHP;
        this.costPerGallon = costPerGallon;

        this.hullUpgradeCreditCost = hullUpgradeCreditCost;
        this.hullUpgradeIronCost = hullUpgradeIronCost;

        this.fuelUpgradeCreditCost = fuelUpgradeCreditCost;
        this.fuelUpgradeIronCost = fuelUpgradeIronCost;

        this.powerUpgradeCreditCost = powerUpgradeCreditCost;
        this.powerUpgradeGoldCost = powerUpgradeGoldCost;

    }

    public int getMaxIronSellPrice() {
        Item invIron = this.court.getPlayerShip().getItem("Iron Ore");
        if (invIron == null) {
            return 0;
        }
        return Math.min(invIron.getAmount(), ironSupply) * ironValue;
    }

    public int getMaxSilverSellPrice() {
        Item invSilver = this.court.getPlayerShip().getItem("Silver Ore");
        if (invSilver == null) {
            return 0;
        }
        return Math.min(invSilver.getAmount(), silverSupply) * silverValue;
    }

    public int getMaxGoldSellPrice() {
        Item invGold = this.court.getPlayerShip().getItem("Gold Ore");
        if (invGold == null) {
            return 0;
        }
        return Math.min(invGold.getAmount(), goldSupply) * goldValue;
    }

    public int getRepairPrice() {
        return (int) Math.min(court.getPlayerShip().getCredits(),
                this.costPerHP * (court.getPlayerShip().getMaxHealth()
                        - court.getPlayerShip().getCurrentHealth()));
    }

    public int getRefuelPrice() {
        return (int) Math.min(court.getPlayerShip().getCredits(), this.costPerGallon
                * (court.getPlayerShip().getMaxFuel() - court.getPlayerShip().getFuel()));
    }

    public String buyIron(int amount) {
        amount = Math.min(amount, ironSupply);
        int total = amount * this.ironValue;
        if (amount == 0) {
            return "No more iron in stock";
        }
        if (total > this.court.getPlayerShip().getCredits()) {
            return "You do not have enough credits";
        }
        this.court.getPlayerShip().addCredits(-total);
        addCredits(total);
        this.court.getPlayerShip().addItem(new Iron(amount));
        return "Bought " + amount + " iron for " + total + "¤";
    }

    public String buySilver(int amount) {
        amount = Math.min(amount, silverSupply);
        int total = amount * this.silverValue;
        if (amount == 0) {
            return "No more silver in stock";
        }
        if (total > this.court.getPlayerShip().getCredits()) {
            return "You do not have enough credits";
        }
        this.court.getPlayerShip().addCredits(-total);
        addCredits(total);
        this.court.getPlayerShip().addItem(new Silver(amount));
        return "Bought " + amount + " iron for " + total + "¤";
    }

    public String buyGold(int amount) {
        amount = Math.min(amount, goldSupply);
        int total = amount * this.goldValue;
        if (amount == 0) {
            return "No more gold in stock";
        }
        if (total > this.court.getPlayerShip().getCredits()) {
            return "You do not have enough credits";
        }
        this.court.getPlayerShip().addCredits(-total);
        addCredits(total);
        this.court.getPlayerShip().addItem(new Gold(amount));
        return "Bought " + amount + " gold for " + total + "¤";
    }

    public String sellIron(int amount) {
        Item invItem = this.court.getPlayerShip().getItem("Iron Ore");
        if (invItem == null) {
            return "You have no iron to sell";
        }

        amount = Math.min(amount, invItem.getAmount());
        if (amount == 0) {
            return "You have no iron to sell";
        }
        amount = Math.min(amount, this.credits / this.ironValue);
        if (amount == 0) {
            return "The bazaar hasn't enough credits";
        }
        int total = amount * this.ironValue;
        this.court.getPlayerShip().addCredits(total);
        addCredits(-total);
        this.ironSupply += amount;
        this.court.getPlayerShip().addItem(new Iron(-amount));

        return "Sold " + amount + " iron for " + total + "¤";
    }

    public String sellSilver(int amount) {
        Item invItem = this.court.getPlayerShip().getItem("Silver Ore");
        if (invItem == null) {
            return "You have no silver to sell";
        }

        amount = Math.min(amount, invItem.getAmount());
        if (amount == 0) {
            return "You have no silver to sell";
        }
        amount = Math.min(amount, this.credits / this.silverValue);
        if (amount == 0) {
            return "The bazaar hasn't enough credits";
        }
        int total = amount * this.silverValue;
        this.court.getPlayerShip().addCredits(total);
        addCredits(-total);
        this.silverSupply += amount;
        this.court.getPlayerShip().addItem(new Silver(-amount));
        return "Sold " + amount + " silver for " + total + "¤";
    }

    public String sellGold(int amount) {
        Item invItem = this.court.getPlayerShip().getItem("Gold Ore");
        if (invItem == null) {
            return "You have no gold to sell";
        }
        amount = Math.min(amount, invItem.getAmount());
        if (amount == 0) {
            return "You have no gold to sell";
        }
        amount = Math.min(amount, this.credits / this.goldValue);
        if (amount == 0) {
            return "The bazaar hasn't enough credits";
        }
        int total = amount * this.goldValue;
        this.court.getPlayerShip().addCredits(total);
        addCredits(-total);
        this.goldSupply += amount;
        this.court.getPlayerShip().addItem(new Gold(-amount));
        return "Sold " + amount + " gold for " + total + "¤";
    }

    public String repairShip() {
        float healthPointsToRestore = this.court.getPlayerShip().getMaxHealth()
                - this.court.getPlayerShip().getCurrentHealth();
        float totalCost = healthPointsToRestore * this.costPerHP;
        totalCost = Math.min(totalCost, this.court.getPlayerShip().getCredits());
        if (this.court.getPlayerShip().getCredits() == 0) {
            return "Not enough credits";
        } else if (totalCost == 0) {
            return "No repairs needed";
        }
        healthPointsToRestore = totalCost / this.costPerHP;
        court.getPlayerShip().heal(healthPointsToRestore);
        court.getPlayerShip().addCredits((int) -totalCost);
        addCredits((int) totalCost);
        return "Ship has been repaired";
    }

    public String refuelShip() {
        float gallonsToFill = this.court.getPlayerShip().getMaxFuel()
                - this.court.getPlayerShip().getFuel();
        float totalCost = gallonsToFill * this.costPerGallon;
        totalCost = Math.min(totalCost, this.court.getPlayerShip().getCredits());
        if (this.court.getPlayerShip().getCredits() == 0) {
            return "Not enough credits";
        } else if (totalCost == 0) {
            return "You're already fully fueled";
        }
        gallonsToFill = totalCost / this.costPerGallon;
        court.getPlayerShip().refuel(gallonsToFill);
        court.getPlayerShip().addCredits((int) -totalCost);
        addCredits((int) totalCost);
        return "Ship has been refueled";
    }

    public String upgradeHull() {
        if (court.getPlayerShip().getCredits() < this.hullUpgradeCreditCost) {
            return "Not enough credits";
        }
        if (court.getPlayerShip().getItem("Iron Ore") == null || court.getPlayerShip()
                .getItem("Iron Ore").getAmount() < this.hullUpgradeIronCost) {
            return "Not enough iron";
        }
        this.addCredits(this.hullUpgradeCreditCost);
        this.ironSupply += this.hullUpgradeIronCost;
        court.getPlayerShip().addCredits(-this.hullUpgradeCreditCost);
        this.court.getPlayerShip().addItem(new Iron(-hullUpgradeIronCost));

        court.getPlayerShip().upgradeHull();
        return "Upgraded hull integrity";
    }

    public String upgradeFuelTank() {
        if (court.getPlayerShip().getCredits() < this.fuelUpgradeCreditCost) {
            return "Not enough credits";
        }
        if (court.getPlayerShip().getItem("Iron Ore") == null || court.getPlayerShip()
                .getItem("Iron Ore").getAmount() < this.fuelUpgradeIronCost) {
            return "Not enough iron";
        }
        this.addCredits(this.fuelUpgradeCreditCost);
        this.ironSupply += this.fuelUpgradeIronCost;
        court.getPlayerShip().addCredits(-this.fuelUpgradeCreditCost);
        this.court.getPlayerShip().addItem(new Iron(-fuelUpgradeIronCost));
        court.getPlayerShip().upgradeFuelTank();
        return "Upgraded fuel tank";
    }

    public String upgradePowerSystem() {
        if (court.getPlayerShip().getCredits() < this.powerUpgradeCreditCost) {
            return "Not enough credits";
        }
        if (court.getPlayerShip().getItem("Gold Ore") == null || court.getPlayerShip()
                .getItem("Gold Ore").getAmount() < this.powerUpgradeGoldCost) {
            return "Not enough gold";
        }
        this.addCredits(this.powerUpgradeCreditCost);
        this.goldSupply += this.powerUpgradeGoldCost;
        court.getPlayerShip().addCredits(-this.powerUpgradeCreditCost);
        this.court.getPlayerShip().addItem(new Gold(-powerUpgradeGoldCost));

        court.getPlayerShip().upgradePower();
        return "Upgraded generator systems";
    }

    public void addCredits(int amt) {
        credits += amt;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(shopUnderlay, null, null);

        g.setColor(Color.WHITE);

        g.drawString("Bazaar Credits: " + credits + " ¤", 350, 60);

        // Fix Ship
        g.drawString(this.getRepairPrice() + " ¤", 400, 230);

        // Refuel
        g.drawString(this.getRefuelPrice() + " ¤", 520, 230);

        // Upgrade Hull
        g.drawString(this.hullUpgradeCreditCost + " ¤", 620, 215);
        g.drawString(this.hullUpgradeIronCost + " Iron Ore", 620, 230);

        // Upgrade Fuel Tank
        g.drawString(this.fuelUpgradeCreditCost + " ¤", 725, 215);
        g.drawString(this.fuelUpgradeIronCost + " Iron Ore", 725, 230);

        // Upgrade Power
        g.drawString(this.powerUpgradeCreditCost + " ¤", 850, 215);
        g.drawString(this.powerUpgradeGoldCost + " Gold Ore", 850, 230);

        // Iron Ore
        g.drawString((ironValue * Math.min(10, ironSupply)) + " ¤", 423, 438);
        g.drawString(ironValue + " ¤", 423, 454);
        g.drawString(getMaxIronSellPrice() + " ¤", 440, 470);
        // Silver Ore

        g.drawString((silverValue * Math.min(10, silverSupply)) + " ¤", 560, 438);
        g.drawString(silverValue + " ¤", 560, 454);
        g.drawString(getMaxSilverSellPrice() + " ¤", 574, 470);

        // Gold Ore

        g.drawString((goldValue * Math.min(10, goldSupply)) + " ¤", 695, 438);
        g.drawString(goldValue + " ¤", 695, 454);
        g.drawString(getMaxGoldSellPrice() + " ¤", 710, 470);

        // Sell All

        g.drawString(
                (getMaxGoldSellPrice() + getMaxSilverSellPrice() + getMaxIronSellPrice()) + " ¤",
                827, 449);

    }

    public int getIronSupply() {
        return ironSupply;
    }

    public int getGoldSupply() {
        return goldSupply;
    }

    public int getSilverSupply() {
        return silverSupply;
    }

    public int getIronValue() {
        return ironValue;
    }

    public int getGoldValue() {
        return goldValue;
    }

    public int getSilverValue() {
        return silverValue;
    }

    public int getCostPerHP() {
        return costPerHP;
    }

    public int getCostPerGallon() {
        return costPerGallon;
    }

    public int getHullUpgradeCreditCost() {
        return hullUpgradeCreditCost;
    }

    public int getHullUpgradeIronCost() {
        return hullUpgradeIronCost;
    }

    public int getFuelUpgradeCreditCost() {
        return fuelUpgradeCreditCost;
    }

    public int getFuelUpgradeIronCost() {
        return fuelUpgradeIronCost;
    }

    public int getPowerUpgradeCreditCost() {
        return powerUpgradeCreditCost;
    }

    public int getPowerUpgradeGoldCost() {
        return powerUpgradeGoldCost;
    }

    public int getCredits() {
        return credits;
    }
}
