package ch.epfl.alpano;
import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public class Interval2DTestPL {

    private static Interval1D iX = new Interval1D(1,3);
    private static Interval1D iY = new Interval1D(2,4);
    
    private static Interval2D inter = new Interval2D(iX,iY);
            
    @Test
    public void iXTest(){
       Interval1D iXTest = inter.iX();
       assertTrue(iXTest.equals(iX));
    }
    
    @Test
    public void iYTest(){
       Interval1D iYTest = inter.iY();
       assertTrue(iYTest.equals(iY));
    }
    
    @Test
    public void contains2DTest(){
        Interval1D inter1 = new Interval1D(0, 2);
        Interval1D inter2 = new Interval1D(1, 4);
        
        Interval2D inter21 = new Interval2D(inter1, inter2);
        
        assertTrue(inter21.contains(1, 1));
        assertFalse(inter21.contains(10, 1));
        assertFalse(inter21.contains(1, 10));
        assertFalse(inter21.contains(10, 10)); 
    }
    
    @Test
    public void sizeTest(){
        Interval1D inter1 = new Interval1D(0,2);
        Interval1D inter2 = new Interval1D(2,3);
        Interval2D inter = new Interval2D(inter1, inter2);
        int expected = 6;
        int calculated = inter.size();
        assertEquals(calculated,expected,0);
    }
    
    @Test
    public void sizeOfIntersect(){
        Interval1D inter1 = new Interval1D(1, 2);
        Interval1D inter2 = new Interval1D(4, 6);
        
        Interval2D inter21 = new Interval2D(inter1, inter2);
        
        Interval1D inter3 = new Interval1D(1, 2);
        Interval1D inter4 = new Interval1D(6, 7);
        
        Interval2D inter22 = new Interval2D(inter3, inter4);
        
        int expected = 2;
        int actual = inter21.sizeOfIntersectionWith(inter22);
        
        assertEquals(expected, actual);
    }
    @Test
    public void stringTest(){
        Interval1D inter1 = new Interval1D(1, 2);
        Interval1D inter2 = new Interval1D(4, 6);
        
        Interval2D inter21 = new Interval2D(inter1, inter2);
        System.out.println(inter1);
        System.out.println(inter21);
    }
    
    @Test
    public void equalsTest(){
        Interval1D inter1 = new Interval1D(0, 2);
        Interval1D inter2 = new Interval1D(2, 4);
        
        Interval2D inter21 = new Interval2D(inter1, inter2);
        
        Interval1D inter3 = new Interval1D(0, 2);
        Interval1D inter4 = new Interval1D(2, 4);
        
        Interval2D inter22 = new Interval2D(inter3, inter4);
        
        assertTrue(inter21.equals(inter22));
        
        
    }
    @Test
    public void boundedUnion(){
        Interval1D inter1 = new Interval1D(0, 2);
        Interval1D inter2 = new Interval1D(4, 6);
        
        Interval2D inter21 = new Interval2D(inter1, inter2);
        
        Interval1D inter5 = new Interval1D(0, 0);
        Interval1D inter6 = new Interval1D(4, 4);
        
        Interval2D inter23 = new Interval2D(inter5, inter6);
        
        Interval1D inter3 = new Interval1D(2, 4);
        Interval1D inter4 = new Interval1D(6, 8);
        
        Interval2D inter22 = new Interval2D(inter3, inter4);
        
        Interval2D expected1 = new Interval2D(new Interval1D(0, 4), new Interval1D(4, 8));
        
        assertTrue(inter21.boundingUnion(inter22).equals(expected1));//1 point dintersection
        
        assertTrue(inter22.boundingUnion(inter22).equals(inter22));
        System.out.println(inter21.boundingUnion(inter23));
        System.out.println(inter21);
        assertTrue(inter21.boundingUnion(inter23).equals(inter21));
       
        
    }
   
    @Test
    public void unionTest(){
        Interval1D inter1 = new Interval1D(0, 2);
        Interval1D inter2 = new Interval1D(4, 6);
        
        Interval2D inter21 = new Interval2D(inter1, inter2);
        
        Interval1D inter5 = new Interval1D(0, 0);
        Interval1D inter6 = new Interval1D(4, 4);
        
        Interval2D inter23 = new Interval2D(inter5, inter6);
        
        Interval1D inter3 = new Interval1D(2, 4);
        Interval1D inter4 = new Interval1D(5, 8);
        
        Interval2D inter22 = new Interval2D(inter3, inter4);
        
        //Interval2D expected1 = new Interval2D(new Interval1D(0, 4), new Interval1D(4, 8));
        
        
        
        assertTrue(inter22.union(inter22).equals(inter22));
        System.out.println(inter21.union(inter23));
        System.out.println(inter21);
        assertTrue(inter21.union(inter23).equals(inter21));
       
        
    }
  
    
    @Test
    public void isUnionableTestWithoutTouching(){ // Cas de 2 carrés qui ne se touchent pas       ____
                                    // mais qui n'ont pas d'intersection entre eux  /____/
        Interval1D inter1 = new Interval1D(0, 1);
        Interval1D inter2 = new Interval1D(0, 1);
        
        Interval2D inter21 = new Interval2D(inter1, inter2);
        
        Interval1D inter3 = new Interval1D(0, 1);
        Interval1D inter4 = new Interval1D(2, 3);
        
        Interval2D inter22 = new Interval2D(inter3, inter4);
        
        System.out.println(); inter21.union(inter22);
        assertTrue(inter21.isUnionableWith(inter22));
    }
    
    @Test
    public void isUnionableTestTouching(){
        Interval1D inter1 = new Interval1D(0, 1);
        Interval1D inter2 = new Interval1D(0, 1);
        
        Interval2D inter21 = new Interval2D(inter1, inter2);
        
        Interval1D inter3 = new Interval1D(0, 1);
        Interval1D inter4 = new Interval1D(1, 2);
        
        Interval2D inter22 = new Interval2D(inter3, inter4);
        assertTrue(inter21.isUnionableWith(inter22));
        System.out.println(inter21.union(inter22));
    
    }

}
