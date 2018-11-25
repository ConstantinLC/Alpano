package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.floorMod;
import static ch.epfl.alpano.Math2.lerp;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.scalb;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 * Représente un profil altimétrique suivant un arc de grand cercle.
 */
public final class ElevationProfile {

    private final ContinuousElevationModel elevationModel;
    private final double length;
    private final List<GeoPoint> intermediateGeoPoints;
    private final static int SPACEMENT= 4096;
    private final double azimuth;
    private final GeoPoint origin;
    
    /**
     * Construit un profil altimétrique basé sur le MNT donné et 
     * dont le tracé débute au point origin, suit le grand cercle 
     * dans la direction donnée par azimuth, et a une longueur de 
     * length mètres ; lève l'exception IllegalArgumentException si 
     * l'azimuth n'est pas canonique, ou si la longueur n'est pas 
     * strictement positive ; lève l'exception NullPointerException 
     * si l'un des deux autres arguments est null.
     * 
     * @param elevationModel
     * @param origin
     * @param azimuth
     * @param length
     * @throws IllegalArgumentException	
     * @throws NullPointerException
     */
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length){
        checkArgument(Azimuth.isCanonical(azimuth) && length > 0);
        this.elevationModel = requireNonNull(elevationModel);
        this.length = length;
        this.azimuth = azimuth;
        this.origin = requireNonNull(origin);
        this.intermediateGeoPoints = createIntermediateGeoPoints();
    }
    
    /**
     * Retourne l'altitude du terrain à la position donnée du profil, 
     * ou lève l'exception IllegalArgumentException si cette position 
     * n'est pas dans les bornes du profil, c-à-d comprise entre 0 
     * et la longueur du profil.
     * @param x
     * @return l'altitude du terrain à la position donnée du profil
     * @throws IllegalArgumentException
     */
    public double elevationAt(double x){
        checkArgument(x >= 0 && x <= length);
        return elevationModel.elevationAt(positionAt(x));
    }
    
    /**
     * retourne les coordonnées du point à la position donnée du 
     * profil, ou lève l'exception IllegalArgumentException si cette 
     * position n'est pas dans les bornes du profil.
     * @param x
     * @return les coordonnées du point à la position donnée du 
     * profil
     * @throws IllegalArgumentException
     */
    public GeoPoint positionAt(double x){
        checkArgument(x >= 0 && x <= length);
        int lowerBound = (int) Math.floor(x/SPACEMENT);
        int upperBound = (int) Math.floor(x/SPACEMENT)+1;
        double longitude = lerp(intermediateGeoPoints.get(lowerBound).longitude(), intermediateGeoPoints.get(upperBound).longitude(), scalb(x, -12)-lowerBound);
        double latitude = lerp(intermediateGeoPoints.get(lowerBound).latitude(), intermediateGeoPoints.get(upperBound).latitude(), scalb(x, -12)-lowerBound);
        return new GeoPoint(longitude, latitude);
        
    }
    
    /**
     * Retourne la pente du terrain à la position donnée du profil, ou 
     * lève l'exception IllegalArgumentException si cette position 
     * n'est pas dans les bornes du profil.
     * @param x
     * @return la pente du terrain à la position donnée du profil
     * @throws IllegalArgumentException
     */
    public double slopeAt(double x){
        checkArgument(x >= 0 && x <= length);
        return elevationModel.slopeAt(positionAt(x));
    }
    
    private List<GeoPoint> createIntermediateGeoPoints() {
        List<GeoPoint> list = new ArrayList<>();
        
        for(int i = 0; i <= length + SPACEMENT; i+= SPACEMENT){
            list.add(geoPointAt(i));
        }
        
        return list;
    }
    
    private GeoPoint geoPointAt(double x){
        double rad = Distance.toRadians(x);
        double angle = toMath(azimuth);
        double latitude = asin(sin(origin.latitude())*cos(rad) + cos(origin.latitude())*sin(rad)*cos(angle));
        double longitude = floorMod(origin.longitude()-asin((sin(angle)*sin(rad))/cos(latitude))+PI, PI2) - PI;
        return new GeoPoint(longitude, latitude);
    }
}
