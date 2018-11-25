package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval2DTestEC {

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsErrorOnNonExistingInterval() {
        new Interval1D(75, 56);
    }

    @Test
    public void containsWorks(){
        Interval2D a = new Interval2D(new Interval1D(5,24), new Interval1D(10,20));
        assertTrue(a.contains(5, 20));
    }
    
    @Test
    public void containsWorksforLimitsValidInvalid(){
        int start = 5, end = 24;
        Interval1D inter = new Interval1D(start ,end);
        assertTrue(inter.contains(start));
        assertTrue(inter.contains(end));
        assertTrue(inter.contains((start+end)/2));
        assertFalse(inter.contains(start-2));
    }
    
    @Test
    public void sizeWorks(){
        int expected = 100;
        int actual = new Interval2D(new Interval1D(5,14), new Interval1D(11,20)).size();
        assertEquals(expected, actual);
    }
    
    @Test
    public void sizeOfIntersectionWithWorks(){
        int expected = 4;
        int actual = new Interval2D(new Interval1D(0,10), new Interval1D(0,10)).sizeOfIntersectionWith(new Interval2D(new Interval1D(9,15), new Interval1D(9,20)));
        assertEquals(expected, actual);
    }
    
    @Test
    public void BoundingUnionWorks(){
        Interval2D actual = new Interval2D(new Interval1D(0,10), new Interval1D(0,10)).boundingUnion(new Interval2D(new Interval1D(0,10), new Interval1D(0,10)));
        Interval2D expected = new Interval2D(new Interval1D(0,10), new Interval1D(0,10));
        System.out.println(expected+"  "+actual);
        assertTrue(expected.iX().equals(actual.iX()) && expected.iY().equals(actual.iY()));
    }
    
    @Test
    public void isUnionableWithFalseForTrivialIntersectionLimit(){
        Interval2D a = new Interval2D(new Interval1D(0,1), new Interval1D(0,1));
        Interval2D b = new Interval2D(new Interval1D(1,2), new Interval1D(1,2));
        assertFalse(a.isUnionableWith(b));
    }
    
    @Test
    public void equalsTrue(){
        Interval2D a =  new Interval2D(new Interval1D(0,10), new Interval1D(0,10));
        Interval2D b =  new Interval2D(new Interval1D(0,10), new Interval1D(0,10));
        assertTrue(a.equals(b));
    }    

}
