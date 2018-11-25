package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Math2;

public class AzimuthTestPL {

    @Test
    public void canonicalize(){
      double expected = Math.PI/2;
      double calculated = Azimuth.canonicalize(5*Math.PI/2);
      assertEquals(expected, calculated, 1e-7);
    }
   
    @Test
    public void canonicalizeTrivial(){
        double expected = 0;
        double calculated = Azimuth.canonicalize(2*Math.PI);
        assertEquals(expected, calculated, 1e-7);
      }

    @Test
    public void floorTest(){
        double expected = 1;
        double calculated = Math2.floorMod(-3, 2);
        assertEquals (expected, calculated, 0);
        
    }
}
