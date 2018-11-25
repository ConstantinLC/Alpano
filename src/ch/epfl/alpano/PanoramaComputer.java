package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Distance.EARTH_RADIUS;
import java.util.function.DoubleUnaryOperator;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import static ch.epfl.alpano.Math2.*;
import static java.lang.Math.*;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 * Représente un calculateur de panorama.
 */
public final class PanoramaComputer {

    /**
     * Coefficient de réfraction de Gauss
     */
    public final static double GAUSS_CONSTANT = 0.13;
    private final static double CALCULATION_CONSTANT = (1 - GAUSS_CONSTANT) / (2 * EARTH_RADIUS);
    public final static int INTERVAL = 64;
    private final static int IMPROVED_INTERVAL = 4;

    private final ContinuousElevationModel dem;

    /**
     * Construit un calculateur de panorama obtenant les données du MNT continu
     * passé en argument, ou lève l'exception NullPointerException s'il est nul.
     * 
     * @param dem
     * @throws NullPointerException
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
        this.dem = requireNonNull(dem);
    }

    /**
     * Calcule et retourne le panorama spécifié par les paramètres.
     * 
     * @param parameters
     * @return panorama spécifié par les paramètres
     */
    public Panorama computePanorama(PanoramaParameters parameters) {
        Panorama.Builder builder = new Panorama.Builder(parameters);

        for (int i = 0; i < parameters.width(); ++i) {

            ElevationProfile profile = new ElevationProfile(dem, parameters.observerPosition(),
                    parameters.azimuthForX(i), parameters.maxDistance());
            double currentX = 0;

            for (int j = parameters.height() - 1; j >= 0; --j) {

                double altitude = parameters.altitudeForY(j);
                
                DoubleUnaryOperator f = rayToGroundDistance(profile, parameters.observerElevation(),
                        tan(altitude));
                double firstInterval = firstIntervalContainingRoot(f, currentX, parameters.maxDistance(), INTERVAL);
                double lowerBound = Double.POSITIVE_INFINITY;

                if (firstInterval != Double.POSITIVE_INFINITY) {
                    lowerBound = improveRoot(f, firstInterval, firstInterval + INTERVAL, IMPROVED_INTERVAL);
                }
                
                if(lowerBound == Double.POSITIVE_INFINITY)
                    break;
                
                GeoPoint geopoint = profile.positionAt(lowerBound);
                
                float distance = (float) (lowerBound / cos(altitude));
                float longitude = (float) geopoint.longitude();
                float latitude = (float) geopoint.latitude();
                float elevation = (float) dem.elevationAt(geopoint);
                float slope = (float) dem.slopeAt(geopoint);

                builder.setDistanceAt(i, j, distance).setElevationAt(i, j, elevation).setLongitudeAt(i, j, longitude)
                        .setLatitudeAt(i, j, latitude).setSlopeAt(i, j, slope);

                currentX = lowerBound;
            }
        }
        return builder.build();
    }

    /**
     * Retourne la fonction donnant la distance entre un rayon d'altitude
     * initiale ray0 et de pente de raySlope, et le profil altimétrique profile.
     * 
     * @param profile
     * @param ray0
     * @param raySlope
     * @return la fonction donnant la distance entre un rayon d'altitude
     *         initiale ray0 et de pente de raySlope, et le profil 
     *         altimétrique profile
     */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {
        return x -> ray0 + x * raySlope - profile.elevationAt(x) + CALCULATION_CONSTANT * Math2.sq(x);
    }

}
