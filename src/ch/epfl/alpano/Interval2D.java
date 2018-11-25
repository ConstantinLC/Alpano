package ch.epfl.alpano;

import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Preconditions.checkArgument;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 *
 *         Représente un intervalle d'entiers bidimensionnel.
 */
public final class Interval2D {

	private final Interval1D iX;
	private final Interval1D iY;

	/**
	 * Construit le produit cartésien des intervalles iX et iY, ou lève
	 * l'exception NullPointerException si l'un ou l'autre de ces intervalles
	 * est null.
	 * 
	 * @param iX
	 * @param iY
	 * @throws NullPointerException
	 */
	public Interval2D(Interval1D iX, Interval1D iY) {
		this.iX = requireNonNull(iX);
		this.iY = requireNonNull(iY);
	}

	/**
	 * Retourne le premier intervalle du produit cartésien.
	 * 
	 * @return le premier intervalle du produit cartésien
	 */
	public Interval1D iX() {
		return iX;
	}

	/**
	 * Retourne le second intervalle du produit cartésien.
	 * 
	 * @return le second intervalle du produit cartésien
	 */
	public Interval1D iY() {
		return iY;
	}

	/**
	 * Retourne vrai ssi l'intervalle contient la paire (x, y).
	 * 
	 * @param x
	 * @param y
	 * @return vrai ssi l'intervalle contient la paire (x, y)
	 */
	public boolean contains(int x, int y) {
		return (iX.contains(x) && iY.contains(y));
	}

	/**
	 * Qui retourne la taille de l'intervalle, c-à-d le nombre d'éléments qu'il
	 * contient.
	 * 
	 * @return la taille de l'intervalle
	 */
	public int size() {
		return iX.size() * iY.size();
	}

	/**
	 * Retourne la taille de l'intersection du récepteur this avec l'argument
	 * that.
	 * 
	 * @param that
	 * @return la taille de l'intersection
	 */
	public int sizeOfIntersectionWith(Interval2D that) {
		int xIntersection = this.iX().sizeOfIntersectionWith(that.iX);
		int yIntersection = this.iY().sizeOfIntersectionWith(that.iY);
		return xIntersection*yIntersection;
	}

	/**
	 * Retourne l'union englobante du récepteur this et de l'argument that.
	 * 
	 * @param that
	 * @return l'union englobante
	 */
	public Interval2D boundingUnion(Interval2D that) {
		Interval1D unionX = this.iX().boundingUnion(that.iX());
		Interval1D unionY = this.iY().boundingUnion(that.iY());
		return new Interval2D(unionX, unionY);
	}

	/**
	 * Retourne vrai ssi le récepteur this et l'argument that sont unionables.
	 * 
	 * @param that
	 * @return vrai ssi le récepteur this et l'argument that sont unionables
	 */
	public boolean isUnionableWith(Interval2D that) {
		return (this.size() + that.size() - sizeOfIntersectionWith(that) == this.boundingUnion(that).size());
	}

	/**
	 * Qui retourne l'union du récepteur et de that ou lève l'exception
	 * IllegalArgumentException s'ils ne sont pas unionables.
	 * 
	 * @param that
	 * @return l'union du récepteur et de that
	 * @throws IllegalArgumentException
	 */
	public Interval2D union(Interval2D that) {
		checkArgument(isUnionableWith(that));
		return this.boundingUnion(that);
	}

	/**
	 * Redéfinit la méthode equals héritée de Object et retourne vrai ssi thatO
	 * est également une instance de Interval2D et l'ensemble de ses éléments
	 * est égal à celui du récepteur.
	 * 
	 * @param thatO
	 * @return vrai ssi thatO est également une instance de Interval2D et
	 *         l'ensemble de ses éléments est égal à celui du récepteur.
	 */
	@Override
	public boolean equals(Object thatO) {
		return (thatO instanceof Interval2D && this.iX().equals(((Interval2D)thatO).iX()) && this.iY().equals(((Interval2D)thatO).iY()));
	}

	/**
	 * Retourne la valeur de hachage à la méthode statique hash de la classe
	 * Objects à laquelle on passe iX() et iY().
	 * 
	 * @return valeur de hachage
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.iX(), this.iY());
	}

	/**
	 * Redéfinit la méthode toString héritée de Object et qui retourne une
	 * chaîne composée de la représentation textuelle des deux intervalles (dans
	 * l'ordre) séparés par la lettre x.
	 * 
	 * @return chaîne représentant l'intervalle 2D
	 */
	@Override
	public String toString() {
		return (iX.toString() + "x" + iY.toString());
	}

}