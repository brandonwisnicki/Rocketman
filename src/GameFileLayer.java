import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class GameFileLayer {

    BufferedReader reader;

    public String getTextFromFile(String saveFilePath) {
        String data = "";

        try {
            if (saveFilePath == null) {
                System.out.println("No save file path inputted");
                return null;
            }
            reader = new BufferedReader(new FileReader(saveFilePath));

            String c = reader.readLine();
            while (c != null) {
                data += c;
                c = reader.readLine();
            }

        } catch (IOException e) {
            System.out.println("I/O Exception while reading file");
        }

        return data;

    }

    public void loadTextToGameData(String text, GameCourt court) {

        // Splits the player and galaxy data apart
        String[] tagSeperators = text.split("#");

        
        
        String[] playerData = tagSeperators[0].split(";");
        String[] galaxyData = tagSeperators[1].split(">");

        float health = 25.0f, maxHealth = 25.0f, fuel = 10.0f, maxFuel = 10.0f, power = 3.0f,
                maxPower = 3.0f;
        int credits = 500, x = 0, y = 0, playerIron = 0, playerSilver = 0, playerGold = 0;

        for (String section : playerData) {
            String[] parsedSection = section.split(":");
            switch (parsedSection[0]) {
                case "HEALTH":
                    health = Float.parseFloat(parsedSection[1]);
                    break;
                case "MAXHEALTH":
                    maxHealth = Float.parseFloat(parsedSection[1]);
                    break;
                case "FUEL":
                    fuel = Float.parseFloat(parsedSection[1]);
                    break;
                case "MAXFUEL":
                    maxFuel = Float.parseFloat(parsedSection[1]);
                    break;
                case "POWER":
                    power = Float.parseFloat(parsedSection[1]);
                    break;
                case "MAXPOWER":
                    maxPower = Float.parseFloat(parsedSection[1]);
                    break;
                case "CREDITS":
                    credits = Integer.parseInt(parsedSection[1]);
                    break;
                case "X":
                    x = Integer.parseInt(parsedSection[1]);
                    break;
                case "Y":
                    y = Integer.parseInt(parsedSection[1]);
                    break;
                case "IRON":
                    playerIron = Integer.parseInt(parsedSection[1]);
                    break;
                case "SILVER":
                    playerSilver = Integer.parseInt(parsedSection[1]);
                    break;
                case "GOLD":
                    playerGold = Integer.parseInt(parsedSection[1]);
                    break;
                default:
                    System.out.println("Unknown attribute " + parsedSection[0]);
                    break;
            }
        }

        Ship loadedShip = new Ship(court, health, maxHealth, fuel, maxFuel, power, maxPower,
                credits, playerIron, playerSilver, playerGold);

        PlanetarySystem[][] loadedGalaxy = new PlanetarySystem[10][10];
        for (int i = 0; i < galaxyData.length; i++) {
            String[] systemData = galaxyData[i].split(";");

            int systemXCoord = i % 10;
            int systemYCoord = i / 10;

            String name = "", type = "";
            boolean discovered = false;
            int ironSupply = 0, silverSupply = 0, goldSupply = 0, repairCost = 0, refuelCost = 0,
                    hullUpgradeCredit = 0, hullUpgradeIron = 0, fuelUpgradeCredit = 0,
                    fuelUpgradeIron = 0, powerUpgradeCredit = 0, powerUpgradeGold = 0,
                    shopCredits = 0, ironValue = 0, silverValue = 0, goldValue = 0;

            for (String section : systemData) {
                String[] parsedSection = section.split(":");
                switch (parsedSection[0]) {
                    case "NAME":
                        name = parsedSection[1];
                        break;
                    case "DISCOVERED":
                        discovered = Boolean.parseBoolean(parsedSection[1]);
                        break;
                    case "TYPE":
                        type = parsedSection[1];
                        break;
                    case "IRONSUPPLY":
                        ironSupply = Integer.parseInt(parsedSection[1]);
                        break;
                    case "SILVERSUPPLY":
                        silverSupply = Integer.parseInt(parsedSection[1]);
                        break;
                    case "GOLDSUPPLY":
                        goldSupply = Integer.parseInt(parsedSection[1]);
                        break;
                    case "IRONVALUE":
                        ironValue = Integer.parseInt(parsedSection[1]);
                        break;
                    case "SILVERVALUE":
                        silverValue = Integer.parseInt(parsedSection[1]);
                        break;
                    case "GOLDVALUE":
                        goldValue = Integer.parseInt(parsedSection[1]);
                        break;
                    case "REPAIRCOST":
                        repairCost = Integer.parseInt(parsedSection[1]);
                        break;
                    case "REFUELCOST":
                        refuelCost = Integer.parseInt(parsedSection[1]);
                        break;
                    case "HULLUPGRADECREDIT":
                        hullUpgradeCredit = Integer.parseInt(parsedSection[1]);
                        break;
                    case "HULLUPGRADEIRON":
                        hullUpgradeIron = Integer.parseInt(parsedSection[1]);
                        break;
                    case "FUELUPGRADECREDIT":
                        fuelUpgradeCredit = Integer.parseInt(parsedSection[1]);
                        break;
                    case "FUELUPGRADEIRON":
                        fuelUpgradeIron = Integer.parseInt(parsedSection[1]);
                        break;
                    case "POWERUPGRADECREDIT":
                        powerUpgradeCredit = Integer.parseInt(parsedSection[1]);
                        break;
                    case "POWERUPGRADEGOLD":
                        powerUpgradeGold = Integer.parseInt(parsedSection[1]);
                        break;
                    case "CREDITS":
                        shopCredits = Integer.parseInt(parsedSection[1]);
                        break;
                    default:
                        System.out.println("Unknown attribute " + parsedSection[0]);
                        break;
                }
            }

            switch (type) {
                case "EMPTY":
                    loadedGalaxy[systemYCoord][systemXCoord] = new PlanetarySystem(name,discovered,
                            court);
                    break;
                case "NEBULA":
                    loadedGalaxy[systemYCoord][systemXCoord] = new NebulaSystem(name,discovered,
                            court);
                    break;
                case "ASTEROID":
                    loadedGalaxy[systemYCoord][systemXCoord] = new AsteroidBeltSystem(name, 
                            discovered, court);
                    break;
                case "OUTPOST":
                    loadedGalaxy[systemYCoord][systemXCoord] = new OutpostSystem(name, court, 
                            ironValue,
                            silverValue, goldValue, ironSupply, silverSupply, goldSupply,
                            shopCredits, repairCost, refuelCost, hullUpgradeCredit, hullUpgradeIron,
                            fuelUpgradeCredit, fuelUpgradeIron, powerUpgradeCredit,
                            powerUpgradeGold, discovered);
                    break;
                default:
                    break;
            }

        }

        court.load(loadedShip, loadedGalaxy, x, y);
    }

    public String gameDataToText(PlanetarySystem[][] galaxy, int systemCoordX, int systemCoordY,
            Ship player) {
        String data = "";

        // Player Data
        data += "HEALTH:" + player.getCurrentHealth();
        data += ";MAXHEALTH:" + player.getMaxHealth();
        data += ";FUEL:" + player.getFuel();
        data += ";MAXFUEL:" + player.getMaxFuel();
        data += ";POWER:" + player.getPower();
        data += ";MAXPOWER:" + player.getMaxPower();
        data += ";CREDITS:" + player.getCredits();
        data += ";X:" + systemCoordX;
        data += ";Y:" + systemCoordY;

        for (Item item : player.getInventory()) {
            data += ";" + item.getTechnicalName() + ":" + item.getAmount();
        }

        // Galaxy Data
        data += "#";

        for (PlanetarySystem[] row : galaxy) {
            for (PlanetarySystem sys : row) {

                if (sys instanceof NebulaSystem) {

                    data += "NAME:" + sys.getName();
                    data += ";DISCOVERED:" + sys.isDiscovered();
                    data += ";TYPE:NEBULA";
                } else if (sys instanceof AsteroidBeltSystem) {
                    data += "NAME:" + sys.getName();
                    data += ";DISCOVERED:" + sys.isDiscovered();
                    data += ";TYPE:ASTEROID";
                } else if (sys instanceof OutpostSystem) {

                    OutpostSystem outpost = (OutpostSystem) sys;
                    Shop shop = outpost.getShop();

                    data += "NAME:" + outpost.getName();
                    data += ";DISCOVERED:" + outpost.isDiscovered();
                    data += ";TYPE:OUTPOST";
                    data += ";IRONSUPPLY:" + shop.getIronSupply();
                    data += ";SILVERSUPPLY:" + shop.getSilverSupply();
                    data += ";GOLDSUPPLY:" + shop.getGoldSupply();
                    data += ";IRONVALUE:" + shop.getIronValue();
                    data += ";SILVERVALUE:" + shop.getSilverValue();
                    data += ";GOLDVALUE:" + shop.getGoldValue();

                    data += ";REPAIRCOST:" + shop.getCostPerHP();
                    data += ";REFUELCOST:" + shop.getCostPerGallon();
                    data += ";HULLUPGRADECREDIT:" + shop.getHullUpgradeCreditCost();
                    data += ";HULLUPGRADEIRON:" + shop.getHullUpgradeIronCost();
                    data += ";FUELUPGRADECREDIT:" + shop.getFuelUpgradeCreditCost();
                    data += ";FUELUPGRADEIRON:" + shop.getFuelUpgradeIronCost();
                    data += ";POWERUPGRADECREDIT:" + shop.getPowerUpgradeCreditCost();
                    data += ";POWERUPGRADEGOLD:" + shop.getPowerUpgradeGoldCost();
                    data += ";CREDITS:" + shop.getCredits();
                } else {
                    // Empty System
                    data += "NAME:" + sys.getName();
                    data += ";DISCOVERED:" + sys.isDiscovered();
                    data += ";TYPE:EMPTY";
                }

                data += ">";
            }
        }

        return data.substring(0, data.length() - 1);
    }

    public void saveGameDataToFile(String text, String saveFilePath) {
        File file = Paths.get(saveFilePath).toFile();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            bw.append(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
