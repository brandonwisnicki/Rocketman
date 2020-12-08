import java.awt.Graphics;

public class OutpostSystem extends PlanetarySystem {

    private Outpost station;

    public OutpostSystem(String name, GameCourt court) {
        super(name, court);
        station = new Outpost(300, 250, court);
    }

    public OutpostSystem(String name, GameCourt court, int ironValue, int silverValue,
            int goldValue, int ironSupply, int silverSupply, int goldSupply, int credits,
            int costPerHP, int costPerGallon, int hullUpgradeCreditCost, int hullUpgradeIronCost,
            int fuelUpgradeCreditCost, int fuelUpgradeIronCost, int powerUpgradeCreditCost,
            int powerUpgradeGoldCost, boolean discovered) {
        super(name, court);
        station = new Outpost(300, 250, court, ironValue, silverValue, goldValue, ironSupply,
                silverSupply, goldSupply, credits, costPerHP, costPerGallon, hullUpgradeCreditCost,
                hullUpgradeIronCost, fuelUpgradeCreditCost, fuelUpgradeIronCost,
                powerUpgradeCreditCost, powerUpgradeGoldCost);
        if (discovered) {
            this.silentDiscovery();
        }
    }

    public void setupScene() {
        super.setupScene();

        this.getEntities().add(station);
    }

    public Shop getShop() {
        return station.getShop();
    }

    public Outpost getOutpost() {
        return station;
    }

    @Override
    public void drawOverlay(Graphics g) {
        return;
    }

}
