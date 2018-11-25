package ch.epfl.alpano;

/**
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 *
 * Permet de convertir des distances à la surface de la Terre de radians
 * en mètres, et inversément.
 */
public interface Distance {

    /**
     * Rayon de la Terre en mètres.
     */
    public final static double EARTH_RADIUS = 6371000;

    /**
     * Convertit une distance à la surface de la Terre exprimée en mètres en
     * l'angle correspondant, en radians.
     * 
     * @param distanceInMeters
     * @return distance (en radians)
     */
    public static double toRadians(double distanceInMeters) {
        return distanceInMeters / EARTH_RADIUS;
    }

    /**
     * Convertit un angle en radians en la distance correspondant à la surface
     * de la Terre, en mètres.
     * 
     * @param distanceInRadians
     * @return distance (en mètres)
     */
    public static double toMeters(double distanceInRadians) {
        return distanceInRadians * EARTH_RADIUS;
    }
}
