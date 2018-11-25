package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval1DTestEC {

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsErrorOnNonExistingInterval() {
        new Interval1D(75, 56);
    }

    @Test
    public void includedFromWorks() {
        int expected = 5;
        int actual = new Interval1D(5, 24).includedFrom();
        assertEquals(expected, actual);
    }

    @Test
    public void includedToWorks() {
        int expected = 24;
        int actual = new Interval1D(5, 24).includedTo();
        assertEquals(expected, actual);
    }

    @Test
    public void containsWorksforLimitsValidInvalid() {
        int start = 5, end = 24;
        Interval1D inter = new Interval1D(start, end);
        assertTrue(inter.contains(start));
        assertTrue(inter.contains(end));
        assertTrue(inter.contains((start + end) / 2));
        assertFalse(inter.contains(start - 2));
    }

    @Test
    public void sizeWorks() {
        int expected = 20;
        int actual = new Interval1D(5, 24).size();
        assertEquals(expected, actual);
    }

    @Test
    public void sizeOfIntersectionWithWorks() {
        int expected = 10;
        int actual = new Interval1D(5, 24)
                .sizeOfIntersectionWith(new Interval1D(15, 30));
        assertEquals(expected, actual);
    }

    @Test
    public void BoundingUnionWorks() {
        Interval1D expected = new Interval1D(-5, 500);
        Interval1D actual = new Interval1D(-5, 24)
                .boundingUnion(new Interval1D(400, 500));
        assertEquals(expected.includedFrom(), actual.includedFrom());
        assertEquals(expected.includedTo(), actual.includedTo());
    }

    @Test
    public void BoundingUnionWorksForSameInterval() {
        Interval1D expected = new Interval1D(0, 10);
        Interval1D actual = new Interval1D(0, 10)
                .boundingUnion(new Interval1D(0, 10));
        assertEquals(expected.includedFrom(), actual.includedFrom());
        assertEquals(expected.includedTo(), actual.includedTo());
    }

    @Test
    public void isUnionableWithTrueForEmptyIntersectionLimit() {
        Interval1D a = new Interval1D(0, 1);
        Interval1D b = new Interval1D(2, 3);
        assertTrue(a.isUnionableWith(b));
    }

    @Test
    public void equalsFalseForIntervalLimit() {
        Interval1D a = new Interval1D(0, 1);
        Interval1D b = new Interval1D(2, 3);
        assertFalse(a.equals(b));
    }

}
