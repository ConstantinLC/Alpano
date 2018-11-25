package ch.epfl.alpano;

import static ch.epfl.alpano.Distance.toMeters;
import static ch.epfl.alpano.Math2.haversin;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;
import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.Locale;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 *
 * Représente un point à la surface de la Terre dont la position est
 * donnée dans un système de coordonnées sphériques.
 */
public final class GeoPoint {

    private final double longitude;
    private final double latitude;

    /**
     * Construit le point de coordonnées données (en radians) ou lève
     * l'exception IllegalArgumentException si la longitude n'appartient pas à
     * [−π;π][−π;π] ou si la latitude n'appartient pas à [−π/2;π/2].
     * 
     * @param longitude
     * @param latitude
     * @throws IllegalArgumentException
     */
    public GeoPoint(double longitude, double latitude){
        checkArgument(longitude <= Math.PI && longitude >= -Math.PI && latitude <= Math.PI / 2 && latitude >= -Math.PI / 2);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * retourne la longitude du point, en radians.
     * 
     * @return la longitude du point
     */
    public double longitude() {
        return longitude;
    }

    /**
     * Retourne la latitude du point, en radians.
     * 
     * @return la latitude du point
     */
    public double latitude() {
        return latitude;
    }

    /**
     * Retourne la distance en mètres séparant le récepteur (this) de l'argument
     * (that).
     * 
     * @param that
     * @return la distance en mètres séparant le récepteur (this) de l'argument
     *         (that).
     */
    public double distanceTo(GeoPoint that) {
        double angle = 2
                * asin(Math.sqrt(haversin(this.latitude - that.latitude)
                        + cos(this.latitude) * cos(that.latitude)
                                * haversin(this.longitude - that.longitude)));
        return toMeters(angle);
    }

    /**
     * Retourne l'azimut de l'argument (that) par rapport au récepteur (this).
     * 
     * @param that
     * @return Retourne l'azimut de l'argument (that) par rapport au récepteur
     *         (this).
     */
    public double azimuthTo(GeoPoint that) {
        return Azimuth.fromMath(Azimuth.canonicalize(atan2(
                (sin(this.longitude - that.longitude) * cos(that.latitude)),
                (cos(this.latitude) * sin(that.latitude)
                        - sin(this.latitude) * cos(that.latitude)
                                * cos(this.longitude - that.longitude)))));
    }

    /**
     * Retourne une chaîne composée des coordonnées du point, exprimées en
     * degrés avec exactement 4 décimales, séparées par une virgule et entourées
     * de parenthèses
     * 
     * @return la chaîne composée des coordonnées du point
     */
    @Override
    public String toString() {
        Locale l = null;
        return String.format(l, "(%.4f,%.4f)", toDegrees(this.longitude),
                toDegrees(this.latitude));
    }
}