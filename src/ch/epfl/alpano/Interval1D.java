package ch.epfl.alpano;

import java.util.Objects;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 *
 * Représente un intervalle d'entiers unidimensionnel.
 */
public final class Interval1D {

    private final int lowestE;
    private final int highestE;

    /**
     * Construit l'intervalle d'entiers allant de includedFrom à includedTo ou
     * lève l'exception IllegalArgumentException si includedTo est strictement
     * inférieur à includedFrom.
     * 
     * @param includedFrom
     * @param includedTo
     * @throws IllegalArgumentException
     */
    public Interval1D(int includedFrom, int includedTo){
        checkArgument(includedFrom <= includedTo);
        lowestE = includedFrom;
        highestE = includedTo;
    }

    /**
     * Retourne le plus petit élément de l'intervalle.
     * 
     * @return le plus petit élément de l'intervalle
     */
    public int includedFrom() {
        return lowestE;
    }

    /**
     * Retourne le plus grand élément de l'intervalle.
     * 
     * @return le plus grand élément de l'intervalle
     */
    public int includedTo() {
        return highestE;
    }

    /**
     * Retourne vrai ssi v appartient à l'intervalle.
     * 
     * @param v
     * @return vrai ssi v appartient à l'intervalle
     */
    public boolean contains(int v) {
        return (v >= lowestE && v <= highestE);
    }

    /**
     * Retourne la taille de l'intervalle, c-à-d le nombre d'éléments qu'il
     * contient.
     * 
     * @return la taille de l'intervalle
     */
    public int size() {
        return (highestE - lowestE + 1);
    }

    /**
     * Retourne la taille de l'intersection du récepteur this et de l'argument
     * that.
     * 
     * @param that
     * @return la taille de l'intersection du récepteur this et de l'argument
     *         that
     */
    public int sizeOfIntersectionWith(Interval1D that) {
    	int intersectionLength = Math.min(this.includedTo(), that.includedTo()) - Math.max(this.includedFrom(),  that.includedFrom())+1;
        return intersectionLength < 0 ? 0 : intersectionLength;
    }

    /**
     * Retourne l'union englobante du récepteur this et de l'argument that.
     * 
     * @param that
     * @return l'union englobante du récepteur this et de l'argument that
     */
    public Interval1D boundingUnion(Interval1D that) {
        return new Interval1D(
                Math.min(that.includedFrom(), this.includedFrom()),
                Math.max(that.includedTo(), this.includedTo()));
    }

    /**
     * Retourne vrai ssi le récepteur this et l'argument that sont unionables.
     * 
     * @param that
     * @return vrai ssi le récepteur this et l'argument that sont unionables.
     */
    public boolean isUnionableWith(Interval1D that) {
        return (this.size() + that.size() - sizeOfIntersectionWith(that) == boundingUnion(that).size());
    }

    /**
     * Retourne l'union du récepteur this et de l'argument that ou lève
     * l'exception IllegalArgumentException s'ils ne sont pas unionables.
     * 
     * @param that
     * @return l'union du récepteur this et de l'argument that
     * @throws IllegalArgumentException
     */
    public Interval1D union(Interval1D that){
        checkArgument(isUnionableWith(that));
        return this.boundingUnion(that);
    }

    /**
     * Retourne vrai ssi thatO est également une instance de Interval1D et
     * l'ensemble de ses éléments est égal à celui du récepteur.
     * 
     * @return vrai ssi thatO est également une instance de Interval1D et
     *         l'ensemble de ses éléments est égal à celui du récepteur
     */
    @Override
    public boolean equals(Object thatO) {
        return (thatO instanceof Interval1D && this.lowestE == ((Interval1D) thatO).lowestE && this.highestE == ((Interval1D) thatO).highestE);
    }

    /**
     * Retourne la valeur de hachage à la méthode statique hash de la classe
     * Objects à laquelle on passe includedFrom() et includedTo().
     * 
     * @return valeur de hachage
     */
    @Override
    public int hashCode() {
        return Objects.hash(includedFrom(), includedTo());
    }

    /**
     * Retourne une chaîne représentant l'intervalle.
     * 
     * @return la chaîne représentant l'intervalle
     */
    @Override
    public String toString() {
        return ("[" + lowestE + ".." + highestE + "]");
    }

}