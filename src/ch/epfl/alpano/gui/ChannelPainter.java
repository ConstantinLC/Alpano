package ch.epfl.alpano.gui;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 * Représente un peintre de canal.
 */
@FunctionalInterface
public interface ChannelPainter {

    public float valueAt(int x, int y);

    /**
     * Retourne un peintre de canal dont la valeur pour un point est la 
     * différence de distance entre le plus lointain des voisins et le 
     * point en question. Pour les points situés dans les bords, dont 
     * certains voisins sont hors du panorama, on considère que la 
     * distance à l'observateur de ces voisins est nulle.
     * @param panorama
     * @return un peintre de canal dont la valeur pour un point est la 
     * différence de distance entre le plus lointain des voisins et le 
     * point en question
     */
    public static ChannelPainter maxDistanceToNeighbors(Panorama panorama){
        return (x, y) ->{
            float max1 = Math.max(panorama.distanceAt(x-1, y, 0), panorama.distanceAt(x + 1, y, 0));
            float max2 = Math.max(panorama.distanceAt(x, y - 1, 0), panorama.distanceAt(x, y + 1, 0));
            float max = Math.max(max1, max2);
            return max - panorama.distanceAt(x, y); 
        };
    }

    /**
     * Permet d'ajouter la valeur produite par le peintre de canal par 
     * une constante passée en argument.
     * @param a
     * @return un nouveau peintre de canal qui ajoute par une constante la 
     * valeur produite par le peintre de canal
     */
    public default ChannelPainter add(float a){
        return (x, y) -> valueAt(x, y) + a;
    }

    /**
     * Permet de soustraire la valeur produite par le peintre de canal par 
     * une constante passée en argument.
     * @param a
     * @return un nouveau peintre de canal qui soustrait par une constante la 
     * valeur produite par le peintre de canal
     */
    public default ChannelPainter sub(float a){
        return (x, y) -> valueAt(x,y) - a;
    }

    /**
     * Permet de multiplier la valeur produite par le peintre de canal par 
     * une constante passée en argument.
     * @param a
     * @return un nouveau peintre de canal qui multiplie par une constante la 
     * valeur produite par le peintre de canal
     */
    public default ChannelPainter mul(float a){
        return (x, y) -> valueAt(x,y)*a;
    }

    /**
     * Permet de diviser la valeur produite par le peintre de canal par 
     * une constante passée en argument.
     * @param a
     * @return un nouveau peintre de canal qui divise par une constante la 
     * valeur produite par le peintre de canal
     */
    public default ChannelPainter div(float a){
        return (x,y) -> valueAt(x,y)/a;
    }

    /**
     * Permet d'appliquer à la valeur produite par le peintre un 
     * opérateur unaire (de type DoubleUnaryOperator) passé en argument 
     * à la méthode.
     * @param f
     * @return un nouveau peintre de canal qui applique un opérateur 
     * unaire à la valuer produite par le peintre de canal
     */
    public default ChannelPainter map(DoubleUnaryOperator f){
        return (x,y) -> (float)f.applyAsDouble(valueAt(x,y));
    }

    public default ChannelPainter inverted(){
        return (x,y) -> 1 - valueAt(x, y);
    }

    public default ChannelPainter clamped(){
        return (x,y) -> Math.max(0, Math.min(valueAt(x,y), 1));
    }

    public default ChannelPainter cycling(){
        return (x,y) -> (float)Math2.floorMod(valueAt(x,y), 1);
    }


}