package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeoPointTestEC {

    @Test
    public void distanceWorksForRandomValues(){
        GeoPoint g1 = new GeoPoint(0.1157327827,0.8119446213);
        GeoPoint g2 = new GeoPoint(0.6566452245,0.9730734179);
        assertEquals(2370000, g1.distanceTo(g2), 3000);
    }
    
    @Test
    public void RightToAzimuthWorksForRandomValues(){
        GeoPoint g1 = new GeoPoint(0.1157327827,0.8119446213);
        GeoPoint g2 = new GeoPoint(0.6566452245,0.9730734179);
        assertEquals(Math.toRadians(52.95), g1.azimuthTo(g2), 1e-4);
    }

    @Test
    public void LeftToAzimuthWorksForRandomValues(){
        GeoPoint g1 = new GeoPoint(0.1157327827,0.8119446213);
        GeoPoint g2 = new GeoPoint(0.6566452245,0.9730734179);
        assertEquals(Math.toRadians(257.39), g2.azimuthTo(g1), 1e-4);
    }
    
    @Test
    public void AzimuthWorksForLimitValues(){
        GeoPoint g1 = new GeoPoint(0.1157327827,0.8119446213);
        assertEquals(0, g1.azimuthTo(g1), 0);
    }
    
    @Test
    public void DistanceWorksForLimitValues(){
        GeoPoint g1 = new GeoPoint(0.1157327827,0.8119446213);
        assertEquals(0, g1.distanceTo(g1), 0);
    }
    
    
    
    
    
    
}