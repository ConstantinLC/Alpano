package ch.epfl.alpano;
import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.Interval1D;

public class Interval1DTestPL {

    @Test
    public void isUnionableWithDisjoined(){
         Interval1D inter1 = new Interval1D(0, 2);
         Interval1D inter2 = new Interval1D(4, 8);
         assertFalse(inter1.isUnionableWith(inter2));
     }
     
     @Test(expected = IllegalArgumentException.class)
     public void unionThrowIllegalArgumentExcepion(){
         Interval1D inter1 = new Interval1D(0, 2);
         Interval1D inter2 = new Interval1D(4, 8);
         inter1.union(inter2);
     }
     
     @Test
     public void unionWithIntersectionEquals(){
         Interval1D inter1 = new Interval1D(0, 5);
         Interval1D inter2 = new Interval1D(4, 8);
         Interval1D union = inter1.union(inter2);
         Interval1D expected = new Interval1D(0,8);
         System.out.println(union);
         assertTrue(union.equals(expected));
         
     }
     
     @Test
     public void sizeOfIntersectionWith1Element(){
         Interval1D inter1 = new Interval1D(0, 2);
         Interval1D inter2 = new Interval1D(2, 4);
         int expected = 1;
         int calculated = inter2.sizeOfIntersectionWith(inter1);
         System.out.println(expected);
         System.out.println(calculated);
         
         assertEquals (expected, calculated, 0);
     }

}
