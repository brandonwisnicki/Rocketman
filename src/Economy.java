import java.util.Random;

public class Economy {

    private float averageIronValue;
    private float averageSilverValue;
    private float averageGoldValue;

    private static final int IRON_VALUE_STANDARD_DEVIATION = 20;
    private static final int SILVER_VALUE_STANDARD_DEVIATION = 50;
    private static final int GOLD_VALUE_STANDARD_DEVIATION = 100;

    private float averageIronSupply;
    private float averageSilverSupply;
    private float averageGoldSupply;
    
    private static final int IRON_AMOUNT_STANDARD_DEVIATION = 100;
    private static final int SILVER_AMOUNT_STANDARD_DEVIATION = 50;
    private static final int GOLD_AMOUNT_STANDARD_DEVIATION = 25;
    
    private Random rng;

    public Economy() {

        /*
         * Randomly generates the galactic averages of all three items. Imagine a number
         * line 0.0 through 1.0 We divide the number line into three sections of each
         * random length Such that all sections add up to 1.0 The lengths of each
         * section help determine the variance of each item's average This method is
         * used to help balance out averages i.e. make sure that not all three items
         * have an upper variance which could skew that game towards the easier side
         */

        rng = new Random();

        float ironRandom = rng.nextFloat();
        averageIronValue =  (ironRandom * 10.0f) + 50;

        float silverRandom = (rng.nextFloat() * (1 - ironRandom)) + ironRandom;
        averageSilverValue =  (silverRandom * 50.0f) + 175;

        float goldRandom = 1 - silverRandom - ironRandom;
        averageGoldValue = (goldRandom * 100.0f) + 350;
        
        
        /*
         * These are the average galactic supplies of each item
         * They are based on the complement of the random value
         * This is to help balance out the economy a bit
         * The std. dev. is moreso variance, but it is also used for std. dev.
         */
        averageIronSupply = (1 - ironRandom) * IRON_AMOUNT_STANDARD_DEVIATION + 400;
        averageSilverSupply = (1 - silverRandom) * SILVER_AMOUNT_STANDARD_DEVIATION + 125;
        averageGoldSupply = (1 - goldRandom) * GOLD_AMOUNT_STANDARD_DEVIATION + 50;
    }



    public int getIronPrice() {
        return (int) (rng.nextGaussian() * IRON_VALUE_STANDARD_DEVIATION + averageIronValue);
    }
    
    public int getSilverPrice() {
        return (int) (rng.nextGaussian() * SILVER_VALUE_STANDARD_DEVIATION + averageSilverValue);
    }
    
    public int getGoldPrice() {
        return (int) (rng.nextGaussian() * GOLD_VALUE_STANDARD_DEVIATION + averageGoldValue);
    }
    
    public int getIronSupply() {
        return (int) (rng.nextGaussian() * IRON_AMOUNT_STANDARD_DEVIATION + averageIronSupply);
    }
    
    public int getSilverSupply() {
        return (int) (rng.nextGaussian() * SILVER_AMOUNT_STANDARD_DEVIATION + averageSilverSupply);
    }
    
    public int getGoldSupply() {
        return (int) (rng.nextGaussian() * GOLD_AMOUNT_STANDARD_DEVIATION + averageGoldSupply);
    }
    
    
 

}
