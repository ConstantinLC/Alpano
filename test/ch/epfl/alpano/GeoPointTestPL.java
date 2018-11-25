package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

public class GeoPointTestPL {
    private final static GeoPoint lausanne = new GeoPoint (Math.toRadians(6.631), Math.toRadians(46.521));
    private final static GeoPoint Moscou = new GeoPoint(Math.toRadians(37.623), Math.toRadians(55.753));
    
    @Test (expected = IllegalArgumentException.class)
    public void GeoPointLatitudeException(){
        new GeoPoint(0,Math.PI);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void GeoPointLongitudeException(){
        new GeoPoint(Math2.PI2,0);
    }
    
    @Test
    public void distanceLausanneMoscou(){
        double distance = lausanne.distanceTo(Moscou);
        double expectedDist = 0.37157*Distance.EARTH_RADIUS;
        System.out.println("distance calculated " + distance);
        System.out.println("distance excpected "+ expectedDist);
        System.out.println("");
        assertEquals(distance, expectedDist, 1000);
    }
    
    @Test
    public void azimuthToLausanneMoscou(){
        double azimuth = lausanne.azimuthTo(Moscou);
        double expectedAzi = Math.toRadians(52.95);
        System.out.println("azimuth calculated " + azimuth);
        System.out.println("azimuth excpected "+ expectedAzi);
        assertEquals (azimuth, expectedAzi, 1e-4);
    }

}
