package ch.epfl.alpano;

import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * 
 * @author Eric Wilders (276016)
 * @author Constantin Le Cleï (272849)
 * 
 *         Représente les paramètres nécessaires au dessin d'un panorama.
 */
public final class PanoramaParameters {

	private final GeoPoint observerPosition;
	private final int observerElevation;
	private final double centerAzimuth;
	private final double horizontalFieldOfView;
	private final double verticalFieldOfView;
	private final int maxDistance;
	private final int width;
	private final int trueWidth;
	private final int height;
	private final int trueHeight;
	

	/**
	 * Construit un objet contenant les paramètres passés en argument et qui
	 * correspondent à ceux énumérés à la section 1.2 ; lève l'exception
	 * NullPointerException si la position de l'observateur est nulle, ou
	 * l'exception IllegalArgumentException si l'azimut central n'est pas
	 * canonique, si l'angle de vue horizontal n'est pas compris entre 0 (exclu)
	 * et 2π2π (inclu) ou si la largeur, hauteur et distance maximales ne sont
	 * pas strictement positives.
	 * 
	 * @param observerPosition
	 * @param observerElevation
	 * @param centerAzimuth
	 * @param horizontalFieldOfView
	 * @param maxDistance
	 * @param width
	 * @param height
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth,
			double horizontalFieldOfView, int maxDistance, int width, int height) {

		checkArgument(
				Azimuth.isCanonical(centerAzimuth) && (horizontalFieldOfView > 0 && horizontalFieldOfView <= Math2.PI2)
						&& (maxDistance > 0 && width > 0 && height > 0));

		this.observerPosition = requireNonNull(observerPosition);
		this.observerElevation = observerElevation;
		this.centerAzimuth = centerAzimuth;
		this.horizontalFieldOfView = horizontalFieldOfView;
		this.maxDistance = maxDistance;
		this.width = width;
		this.trueWidth = width - 1;
		this.height = height;
		this.trueHeight = height - 1;
		this.verticalFieldOfView = (height - 1) * horizontalFieldOfView / (width - 1);
	}

	/**
	 * Retourne la hauteur du panorama.
	 * 
	 * @return la hauteur du panorama
	 */
	public int height() {
		return height;
	}

	/**
	 * Retourne l'azimuth au centre du panorama.
	 * 
	 * @return l'azimuth au centre du panorama
	 */
	public double centerAzimuth() {
		return centerAzimuth;
	}

	/**
	 * Retourne l'angle de vue horizontal.
	 * 
	 * @return l'angle de vue horizontal
	 */
	public double horizontalFieldOfView() {
		return horizontalFieldOfView;
	}

	/**
	 * Retourne la largeur du panorama.
	 * 
	 * @return la largeur du panorama
	 */
	public int width() {
		return width;
	}

	/**
	 * Retourne l'élévation de l'observateur.
	 * 
	 * @return l'élévation de l'observateur
	 */
	public int observerElevation() {
		return observerElevation;
	}

	/**
	 * Retourne la distance maximale du panorama.
	 * 
	 * @return la distance maximale du panorama
	 */
	public int maxDistance() {
		return maxDistance;
	}

	/**
	 * Retourne la position de l'observateur.
	 * 
	 * @return la position de l'observateur
	 */
	public GeoPoint observerPosition() {
		return observerPosition;
	}

	/**
	 * Retourne l'angle de vue vertical.
	 * 
	 * @return l'angle de vue vertical
	 */
	public double verticalFieldOfView() {
		return verticalFieldOfView;
	}

	/**
	 * Retourne l'azimut canonique correspondant à l'index de pixel horizontal
	 * x, ou lève l'exception IllegalArgumentException si celui-ci est inférieur
	 * à zéro, ou supérieur à la largeur moins un.
	 * 
	 * @param x
	 * @return l'azimut canonique correspondant à l'index de pixel horizontal x
	 * @throws IllegalArgumentException
	 */
	public double azimuthForX(double x) {
		checkArgument(x >= 0 && x <= trueWidth);
		return canonicalize(centerAzimuth + (x - trueWidth / 2d) * horizontalFieldOfView() / trueWidth);
	}

	/**
	 * Retourne l'index de pixel horizontal correspondant à l'azimut donné, ou
	 * lève l'exception IllegalArgumentException si cet azimut n'appartient pas
	 * à la zone visible.
	 * 
	 * @param a
	 * @return l'index de pixel horizontal correspondant à l'azimut donné
	 * @throws IllegalArgumentException
	 */
	public double xForAzimuth(double a) {
		checkArgument(a <= centerAzimuth + horizontalFieldOfView() / 2 && a >= centerAzimuth - horizontalFieldOfView() / 2);
		
		return ((a - centerAzimuth) * trueWidth / horizontalFieldOfView() + trueWidth / 2d);
	}

	/**
	 * retourne l'élévation correspondant à l'index de pixel vertical y, ou lève
	 * l'exception IllegalArgumentException si celui-ci est inférieur à zéro, ou
	 * supérieur à la hauteur moins un.
	 * 
	 * @param y
	 * @return l'élévation correspondant à l'index de pixel vertical y
	 * @throws IllegalArgumentException
	 */
	public double altitudeForY(double y) {
		checkArgument(y >= 0 && y <= trueHeight);
		return (trueHeight / 2d - y) / trueHeight * verticalFieldOfView();
	}

	/**
	 * Retourne l'index de pixel vertical correspondant à l'élévation donnée, ou
	 * lève l'exception IllegalArgumentException si celle-ci n'appartient pas à
	 * la zone visible.
	 * 
	 * @param a
	 * @return l'index de pixel vertical correspondant à l'élévation donnée
	 * @throws IllegalArgumentException
	 */
	public double yForAltitude(double a) {
		checkArgument(a <= verticalFieldOfView() / 2 && a >= -verticalFieldOfView() / 2);
		return (-a * trueHeight / verticalFieldOfView() + trueHeight / 2d);
	}

	/**
	 * Retourne vrai si et seulement si l'index passé est un index de pixel
	 * valide.
	 * 
	 * @param x
	 * @param y
	 * @return vrai ssi l'index passé est un index de pixel valide
	 */
	boolean isValidSampleIndex(int x, int y) {
		return (x <= trueWidth && x >= 0 && y >= 0 && y <= trueHeight);
	}

	/**
	 * Retourne l'index linéaire du pixel d'index donné.
	 * 
	 * @param x
	 * @param y
	 * @return l'index linéaire du pixel d'index donné
	 */
	int linearSampleIndex(int x, int y) {
		return (y * width + x);
	}

}