
public class NameGenerator {

    private static String[] baseNames = { "Centauri", "Lacaille", "Struve", "Alzoc",
                                          "Tauceti", "Kapteyn", "Eridani", "Pictoris", 
                                          "Librae", "Kepler", "Aduba", "Canis",
                                          "Fornax", "Lyra", "Brandon", "Crait", 
                                          "Concord", "Eriadu", "Esselles" };

    public static String generatePlanetarySystemName() {

        String base = baseNames[(int) (Math.random() * baseNames.length)];

        int number = (int) (Math.random() * 1000);

        return base + "-" + number;

    }

}
